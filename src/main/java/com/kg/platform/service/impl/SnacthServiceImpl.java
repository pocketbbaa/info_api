package com.kg.platform.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.mq.MQConfig;
import com.kg.platform.common.mq.MQProduct;
import org.apache.commons.collections4.map.HashedMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.utils.HttpUtil;
import com.kg.platform.common.utils.JedisKey;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.dao.entity.Article;
import com.kg.platform.dao.read.ArticleRMapper;
import com.kg.platform.dao.write.ArticleWMapper;
import com.kg.platform.dao.write.KgArticleStatisticsWMapper;
import com.kg.platform.enumeration.NewsWebSiteEnum;
import com.kg.platform.model.in.KgArticleStatisticsInModel;
import com.kg.platform.model.out.ArticleOutModel;
import com.kg.platform.service.SnacthService;

//来源
/**
 * Created by Administrator on 2018/1/10.
 */
@Service("snacthService")
public class SnacthServiceImpl implements SnacthService {
    private static final Logger logger = LoggerFactory.getLogger(BaseInfoServiceImpl.class);

    @Autowired
    private JedisUtils jedisUtils;

    @Autowired
    private ArticleWMapper articleWMapper;

    @Autowired
    private ArticleRMapper articleRMapper;

    @Inject
    private IDGen generater;

    @Autowired
    private KgArticleStatisticsWMapper kgArticleStatisticsWMapper;

    @Autowired
    private MQProduct mqProduct;

    @Autowired
    private MQConfig articleSyncMqConfig;

    @Override
    public void snatchNews() {
        // 1.没有操作数据库，没有下载和上传图片 spend time:11878ms
        // 2.操作数据库加上缓存 没有下载上传图片 spend time:14999ms
        long start = System.currentTimeMillis();
        logger.info("新闻抓取开始........");
        snacth163News("http://money.163.com/special/00252G50/", "macro"); // 宏观
        snacth163News("http://money.163.com/special/00252C1E/", "gjcj"); // 国际经济
        snacthEastNews("http://finance.eastmoney.com/news/", "cgnjj");// 国内经济
        snacthBi126News("http://www.bi126.com/news/btc/", "index");// 数字货币！！！！
                                                                   // 400266317317414912
        // NO股票
        snacthSouhuNews("http://v2.sohu.com/public-api/feed/", "66048");// 期货400659904509124608
        // snacthFinanceNews("http://finance.qq.com/money/forex/index.htm");//外汇**
        snacthSouhuNews("http://v2.sohu.com/public-api/feed/", "67040");// 外汇
        snacthEastNews("http://biz.eastmoney.com/news/", "csyzx");// 商业
        snacthCbNews("http://bmr.cb.com.cn/");// 商学院
        snacthEastNews("http://finance.eastmoney.com/news/", "cfxtz");// 创投
        snacthEastNews("http://biz.eastmoney.com/news/", "cgl");// 管理
        // snacthEastNews("http://biz.eastmoney.com/news/","cyx");//营销 **
        snacthSouhuNews("http://v2.sohu.com/public-api/feed/", "66031");// 营销
        snacthEastNews("http://finance.eastmoney.com/news/", "ccjxw");// 产经
        snacthPeopleNews("http://energy.people.com.cn/", "index");// 能源
        snacthSouhuNews("http://v2.sohu.com/public-api/feed/", "67245");// 房地产
        snacth163News("http://money.163.com/special/002526O3/", "trade09");// 商贸
        snacthFinanceQQNews("http://finance.qq.com/c/", "jrscllist");// 金融
                                                                     // 400665229048029184
        // NO理财
        snacthSouhuNews("http://v2.sohu.com/public-api/feed/", "66047");// 基金
        long end = System.currentTimeMillis();
        logger.info("新闻抓取结束，花费时间：" + (end - start));
    }

    public static void main(String[] args) {
        // 1.没有操作数据库，没有下载和上传图片 spend time:11878ms
        // long start = System.currentTimeMillis();
        // snacthFinanceQQNews("http://finance.qq.com/c/","jrscllist");//金融
        // 400665229048029184
        /*
         * snacth163News("http://money.163.com/special/00252G50/","macro"); //宏观
         * snacth163News("http://money.163.com/special/00252C1E/","gjcj");
         * //国际经济
         * snacthEastNews("http://finance.eastmoney.com/news/","cgnjj");//国内经济
         * snacthBi126News("http://www.bi126.com/news/btc/","index");//数字货币！！！！
         * // NO股票
         * snacthSouhuNews("http://v2.sohu.com/public-api/feed/","66048");//期货
         * //
         * snacthFinanceNews("http://finance.qq.com/money/forex/index.htm");//外汇
         * **
         * snacthSouhuNews("http://v2.sohu.com/public-api/feed/","67040");//外汇
         * snacthEastNews("http://biz.eastmoney.com/news/","csyzx");//商业
         * snacthCbNews("http://bmr.cb.com.cn/");//商学院
         * snacthEastNews("http://finance.eastmoney.com/news/","cfxtz");//创投
         * snacthEastNews("http://biz.eastmoney.com/news/","cgl");//管理 //
         * snacthEastNews("http://biz.eastmoney.com/news/","cyx");//营销 **
         * snacthSouhuNews("http://v2.sohu.com/public-api/feed/","66031");//营销
         * snacthEastNews("http://finance.eastmoney.com/news/","ccjxw");//产经
         * snacthPeopleNews("http://energy.people.com.cn/","index");//能源
         * snacthSouhuNews("http://v2.sohu.com/public-api/feed/","67245");//房地产
         * snacth163News("http://money.163.com/special/002526O3/","trade09");//
         * 商贸 snacthFinanceQQNews("http://finance.qq.com/c/","jrscllist");//金融
         * // NO理财
         * snacthSouhuNews("http://v2.sohu.com/public-api/feed/","66047");//基金
         */
        // long end = System.currentTimeMillis();
        // System.out.println("spend time:"+(end-start));

    }

