package com.kg.platform.model.response;

import java.io.Serializable;

public class ArticleAppResponse implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5792499177876973579L;

    private String articleId;

    private String articleTitle;

    private String articleImage;

    private String displayStatus;  //显示设置 1 正常显示 2 首页置顶 3 首页推荐 4 前台隐藏

    private String createUser;

    private String username;

    private Integer articleType; // 文章类别 1 原创 2 转载

    private String updateDate;

    private Integer browseNum;// 文章浏览数

    private long articlefrom;// 来源 1 本站 2 btc123 3 网易财经 4 东方财富网 5 币世界 6 中国经营网 7 人民网 8 腾讯网 9 搜狐新闻

    private String updateTimestamp;

    private Integer publishKind;

    private Integer articleImgSize;

    private String blockchainUrl; //文章上链后的查看地址

    public String getBlockchainUrl() {
        return blockchainUrl;
    }

    public void setBlockchainUrl(String blockchainUrl) {
        this.blockchainUrl = blockchainUrl;
    }
    public Integer getArticleImgSize() {
        return articleImgSize;
    }

    public void setArticleImgSize(Integer articleImgSize) {
        this.articleImgSize = articleImgSize;
    }

    public Integer getPublishKind() {
        return publishKind;
    }

    public void setPublishKind(Integer publishKind) {
        this.publishKind = publishKind;
    }

    public String getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(String updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public Integer getArticleType() {
        return articleType;
    }

    public void setArticleType(Integer articleType) {
        this.articleType = articleType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

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

    public String getArticleImage() {
        return articleImage;
    }

    public void setArticleImage(String articleImage) {
        this.articleImage = articleImage;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getBrowseNum() {
        return browseNum;
    }

    public void setBrowseNum(Integer browseNum) {
        this.browseNum = browseNum;
    }

    public long getArticlefrom() {
        return articlefrom;
    }

    public void setArticlefrom(long articlefrom) {
        this.articlefrom = articlefrom;
    }
}
