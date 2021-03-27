package com.kg.platform.service;

import com.kg.platform.model.response.TickerResponse;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/19.
 */
public interface TickerService {
    /**
     * 获取各币种行情数据（指数、涨幅等）
     * @return
     */
    Map<String,Object> getTicker();
}
