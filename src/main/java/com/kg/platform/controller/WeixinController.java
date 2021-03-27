package com.kg.platform.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.model.request.admin.WeixinRequest;
import com.kg.platform.service.WeixinService;
import com.kg.platform.weixin.WeChatConstant;
import com.kg.platform.weixin.WeChatUtils;

@RestController
@RequestMapping("weixin")
public class WeixinController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(WeixinController.class);

    @Autowired
    private WeixinService weixinService;

    @RequestMapping(value = "weixinConfig", method = RequestMethod.POST)
    @BaseControllerNote(needCheckToken = false, isLogin = false, beanClazz = WeixinRequest.class)
    public JsonEntity weixinConfig(@RequestAttribute WeixinRequest request) throws Exception {
        String appId = WeChatConstant.APP_ID;
        long timestamp = WeChatUtils.getTimeStamp();
        String nonceStr = WeChatUtils.getRandomStr(20);
        Map<String, String> map = new HashMap<String, String>();
        map.put("appId", appId);
        map.put("timestamp", String.valueOf(timestamp));// 当前时间戳)
        map.put("nonceStr", nonceStr);// 不长于32位的随机字符串);
        String ticket = weixinService.getJsapiTicket();
        SortedMap<String, Object> signMap = Maps.newTreeMap();// 自然升序map
        signMap.put("noncestr", nonceStr);
        signMap.put("jsapi_ticket", ticket);
        signMap.put("timestamp", timestamp);
        signMap.put("url", request.getUrl());
        map.put("signature", WeChatUtils.getSignBySHA1(signMap));
        logger.info("signature-----------------" + map.get("signature"));
        return JsonEntity.makeSuccessJsonEntity(map);
    }
}
