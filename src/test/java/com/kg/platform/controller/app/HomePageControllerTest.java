package com.kg.platform.controller.app;

import javax.inject.Inject;

import com.alibaba.fastjson.JSON;
import com.kg.platform.model.request.SearchRequest;
import com.kg.platform.service.m.MArticleService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.controller.BaseTest;
import com.kg.platform.model.request.ArticleRequest;
import com.kg.platform.model.request.KeywordsRequest;
import com.kg.platform.model.request.SiteimageAppRequest;
import com.kg.platform.model.request.admin.AppVersionManageRequest;
import com.kg.platform.model.response.ArticleAppResponse;
import com.kg.platform.model.response.ArticleResponse;
import com.kg.platform.model.response.UserkgResponse;

public class HomePageControllerTest extends BaseTest {
    @Test
    public void getVideoTabInfo() throws Exception {
        ArticleRequest request = new ArticleRequest();
        request.setCurrentPage(1);
        request.setPageSize(20);
        JSONObject result = homePageController.getVideoTabInfo(request, new PageModel<>(),new MockHttpServletRequest());
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void getV2ArticlesWithType() throws Exception {
        ArticleRequest request = new ArticleRequest();
        request.setUserId("428596656615858176");
        PageModel<ArticleResponse> page = new PageModel<>();
        request.setColumnId("-2");
        request.setSecondColumn(null);
        request.setArticleTagnames(null);
        request.setCurrentPage(1);
        request.setPageSize(20);
        JSONObject result = homePageController.getV2ArticlesWithType(request, page, new MockHttpServletRequest());
        logger.info("测试结果：" + result.toJSONString());
    }

    private Logger logger = LoggerFactory.getLogger(HomePageControllerTest.class);

    @Inject
    private HomePageController homePageController;

    @Inject
    private AppVersionController appVersionController;

    @Inject
    private MArticleService mArticleService;

	@Test
    public void test(){
		logger.info("測試結果："+JSON.toJSONString(mArticleService.getTopMenus()));
	}

    @Test
    public void ifHaveNotRead() throws Exception {
        UserkgResponse response = new UserkgResponse();
        JSONObject result = homePageController.ifHaveNotRead(response);
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void getPushedInfo() throws Exception {
        UserkgResponse userkgResponse = new UserkgResponse();
        PageModel pageModel = new PageModel();
        pageModel.setCurrentPage(1);
        pageModel.setPageSize(5);
        JSONObject result = homePageController.getPushedInfo(userkgResponse, pageModel);
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void getBanner() {

        SiteimageAppRequest siteimageRequest = new SiteimageAppRequest();
        /**
         * Integer columnId; //栏目ID Integer image_type;// 图片类型 1 资讯 2 广告 3 其他
         * Integer navigator_pos;// 展示位置 1 首页 2 栏目列表 3 频道页 4 资讯详情 Integer
         * image_pos;// 对应具体位置
         */
        siteimageRequest.setColumnId(-1);
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.addHeader("os_version", 1);
        JSONObject json = homePageController.getBanner(siteimageRequest, servletRequest);
        System.err.println(json.toString());
    }

    @Test
    public void searchArticle() {
        ArticleRequest articleRequest = new ArticleRequest();
        PageModel<ArticleAppResponse> page = new PageModel<>();
        articleRequest.setArticleTitle("区块");
        articleRequest.setCurrentPage(1);
        page.setPageSize(3);
        AppJsonEntity json = homePageController.searchArticle(articleRequest, page);

        System.err.println(JSONObject.toJSONString(json));
    }

    @Test
    public void search(){
        SearchRequest request = new SearchRequest();
        request.setCurrentPage(1);
        request.setPageSize(2);
        request.setType(1);
        request.setSearchStr("B");
        AppJsonEntity jsonEntity = homePageController.search(request);
        System.err.println(JSONObject.toJSONString(jsonEntity));
    }

    @Test
    public void getTopMenus() throws Exception {
        JSONObject result = homePageController.getTopMenus();
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void getArticlesWithType() throws Exception {
        ArticleRequest request = new ArticleRequest();
        PageModel<ArticleResponse> page = new PageModel<>();
        request.setColumnId(null);
        request.setSecondColumn(null);
        request.setCurrentPage(1);
        request.setPageSize(50);
        JSONObject result = homePageController.getArticlesWithType(request, page, new MockHttpServletRequest());
        logger.info("测试结果：" + result.toJSONString());
    }

    @Test
    public void getKeywordsAll() {
        // 0为首页 否则为 频道页ID
        KeywordsRequest request = new KeywordsRequest();
        request.setSecondChannel(0);
        JSONObject json = homePageController.getkeywords(request);
        System.err.println(json.toString());
    }

    @Test
    public void getLastVersion() {
        AppVersionManageRequest appVersionManageRequest = new AppVersionManageRequest();
        appVersionManageRequest.setSystemType(2);
        AppJsonEntity appJsonEntity = appVersionController.getLastVersion(appVersionManageRequest);
        System.err.println(JSONObject.toJSONString(appJsonEntity));
    }

    @Test
    public void getKeyWorks() {
        KeywordsRequest request = new KeywordsRequest();
        request.setSecondChannel(1);
        JSONObject jsonObject = homePageController.getkeywords(request);
        System.err.println(jsonObject.toString());
    }
}
