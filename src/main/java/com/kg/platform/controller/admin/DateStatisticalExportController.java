package com.kg.platform.controller.admin;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.model.request.admin.ExportRequest;
import com.kg.platform.service.admin.DateStatisticalExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/admin/data/export")
public class DateStatisticalExportController {

    @Autowired
    private DateStatisticalExportService dateStatisticalExportService;

    /**
     * 文章视频数据导出
     *
     * @param request
     * @param response
     * @param servletRequest
     */
    @ResponseBody
    @RequestMapping("article")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ExportRequest.class)
    public void exportArticleData(@RequestAttribute ExportRequest request, HttpServletResponse response, HttpServletRequest servletRequest) {
        dateStatisticalExportService.exportArticleData(request, response, servletRequest);
    }

    /**
     * 普通注册用户
     *
     * @param request
     * @param response
     * @param servletRequest
     */
    @ResponseBody
    @RequestMapping("averageUser")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ExportRequest.class)
    public void exportAsverageUserData(@RequestAttribute ExportRequest request, HttpServletResponse response, HttpServletRequest servletRequest) {
        dateStatisticalExportService.exportAsverageUserData(request, response, servletRequest);
    }

    /**
     * 作者数据
     *
     * @param request
     * @param response
     * @param servletRequest
     */
    @ResponseBody
    @RequestMapping("author")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ExportRequest.class)
    public void exportAuthorData(@RequestAttribute ExportRequest request, HttpServletResponse response, HttpServletRequest servletRequest) {
        dateStatisticalExportService.exportAuthorData(request, response, servletRequest);
    }


}
