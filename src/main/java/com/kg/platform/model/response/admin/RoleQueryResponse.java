package com.kg.platform.model.response.admin;

/**
 * 角色查询出参
 */
public class RoleQueryResponse extends AdminBaseResponse {

    /**
     * 
     */
    private static final long serialVersionUID = -3457452007584344269L;

    private Integer id;

    private String name;

    private Long userCount;

    private String levels;

    private Boolean status;

    private String statusDisplay;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserCount() {
        return userCount;
    }

    public void setUserCount(Long userCount) {
        this.userCount = userCount;
    }

    public String getLevels() {
        return levels;
    }

    public void setLevels(String levels) {
        this.levels = levels;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }
}
