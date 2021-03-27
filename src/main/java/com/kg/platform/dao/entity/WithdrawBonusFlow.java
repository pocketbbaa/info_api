package com.kg.platform.dao.entity;

import java.math.BigDecimal;
import java.util.Date;

public class WithdrawBonusFlow {
	
    private Long withdrawFlowId;

    private Long accountFlowId;

    private Long userId;

    private Integer inviteCount;

    private String userPhone;

    private String userEmail;

    private BigDecimal withdrawAmount;

    private BigDecimal accountAmount;

    private BigDecimal poundageAmount;

    private Date withdrawTime;

    private Date accountTime;

    private String remark;

    private Integer status;

    public Long getWithdrawFlowId() {
        return withdrawFlowId;
    }

    public void setWithdrawFlowId(Long withdrawFlowId) {
        this.withdrawFlowId = withdrawFlowId;
    }

    public Long getAccountFlowId() {
        return accountFlowId;
    }

    public void setAccountFlowId(Long accountFlowId) {
        this.accountFlowId = accountFlowId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(Integer inviteCount) {
        this.inviteCount = inviteCount;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public BigDecimal getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(BigDecimal withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public BigDecimal getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(BigDecimal accountAmount) {
        this.accountAmount = accountAmount;
    }

    public BigDecimal getPoundageAmount() {
        return poundageAmount;
    }

    public void setPoundageAmount(BigDecimal poundageAmount) {
        this.poundageAmount = poundageAmount;
    }

    public Date getWithdrawTime() {
        return withdrawTime;
    }

    public void setWithdrawTime(Date withdrawTime) {
        this.withdrawTime = withdrawTime;
    }

    public Date getAccountTime() {
        return accountTime;
    }

    public void setAccountTime(Date accountTime) {
        this.accountTime = accountTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}