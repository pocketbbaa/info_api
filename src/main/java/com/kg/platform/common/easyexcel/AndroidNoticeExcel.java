package com.kg.platform.common.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.common.utils.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/7/20.
 */
public class AndroidNoticeExcel extends IosNoticeExcel{
    @ExcelProperty(value = "用户ID" ,index = 0)
    private String userId;//用户ID
    @ExcelProperty(value = "设备ID" ,index = 1)
    private String deviceId;//设备ID
    @ExcelProperty(value = "设备类型" ,index = 2)
    private String deviceType;//设备类型
    @ExcelProperty(value = "接收时间" ,index = 3)
    private String receiveDate;//接收时间
    @ExcelProperty(value = "是否被点击" ,index = 4)
    private String ifClick;//是否被点击
    @ExcelProperty(value = "是否被清理" ,index = 5)
    private String ifClear;//是否被清理
    @ExcelProperty(value = "清理时间" ,index = 6)
    private String clearDate;//清理时间
    @ExcelProperty(value = "阿里云消息ID" ,index = 7)
    private String aliMessageId;//阿里云消息ID
    @ExcelProperty(value = "业务ID" ,index = 8)
    private String serviceId;//业务ID
    @ExcelProperty(value = "通知类型" ,index = 9)
    private String noticeType;//通知类型
    @ExcelProperty(value = "点击时间" ,index = 10)
    private String clickDate;//点击时间
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

    public static List<AndroidNoticeExcel> androidMeaningOfConversion(List<AndroidNoticeExcel> list){
        for (AndroidNoticeExcel androidNoticeExcel:list) {
            //上传时间
            androidNoticeExcel.setCreateDate(DateUtils.parseTimestamp(androidNoticeExcel.getCreateDate()));
            //清理时间
            if(StringUtils.isNotBlank(androidNoticeExcel.getClearDate())){
                if(Long.valueOf(androidNoticeExcel.getClearDate())>0){
                    androidNoticeExcel.setClearDate(DateUtils.parseTimestamp(androidNoticeExcel.getClearDate()));
                }
            }
            //点击时间
            if(StringUtils.isNotBlank(androidNoticeExcel.getClickDate())){
                if(Long.valueOf(androidNoticeExcel.getClickDate())>0){
                    androidNoticeExcel.setClickDate(DateUtils.parseTimestamp(androidNoticeExcel.getClickDate()));
                }
            }
            //通知类型 1：系统通知 2：动态 3：资讯 4：快讯
			if(StringUtils.isNotBlank(androidNoticeExcel.getNoticeType())){
				switch (androidNoticeExcel.getNoticeType()){
					case ("1"):
						androidNoticeExcel.setNoticeType("系统通知");
						break;
					case ("2"):
						androidNoticeExcel.setNoticeType("动态");
						break;
					case ("3"):
						androidNoticeExcel.setNoticeType("资讯");
						break;
					case ("4"):
						androidNoticeExcel.setNoticeType("快讯");
						break;
				}
			}

            //是否被清理 0：否 1：是
            androidNoticeExcel.setIfClear("1".equals(androidNoticeExcel.getIfClear())?"是":"否");
            //是否被点击 0:否 1：是
            androidNoticeExcel.setIfClick("1".equals(androidNoticeExcel.getIfClick())?"是":"否");
            //接收时间
            if(StringUtils.isNotBlank(androidNoticeExcel.getReceiveDate())){
                if(Long.valueOf(androidNoticeExcel.getReceiveDate())>0){
                    androidNoticeExcel.setReceiveDate(DateUtils.parseTimestamp(androidNoticeExcel.getReceiveDate()));
                }
            }

        }
        return list;
    }
}
