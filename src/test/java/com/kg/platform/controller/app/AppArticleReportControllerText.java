package com.kg.platform.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.controller.BaseTest;
import com.kg.platform.model.request.AppArticleReportRequest;
import com.kg.platform.model.response.UserkgResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AppArticleReportControllerText extends BaseTest {

    @Autowired
    private AppArticleReportController appArticleReportController;

    @Test
    public void reportList() {
        AppJsonEntity jsonEntity = appArticleReportController.reportList();

        System.err.println(JSONObject.toJSONString(jsonEntity));
    }


    @Test
    public void report() {
        AppArticleReportRequest request = new AppArticleReportRequest();
        UserkgResponse kguser = new UserkgResponse();
        request.setArticleId("123");
        request.setReportTextId("1");
        kguser.setUserId("123123");

        AppJsonEntity jsonEntity = appArticleReportController.report(request, kguser);

        System.err.println(JSONObject.toJSONString(jsonEntity));
    }
}
