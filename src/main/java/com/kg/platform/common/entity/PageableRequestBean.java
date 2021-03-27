package com.kg.platform.common.entity;

import java.io.Serializable;

public class PageableRequestBean implements Serializable {

    private static final long serialVersionUID = 2643472641793501437L;

    private static final int DEFAULT_PAGESIZE = 20;

    private int currentPage = 1;

    private int pageSize = DEFAULT_PAGESIZE;

    public int getCurrentPage() {
        return this.currentPage < 1 ? 1 : this.currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageIndex() {
        return (getCurrentPage() - 1) * getPageSize();
    }

    public int getPageSize() {
        return this.pageSize <= 0 ? DEFAULT_PAGESIZE : this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
