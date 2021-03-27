package com.kg.platform.service.impl;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kg.framework.toolkit.Check;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.utils.JedisKey;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.dao.read.SiteinfoRMapper;
import com.kg.platform.model.out.SiteinfoOutModel;
import com.kg.platform.model.response.SiteinfoResponse;
import com.kg.platform.service.SiteinfoService;

@Service
public class SiteinfoServiceImpl implements SiteinfoService {
    private static final Logger logger = LoggerFactory.getLogger(SiteinfoServiceImpl.class);

    @Inject
    SiteinfoRMapper siteinfoRMapper;

    @Autowired
    protected JedisUtils jedisUtils;

    /**
     * 首页TDK
     */
    @Override
    public Result<SiteinfoResponse> selectSiteInfo() {

//        SiteinfoOutModel model = jedisUtils.get(JedisKey.siteInfoKey(), SiteinfoOutModel.class);

//        if (model == null) {
//
//
//            this.jedisUtils.set(JedisKey.siteInfoKey(), model, JedisKey.THREE_MINUTE);
//        }
        SiteinfoOutModel model = siteinfoRMapper.selectSiteinfo();

        // logger.info("查询出的站点信息：{}", JSON.toJSONString(model));
        SiteinfoResponse response = new SiteinfoResponse();

        if (!Check.NuNObject(model)) {
            response.setSiteId(model.getSiteId());
            response.setSiteLogo(model.getSiteLogo());
            response.setSiteIcon(model.getSiteIcon());
            response.setSiteTitle(model.getSiteTitle());
            response.setSiteDesc(model.getSiteDesc());
            response.setSiteKeyword(model.getSiteKeyword());
            response.setLimitIp(model.getLimitIp());
            response.setLoginTime(model.getLoginTime());
            response.setLoginTimeUnit(model.getLoginTimeUnit());
            response.setCommentAudit(model.getCommentAudit());

        }

        return new Result<SiteinfoResponse>(response);

    }

}
