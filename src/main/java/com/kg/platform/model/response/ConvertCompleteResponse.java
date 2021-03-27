package com.kg.platform.model.response;

/**
 * 兑换完成
 */
public class ConvertCompleteResponse {


    private String convertRitToday; //今日已兑换
    private String kgBalance; //氪金剩余

    public String getConvertRitToday() {
        return convertRitToday;
    }

    public void setConvertRitToday(String convertRitToday) {
        this.convertRitToday = convertRitToday;
    }

    public String getKgBalance() {
        return kgBalance;
    }

    public void setKgBalance(String kgBalance) {
        this.kgBalance = kgBalance;
    }
}
