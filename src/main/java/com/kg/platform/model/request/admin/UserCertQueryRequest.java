package com.kg.platform.model.request.admin;

import java.util.Date;

/**
 * 实名认证查询入参
 */
public class UserCertQueryRequest extends AdminBaseRequest {

    /**
     * 
     */
    private static final long serialVersionUID = 4812624957512466122L;

    private String userId;

    private String email;

    private String mobile;

    private Date startDate;

    private Date endDate;

    private Integer status;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
