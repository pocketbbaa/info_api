package com.kg.platform.model.response;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2018/9/5.
 */
public class AccountWithdrawFlowRitResponse {

    private String withdrawFlowId;//提现工单ID

    private String userId;//用户ID

    private String userPhone;//用户手机号

    private String userEmail;//用户邮箱

    private String userName;//用户昵称

    private String accountTime;//转出时间

    private String auditUserName;//审核人用户姓名

    private String withdrawAmount;//RIT提现数量

    private String accountAmount;//RIT实际到账数

    private String poundageAmount;//手续费

    private String withdrawTime;//提现时间

    private String toAddress;//提现地址

    private Integer status;//提现状态 0：审核中 1：等待转出（审核通过） 2：审核不通过 3：已转出 4：转出失败 5：已撤销

    private String statusName;//状态含义

    private Integer userRole;//用户角色

    private String userRoleName;//用户角色名

    private Integer ifOper;//是否在操作中 0：否 1：是

    private List<WithdrawOperRecordResponse> withdrawOperRecordResponseList;

    public Integer getIfOper() {
        return ifOper;
    }

    public void setIfOper(Integer ifOper) {
        this.ifOper = ifOper;
    }

    public List<WithdrawOperRecordResponse> getWithdrawOperRecordResponseList() {
        return withdrawOperRecordResponseList;
    }

    public void setWithdrawOperRecordResponseList(List<WithdrawOperRecordResponse> withdrawOperRecordResponseList) {
        this.withdrawOperRecordResponseList = withdrawOperRecordResponseList;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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

    public String getAccountTime() {
        return accountTime;
    }

    public void setAccountTime(String accountTime) {
        this.accountTime = accountTime;
    }

    public String getAuditUserName() {
        return auditUserName;
    }

    public void setAuditUserName(String auditUserName) {
        this.auditUserName = auditUserName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getWithdrawFlowId() {
        return withdrawFlowId;
    }

    public void setWithdrawFlowId(String withdrawFlowId) {
        this.withdrawFlowId = withdrawFlowId;
    }

    public String getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(String withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public String getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(String accountAmount) {
        this.accountAmount = accountAmount;
    }

    public String getPoundageAmount() {
        return poundageAmount;
    }

    public void setPoundageAmount(String poundageAmount) {
        this.poundageAmount = poundageAmount;
    }

    public String getWithdrawTime() {
        return withdrawTime;
    }

    public void setWithdrawTime(String withdrawTime) {
        this.withdrawTime = withdrawTime;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