    // 163新闻抓取
    public void snacth163News(String domain, String detail) {
        // 首页
        String url = domain + detail + ".html";
        try {
            snacth163Util(url, detail);
            // 抓取该类型新闻3页
            for (int n = 2; n <= NewsWebSiteEnum.pagenum.getStatus(); n++) {
                String index = "";
                if (n <= 9) {
                    index = "0" + n;
                } else {
                    index = String.valueOf(n);
                }
                String nexturl = domain + detail + "_" + index + ".html";
                snacth163Util(nexturl, detail);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @SuppressWarnings("unused")
    public void snacth163Util(String url, String detail) {
        // http://money.163.com/special/00252G50/
        try {
            // 从缓存中取对应栏目的新闻
            List<String> titlelist = new ArrayList<>();
            Map<String, Object> map = new HashedMap<String, Object>();
            String key = "";
            int count = 0;
            Integer column = null;// 1级栏目
            Integer second = null;// 2级栏目
            String createuser = "";
            if ("macro".equals(detail)) {
                column = NewsWebSiteEnum.hongguan.getStatus();
                createuser = "400654947265421312";
                key = NewsWebSiteEnum.hongguan.getDisplay();
                map.put("columnId", column);
                map.put("secondColumn", second);
                map.put("articleFrom", 3);
                titlelist = flushCache(key, map);
            }
            if ("gjcj".equals(detail)) {
                column = 340;
                second = NewsWebSiteEnum.guojijingji.getStatus();
                createuser = "400655772389875712";
                key = NewsWebSiteEnum.guojijingji.getDisplay();
                map.put("columnId", column);
                map.put("secondColumn", second);
                map.put("articleFrom", 3);
                titlelist = flushCache(key, map);
            }
            if ("trade09".equals(detail)) {
                column = 344;
                second = NewsWebSiteEnum.shangmao.getStatus();
                createuser = "400664701488472064";
                key = NewsWebSiteEnum.shangmao.getDisplay();
                map.put("columnId", column);
                map.put("secondColumn", second);
                map.put("articleFrom", 3);
                titlelist = flushCache(key, map);
            }
            Document doc = Jsoup.connect(url).timeout(5000).get();
            if (doc != null) {
                Elements body = doc.getElementsByClass("col_l");
                Elements elements = body.get(0).getElementsByClass("clearfix");
                // List<Article> articleList = new ArrayList<>();
                if (elements.size() > 0) {
                    for (int i = 0; i < elements.size(); i++) {
                        Element element = elements.get(i).getElementsByClass("item_top").get(0);
                        // 标题
                        String title = element.getElementsByTag("h2").get(0).getElementsByTag("a").get(0).html();
                        Elements imgelems = element.getElementsByClass("newsimg");
                        String imgpath = "";
                        if (imgelems.size() > 0) {
                            // 图片地址
                            imgpath = imgelems.get(0).getElementsByTag("img").get(0).attr("src");
                            // 下载图片并上传至KG
                            // InputStream inputStream =
                            // HttpUtil.getInputStream(imgpath);
                        }
                        // 描述
                        Element p = element.getElementsByTag("p").get(0);
                        Element span = p.getElementsByTag("span").get(0);
                        span.remove();
                        String descrip = p.text();
                        // 新闻链接
                        String link = element.getElementsByTag("h2").get(0).getElementsByTag("a").get(0).attr("href");

                        logger.debug("抓取:" + key);
                        // 对比title判断是否是新发布的新闻
                        if (!titlelist.contains(title)) {
                            // 根据链接取新闻正文
                            Document context = Jsoup.connect(link).timeout(5000).get();
                            String articletext = context.getElementById("endText").html();
                            // 新的才插入数据库
                            Article article = new Article();
                            article.setArticleId(String.valueOf(generater.nextId()));
                            article.setArticleTitle(title);
                            article.setArticleText(articletext);
                            article.setArticleDescription(descrip);
                            article.setArticleImage(imgpath);
                            article.setArticleSource("网易财经");
                            article.setArticleLink(link);
                            article.setColumnId(column);
                            article.setSecondColumn(second);
                            // article.setCreateDate(DateUtils.parseDate(span.text(),DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
                            article.setPublishStatus(1);
                            article.setCreateUser(createuser);
                            article.setArticleFrom(3);
                            article.setArticleType(2);
                            int state = articleWMapper.insertSelective(article);
                            KgArticleStatisticsInModel statisticsInModel = new KgArticleStatisticsInModel();
                            statisticsInModel.setArticleId(Long.valueOf(article.getArticleId()));
                            statisticsInModel.setCreateUser(Long.valueOf(createuser));
                            int status = kgArticleStatisticsWMapper.insertSelective(statisticsInModel);
                            if (state > 0 && status > 0) {
                                if (titlelist.size() < 60) {
                                    List<String> newtitles = new ArrayList<>();
                                    newtitles.add(title);
                                    newtitles.addAll(titlelist);
                                    titlelist = newtitles;
                                    jedisUtils.set(JedisKey.getNewsKey(key), newtitles,
                                            NewsWebSiteEnum.cachetime.getStatus());
                                } else {
                                    // 装满60条，移除最后一条，加上最新的一条至开头
                                    List<String> newtitles = new ArrayList<>();
                                    newtitles.add(title);
                                    newtitles.addAll(titlelist.subList(0, titlelist.size() - 1));
                                    titlelist = newtitles;
                                    jedisUtils.set(JedisKey.getNewsKey(key), newtitles,
                                            NewsWebSiteEnum.cachetime.getStatus());
                                }
                                sendArticleSyncMessage(article);
                            }
                        }

                    }
                }
            } else {
                snacth163Util(url, detail);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    // 东方财富网
    public void snacthEastNews(String domain, String detail) {
        // 首页
        String url = domain + detail + ".html";
        try {
            snacthEastUtil(url, detail);
            // 如果为营销和管理则取2页
            if ("cgl".equals(detail) || "cyx".equals(detail)) {
                for (int n = 2; n < NewsWebSiteEnum.pagenum.getStatus(); n++) {
                    String nexturl = domain + detail + "_" + n + ".html";
                    snacthEastUtil(nexturl, detail);
                }
            } else {
                // 抓取该类型新闻3页
                for (int n = 2; n <= NewsWebSiteEnum.pagenum.getStatus(); n++) {
                    String nexturl = domain + detail + "_" + n + ".html";
                    snacthEastUtil(nexturl, detail);
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @SuppressWarnings("unused")
    public void snacthEastUtil(String url, String detail) {
        try {
            // 从缓存中取对应栏目的新闻
            List<String> titlelist = new ArrayList<>();
            Map<String, Object> map = new HashedMap<String, Object>();
            String key = "";
            Integer column = null;// 1级栏目
            Integer second = null;// 2级栏目
            String createuser = "";

            if ("cgnjj".equals(detail)) {
                column = 340;
                second = NewsWebSiteEnum.guoneijingji.getStatus();
                createuser = "400656211550281728";
                key = NewsWebSiteEnum.guoneijingji.getDisplay();
                map.put("columnId", column);
                map.put("secondColumn", second);
                map.put("articleFrom", 4);
                titlelist = flushCache(key, map);
            }
            if ("csyzx".equals(detail)) {
                column = NewsWebSiteEnum.shangye.getStatus();
                createuser = "400660791889633280";
                key = NewsWebSiteEnum.shangye.getDisplay();
                map.put("columnId", column);
                map.put("secondColumn", second);
                map.put("articleFrom", 4);
                titlelist = flushCache(key, map);
            }
            if ("cfxtz".equals(detail)) {
                column = 349;
                second = NewsWebSiteEnum.chuangtou.getStatus();
                createuser = "400662113393188864";
                key = NewsWebSiteEnum.chuangtou.getDisplay();
                map.put("columnId", column);
                map.put("secondColumn", second);
                map.put("articleFrom", 4);
                titlelist = flushCache(key, map);
            }
            if ("cgl".equals(detail)) {
                column = 349;
                second = NewsWebSiteEnum.guanli.getStatus();
                createuser = "400660791889633280";
                key = NewsWebSiteEnum.guanli.getDisplay();
                map.put("columnId", column);
                map.put("secondColumn", second);
                map.put("articleFrom", 4);
                titlelist = flushCache(key, map);
            }
            if ("ccjxw".equals(detail)) {
                column = NewsWebSiteEnum.chanjing.getStatus();
                createuser = "400663396728578048";
                key = NewsWebSiteEnum.chanjing.getDisplay();
                map.put("columnId", column);
                map.put("secondColumn", second);
                map.put("articleFrom", 4);
                titlelist = flushCache(key, map);
            }

            Document doc = Jsoup.connect(url).timeout(5000).get();
            if (doc != null) {
                Element newsContain = doc.getElementById("newsListContent");
                Elements newsList = newsContain.getElementsByTag("li");
                for (int i = 0; i < newsList.size(); i++) {
                    String imgpath = "";
                    String title = "";
                    String desc = "";
                    String link = "";
                    String time = "";
                    Element news = newsList.get(i);
                    Elements img = news.getElementsByClass("image");
                    if (img.size() > 0) {
                        // 有图片
                        imgpath = img.get(0).getElementsByTag("a").get(0).getElementsByTag("img").get(0).attr("src");
                    }
                    // 标题
                    title = news.getElementsByClass("text").get(0).getElementsByClass("title").get(0)
                            .getElementsByTag("a").get(0).text();
                    // 新闻链接
                    link = news.getElementsByClass("text").get(0).getElementsByClass("title").get(0)
                            .getElementsByTag("a").get(0).attr("href");
                    // 描述
                    desc = news.getElementsByClass("text").get(0).getElementsByClass("info").get(0).text();
                    // 时间
                    // time =
                    // news.getElementsByClass("text").get(0).getElementsByClass("time").get(0).text();

                    logger.debug("抓取:" + key);
                    // 对比title判断是否是新发布的新闻
                    if (!titlelist.contains(title)) {
                        // 根据链接取新闻正文
                        Document context = Jsoup.connect(link).timeout(5000).get();
                        String articletext = context.getElementById("ContentBody").getElementsByTag("p").get(0).html();
                        Article article = new Article();
                        article.setArticleId(String.valueOf(generater.nextId()));
                        article.setArticleTitle(title);
                        article.setArticleText(articletext);
                        article.setArticleDescription(desc);
                        article.setArticleImage(imgpath);
                        article.setArticleSource("东方财富网");
                        article.setArticleLink(link);
                        article.setColumnId(column);
                        article.setSecondColumn(second);
                        // article.setCreateDate(DateUtils.parseDate(time,DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
                        article.setPublishStatus(1);
                        article.setCreateUser(createuser);
                        article.setArticleFrom(4);
                        article.setArticleType(2);
                        int state = articleWMapper.insertSelective(article);
                        KgArticleStatisticsInModel statisticsInModel = new KgArticleStatisticsInModel();
                        statisticsInModel.setArticleId(Long.valueOf(article.getArticleId()));
                        statisticsInModel.setCreateUser(Long.valueOf(createuser));
                        int status = kgArticleStatisticsWMapper.insertSelective(statisticsInModel);
                        if (state > 0 && status > 0) {
                            if (titlelist.size() < 60) {
                                /*
                                 * titlelist.add(title);
                                 * jedisUtils.set(JedisKey.getNewsKey(key),
                                 * titlelist,NewsWebSiteEnum.cachetime.getStatus
                                 * ());
                                 */
                                List<String> newtitles = new ArrayList<>();
                                newtitles.add(title);
                                newtitles.addAll(titlelist);
                                titlelist = newtitles;
                                jedisUtils.set(JedisKey.getNewsKey(key), newtitles,
                                        NewsWebSiteEnum.cachetime.getStatus());
                            } else {
                                // 装满60条，移除最后一条，加上最新的一条至开头
                                List<String> newtitles = new ArrayList<>();
                                newtitles.add(title);
                                newtitles.addAll(titlelist.subList(0, titlelist.size() - 1));
                                titlelist = newtitles;
                                jedisUtils.set(JedisKey.getNewsKey(key), newtitles,
                                        NewsWebSiteEnum.cachetime.getStatus());
                            }
                            sendArticleSyncMessage(article);
                        }
                    }
                }
            } else {
                snacthEastUtil(url, detail);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    // Bi126新闻抓取
    public void snacthBi126News(String domain, String detail) {
        // http://www.bi126.com/news/btc/index.html
        // 首页
        String url = domain + detail + ".html";
        try {
            snacthBi126Util(url);
            // 抓取该类型新闻3页
            for (int n = 2; n <= NewsWebSiteEnum.pagenum.getStatus(); n++) {
                String nexturl = domain + detail + "_" + n + ".html";
                snacthBi126Util(nexturl);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @SuppressWarnings("unused")
    public void snacthBi126Util(String url) {
        try {
            // 从缓存中取对应栏目的新闻
            List<String> titlelist = new ArrayList<>();
            Map<String, Object> map = new HashedMap<String, Object>();
            map.put("columnId", NewsWebSiteEnum.shuzihuobi.getStatus());
            map.put("secondColumn", null);
            map.put("articleFrom", 5);
            titlelist = flushCache(NewsWebSiteEnum.shuzihuobi.getDisplay(), map);
            Document doc = Jsoup.connect(url).timeout(5000).get();
            if (doc != null) {
                Elements news_list = doc.getElementsByClass("news_list").get(0).getElementsByClass("fixed");
                for (int i = 0; i < news_list.size(); i++) {
                    Element news = news_list.get(i);
                    String imgpath = "";
                    String title = "";
                    String desc = "";
                    String link = "";
                    String time = "";

                    // 图片链接
                    imgpath = news.getElementsByClass("news_cover").get(0).getElementsByTag("a").get(0)
                            .getElementsByTag("img").get(0).attr("src");
                    if (imgpath.indexOf("notimg") != -1) {
                        // 没图片
                        imgpath = "";
                    }
                    // 新闻链接
                    link = news.getElementsByClass("news_cover").get(0).getElementsByTag("a").attr("href");
                    // 标题
                    title = news.getElementsByClass("news_text").get(0).getElementsByTag("h3").get(0)
                            .getElementsByTag("a").get(0).text();
                    // 描述
                    desc = news.getElementsByClass("news_text").get(0).getElementsByTag("p").get(0).text();
                    // 时间
                    // time =
                    // news.getElementsByClass("news_text").get(0).getElementsByClass("fr").get(0).text();

                    logger.debug("抓取:" + NewsWebSiteEnum.shuzihuobi.getDisplay());
                    if (!titlelist.contains(title)) {
                        // 根据链接取新闻正文
                        Document context = Jsoup.connect(link).timeout(5000).get();
                        String articletext = context.getElementById("text").html();
                        Article article = new Article();
                        article.setArticleId(String.valueOf(generater.nextId()));
                        article.setArticleTitle(title);
                        article.setArticleText(articletext);
                        article.setArticleDescription(desc);
                        article.setArticleImage(imgpath);
                        article.setArticleSource("币世界");
                        article.setArticleLink(link);
                        article.setColumnId(NewsWebSiteEnum.shuzihuobi.getStatus());
                        // article.setCreateDate(DateUtils.parseDate(time,DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
                        article.setPublishStatus(1);
                        article.setCreateUser("400266317317414912");
                        article.setArticleFrom(5);
                        article.setArticleType(2);
                        int state = articleWMapper.insertSelective(article);
                        KgArticleStatisticsInModel statisticsInModel = new KgArticleStatisticsInModel();
                        statisticsInModel.setArticleId(Long.valueOf(article.getArticleId()));
                        statisticsInModel.setCreateUser(Long.valueOf(article.getCreateUser()));
                        int status = kgArticleStatisticsWMapper.insertSelective(statisticsInModel);
                        if (state > 0 && status > 0) {
                            if (titlelist.size() < 60) {
                                /*
                                 * titlelist.add(title);
                                 * jedisUtils.set(JedisKey.getNewsKey(
                                 * NewsWebSiteEnum.shuzihuobi.getDisplay()),
                                 * titlelist,NewsWebSiteEnum.cachetime.getStatus
                                 * ());
                                 */
                                List<String> newtitles = new ArrayList<>();
                                newtitles.add(title);
                                newtitles.addAll(titlelist);
                                titlelist = newtitles;
                                jedisUtils.set(JedisKey.getNewsKey(NewsWebSiteEnum.shuzihuobi.getDisplay()), newtitles,
                                        NewsWebSiteEnum.cachetime.getStatus());

                            } else {
                                // 装满60条，移除最后一条，加上最新的一条至开头
                                List<String> newtitles = new ArrayList<>();
                                newtitles.add(title);
                                newtitles.addAll(titlelist.subList(0, titlelist.size() - 1));
                                titlelist = newtitles;
                                jedisUtils.set(JedisKey.getNewsKey(NewsWebSiteEnum.shuzihuobi.getDisplay()), newtitles,
                                        NewsWebSiteEnum.cachetime.getStatus());
                            }
                            sendArticleSyncMessage(article);
                        }
                    }

                }
            } else {
                snacthBi126Util(url);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    // 腾讯外汇新闻抓取
    @SuppressWarnings("unused")
    public void snacthFinanceNews(String url) {
        try {
            Document doc = Jsoup.connect(url).timeout(5000).get();
            if (doc != null) {
                String title1 = "";
                String desc1 = "";
                String link1 = "";
                // 第一条大新闻
                link1 = doc.getElementsByClass("left-div1").get(0).getElementsByTag("h1").get(0).getElementsByTag("a")
                        .get(0).attr("href");
                title1 = doc.getElementsByClass("left-div1").get(0).getElementsByTag("h1").get(0).getElementsByTag("a")
                        .get(0).text();
                desc1 = doc.getElementsByClass("left-div1").get(0).getElementsByTag("p").get(0).text();
                // 要闻
                Elements yaowennews = doc.getElementsByClass("left-div2").get(0).getElementsByClass("left-ul1").get(0)
                        .getElementsByTag("li");
                for (int i = 0; i < yaowennews.size(); i++) {
                    String imgpath = "";
                    String title = "";
                    String desc = "";
                    String link = "";
                    String time = "";

                    Element yaowen = yaowennews.get(i);
                    link = "http://finance.qq.com"
                            + yaowen.getElementsByTag("h2").get(0).getElementsByTag("a").attr("href");
                    title = yaowen.getElementsByTag("h2").get(0).getElementsByTag("a").text();
                    desc = yaowen.getElementsByTag("b").get(0).text();
                }
                // 专栏
                Elements zhuanlannews = doc.getElementsByClass("left-div3").get(0).getElementsByClass("left-ul2").get(0)
                        .getElementsByTag("li");
                for (int i = 0; i < zhuanlannews.size(); i++) {
                    String imgpath = "";
                    String title = "";
                    String desc = "";
                    String link = "";
                    String time = "";

                    Element zhuanlan = zhuanlannews.get(i);
                    link = "http://finance.qq.com"
                            + zhuanlan.getElementsByTag("h2").get(0).getElementsByTag("a").attr("href");
                    title = zhuanlan.getElementsByTag("h2").get(0).getElementsByTag("a").text();
                    desc = zhuanlan.getElementsByTag("b").get(0).text();

                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    // 商学院新闻抓取
    @SuppressWarnings("unused")
    public void snacthCbNews(String url) {
        try {
            // 从缓存中取对应栏目的新闻
            List<String> titlelist = new ArrayList<>();
            Map<String, Object> map = new HashedMap<String, Object>();
            map.put("columnId", 349);
            map.put("secondColumn", NewsWebSiteEnum.shangxueyuan.getStatus());
            map.put("articleFrom", 6);
            titlelist = flushCache(NewsWebSiteEnum.shangxueyuan.getDisplay(), map);
            Document doc = Jsoup.connect(url).timeout(5000).get();
            if (doc != null) {
                // 实战商学院（szsxy）
                Element contents = doc.getElementsByClass("content").get(0).getElementsByClass("fl").get(0);
                Elements newslist = contents.getElementsByClass("frame_db");
                for (int i = 0; i < newslist.size(); i++) {
                    Element news = newslist.get(i);
                    String imgpath = "";
                    String title = "";
                    String desc = "";
                    String link = "";
                    String time = "";
                    String type = "";
                    if (i == 0)
                        type = "szsxy";// 实战商学院
                    if (i == 1)
                        type = "jdsxy";// 经典商学院
                    if (i == 2)
                        type = "kjsxy";// 跨界商学院
                    if (i == 3)
                        type = "wlsxy";// 未来商学院
                    if (i == 4)
                        type = "xfsxy";// 幸福商学院

                    Elements typenews = news.getElementsByClass("c_listtext");
                    for (int m = 0; m < typenews.size(); m++) {
                        Element element = typenews.get(m);
                        link = element.getElementsByTag("h1").get(0).getElementsByTag("a").get(0).attr("href");
                        title = element.getElementsByTag("h1").get(0).getElementsByTag("a").get(0).text();
                        Elements img = element.getElementsByClass("c_listtext1").get(0).getElementsByTag("img");
                        if (img.size() > 0) {
                            imgpath = img.get(0).attr("src");
                        }
                        desc = element.getElementsByClass("c_listtext1").get(0).getElementsByTag("p").get(0).text();
                        desc = desc.substring(0, desc.indexOf(".")) + "..........";
                        logger.debug("抓取：" + NewsWebSiteEnum.shangxueyuan.getDisplay());
                        if (!titlelist.contains(title)) {
                            // 根据链接取新闻正文
                            Document context = Jsoup.connect(link).timeout(5000).get();
                            String articletext = context.getElementsByClass("c_listtext").get(0)
                                    .getElementsByClass("text").get(0).html();
                            Article article = new Article();
                            article.setArticleId(String.valueOf(generater.nextId()));
                            article.setArticleTitle(title);
                            article.setArticleText(articletext);
                            article.setArticleDescription(desc);
                            article.setArticleImage(imgpath);
                            article.setArticleSource("中国经营网");
                            article.setArticleLink(link);
                            article.setColumnId(349);
                            article.setSecondColumn(NewsWebSiteEnum.shangxueyuan.getStatus());
                            // article.setCreateDate(DateUtils.parseDate(time,DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
                            article.setPublishStatus(1);
                            article.setCreateUser("400661589289738240");
                            article.setArticleFrom(6);
                            article.setArticleType(2);
                            int state = articleWMapper.insertSelective(article);
                            KgArticleStatisticsInModel statisticsInModel = new KgArticleStatisticsInModel();
                            statisticsInModel.setArticleId(Long.valueOf(article.getArticleId()));
                            statisticsInModel.setCreateUser(Long.valueOf(article.getCreateUser()));
                            int status = kgArticleStatisticsWMapper.insertSelective(statisticsInModel);
                            if (state > 0 && status > 0) {
                                if (titlelist.size() < 60) {
                                    /*
                                     * titlelist.add(title);
                                     * jedisUtils.set(JedisKey.getNewsKey(
                                     * NewsWebSiteEnum.shangxueyuan.getDisplay()
                                     * ),titlelist,NewsWebSiteEnum.cachetime.
                                     * getStatus());
                                     */
                                    List<String> newtitles = new ArrayList<>();
                                    newtitles.add(title);
                                    newtitles.addAll(titlelist);
                                    titlelist = newtitles;
                                    jedisUtils.set(JedisKey.getNewsKey(NewsWebSiteEnum.shangxueyuan.getDisplay()),
                                            newtitles, NewsWebSiteEnum.cachetime.getStatus());
                                } else {
                                    // 装满60条，移除最后一条，加上最新的一条至开头
                                    List<String> newtitles = new ArrayList<>();
                                    newtitles.add(title);
                                    newtitles.addAll(titlelist.subList(0, titlelist.size() - 1));
                                    titlelist = newtitles;
                                    jedisUtils.set(JedisKey.getNewsKey(NewsWebSiteEnum.shangxueyuan.getDisplay()),
                                            newtitles, NewsWebSiteEnum.cachetime.getStatus());
                                }
                                sendArticleSyncMessage(article);

                            }
                        }

                    }

                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 发送数据同步消息
     * @param article
     */
    private void sendArticleSyncMessage(Article article) {
        Map<String, Object> map = new HashMap<>();
        map.put("types", "0");
        map.put("articleId", article.getArticleId());
        logger.info("ES同步文章MQ------topic:{},  map:{}", articleSyncMqConfig.getTopic(), JSONObject.toJSON(map));
        mqProduct.sendMessage(articleSyncMqConfig.getTopic(), articleSyncMqConfig.getTags(), null, JSON.toJSONString(map));
    }

    public void snacthPeopleNews(String domain, String detail) {
        // 首页
        String url = "http://energy.people.com.cn/";
        try {
            snacthPeopleUtil(url);
            // 抓取该类型新闻3页
            for (int n = 2; n <= NewsWebSiteEnum.pagenum.getStatus(); n++) {
                String nexturl = domain + detail + n + ".html";
                snacthPeopleUtil(nexturl);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @SuppressWarnings("unused")
    public void snacthPeopleUtil(String url) {
        try {
            // 从缓存中取对应栏目的新闻
            List<String> titlelist = new ArrayList<>();
            Map<String, Object> map = new HashedMap<String, Object>();
            map.put("columnId", 344);
            map.put("secondColumn", NewsWebSiteEnum.nengyuan.getStatus());
            map.put("articleFrom", 7);
            titlelist = flushCache(NewsWebSiteEnum.nengyuan.getDisplay(), map);
            Document doc = Jsoup.connect(url).timeout(5000).get();
            if (doc != null) {
                Element content = doc.getElementsByClass("p3_content").get(0).getElementsByClass("fl").get(0);
                Elements newsList = content.getElementsByClass("headingNews").get(0).getElementsByClass("clearfix");
                for (int i = 0; i < newsList.size() - 1; i++) {
                    String imgpath = "";
                    String title = "";
                    String desc = "";
                    String link = "";
                    String time = "";

                    Element news = newsList.get(i);
                    Element p = news.getElementsByTag("p").get(0);
                    link = "http://energy.people.com.cn"
                            + p.getElementsByTag("strong").get(0).getElementsByTag("a").get(0).attr("href");
                    title = p.getElementsByTag("strong").get(0).getElementsByTag("a").get(0).text();
                    desc = p.getElementsByClass("gray2").get(0).getElementsByTag("a").get(0).text();
                    Elements img = p.getElementsByTag("img");
                    if (img.size() > 0) {
                        imgpath = img.get(0).attr("src");
                    }
                    logger.debug("抓取：" + NewsWebSiteEnum.shangxueyuan.getDisplay());
                    if (!titlelist.contains(title)) {
                        // 根据链接取新闻正文
                        Document context = Jsoup.connect(link).timeout(5000).get();
                        String articletext = context.getElementById("rwb_zw").html();
                        Article article = new Article();
                        article.setArticleId(String.valueOf(generater.nextId()));
                        article.setArticleTitle(title);
                        article.setArticleText(articletext);
                        article.setArticleDescription(desc);
                        article.setArticleImage(imgpath);
                        article.setArticleSource("人民网");
                        article.setArticleLink(link);
                        article.setColumnId(344);
                        article.setSecondColumn(NewsWebSiteEnum.nengyuan.getStatus());
                        // article.setCreateDate(DateUtils.parseDate(time,DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
                        article.setPublishStatus(1);
                        article.setCreateUser("400663819501838336");
                        article.setArticleFrom(7);
                        article.setArticleType(2);
                        int state = articleWMapper.insertSelective(article);
                        KgArticleStatisticsInModel statisticsInModel = new KgArticleStatisticsInModel();
                        statisticsInModel.setArticleId(Long.valueOf(article.getArticleId()));
                        statisticsInModel.setCreateUser(Long.valueOf(article.getCreateUser()));
                        int status = kgArticleStatisticsWMapper.insertSelective(statisticsInModel);
                        if (state > 0 && status > 0) {
                            if (titlelist.size() < 60) {
                                /*
                                 * titlelist.add(title);
                                 * jedisUtils.set(JedisKey.getNewsKey(
                                 * NewsWebSiteEnum.nengyuan.getDisplay()),
                                 * titlelist,NewsWebSiteEnum.cachetime.getStatus
                                 * ());
                                 */
                                List<String> newtitles = new ArrayList<>();
                                newtitles.add(title);
                                newtitles.addAll(titlelist);
                                titlelist = newtitles;
                                jedisUtils.set(JedisKey.getNewsKey(NewsWebSiteEnum.nengyuan.getDisplay()), newtitles,
                                        NewsWebSiteEnum.cachetime.getStatus());
                            } else {
                                // 装满60条，移除最后一条，加上最新的一条至开头
                                List<String> newtitles = new ArrayList<>();
                                newtitles.add(title);
                                newtitles.addAll(titlelist.subList(0, titlelist.size() - 1));
                                titlelist = newtitles;
                                jedisUtils.set(JedisKey.getNewsKey(NewsWebSiteEnum.nengyuan.getDisplay()), newtitles,
                                        NewsWebSiteEnum.cachetime.getStatus());
                            }
                            sendArticleSyncMessage(article);
                        }
                    }

                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    public void snacthFinanceQQNews(String domain, String detail) {
        try {
            // 抓取该类型新闻3页
            for (int n = 1; n <= NewsWebSiteEnum.pagenum.getStatus(); n++) {
                String nexturl = domain + detail + "_" + n + ".htm";
                snacthFinanceQQUtil(nexturl);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @SuppressWarnings("unused")
    public void snacthFinanceQQUtil(String url) {
        try {

            // 从缓存中取对应栏目的新闻
            List<String> titlelist = new ArrayList<>();
            Map<String, Object> map = new HashedMap<String, Object>();
            map.put("columnId", NewsWebSiteEnum.jinrong.getStatus());
            map.put("secondColumn", null);
            map.put("articleFrom", 8);
            // titlelist = flushCache(NewsWebSiteEnum.jinrong.getDisplay(),map);
            Document doc = Jsoup.connect(url).timeout(5000).get();
            Elements newsList = doc.getElementsByClass("Q-tpList").get(0).getElementById("listZone")
                    .getElementsByClass("Q-tpWrap");
            for (int i = 0; i < newsList.size(); i++) {
                String imgpath = "";
                String title = "";
                String desc = "";
                String link = "";
                String time = "";

                Element news = newsList.get(i);
                Elements img = news.getElementsByClass("pic");
                if (img.size() > 0) {
                    imgpath = img.get(0).getElementsByTag("img").get(0).attr("src");
                }
                link = "http://finance.qq.com"
                        + news.getElementsByTag("em").get(0).getElementsByTag("a").get(0).attr("href");
                title = news.getElementsByTag("em").get(0).getElementsByTag("a").get(0).text();
                desc = news.getElementsByTag("p").get(0).text();
                // time = news.getElementsByClass("l22").get(0).text(); //01月09日
                // 16：01(格式)
                logger.debug("抓取：" + NewsWebSiteEnum.jinrong.getDisplay());
                if (!titlelist.contains(title)) {
                    // 根据链接取新闻正文
                    Document context = Jsoup.connect(link).timeout(5000).get();
                    String articletext = context.getElementsByClass("qq_article").get(0).getElementsByClass("bd").get(0)
                            .html();
                    Article article = new Article();
                    article.setArticleId(String.valueOf(generater.nextId()));
                    article.setArticleTitle(title);
                    article.setArticleText(articletext);
                    article.setArticleDescription(desc);
                    article.setArticleImage(imgpath);
                    article.setArticleSource("腾讯网");
                    article.setArticleLink(link);
                    article.setColumnId(NewsWebSiteEnum.jinrong.getStatus());
                    // article.setCreateDate(DateUtils.parseDate(time,DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
                    article.setPublishStatus(1);
                    article.setCreateUser("400665229048029184");
                    article.setArticleFrom(8);
                    article.setArticleType(2);
                    int state = articleWMapper.insertSelective(article);
                    KgArticleStatisticsInModel statisticsInModel = new KgArticleStatisticsInModel();
                    statisticsInModel.setArticleId(Long.valueOf(article.getArticleId()));
                    statisticsInModel.setCreateUser(Long.valueOf(article.getCreateUser()));
                    int status = kgArticleStatisticsWMapper.insertSelective(statisticsInModel);
                    if (state > 0 && status > 0) {
                        if (titlelist.size() < 60) {
                            /*
                             * titlelist.add(title);
                             * jedisUtils.set(JedisKey.getNewsKey(
                             * NewsWebSiteEnum.jinrong.getDisplay()),titlelist,
                             * NewsWebSiteEnum.cachetime.getStatus());
                             */
                            List<String> newtitles = new ArrayList<>();
                            newtitles.add(title);
                            newtitles.addAll(titlelist);
                            titlelist = newtitles;
                            jedisUtils.set(JedisKey.getNewsKey(NewsWebSiteEnum.jinrong.getDisplay()), newtitles,
                                    NewsWebSiteEnum.cachetime.getStatus());
                        } else {
                            // 装满60条，移除最后一条，加上最新的一条至开头
                            List<String> newtitles = new ArrayList<>();
                            newtitles.add(title);
                            newtitles.addAll(titlelist.subList(0, titlelist.size() - 1));
                            titlelist = newtitles;
                            jedisUtils.set(JedisKey.getNewsKey(NewsWebSiteEnum.jinrong.getDisplay()), newtitles,
                                    NewsWebSiteEnum.cachetime.getStatus());
                        }
                        sendArticleSyncMessage(article);
                    }
                }

            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void snacthSouhuNews(String domain, String detail) {
        for (int i = 1; i <= NewsWebSiteEnum.pagenum.getStatus(); i++) {
            Map<String, Object> param = new HashedMap<String, Object>();
            param.put("scene", "TAG");
            param.put("sceneId", detail);
            param.put("page", i);
            param.put("size", 20);
            snacthSouhuUtil(domain, param);
        }
    }

    @SuppressWarnings("unused")
    public void snacthSouhuUtil(String url, Map<String, Object> param) {
        try {
            // 从缓存中取对应栏目的新闻
            List<String> titlelist = new ArrayList<>();
            Map<String, Object> map = new HashedMap<String, Object>();
            String key = "";
            Integer column = null;// 1级栏目
            Integer second = null;// 2级栏目
            String createuser = "";

            if (param.get("sceneId").equals("66048")) {
                column = NewsWebSiteEnum.qihuo.getStatus();
                createuser = "400659904509124608";
                key = NewsWebSiteEnum.qihuo.getDisplay();
                map.put("columnId", column);
                map.put("secondColumn", null);
                map.put("articleFrom", 9);
                titlelist = flushCache(key, map);
            }
            if (param.get("sceneId").equals("67040")) {
                column = NewsWebSiteEnum.waihui.getStatus();
                createuser = "400660437110235136";
                key = NewsWebSiteEnum.waihui.getDisplay();
                map.put("columnId", column);
                map.put("secondColumn", null);
                map.put("articleFrom", 9);
                titlelist = flushCache(key, map);
            }
            if (param.get("sceneId").equals("66031")) {
                column = 349;
                second = NewsWebSiteEnum.yingxiao.getStatus();
                createuser = "400662988375334912";
                key = NewsWebSiteEnum.yingxiao.getDisplay();
                map.put("columnId", column);
                map.put("secondColumn", second);
                map.put("articleFrom", 9);
                titlelist = flushCache(key, map);
            }
            if (param.get("sceneId").equals("67245")) {
                column = 344;
                second = NewsWebSiteEnum.fangdichan.getStatus();
                createuser = "400664280967553024";
                key = NewsWebSiteEnum.fangdichan.getDisplay();
                map.put("columnId", column);
                map.put("secondColumn", second);
                map.put("articleFrom", 9);
                titlelist = flushCache(key, map);
            }
            if (param.get("sceneId").equals("66047")) {
                column = NewsWebSiteEnum.jijin.getStatus();
                createuser = "400666210473222144";
                key = NewsWebSiteEnum.jijin.getDisplay();
                map.put("columnId", column);
                map.put("secondColumn", second);
                map.put("articleFrom", 9);
                titlelist = flushCache(key, map);
            }

            String s = HttpUtil.get(url, param);
            if (s != null || !"".equals(s)) {
                JSONArray result = JSONArray.parseArray(s);
                for (int i = 0; i < result.size(); i++) {

                    String imgpath = "";
                    String title = "";
                    String desc = "";
                    String link = "";
                    String time = "";

                    JSONObject news = result.getJSONObject(i);
                    JSONArray imgs = news.getJSONArray("images");
                    if (imgs.size() > 0) {
                        imgpath = "http:" + imgs.getString(0);
                    }
                    title = news.getString("title");
                    desc = news.getString("mobileTitle");

                    String id = news.getString("id");
                    String authorId = news.getString("authorId");
                    link = "http://www.sohu.com/a/" + id + "_" + authorId + ".html";
                    logger.debug("抓取：" + key);
                    /*
                     * long utime = news.getLongValue("publicTime");//UNIX时间戳
                     * SimpleDateFormat format = new
                     * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String d =
                     * format.format(utime); Date date=format.parse(d); time =
                     * DateUtils.formatDate(date,"yyyy-MM-dd HH:mm:ss");
                     */
                    if (!titlelist.contains(title)) {
                        // 根据链接取新闻正文
                        Document context = Jsoup.connect(link).timeout(5000).get();
                        String articletext = context.getElementsByClass("text").get(0).getElementById("mp-editor")
                                .html();
                        Article article = new Article();
                        article.setArticleId(String.valueOf(generater.nextId()));
                        article.setArticleTitle(title);
                        article.setArticleText(articletext);
                        article.setArticleDescription(desc);
                        article.setArticleImage(imgpath);
                        article.setArticleSource("搜狐新闻");
                        article.setArticleLink(link);
                        article.setColumnId(column);
                        article.setSecondColumn(second);
                        // article.setCreateDate(DateUtils.parseDate(time,DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
                        article.setPublishStatus(1);
                        article.setCreateUser(createuser);
                        article.setArticleFrom(9);
                        article.setArticleType(2);
                        int state = articleWMapper.insertSelective(article);
                        KgArticleStatisticsInModel statisticsInModel = new KgArticleStatisticsInModel();
                        statisticsInModel.setArticleId(Long.valueOf(article.getArticleId()));
                        statisticsInModel.setCreateUser(Long.valueOf(article.getCreateUser()));
                        int status = kgArticleStatisticsWMapper.insertSelective(statisticsInModel);
                        if (state > 0 && status > 0) {
                            if (titlelist.size() < 60) {
                                List<String> newtitles = new ArrayList<>();
                                newtitles.add(title);
                                newtitles.addAll(titlelist);
                                titlelist = newtitles;
                                jedisUtils.set(JedisKey.getNewsKey(key), newtitles,
                                        NewsWebSiteEnum.cachetime.getStatus());
                            } else {
                                // 装满60条，移除最后一条，加上最新的一条至开头
                                List<String> newtitles = new ArrayList<>();
                                newtitles.add(title);
                                newtitles.addAll(titlelist.subList(0, titlelist.size() - 1));
                                titlelist = newtitles;
                                jedisUtils.set(JedisKey.getNewsKey(key), newtitles,
                                        NewsWebSiteEnum.cachetime.getStatus());
                            }
                            sendArticleSyncMessage(article);
                        }
                    }

                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    // 从缓存中取对应栏目的新闻,如无则刷新缓存
    public List<String> flushCache(String key, Map<String, Object> map) {
        @SuppressWarnings("unchecked")
        List<String> titlelist = jedisUtils.get(JedisKey.getNewsKey(key), List.class);
        List<String> titles = new ArrayList<>();
        if (titlelist == null || titlelist.size() == 0) {
            // 从数据库中取最多60条数据
            List<ArticleOutModel> articleOutModels = articleRMapper.getOutterNews(map);
            if (articleOutModels != null && articleOutModels.size() != 0) {
                for (ArticleOutModel articleOutModel : articleOutModels) {
                    titles.add(articleOutModel.getArticleTitle());
                }
                // 刷新缓存
                jedisUtils.set(JedisKey.getNewsKey(key), titles, NewsWebSiteEnum.cachetime.getStatus());
            }
            return titles;
        } else {
            return titlelist;
        }
    }
}
