package com.kg.platform.model.response;

/**
 * Created by Administrator on 2018/3/19.
 */
public class TickerResponse {
    private String coinType;
    private String price;
    private String riseRate;
    private String name;
    private String tradeUrl;

    public String getTradeUrl() {
        return tradeUrl;
    }

    public void setTradeUrl(String tradeUrl) {
        this.tradeUrl = tradeUrl;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRiseRate() {
        return riseRate;
    }

    public void setRiseRate(String riseRate) {
        this.riseRate = riseRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
