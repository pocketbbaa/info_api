package com.kg.platform.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.utils.Constants;
import com.kg.platform.controller.BaseTest;
import com.kg.platform.dao.entity.SysUser;
import com.kg.platform.model.in.FreezeInModel;
import com.kg.platform.model.request.AccountWithDrawRequest;
import com.kg.platform.model.request.admin.AccountQueryRequest;
import com.kg.platform.model.request.admin.RitExchangeQueryRequest;
import com.kg.platform.model.request.admin.UserBonusQueryRequest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AccountControllerTest extends BaseTest {
    @Test
    public void batchAuditRitWithdraw() throws Exception {
        AccountWithDrawRequest request = new AccountWithDrawRequest();
        request.setWithdrawFlowIds("[\"491253931507322880\",\"491253832500776960\"]");
        request.setAuditRole(1);
        request.setStatus(1);
        request.setAuditRemark("passpass");
        SysUser sysUser = new SysUser();
        sysUser.setSysUserName("super_admin--dd");
        sysUser.setSysUserId(-999L);
        JsonEntity result = accountController.batchAuditRitWithdraw(request,sysUser,new MockHttpServletRequest());
        logger.info("测试结果:{}", JSON.toJSONString(result));
    }

    @Test
    public void manualTransfer() throws Exception {
        AccountWithDrawRequest request = new AccountWithDrawRequest();
        request.setUserId("400636375558397952");
        request.setMemo("oooooooooooooooo");
        request.setCoinType(3);
        request.setTxAddress("0X123ALKSJDFLKAJSLDKJRLKAJ12321");
        request.setTxAmount(new BigDecimal("60.5"));
        SysUser sysUser = new SysUser();
        sysUser.setSysUserName("super_admin--dd");
        sysUser.setSysUserId(-999L);
        JsonEntity result = accountController.manualTransfer(request,sysUser,new MockHttpServletRequest());
        logger.info("测试结果:{}", JSON.toJSONString(result));
    }

    @Test
    public void auditRitWithdraw() throws Exception {
        AccountWithDrawRequest request = new AccountWithDrawRequest();
        request.setAuditRole(2);
        request.setStatus(3);
        request.setWithdrawFlowId("489493390254280704");
        request.setAuditRemark("成功了！！！");
        SysUser sysUser = new SysUser();
        sysUser.setSysUserName("super_admin——");
        sysUser.setSysUserId(-999L);
        JsonEntity result = accountController.auditRitWithdraw(request,sysUser,new MockHttpServletRequest());
        logger.info("测试结果:{}", JSON.toJSONString(result));
    }

    @Test
    public void withDrawSearchConditions() throws Exception {
        JsonEntity result = accountController.withDrawSearchConditions(new AccountWithDrawRequest());
        logger.info("测试结果:{}", JSON.toJSONString(result));
    }

    @Test
    public void detailRitWithdraw() throws Exception {
        AccountWithDrawRequest request = new AccountWithDrawRequest();
        request.setWithdrawFlowId("491278822956732416");
        JsonEntity result = accountController.detailRitWithdraw(request);
        logger.info("测试结果:{}", JSON.toJSONString(result));
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void getRitWithdrawList() throws Exception {
        AccountWithDrawRequest request = new AccountWithDrawRequest();
        request.setCurrentPage(1);
        request.setPageSize(100);
        request.setAuditRole(1);
        request.setStatus(6);
        JsonEntity result = accountController.getRitWithdrawList(request,new PageModel<>());
        logger.info("测试结果:{}", JSON.toJSONString(result));
    }

    @Autowired
    private AccountController accountController;

    @Test
    public void getAccountList() {
        AccountQueryRequest accountQueryRequest = new AccountQueryRequest();
        accountQueryRequest.setCurrentPage(1);
        accountQueryRequest.setPageSize(25);
        accountQueryRequest.setBusinessTypeId(null);
        accountQueryRequest.setMinAmount(null);
        accountQueryRequest.setMaxAmount(null);
        System.out.println(accountController.getAccount(accountQueryRequest));
    }

    @Test
    public void getAccountRitList() throws ParseException {
        AccountQueryRequest accountQueryRequest = new AccountQueryRequest();
        accountQueryRequest.setCurrentPage(1);
        accountQueryRequest.setPageSize(25);
        accountQueryRequest.setBusinessTypeId(null);
        accountQueryRequest.setMinAmount("0");
        accountQueryRequest.setMaxAmount("10");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        accountQueryRequest.setStartDate(sdf.parse("2018-09-06 14:00:00"));
        accountQueryRequest.setEndDate(sdf.parse("2018-09-07 14:00:00"));
        accountQueryRequest.setMobile(null);
        //accountQueryRequest.setSearchType("bonus");
        System.out.println(accountController.getRitAccount(accountQueryRequest));
    }

    @Test
    public void getUserBonusList() {
        UserBonusQueryRequest accountQueryRequest = new UserBonusQueryRequest();
        accountQueryRequest.setCurrentPage(1);
        accountQueryRequest.setPageSize(25);
        accountQueryRequest.setStartRitBonus(new BigDecimal(25));
        accountQueryRequest.setEndRitBonus(new BigDecimal(30));
        System.out.println(accountController.getUserBonusList(accountQueryRequest));
    }



    @Test
    public void getUserBonusDetail() {
        UserBonusQueryRequest accountQueryRequest = new UserBonusQueryRequest();
        accountQueryRequest.setExtraBonusId("481881924793667584");
        System.out.println(accountController.getUserBonusDetail(accountQueryRequest));
    }


    @Test
    public void getRitExchangeList() {
        RitExchangeQueryRequest request = new RitExchangeQueryRequest();
        request.setCurrentPage(2);
        request.setPageSize(10);
        JsonEntity jsonEntity = accountController.getRitExchangeList(request);
        System.err.println(JSONObject.toJSONString(jsonEntity));
    }

    @Test
    public void getRitExchangeAmount(){
        RitExchangeQueryRequest request = new RitExchangeQueryRequest();
        request.setCurrentPage(1);
        request.setPageSize(20);
        JsonEntity jsonEntity = accountController.getRitExchangeAmount(request);
        System.err.println(JSONObject.toJSONString(jsonEntity));
    }

    @Test
    public void getUserAssetInfo(){
        AccountWithDrawRequest request = new AccountWithDrawRequest();
        request.setUserId("436468820715118592");
        System.err.println(accountController.getUserAssetInfo(request));
    }

    @Test
    public void assetOperation(){
        SysUser sysUser = new SysUser();
        sysUser.setSysUserId(411267783704322048L);
        sysUser.setSysUserName("xiaoming");
        FreezeInModel request = new FreezeInModel();
        request.setAmount(new BigDecimal("10"));
        request.setCallMethod(Constants.FREEZE);
        request.setCause("我就想做个测试行不行");
        request.setUserId("477416041727791104");
        System.err.println(accountController.assetOperation(sysUser,request));
    }

    public static void main(String[] args) {
        FreezeInModel request = new FreezeInModel();
        request.setAmount(new BigDecimal("10.255455"));
        request.setCallMethod(Constants.RELVE);
        request.setCause("我就想做个测试行不行");
        request.setUserId("454646085915422720");
//        System.err.println(accountController.assetOperation(request));
        System.err.println(JSON.toJSONString(request));
    }

}
