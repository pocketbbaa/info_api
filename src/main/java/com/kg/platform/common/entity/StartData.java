package com.kg.platform.common.entity;

/**
 * Created by Administrator on 2018/7/20.
 */
public class StartData{
    private String longitudeAndLatitude;//经纬度
    private String address;//地址信息
    private String networkingMethod;//联网方式
    private String mobileBrand;//手机品牌
    private String moblieModel;//手机型号
    private String systemVersion;//系统版本
    private String appVersion;//APP版本

    public String getLongitudeAndLatitude() {
        return longitudeAndLatitude;
    }

    public void setLongitudeAndLatitude(String longitudeAndLatitude) {
        this.longitudeAndLatitude = longitudeAndLatitude;
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
}
