package com.kg.platform.common.entity;

/**
 * Created by Administrator on 2018/7/12.
 */
public class BuryingPointData {
    private Integer dataType;//数据类型：1.启动数据 2.视频数据 3.资讯数据 4.IOS通知 5.安卓通知 6.广告数据
    private String createDate;//数据上传时间
    //启动数据
    private StartData startData;
    //视频数据
    private VideoData videoData;
    //资讯数据
    private ArticleData articleData;
    //IOS通知
    private IosNotice iosNotice;
    //安卓通知
    private AndroidNotice androidNotice;
    //广告数据
    private AdvData advData;


    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public StartData getStartData() {
        return startData;
    }

    public void setStartData(StartData startData) {
        this.startData = startData;
    }

    public VideoData getVideoData() {
        return videoData;
    }

    public void setVideoData(VideoData videoData) {
        this.videoData = videoData;
    }

    public ArticleData getArticleData() {
        return articleData;
    }

    public void setArticleData(ArticleData articleData) {
        this.articleData = articleData;
    }

    public IosNotice getIosNotice() {
        return iosNotice;
    }

    public void setIosNotice(IosNotice iosNotice) {
        this.iosNotice = iosNotice;
    }

    public AndroidNotice getAndroidNotice() {
        return androidNotice;
    }

    public void setAndroidNotice(AndroidNotice androidNotice) {
        this.androidNotice = androidNotice;
    }

    public AdvData getAdvData() {
        return advData;
    }

    public void setAdvData(AdvData advData) {
        this.advData = advData;
    }
}


