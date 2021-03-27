package com.kg.platform.model.response.admin;

/**
 * 岗位查询出参
 */
public class PostQueryResponse extends AdminBaseResponse {

    /**
     * 
     */
    private static final long serialVersionUID = -7044975994934177046L;

    private Integer postId;

    private String auth;

    private Boolean status;

    private String name;

    private String statusDisplay;

    private String authIds;

    public String getAuthIds() {
        return authIds;
    }

    public void setAuthIds(String authIds) {
        this.authIds = authIds;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
