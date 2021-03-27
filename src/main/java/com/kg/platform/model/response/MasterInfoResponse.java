package com.kg.platform.model.response;

import java.math.BigDecimal;

public class MasterInfoResponse {

    private BigDecimal Income = BigDecimal.ZERO;// 累积所得

    private BigDecimal TodayIncome = BigDecimal.ZERO;// 今日所得

    private String recenflowDate; // 最近时间

    private BigDecimal awrad = BigDecimal.ZERO;// 我的奖励

    private String avatar;// 头像

    private String userName; // 用户名

    private String tUId; // 师傅id

    private String identityTag; //身份标识

    private int realAuthedTag; //实名标签 0:无，1：有

    private int vipTag; //是否有大V标签 0：无，1：有

    private int concernedStatus; //关注状态 0：未关注，1：已关注，2：互相关注

    private int userRole;

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public int getConcernedStatus() {
        return concernedStatus;
    }

    public void setConcernedStatus(int concernedStatus) {
        this.concernedStatus = concernedStatus;
    }

    public String getIdentityTag() {
        return identityTag;
    }

    public void setIdentityTag(String identityTag) {
        this.identityTag = identityTag;
    }

    public int getRealAuthedTag() {
        return realAuthedTag;
    }

    public void setRealAuthedTag(int realAuthedTag) {
        this.realAuthedTag = realAuthedTag;
    }

    public int getVipTag() {
        return vipTag;
    }

    public void setVipTag(int vipTag) {
        this.vipTag = vipTag;
    }

    public BigDecimal getIncome() {
        return Income;
    }

    public void setIncome(BigDecimal income) {
        Income = income;
    }

    public BigDecimal getTodayIncome() {
        return TodayIncome;
    }

    public void setTodayIncome(BigDecimal todayIncome) {
        TodayIncome = todayIncome;
    }

    public String getRecenflowDate() {
        return recenflowDate;
    }

    public void setRecenflowDate(String recenflowDate) {
        this.recenflowDate = recenflowDate;
    }

    public BigDecimal getAwrad() {
        return awrad;
    }

    public void setAwrad(BigDecimal awrad) {
        this.awrad = awrad;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String gettUId() {
        return tUId;
    }

    public void settUId(String tUId) {
        this.tUId = tUId;
    }

}