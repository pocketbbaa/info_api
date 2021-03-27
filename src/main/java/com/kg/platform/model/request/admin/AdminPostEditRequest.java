package com.kg.platform.model.request.admin;

/**
 * 添加/编辑岗位入参
 */
public class AdminPostEditRequest extends AdminBaseRequest {

    /**
     * 
     */
    private static final long serialVersionUID = 1718264879273122162L;

    private Integer postId;

    private String name;

    private String authIds;

    private Integer userId;

    private Boolean status;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthIds() {
        return authIds;
    }

    public void setAuthIds(String authIds) {
        this.authIds = authIds;
    }
}
