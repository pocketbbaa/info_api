package com.kg.platform.controller.admin;

import com.kg.platform.common.utils.JsonUtil;
import com.kg.platform.model.request.admin.PublishBonusQueryRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring-context*.xml"})
public class BonusControllerTest {

    @Autowired
    private  BonusController  bonusController;

    @Test
    public void publishArticleBonus() {
        PublishBonusQueryRequest publishBonusQueryRequest=new PublishBonusQueryRequest();
        publishBonusQueryRequest.setCurrentPage(1);
        publishBonusQueryRequest.setPageSize(10);
        System.out.println("----data------"+ JsonUtil.writeValueAsString(bonusController.publishArticleBonus(publishBonusQueryRequest)));
    }
}