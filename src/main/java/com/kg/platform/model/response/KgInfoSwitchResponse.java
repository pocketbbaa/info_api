package com.kg.platform.model.response;

import java.io.Serializable;

/**
 * @author
 */
public class KgInfoSwitchResponse implements Serializable {
    private String userId;

    /**
     * 系统消息推送开关
     */
    private Integer systemInfoSwitch;

    /**
     * 动态消息推送开关
     */
    private Integer dynamicMessageSwitch;

    /**
     * 动态消息推送开关
     */
    private Integer bonusSwitch;

    /**
     * 热点资讯推送开关
     */
    private Integer hotNewsSwitch;

    /**
     * 预留字段
     */
    private Integer newsflashSwitch;

    private static final long serialVersionUID = 1L;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getSystemInfoSwitch() {
        return systemInfoSwitch;
    }

    public void setSystemInfoSwitch(Integer systemInfoSwitch) {
        this.systemInfoSwitch = systemInfoSwitch;
    }

    public Integer getHotNewsSwitch() {
        return hotNewsSwitch;
    }

    public void setHotNewsSwitch(Integer hotNewsSwitch) {
        this.hotNewsSwitch = hotNewsSwitch;
    }

    public Integer getNewsflashSwitch() {
        return newsflashSwitch;
    }

    public void setNewsflashSwitch(Integer newsflashSwitch) {
        this.newsflashSwitch = newsflashSwitch;
    }

    public Integer getDynamicMessageSwitch() {
        return dynamicMessageSwitch;
    }

    public void setDynamicMessageSwitch(Integer dynamicMessageSwitch) {
        this.dynamicMessageSwitch = dynamicMessageSwitch;
    }

    public Integer getBonusSwitch() {
        return bonusSwitch;
    }

    public void setBonusSwitch(Integer bonusSwitch) {
        this.bonusSwitch = bonusSwitch;
    }
}