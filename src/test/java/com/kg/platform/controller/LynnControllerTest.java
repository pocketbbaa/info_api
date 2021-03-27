package com.kg.platform.controller;

import com.alibaba.fastjson.JSON;
import com.kg.platform.model.request.TxPasswordEditRequest;
import com.kg.platform.model.response.UserkgResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LynnControllerTest extends BaseTest {

    @Autowired
    private LynnController lynnController;

    @Test
    public void testGetTxAuthCode(){
        TxPasswordEditRequest request = new TxPasswordEditRequest();
        request.setCode("664097");

        UserkgResponse kgUser = new UserkgResponse();
        kgUser.setMobIle("18224449005");
        System.out.println(JSON.toJSONString(lynnController.getAuthCode(request,kgUser)));
    }

    @Test
    public void testUpdateTxPassword(){
        TxPasswordEditRequest request = new TxPasswordEditRequest();
        request.setCode("372136112304");
        request.setPassword("test111");

        UserkgResponse kgUser = new UserkgResponse();
        kgUser.setMobIle("18224449005");

        System.out.println(JSON.toJSONString(lynnController.updateTxPassword(request,kgUser)));
    }
}
