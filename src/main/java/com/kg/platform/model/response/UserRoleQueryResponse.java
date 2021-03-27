package com.kg.platform.model.response;

import com.kg.platform.model.response.admin.AdminBaseResponse;

public class UserRoleQueryResponse extends AdminBaseResponse {

    /**
     * 
     */
    private static final long serialVersionUID = 5662255524896844341L;

    private Long roleId;

    private String roleName;

    private Long userCount;// 包含用户数

    private String level;// 多个等级以逗号隔开

    private Boolean levelStatus;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getUserCount() {
        return userCount;
    }

    public void setUserCount(Long userCount) {
        this.userCount = userCount;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Boolean getLevelStatus() {
        return levelStatus;
    }

    public void setLevelStatus(Boolean levelStatus) {
        this.levelStatus = levelStatus;
    }
}
