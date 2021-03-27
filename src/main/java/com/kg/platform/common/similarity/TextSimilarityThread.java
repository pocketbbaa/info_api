package com.kg.platform.common.similarity;

import com.kg.platform.common.similarity.utils.SimHashUtil;
import com.kg.platform.model.request.BaseRequest;
import com.kg.platform.service.ArticlekgService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.util.List;

@Component
public class TextSimilarityThread implements Runnable {

    private int pageIndex;
    private int pageSize;
    private TextSimilarity textSimilarity;
    private ArticlekgService articleService;

    public TextSimilarityThread(int pageIndex, int pageSize, TextSimilarity textSimilarity, ArticlekgService articleService) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.textSimilarity = textSimilarity;
        this.articleService = articleService;
    }

    @Override
    public void run() {
        System.out.println("pageIndex:" + pageIndex + "  pageSize:" + pageSize);
        long start = System.currentTimeMillis();
        BaseRequest request = new BaseRequest();
        request.setStart(pageIndex);
        request.setLimit(pageSize);
        List<BigInteger> hashCodeList = articleService.getArticleSimHashCode(request);
        System.out.println(Thread.currentThread().getName() + "本线程查询结束，查询数量" + hashCodeList.size() + "  读取耗费时间：" + (System.currentTimeMillis() - start));

        if (CollectionUtils.isEmpty(hashCodeList)) {
            System.out.println("hashCodeList is empty !!!");
            return;
        }
        long start1 = System.currentTimeMillis();
        for (BigInteger hashCode : hashCodeList) {
            BigInteger hashCode1 = textSimilarity.getSimHashCode();
            int distance = SimHashUtil.hammingDistance(hashCode1, hashCode);
            double per = SimHashUtil.getSemblance(distance);
//            System.out.println("hashCode1:" + hashCode1 + "  hashCode:" + hashCode + "  海明距离为:" + distance);
            if (per > textSimilarity.getPercent()) {
                SimilarityResultModel model = new SimilarityResultModel(hashCode, SimHashUtil.getPercent(per));
                textSimilarity.addSimilarityResult(model);
            }
        }
        System.out.println(Thread.currentThread().getName() + "本线程计算结束 ，耗时 ：" + (System.currentTimeMillis() - start1));

    }
}
