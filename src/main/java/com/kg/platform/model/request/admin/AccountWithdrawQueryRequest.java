package com.kg.platform.model.request.admin;

import java.util.Date;

/**
 * 提币管理查询入参
 */
public class AccountWithdrawQueryRequest extends AdminBaseRequest {

    /**
     * 
     */
    private static final long serialVersionUID = -1132405783079444189L;

    private String email;

    private String mobile;

    private Date startDate;

    private Date endDate;

    private Integer status;

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
