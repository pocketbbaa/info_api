package com.kg.platform.model.request;

/**
 * Created by Administrator on 2018/5/16.
 */
public class PushArticleRequest {
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
