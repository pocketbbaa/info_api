package com.kg.platform.model.request;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.kg.platform.common.entity.*;

import java.util.List;

/**
 * Created by Administrator on 2018/7/13.
 */
public class BuryingPointRequest {

    private Integer dataType;//数据类型：1.启动数据 2.视频数据 3.资讯数据 4.IOS通知 5.安卓通知 6.广告数据
    private Integer currentPage;
    private Integer pageSize;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }



    public static void main(String[] args) {

        CommonData commonData = new CommonData();
        commonData.setUserId("asdf");
        commonData.setDeviceId("asdfag");
        commonData.setDeviceType("1");

        StartData startData = new StartData();
        startData.setAddress("qwe");
        startData.setAppVersion("asdf");
        startData.setLongitudeAndLatitude("qqr");
        startData.setMobileBrand("sadfg");
        startData.setMoblieModel("23qwe");
        startData.setNetworkingMethod("sdfg");
        startData.setSystemVersion("asdgz");

        VideoData videoData = new VideoData();
        videoData.setExitVideoDate("ggg");
        videoData.setPlayLocation("dasas");
        videoData.setPlayPercent("rtet");
        videoData.setSartPlayDate("adsfa");
        videoData.setVideoId("zxsadf");

        ArticleData articleData = new ArticleData();
        articleData.setArticleId("wert");
        articleData.setExitArticleDate("wer");
        articleData.setFinalScroll("twert");
        articleData.setIfClickCommentBtn("wetw");
        articleData.setIntoArticleDate("qweqw");
        articleData.setReadDuration("wertw");

        IosNotice iosNotice = new IosNotice();
        iosNotice.setAliMessageId("qwre");
        iosNotice.setClickDate("qwe");
        iosNotice.setNoticeType("qer");
        iosNotice.setServiceId("wert");

        AndroidNotice androidNotice = new AndroidNotice();
        androidNotice.setClearDate("qwras");
        androidNotice.setIfClear("asf");
        androidNotice.setIfClick("asd");
        androidNotice.setReceiveDate("asde");
        androidNotice.setAliMessageId("wt");
        androidNotice.setClickDate("ewr");
        androidNotice.setNoticeType("dfg");
        androidNotice.setServiceId("sdfg");

        AdvData advData = new AdvData();
        advData.setAdvId("qqwe");
        advData.setClickNum("sdf");
        advData.setExposure("afg");

        List<BuryingPointData> buryingPointDataList = Lists.newArrayList();
        //
        BuryingPointData data = new BuryingPointData();
        data.setDataType(1);
        data.setCreateDate("2017");
        data.setStartData(startData);
        //
        BuryingPointData data2 = new BuryingPointData();
        data2.setDataType(2);
        data2.setCreateDate("2018");
        data2.setVideoData(videoData);
        //
        BuryingPointData data3 = new BuryingPointData();
        data3.setDataType(3);
        data3.setCreateDate("2018");
        data3.setArticleData(articleData);
        //
        BuryingPointData data4 = new BuryingPointData();
        data4.setDataType(4);
        data4.setCreateDate("2018");
        data4.setIosNotice(iosNotice);
        //
        BuryingPointData data5 = new BuryingPointData();
        data5.setDataType(5);
        data5.setCreateDate("2018");
        data5.setAndroidNotice(androidNotice);
        //
        BuryingPointData data6 = new BuryingPointData();
        data6.setDataType(6);
        data6.setCreateDate("2018");
        data6.setAdvData(advData);

        buryingPointDataList.add(data);
        buryingPointDataList.add(data2);
        buryingPointDataList.add(data3);
        buryingPointDataList.add(data4);
        buryingPointDataList.add(data5);
        buryingPointDataList.add(data6);
        System.out.println(JSON.toJSONString(buryingPointDataList));

      /*  String s = JSON.toJSONString(buryingPointDataList);
        List list = JSON.parseObject(s,List.class);
        System.out.println(list);*/

    }
}
