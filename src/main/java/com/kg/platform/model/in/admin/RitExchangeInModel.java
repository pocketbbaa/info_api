package com.kg.platform.model.in.admin;

import java.math.BigDecimal;

public class RitExchangeInModel {

    private Long id;
    private Long userId;
    private String userName;
    private String userPhone;
    private Integer userRole;
    private BigDecimal ritAmount;
    private BigDecimal kgAmount;
    private Long createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    public BigDecimal getRitAmount() {
        return ritAmount;
    }

    public void setRitAmount(BigDecimal ritAmount) {
        this.ritAmount = ritAmount;
    }

    public BigDecimal getKgAmount() {
        return kgAmount;
    }

    public void setKgAmount(BigDecimal kgAmount) {
        this.kgAmount = kgAmount;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
