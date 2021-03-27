package com.kg.platform.model.response;

/**
 * Created by Administrator on 2018/5/16.
 */
public class PushNewsFlashResponse {
    public static final String SETTING_KEY = "push_newsFlash_limit";
    private Integer pushNewsFlashLimit;
    private Integer pushNewsFlashNumber;

    public Integer getPushNewsFlashLimit() {
        return pushNewsFlashLimit;
    }

    public void setPushNewsFlashLimit(Integer pushNewsFlashLimit) {
        this.pushNewsFlashLimit = pushNewsFlashLimit;
    }

    public Integer getPushNewsFlashNumber() {
        return pushNewsFlashNumber;
    }

    public void setPushNewsFlashNumber(Integer pushNewsFlashNumber) {
        this.pushNewsFlashNumber = pushNewsFlashNumber;
    }
}
