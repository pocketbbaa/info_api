package com.kg.platform.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.utils.MD5Util;
import com.kg.platform.common.utils.UserTagsUtil;
import com.kg.platform.model.TokenModel;
import com.kg.platform.model.UserTagBuild;
import com.kg.platform.model.request.UserkgRequest;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.TokenManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/3/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring-context*.xml"})
public class UserKgAppControllerTest {


    private static final Logger logger = LoggerFactory.getLogger(UserKgAppControllerTest.class);
    @Inject
    private UserKgAppController userKgAppController;
    @Inject
    private TokenManager tokenManager;
    @Inject
    private UserTagsUtil userTagsUtil;

    @Test
    public void register() throws Exception {
        UserkgRequest request = new UserkgRequest();
        request.setCall_method("adduser");
        request.setUserName("llp");
        request.setUserMobile("17602883379");
        request.setCode("155845");
        request.setMobileArea("86");
        MockHttpServletRequest request1 = new MockHttpServletRequest();
        JSONObject result = userKgAppController.register(request, request1);
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void sendSmsCode() throws Exception {
        UserkgRequest request = new UserkgRequest();
        request.setVerIfy("17602883379");
        request.setMobileArea("86");
        request.setValiDation("2");
        MockHttpServletRequest request1 = new MockHttpServletRequest();
        JSONObject result = userKgAppController.sendSmsCode(request, request1);
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void chckSmsEmailCode() throws Exception {
        UserkgRequest request = new UserkgRequest();
        request.setUserMobile("17602883379");
        request.setCode("155845");
        JSONObject result = userKgAppController.chckSmsEmailCode(request);
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void completeProfile() throws Exception {
        UserkgRequest request = new UserkgRequest();
        request.setUserMobile("17602883379");
        request.setCode("867943");
        request.setAvatar("xixixixi");
        request.setUserName("lilianpeng");
        request.setUserPassword("123456");
        request.setConfirmPassword("123456");
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("426071933906460672");
        JSONObject result = userKgAppController.completeProfile(request, kguser, servletRequest);
        logger.info("测试结果：" + result.toJSONString());
    }


    @Test
    public void tokenApp() {
        TokenModel model = tokenManager.createAppToken(436468820715118592L);
        System.err.println(JSONObject.toJSONString(model));
    }

    @Test
    public void token() {
        TokenModel model = tokenManager.createToken(436468820715118592L);
        System.err.println(JSONObject.toJSONString(model));
    }

    @Test
    public void sign() {
        String sign = MD5Util.md5Hex("eyJ1c2VySWQiOiI0NTQzNjAyNjkwMzcxNTQzMDQiLCJjaGFubmVsSWQiOiIxIiwicGFja2FnZUlkIjoxLCJudW1iZXIiOjEsInRvdGFsUHJpY2UiOjQwMCwidG90YWxQZXJmb3JtYW5jZSI6MX0=" + "454360269037154304_a1fc66e95385474f98a926fefdc598af");
        System.err.println(sign);
    }

    @Test
    public void tag() {
        UserTagBuild userTagBuild = userTagsUtil.buildTags(1L);
        System.out.println(JSONObject.toJSONString(userTagBuild));
    }


    public static void main(String[] args) {
        String sign = MD5Util.md5Hex("eyJ2ZXJJZnkiOiIxNTIwMTAzMzA5NiIsInZhbGlEYXRpb24iOjJ9");
        System.err.println(sign);
    }


}