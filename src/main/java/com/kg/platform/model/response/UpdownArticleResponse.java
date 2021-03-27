package com.kg.platform.model.response;

import java.io.Serializable;

public class UpdownArticleResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8304519296527192420L;

    private String upArticleId;

    private String upArticleTitle;

    private String upArticleAuthor;

    private String downArticleId;

    private String downArticleTitle;

    private String downArticleAuthor;

    private Integer UpPublishKind; // 发布类型 1 文章 2 视频

    private Integer downPublishKind;

    public Integer getDownPublishKind() {
        return downPublishKind;
    }

    public void setDownPublishKind(Integer downPublishKind) {
        this.downPublishKind = downPublishKind;
    }

    public Integer getUpPublishKind() {
        return UpPublishKind;
    }

    public void setUpPublishKind(Integer upPublishKind) {
        UpPublishKind = upPublishKind;
    }

    public String getUpArticleId() {
        return upArticleId;
    }

    public void setUpArticleId(String upArticleId) {
        this.upArticleId = upArticleId;
    }

    public String getUpArticleTitle() {
        return upArticleTitle;
    }

    public void setUpArticleTitle(String upArticleTitle) {
        this.upArticleTitle = upArticleTitle;
    }

    public String getDownArticleId() {
        return downArticleId;
    }

    public void setDownArticleId(String downArticleId) {
        this.downArticleId = downArticleId;
    }

    public String getDownArticleTitle() {
        return downArticleTitle;
    }

    public void setDownArticleTitle(String downArticleTitle) {
        this.downArticleTitle = downArticleTitle;
    }

    public String getUpArticleAuthor() {
        return upArticleAuthor;
    }

    public void setUpArticleAuthor(String upArticleAuthor) {
        this.upArticleAuthor = upArticleAuthor;
    }

    public String getDownArticleAuthor() {
        return downArticleAuthor;
    }

    public void setDownArticleAuthor(String downArticleAuthor) {
        this.downArticleAuthor = downArticleAuthor;
    }

}