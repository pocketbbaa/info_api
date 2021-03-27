package com.kg.platform.model.in;

import java.util.Date;

public class PrizeInModel {

    private Long lotteryId;

    private Long userId;

    private int prizeId;// 奖品id

    private String prizeName;// 奖品名称

    private int prizeAmount;// 奖品（剩余）数量

    private int prizeWeight;// 奖品权重

    private Date recieveTime;

    private int lotteryStatus;

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public int getPrizeAmount() {
        return prizeAmount;
    }

    public void setPrizeAmount(int prizeAmount) {
        this.prizeAmount = prizeAmount;
    }

    public int getPrizeWeight() {
        return prizeWeight;
    }

    public void setPrizeWeight(int prizeWeight) {
        this.prizeWeight = prizeWeight;
    }

    public Long getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(Long lotteryId) {
        this.lotteryId = lotteryId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(int prizeId) {
        this.prizeId = prizeId;
    }

    public Date getRecieveTime() {
        return recieveTime;
    }

    public void setRecieveTime(Date recieveTime) {
        this.recieveTime = recieveTime;
    }

    public int getLotteryStatus() {
        return lotteryStatus;
    }

    public void setLotteryStatus(int lotteryStatus) {
        this.lotteryStatus = lotteryStatus;
    }

}