package com.kg.platform.model.response;

import java.io.Serializable;
import java.util.List;


public class CurrencyResponse implements Serializable {

    /**
     * 名称
     */
    private String currencyName;
    /**
     * 当前难度
     */
    private String currencyDifficulty;
    /**
     * 系数
     */
    private String currencyCoefficient;

    /**
     * 区块奖励
     */
    private String blockAward;

    /**
     * 人民币价格
     */
    private String lastPriceCNY;

    /**
     * 美元价格
     */
    private String lastPrice;

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyDifficulty() {
        return currencyDifficulty;
    }

    public void setCurrencyDifficulty(String currencyDifficulty) {
        this.currencyDifficulty = currencyDifficulty;
    }

    public String getCurrencyCoefficient() {
        return currencyCoefficient;
    }

    public void setCurrencyCoefficient(String currencyCoefficient) {
        this.currencyCoefficient = currencyCoefficient;
    }

    public String getBlockAward() {
        return blockAward;
    }

    public void setBlockAward(String blockAward) {
        this.blockAward = blockAward;
    }

    public String getLastPriceCNY() {
        return lastPriceCNY;
    }

    public void setLastPriceCNY(String lastPriceCNY) {
        this.lastPriceCNY = lastPriceCNY;
    }

    public String getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }
}
