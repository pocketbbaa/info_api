package com.kg.platform.common.entity;

/**
 * Created by Administrator on 2018/7/20.
 */
public class ArticleData{
    private String articleId;//资讯ID
    private String intoArticleDate;//进入资讯时间
    private String finalScroll;//最终滚动位置（百分比）
    private String readDuration;//阅读时长
    private String exitArticleDate;//退出时间
    private String ifClickCommentBtn;//是否点击评论按钮 0:否 1：是

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getIntoArticleDate() {
        return intoArticleDate;
    }

    public void setIntoArticleDate(String intoArticleDate) {
        this.intoArticleDate = intoArticleDate;
    }

    public String getFinalScroll() {
        return finalScroll;
    }

    public void setFinalScroll(String finalScroll) {
        this.finalScroll = finalScroll;
    }

    public String getReadDuration() {
        return readDuration;
    }

    public void setReadDuration(String readDuration) {
        this.readDuration = readDuration;
    }

    public String getExitArticleDate() {
        return exitArticleDate;
    }

    public void setExitArticleDate(String exitArticleDate) {
        this.exitArticleDate = exitArticleDate;
    }

    public String getIfClickCommentBtn() {
        return ifClickCommentBtn;
    }

    public void setIfClickCommentBtn(String ifClickCommentBtn) {
        this.ifClickCommentBtn = ifClickCommentBtn;
    }
}
