package com.kg.platform.service.admin;

import com.kg.platform.model.request.admin.ExportRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface DateStatisticalExportService {


    void exportArticleData(ExportRequest request, HttpServletResponse response, HttpServletRequest servletRequest);

    void exportAsverageUserData(ExportRequest request, HttpServletResponse response, HttpServletRequest servletRequest);

    void exportAuthorData(ExportRequest request, HttpServletResponse response, HttpServletRequest servletRequest);
}
