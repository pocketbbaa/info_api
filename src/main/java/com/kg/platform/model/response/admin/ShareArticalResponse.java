package com.kg.platform.model.response.admin;

public class ShareArticalResponse extends AdminBaseResponse {

    /**
     * 
     */
    private static final long serialVersionUID = -8549770256464921903L;

    private String flowId;

    private String nickName;

    private Long userId;

    private String userPhone;

    private String userRole;

    private String userLevel;

    private String shareTotalBonus;

    private String shareTodayBonus;

    private String masterShareTodayBonus;

    private String masterShareTotalBonus;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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

    public String getShareTotalBonus() {
        return shareTotalBonus;
    }

    public void setShareTotalBonus(String shareTotalBonus) {
        this.shareTotalBonus = shareTotalBonus;
    }

    public String getShareTodayBonus() {
        return shareTodayBonus;
    }

    public void setShareTodayBonus(String shareTodayBonus) {
        this.shareTodayBonus = shareTodayBonus;
    }

    public String getMasterShareTodayBonus() {
        return masterShareTodayBonus;
    }

    public void setMasterShareTodayBonus(String masterShareTodayBonus) {
        this.masterShareTodayBonus = masterShareTodayBonus;
    }

    public String getMasterShareTotalBonus() {
        return masterShareTotalBonus;
    }

    public void setMasterShareTotalBonus(String masterShareTotalBonus) {
        this.masterShareTotalBonus = masterShareTotalBonus;
    }

}
