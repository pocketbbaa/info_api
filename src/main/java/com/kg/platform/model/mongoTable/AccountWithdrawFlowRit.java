package com.kg.platform.model.mongoTable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/9/5.
 */
public class AccountWithdrawFlowRit {

    private Long withdrawFlowId;//提现工单ID

    private Long userId;//用户ID

    private Long manualTransferUserId;//手动发起转账的后台人员ID

    private String userPhone;//用户手机号

    private String userName;//用户昵称

    private BigDecimal withdrawAmount;//RIT提现数量

    private BigDecimal accountAmount;//RIT实际到账数

    private BigDecimal poundageAmount;//手续费

    private Long withdrawTime;//提现时间

    private Long accountTime;//转出时间

    private String fromAddress;//千氪地址

    private String toAddress;//提现地址

    private String txId;//交易ID

    private String memo;//用户的备注

    private Integer status;//提现状态 0：审核中 1：等待转出（审核通过） 2：审核不通过 3：已转出 4：转出失败 5：已撤销

    private String operatorAuditRemark;//运营的审批备注

    private String financialAuditRemark;//财务的审批备注

    private String auditUserId;//审核人用户ID

    private String auditUserName;//审核人用户姓名

    private Integer userRole;//用户角色

    private String userRoleName;//用户角色名

    private Integer ifOper;//是否在操作中 0：否 1：是

    public Integer getIfOper() {
        return ifOper;
    }

    public void setIfOper(Integer ifOper) {
        this.ifOper = ifOper;
    }

    public Long getManualTransferUserId() {
        return manualTransferUserId;
    }

    public void setManualTransferUserId(Long manualTransferUserId) {
        this.manualTransferUserId = manualTransferUserId;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public String getUserRoleName() {
        return userRoleName;
    }

    public void setUserRoleName(String userRoleName) {
        this.userRoleName = userRoleName;
    }

    public String getAuditUserId() {
        return auditUserId;
    }

    public void setAuditUserId(String auditUserId) {
        this.auditUserId = auditUserId;
    }

    public String getAuditUserName() {
        return auditUserName;
    }

    public void setAuditUserName(String auditUserName) {
        this.auditUserName = auditUserName;
    }

    public Long getWithdrawFlowId() {
        return withdrawFlowId;
    }

    public void setWithdrawFlowId(Long withdrawFlowId) {
        this.withdrawFlowId = withdrawFlowId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Long getWithdrawTime() {
        return withdrawTime;
    }

    public void setWithdrawTime(Long withdrawTime) {
        this.withdrawTime = withdrawTime;
    }

    public Long getAccountTime() {
        return accountTime;
    }

    public void setAccountTime(Long accountTime) {
        this.accountTime = accountTime;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOperatorAuditRemark() {
        return operatorAuditRemark;
    }

    public void setOperatorAuditRemark(String operatorAuditRemark) {
        this.operatorAuditRemark = operatorAuditRemark;
    }

    public String getFinancialAuditRemark() {
        return financialAuditRemark;
    }

    public void setFinancialAuditRemark(String financialAuditRemark) {
        this.financialAuditRemark = financialAuditRemark;
    }
}
