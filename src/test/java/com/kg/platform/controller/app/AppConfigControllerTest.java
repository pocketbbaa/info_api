package com.kg.platform.controller.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.utils.JedisKey;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.model.TokenModel;
import com.kg.platform.model.request.admin.AppVersionManageRequest;
import com.kg.platform.service.TokenManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.Set;

/**
 * Created by Administrator on 2018/3/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/spring-context*.xml" })
public class AppConfigControllerTest {

    @Autowired
    private JedisUtils jedisUtils;

    @Test
    public void getInviteRule() throws Exception {
        JSONObject jsonObject = appConfigController.getInviteRule();
        logger.info("测试结果："+jsonObject.toJSONString());
    }

    private static final Logger logger = LoggerFactory.getLogger(AppConfigControllerTest.class);
    @Inject
    private IDGen idGenerater;
    @Autowired
    private AppConfigController appConfigController;
    @Autowired
    private TokenManager tokenManager;
    @Test
    public void getAppConfig() throws Exception {
        JSONObject jsonObject = appConfigController.getAppConfig(new AppVersionManageRequest(),new MockHttpServletRequest());
        logger.info(jsonObject.toJSONString());
    }
    @Test
    public void gerID(){
        String pattern = JedisKey.getArticlePageKeyPattern("360");
        jedisUtils.delKeys(pattern, Lists.newArrayList());
    }

    @Test
    public void gtoken(){
        TokenModel tokenModel = tokenManager.createAppToken(436468820715118592L);
        logger.info("测试结果：{}", JSON.toJSONString(tokenModel));
    }

}