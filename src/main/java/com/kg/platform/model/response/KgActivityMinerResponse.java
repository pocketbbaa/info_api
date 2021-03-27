package com.kg.platform.model.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 
 */
public class KgActivityMinerResponse implements Serializable {
    /**
     * 矿机ID
     */
    private String minerId;

    /**
     * 矿机名
     */
    private String minerName;

    /**
     * 矿机价格
     */
    private String minerPrice;

    /**
     * 需助力人数（不包括自己）
     */
    private Integer minerAssistFrequency;

    /**
     * 参与人数
     */
    private Integer minerJoinFrequency;

    /**
     * 剩余数量
     */
    private Integer minerNumber;

    /**
     * 活动开始时间
     */
    private Date minerStartDate;

    /**
     * 活动结束时间
     */
    private Date minerEndDate;

    /**
     * 矿机简介
     */
    private String minerDesc;

    /**
     * 矿机购买链接
     */
    private String minerLink;

    /**
     * 矿机主图地址
     */
    private String minerPhoto;

    private Long robedNumber;

    private static final long serialVersionUID = 1L;

    public Long getRobedNumber() {
        return robedNumber;
    }

    public void setRobedNumber(Long robedNumber) {
        this.robedNumber = robedNumber;
    }

    public String getMinerId() {
        return minerId;
    }

    public void setMinerId(String minerId) {
        this.minerId = minerId;
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

    public Integer getMinerAssistFrequency() {
        return minerAssistFrequency;
    }

    public void setMinerAssistFrequency(Integer minerAssistFrequency) {
        this.minerAssistFrequency = minerAssistFrequency;
    }

    public Integer getMinerJoinFrequency() {
        return minerJoinFrequency;
    }

    public void setMinerJoinFrequency(Integer minerJoinFrequency) {
        this.minerJoinFrequency = minerJoinFrequency;
    }

    public Integer getMinerNumber() {
        return minerNumber;
    }

    public void setMinerNumber(Integer minerNumber) {
        this.minerNumber = minerNumber;
    }

    public Date getMinerStartDate() {
        return minerStartDate;
    }

    public void setMinerStartDate(Date minerStartDate) {
        this.minerStartDate = minerStartDate;
    }

    public Date getMinerEndDate() {
        return minerEndDate;
    }

    public void setMinerEndDate(Date minerEndDate) {
        this.minerEndDate = minerEndDate;
    }

    public String getMinerDesc() {
        return minerDesc;
    }

    public void setMinerDesc(String minerDesc) {
        this.minerDesc = minerDesc;
    }

    public String getMinerLink() {
        return minerLink;
    }

    public void setMinerLink(String minerLink) {
        this.minerLink = minerLink;
    }

    public String getMinerPhoto() {
        return minerPhoto;
    }

    public void setMinerPhoto(String minerPhoto) {
        this.minerPhoto = minerPhoto;
    }
}