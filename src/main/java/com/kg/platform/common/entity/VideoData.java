package com.kg.platform.common.entity;

/**
 * Created by Administrator on 2018/7/20.
 */
public class VideoData{
    private String videoId;//视频ID
    private String playLocation;//播放位置
    private String sartPlayDate;//开始播放时间
    private String playPercent;//播放百分比
    private String exitVideoDate;//退出时间

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getPlayLocation() {
        return playLocation;
    }

    public void setPlayLocation(String playLocation) {
        this.playLocation = playLocation;
    }

    public String getSartPlayDate() {
        return sartPlayDate;
    }

    public void setSartPlayDate(String sartPlayDate) {
        this.sartPlayDate = sartPlayDate;
    }

    public String getPlayPercent() {
        return playPercent;
    }

    public void setPlayPercent(String playPercent) {
        this.playPercent = playPercent;
    }

    public String getExitVideoDate() {
        return exitVideoDate;
    }

    public void setExitVideoDate(String exitVideoDate) {
        this.exitVideoDate = exitVideoDate;
    }
}
