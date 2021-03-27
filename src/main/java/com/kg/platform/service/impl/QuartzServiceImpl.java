package com.kg.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.mq.MQProduct;
import com.kg.platform.dao.entity.Article;
import com.kg.platform.dao.entity.PushMessage;
import com.kg.platform.dao.read.KgInfoSwitchRMapper;
import com.kg.platform.dao.read.admin.AdminArticleRMapper;
import com.kg.platform.dao.write.ArticleWMapper;
import com.kg.platform.enumeration.ArticleAuditStatusEnum;
import com.kg.platform.model.response.admin.ArticleQueryResponse;
import com.kg.platform.service.PushService;
import com.kg.platform.service.QuartzService;
import com.kg.platform.service.admin.ArticleService;
import com.kg.platform.common.mq.MQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("quartzService")
public class QuartzServiceImpl implements QuartzService {

    @Autowired
    private AdminArticleRMapper adminArticleRMapper;

    @Autowired
    private ArticleWMapper articleWMapper;

    @Inject
    private IDGen idGenerater;

    @Autowired
    private MQProduct mqProduct;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private MQConfig articleSyncMqConfig;

    @Autowired
    private PushService pushService;

    protected Logger logger = LoggerFactory.getLogger(QuartzServiceImpl.class);

    @SuppressWarnings("deprecation")
    @Override
    public void autoPublishArticle() {
        // 查找所有定时发布的文章并且在草稿中的文章，并且发布时间和当前时间匹配，则将状态设置为已发布
        List<ArticleQueryResponse> list = adminArticleRMapper.selectAutoPublishArticle();
        if (null != list && list.size() > 0) {
            list.forEach(item -> {
                if (null != item.getPublishTime()) {
                    Date date = item.getPublishTime();
                    // 到点了
                    if (System.currentTimeMillis()>=date.getTime()) {
                        Article article = new Article();
                        article.setArticleId(item.getArticleId());
                        article.setPublishStatus(ArticleAuditStatusEnum.PASS.getStatus());
						logger.info("定时发布文章触发成功，更新发布时间和审核时间------");
                        article.setPublishDate(new Date());
                        article.setAuditDate(new Date());
                        articleWMapper.updateByPrimaryKeySelective(article);
                        if (item.getIfPush() != null && item.getIfPush().intValue() == 1) {
                            //推送文章
                            mqPush(item);
                        }
                        if (item.getIfPlatformPublishAward() != null && item.getIfPlatformPublishAward().intValue() == 1) {
                            //发放基础发文奖励 app1.2.0屏蔽发文奖励
                            // articleService.pushPublishTvBonus(Long.valueOf(item.getArticleId()));
                        }
                        //改变了文章显示，则要重新计算文章总量，将此记录记入mongo
                        articleService.insertCountArticleRecord(item.getArticleId(), item.getColumnId(), item.getSecondColumn(), System.currentTimeMillis(), 1);
                        sendArticleSyncMessage(article);
                    }
                }
            });
        }
    }

    /**
     * 发送数据同步消息
     *
     * @param article
     */
    private void sendArticleSyncMessage(Article article) {
        Map<String, Object> map = new HashMap<>();
        map.put("types", "0");
        map.put("articleId", article.getArticleId());
        logger.info("ES同步文章MQ------topic:{},  map:{}", articleSyncMqConfig.getTopic(), JSONObject.toJSON(map));
        mqProduct.sendMessage(articleSyncMqConfig.getTopic(), articleSyncMqConfig.getTags(), null, JSON.toJSONString(map));
    }

    public void mqPush(ArticleQueryResponse article) {
        logger.info(">>>>>>>>>>推送定时发文的消息");
        String messageText = article.getArticleDescription();
        Map<String, Object> extra = new HashMap<>();
        extra.put("type", 3);
        extra.put("articleId", article.getArticleId());
        extra.put("articleImage", article.getArticleImage());
        extra.put("serviceType", 1);

        pushService.autoPublishArticle(article.getArticleTitle(), messageText, extra, 2);
    }

}
