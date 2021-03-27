package com.kg.platform.dao.entity;

import java.util.Date;

public class Column {
    private Integer columnId;

    private Integer prevColumn;

    private String columnName;

    private String columnUrlname;

    private Integer navigatorDisplay;

    private Boolean displayStatus;

    private Integer columnOrder;

    private Integer displayMode;

    private String seoTitle;

    private String seoKeyword;

    private String seoDescription;

    private Date createDate;

    private Integer createUser;

    private Date updateDate;

    private Integer updateUser;

    private Integer columnLevel;

    public Integer getColumnId() {
        return columnId;
    }

    public String getColumnUrlname() {
        return columnUrlname;
    }

    public void setColumnUrlname(String columnUrlname) {
        this.columnUrlname = columnUrlname;
    }

    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
    }

    public Integer getPrevColumn() {
        return prevColumn;
    }

    public void setPrevColumn(Integer prevColumn) {
        this.prevColumn = prevColumn;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Integer getNavigatorDisplay() {
        return navigatorDisplay;
    }

    public void setNavigatorDisplay(Integer navigatorDisplay) {
        this.navigatorDisplay = navigatorDisplay;
    }

    public Boolean getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(Boolean displayStatus) {
        this.displayStatus = displayStatus;
    }

    public Integer getColumnOrder() {
        return columnOrder;
    }

    public void setColumnOrder(Integer columnOrder) {
        this.columnOrder = columnOrder;
    }

    public Integer getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(Integer displayMode) {
        this.displayMode = displayMode;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getSeoKeyword() {
        return seoKeyword;
    }

    public void setSeoKeyword(String seoKeyword) {
        this.seoKeyword = seoKeyword;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser) {
        this.updateUser = updateUser;
    }

    public Integer getColumnLevel() {
        return columnLevel;
    }

    public void setColumnLevel(Integer columnLevel) {
        this.columnLevel = columnLevel;
    }
}