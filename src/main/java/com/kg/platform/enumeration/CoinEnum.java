package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

public enum CoinEnum {


    KG(2, "KG","氪金",1),
    RIT(3, "RIT","RIT",1),
    TV(1, "TV","钛值",0);

    private int code;
    private String coinName;
    private String coinNameCH;
    private int cardDisplayStatus;

    CoinEnum(int code, String coinName, String coinNameCH,int cardDisplayStatus) {
        this.code = code;
        this.coinName = coinName;
        this.coinNameCH = coinNameCH;
        this.cardDisplayStatus = cardDisplayStatus;
    }

    public static CoinEnum getByCode(int code) {
        CoinEnum roles[] = CoinEnum.values();
        Optional<CoinEnum> optional = Arrays.stream(roles).filter(item -> item.code == code).findFirst();
        return optional.orElse(CoinEnum.KG);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getCoinNameCH() {
        return coinNameCH;
    }

    public void setCoinNameCH(String coinNameCH) {
        this.coinNameCH = coinNameCH;
    }

	public int getCardDisplayStatus() {
		return cardDisplayStatus;
	}

	public void setCardDisplayStatus(int cardDisplayStatus) {
		this.cardDisplayStatus = cardDisplayStatus;
	}
}
