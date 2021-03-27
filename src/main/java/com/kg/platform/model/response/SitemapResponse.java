package com.kg.platform.model.response;

import java.io.Serializable;

public class SitemapResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8242019953625150393L;

    private String articleId;

    private String articleTitle;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

}
