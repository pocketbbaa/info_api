package com.kg.platform.model.response;

import java.math.BigDecimal;
import java.util.Date;

public class PersonalAdvResponse {

    private String adverId;

    private String imageUrl;

    private String skipTo;

    private Integer skipType;

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

    public Integer getSkipType() {
        return skipType;
    }

    public void setSkipType(Integer skipType) {
        this.skipType = skipType;
    }

    public String getAdverId() {
        return adverId;
    }

    public void setAdverId(String adverId) {
        this.adverId = adverId;
    }
}