package com.kg.platform.model.response.admin;

public class UserBonusQueryResponse extends AdminBaseResponse {

    /**
     * 
     */
    private static final long serialVersionUID = 7083294568222846398L;

    private String extraBonusId;

    private String totalTv;

    private String totalKg;

    private String  totalRitAmount;

    private String bonusReason;

    private String createTime;

    private String adminName;

    private Integer totalNum;

    public String getExtraBonusId() {
        return extraBonusId;
    }

    public void setExtraBonusId(String extraBonusId) {
        this.extraBonusId = extraBonusId;
    }

    public String getTotalTv() {
        return totalTv;
    }

    public void setTotalTv(String totalTv) {
        this.totalTv = totalTv;
    }

    public String getTotalKg() {
        return totalKg;
    }

    public void setTotalKg(String totalKg) {
        this.totalKg = totalKg;
    }

    public String getBonusReason() {
        return bonusReason;
    }

    public void setBonusReason(String bonusReason) {
        this.bonusReason = bonusReason;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public String getTotalRitAmount() {
        return totalRitAmount;
    }

    public void setTotalRitAmount(String totalRitAmount) {
        this.totalRitAmount = totalRitAmount;
    }
}
