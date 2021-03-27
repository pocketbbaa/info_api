package com.kg.platform.model.response;

import java.io.Serializable;
import java.util.Date;

/**
 * 首页图片推荐位出参
 * 
 * @author think
 *
 */
public class AdverResponse implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -459636631354222137L;

    private String adverId;

    private String imageUrl;

    private String[] imageUrls;

    private String skipTo;

    private String adverTitle;

    private String adverOwner;

    private Integer skipType ;

    private Long createDate ;

    private Integer adverImgType ;

    private Integer inmageType ;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSkipTo() {
        return skipTo;
    }

    public void setSkipTo(String skipTo) {
        this.skipTo = skipTo;
    }

    public String getAdverTitle() {
        return adverTitle;
    }

    public void setAdverTitle(String adverTitle) {
        this.adverTitle = adverTitle;
    }

    public String getAdverOwner() {
        return adverOwner;
    }

    public void setAdverOwner(String adverOwner) {
        this.adverOwner = adverOwner;
    }

    public Integer getSkipType() {
        return skipType;
    }

    public void setSkipType(Integer skipType) {
        this.skipType = skipType;
    }

    public String[] getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String[] imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getAdverId() {
        return adverId;
    }

    public void setAdverId(String adverId) {
        this.adverId = adverId;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public Integer getAdverImgType() {
        return adverImgType;
    }

    public void setAdverImgType(Integer adverImgType) {
        this.adverImgType = adverImgType;
    }

    public Integer getInmageType() {
        return inmageType;
    }

    public void setInmageType(Integer inmageType) {
        this.inmageType = inmageType;
    }
}
