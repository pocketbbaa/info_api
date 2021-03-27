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
 * Created by Administrator on 2018/6/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/spring-context*.xml" })
public class VideoControllerTest {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private VideoController videoController;
    @Test
    public void hotVideoList() throws Exception {
        JsonEntity result = videoController.hotVideoList();
        logger.info("测试结果："+ JSON.toJSONString(result));
    }

}