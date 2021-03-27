package com.kg.platform.dao.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author
 */
public class CloudOrder implements Serializable {

    public static final Integer PAYSTATE_NOTPAY = 0;
    public static final Integer PAYSTATE_PAY = 1;
    public static final Integer STATE_TIMEING = 0;
    public static final Integer STATE_TIMEOUT = 1;

    /**
     * 购买工单号
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 付款状态(0:未付款给，1：已付款)
     */
    private Integer payState;

    /**
     * 购买套餐ID
     */
    private Integer packageId;

    /**
     * 购买套餐名称
     */
    private String packageName;

    /**
     * 购买份数
     */
    private Integer number;

    /**
     * 买入金额（元）
     */
    private Double price;

    /**
     * 查询买入最小金额（元）
     */
    private Double priceMin;

    /**
     * 查询买入最大金额（元）
     */
    private Double priceMax;

    /**
     * 买入算力（T）
     */
    private Double performance;

    /**
     * 购买渠道ID
     */
    private Integer channelId;

    /**
     * 工单提交时间
     */
    private Date createTime;

    /**
     * 查询工单提交开始时间
     */
    private String createTimeFrom;

    /**
     * 查询工单提交结束时间
     */
    private String createTimeTo;

    /**
     * 完成付款时间
     */
    private Date payTime;

    /**
     * 工单状态(0：执行中，1：已到期)
     */
    private Integer state;

    /**
     * 操作人ID
     */
    private Long sysUserId;

    /**
     * 操作人姓名
     */
    private String sysUserName;

    /**
     * 收款人姓名
     */
    private String payee;

    /**
     * 累计收益
     */
    private Long totalRevenue;

    private Integer start;

    private Integer limit;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(Double priceMin) {
        this.priceMin = priceMin;
    }

    public Double getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(Double priceMax) {
        this.priceMax = priceMax;
    }

    public Double getPerformance() {
        return performance;
    }

    public void setPerformance(Double performance) {
        this.performance = performance;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeFrom() {
        return createTimeFrom;
    }

    public void setCreateTimeFrom(String createTimeFrom) {
        this.createTimeFrom = createTimeFrom;
    }

    public String getCreateTimeTo() {
        return createTimeTo;
    }

    public void setCreateTimeTo(String createTimeTo) {
        this.createTimeTo = createTimeTo;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getSysUserName() {
        return sysUserName;
    }

    public void setSysUserName(String sysUserName) {
        this.sysUserName = sysUserName;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public Long getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Long totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}