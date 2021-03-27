package com.kg.platform.model.request.admin;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class AdminBaseRequest implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3061832223933662412L;

    private int limit;

    private int start;

    private int currentPage;

    private int pageSize;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
