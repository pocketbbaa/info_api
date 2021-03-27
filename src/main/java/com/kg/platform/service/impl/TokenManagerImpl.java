package com.kg.platform.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kg.framework.toolkit.Check;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.utils.CheckUtils;
import com.kg.platform.common.utils.JedisKey;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.model.TokenModel;
import com.kg.platform.model.response.SiteinfoResponse;
import com.kg.platform.service.SiteinfoService;
import com.kg.platform.service.TokenManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.UUID;

@Component
public class TokenManagerImpl implements TokenManager {
    private static final Logger logger = LoggerFactory.getLogger(TokenManagerImpl.class);

    @Autowired
    protected JedisUtils jedisUtils;

    @Inject
    private SiteinfoService siteinfoService;

    @Override
    public TokenModel createToken(Long userId) {
        // 使用uuid作为源token
        String token = UUID.randomUUID().toString().replace("-", "");
        TokenModel model = new TokenModel(userId, token);
        // 存储到redis并设置过期时间

        logger.info("为用户 {} 设置token:{}", userId, token);

        Integer login_time = getLoginTime();
        if (login_time != null) {
            this.jedisUtils.set(JedisKey.userTokenKey(userId), token, JedisKey.ONE_HOUR * login_time.intValue());
        } else {
            this.jedisUtils.set(JedisKey.userTokenKey(userId), token, JedisKey.ONE_WEEK);
        }

        return model;
    }

    @Override
    public TokenModel createAppToken(Long userId) {
        String appkey = JedisKey.userTokenKey(userId) + "APP";
        String token = UUID.randomUUID().toString().replace("-", "");
        TokenModel model = new TokenModel(userId, token);
        logger.info("为用户 {} 设置Apptoken:{}", userId, token);
        Integer login_time = getLoginTime();
        if (login_time != null) {
            this.jedisUtils.set(appkey, token, JedisKey.ONE_HOUR * login_time);
        } else {
            this.jedisUtils.set(appkey, token, JedisKey.ONE_WEEK);
        }

        return model;
    }

    @Override
    public TokenModel getToken(String authentication) {
        // logger.info("前端传入的token:{}", authentication);

        if (authentication == null || authentication.length() == 0) {
            return null;
        }
        String[] param = authentication.split("_");
        if (param.length != 2) {
            return null;
        }
        if (StringUtils.isAnyBlank(param[0], param[1]) || param[0].equals("null") || param[1].equals("null")) {
            return null;
        }
        // 使用userId和源token简单拼接成的token，可以增加加密措施
        long userId = Long.parseLong(param[0]);
        String token = param[1];
        return new TokenModel(userId, token);
    }

    @Override
    public String getTokenByUserId(Long userId) {
        return jedisUtils.get(JedisKey.userTokenKey(userId) + "APP");
    }

    @Override
    public boolean checkToken(TokenModel model) {
        // logger.info("前端传入的TokenModel:{}", JSON.toJSONString(model));

        if (model == null) {
            return false;
        }
        String token = this.jedisUtils.get(JedisKey.userTokenKey(model.getUserId()));
        if (token == null || !token.equals(model.getToken())) {
            return false;
        }
        // 如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间

        Integer login_time = getLoginTime();
        if (login_time != null) {
            this.jedisUtils.expire(JedisKey.userTokenKey(model.getUserId()), JedisKey.ONE_HOUR * login_time.intValue());
        } else {
            this.jedisUtils.expire(JedisKey.userTokenKey(model.getUserId()), JedisKey.ONE_WEEK);
        }

        return true;
    }

    @Override
    public void deleteToken(Long userId) {
        jedisUtils.del(JedisKey.userTokenKey(userId));
    }

    @Override
    public void deleteAppToken(Long userId) {
        String appkey = JedisKey.userTokenKey(userId) + "APP";
        jedisUtils.del(appkey);
    }

    @Override
    public boolean checkAppToken(TokenModel model) {
        logger.info("【校验APPtoken】前端传入的TokenModel:{}", JSONObject.toJSONString(model));
        if (model == null) {
            return false;
        }
        String appkey = JedisKey.userTokenKey(model.getUserId()) + "APP";
        String token = this.jedisUtils.get(appkey);
        if (token == null || !token.equals(model.getToken())) {
            return false;
        }
        // 如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
        Integer login_time = getLoginTime();
        if (login_time != null) {
            this.jedisUtils.expire(appkey, JedisKey.ONE_HOUR * login_time);
        } else {
            this.jedisUtils.expire(appkey, JedisKey.ONE_WEEK);
        }

        return true;
    }

    private Integer getLoginTime() {

        Result<SiteinfoResponse> siteinfoResponse = siteinfoService.selectSiteInfo();
        CheckUtils.checkRetInfo(siteinfoResponse, true);
        Integer login_time = null;

        SiteinfoResponse siteInfo = siteinfoResponse.getData();
        if (!Check.NuNObject(siteInfo.getLoginTime()) && !Check.NuNObject(siteInfo.getLoginTimeUnit())) {

            // logger.info("===getLoginTime:{}", siteInfo.getLoginTime());
            // logger.info("===getLoginTileUnit:{}",
            // siteInfo.getLoginTimeUnit());
            login_time = Integer.parseInt(siteInfo.getLoginTime());
            Integer login_time_unit = siteInfo.getLoginTimeUnit();

            if (login_time_unit.intValue() == 2) {
                login_time = login_time * 24;
            }
        }

        return login_time;
    }

}
