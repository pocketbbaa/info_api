package com.kg.platform.model.response.admin;

public class RitExchangeQueryResponse {

    private String id;
    private String userId;
    private String userName;
    private String userPhone;
    private Integer userRole;
    private String ritAmount;
    private String kgAmount;
    private String createTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRitAmount() {
        return ritAmount;
    }

    public void setRitAmount(String ritAmount) {
        this.ritAmount = ritAmount;
    }

    public String getKgAmount() {
        return kgAmount;
    }

    public void setKgAmount(String kgAmount) {
        this.kgAmount = kgAmount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
