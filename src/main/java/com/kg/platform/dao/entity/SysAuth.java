package com.kg.platform.dao.entity;

public class SysAuth {
    private Integer sysAuthId;

    private String sysAuthName;

    private Integer menuId;

    private Integer prvAuthId;

    private String operationType;

    public Integer getSysAuthId() {
        return sysAuthId;
    }

    public void setSysAuthId(Integer sysAuthId) {
        this.sysAuthId = sysAuthId;
    }

    public String getSysAuthName() {
        return sysAuthName;
    }

    public void setSysAuthName(String sysAuthName) {
        this.sysAuthName = sysAuthName;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getPrvAuthId() {
        return prvAuthId;
    }

    public void setPrvAuthId(Integer prvAuthId) {
        this.prvAuthId = prvAuthId;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}