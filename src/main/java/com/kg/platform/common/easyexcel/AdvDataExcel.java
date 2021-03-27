package com.kg.platform.common.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.kg.platform.common.utils.DateUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/7/20.
 */
public class AdvDataExcel extends BaseRowModel {
    @ExcelProperty(value = "用户ID" ,index = 0)
    private String userId;//用户ID
    @ExcelProperty(value = "设备ID" ,index = 1)
    private String deviceId;//设备ID
    @ExcelProperty(value = "设备类型" ,index = 2)
    private String deviceType;//设备类型
    @ExcelProperty(value = "广告ID" ,index = 3)
    private String advId;//广告ID
    @ExcelProperty(value = "曝光量" ,index = 4)
    private String exposure;//曝光量
    @ExcelProperty(value = "点击量" ,index = 5)
    private String clickNum;//点击量
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

    public String getAdvId() {
        return advId;
    }

    public void setAdvId(String advId) {
        this.advId = advId;
    }

    public String getExposure() {
        return exposure;
    }

    public void setExposure(String exposure) {
        this.exposure = exposure;
    }

    public String getClickNum() {
        return clickNum;
    }

    public void setClickNum(String clickNum) {
        this.clickNum = clickNum;
    }

    public static List<AdvDataExcel> MeaningOfConversion(List<AdvDataExcel> list){
        for (AdvDataExcel advDataExcel:list) {
            //上传时间
            advDataExcel.setCreateDate(DateUtils.parseTimestamp(advDataExcel.getCreateDate()));
        }
        return list;
    }
}
