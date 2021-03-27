package com.kg.platform.controller.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.model.request.admin.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import com.kg.platform.controller.BaseTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


public class UserBonusTest extends BaseTest {

    @Autowired
    private BonusController bonusController;

    @Autowired
    private AccountController accountController;

    /**
     * 检索发放用户奖励
     *
     * @param request
     * @return
     */
    @Test
    public void checkInfo() {
        UserBonusQueryRequest requst = new UserBonusQueryRequest();
        requst.setUserMobiles("15881155743,15881155743");
        System.out.println(">>>>>>" + bonusController.checkInfo(requst));
    }

    /**
     * 确认发放用户奖励
     *
     * @param
     * @return
     */
    @Test
    public void confirmBonus() {
        UserBonusQueryRequest requst = new UserBonusQueryRequest();
        requst.setUserMobiles("15881155743,15881155746");
        requst.setBonus(new BigDecimal(10));
        requst.setAwardType(1);
        requst.setAdminId(32);
        requst.setBonusReason("XXX活动");
        System.out.println(">>>>>>" + bonusController.confirmBonus(requst, new MockHttpServletRequest()));
    }


    @Test
    public void confirmBonusForFile() {
        UserBonusListRequest request = new UserBonusListRequest();
        request.setAdminId(32);
        request.setAwardType(1);

        List<UserBonus> list = new ArrayList<>();
        String[] userIds = {"411541162382467072", "401406087770808320", "13111111600131", "13111111567131"};
        for (String userId : userIds) {
            UserBonus userBonus = new UserBonus();
            userBonus.setUserId(userId);
            userBonus.setAmount(new BigDecimal(10));
            userBonus.setReason("任性:" + userId);
            list.add(userBonus);
        }

        request.setList(list);
        System.err.println(JSONObject.toJSONString(request));
        JsonEntity jsonEntity = bonusController.confirmBonusForFile(request);
        System.err.println(JSONObject.toJSONString(jsonEntity));
    }

    /**
     * 用户奖励列表
     *
     * @return
     */
    @Test
    public void getUserBonusList() {
        UserBonusQueryRequest requst = new UserBonusQueryRequest();
        requst.setPageSize(10);
        requst.setCurrentPage(1);
        requst.setNumStart(1);
        requst.setNumEnd(100);
        requst.setStartTime("2018-05-18");
        requst.setEndTime("2018-05-19");
        System.out.println(">>>>>>" + accountController.getUserBonusList(requst));
    }

    /**
     * 用户奖励详情
     *
     * @return
     */
    @Test
    public void getUserBonusDetail() {
        UserBonusQueryRequest requst = new UserBonusQueryRequest();
        requst.setExtraBonusId("446617622591709184");
        System.out.println(">>>>>>" + accountController.getUserBonusDetail(requst));
    }

    /**
     * 用户奖励详情列表
     *
     * @return
     */
    @Test
    public void getUserBonusDetailList() {
        UserBonusQueryRequest requst = new UserBonusQueryRequest();
        requst.setStartTime("2018-05-18");
        requst.setEndTime("2018-05-19");
        requst.setExtraBonusId("447392751873564672");
        System.out.println(">>>>>>" + accountController.getUserBonusDetailList(requst));
    }

    /**
     * 统计用户钛值 氪金奖励总额
     *
     * @return
     */
    @Test
    public void getSumUserBonus() {
        AccountQueryRequest requst = new AccountQueryRequest();
        requst.setMobile("15881155743");
        System.out.println(">>>---------------------getSumUserBonus------>>>" + accountController.getSumBonus(requst));
    }


    /**
     * 发文奖励列表
     *
     * @return
     */
    @Test
    public void publishArticleBonus() {
        PublishBonusQueryRequest request = new PublishBonusQueryRequest();
        request.setPageSize(10);
        request.setCurrentPage(1);
        request.setAuditStartDate("2018-08-20");
        request.setAuditEndDate("2018-08-23");
        System.out.println(">>>>>>" + bonusController.publishArticleBonus(request));
    }
}
