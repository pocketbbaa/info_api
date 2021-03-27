package com.kg.platform.common.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

public class AsverageUserExportExcel extends BaseRowModel {

    @ExcelProperty(value = "用户ID", index = 0)
    private String userId;

    @ExcelProperty(value = "用户昵称", index = 1)
    private String userName;

    @ExcelProperty(value = "手机号码", index = 2)
    private String userPhone;

    @ExcelProperty(value = "注册时间", index = 3)
    private String createTime;

    @ExcelProperty(value = "师傅ID", index = 4)
    private String masterUserId;

    @ExcelProperty(value = "师傅昵称", index = 5)
    private String masterUserName;

    @ExcelProperty(value = "注册来源", index = 6)
    private String registFrom;

    @ExcelProperty(value = "渠道来源", index = 7)
    private String channel;

    @ExcelProperty(value = "活动渠道来源", index = 8)
    private String activityChannel;

    @ExcelProperty(value = "浏览增量", index = 9)
    private String browseNum;

    @ExcelProperty(value = "收藏增量", index = 10)
    private String collectNum;

    @ExcelProperty(value = "点赞增量", index = 11)
    private String thumbupNum;

    @ExcelProperty(value = "分享增量", index = 12)
    private String shareNum;

    @ExcelProperty(value = "评论增量", index = 13)
    private String commentNum;

    @ExcelProperty(value = "氪金可用余额总量", index = 14)
    private String kgBalance;

    @ExcelProperty(value = "RIT可用余额总量", index = 15)
    private String ritBalance;

    @ExcelProperty(value = "最后登陆时间", index = 16)
    private String lastLoginTime;

    @ExcelProperty(value = "最后一条浏览记录时间", index = 17)
    private String lastReadTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMasterUserId() {
        return masterUserId;
    }

    public void setMasterUserId(String masterUserId) {
        this.masterUserId = masterUserId;
    }

    public String getMasterUserName() {
        return masterUserName;
    }

    public void setMasterUserName(String masterUserName) {
        this.masterUserName = masterUserName;
    }

    public String getRegistFrom() {
        return registFrom;
    }

    public void setRegistFrom(String registFrom) {
        this.registFrom = registFrom;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getActivityChannel() {
        return activityChannel;
    }

    public void setActivityChannel(String activityChannel) {
        this.activityChannel = activityChannel;
    }

    public String getBrowseNum() {
        return browseNum;
    }

    public void setBrowseNum(String browseNum) {
        this.browseNum = browseNum;
    }

    public String getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(String collectNum) {
        this.collectNum = collectNum;
    }

    public String getThumbupNum() {
        return thumbupNum;
    }

    public void setThumbupNum(String thumbupNum) {
        this.thumbupNum = thumbupNum;
    }

    public String getShareNum() {
        return shareNum;
    }

    public void setShareNum(String shareNum) {
        this.shareNum = shareNum;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public String getKgBalance() {
        return kgBalance;
    }

    public void setKgBalance(String kgBalance) {
        this.kgBalance = kgBalance;
    }

    public String getRitBalance() {
        return ritBalance;
    }

    public void setRitBalance(String ritBalance) {
        this.ritBalance = ritBalance;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(String lastReadTime) {
        this.lastReadTime = lastReadTime;
    }
}
