package com.kg.platform.model.response;

import java.io.Serializable;

/**
 * 首页图片推荐位出参
 * 
 * @author think
 *
 */
public class AppStartPageResponse implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -459636631354222137L;


    private String imageUrl;

    private String adverId;

    private Integer startPageDisplaySecond;

    private String skipType;

    private String skipTo;

    private String adverTitle;

    private String adverOwner;

    private Long  createDate;

    private Integer  adverImgType;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAdverId() {
        return adverId;
    }

    public void setAdverId(String adverId) {
        this.adverId = adverId;
    }

    public Integer getStartPageDisplaySecond() {
        return startPageDisplaySecond;
    }

    public void setStartPageDisplaySecond(Integer startPageDisplaySecond) {
        this.startPageDisplaySecond = startPageDisplaySecond;
    }

    public String getSkipType() {
        return skipType;
    }

    public void setSkipType(String skipType) {
        this.skipType = skipType;
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
}
