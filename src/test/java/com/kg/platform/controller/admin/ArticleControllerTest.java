package com.kg.platform.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.model.request.admin.ArticleBatchReviewRequest;
import com.kg.platform.model.request.admin.ArticleQueryRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2018/5/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring-context*.xml"})
public class ArticleControllerTest {


    @Test
    public void getArticleById() throws Exception {
        ArticleQueryRequest request = new ArticleQueryRequest();
        request.setArticleId("442988993387307008");
        JsonEntity jsonEntity = articleController.getArticleById(request);
        logger.info("测试结果："+JSON.toJSONString(jsonEntity));
    }

    @Test
    public void getArticleList() throws Exception {
        ArticleQueryRequest request = new ArticleQueryRequest();
        request.setCurrentPage(1);
        request.setPageSize(5);
        request.setStartDay("2017-01-01");
        request.setEndDay("2018-01-01");
        JsonEntity jsonEntity = articleController.getArticleList(request);
        logger.info("测试结果：" + JSON.toJSONString(jsonEntity));
    }

    @Autowired
    ArticleController articleController;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void getPushAticleInfo() throws Exception {

        JsonEntity jsonEntity = articleController.getPushAticleInfo();
        logger.info("测试结果：" + JSON.toJSONString(jsonEntity));
    }

    @Test
    public void batchReviewArticle() {
        ArticleBatchReviewRequest request = new ArticleBatchReviewRequest();

        request.setArticleIds("461132567433883648,461098812883251200,461095263789035520");
        request.setAuditUser("1");
        request.setColumnId(1);
        request.setIfPlatformPublishAward(1);
        request.setPublicStatus(3);
        JsonEntity json = articleController.batchReviewArticle(request);
        System.err.println(JSONObject.toJSONString(json));
    }


    @Test
    public void getArticle() throws Exception {
        ArticleQueryRequest request = new ArticleQueryRequest();
        request.setArticleTitle("虐童事件引发广");
        JsonEntity jsonEntity =  articleController.getArticles(request);
        logger.info("测试结果："+JSON.toJSONString(jsonEntity));
    }

}