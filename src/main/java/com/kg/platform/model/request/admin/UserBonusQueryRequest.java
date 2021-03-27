package com.kg.platform.model.request.admin;

import java.math.BigDecimal;

public class UserBonusQueryRequest extends AdminBaseRequest {

    /**
     * 
     */
    private static final long serialVersionUID = 1218202388844625205L;

    private String extraBonusId;

    private String userId;

    private String nickName;

    private String userMobile;

    private BigDecimal startTvBonus;

    private BigDecimal endTvBonus;

    private BigDecimal startKgBonus;

    private BigDecimal endKgBonus;

    private BigDecimal startRitBonus;

    private BigDecimal  endRitBonus;

    private String startTime;

    private String endTime;

    private Integer numStart;

    private Integer numEnd;

    private String adminName;

    private String userMobiles;

    private String userIds;

    private Integer adminId;

    private BigDecimal tvBonus;

    private BigDecimal kgBonus;

    private BigDecimal bonus;

    private String bonusReason;

   ////0是RIT（余额），1是RIT（冻结），2是KG
    private Integer awardType;

    public String getExtraBonusId() {
        return extraBonusId;
    }

    public void setExtraBonusId(String extraBonusId) {
        this.extraBonusId = extraBonusId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public BigDecimal getStartTvBonus() {
        return startTvBonus;
    }

    public void setStartTvBonus(BigDecimal startTvBonus) {
        this.startTvBonus = startTvBonus;
    }

    public BigDecimal getEndTvBonus() {
        return endTvBonus;
    }

    public void setEndTvBonus(BigDecimal endTvBonus) {
        this.endTvBonus = endTvBonus;
    }

    public BigDecimal getStartKgBonus() {
        return startKgBonus;
    }

    public void setStartKgBonus(BigDecimal startKgBonus) {
        this.startKgBonus = startKgBonus;
    }

    public BigDecimal getEndKgBonus() {
        return endKgBonus;
    }

    public void setEndKgBonus(BigDecimal endKgBonus) {
        this.endKgBonus = endKgBonus;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getNumStart() {
        return numStart;
    }

    public void setNumStart(Integer numStart) {
        this.numStart = numStart;
    }

    public Integer getNumEnd() {
        return numEnd;
    }

    public void setNumEnd(Integer numEnd) {
        this.numEnd = numEnd;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getUserMobiles() {
        return userMobiles;
    }

    public void setUserMobiles(String userMobiles) {
        this.userMobiles = userMobiles;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public BigDecimal getTvBonus() {
        return tvBonus;
    }

    public void setTvBonus(BigDecimal tvBonus) {
        this.tvBonus = tvBonus;
    }

    public BigDecimal getKgBonus() {
        return kgBonus;
    }

    public void setKgBonus(BigDecimal kgBonus) {
        this.kgBonus = kgBonus;
    }

    public String getBonusReason() {
        return bonusReason;
    }

    public void setBonusReason(String bonusReason) {
        this.bonusReason = bonusReason;
    }

    public Integer getAwardType() {
        return awardType;
    }

    public void setAwardType(Integer awardType) {
        this.awardType = awardType;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }


    public BigDecimal getStartRitBonus() {
        return startRitBonus;
    }

    public void setStartRitBonus(BigDecimal startRitBonus) {
        this.startRitBonus = startRitBonus;
    }

    public BigDecimal getEndRitBonus() {
        return endRitBonus;
    }

    public void setEndRitBonus(BigDecimal endRitBonus) {
        this.endRitBonus = endRitBonus;
    }


}
