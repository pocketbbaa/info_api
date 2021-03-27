package com.kg.platform.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.controller.BaseTest;
import com.kg.platform.model.request.admin.AdminBaseRequest;
import com.kg.platform.model.request.admin.ArticleEditRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AdminArticleReportControllerTest extends BaseTest {


    @Autowired
    private AdminArticleReportController adminArticleReportController;

    @Test
    public void toAuditList() {
        AdminBaseRequest request = new AdminBaseRequest();
        request.setCurrentPage(1);
        request.setPageSize(10);
        JsonEntity jsonEntity = adminArticleReportController.toAuditList(request);

        System.err.println(JSONObject.toJSONString(jsonEntity));
    }

    @Test
    public void toAuditListByArticleId() {
        ArticleEditRequest request = new ArticleEditRequest();
        request.setArticleId(477412019092459520L);
        request.setCurrentPage(1);
        request.setPageSize(10);
        JsonEntity jsonEntity = adminArticleReportController.toAuditListByArticleId(request);
        System.err.println(JSONObject.toJSONString(jsonEntity));
    }

    @Test
    public void disposeReport() {
        ArticleEditRequest request = new ArticleEditRequest();
        request.setArticleId(477412019092459520L);
        JsonEntity jsonEntity = adminArticleReportController.disposeReport(request);
        System.err.println(JSONObject.toJSONString(jsonEntity));
    }
}
