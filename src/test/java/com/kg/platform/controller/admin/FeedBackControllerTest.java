package com.kg.platform.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.controller.BaseTest;
import com.kg.platform.model.request.admin.FeedbackQueryRequest;
import org.junit.Test;

import javax.inject.Inject;

public class FeedBackControllerTest extends BaseTest {

    @Inject
    private FeedbackController feedbackController;

    @Test
    public void getFeedbackList() {
        FeedbackQueryRequest request = new FeedbackQueryRequest();
        JsonEntity json = feedbackController.getFeedbackList(request);
        System.err.println(JSONObject.toJSONString(json));
    }


}
