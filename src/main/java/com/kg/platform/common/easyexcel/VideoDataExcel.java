package com.kg.platform.common.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.common.utils.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/7/20.
 */
public class VideoDataExcel extends BaseRowModel {
    @ExcelProperty(value = "用户ID" ,index = 0)
    private String userId;//用户ID
    @ExcelProperty(value = "设备ID" ,index = 1)
    private String deviceId;//设备ID
    @ExcelProperty(value = "设备类型" ,index = 2)
    private String deviceType;//设备类型
    @ExcelProperty(value = "视频ID" ,index = 3)
    private String videoId;//视频ID
    @ExcelProperty(value = "播放位置" ,index = 4)
    private String playLocation;//播放位置
    @ExcelProperty(value = "开始播放时间" ,index = 5)
    private String sartPlayDate;//开始播放时间
    @ExcelProperty(value = "播放百分比" ,index = 6)
    private String playPercent;//播放百分比
    @ExcelProperty(value = "退出时间" ,index = 7)
    private String exitVideoDate;//退出时间
    @ExcelProperty(value = "数据上传时间" ,index = 9)
    private String createDate;//数据上传时间

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

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

    public static List<VideoDataExcel> MeaningOfConversion(List<VideoDataExcel> list){
        for (VideoDataExcel videoDataExcel:list) {
            //上传时间
            videoDataExcel.setCreateDate(DateUtils.parseTimestamp(videoDataExcel.getCreateDate()));
            //退出时间
            if(StringUtils.isNotBlank(videoDataExcel.getExitVideoDate())){
                if(Long.parseLong(videoDataExcel.getExitVideoDate())>0){
                    videoDataExcel.setExitVideoDate(DateUtils.parseTimestamp(videoDataExcel.getExitVideoDate()));
                }
            }
            //播放位置（0：列表，1：详情）
            videoDataExcel.setPlayLocation("0".equals(videoDataExcel.getPlayLocation())?"列表":"详情");
            //	播放百分比
            videoDataExcel.setPlayPercent(StringUtils.isNotBlank(videoDataExcel.getPlayPercent())?videoDataExcel.getPlayPercent()+"%":null);
            //开始播放时间
            if(StringUtils.isNotBlank(videoDataExcel.getSartPlayDate())){
                if(Long.parseLong(videoDataExcel.getSartPlayDate())>0){
                    videoDataExcel.setSartPlayDate(DateUtils.parseTimestamp(videoDataExcel.getSartPlayDate()));
                }
            }
        }
        return list;
    }
}
