package com.kg.platform.common.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.common.utils.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/7/20.
 */
public class IosNoticeExcel extends BaseRowModel {
    @ExcelProperty(value = "用户ID" ,index = 0)
    private String userId;//用户ID
    @ExcelProperty(value = "设备ID" ,index = 1)
    private String deviceId;//设备ID
    @ExcelProperty(value = "设备类型" ,index = 2)
    private String deviceType;//设备类型
    @ExcelProperty(value = "阿里云消息ID" ,index = 3)
    private String aliMessageId;//阿里云消息ID
    @ExcelProperty(value = "业务ID" ,index = 4)
    private String serviceId;//业务ID
    @ExcelProperty(value = "通知类型" ,index = 5)
    private String noticeType;//通知类型
    @ExcelProperty(value = "点击时间" ,index = 6)
    private String clickDate;//点击时间
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

    public String getAliMessageId() {
        return aliMessageId;
    }

    public void setAliMessageId(String aliMessageId) {
        this.aliMessageId = aliMessageId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getClickDate() {
        return clickDate;
    }

    public void setClickDate(String clickDate) {
        this.clickDate = clickDate;
    }

    public static List<IosNoticeExcel> MeaningOfConversion(List<IosNoticeExcel> list){
        for (IosNoticeExcel iosNoticeExcel:list) {
            //上传时间
            iosNoticeExcel.setCreateDate(DateUtils.parseTimestamp(iosNoticeExcel.getCreateDate()));
            //点击时间
            if(StringUtils.isNotBlank(iosNoticeExcel.getClickDate())){
                if(Long.valueOf(iosNoticeExcel.getClickDate())>0){
                    iosNoticeExcel.setClickDate(DateUtils.parseTimestamp(iosNoticeExcel.getClickDate()));
                }
            }
            //通知类型 1：系统通知 2：动态 3：资讯 4：快讯
			if(StringUtils.isNotBlank(iosNoticeExcel.getNoticeType())){
				switch (iosNoticeExcel.getNoticeType()){
					case ("1"):
						iosNoticeExcel.setNoticeType("系统通知");
						break;
					case ("2"):
						iosNoticeExcel.setNoticeType("动态");
						break;
					case ("3"):
						iosNoticeExcel.setNoticeType("资讯");
						break;
					case ("4"):
						iosNoticeExcel.setNoticeType("快讯");
						break;
				}
			}
        }
        return list;
    }
}
