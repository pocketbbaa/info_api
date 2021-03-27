package com.kg.platform.model.request.admin;

import java.util.Date;

/**
 * APP版本管理
 */
public class AppVersionManageRequest extends AdminBaseRequest {

    private Long id;
    private String versionNum;
    private String prompt;
    private Integer forced;
    private Integer systemType;  //操作系统 1：android。2：ios
    private String downloadUrl; //服务器上传地址
    private String downloadUrlApk; //渠道地址
    private String channel; //安卓渠道
    private String userId;

    private Date createTime;
    private Date updateTime;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDownloadUrlApk() {
        return downloadUrlApk;
    }

    public void setDownloadUrlApk(String downloadUrlApk) {
        this.downloadUrlApk = downloadUrlApk;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(String versionNum) {
        this.versionNum = versionNum;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public Integer getForced() {
        return forced;
    }

    public void setForced(Integer forced) {
        this.forced = forced;
    }

    public Integer getSystemType() {
        return systemType;
    }

    public void setSystemType(Integer systemType) {
        this.systemType = systemType;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
