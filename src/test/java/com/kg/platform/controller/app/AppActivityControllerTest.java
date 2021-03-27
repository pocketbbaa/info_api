package com.kg.platform.controller.app;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.model.request.KgActivityCompetionRequest;
import com.kg.platform.model.request.KgActivityMinerRequest;
import com.kg.platform.model.request.KgMinerAssistRequest;
import com.kg.platform.model.request.KgMinerRobRequest;
import com.kg.platform.model.response.UserkgResponse;

/**
 * Created by Administrator on 2018/5/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/spring-context*.xml" })
public class AppActivityControllerTest {
    @Test
    public void worldCupCompetionList() throws Exception {
        KgActivityCompetionRequest request = new KgActivityCompetionRequest();
        UserkgResponse response = new UserkgResponse();
        response.setUserId("425717878151585792");
        JSONObject result = appActivityController.worldCupCompetionList(response);
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void worldcupTime() throws Exception {
        JSONObject result = appActivityController.worldCupTime();
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void activityPopInfo() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("os_version",2);
        JSONObject result = appActivityController.activityPopInfo(request);
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void minerProgressList() throws Exception {
        KgMinerAssistRequest request = new KgMinerAssistRequest();
        request.setMinerId(446732081771454466L);
        JSONObject result = appActivityController.minerProgressList(request);
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void minerTime() throws Exception {
        JSONObject result = appActivityController.minerTime();
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void assistList() throws Exception {
        KgMinerAssistRequest request = new KgMinerAssistRequest();
        request.setRobId(449226102955749376L);
        request.setCurrentPage(1);
        request.setPageSize(1);
        JSONObject result = appActivityController.assistList(request);
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void detailRob() throws Exception {
        KgMinerRobRequest request = new KgMinerRobRequest();
        request.setRobId(448952059136487424L);
        JSONObject result = appActivityController.detailRob(request);
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void myRobList() throws Exception {
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("123456");
        JSONObject result = appActivityController.myRobList(kguser);
        logger.info("测试结果：" + result.toJSONString());
    }

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AppActivityController appActivityController;

    @Test
    public void minerList() throws Exception {
        JSONObject result = appActivityController.minerList();
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void rushToMiner() throws Exception {
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("420245869804265472");
        KgActivityMinerRequest request = new KgActivityMinerRequest();
        request.setMinerId(446732081771454464L);
        JSONObject result = appActivityController.rushToMiner(kguser, request);
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void friendHelp() throws Exception {
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("428632067224510464");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        kguser.setCreateDate(c.getTime());
        KgActivityMinerRequest request = new KgActivityMinerRequest();
        request.setAssistCode("2WlNpi");
        JSONObject result = appActivityController.friendHelp(kguser, request);
        logger.info("测试结果：" + result.toJSONString());
    }

}