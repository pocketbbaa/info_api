package com.kg.platform.common.entity;

import java.io.Serializable;
import java.util.List;

public class QueryResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int total;

    private int current_page;

    private int total_page;

    private int page_size;

    private List<T> records;

    public QueryResult(int currentPage, int pageSize) {
        this.current_page = currentPage;
        this.page_size = pageSize;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
        this.total_page = (this.total % this.page_size == 0 ? this.total / this.page_size
                : this.total / this.page_size + 1);
    }

    public int getCurrentPage() {
        return this.current_page;
    }

    public long getTotalPage() {
        return this.total_page;
    }

    public List<T> getRecords() {
        return this.records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}
