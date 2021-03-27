package com.kg.platform.controller;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.request.UserConcernRequest;
import com.kg.platform.model.request.UserkgRequest;
import com.kg.platform.model.response.UserConcernListResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserKgControllerTest extends BaseTest {

    @Autowired
    private UserkgController userkgController;

    @Test
    public void getUserDetails() {
        UserkgRequest request = new UserkgRequest();
        request.setUserId(425957073499594752L);
        JsonEntity jsonEntity = userkgController.getUserDetails(request);
        System.err.println(JSONObject.toJSONString(jsonEntity));

    }

    @Test
    public void searchauthor() {
        UserConcernRequest userConcernRequest = new UserConcernRequest();
        userConcernRequest.setSearchStr("BTC");
        userConcernRequest.setCurrentPage(1);
        userConcernRequest.setPageSize(10);
        PageModel<UserConcernListResponse> page = new PageModel<>();
        JsonEntity jsonEntity = userkgController.searchAuthor(userConcernRequest, page);
        System.err.println(JSONObject.toJSONString(jsonEntity));
    }
}
