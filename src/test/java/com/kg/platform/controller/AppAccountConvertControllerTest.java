package com.kg.platform.controller;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.controller.app.AppAccountConvertController;
import com.kg.platform.model.response.UserkgResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AppAccountConvertControllerTest extends BaseTest {

    @Autowired
    private AppAccountConvertController appAccountConvertController;

    @Test
    public void kgToRitIndex() {
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("428944063207710720");
        kguser.setUserRole(1);
        AppJsonEntity appJsonEntity = appAccountConvertController.kgToRitIndex(kguser);
        System.err.println(JSONObject.toJSONString(appJsonEntity));
    }

    @Test
    public void kgToRitCheck() {
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("428944063207710720");
        kguser.setUserRole(1);
        AppJsonEntity jsonEntity = appAccountConvertController.kgToRitCheck(kguser);
        System.err.println(JSONObject.toJSONString(jsonEntity));
    }

    @Test
    public void kgToRit() {
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("428944063207710720");
        kguser.setUserRole(1);
        kguser.setUserName("wangyang");
        kguser.setMobIle("13880264646");
        AppJsonEntity jsonEntity = appAccountConvertController.kgToRit(kguser);
        System.err.println(JSONObject.toJSONString(jsonEntity));
    }
}
