package com.kg.platform.model.out;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 
 */
public class KgMinerRobOutModel implements Serializable {
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

    /**
     * 矿机名
     */
    private String minerName;

    /**
     * 矿机价格
     */
    private BigDecimal minerPrice;
    /**
     * 剩余数量
     */
    private Integer minerNumber;

    /**
     * 活动结束时间
     */
    private Date minerEndDate;
    /**
     * 蓄能值
     */
    private Integer assistAmount;
    /**
     * 矿机主图地址
     */
    private String minerPhoto;

    private Date dealDate;

    private static final long serialVersionUID = 1L;

    public Date getDealDate() {
        return dealDate;
    }

    public void setDealDate(Date dealDate) {
        this.dealDate = dealDate;
    }

    public String getMinerPhoto() {
        return minerPhoto;
    }

    public void setMinerPhoto(String minerPhoto) {
        this.minerPhoto = minerPhoto;
    }

    public String getMinerName() {
        return minerName;
    }

    public void setMinerName(String minerName) {
        this.minerName = minerName;
    }

    public BigDecimal getMinerPrice() {
        return minerPrice;
    }

    public void setMinerPrice(BigDecimal minerPrice) {
        this.minerPrice = minerPrice;
    }

    public Integer getMinerNumber() {
        return minerNumber;
    }

    public void setMinerNumber(Integer minerNumber) {
        this.minerNumber = minerNumber;
    }

    public Date getMinerEndDate() {
        return minerEndDate;
    }

    public void setMinerEndDate(Date minerEndDate) {
        this.minerEndDate = minerEndDate;
    }

    public Integer getAssistAmount() {
        return assistAmount;
    }

    public void setAssistAmount(Integer assistAmount) {
        this.assistAmount = assistAmount;
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