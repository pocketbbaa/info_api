package com.kg.platform.controller;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.model.response.TickerResponse;
import com.kg.platform.service.TickerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/19.
 */
@Controller
@RequestMapping("ticker")
public class TickerController {
    @Inject
    private TickerService tickerService;

    /**
     * 获取各币种行情数据（指数、涨幅等）
     * @return
     */
    @ResponseBody
    @RequestMapping("getTicker")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = false,isLogin = true)
    public JsonEntity getTicker(){
        return JsonEntity.makeSuccessJsonEntity(tickerService.getTicker());
    }
}
