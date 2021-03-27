package com.kg.platform.model.request;

import java.io.Serializable;

public class DiscipleRequest extends PagerRequest implements Serializable {

    private static final long serialVersionUID = 7732911890866398499L;

    // 栏目
    private Integer columnType;

    private Long userId;

    // 时间排序
    private Integer orderByTime;

    // 进贡量排序
    private Integer orderByContribution;

    public Integer getColumnType() {
        return columnType;
    }

    public void setColumnType(Integer columnType) {
        this.columnType = columnType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getOrderByTime() {
        return orderByTime;
    }

    public void setOrderByTime(Integer orderByTime) {
        this.orderByTime = orderByTime;
    }

    public Integer getOrderByContribution() {
        return orderByContribution;
    }

    public void setOrderByContribution(Integer orderByContribution) {
        this.orderByContribution = orderByContribution;
    }

}