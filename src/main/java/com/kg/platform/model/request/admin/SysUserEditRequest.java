package com.kg.platform.model.request.admin;

/**
 * 添加/编辑系统账号入参
 */
public class SysUserEditRequest extends AdminBaseRequest {

    /**
     * 
     */
    private static final long serialVersionUID = -5692198454803556374L;

    private Long userId;

    private String username;

    private String mobile;

    private String realname;

    private String password;

    private Integer postId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }
}
