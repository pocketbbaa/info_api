package com.kg.platform.model.request;

import java.io.Serializable;

/**
 * @author 
 */
public class KgCommonSettingRequest implements Serializable {
    /**
     * 配置ID
     */
    private Integer settingId;

    /**
     * 配置的key，通过key取相关配置信息
     */
    private String settingKey;

    /**
     * 配置信息（配置值）
     */
    private String settingValue;

    /**
     * 配置的描述
     */
    private String settingDesc;

    /**
     * 配置开关状态 0：关闭 1：开启
     */
    private Boolean settingStatus;

    private static final long serialVersionUID = 1L;

    public Integer getSettingId() {
        return settingId;
    }

    public void setSettingId(Integer settingId) {
        this.settingId = settingId;
    }

    public String getSettingKey() {
        return settingKey;
    }

    public void setSettingKey(String settingKey) {
        this.settingKey = settingKey;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }

    public String getSettingDesc() {
        return settingDesc;
    }

    public void setSettingDesc(String settingDesc) {
        this.settingDesc = settingDesc;
    }

    public Boolean getSettingStatus() {
        return settingStatus;
    }

    public void setSettingStatus(Boolean settingStatus) {
        this.settingStatus = settingStatus;
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
        KgCommonSettingRequest other = (KgCommonSettingRequest) that;
        return (this.getSettingId() == null ? other.getSettingId() == null : this.getSettingId().equals(other.getSettingId()))
            && (this.getSettingKey() == null ? other.getSettingKey() == null : this.getSettingKey().equals(other.getSettingKey()))
            && (this.getSettingValue() == null ? other.getSettingValue() == null : this.getSettingValue().equals(other.getSettingValue()))
            && (this.getSettingDesc() == null ? other.getSettingDesc() == null : this.getSettingDesc().equals(other.getSettingDesc()))
            && (this.getSettingStatus() == null ? other.getSettingStatus() == null : this.getSettingStatus().equals(other.getSettingStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSettingId() == null) ? 0 : getSettingId().hashCode());
        result = prime * result + ((getSettingKey() == null) ? 0 : getSettingKey().hashCode());
        result = prime * result + ((getSettingValue() == null) ? 0 : getSettingValue().hashCode());
        result = prime * result + ((getSettingDesc() == null) ? 0 : getSettingDesc().hashCode());
        result = prime * result + ((getSettingStatus() == null) ? 0 : getSettingStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", settingId=").append(settingId);
        sb.append(", settingKey=").append(settingKey);
        sb.append(", settingValue=").append(settingValue);
        sb.append(", settingDesc=").append(settingDesc);
        sb.append(", settingStatus=").append(settingStatus);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}