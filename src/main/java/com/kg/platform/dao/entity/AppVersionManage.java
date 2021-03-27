package com.kg.platform.dao.entity;

import java.util.Date;

public class AppVersionManage {

    private Long id;
    private String versionNum;
    private String prompt;
    private Integer forced;
    private Integer systemType;
    private String downloadUrl;
    private String downloadUrlApk;
    private String channel; //安卓渠道

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
}
