package com.kg.platform.simhash;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.similarity.SimilarityResultModel;
import com.kg.platform.common.similarity.TextSimilarity;
import com.kg.platform.controller.BaseTest;
import com.kg.platform.service.ArticlekgService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.Set;

public class TextSimilarityText extends BaseTest {

    @Autowired
    private ArticlekgService articleService;

    @Test
    public void test1() {
        long start = System.currentTimeMillis();

        int total = articleService.getSimHashTotal();
        System.out.println("total : " + total);

        TextSimilarity textSimilarity = new TextSimilarity.Builde()
                .setCountTotal(total)
                .setSimHashCode(new BigInteger("18081720030363125979"))
                .setThreadCount(10)
                .setArticleService(articleService)
                .setPercent(0.7)
                .build();
        textSimilarity.start();
        Set<SimilarityResultModel> similarityResultModels = textSimilarity.getSimilarityResultSet();

        System.out.println("计算结束，耗时：" + (System.currentTimeMillis() - start) + "  数量：" + similarityResultModels.size());
        for (SimilarityResultModel model : similarityResultModels) {
            System.out.println(JSONObject.toJSONString(model));
        }
    }

}
