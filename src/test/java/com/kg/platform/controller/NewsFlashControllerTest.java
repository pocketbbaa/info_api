package com.kg.platform.controller;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.entity.JsonEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2018/6/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/spring-context*.xml" })
public class NewsFlashControllerTest {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NewsFlashController newsFlashController;
    @Test
    public void websocketNewsFlash() throws Exception {
        JsonEntity jsonEntity = newsFlashController.websocketNewsFlash();
        logger.info("测试结果："+ JSON.toJSONString(jsonEntity));
    }

}