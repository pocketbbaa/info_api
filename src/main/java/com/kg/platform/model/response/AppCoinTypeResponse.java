package com.kg.platform.model.response;

import com.kg.platform.enumeration.CoinEnum;

/**
 * Created by Administrator on 2018/4/20.
 */
public class AppCoinTypeResponse {
    private Integer coinType;
    private String coinName;
    private String CNcoinName;

    public AppCoinTypeResponse() {
    }

    public AppCoinTypeResponse(Integer coinType, String coinName, String CNcoinName) {
        this.coinType = coinType;
        this.coinName = coinName;
        this.CNcoinName = CNcoinName;
    }

    public String getCNcoinName() {
        return CNcoinName;
    }

    public void setCNcoinName(String CNcoinName) {
        this.CNcoinName = CNcoinName;
    }

    public Integer getCoinType() {
        return coinType;
    }

    public void setCoinType(Integer coinType) {
        this.coinType = coinType;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public static AppCoinTypeResponse buildByCoinType(CoinEnum coinEnum) {
        return new AppCoinTypeResponse(coinEnum.getCode(), coinEnum.getCoinName(), coinEnum.getCoinNameCH());
    }
}
