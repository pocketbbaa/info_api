package com.kg.platform.weixin;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class JsapiTicket implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -3258373956189505961L;

    private String errcode;

    private String errmsg;

    private String ticket;

    @JSONField(name = "expires_in")
    private String expiresIn;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }
}
