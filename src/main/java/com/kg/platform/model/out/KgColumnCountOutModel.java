package com.kg.platform.model.out;

import java.io.Serializable;

/**
 * @author 
 */
public class KgColumnCountOutModel implements Serializable {
    /**
     * 根据栏目生成的KEY（对应Redis的key）
     */
    private String columnKey;

    /**
     * 新闻总量
     */
    private Integer articleNum;

    private static final long serialVersionUID = 1L;

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public Integer getArticleNum() {
        return articleNum;
    }

    public void setArticleNum(Integer articleNum) {
        this.articleNum = articleNum;
    }
}