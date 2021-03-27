package com.kg.platform.model.response;

/**
 * Created by Administrator on 2018/5/16.
 */
public class PushArticleResponse {
    public static final String SETTING_KEY = "push_article_limit";
    private Integer pushArticleLimit;
    private Integer pushArticleNumber;

    public Integer getPushArticleLimit() {
        return pushArticleLimit;
    }

    public void setPushArticleLimit(Integer pushArticleLimit) {
        this.pushArticleLimit = pushArticleLimit;
    }

    public Integer getPushArticleNumber() {
        return pushArticleNumber;
    }

    public void setPushArticleNumber(Integer pushArticleNumber) {
        this.pushArticleNumber = pushArticleNumber;
    }
}
