package com.kg.platform.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.kg.framework.utils.StringUtils;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.dao.entity.WeixinJsapiTicket;
import com.kg.platform.dao.write.WeixinJsapiTicketWMapper;
import com.kg.platform.http.Method;
import com.kg.platform.http.Request;
import com.kg.platform.http.WebUtils;
import com.kg.platform.service.WeixinService;
import com.kg.platform.weixin.AccessToken;
import com.kg.platform.weixin.HttpClientUtil;
import com.kg.platform.weixin.JsapiTicket;
import com.kg.platform.weixin.WeChatConstant;
import com.kg.platform.weixin.WeChatUtils;

@Service
public class WeixinServiceImpl implements WeixinService {

    @Autowired
    private WeixinJsapiTicketWMapper weixinJsapiTicketWMapper;

    @Autowired
    protected JedisUtils jedisUtils;

    public String getJsapiTicket() throws Exception {
        String ticket = "";
        List<WeixinJsapiTicket> weixinJsapiTicketList = weixinJsapiTicketWMapper.selectAll();
        if (null != weixinJsapiTicketList && weixinJsapiTicketList.size() > 0) {
            WeixinJsapiTicket weixinJsapiTicket = weixinJsapiTicketList.get(0);
            Long expireTime = weixinJsapiTicket.getExpire();
            // 当前时间
            long nowTime = WeChatUtils.getTimeStamp();
            // 超期
            String json = "";
            JsapiTicket jsapiTicket;
            if (nowTime > expireTime) {
                json = HttpClientUtil.httpGet(WeChatConstant.JSAPI_TICKET.replace("ACCESS_TOKEN", getAccessTokenStr()));
                jsapiTicket = JSON.parseObject(json, JsapiTicket.class);
                if (null == jsapiTicket.getTicket()) {// accesstoken过期
                    // 重新获取token
                    this.getAccessToken();
                    json = HttpClientUtil.httpGet(WeChatConstant.JSAPI_TICKET.replace("ACCESS_TOKEN",
                            getAccessTokenStr()));
                    jsapiTicket = JSON.parseObject(json, JsapiTicket.class);
                }
                weixinJsapiTicket.setTicket(jsapiTicket.getTicket());
                // 7200秒
                weixinJsapiTicket.setExpire(WeChatUtils.getTimeStamp() + 7200);
                weixinJsapiTicketWMapper.update(weixinJsapiTicket);
                ticket = weixinJsapiTicket.getTicket();
            } else {
                ticket = weixinJsapiTicket.getTicket();
            }
        } else {
            // 第一次访问
            String json = HttpClientUtil.httpGet(WeChatConstant.JSAPI_TICKET.replace("ACCESS_TOKEN",
                    getAccessTokenStr()));
            JsapiTicket jsapiTicket = JSON.parseObject(json, JsapiTicket.class);
            // 第一次存入数据库
            WeixinJsapiTicket weixinJsapiTicket = new WeixinJsapiTicket();
            weixinJsapiTicket.setTicket(jsapiTicket.getTicket());
            weixinJsapiTicket.setExpire(WeChatUtils.getTimeStamp() + 7200);
            weixinJsapiTicketWMapper.insert(weixinJsapiTicket);
            ticket = weixinJsapiTicket.getTicket();
        }
        return ticket;
    }

    public AccessToken getAccessToken() throws Exception {
        Request request = Request
                .options()
                .setUrl("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                        + WeChatConstant.APP_ID + "&secret=" + WeChatConstant.APP_SECRET).setMethod(Method.GET).build();
        String json = WebUtils.executeHttp(request).getJsonString();
        System.out.println("accesstoken======" + json);
        AccessToken accessToken = JSON.parseObject(json, AccessToken.class);
        if (null != accessToken.getAccessToken()) {
            jedisUtils.set("weixin_access_token", accessToken.getAccessToken(), 3600);
        }
        return accessToken;
    }

    /**
     * 从字典表获得access_token
     *
     * @return
     * @throws Exception
     */
    public String getAccessTokenStr() throws Exception {
        String value = jedisUtils.get("weixin_access_token");
        if (StringUtils.isBlank(value)) {
            AccessToken accessToken = getAccessToken();
            value = accessToken.getAccessToken();
        }
        return StringUtils.isEmpty(value) ? "" : value;
    }

}
