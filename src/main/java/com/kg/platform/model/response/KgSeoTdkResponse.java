package com.kg.platform.model.response;

import java.io.Serializable;

/**
 * @author 
 */
public class KgSeoTdkResponse implements Serializable {
    private Integer tdkId;

    private String seoTitle;

    private String seoDesc;

    private String seoKeywords;

    private static final long serialVersionUID = 1L;

    public Integer getTdkId() {
        return tdkId;
    }

    public void setTdkId(Integer tdkId) {
        this.tdkId = tdkId;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getSeoDesc() {
        return seoDesc;
    }

    public void setSeoDesc(String seoDesc) {
        this.seoDesc = seoDesc;
    }

    public String getSeoKeywords() {
        return seoKeywords;
    }

    public void setSeoKeywords(String seoKeywords) {
        this.seoKeywords = seoKeywords;
    }

}