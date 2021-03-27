package com.kg.platform.model.out;

import java.io.Serializable;

/**
 * @author
 */
public class KgInfoSwitchOutModel implements Serializable {
    private Long userId;

    /**
     * 系统消息推送开关 0
     */
    private Integer systemInfoSwitch;

    /**
     * 奖励推送开关 1
     */
    private Integer bonusSwitch;

    /**
     * 动态消息推送开关 2
     */
    private Integer dynamicMessageSwitch;

    /**
     * 快讯更新推送开关 3
     */
    private Integer newsflashSwitch;

    /**
     * 热点资讯推送开关 4
     */
    private Integer hotNewsSwitch;


    public Integer switchResult(int classify) {
        if (classify == 1) {
            return this.bonusSwitch;
        }
        if (classify == 2) {
            return this.dynamicMessageSwitch;
        }
        if (classify == 3) {
            return this.newsflashSwitch;
        }
        if (classify == 4) {
            return this.hotNewsSwitch;
        }
        return null;
    }

    private static final long serialVersionUID = 1L;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getSystemInfoSwitch() {
        return systemInfoSwitch;
    }

    public void setSystemInfoSwitch(Integer systemInfoSwitch) {
        this.systemInfoSwitch = systemInfoSwitch;
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

}