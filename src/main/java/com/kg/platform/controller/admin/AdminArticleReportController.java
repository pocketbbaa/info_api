package com.kg.platform.controller.admin;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.request.admin.AdminBaseRequest;
import com.kg.platform.model.request.admin.ArticleEditRequest;
import com.kg.platform.model.response.admin.AdminArticleReportByArticleIdResponse;
import com.kg.platform.model.response.admin.AdminArticleReportResponse;
import com.kg.platform.service.admin.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/article/report")
public class AdminArticleReportController {

    @Autowired
    private ArticleService articleService;

    /**
     * 待处理的内容举报列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "index/toAuditList", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = AdminBaseRequest.class)
    public JsonEntity toAuditList(@RequestAttribute AdminBaseRequest request) {
        int currentPage = request.getCurrentPage();
        int pageSize = request.getPageSize();
        currentPage = currentPage <= 0 ? 1 : currentPage;
        pageSize = pageSize <= 0 ? 20 : pageSize;
        PageModel<AdminArticleReportResponse> page = new PageModel<>();
        page.setCurrentPage(currentPage);
        page.setPageSize(pageSize);
        page = articleService.toAuditList(page);
        return JsonEntity.makeSuccessJsonEntity(page);
    }

    /**
     * 编辑文章页面举报信息列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/toAuditList", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = ArticleEditRequest.class)
    public JsonEntity toAuditListByArticleId(@RequestAttribute ArticleEditRequest request) {
        int currentPage = request.getCurrentPage();
        int pageSize = request.getPageSize();
        currentPage = currentPage <= 0 ? 1 : currentPage;
        pageSize = pageSize <= 0 ? 10 : pageSize;
        PageModel<AdminArticleReportByArticleIdResponse> page = new PageModel<>();
        page.setCurrentPage(currentPage);
        page.setPageSize(pageSize);
        page = articleService.toAuditListByArticleId(page, request);
        return JsonEntity.makeSuccessJsonEntity(page);
    }

    /**
     * 处理举报
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/disposeReport", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = ArticleEditRequest.class)
    public JsonEntity disposeReport(@RequestAttribute ArticleEditRequest request) {
        return articleService.disposeReport(request);
    }
}
