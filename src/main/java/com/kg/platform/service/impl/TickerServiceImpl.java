package com.kg.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.utils.JedisKey;
import com.kg.platform.common.utils.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.utils.HttpUtil;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.model.request.TickerRequest;
import com.kg.platform.model.response.TickerResponse;
import com.kg.platform.service.TickerService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/19.
 */
@Service
public class TickerServiceImpl implements TickerService {
    @Autowired
    private JedisUtils jedisUtils;
    /**
     * 获取各币种行情数据（指数、涨幅等）
     * 
     * @return
     */
    @Override
    public Map<String,Object> getTicker() {
        Map map = new HashMap();
        TickerResponse[] ticker = jedisUtils.get(JedisKey.getPriceKey("coinTicker"),TickerResponse[].class);
        map.put("coinTicker",ticker);
        Map stock = jedisUtils.get(JedisKey.getPriceKey("stock"),Map.class);
        map.put("stock",stock);
        return map;
    }
}
