package com.kg.platform.controller.dubbo;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.controller.BaseTest;
import com.kg.search.dto.BaseResult;
import com.kg.search.dubboservice.BuryingpointDubboService;
import com.kg.search.enums.BuryingPointEnum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DubboServiceTest extends BaseTest {


    @Autowired
    private BuryingpointDubboService buryingpointDubboService;


    @Test
    public void test1() {
        BuryingPointEnum buryingPointEnum = BuryingPointEnum.getByCode(1);
        BaseResult result = buryingpointDubboService.queryBuryingPointData(buryingPointEnum, "", "", 1, 20);
        System.out.println(JSONObject.toJSONString(result));
    }

}
