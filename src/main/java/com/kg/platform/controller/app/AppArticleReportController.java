package com.kg.platform.controller.app;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.model.request.AppArticleReportRequest;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.app.AppArticleReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * APP举报相关
 */
@Controller
@RequestMapping("/kgApp")
public class AppArticleReportController {

    @Autowired
    private AppArticleReportService appArticleReportService;

    /**
     * 获取举报内容列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/article/report/list")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = false)
    public AppJsonEntity reportList() {
        return appArticleReportService.reportList();
    }

    /**
     * 举报
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/article/report")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = false, beanClazz = AppArticleReportRequest.class)
    public AppJsonEntity report(@RequestAttribute AppArticleReportRequest request, @RequestAttribute UserkgResponse kguser) {
        return appArticleReportService.report(request, kguser);
    }

}
