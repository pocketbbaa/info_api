package com.kg.platform.dao.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 
 */
public class CloudPackage implements Serializable {
    private Integer id;

    /**
     * 套餐名
     */
    private String name;

    /**
     * 每份单价（元）
     */
    private Double price;

    /**
     * 起投额（元）
     */
    private Double castLines;

    /**
     * 每份算力(T)
     */
    private Double performance;

    /**
     * 预计年收益率
     */
    private Double yearsEarningsPercent;

    /**
     * 产品期限(天)
     */
    private Integer timeLimit;

    /**
     * 是否包电费(0：否，1：是)
     */
    private Integer haveElectricPrice;

    /**
     * 日耗电度数
     */
    private Double dailyElectricPower;

    /**
     * 每度电费(元)
     */
    private Double electricPrice;

    /**
     * 是否包其他费用(0：否，1：是)
     */
    private Integer haveOtherPrice;

    /**
     * 其他费用名称
     */
    private String otherPriceName;

    /**
     * 分类（1：高回报，2：低收益）
     */
    private Integer classify;

    /**
     * 类型（1：标准版，2：加速版，3：尝鲜版，4：升级版）
     */
    private Integer type;

    /**
     * 日收益（BTC/份）
     */
    private BigDecimal dailyEarningsBtc;

    /**
     * 日收益（元/份）
     */
    private Double dailyEarningsRmb;

    private Date createTime;

    private Date updateTime;

    /**
     * 状态（0：删除，1：正常）
     */
    private Integer state;

    /**
     * 备注
     */
    private String remark;

    private static final long serialVersionUID = 1L;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getCastLines() {
        return castLines;
    }

    public void setCastLines(Double castLines) {
        this.castLines = castLines;
    }

    public Double getPerformance() {
        return performance;
    }

    public void setPerformance(Double performance) {
        this.performance = performance;
    }

    public Double getYearsEarningsPercent() {
        return yearsEarningsPercent;
    }

    public void setYearsEarningsPercent(Double yearsEarningsPercent) {
        this.yearsEarningsPercent = yearsEarningsPercent;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Integer getHaveElectricPrice() {
        return haveElectricPrice;
    }

    public void setHaveElectricPrice(Integer haveElectricPrice) {
        this.haveElectricPrice = haveElectricPrice;
    }

    public Double getDailyElectricPower() {
        return dailyElectricPower;
    }

    public void setDailyElectricPower(Double dailyElectricPower) {
        this.dailyElectricPower = dailyElectricPower;
    }

    public Double getElectricPrice() {
        return electricPrice;
    }

    public void setElectricPrice(Double electricPrice) {
        this.electricPrice = electricPrice;
    }

    public Integer getHaveOtherPrice() {
        return haveOtherPrice;
    }

    public void setHaveOtherPrice(Integer haveOtherPrice) {
        this.haveOtherPrice = haveOtherPrice;
    }

    public String getOtherPriceName() {
        return otherPriceName;
    }

    public void setOtherPriceName(String otherPriceName) {
        this.otherPriceName = otherPriceName;
    }

    public Integer getClassify() {
        return classify;
    }

    public void setClassify(Integer classify) {
        this.classify = classify;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getDailyEarningsBtc() {
        return dailyEarningsBtc;
    }

    public void setDailyEarningsBtc(BigDecimal dailyEarningsBtc) {
        this.dailyEarningsBtc = dailyEarningsBtc;
    }

    public Double getDailyEarningsRmb() {
        return dailyEarningsRmb;
    }

    public void setDailyEarningsRmb(Double dailyEarningsRmb) {
        this.dailyEarningsRmb = dailyEarningsRmb;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}