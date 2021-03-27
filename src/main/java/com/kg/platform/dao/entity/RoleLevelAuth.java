package com.kg.platform.dao.entity;

public class RoleLevelAuth {
    private Integer authId;

    private Integer roleId;

    private Integer levelId;

    private Byte authAction;

    private String authActionType;

    private String authActionName;

    private Integer actionTimes;

    private Boolean actionTimesType;

    public Integer getAuthId() {
        return authId;
    }

    public void setAuthId(Integer authId) {
        this.authId = authId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public Byte getAuthAction() {
        return authAction;
    }

    public void setAuthAction(Byte authAction) {
        this.authAction = authAction;
    }

    public String getAuthActionType() {
        return authActionType;
    }

    public void setAuthActionType(String authActionType) {
        this.authActionType = authActionType;
    }

    public String getAuthActionName() {
        return authActionName;
    }

    public void setAuthActionName(String authActionName) {
        this.authActionName = authActionName;
    }

    public Integer getActionTimes() {
        return actionTimes;
    }

    public void setActionTimes(Integer actionTimes) {
        this.actionTimes = actionTimes;
    }

    public Boolean getActionTimesType() {
        return actionTimesType;
    }

    public void setActionTimesType(Boolean actionTimesType) {
        this.actionTimesType = actionTimesType;
    }
}