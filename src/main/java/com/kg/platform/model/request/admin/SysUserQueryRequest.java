package com.kg.platform.model.request.admin;

/**
 * 系统账号查询条件入参
 */
public class SysUserQueryRequest extends AdminBaseRequest {

    /**
     * 
     */
    private static final long serialVersionUID = -2740900290414764677L;

    private String username;

    private String mobile;

    private Integer postId;

    private Long userId;

    private String password;

    private Boolean status;

    private String kgUserId;

    public String getKgUserId() {
        return kgUserId;
    }

    public void setKgUserId(String kgUserId) {
        this.kgUserId = kgUserId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }
}
