package com.kg.platform.model.request.admin;

import java.util.Date;

/**
 * 保证金管理查询入参
 */
public class AccountDipositQueryRequest extends AdminBaseRequest {

    /**
     * 
     */
    private static final long serialVersionUID = 7975394648179969703L;

    private String userId;

    private String mobile;

    private Date startDate;

    private Date endDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}
