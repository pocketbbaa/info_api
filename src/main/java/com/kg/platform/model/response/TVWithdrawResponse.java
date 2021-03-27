package com.kg.platform.model.response;

import java.io.Serializable;

public class TVWithdrawResponse implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -1553259597261839974L;

    private String code;

    private String data;

    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
