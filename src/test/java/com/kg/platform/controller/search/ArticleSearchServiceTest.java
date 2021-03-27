package com.kg.platform.controller.search;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.controller.BaseTest;
import com.kg.search.model.result.BaseResult;
import com.kg.search.service.ArticleSearchService;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class ArticleSearchServiceTest extends BaseTest {

    @Autowired
    private ArticleSearchService articleSearchService;


    @Test
    public void testSearch() {
        BaseResult baseResult = articleSearchService.searchArticle("B", 1, 0, 20);
        SearchHits hits = (SearchHits) baseResult.getResponseBody();
        for (SearchHit hit : hits) {
            Map<String, HighlightField> highlightFieldMap = hit.getHighlightFields();
            HighlightField articleTitle = highlightFieldMap.get("articleTitle");
            HighlightField createUserName = highlightFieldMap.get("createUserName");
            Text[] texts = articleTitle.getFragments();
            System.out.println("texts:" + texts[0].toString());
            System.err.println(JSONObject.toJSONString(hit.getSourceAsString()));
        }
    }


    @Test
    public void test1() {
        BaseResult baseResult = articleSearchService.articleIndexList(0, 0, 20);
        SearchHits hits = (SearchHits) baseResult.getResponseBody();
        for (SearchHit hit : hits) {
            System.err.println(JSONObject.toJSONString(hit.getSourceAsString()));
        }

    }

}
