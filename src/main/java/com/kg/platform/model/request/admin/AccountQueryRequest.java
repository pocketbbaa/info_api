package com.kg.platform.model.request.admin;

import java.util.Date;

/**
 * 交易记录入参
 */
public class AccountQueryRequest extends AdminBaseRequest {

    /**
     * 
     */
    private static final long serialVersionUID = -3856391458987099549L;

    private Long flowId;

    private Integer businessTypeId;

    private Date startDate;

    private Date endDate;

    private String minAmount;

    private String maxAmount;

    private String mobile;

    private String email;

    private String searchType;

    private String nickName;

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public Integer getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(Integer businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public String getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(String maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

}
