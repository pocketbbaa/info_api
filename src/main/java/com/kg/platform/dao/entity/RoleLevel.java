package com.kg.platform.dao.entity;

public class RoleLevel {
    private Integer roleLevelId;

    private Integer roleId;

    private String levelName;

    private String levelInfo;

    private Boolean levelStatus;

    public Integer getRoleLevelId() {
        return roleLevelId;
    }

    public void setRoleLevelId(Integer roleLevelId) {
        this.roleLevelId = roleLevelId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getLevelInfo() {
        return levelInfo;
    }

    public void setLevelInfo(String levelInfo) {
        this.levelInfo = levelInfo;
    }

    public Boolean getLevelStatus() {
        return levelStatus;
    }

    public void setLevelStatus(Boolean levelStatus) {
        this.levelStatus = levelStatus;
    }
}