package com.kg.platform.model.response;

import com.kg.platform.dao.entity.KgRitConvert;

import java.math.BigDecimal;

/**
 * KG兑换RIT页面数据
 */
public class ConvertIndexResponse {

    private Integer isConvertStart; //是否已开启兑换 1:是，0：否
    private String startTime; //开启兑换时间

    private Integer isConvertOver; //兑换额度是否已抢完 1:是 ，0：否

    private String kgBalance; //氪金余额
    private String kgRate; //氪金汇率
    private String ritRate; //RIT汇率
    private String convertRitTodayTotal; //今日可兑换RIT总量
    private String convertCount; //每日兑换次数

    /**
     * 计算当前可兑换RIT
     *
     * @param kgBalance
     * @return
     */
    public ConvertIndexResponse buildConvertRit(BigDecimal kgBalance) {
        this.kgBalance = kgBalance.stripTrailingZeros().toPlainString();
        return this;
    }

    /**
     * 获取当前汇率
     *
     * @param kgToRitRateArry
     * @return
     */
    public ConvertIndexResponse buildRate(String[] kgToRitRateArry) {
        String kgRate = kgToRitRateArry[0];
        String ritRate = kgToRitRateArry[1];
        this.kgRate = kgRate;
        this.ritRate = ritRate;
        return this;
    }

    /**
     * 构建兑换总量和次数
     *
     * @param kgRitConvert
     * @return
     */
    public ConvertIndexResponse buildConvertCountSet(KgRitConvert kgRitConvert) {
        String convertRitTodayTotal = String.valueOf(kgRitConvert.getDayLimit());
        String convertCount = String.valueOf(kgRitConvert.getDayCnt());
        this.convertRitTodayTotal = convertRitTodayTotal;
        this.convertCount = convertCount;
        return this;
    }

    public Integer getIsConvertOver() {
        return isConvertOver;
    }

    public void setIsConvertOver(Integer isConvertOver) {
        this.isConvertOver = isConvertOver;
    }

    public Integer getIsConvertStart() {
        return isConvertStart;
    }

    public void setIsConvertStart(Integer isConvertStart) {
        this.isConvertStart = isConvertStart;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getKgBalance() {
        return kgBalance;
    }

    public void setKgBalance(String kgBalance) {
        this.kgBalance = kgBalance;
    }

    public String getKgRate() {
        return kgRate;
    }

    public void setKgRate(String kgRate) {
        this.kgRate = kgRate;
    }

    public String getRitRate() {
        return ritRate;
    }

    public void setRitRate(String ritRate) {
        this.ritRate = ritRate;
    }

    public String getConvertRitTodayTotal() {
        return convertRitTodayTotal;
    }

    public void setConvertRitTodayTotal(String convertRitTodayTotal) {
        this.convertRitTodayTotal = convertRitTodayTotal;
    }

    public String getConvertCount() {
        return convertCount;
    }

    public void setConvertCount(String convertCount) {
        this.convertCount = convertCount;
    }
}
