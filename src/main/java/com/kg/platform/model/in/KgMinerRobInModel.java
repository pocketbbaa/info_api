package com.kg.platform.model.in;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class KgMinerRobInModel implements Serializable {
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

    private Date dealDate;

    private static final long serialVersionUID = 1L;

    public Date getDealDate() {
        return dealDate;
    }

    public void setDealDate(Date dealDate) {
        this.dealDate = dealDate;
    }

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
}