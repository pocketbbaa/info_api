package com.kg.platform.controller;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.controller.admin.UserController;
import com.kg.platform.model.request.UserProfileRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2018/6/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/spring-context*.xml" })
public class UserProfileControllerTest {

    @Test
    public void getProfile() throws Exception {
        UserProfileRequest request = new UserProfileRequest();
        request.setUserId(425717878151585792L);
        JsonEntity result = userProfileController.getProfile(request,new MockHttpServletRequest());
        logger.info("测试结果："+ JSON.toJSONString(result));
    }


    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserProfileController userProfileController;

    @Autowired
    private UserController userController;

    @Test
    public void getUserproFile() throws Exception {
        JsonEntity jsonEntity = userProfileController.getUserproFile();
        logger.info("测试结果："+ JSON.toJSONString(jsonEntity));
    }

    @Test
    public void selectByuserprofileId() throws Exception {
        UserProfileRequest request = new UserProfileRequest();
        request.setArticleId(450979690111479808L);
        JsonEntity jsonEntity = userProfileController.selectByuserprofileId(request);
    }

    @Test
    public void updateColumnName(){
        UserProfileRequest request = new UserProfileRequest();
        request.setUserId(461854083565924352L);
        request.setColumnName("test name");
        System.out.println(userController.updateColumnName(request));
    }


}
