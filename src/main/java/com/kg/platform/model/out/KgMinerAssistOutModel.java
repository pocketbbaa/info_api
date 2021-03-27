package com.kg.platform.model.out;

import java.io.Serializable;
import java.util.Date;

/**
 * @author
 */
public class KgMinerAssistOutModel implements Serializable {
    /**
     * 助力ID
     */
    private Long assistId;

    /**
     * 抢单ID
     */
    private Long robId;

    /**
     * 助力用户ID
     */
    private Long userId;

    /**
     * 蓄能值
     */
    private Integer assistAmount;

    /**
     * 助力用户头像
     */
    private String assistAvatar;

    /**
     * 助力用户昵称
     */
    private String assistName;

    /**
     * 助力时间
     */
    private Date assistDate;

    /**
     * 助力状态 1：正常
     */
    private Integer assistStatus;

    /**
     * 助力人数
     */
    private Integer assistCount;

    private Long minerId;

    private static final long serialVersionUID = 1L;

    public Long getAssistId() {
        return assistId;
    }

    public void setAssistId(Long assistId) {
        this.assistId = assistId;
    }

    public Long getRobId() {
        return robId;
    }

    public void setRobId(Long robId) {
        this.robId = robId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAssistAvatar() {
        return assistAvatar;
    }

    public void setAssistAvatar(String assistAvatar) {
        this.assistAvatar = assistAvatar;
    }

    public String getAssistName() {
        return assistName;
    }

    public void setAssistName(String assistName) {
        this.assistName = assistName;
    }

    public Date getAssistDate() {
        return assistDate;
    }

    public void setAssistDate(Date assistDate) {
        this.assistDate = assistDate;
    }

    public Integer getAssistStatus() {
        return assistStatus;
    }

    public void setAssistStatus(Integer assistStatus) {
        this.assistStatus = assistStatus;
    }

    public Long getMinerId() {
        return minerId;
    }

    public void setMinerId(Long minerId) {
        this.minerId = minerId;
    }

    public Integer getAssistAmount() {
        return assistAmount;
    }

    public void setAssistAmount(Integer assistAmount) {
        this.assistAmount = assistAmount;
    }

    public Integer getAssistCount() {
        return assistCount;
    }

    public void setAssistCount(Integer assistCount) {
        this.assistCount = assistCount;
    }

}