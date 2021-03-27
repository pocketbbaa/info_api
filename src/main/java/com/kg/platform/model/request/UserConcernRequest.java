package com.kg.platform.model.request;

import java.io.Serializable;
import java.util.Date;

public class UserConcernRequest implements Serializable{

    private  Long  concernId;

    private  Long  userId;

    private  String  concernUserId;

    private Integer  concernStatus;

    private Integer currentPage ;

    private Integer pageSize;

    private String searchStr;

    public String getSearchStr() {
        return searchStr;
    }

    public void setSearchStr(String searchStr) {
        this.searchStr = searchStr;
    }

    public void setConcernUserId(String concernUserId) {
        this.concernUserId = concernUserId;
    }

    private Date createDate;

    public Long getConcernId() {
        return concernId;
    }

    public void setConcernId(Long concernId) {
        this.concernId = concernId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getConcernStatus() {
        return concernStatus;
    }

    public void setConcernStatus(Integer concernStatus) {
        this.concernStatus = concernStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getConcernUserId() {
        return concernUserId;
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