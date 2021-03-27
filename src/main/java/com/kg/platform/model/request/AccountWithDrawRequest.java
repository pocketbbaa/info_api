package com.kg.platform.model.request;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/9/7.
 */
public class AccountWithDrawRequest {

    private String txAddress;//转出地址

    private String memo;//转出备注

    private BigDecimal txAmount;//转出数量

    private String txPassword;//交易密码

    private String code;//验证码

    private Integer coinType;//币种

    private String userId;

    private BigDecimal fee;//手续费

    private String withdrawFlowId;//提现工单ID

    private String userPhone;//用户手机号

    private String userName;

    private Integer status;//提现状态 0：审核中 1：等待转出（审核通过） 2：审核不通过 3：已转出 4：转出失败 5：已撤销

    private Long startTime;//开始时间

    private Long endTime;//结束时间

    private Integer currentPage;

    private Integer pageSize;

    private Integer userRole;//用户角色

    private Integer auditRole;//1:运营 2：财务

    private String auditRemark;//审核备注

    private String txId;//交易ID

    private String withdrawFlowIds;//多个提现工单ID(JSONARRAY)

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getWithdrawFlowIds() {
        return withdrawFlowIds;
    }

    public void setWithdrawFlowIds(String withdrawFlowIds) {
        this.withdrawFlowIds = withdrawFlowIds;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public Integer getAuditRole() {
        return auditRole;
    }

    public void setAuditRole(Integer auditRole) {
        this.auditRole = auditRole;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getWithdrawFlowId() {
        return withdrawFlowId;
    }

    public void setWithdrawFlowId(String withdrawFlowId) {
        this.withdrawFlowId = withdrawFlowId;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getCoinType() {
        return coinType;
    }

    public void setCoinType(Integer coinType) {
        this.coinType = coinType;
    }

    public String getTxAddress() {
        return txAddress;
    }

    public void setTxAddress(String txAddress) {
        this.txAddress = txAddress;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public BigDecimal getTxAmount() {
        return txAmount;
    }

    public void setTxAmount(BigDecimal txAmount) {
        this.txAmount = txAmount;
    }

    public String getTxPassword() {
        return txPassword;
    }

    public void setTxPassword(String txPassword) {
        this.txPassword = txPassword;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
