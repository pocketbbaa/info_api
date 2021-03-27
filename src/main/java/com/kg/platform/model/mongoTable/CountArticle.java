package com.kg.platform.model.mongoTable;

/**
 * Created by Administrator on 2018/8/26.
 */
public class CountArticle {
    private Integer columnId;
    private Integer secondColumn;
    private String articleId;
    private Long createDate;
    private Integer type;//1.显示状态发生改变 2.标题发生改变 3.摘要发生改变 4.封面图发生改变

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSecondColumn() {
        return secondColumn;
    }

    public void setSecondColumn(Integer secondColumn) {
        this.secondColumn = secondColumn;
    }

    public Integer getColumnId() {
        return columnId;
    }

    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }
}
