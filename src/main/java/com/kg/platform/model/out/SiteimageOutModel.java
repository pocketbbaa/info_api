package com.kg.platform.model.out;

import java.io.Serializable;
import java.util.Date;

/**
 * 首页图片推荐位service出参
 * 
 * @author think
 *
 */
public class SiteimageOutModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -459636631354222137L;

    private Long imageId;

    private String imageAddress;

    private int imageType;

    private String imageDetail;

    private int navigatorPos;

    private int imagePos;

    private Boolean imageStatus;

    private Integer imageOrder;

    private Date createDate;

    private Integer createUser;

    private Date updateDate;

    private Integer updateUser;

    private long articleId;

    private String articleTitle;

    private String adverLink;

    private Integer adverStyle;

    private Integer adverImgType;

    private String adverIntro ;

    private String adverTarget ;

    private Integer spreadTime ;

    private Date spreadTimeS;

    private Date spreadTimeE;

    private String adverOwner;

    private String  adverTitle;

    private int startPageDisplaySecond;

    private Integer skipType;


    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

    public int getImageType() {
        return imageType;
    }

    public void setImageType(int imageType) {
        this.imageType = imageType;
    }

    public String getImageDetail() {
        return imageDetail;
    }

    public void setImageDetail(String imageDetail) {
        this.imageDetail = imageDetail;
    }

    public int getNavigatorPos() {
        return navigatorPos;
    }

    public void setNavigatorPos(int navigatorPos) {
        this.navigatorPos = navigatorPos;
    }

    public int getImagePos() {
        return imagePos;
    }

    public void setImagePos(int imagePos) {
        this.imagePos = imagePos;
    }

    public Boolean getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(Boolean imageStatus) {
        this.imageStatus = imageStatus;
    }

    public Integer getImageOrder() {
        return imageOrder;
    }

    public void setImageOrder(Integer imageOrder) {
        this.imageOrder = imageOrder;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser) {
        this.updateUser = updateUser;
    }

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getAdverLink() {
        return adverLink;
    }

    public void setAdverLink(String adverLink) {
        this.adverLink = adverLink;
    }

    public Integer getAdverStyle() {
        return adverStyle;
    }

    public void setAdverStyle(Integer adverStyle) {
        this.adverStyle = adverStyle;
    }

    public Integer getAdverImgType() {
        return adverImgType;
    }

    public void setAdverImgType(Integer adverImgType) {
        this.adverImgType = adverImgType;
    }

    public String getAdverIntro() {
        return adverIntro;
    }

    public void setAdverIntro(String adverIntro) {
        this.adverIntro = adverIntro;
    }

    public String getAdverTarget() {
        return adverTarget;
    }

    public void setAdverTarget(String adverTarget) {
        this.adverTarget = adverTarget;
    }

    public Integer getSpreadTime() {
        return spreadTime;
    }

    public void setSpreadTime(Integer spreadTime) {
        this.spreadTime = spreadTime;
    }

    public Date getSpreadTimeS() {
        return spreadTimeS;
    }

    public void setSpreadTimeS(Date spreadTimeS) {
        this.spreadTimeS = spreadTimeS;
    }

    public Date getSpreadTimeE() {
        return spreadTimeE;
    }

    public void setSpreadTimeE(Date spreadTimeE) {
        this.spreadTimeE = spreadTimeE;
    }

    public String getAdverOwner() {
        return adverOwner;
    }

    public void setAdverOwner(String adverOwner) {
        this.adverOwner = adverOwner;
    }

    public String getAdverTitle() {
        return adverTitle;
    }

    public void setAdverTitle(String adverTitle) {
        this.adverTitle = adverTitle;
    }

    public int getStartPageDisplaySecond() {
        return startPageDisplaySecond;
    }

    public void setStartPageDisplaySecond(int startPageDisplaySecond) {
        this.startPageDisplaySecond = startPageDisplaySecond;
    }

    public Integer getSkipType() {
        return skipType;
    }

    public void setSkipType(Integer skipType) {
        this.skipType = skipType;
    }
}
