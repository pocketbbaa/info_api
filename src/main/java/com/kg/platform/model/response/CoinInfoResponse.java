package com.kg.platform.model.response;

public class CoinInfoResponse {

    private Integer coinType;
    private String coinUnit;
    private String coinName;
    private String balance;
    private String frozenBalance;
    private String background;
    private String intro;

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

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
