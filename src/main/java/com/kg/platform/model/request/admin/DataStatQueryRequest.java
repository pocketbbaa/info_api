package com.kg.platform.model.request.admin;

import java.util.Date;

/**
 * 数据统计入参
 */
public class DataStatQueryRequest extends AdminBaseRequest {

    /**
     * 
     */
    private static final long serialVersionUID = 7141425858674761777L;

    private Integer time;// 1、最近三天2、最近7天3、最近30天

    private Integer type;// 1、用户数据2、专栏数据

    private Date startDate;

    private Date endDate;

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

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
