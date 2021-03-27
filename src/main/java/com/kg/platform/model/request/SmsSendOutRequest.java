package com.kg.platform.model.request;

/**
 * 
 */

public class SmsSendOutRequest {

    private String account;

    private String password;

    private String msg;

    private String mobile;

    public SmsSendOutRequest() {

    }

    public SmsSendOutRequest(String account, String password, String msg, String mobile) {
        super();
        this.account = account;
        this.password = password;
        this.msg = msg;
        this.mobile = mobile;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
