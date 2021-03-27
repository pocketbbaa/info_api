package com.kg.platform.model.request;

public class SearchRequest {

    public static final int ARTICLE = 1;
    public static final int VIDEO = 2;
    public static final int USER = 3;

    private String searchStr;
    private Integer type; //1:资讯，2:视频，3:用户
    private Integer currentPage = 1;
    private Integer pageSize = 20;
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSearchStr() {
        return searchStr;
    }

    public void setSearchStr(String searchStr) {
        this.searchStr = searchStr;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
