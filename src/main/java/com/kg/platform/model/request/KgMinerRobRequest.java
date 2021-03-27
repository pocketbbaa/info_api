package com.kg.platform.model.request;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class KgMinerRobRequest implements Serializable {
    /**
     * 抢单ID
     */
    private Long robId;

    /**
     * 矿机ID
     */
    private Long minerId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 助力码
     */
    private String assistCode;

    /**
     * 抢单时间
     */
    private Date robDate;

    /**
     * 抢单状态 1：正常
     */
    private Integer robStatus;

    /**
     * 抢购用户头像
     */
    private String robAvatar;

    /**
     * 抢购用户昵称
     */
    private String robName;

    private static final long serialVersionUID = 1L;

    public Long getRobId() {
        return robId;
    }

    public void setRobId(Long robId) {
        this.robId = robId;
    }

    public Long getMinerId() {
        return minerId;
    }

    public void setMinerId(Long minerId) {
        this.minerId = minerId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAssistCode() {
        return assistCode;
    }

    public void setAssistCode(String assistCode) {
        this.assistCode = assistCode;
    }

    public Date getRobDate() {
        return robDate;
    }

    public void setRobDate(Date robDate) {
        this.robDate = robDate;
    }

    public Integer getRobStatus() {
        return robStatus;
    }

    public void setRobStatus(Integer robStatus) {
        this.robStatus = robStatus;
    }

    public String getRobAvatar() {
        return robAvatar;
    }

    public void setRobAvatar(String robAvatar) {
        this.robAvatar = robAvatar;
    }

    public String getRobName() {
        return robName;
    }

    public void setRobName(String robName) {
        this.robName = robName;
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
        KgMinerRobRequest other = (KgMinerRobRequest) that;
        return (this.getRobId() == null ? other.getRobId() == null : this.getRobId().equals(other.getRobId()))
            && (this.getMinerId() == null ? other.getMinerId() == null : this.getMinerId().equals(other.getMinerId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getAssistCode() == null ? other.getAssistCode() == null : this.getAssistCode().equals(other.getAssistCode()))
            && (this.getRobDate() == null ? other.getRobDate() == null : this.getRobDate().equals(other.getRobDate()))
            && (this.getRobStatus() == null ? other.getRobStatus() == null : this.getRobStatus().equals(other.getRobStatus()))
            && (this.getRobAvatar() == null ? other.getRobAvatar() == null : this.getRobAvatar().equals(other.getRobAvatar()))
            && (this.getRobName() == null ? other.getRobName() == null : this.getRobName().equals(other.getRobName()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRobId() == null) ? 0 : getRobId().hashCode());
        result = prime * result + ((getMinerId() == null) ? 0 : getMinerId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getAssistCode() == null) ? 0 : getAssistCode().hashCode());
        result = prime * result + ((getRobDate() == null) ? 0 : getRobDate().hashCode());
        result = prime * result + ((getRobStatus() == null) ? 0 : getRobStatus().hashCode());
        result = prime * result + ((getRobAvatar() == null) ? 0 : getRobAvatar().hashCode());
        result = prime * result + ((getRobName() == null) ? 0 : getRobName().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", robId=").append(robId);
        sb.append(", minerId=").append(minerId);
        sb.append(", userId=").append(userId);
        sb.append(", assistCode=").append(assistCode);
        sb.append(", robDate=").append(robDate);
        sb.append(", robStatus=").append(robStatus);
        sb.append(", robAvatar=").append(robAvatar);
        sb.append(", robName=").append(robName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}