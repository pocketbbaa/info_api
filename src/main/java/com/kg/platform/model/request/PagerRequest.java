package com.kg.platform.model.request;

/**
 * 分页参数
 * 
 * @author gavine
 *
 */
public class PagerRequest {

    private static final long serialVersionUID = -3397246794891369611L;

    /** 每页多少条数据 */
    private int pageSize = 5;

    /** 第几页 */
    private int currentPage = 1;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

}
