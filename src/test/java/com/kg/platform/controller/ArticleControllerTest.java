package com.kg.platform.controller;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.JsonEntity;

import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.request.ArticleRequest;
import com.kg.platform.model.response.UserkgResponse;

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
public class ArticleControllerTest {
    @Test
    public void getVideoTabInfo() throws Exception {
        ArticleRequest request = new ArticleRequest();
        request.setCurrentPage(1);
        request.setPageSize(20);
        JsonEntity result = articleController.getVideoTabInfo(request, new PageModel<>(),new MockHttpServletRequest());
        logger.info("测试结果：" + JSON.toJSONString(result));
    }

    @Test
    public void getChannelArt() throws Exception {
        ArticleRequest request = new ArticleRequest();
        request.setColumnId("360");
        request.setCurrentPage(1);
        request.setPageSize(20);
        JsonEntity jsonEntity = articleController.getChannelArt(request,new PageModel<>(),new MockHttpServletRequest());
        logger.info("測試結果:"+ JSON.toJSONString(jsonEntity));
    }

    @Autowired
    private ArticleController articleController;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());


    @Test
    public void getArticleContent() throws Exception {
        ArticleRequest request = new ArticleRequest();
        request.setArticleId(450979690111479808L);
        JsonEntity jsonEntity = articleController.getArticleContent(request);
        logger.info("測試結果:"+ JSON.toJSONString(jsonEntity));
    }

    @Test
    public void getSearchArticle() throws Exception {
        ArticleRequest request = new ArticleRequest();
        request.setCurrentPage(1);
        request.setPageSize(10);
        request.setArticleTitle("中国");
        request.setPublishKind(1);
        JsonEntity jsonEntity = articleController.getSearchArticle(request,new PageModel<>());
        logger.info("測試結果:"+ JSON.toJSONString(jsonEntity));
    }

    @Test
    public void getChannelAll() throws Exception {
        ArticleRequest request = new ArticleRequest();
        request.setColumnId("362");
        JsonEntity jsonEntity = articleController.getChannelAll(request);
        logger.info("測試結果:"+ JSON.toJSONString(jsonEntity));
    }

    @Test
    public void selectRelatedArticle() throws Exception {
        ArticleRequest request = new ArticleRequest();
        request.setArticleId(450979690111479808L);
        JsonEntity jsonEntity = articleController.selectRelatedArticle(request);
        logger.info("測試結果:"+ JSON.toJSONString(jsonEntity));
    }

    @Test
    public void selectByIdDetails() throws Exception {
        ArticleRequest request = new ArticleRequest();
        request.setArticleId(450979690111479808L);
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("406821628257906688");
        JsonEntity jsonEntity = articleController.selectByIdDetails(request,kguser);
        logger.info("測試結果:"+ JSON.toJSONString(jsonEntity));
    }

    @Test
    public void selectTopArticle() throws Exception {
        JsonEntity jsonEntity = articleController.selectTopArticle();
        logger.info("測試結果:"+ JSON.toJSONString(jsonEntity));
    }

    @Test
    public void selectArticleAll() throws Exception {
        ArticleRequest request = new ArticleRequest();
        request.setCurrentPage(1);
        request.setPageSize(20);
        PageModel pageModel = new PageModel<>();
        JsonEntity jsonEntity = articleController.selectArticleAll(request, pageModel,new MockHttpServletRequest());
        logger.info("測試結果:" + JSON.toJSONString(jsonEntity));

    }

    @Test
    public void encyclopediaList() throws Exception {
        JsonEntity jsonEntity = articleController.encyclopediaList();
        logger.info("测试结果："+ JSON.toJSONString(jsonEntity));

    }

}