package com.kg.platform.weixin;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class WxPayNotifySendData {

    /** 公众账号ID 必须 */
    @XStreamAlias("appid")
    private String appId;

    /** 附加数据 */
    @XStreamAlias("attach")
    private String attach;

    /** 商户号 必须 */
    @XStreamAlias("out_trade_no")
    private String outTradeNo;

    @XStreamAlias("cash_fee")
    private String cashFee;

    @XStreamAlias("device_info")
    private String deviceInfo;

    @XStreamAlias("fee_type")
    private String feeType;

    @XStreamAlias("total_fee")
    private String totalFee;

    @XStreamAlias("trade_type")
    private String tradeType;

    @XStreamAlias("mch_id")
    private String mchId;

    @XStreamAlias("nonce_str")
    private String nonceStr;

    @XStreamAlias("result_code")
    private String resultCode;

    @XStreamAlias("openid")
    private String openid;

    @XStreamAlias("transaction_id")
    private String transactionId;

    @XStreamAlias("bank_type")
    private String bankType;

    @XStreamAlias("time_end")
    private String timeEnd;

    @XStreamAlias("return_code")
    private String returnCode;

    @XStreamAlias("is_subscribe")
    private String isSubscribe;

    @XStreamAlias("sign")
    private String sign;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getCashFee() {
        return cashFee;
    }

    public void setCashFee(String cashFee) {
        this.cashFee = cashFee;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(String isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
    /** getter() and setter() */

}