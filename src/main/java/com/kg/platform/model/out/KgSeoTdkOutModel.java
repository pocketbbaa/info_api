package com.kg.platform.model.out;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class KgSeoTdkOutModel implements Serializable {
    private Integer tdkId;

    /**
     * target_type=1时代表首页栏目ID，=2时代表栏目ID，=3代表文章ID
     */
    private Long targetId;

    private String seoTitle;

    private String seoDesc;

    private String seoKeywords;

    /**
     * 1:首页TDK 2：栏目页TDK 3：文章详情TDK
     */
    private Integer targetType;

    private Date updateDate;

    private static final long serialVersionUID = 1L;

    public Integer getTdkId() {
        return tdkId;
    }

    public void setTdkId(Integer tdkId) {
        this.tdkId = tdkId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
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

    public Integer getTargetType() {
        return targetType;
    }

    public void setTargetType(Integer targetType) {
        this.targetType = targetType;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}