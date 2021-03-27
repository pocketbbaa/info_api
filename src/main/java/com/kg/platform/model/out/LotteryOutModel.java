package com.kg.platform.model.out;

import java.util.Date;

public class LotteryOutModel {

    private Long lotteryId;// 奖品id

    private Long userId;// 奖品名称

    private int prizeId;// 奖品（剩余）数量

    private Date createTime;// 奖品权重

    private int activityId;

    private int lotteryStatus; // 领取状态 0 未中奖 1未领取 2已发放

    private Date recieveTime;// 获得时间

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getLotteryStatus() {
        return lotteryStatus;
    }

    public void setLotteryStatus(int lotteryStatus) {
        this.lotteryStatus = lotteryStatus;
    }

    public Date getRecieveTime() {
        return recieveTime;
    }

    public void setRecieveTime(Date recieveTime) {
        this.recieveTime = recieveTime;
    }

}