package com.kg.platform.controller;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.request.AccountFlowAppRequest;
import com.kg.platform.model.request.AccountFlowRequest;
import com.kg.platform.model.request.AccountRequest;
import com.kg.platform.model.response.AccountFlowResponse;
import com.kg.platform.model.response.AccountFlowResponse130;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2018/6/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/spring-context*.xml" })
public class AccountFlowControllerTest {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AccountFlowController accountFlowController;
    @Test
    public void detailBill() throws Exception {
        AccountFlowAppRequest request = new AccountFlowAppRequest();
        request.setType(1);
        request.setAccountFlowId(395984516835057664L);
        JsonEntity jsonEntity = accountFlowController.detailBill(request);
        logger.info("測試結果"+ JSON.toJSONString(jsonEntity));
    }

    @Test
    public void selectUserTxbflow() throws Exception {
        PageModel<AccountFlowResponse130> page=new PageModel<>();
        page.setCurrentPage(8);
        page.setPageSize(25);

        AccountFlowRequest request=new AccountFlowRequest();
        request.setUserId(433657149114163200L);
        request.setCurrentPage(1);
        //request.setType("2");

        JsonEntity jsonEntity = accountFlowController.selectUserTxbflow(request,page);
        logger.info("---------測試結果--------------"+ JSON.toJSONString(jsonEntity));
    }

}