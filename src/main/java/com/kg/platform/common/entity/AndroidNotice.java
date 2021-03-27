package com.kg.platform.common.entity;

/**
 * Created by Administrator on 2018/7/20.
 */
public class AndroidNotice extends IosNotice {
    private String receiveDate;//接收时间
    private String ifClick;//是否被点击
    private String ifClear;//是否被清理
    private String clearDate;//清理时间

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getIfClick() {
        return ifClick;
    }

    public void setIfClick(String ifClick) {
        this.ifClick = ifClick;
    }

    public String getIfClear() {
        return ifClear;
    }

    public void setIfClear(String ifClear) {
        this.ifClear = ifClear;
    }

    public String getClearDate() {
        return clearDate;
    }

    public void setClearDate(String clearDate) {
        this.clearDate = clearDate;
    }
}
