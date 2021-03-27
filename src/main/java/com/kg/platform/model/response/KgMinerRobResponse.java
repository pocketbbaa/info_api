package com.kg.platform.model.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 
 */
public class KgMinerRobResponse implements Serializable {
    /**
     * 抢单ID
     */
    private String robId;

    /**
     * 矿机ID
     */
    private String minerId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 助力码
     */
    private String assistCode;

    /**
     * 抢单时间
     */
    private Date robDate;

    /**
     * 抢单状态  0无效 1.已抢到 2.未抢到
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
    private String minerPrice;
    /**
     * 剩余数量
     */
    private Integer minerNumber;

    /**
     * 到活动结束的剩余时间
     */
    private String remainTime;
    /**
     * 蓄能值
     */
    private Integer assistAmount;
    /**
     * 矿机主图地址
     */
    private String minerPhoto;

    private String minerLink;

    private Integer minerJoinFrequency;

    private String minerDesc;

    private static final long serialVersionUID = 1L;

    public String getMinerDesc() {
        return minerDesc;
    }

    public void setMinerDesc(String minerDesc) {
        this.minerDesc = minerDesc;
    }

    public Integer getMinerJoinFrequency() {
        return minerJoinFrequency;
    }

    public void setMinerJoinFrequency(Integer minerJoinFrequency) {
        this.minerJoinFrequency = minerJoinFrequency;
    }

    public String getMinerLink() {
        return minerLink;
    }

    public void setMinerLink(String minerLink) {
        this.minerLink = minerLink;
    }

    public String getRobId() {
        return robId;
    }

    public void setRobId(String robId) {
        this.robId = robId;
    }

    public String getMinerId() {
        return minerId;
    }

    public void setMinerId(String minerId) {
        this.minerId = minerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMinerName() {
        return minerName;
    }

    public void setMinerName(String minerName) {
        this.minerName = minerName;
    }

    public String getMinerPrice() {
        return minerPrice;
    }

    public void setMinerPrice(String minerPrice) {
        this.minerPrice = minerPrice;
    }

    public Integer getMinerNumber() {
        return minerNumber;
    }

    public void setMinerNumber(Integer minerNumber) {
        this.minerNumber = minerNumber;
    }

    public String getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(String remainTime) {
        this.remainTime = remainTime;
    }

    public Integer getAssistAmount() {
        return assistAmount;
    }

    public void setAssistAmount(Integer assistAmount) {
        this.assistAmount = assistAmount;
    }

    public String getMinerPhoto() {
        return minerPhoto;
    }

    public void setMinerPhoto(String minerPhoto) {
        this.minerPhoto = minerPhoto;
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