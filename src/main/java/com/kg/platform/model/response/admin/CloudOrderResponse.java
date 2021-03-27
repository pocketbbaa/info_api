package com.kg.platform.model.response.admin;

import java.io.Serializable;

/**
 * @author 
 */
public class CloudOrderResponse implements Serializable {
    /**
     * 购买工单号
     */
    private String id;

    /**
     * 用户ID
     */
    private String userId;

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
    private String packageId;

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
    private String price;

    /**
     * 买入算力（T）
     */
    private String performance;

    /**
     * 购买渠道ID
     */
    private String channelId;

    /**
     * 工单提交时间
     */
    private String createTime;

    /**
     * 完成付款时间
     */
    private String payTime;

    /**
     * 工单状态(0：执行中，1：已到期)
     */
    private Integer state;

    /**
     * 操作人ID
     */
    private String sysUserId;

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
    private String totalRevenue;

    private static final long serialVersionUID = 1L;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSysUserName() {
		return sysUserName;
	}

	public void setSysUserName(String sysUserName) {
		this.sysUserName = sysUserName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPerformance() {
		return performance;
	}

	public void setPerformance(String performance) {
		this.performance = performance;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
	}

	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public String getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(String totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

	public Integer getPayState() {
		return payState;
	}

	public void setPayState(Integer payState) {
		this.payState = payState;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
}