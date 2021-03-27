package com.kg.platform.model.response.admin;

import java.util.Date;

/**
 * 交易记录查询出参
 */
public class AccountQueryResponse extends AdminBaseResponse {

    /**
     * 
     */
    private static final long serialVersionUID = 4389132702591588908L;

    private String flowId;

    private String userId;

    private String userName;

    private Integer businessTypeId;

    private String poundageAmount;

    private String address;

    private String businessTypeName;

    private String amount;

    private String allAmount;

    private String accountAmount;

    private String freezeAmount;

    private String flowDate;

    private String mobile;

    private String email;

    private String status;

    private Integer userRole;

    private String userRoleDisplay;

    private Integer level;

    private String levelDisplay;

    private String articleId;

    private String articleTitle;

    private Integer bonusTotalPerson;

    //操作账户  1可用余额 2冻结余额
    private Integer operAccount;

    private String flowDetail;

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(Integer businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getPoundageAmount() {
        return poundageAmount;
    }

    public void setPoundageAmount(String poundageAmount) {
        this.poundageAmount = poundageAmount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAllAmount() {
        return allAmount;
    }

    public void setAllAmount(String allAmount) {
        this.allAmount = allAmount;
    }

    public String getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(String accountAmount) {
        this.accountAmount = accountAmount;
    }

    public String getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(String freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public String getFlowDate() {
        return flowDate;
    }

    public void setFlowDate(String flowDate) {
        this.flowDate = flowDate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public String getUserRoleDisplay() {
        return userRoleDisplay;
    }

    public void setUserRoleDisplay(String userRoleDisplay) {
        this.userRoleDisplay = userRoleDisplay;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getLevelDisplay() {
        return levelDisplay;
    }

    public void setLevelDisplay(String levelDisplay) {
        this.levelDisplay = levelDisplay;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public Integer getBonusTotalPerson() {
        return bonusTotalPerson;
    }

    public void setBonusTotalPerson(Integer bonusTotalPerson) {
        this.bonusTotalPerson = bonusTotalPerson;
    }

    public String getFlowDetail() {
        return flowDetail;
    }

    public void setFlowDetail(String flowDetail) {
        this.flowDetail = flowDetail;
    }

    public Integer getOperAccount() {
        return operAccount;
    }

    public void setOperAccount(Integer operAccount) {
        this.operAccount = operAccount;
    }
}
