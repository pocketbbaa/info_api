package com.kg.platform.service.app;

import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.model.request.AppArticleReportRequest;
import com.kg.platform.model.response.UserkgResponse;

public interface AppArticleReportService {

    /**
     * 获取举报文本列表
     *
     * @return
     */
    AppJsonEntity reportList();

    /**
     * APP举报
     *
     * @param request
     * @param kguser
     * @return
     */
    AppJsonEntity report(AppArticleReportRequest request, UserkgResponse kguser);
}
