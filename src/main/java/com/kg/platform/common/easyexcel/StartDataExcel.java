package com.kg.platform.common.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.kg.platform.common.utils.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/7/20.
 */
public class StartDataExcel extends BaseRowModel {
    @ExcelProperty(value = "用户ID" ,index = 0)
    private String userId;//用户ID
    @ExcelProperty(value = "设备ID" ,index = 1)
    private String deviceId;//设备ID
    @ExcelProperty(value = "设备类型" ,index = 2)
    private String deviceType;//设备类型
    @ExcelProperty(value = "经度" ,index = 3)
    private String longitude;//经度
    @ExcelProperty(value = "纬度" ,index = 4)
    private String latitude;//纬度
    @ExcelProperty(value = "地址信息" ,index = 5)
    private String address;//地址信息
    @ExcelProperty(value = "联网方式" ,index = 6)
    private String networkingMethod;//联网方式
    @ExcelProperty(value = "手机品牌" ,index = 7)
    private String mobileBrand;//手机品牌
    @ExcelProperty(value = "手机型号" ,index = 8)
    private String moblieModel;//手机型号
    @ExcelProperty(value = "系统版本" ,index = 9)
    private String systemVersion;//系统版本
    @ExcelProperty(value = "APP版本" ,index = 10)
    private String appVersion;//APP版本
    @ExcelProperty(value = "数据上传时间" ,index = 11)
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

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNetworkingMethod() {
        return networkingMethod;
    }

    public void setNetworkingMethod(String networkingMethod) {
        this.networkingMethod = networkingMethod;
    }

    public String getMobileBrand() {
        return mobileBrand;
    }

    public void setMobileBrand(String mobileBrand) {
        this.mobileBrand = mobileBrand;
    }

    public String getMoblieModel() {
        return moblieModel;
    }

    public void setMoblieModel(String moblieModel) {
        this.moblieModel = moblieModel;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public static List<StartDataExcel> MeaningOfConversion(List<StartDataExcel> list){
        for (StartDataExcel startDataExcel:list) {
            startDataExcel.setCreateDate(DateUtils.parseTimestamp(startDataExcel.getCreateDate()));
        }
        return list;
    }
}
