package com.kg.platform.model.response;

/**
 * 我的算力收益
 */
public class PerformanceEarningsResponse {


    private String performance;
    private String yesterdayEarningsBtc;
    private String yesterdayEarningsRmb;
    private String totalEarningsBtc;
    private String totalEarningsRmb;
    private String btcBalance;
    private String rmbBalance;


    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public String getYesterdayEarningsBtc() {
        return yesterdayEarningsBtc;
    }

    public void setYesterdayEarningsBtc(String yesterdayEarningsBtc) {
        this.yesterdayEarningsBtc = yesterdayEarningsBtc;
    }

    public String getYesterdayEarningsRmb() {
        return yesterdayEarningsRmb;
    }

    public void setYesterdayEarningsRmb(String yesterdayEarningsRmb) {
        this.yesterdayEarningsRmb = yesterdayEarningsRmb;
    }

    public String getTotalEarningsBtc() {
        return totalEarningsBtc;
    }

    public void setTotalEarningsBtc(String totalEarningsBtc) {
        this.totalEarningsBtc = totalEarningsBtc;
    }

    public String getTotalEarningsRmb() {
        return totalEarningsRmb;
    }

    public void setTotalEarningsRmb(String totalEarningsRmb) {
        this.totalEarningsRmb = totalEarningsRmb;
    }

    public String getBtcBalance() {
        return btcBalance;
    }

    public void setBtcBalance(String btcBalance) {
        this.btcBalance = btcBalance;
    }

    public String getRmbBalance() {
        return rmbBalance;
    }

    public void setRmbBalance(String rmbBalance) {
        this.rmbBalance = rmbBalance;
    }
}
