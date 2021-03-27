package com.kg.platform.model.response.admin;

import java.util.Date;

/**
 * 系统账号查询出参
 */
public class SysUserQueryResponse extends AdminBaseResponse {

    /**
     * 
     */
    private static final long serialVersionUID = 1761614872460246083L;

    private String id;

    private String username;

    private String mobile;

    private String postName;

    private Date createDate;

    private Integer status;

    private String statusDisplay;

    private String kgUsername;

    private Integer postId;

    private String realname;

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }

    public String getKgUsername() {
        return kgUsername;
    }

    public void setKgUsername(String kgUsername) {
        this.kgUsername = kgUsername;
    }
}
