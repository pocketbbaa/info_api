package com.kg.platform.test.junit5;

import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.model.request.admin.ArticleQueryRequest;
import com.kg.platform.model.response.admin.ArticleQueryResponse;
import com.kg.platform.service.admin.ArticleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "classpath*:/spring-context*.xml")
@WebAppConfiguration
public class SpringTest {
    private static final Logger logger = LoggerFactory.getLogger(SpringTest.class);

    @Inject
    private ArticleService articleService;

    @Test
    public void getArticleList() {
        ArticleQueryRequest request = new ArticleQueryRequest();
        request.setBlockchainUrl(0);

        PageModel<ArticleQueryResponse> page = new PageModel<>();
        page.setCurrentPage(1);
        page.setPageSize(2000000);

        logger.error("==========begin");
        page = articleService.getArticleList(request, page);
        page.getData().forEach(article -> {
            if (StringUtils.isNotBlank(article.getBlockchainUrl()))
                logger.info(article.getBlockchainUrl());
        });
        logger.error("==========end");
    }

    @Test
    public void testSetBlockchainUrl() {
        articleService.setBlockchainUrl("5", "");
        articleService.setBlockchainUrl("437577031094509568", "https://www.btc123.com");
        articleService.setBlockchainUrl("3", "https://m.kg.com");

        logger.error("success");
    }
}
