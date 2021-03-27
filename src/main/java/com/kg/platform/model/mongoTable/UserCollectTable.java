package com.kg.platform.model.mongoTable;

import java.math.BigDecimal;

public class UserCollectTable {

    private Long _id;

    // 操作类型 1 收藏 2 点赞 3 分享 4 浏览
    private Integer operType;

    // 创建时间
    private String createDate;

    // 平台给文章作者奖励
    private BigDecimal articleBonus;

    // 平台给读者奖励
    private BigDecimal readerBonus;

    // 用户ID
    private Long userId;

    // app设备ID
    private String deviceId;

    // 来源 1 IOS 2 Android 3 h5 4 pc
    private Integer platfrom;

    // 文章ID
    private Long articleId;

    // 状态 是否已经领取奖励 0未领取 1已领取
    private Integer bonusStatus;

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Integer getOperType() {
        return operType;
    }

    public void setOperType(Integer operType) {
        this.operType = operType;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public BigDecimal getArticleBonus() {
        return articleBonus;
    }

    public void setArticleBonus(BigDecimal articleBonus) {
        this.articleBonus = articleBonus;
    }

    public BigDecimal getReaderBonus() {
        return readerBonus;
    }

    public void setReaderBonus(BigDecimal readerBonus) {
        this.readerBonus = readerBonus;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getPlatfrom() {
        return platfrom;
    }

    public void setPlatfrom(Integer platfrom) {
        this.platfrom = platfrom;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Integer getBonusStatus() {
        return bonusStatus;
    }

    public void setBonusStatus(Integer bonusStatus) {
        this.bonusStatus = bonusStatus;
    }

}