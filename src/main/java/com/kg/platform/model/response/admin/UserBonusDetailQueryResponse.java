package com.kg.platform.model.response.admin;

import java.math.BigDecimal;

public class UserBonusDetailQueryResponse extends AdminBaseResponse {

    /**
     * 
     */
    private static final long serialVersionUID = 7083294568222846398L;

    private Long bonusDetailId;

    private String extraBonusId;

    private String userId;

    private String userName;

    private String createTime;

    private String userMobile;

    private BigDecimal tvAmount;

    private BigDecimal kgAmount;

    private Long tvAccountFlowId;

    private Long kgAccountFlowId;

    private Integer userRole;

    private Integer columnAuthed;

    private Integer userLevel;

    public Long getBonusDetailId() {
        return bonusDetailId;
    }

    public void setBonusDetailId(Long bonusDetailId) {
        this.bonusDetailId = bonusDetailId;
    }

    public String getExtraBonusId() {
        return extraBonusId;
    }

    public void setExtraBonusId(String extraBonusId) {
        this.extraBonusId = extraBonusId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getTvAmount() {
        return tvAmount;
    }

    public void setTvAmount(BigDecimal tvAmount) {
        this.tvAmount = tvAmount;
    }

    public BigDecimal getKgAmount() {
        return kgAmount;
    }

    public void setKgAmount(BigDecimal kgAmount) {
        this.kgAmount = kgAmount;
    }

    public Long getTvAccountFlowId() {
        return tvAccountFlowId;
    }

    public void setTvAccountFlowId(Long tvAccountFlowId) {
        this.tvAccountFlowId = tvAccountFlowId;
    }

    public Long getKgAccountFlowId() {
        return kgAccountFlowId;
    }

    public void setKgAccountFlowId(Long kgAccountFlowId) {
        this.kgAccountFlowId = kgAccountFlowId;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public Integer getColumnAuthed() {
        return columnAuthed;
    }

    public void setColumnAuthed(Integer columnAuthed) {
        this.columnAuthed = columnAuthed;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

}
