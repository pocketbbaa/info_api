package com.kg.platform.model.request;

/**
 * APP举报
 */
public class AppArticleReportRequest {

    private String articleId;
    private String reportTextId;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getReportTextId() {
        return reportTextId;
    }

    public void setReportTextId(String reportTextId) {
        this.reportTextId = reportTextId;
    }
}
