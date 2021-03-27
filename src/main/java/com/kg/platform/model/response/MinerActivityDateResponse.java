package com.kg.platform.model.response;

/**
 * Created by Administrator on 2018/5/19.
 */
public class MinerActivityDateResponse {
    public static final String SETTING_KEY = "miner_activity_date";
    private String startTime;
    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
