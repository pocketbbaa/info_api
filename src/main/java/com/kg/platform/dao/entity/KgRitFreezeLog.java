package com.kg.platform.dao.entity;

import java.io.Serializable;

/**
 * kg_rit_freeze_log
 * @author 
 */
public class KgRitFreezeLog implements Serializable {
    private Long id;

    /**
     * 操作者userId
     */
    private Long operUserId;

    /**
     * 被冻结/解冻用户id
     */
    private Long userId;

    /**
     * 冻结数量
     */
    private Long freezeCnt;

    /**
     * 操作类型 0冻结 1解冻
     */
    private Integer type;

    /**
     * 原因
     */
    private String cause;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOperUserId() {
        return operUserId;
    }

    public void setOperUserId(Long operUserId) {
        this.operUserId = operUserId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFreezeCnt() {
        return freezeCnt;
    }

    public void setFreezeCnt(Long freezeCnt) {
        this.freezeCnt = freezeCnt;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
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
        KgRitFreezeLog other = (KgRitFreezeLog) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOperUserId() == null ? other.getOperUserId() == null : this.getOperUserId().equals(other.getOperUserId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getFreezeCnt() == null ? other.getFreezeCnt() == null : this.getFreezeCnt().equals(other.getFreezeCnt()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getCause() == null ? other.getCause() == null : this.getCause().equals(other.getCause()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOperUserId() == null) ? 0 : getOperUserId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getFreezeCnt() == null) ? 0 : getFreezeCnt().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getCause() == null) ? 0 : getCause().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", operUserId=").append(operUserId);
        sb.append(", userId=").append(userId);
        sb.append(", freezeCnt=").append(freezeCnt);
        sb.append(", type=").append(type);
        sb.append(", cause=").append(cause);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}