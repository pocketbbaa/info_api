package com.kg.platform.controller.admin;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.request.KgNewsFlashRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Administrator on 2018/5/31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/spring-context*.xml" })
public class AdminNewsFlashControllerTest {
    @Test
    public void getPushNewsFlashInfo() throws Exception {
        JsonEntity jsonEntity = adminNewsFlashController.getPushNewsFlashInfo();
        logger.info("测试结果："+ JSON.toJSONString(jsonEntity));
    }

    @Test
    public void getNewsFlashTopMenus() throws Exception {
        JsonEntity jsonEntity = adminNewsFlashController.getNewsFlashTopMenus();
        logger.info("测试结果："+ JSON.toJSONString(jsonEntity));
    }

    @Test
    public void updateNewsFlash() throws Exception {
        KgNewsFlashRequest request = new KgNewsFlashRequest();
        request.setNewsflashId(443779173014708224L);
        request.setUpdateUser(1L);
        request.setNewsflashTitle("unitTestupdate2");
        request.setNewsflashText("laksdjflkasjdflkajsdf222222");
        request.setNewsflashType(1);
        request.setIfPush(1);
        request.setDisplayStatus(0);
        JsonEntity jsonEntity = adminNewsFlashController.updateNewsFlash(request);
        logger.info("测试结果："+ JSON.toJSONString(jsonEntity));
    }

    @Test
    public void delNewsFlash() throws Exception {
        KgNewsFlashRequest request = new KgNewsFlashRequest();
        request.setNewsflashId(443779744002088960L);
        JsonEntity jsonEntity = adminNewsFlashController.delNewsFlash(request);
        logger.info("测试结果："+ JSON.toJSONString(jsonEntity));
    }

    @Test
    public void addNewsFlash() throws Exception {
        KgNewsFlashRequest request = new KgNewsFlashRequest();
        request.setNewsflashTitle("unitTest55");
        request.setNewsflashText("laksdjflkasjdflkajsdf555");
        request.setNewsflashType(1);
        request.setIfPush(0);
        request.setDisplayStatus(1);
        request.setCreateUser(10L);
        JsonEntity jsonEntity = adminNewsFlashController.addNewsFlash(request);
        logger.info("测试结果："+ JSON.toJSONString(jsonEntity));
    }

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AdminNewsFlashController adminNewsFlashController;
    @Test
    public void getNewsFlashListByCondition() throws Exception {
        KgNewsFlashRequest request = new KgNewsFlashRequest();
        request.setRemark("东方财富");
        request.setCurrentPage(1);
        request.setPageSize(15);
        JsonEntity jsonEntity = adminNewsFlashController.getNewsFlashListByCondition(request,new PageModel<>());
        logger.info("测试结果："+ JSON.toJSONString(jsonEntity));
    }

    @Test
    public void detailNewsFlash() throws Exception {
        KgNewsFlashRequest request = new KgNewsFlashRequest();
        request.setNewsflashId(443779004412076032L);
        JsonEntity jsonEntity = adminNewsFlashController.detailNewsFlash(request);
        logger.info("测试结果："+ JSON.toJSONString(jsonEntity));
    }

}