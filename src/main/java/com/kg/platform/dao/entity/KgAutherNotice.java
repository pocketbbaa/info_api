package com.kg.platform.dao.entity;

import java.io.Serializable;

/**
 * kg_auther_notice
 * @author
 */
public class KgAutherNotice implements Serializable {
    /**
     * 唯一标识
     */
    private Long id;

    /**
     * 添加时间
     */
    private String addTime;

    /**
     * 公告内容
     */
    private String info;

    /**
     * 添加公告用户
     */
    private String addUser;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 添加时间的时间戳
     */
    private Long time;

    /**
     * 标题
     */
    private String title;

    /**
     * 更新公告用户
     */
    private String updateUser;

    /**
     * 用户ID
     */
    private String userId;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getAddUser() {
        return addUser;
    }

    public void setAddUser(String addUser) {
        this.addUser = addUser;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        KgAutherNotice other = (KgAutherNotice) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getAddTime() == null ? other.getAddTime() == null : this.getAddTime().equals(other.getAddTime()))
                && (this.getInfo() == null ? other.getInfo() == null : this.getInfo().equals(other.getInfo()))
                && (this.getAddUser() == null ? other.getAddUser() == null : this.getAddUser().equals(other.getAddUser()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getTime() == null ? other.getTime() == null : this.getTime().equals(other.getTime()))
                && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
                && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
                && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAddTime() == null) ? 0 : getAddTime().hashCode());
        result = prime * result + ((getInfo() == null) ? 0 : getInfo().hashCode());
        result = prime * result + ((getAddUser() == null) ? 0 : getAddUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getTime() == null) ? 0 : getTime().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", addTime=").append(addTime);
        sb.append(", info=").append(info);
        sb.append(", addUser=").append(addUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", time=").append(time);
        sb.append(", title=").append(title);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", userId=").append(userId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

}

