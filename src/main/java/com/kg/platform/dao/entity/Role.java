package com.kg.platform.dao.entity;

public class Role {
    private Integer roleId;

    private String roleName;

    private Boolean roleStatus;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Boolean getRoleStatus() {
        return roleStatus;
    }

    public void setRoleStatus(Boolean roleStatus) {
        this.roleStatus = roleStatus;
    }
}