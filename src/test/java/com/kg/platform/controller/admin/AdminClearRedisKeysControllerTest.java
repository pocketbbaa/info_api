/*
package com.kg.platform.controller.admin;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.model.request.admin.AdminRedisUtilRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

*/
/**
 * Created by Administrator on 2018/8/3.
 *//*

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/spring-context*.xml" })
public class AdminClearRedisKeysControllerTest {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdminClearRedisKeysController adminClearRedisKeysController;
    @Test
    public void clearRedisKeys() throws Exception {
        AdminRedisUtilRequest redisUtilRequest = new AdminRedisUtilRequest();
        redisUtilRequest.setUserId("123456789");
        redisUtilRequest.setAccess("5682c10513bdc30ddc6641bdf04be3b1");
        JsonEntity jsonEntity =  adminClearRedisKeysController.clearRedisKeys(redisUtilRequest);
        logger.info("测试结果：" + JSON.toJSONString(jsonEntity));
    }

}*/
