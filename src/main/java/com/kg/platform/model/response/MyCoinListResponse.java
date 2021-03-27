package com.kg.platform.model.response;

import com.kg.platform.enumeration.CoinEnum;

public class MyCoinListResponse {

    private Integer coinType;
    private String coinUnit;
    private String coinName;
    private String balance;
    private String frozenBalance;
    private String background;

    public MyCoinListResponse() {
    }

    public MyCoinListResponse(Integer coinType, String coinUnit, String coinName) {
        this.coinType = coinType;
        this.coinUnit = coinUnit;
        this.coinName = coinName;
    }

    public static MyCoinListResponse initCoinData(CoinEnum coinEnum) {
        return new MyCoinListResponse(coinEnum.getCode(), coinEnum.getCoinName(), coinEnum.getCoinNameCH());
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Integer getCoinType() {
        return coinType;
    }

    public void setCoinType(Integer coinType) {
        this.coinType = coinType;
    }

    public String getCoinUnit() {
        return coinUnit;
    }

    public void setCoinUnit(String coinUnit) {
        this.coinUnit = coinUnit;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getFrozenBalance() {
        return frozenBalance;
    }

    public void setFrozenBalance(String frozenBalance) {
        this.frozenBalance = frozenBalance;
    }
}
