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
 * Created by Administrator on 2018/6/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/spring-context*.xml" })
public class TickerControllerTest {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private TickerController tickerController;
    @Test
    public void getTicker() throws Exception {
        JsonEntity jsonEntity = tickerController.getTicker();
        logger.info("测试："+ JSON.toJSONString(jsonEntity));
    }

}