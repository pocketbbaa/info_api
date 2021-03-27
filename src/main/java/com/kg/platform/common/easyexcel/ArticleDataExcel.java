package com.kg.platform.common.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.common.utils.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/7/20.
 */
public class ArticleDataExcel extends BaseRowModel {
    @ExcelProperty(value = "用户ID" ,index = 0)
    private String userId;//用户ID
    @ExcelProperty(value = "设备ID" ,index = 1)
    private String deviceId;//设备ID
    @ExcelProperty(value = "设备类型" ,index = 2)
    private String deviceType;//设备类型
    @ExcelProperty(value = "资讯ID" ,index = 3)
    private String articleId;//资讯ID
    @ExcelProperty(value = "进入资讯时间" ,index = 4)
    private String intoArticleDate;//进入资讯时间
    @ExcelProperty(value = "最终滚动位置（百分比）" ,index = 5)
    private String finalScroll;//最终滚动位置（百分比）
    @ExcelProperty(value = "阅读时长" ,index = 6)
    private String readDuration;//阅读时长
    @ExcelProperty(value = "退出时间" ,index = 7)
    private String exitArticleDate;//退出时间
    @ExcelProperty(value = "是否点击评论按钮" ,index = 8)
    private String ifClickCommentBtn;//是否点击评论按钮 0:否 1：是
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

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getIntoArticleDate() {
        return intoArticleDate;
    }

    public void setIntoArticleDate(String intoArticleDate) {
        this.intoArticleDate = intoArticleDate;
    }

    public String getFinalScroll() {
        return finalScroll;
    }

    public void setFinalScroll(String finalScroll) {
        this.finalScroll = finalScroll;
    }

    public String getReadDuration() {
        return readDuration;
    }

    public void setReadDuration(String readDuration) {
        this.readDuration = readDuration;
    }

    public String getExitArticleDate() {
        return exitArticleDate;
    }

    public void setExitArticleDate(String exitArticleDate) {
        this.exitArticleDate = exitArticleDate;
    }

    public String getIfClickCommentBtn() {
        return ifClickCommentBtn;
    }

    public void setIfClickCommentBtn(String ifClickCommentBtn) {
        this.ifClickCommentBtn = ifClickCommentBtn;
    }

    public static List<ArticleDataExcel> MeaningOfConversion(List<ArticleDataExcel> list){
        for (ArticleDataExcel articleDataExcel:list) {
            //上传时间
            articleDataExcel.setCreateDate(DateUtils.parseTimestamp(articleDataExcel.getCreateDate()));
            //退出时间
            if(StringUtils.isNotBlank(articleDataExcel.getExitArticleDate())){
                if(Long.parseLong(articleDataExcel.getExitArticleDate())>0){
                    articleDataExcel.setExitArticleDate(DateUtils.parseTimestamp(articleDataExcel.getExitArticleDate()));
                }
            }
            //最终滚动位置（百分比）
            articleDataExcel.setFinalScroll(StringUtils.isNotBlank(articleDataExcel.getFinalScroll())?articleDataExcel.getFinalScroll()+"%":null);
            //是否点击评论按钮 0:否 1：是
            articleDataExcel.setIfClickCommentBtn("1".equals(articleDataExcel.getIfClickCommentBtn())?"是":"否");
            //进入资讯时间
            if(StringUtils.isNotBlank(articleDataExcel.getIntoArticleDate())){
                if(Long.parseLong(articleDataExcel.getIntoArticleDate())>0){
                    articleDataExcel.setIntoArticleDate(DateUtils.parseTimestamp(articleDataExcel.getIntoArticleDate()));
                }
            }
            //阅读时长（毫秒）
            articleDataExcel.setReadDuration(articleDataExcel.getReadDuration()+"毫秒");
        }
        return list;
    }
}
