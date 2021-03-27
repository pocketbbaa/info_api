package com.kg.platform.model.in;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author
 */
public class KgMinerAssistInModel implements Serializable {
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

    private Long minerId;

    // 总蓄力值
    private BigDecimal minerPrice;

    // 总蓄力人数
    private Integer assistNumber;

    // 已蓄力人数
    private Integer assistedNumber;

    // 正在蓄力人数
    private Integer nowAssitCount;

    // 已蓄力值
    private BigDecimal assistedAmount;

    private Integer start;

    private Integer limit;

    private static final long serialVersionUID = 1L;

    public Long getMinerId() {
        return minerId;
    }

    public void setMinerId(Long minerId) {
        this.minerId = minerId;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

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

    public Integer getAssistAmount() {
        return assistAmount;
    }

    public void setAssistAmount(Integer assistAmount) {
        this.assistAmount = assistAmount;
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

    public BigDecimal getMinerPrice() {
        return minerPrice;
    }

    public void setMinerPrice(BigDecimal minerPrice) {
        this.minerPrice = minerPrice;
    }

    public Integer getAssistNumber() {
        return assistNumber;
    }

    public void setAssistNumber(Integer assistNumber) {
        this.assistNumber = assistNumber;
    }

    public Integer getAssistedNumber() {
        return assistedNumber;
    }

    public void setAssistedNumber(Integer assistedNumber) {
        this.assistedNumber = assistedNumber;
    }

    public BigDecimal getAssistedAmount() {
        return assistedAmount;
    }

    public void setAssistedAmount(BigDecimal assistedAmount) {
        this.assistedAmount = assistedAmount;
    }

    public Integer getNowAssitCount() {
        return nowAssitCount;
    }

    public void setNowAssitCount(Integer nowAssitCount) {
        this.nowAssitCount = nowAssitCount;
    }

}