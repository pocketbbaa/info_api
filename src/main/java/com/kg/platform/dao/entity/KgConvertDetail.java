package com.kg.platform.dao.entity;

import java.io.Serializable;

/**
 * kg_convert_detail
 * @author 
 */
public class KgConvertDetail implements Serializable {
    private Long id;

    /**
     * 操作人用户名
     */
    private String userName;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户类型 0普通 1 专栏 -1系统参数设置
     */
    private Integer userType;

    /**
     * 操作人姓名
     */
    private String userNick;

    /**
     * 操作时间
     */
    private String updateTime;

    /**
     * 修改信息
     */
    private String updateInfo;

    /**
     * 操作类型 0 提现 1兑换 2系统参数设置
     */
    private Integer operationType;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
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
        KgConvertDetail other = (KgConvertDetail) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getUserType() == null ? other.getUserType() == null : this.getUserType().equals(other.getUserType()))
            && (this.getUserNick() == null ? other.getUserNick() == null : this.getUserNick().equals(other.getUserNick()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getUpdateInfo() == null ? other.getUpdateInfo() == null : this.getUpdateInfo().equals(other.getUpdateInfo()))
            && (this.getOperationType() == null ? other.getOperationType() == null : this.getOperationType().equals(other.getOperationType()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getUserType() == null) ? 0 : getUserType().hashCode());
        result = prime * result + ((getUserNick() == null) ? 0 : getUserNick().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateInfo() == null) ? 0 : getUpdateInfo().hashCode());
        result = prime * result + ((getOperationType() == null) ? 0 : getOperationType().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userName=").append(userName);
        sb.append(", userId=").append(userId);
        sb.append(", userType=").append(userType);
        sb.append(", userNick=").append(userNick);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updateInfo=").append(updateInfo);
        sb.append(", operationType=").append(operationType);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}