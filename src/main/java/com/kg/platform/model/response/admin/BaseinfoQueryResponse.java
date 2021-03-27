package com.kg.platform.model.response.admin;

import com.alibaba.fastjson.JSON;

/**
 * 基础信息查询出参
 */
public class BaseinfoQueryResponse extends AdminBaseResponse {

    /**
     * 
     */
    private static final long serialVersionUID = -8162245673386174432L;

    private String id;

    private String name;

    private String type;

    private String user;

    private String createDate;

    private String updateDate;

    private Boolean infoStatus;

    private String infoStatusDisplay;

    private Integer infoOrder;

    private String infoDetail;

    public String getInfoDetail() {
        return infoDetail;
    }

    public void setInfoDetail(String infoDetail) {
        this.infoDetail = infoDetail;
    }

    public Integer getInfoOrder() {
        return infoOrder;
    }

    public void setInfoOrder(Integer infoOrder) {
        this.infoOrder = infoOrder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public Boolean getInfoStatus() {
        return infoStatus;
    }

    public void setInfoStatus(Boolean infoStatus) {
        this.infoStatus = infoStatus;
    }

    public String getInfoStatusDisplay() {
        return infoStatusDisplay;
    }

    public void setInfoStatusDisplay(String infoStatusDisplay) {
        this.infoStatusDisplay = infoStatusDisplay;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
