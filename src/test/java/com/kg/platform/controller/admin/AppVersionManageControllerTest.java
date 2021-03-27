package com.kg.platform.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.controller.BaseTest;
import com.kg.platform.controller.app.AppVersionController;
import com.kg.platform.model.request.admin.AppVersionManageRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

public class AppVersionManageControllerTest extends BaseTest {

    @Autowired
    private AppVersionManageController appVersionManageController;
    @Autowired
    private AppVersionController appVersionController;

    @Test
    public void create() {
        AppVersionManageRequest appVersionManageRequest = new AppVersionManageRequest();
        appVersionManageRequest.setVersionNum("1.2.7");
        appVersionManageRequest.setPrompt("更新提示");
        appVersionManageRequest.setForced(0);
        appVersionManageRequest.setSystemType(1);
        appVersionManageRequest.setDownloadUrl("http://aasdasd...");
        appVersionManageRequest.setDownloadUrlApk("https://asdasdasdasdasd...");
        appVersionManageRequest.setChannel("channel_vivo");
        JsonEntity json = appVersionManageController.create(appVersionManageRequest);
        System.err.println(JSONObject.toJSONString(json));
    }

    @Test
    public void createList() {
        for (int i = 1; i <= 20; i++) {
            AppVersionManageRequest appVersionManageRequest = new AppVersionManageRequest();
            appVersionManageRequest.setVersionNum("1." + i);
            appVersionManageRequest.setPrompt("更新提示");
            appVersionManageRequest.setForced(1);
            appVersionManageRequest.setSystemType(1);
            appVersionManageRequest.setDownloadUrl("http://aasdasd...");
            JsonEntity json = appVersionManageController.create(appVersionManageRequest);
            System.err.println(JSONObject.toJSONString(json));
        }
    }

    @Test
    public void delete() {
        AppVersionManageRequest appVersionManageRequest = new AppVersionManageRequest();
        appVersionManageRequest.setId(3L);
        JsonEntity json = appVersionManageController.delete(appVersionManageRequest);
        System.err.println(JSONObject.toJSONString(json));
    }

    @Test
    public void getDetail() {
        AppVersionManageRequest appVersionManageRequest = new AppVersionManageRequest();
        appVersionManageRequest.setId(213L);
        JsonEntity json = appVersionManageController.getDetail(appVersionManageRequest);
        System.err.println(JSONObject.toJSONString(json));
    }

    @Test
    public void list() {
        AppVersionManageRequest request = new AppVersionManageRequest();
        request.setCurrentPage(1);
        request.setPageSize(3);
        JsonEntity json = appVersionManageController.list(request);
        System.err.println(JSONObject.toJSONString(json));
    }

    @Test
    public void getVersion() {
        AppVersionManageRequest request = new AppVersionManageRequest();
        request.setSystemType(2);
        request.setVersionNum("1.2.5");
//        request.setChannel("channel_xiaomi");
        AppJsonEntity appJsonEntity = appVersionController.getVersion(request,new MockHttpServletRequest());
        System.err.println(JSONObject.toJSONString(appJsonEntity));
    }


}
