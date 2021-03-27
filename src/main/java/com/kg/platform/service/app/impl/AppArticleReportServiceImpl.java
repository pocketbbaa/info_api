package com.kg.platform.service.app.impl;

import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.dao.read.AppArticleReportRMapper;
import com.kg.platform.dao.write.AppArticleReportWMapper;
import com.kg.platform.model.in.ArticleReportInModel;
import com.kg.platform.model.request.AppArticleReportRequest;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.app.AppArticleReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppArticleReportServiceImpl implements AppArticleReportService {

    @Autowired
    private AppArticleReportRMapper appArticleReportRMapper;
    @Autowired
    private AppArticleReportWMapper appArticleReportWMapper;

    @Override
    public AppJsonEntity reportList() {
        return AppJsonEntity.makeSuccessJsonEntity(appArticleReportRMapper.reportList());
    }

    @Override
    public AppJsonEntity report(AppArticleReportRequest request, UserkgResponse kguser) {
        ArticleReportInModel inModel = new ArticleReportInModel();
        inModel.setUserId(Long.valueOf(kguser.getUserId()));
        inModel.setArticleId(Long.valueOf(request.getArticleId()));
        inModel.setReportTextId(Long.valueOf(request.getReportTextId()));
        int success = appArticleReportWMapper.addReport(inModel);
        if (success > 0) {
            return AppJsonEntity.makeSuccessJsonEntity("SUCCESS");
        }
        return AppJsonEntity.makeExceptionJsonEntity("", "报告失败");
    }
}
