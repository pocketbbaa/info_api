package com.kg.platform.model.response.admin;

import java.math.BigDecimal;

public class ShareQueryResponse extends AdminBaseResponse {

    /**
     * 
     */
    private static final long serialVersionUID = -8549770256464921903L;

    private String flowId;

    private String nickName;

    private String userId;

    private String userPhone;

    private String userRole;

    private String userLevel;

    private BigDecimal stTv;

    private BigDecimal stKg;

    private BigDecimal mTv;

    private BigDecimal mKg;

    private BigDecimal snTv;

    private BigDecimal snKg;

    private BigDecimal mtTv;

    private BigDecimal mtKg;

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public BigDecimal getStTv() {
        return stTv;
    }

    public void setStTv(BigDecimal stTv) {
        this.stTv = stTv;
    }

    public BigDecimal getStKg() {
        return stKg;
    }

    public void setStKg(BigDecimal stKg) {
        this.stKg = stKg;
    }

    public BigDecimal getmTv() {
        return mTv;
    }

    public void setmTv(BigDecimal mTv) {
        this.mTv = mTv;
    }

    public BigDecimal getmKg() {
        return mKg;
    }

    public void setmKg(BigDecimal mKg) {
        this.mKg = mKg;
    }

    public BigDecimal getSnTv() {
        return snTv;
    }

    public void setSnTv(BigDecimal snTv) {
        this.snTv = snTv;
    }

    public BigDecimal getSnKg() {
        return snKg;
    }

    public void setSnKg(BigDecimal snKg) {
        this.snKg = snKg;
    }

    public BigDecimal getMtTv() {
        return mtTv;
    }

    public void setMtTv(BigDecimal mtTv) {
        this.mtTv = mtTv;
    }

    public BigDecimal getMtKg() {
        return mtKg;
    }

    public void setMtKg(BigDecimal mtKg) {
        this.mtKg = mtKg;
    }
}
