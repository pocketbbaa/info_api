package com.kg.platform.model.request.admin;

public class AdminSiteimageQueryRequest extends AdminBaseRequest {

    private static final long serialVersionUID = 6944375982599125279L;

    private Integer imageId;

    private Integer imageType;

    private Integer navigatorPos;

    private Integer imagePos;

    private Integer imageStatus;

    private Integer displayPort;

    private Integer adverStyle;

    private String adverTitle;

    private String adverOwner;

    private String orderByClause;

    private Integer adverType;

    public Integer getAdverType() {
        return adverType;
    }

    public void setAdverType(Integer adverType) {
        this.adverType = adverType;
    }

    public Integer getImageType() {
        return imageType;
    }

    public void setImageType(Integer imageType) {
        this.imageType = imageType;
    }

    public Integer getNavigatorPos() {
        return navigatorPos;
    }

    public void setNavigatorPos(Integer navigatorPos) {
        this.navigatorPos = navigatorPos;
    }

    public Integer getImagePos() {
        return imagePos;
    }

    public void setImagePos(Integer imagePos) {
        this.imagePos = imagePos;
    }

    public Integer getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(Integer imageStatus) {
        this.imageStatus = imageStatus;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public Integer getDisplayPort() {
        return displayPort;
    }

    public void setDisplayPort(Integer displayPort) {
        this.displayPort = displayPort;
    }

    public Integer getAdverStyle() {
        return adverStyle;
    }

    public void setAdverStyle(Integer adverStyle) {
        this.adverStyle = adverStyle;
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

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }
}
