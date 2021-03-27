package com.kg.platform.service;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.entity.MJsonEntity;
import com.kg.platform.controller.BaseTest;
import com.kg.platform.model.request.CloudOrderRequest;
import com.kg.platform.service.m.CloudOrderService;
import com.kg.platform.service.m.CloudPackageService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CloudPackageServiceTest extends BaseTest {

    @Autowired
    private CloudPackageService cloudPackageService;

    @Autowired
    private CloudOrderService cloudOrderService;

    @Test
    public void getPackageList() {
        MJsonEntity mJsonEntity = cloudPackageService.getPackageList();
        System.err.println(JSONObject.toJSONString(mJsonEntity));
    }

    @Test
    public void submitOrder() {
        CloudOrderRequest cloudOrderRequest = new CloudOrderRequest();
        cloudOrderRequest.setUserId(88813017357585L);
        cloudOrderRequest.setChannelId(1);
        cloudOrderRequest.setNumber(1);
        cloudOrderRequest.setPackageId(1);
        cloudOrderRequest.setTotalPrice("600");
        cloudOrderRequest.setTotalPerformance("1");
        MJsonEntity mJsonEntity = cloudOrderService.submitOrder(cloudOrderRequest);
        System.err.println(JSONObject.toJSONString(mJsonEntity));
    }

}
