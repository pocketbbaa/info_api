package com.kg.platform.common.similarity;


import com.kg.platform.service.ArticlekgService;
import com.kg.platform.service.admin.ArticleService;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TextSimilarity {

    //待计算的simHashCode
    private volatile BigInteger simHashCode;

    //待计算的总数
    private volatile int countTotal;

    //并发线程数
    private volatile int threadCount = 1;

    //相似比对结果池 相似度 > 50% 入池
    private volatile Set<SimilarityResultModel> similarityResultSet = Collections.synchronizedSet(new HashSet<SimilarityResultModel>());

    // 线程引用镜像
    private List<TextSimilarityThread> textSimilarityThreads = new CopyOnWriteArrayList<>();

    // 线程池
    private ExecutorService threads = Executors.newCachedThreadPool();

    private ArticlekgService articleService;

    //相似百分比（高于此百分比存入结果集）
    private double percent;


    public static class Builde {
        TextSimilarity textSimilarity = new TextSimilarity();

        /**
         * 设置并发线程数
         *
         * @param threadCount
         * @return
         */
        public Builde setThreadCount(int threadCount) {
            textSimilarity.setThreadCount(threadCount);
            return this;
        }

        /**
         * 设置待计算的simHashCode
         *
         * @param simHashCode
         * @return
         */
        public Builde setSimHashCode(BigInteger simHashCode) {
            textSimilarity.setSimHashCode(simHashCode);
            return this;
        }

        /**
         * 设置待计算的总量
         *
         * @param countTotal
         * @return
         */
        public Builde setCountTotal(int countTotal) {
            textSimilarity.setCountTotal(countTotal);
            return this;
        }

        /**
         * 设置Articleservice
         *
         * @param articleService
         * @return
         */
        public Builde setArticleService(ArticlekgService articleService) {
            textSimilarity.setArticleService(articleService);
            return this;
        }

        /**
         * 设置百分比
         *
         * @param percent
         * @return
         */
        public Builde setPercent(double percent) {
            textSimilarity.setPercent(percent);
            return this;
        }

        public TextSimilarity build() {
            return textSimilarity;
        }
    }

    public void start() {
        if (threadCount < 1 || threadCount > 1000) {
            throw new RuntimeException("并发线程数异常!!!");
        }
        int pageSize = getSize(this.countTotal, threadCount);

        for (int i = 0; i < threadCount; i++) {

            //计算该线程需查询的数据
            int pageIndex = i * pageSize;
            TextSimilarityThread similarityThread = new TextSimilarityThread(pageIndex, pageSize, this, articleService);
            textSimilarityThreads.add(similarityThread);
        }
        for (TextSimilarityThread similarityThread : textSimilarityThreads) {
            threads.execute(similarityThread);
        }
        threads.shutdown();
        while (true) {
            if (threads.isTerminated()) {
                System.out.println("所有的子线程都结束了！");
                break;
            }
        }
    }

    /**
     * 添加结果集
     *
     * @param model
     */
    public void addSimilarityResult(SimilarityResultModel model) {
        similarityResultSet.add(model);
    }

    public BigInteger getSimHashCode() {
        return simHashCode;
    }

    public void setSimHashCode(BigInteger simHashCode) {
        this.simHashCode = simHashCode;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public Set<SimilarityResultModel> getSimilarityResultSet() {
        return similarityResultSet;
    }

    public void setSimilarityResultSet(Set<SimilarityResultModel> similarityResultSet) {
        this.similarityResultSet = similarityResultSet;
    }

    public List<TextSimilarityThread> getTextSimilarityThreads() {
        return textSimilarityThreads;
    }

    public void setTextSimilarityThreads(List<TextSimilarityThread> textSimilarityThreads) {
        this.textSimilarityThreads = textSimilarityThreads;
    }

    public ExecutorService getThreads() {
        return threads;
    }

    public void setThreads(ExecutorService threads) {
        this.threads = threads;
    }

    public int getCountTotal() {
        return countTotal;
    }

    public void setCountTotal(int countTotal) {
        this.countTotal = countTotal;
    }

    public ArticlekgService getArticleService() {
        return articleService;
    }

    public void setArticleService(ArticlekgService articleService) {
        this.articleService = articleService;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    private int getSize(int total, int count) {
        int size = total / count;
        if (total % count != 0) {
            size++;
        }
        return size;
    }

    public static void main(String[] args) {

        int count = 16;
        int page = 5;

        int pageSize = count / page;
        if (count % page != 0) {
            pageSize++;
        }

        System.out.println(pageSize);


    }
}
