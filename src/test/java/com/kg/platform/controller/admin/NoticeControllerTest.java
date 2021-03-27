package com.kg.platform.controller.admin;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.utils.CommonService;
import com.kg.platform.controller.BaseTest;
import com.kg.platform.dao.entity.KgAutherNotice;
import com.kg.platform.dao.read.admin.KgAutherNoticeRMapper;
import com.kg.platform.model.in.admin.NoticeListModel;
import com.kg.platform.model.response.UserkgResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;

import java.util.List;

public class NoticeControllerTest extends BaseTest {

    @Autowired
    CommonService commonService;
    @Autowired
    NoticeController noticeController;
    @Autowired
    KgAutherNoticeRMapper kgAutherNoticeRMapper;

    @Test
    public void addNoticeTest(){
        KgAutherNotice request = new KgAutherNotice();
        UserkgResponse userkgResponse = defaultTokenBean();

        request.setInfo("this is is test noticethis is is test noticethis is is test noti" +
                "cethis is is test noticethis is is test noticethis is is test noticethis is is test " +
                "noticethis is is test noticethis is is test noticethis is is test noticethis is is test noticethis" +
                " is is test noticethis is is test noticethis is is test noticethis is is test n" +
                "oticethis is is test noticethis is is test noticethis is is test noticethis is is test noticethis is is test noticethis is is test noti\" +\n" +
                "                \"cethis is is test noticethis is is test noticethis is is test noticethis is is test \" +\n" +
                "                \"noticethis is is test noticethis is is test noticethis is is test noticethis is is test noticethis\" +\n" +
                "                \" is is test noticethis is is test noticethis is is test noticethis is is test n\" +\n" +
                "                \"oticethis is is test noticethis is is test noticethis is is test noticethis is is test noticethis is is test noticethis is is test noti\" +\n" +
                "                \"cethis is is test noticethis is is test noticethis is is test noticethis is is test \" +\n" +
                "                \"noticethis is is test noticethis is is test noticethis is is test noticethis is is test noticethis\" +\n" +
                "                \" is is test noticethis is is test noticethis is is test noticethis is is test n\" +\n" +
                "                \"oticethis is is test noticethis is is test noticethis is is test noticethis is is test noticethis is is test noticethis is is test noti\" +\n" +
                "                \"cethis is is test noticethis is is test noticethis is is test noticethis is is test \" +\n" +
                "                \"noticethis is is test noticethis is is test noticethis is is test noticethis is is test noticethis\" +\n" +
                "                \" is is test noticethis is is test noticethis is is test noticethis is is test n\" +\n" +
                "                \"oticethis is is test noticethis is is test noticethis is is test notice");

        request.setTitle("test job 2");
        request.setUserId("16");

        noticeController.addNoticeInfo(request,userkgResponse);
    }

    @Test
    public void deleteNoticeTest(){
        KgAutherNotice request = new KgAutherNotice();

        request.setId(14L);

        noticeController.deleteNotice(request);
    }

    @Test
    public void updateNoticeTest(){
        KgAutherNotice request = new KgAutherNotice();

        request.setId(15L);
        request.setInfo("update info1113333");
        request.setTitle("update title1111333");
        request.setUserId(18+"");

        noticeController.updateNotice(request);
    }

    @Test
    public void getNoticeById(){
        KgAutherNotice request = new KgAutherNotice();

        request.setId(15L);

        JsonEntity jsonEntity = noticeController.getNoticeById(request);

        System.out.println(JSON.toJSONString(jsonEntity));
    }

    @Test
    public void getBkgNoticeInfo(){
        NoticeListModel noticeListModel = new NoticeListModel();
//        noticeListModel.setAddUser("guokang");

        noticeListModel.setCurrentPage(2);
        noticeListModel.setPageSize(25);

        System.out.println(JSON.toJSONString(noticeController.getBkgNoticeInfo(noticeListModel)));
    }

    @Test
    public void testExecuteSql(){
        String sql = "SELECT * FROM kg_auther_notice";
        List<Object> result = kgAutherNoticeRMapper.execSqlList_sp(sql);
        System.out.println(JSON.toJSONString(result));
    }


    private static UserkgResponse defaultTokenBean(){
        UserkgResponse userkgResponse = new UserkgResponse();

        userkgResponse.setUserId("32435");
        return userkgResponse;
    }
}
