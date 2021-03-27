package com.kg.platform.model.response;

/**
 * Created by Administrator on 2018/6/6.
 */
public class WorldcupActivityDateResponse {
    public static final String SETTING_KEY = "worldcup_activity_date";
    private String startTime;
    private String endTime;
    private Integer ifStart;// 0:活动还未开始 1:活动已开始 2: 活动已结束

    public Integer getIfStart() {
        return ifStart;
    }

    public void setIfStart(Integer ifStart) {
        this.ifStart = ifStart;
    }

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
