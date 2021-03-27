package com.kg.platform.model.response.admin;

public class ArticleStatQueryResponse {

    private String articleId;

    private Integer bonusNum;

    private Double bonusValue;

    private Double bonusTotal;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public Integer getBonusNum() {
        return bonusNum;
    }

    public void setBonusNum(Integer bonusNum) {
        this.bonusNum = bonusNum;
    }

    public Double getBonusValue() {
        return bonusValue;
    }

    public void setBonusValue(Double bonusValue) {
        this.bonusValue = bonusValue;
    }

    public Double getBonusTotal() {
        return bonusTotal;
    }

    public void setBonusTotal(Double bonusTotal) {
        this.bonusTotal = bonusTotal;
    }
}
