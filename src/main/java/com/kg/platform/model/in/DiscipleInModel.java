package com.kg.platform.model.in;

import java.io.Serializable;
import java.util.List;

public class DiscipleInModel extends PagerInModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4248067420339772018L;

    // 栏目
    private Integer columnType;

    private Long userId;

    // 时间排序
    private Integer orderByTime;

    // 进贡量排序
    private Integer orderByContribution;

    // 最后获取时间
    private String lastTime;

    private List<Long> subList;

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

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public List<Long> getSubList() {
        return subList;
    }

    public void setSubList(List<Long> subList) {
        this.subList = subList;
    }
}