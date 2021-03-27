package com.kg.platform.controller.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.mongo.MongoTables;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.utils.Bean2MapUtil;
import com.kg.platform.model.mongoTable.AccountWithdrawFlowRit;
import com.kg.platform.model.request.AccountWithDrawRequest;
import com.kg.platform.model.response.UserkgResponse;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2018/9/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring-context*.xml"})
public class AppAccountControllerTest {
    @Test
    public void cancelWithdraw() throws Exception {
        AccountWithDrawRequest request = new AccountWithDrawRequest();
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("436468820715118592");
        kguser.setUserName("LP");
        request.setCoinType(3);
        request.setWithdrawFlowId("489101334876917760");
        AppJsonEntity result = appAccountController.cancelWithdraw(kguser, request);
        logger.info("测试结果：{}", JSON.toJSONString(result));
    }

    @Test
    public void withdrawByCoinType() throws Exception {
        AccountWithDrawRequest request = new AccountWithDrawRequest();
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("449168532492693504");
        request.setCoinType(3);
        request.setMemo("heihei");
        request.setTxAddress("TVDDDDDDDDDDDDDDDDDDDDDDD");
        request.setTxAmount(new BigDecimal("33"));
        request.setTxPassword("123456");
        request.setCode("1234");
        for (int i = 0; i < 200; i++) {
            AppJsonEntity result = appAccountController.withdrawByCoinType(kguser, request);
            logger.info("测试结果：{}------第{}条", JSON.toJSONString(result), i + 1);
        }
    }

    @Autowired
    private AppAccountController appAccountController;

    @Autowired
    private IDGen idGen;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void withdrawPageData1() throws Exception {
        UserkgResponse request = new UserkgResponse();
        request.setUserId("436468820715118592");
        AppJsonEntity result = appAccountController.withdrawPageData(request);
        logger.info("测试结果：{}", JSON.toJSONString(result));
    }

    @Test
    public void initTable() {
        AccountWithdrawFlowRit accountWithdrawFlowRit = new AccountWithdrawFlowRit();
        accountWithdrawFlowRit.setWithdrawFlowId(idGen.nextId());
        accountWithdrawFlowRit.setUserId(436468820715118592L);
        accountWithdrawFlowRit.setUserName("llp");
        accountWithdrawFlowRit.setStatus(0);
        accountWithdrawFlowRit.setWithdrawAmount(new BigDecimal("30.33"));
        accountWithdrawFlowRit.setAccountAmount(new BigDecimal("27.33"));
        accountWithdrawFlowRit.setMemo("xixi2222");
        accountWithdrawFlowRit.setToAddress("TVKASJDKFJALSDJFLKAJSFLKdddd2222");
        accountWithdrawFlowRit.setWithdrawTime(new Date().getTime());
        accountWithdrawFlowRit.setPoundageAmount(new BigDecimal("3"));
        MongoUtils.insertOne(MongoTables.ACCOUNT_WITHDRAW_FLOW_RIT, new Document(Bean2MapUtil.bean2map(accountWithdrawFlowRit)));
    }


    @Test
    public void performanceEarnings() {
        UserkgResponse response = new UserkgResponse();
        response.setUserId("454360269037154304");
        AppJsonEntity jsonEntity = appAccountController.performanceEarnings(response);
        System.err.println(JSONObject.toJSONString(jsonEntity));
    }


}