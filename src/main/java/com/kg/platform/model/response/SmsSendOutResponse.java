package com.kg.platform.model.response;

/**
 * 
 */

public class SmsSendOutResponse {

    private String msgid;

    private String error;

    private String code;

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "SmsSingleResponse [msgid=" + msgid + ", error=" + error + ", code=" + code + "]";
    }

}
