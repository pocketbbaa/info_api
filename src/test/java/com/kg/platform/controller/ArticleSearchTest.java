package com.kg.platform.controller;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.request.ArticleRequest;
import com.kg.platform.model.response.ArticleResponse;
import com.kg.platform.service.ArticlekgService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ArticleSearchTest extends BaseTest {

    @Autowired
    private ArticlekgService articlekgService;

    @Test
    public void test1() {
        ArticleRequest request = new ArticleRequest();
        PageModel<ArticleResponse> page = new PageModel<>();
        request.setArticleTitle("测试");
        page.setCurrentPage(1);
        page.setPageSize(100);
        PageModel<ArticleResponse> responsePageModel = articlekgService.getSearchArticle(request, page);
        System.err.println(JSONObject.toJSONString(responsePageModel));
    }


}
