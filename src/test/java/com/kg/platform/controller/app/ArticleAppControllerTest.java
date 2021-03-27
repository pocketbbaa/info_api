package com.kg.platform.controller.app;

import java.util.List;

import javax.inject.Inject;

import com.kg.platform.model.request.AccountRequest;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.utils.PropertyLoader;
import com.kg.platform.controller.BaseTest;
import com.kg.platform.dao.read.UserCommentRMapper;
import com.kg.platform.model.in.UserCommentInModel;
import com.kg.platform.model.out.UserCommentOutModel;
import com.kg.platform.model.request.ArticleRequest;
import com.kg.platform.model.request.UserCollectRequest;
import com.kg.platform.model.request.UserCommentRequest;
import com.kg.platform.model.response.ArticleAppResponse;
import com.kg.platform.model.response.UserCommentResponse;
import com.kg.platform.model.response.UserkgResponse;

public class ArticleAppControllerTest extends BaseTest {

    protected Logger logger = LoggerFactory.getLogger(ArticleAppControllerTest.class);

    @Inject
    private ArticleAppController articleAppController;

    @Inject
    private PropertyLoader propertyLoader;

    @Inject
    private UserCommentRMapper userCommentRMapper;

    @Test
    public void recommendArticles() {

        String msg = StringUtils.replaceAll(propertyLoader.getProperty("message", "sms.validatecount"), "######","12387456");
        System.err.println("=========="+msg);

        ArticleRequest request = new ArticleRequest();
        request.setCurrentPage(1);
        PageModel<ArticleAppResponse> page = new PageModel<>();
        page.setPageSize(10);
        AppJsonEntity appJsonEntity = articleAppController.recommendArticles(request, page);
        System.err.println(JSONObject.toJSONString(appJsonEntity));
    }

    @Test
    public void detailArticle() {
        ArticleRequest articleRequest = new ArticleRequest();
        articleRequest.setArticleId(5L);
        articleRequest.setUserId("1");
        UserkgResponse userkgResponse = new UserkgResponse();
        userkgResponse.setUserId("1");
        AppJsonEntity appJsonEntity = articleAppController.detailArticle(articleRequest, new MockHttpServletRequest());
        System.err.println(JSONObject.toJSONString(appJsonEntity));
    }

    @Test
    public void getComments() {
        UserCommentRequest userCommentRequest = new UserCommentRequest();
        PageModel<UserCommentResponse> page = new PageModel<>();
        userCommentRequest.setArticleId("424242553257336832");
        userCommentRequest.setCurrentPage(1);
        userCommentRequest.setPageSize(20);
        AppJsonEntity appJsonEntity = articleAppController.getComments(userCommentRequest, page);

        System.err.println(JSONObject.toJSONString(appJsonEntity));

    }

    @Test
    public void getCommentsDaoTest() {
        UserCommentInModel userCommentInModel = new UserCommentInModel();

        userCommentInModel.setStart(0);
        userCommentInModel.setLimit(20);
        userCommentInModel.setUserId(395260122580000768L);
        List<UserCommentOutModel> list = userCommentRMapper.getUserCommentForApp(userCommentInModel);

        System.err.println(JSONObject.toJSONString(list));
    }

    @Test
    public void getCommentsWithUserId() {
        UserCommentRequest request = new UserCommentRequest();
        request.setUserId("395260122580000768");
        request.setCurrentPage(1);
        PageModel<UserCommentResponse> page = new PageModel<>();
        page.setPageSize(10);
        AppJsonEntity appJsonEntity = articleAppController.getCommentsWithUserId(request, page);
        System.out.println(JSONObject.toJSONString(appJsonEntity));
    }

    @Test
    public void recommendForYou() {
        ArticleRequest articleRequest = new ArticleRequest();
        articleRequest.setArticleId(26L);
        JSONObject json = articleAppController.recommendForYou(articleRequest);
        System.err.println(json.toString());
    }

    @Test
    public void articleRecommend() {
        ArticleRequest articleRequest = new ArticleRequest();
        articleRequest.setArticleId(442989020633505792L);
        JSONObject json = articleAppController.articleRecommend(articleRequest);
        System.err.println(json.toString());
    }

    @Test
    public void test() {
        String str = propertyLoader.getProperty("redis.properties", "redis.host");
        System.err.println(str);
    }

    @Test
    public void addCollect() throws Exception {
        UserCollectRequest request = new UserCollectRequest();
        request.setArticleId(395573250555518976l);
        request.setUserId(426071933906460672l);
        request.setOperType(2);
        JSONObject result = articleAppController.addCollect(request, new MockHttpServletRequest());
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void commentArticle() {
        UserCommentRequest request = new UserCommentRequest();
        UserkgResponse userToken = new UserkgResponse();
        userToken.setUserId("1");

        // request.setCommentStatus(1);
        request.setCommentDesc("评论内容炸学校后勤集团");
        request.setArticleId("424242553257336832");
        AppJsonEntity appJsonEntity = articleAppController.commentArticle(request, userToken);

        System.err.println(JSONObject.toJSONString(appJsonEntity));
    }


    @Test
    public void shareBonus() {
        UserkgResponse kguser=new UserkgResponse();
        kguser.setUserId("433657149114163200");
        MockHttpServletRequest  servletRequest=new MockHttpServletRequest();
        servletRequest.addHeader("device_id","123456");
        servletRequest.addHeader("os_version",1);

        AccountRequest request=new AccountRequest();
        request.setArticleId(484805567232217088L);

        articleAppController.shareBonus(request,servletRequest);
    }

    @Test
    public void readBonus() {
        UserkgResponse kguser=new UserkgResponse();
        kguser.setUserId("433657149114163200");
        MockHttpServletRequest  servletRequest=new MockHttpServletRequest();
        servletRequest.addHeader("device_id","123456");
        servletRequest.addHeader("os_version",1);

        AccountRequest request=new AccountRequest();
        request.setArticleId(484805567232217088L);
        request.setUserId(433657149114163200L);
        articleAppController.readBonus(request,servletRequest);
    }




    /**
     * // bonusType:1:首次浏览文章，2：首次点赞文章，3首次收藏文章，41：首次分享成功至微信或微博，
     * 42：首次分享成功至微信，43：首次分享成功至微博 if (null != request.getSource()) { if
     * ("Weixin".equals(request.getSource())) {
     * request.setBonusSecondType("42"); } if
     * ("Weibo".equals(request.getSource())) { request.setBonusSecondType("43");
     * } if ("WbAndWx".equals(request.getSource())) {
     * request.setBonusSecondType("41"); } request.setBonusType(null);
     */
    // @Test
    // public void updateUserbalance() {
    // AccountRequest accountRequest = new AccountRequest();
    // accountRequest.setUserId(1L);
    // accountRequest.setArticleId(1L);
    // accountRequest.setSource("Weixin");
    // accountRequest.setBonusSecondType("1");
    // accountRequest.setBonusType("1");
    //
    // UserkgResponse response = new UserkgResponse();
    // response.setUserId("1");
    // AppJsonEntity appJsonEntity =
    // articleAppController.updateUserbalance(accountRequest, response,new
    // MockHttpServletRequest());
    // System.err.println(JSONObject.toJSONString(appJsonEntity));
    // }
}
