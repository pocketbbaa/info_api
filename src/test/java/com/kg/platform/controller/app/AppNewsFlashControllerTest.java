package com.kg.platform.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.request.KgNewsFlashRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Administrator on 2018/3/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/spring-context*.xml" })
public class AppNewsFlashControllerTest {
    @Test
    public void getNewsFlashTopMenus() throws Exception {
        JSONObject result = appNewsFlashController.getNewsFlashTopMenus();
        logger.info("测试结果："+result.toJSONString());
    }

    protected Logger logger = LoggerFactory.getLogger(AppNewsFlashControllerTest.class);
    @Autowired
    private AppNewsFlashController appNewsFlashController;
    @Test
    public void getNewsFlashListByType() throws Exception {
        KgNewsFlashRequest request = new KgNewsFlashRequest();
        request.setNewsflashType(2);
        request.setCurrentPage(1);
        request.setPageSize(20);
        PageModel pageModel = new PageModel();
        JSONObject result = appNewsFlashController.getNewsFlashListByType(request,pageModel,new MockHttpServletRequest());
        logger.info("测试结果："+result.toJSONString());
    }

    @Test
    public void getNewsFlashDetail() throws Exception {
        KgNewsFlashRequest request = new KgNewsFlashRequest();
        request.setNewsflashId(443779437331357696L);
        JSONObject result = appNewsFlashController.getNewsFlashDetail(request);
        logger.info("测试结果："+result.toJSONString());
    }

}