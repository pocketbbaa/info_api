package com.kg.platform.model.request;

import java.io.Serializable;

public class TVWithdrawRequest implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -1553259597261839974L;

    private String amount;

    private String toAddress;

    private String memo;

    public TVWithdrawRequest(String amount, String toAddress, String memo) {
        super();
        this.amount = amount;
        this.toAddress = toAddress;
        this.memo = memo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

}
