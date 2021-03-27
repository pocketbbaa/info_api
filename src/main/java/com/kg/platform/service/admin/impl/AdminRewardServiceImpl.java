package com.kg.platform.service.admin.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.mongo.MongoTables;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.mq.MQConfig;
import com.kg.platform.common.mq.MQProduct;
import com.kg.platform.common.utils.Bean2MapUtil;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.common.utils.message.SendSms;
import com.kg.platform.dao.entity.Article;
import com.kg.platform.dao.entity.PushMessage;
import com.kg.platform.dao.read.ArticleRMapper;
import com.kg.platform.dao.read.UserRMapper;
import com.kg.platform.enumeration.BusinessTypeEnum;
import com.kg.platform.model.in.ArticleInModel;
import com.kg.platform.model.out.ArticleOutModel;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.service.PushService;
import com.kg.platform.service.admin.AdminRewardService;
import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;

@Service
public class AdminRewardServiceImpl implements AdminRewardService {

    private static Logger logger = LoggerFactory.getLogger(AdminRewardServiceImpl.class);

    @Autowired
    private UserRMapper userRMapper;
    @Autowired
    private MQProduct mqProduct;
    @Autowired
    private MQConfig kgRewardFirstPostMqConfig;
    @Autowired
    private MQConfig kgRewardSetQualityMqConfig;
    @Autowired
    private MQConfig kgRewardSetTopMqConfig;
    @Autowired
    private MQConfig kgRewardSetRecommendMqConfig;
    @Autowired
    private SendSms sendSms;
    @Autowired
    private ArticleRMapper articleRMapper;
    @Inject
    private IDGen idGenerater;

    @Autowired
    private PushService pushService;

    @Override
    public void setDisplayStatusReward(Integer displayStatus, String articleId, Article article1, Integer displayStatusOld, Integer publishStatus) {
        logger.info("【获取置顶推荐奖励】displayStatus：" + displayStatus + "   displayStatusOld:" + displayStatusOld + "  publishStatus:" + publishStatus + "  articleId：" + articleId + "  article1：" + JSONObject.toJSONString(article1));

        if (displayStatus != 2 && displayStatus != 3) {
            return;
        }

        //如果是草稿则不做原显示校验
        if (publishStatus != 4 && publishStatus != 0) {
            if (displayStatusOld != null && displayStatus.equals(displayStatusOld)) {
                logger.info("【获取置顶推荐奖励】 显示状态未改变，不发送奖励 displayStatus：" + displayStatus + "  displayStatusOld：" + displayStatusOld);
                return;
            }
        }
        String pushMessage = "";
        String smsMessage = "";
        String articleTitle = article1.getArticleTitle();
        String createUser = article1.getCreateUser();
        if (StringUtils.isEmpty(createUser)) {
            return;
        }
        UserkgOutModel userkgOutModel = userRMapper.selectByPrimaryKey(Long.valueOf(createUser));
        if (displayStatus == 2) {
            sendkgRewardSetTopMqMessage(articleId);
            pushMessage = "您的文章《" + articleTitle + "》已被设置为首页置顶，500氪金已发放至您的账户，期待您的更多优质原创作品！";
            smsMessage = "【千氪】您的文章《" + articleTitle + "》已被设置为首页置顶，500氪金已发放至您的账户，期待您的更多优质原创作品！";
        }
        if (displayStatus == 3) {
            sendkgRewardSetRecommendMqMessage(articleId);
            pushMessage = "您的文章《" + articleTitle + "》获得系统推荐，500氪金奖励已发放至您的账户，期待您的更多优质原创作品！";
            smsMessage = "【千氪】您的文章《" + articleTitle + "》获得系统推荐，500氪金奖励已发放至您的账户，期待您的更多优质原创作品！";
        }
        if (StringUtils.isNotEmpty(createUser) && StringUtils.isNotEmpty(pushMessage)) {
            rewardPush(createUser, pushMessage);
        }
        if (userkgOutModel != null && StringUtils.isNotEmpty(smsMessage)) {
            sendSms.send(userkgOutModel.getUserMobile(), smsMessage);
        }
        logger.info("【获取置顶推荐奖励】 结束 ...");
    }

    @Override
    public void setFirstPostReward(Article article) {
        String createUser = article.getCreateUser();
        String id = article.getArticleId();
        if (StringUtils.isEmpty(createUser) || StringUtils.isEmpty(id)) {
            return;
        }
        Long userId = Long.valueOf(createUser);
        Long articleId = Long.valueOf(id);

        boolean success = checkFirstPost(userId, articleId);
        if (success) {
            // 首次发文并审核通过后发推送+短信
            sendkgRewardFirstPostPushSmsMessage(userId, createUser);
            // 首次发文并审核通过后自动发放奖励
            sendkgRewardFirstPostMqMessage(article);
        }

    }

    @Override
    public void setFirstPostRewardForBatch(String articleIds) {
        sendkgRewardFirstPostMqMessageForBatch(articleIds);
        sendkgRewardFirstPostPushSmsMessageForBatch(articleIds);
    }

    @Override
    public void markHighQualityArticlesReward(String articleId) {
        sendkgRewardSetQualityMqMessage(articleId);
        sendPushSmsMessage(articleId);
    }

    /**
     * 设置优质原创发送推送短信
     *
     * @param articleId
     */
    private void sendPushSmsMessage(String articleId) {
        logger.info("【设置优质原创发送推送短信】articleId：" + articleId);
        ArticleInModel articleInModel = new ArticleInModel();
        articleInModel.setArticleId(Long.valueOf(articleId));
        ArticleOutModel articleOutModel = articleRMapper.getArticleCreateuser(articleInModel);
        String articleTitle = articleOutModel.getArticleTitle();
        Long createUser = articleOutModel.getCreateUser();
        String pushMessage = "您的文章《" + articleTitle + "》被标记为优质发文，1500氪金已发放至您的账户，期待您的更多优质原创作品！";
        String smsMessage = "【千氪】您的文章《" + articleTitle + "》被标记为优质发文，1500氪金已发放至您的账户，期待您的更多优质原创作品！";
        UserkgOutModel userkgOutModel = userRMapper.selectByPrimaryKey(createUser);
        if (createUser != null) {
            rewardPush(String.valueOf(createUser), pushMessage);
        }
        if (userkgOutModel != null) {
            sendSms.send(userkgOutModel.getUserMobile(), smsMessage);
        }
        logger.info("【设置优质原创发送推送短信】 结束 ...");
    }

    /**
     * 设置优质原创奖励消息
     *
     * @param articleId
     */
    private void sendkgRewardSetQualityMqMessage(String articleId) {
        Map<String, String> mqMap = new HashMap<>();
        mqMap.put("articleId", articleId);
        mqProduct.sendMessage(kgRewardSetQualityMqConfig.getTopic(), kgRewardSetQualityMqConfig.getTags(), articleId,
                JSONObject.toJSONString(mqMap));
    }

    /**
     * 首次发文推送短信（批量审核处理）
     *
     * @param articleIds
     */
    private void sendkgRewardFirstPostPushSmsMessageForBatch(String articleIds) {
        String[] articleIdArray = articleIds.split(",");
        Map<Long, List<Long>> users = new HashMap<>();
        for (String articleId : articleIdArray) {
            ArticleInModel inModel = new ArticleInModel();
            inModel.setArticleId(Long.valueOf(articleId));
            ArticleOutModel articleOutModel = articleRMapper.getArticleCreateuser(inModel);
            Long userId = articleOutModel.getCreateUser();
            Long article = Long.valueOf(articleOutModel.getArticleId());
            List<Long> ids = users.get(userId);
            if (org.springframework.util.CollectionUtils.isEmpty(ids)) {
                ids = new ArrayList<>();
            }
            ids.add(article);
            users.put(userId, ids);
        }
        for (Long userId : users.keySet()) {
            UserkgOutModel userKgModel = userRMapper.selectByPrimaryKey(userId);
            if (userKgModel == null) {
                logger.info("【发送首次发文奖励推送和短信】,用户不存在 userKgModel：null");
                return;
            }
            List<Long> aIds = users.get(userId);
            Integer postCount = userRMapper.postCountForBatch(userId, aIds);
            if (postCount != null && postCount > 0) {
                logger.info("【发送首次发文奖励推送和短信】,该用户不是首次发文 userId:" + userId + "  aIds:" + JSONObject.toJSONString(aIds) + "  postCount:" + postCount);
                return;
            }
            if (postCount != null && postCount > 0) {
                logger.info("【首次发文奖励】,该用户不是首次发文 userId:" + userId + "  aIds:" + JSONObject.toJSONString(aIds) + "  postCount:" + postCount);
                return;
            }

            long getCount = MongoUtils.count(MongoTables.ACCOUNT_FLOW_KG, buildQuery(userId));
            if (getCount > 0) {
                logger.info("【发送首次发文奖励推送和短信】,该用户已经获取过奖励 userId:" + userId + "  getCount:" + getCount);
                return;
            }

            UserkgOutModel userkgOutModel = userRMapper.selectByPrimaryKey(userId);
            String userName = userkgOutModel.getUserName();

            String pushMessage = "您已在千氪完成首次发文，1000氪金奖励已发放至您的账户，期待您的更多优质原创作品！";
            String smsMessage = "【千氪】" + userName + "，您已在千氪完成首次发文，1000氪金奖励已发放至您的账户，期待您的更多优质原创作品！";
            rewardPush(String.valueOf(userId), pushMessage);
            sendSms.send(userkgOutModel.getUserMobile(), smsMessage);
            logger.info("【发送首次发文奖励推送和短信】 结束 ...");
        }

    }

    /**
     * 发送首次发文奖励消息
     *
     * @param article
     */
    private void sendkgRewardFirstPostMqMessageForBatch(String articleIds) {
        Map<String, String> mqMap = new HashMap<>();
        mqMap.put("userId", "-2");
        mqMap.put("articleId", articleIds);
        mqProduct.sendMessage(kgRewardFirstPostMqConfig.getTopic(), kgRewardFirstPostMqConfig.getTags(), String.valueOf(idGenerater.nextId()),
                JSONObject.toJSONString(mqMap));
    }

    /**
     * 发送首次发文奖励推送和短信
     *
     * @param article
     */
    private void sendkgRewardFirstPostPushSmsMessage(Long userId, String createUser) {

        UserkgOutModel userkgOutModel = userRMapper.selectByPrimaryKey(userId);
        String userName = userkgOutModel.getUserName();

        String pushMessage = "您已在千氪完成首次发文，1000氪金奖励已发放至您的账户，期待您的更多优质原创作品！";
        String smsMessage = "【千氪】" + userName + "，您已在千氪完成首次发文，1000氪金奖励已发放至您的账户，期待您的更多优质原创作品！";
        rewardPush(createUser, pushMessage);
        sendSms.send(userkgOutModel.getUserMobile(), smsMessage);
        logger.info("【发送首次发文奖励推送和短信】 结束 ...");
    }

    private boolean checkFirstPost(Long userId, Long articleId) {
        long getCount = MongoUtils.count(MongoTables.ACCOUNT_FLOW_KG, buildQuery(userId));
        if (getCount > 0) {
            logger.info("【发送首次发文奖励推送和短信】,该用户已经获取过奖励 userId:" + userId + "  getCount:" + getCount);
            return false;
        }

        UserkgOutModel userKgModel = userRMapper.selectByPrimaryKey(userId);
        if (userKgModel == null) {
            logger.info("【发送首次发文奖励推送和短信】,用户不存在 userKgModel：null");
            return false;
        }

        Integer postCount = userRMapper.postCount(userId, articleId);
        if (postCount != null && postCount > 0) {
            logger.info("【发送首次发文奖励推送和短信】,该用户不是首次发文 userId:" + userId + "  articleId:" + articleId + "  postCount:" + postCount);
            return false;
        }
        return true;
    }

    /**
     * 构建查询参数
     *
     * @param userId
     * @return
     */
    private Bson buildQuery(Long userId) {
        BasicDBObject query = new BasicDBObject();
        query.append("user_id", userId);
        query.append("business_type_id", BusinessTypeEnum.KG_REWARD_FIRST_POST.getStatus());
        return query;
    }

    /**
     * 发送首次发文奖励消息
     *
     * @param article
     */
    private void sendkgRewardFirstPostMqMessage(Article article) {
        Map<String, String> mqMap = new HashMap<>();
        mqMap.put("userId", article.getCreateUser());
        mqMap.put("articleId", article.getArticleId());
        mqProduct.sendMessage(kgRewardFirstPostMqConfig.getTopic(), kgRewardFirstPostMqConfig.getTags(), article.getCreateUser(),
                JSONObject.toJSONString(mqMap));
    }

    /**
     * 设置置顶奖励消息
     *
     * @param articleId
     */
    private void sendkgRewardSetTopMqMessage(String articleId) {
        Map<String, String> mqMap = new HashMap<>();
        mqMap.put("articleId", articleId);
        mqProduct.sendMessage(kgRewardSetTopMqConfig.getTopic(), kgRewardSetTopMqConfig.getTags(), articleId,
                JSONObject.toJSONString(mqMap));
    }

    /**
     * 设置置推荐励消息
     *
     * @param articleId
     */
    private void sendkgRewardSetRecommendMqMessage(String articleId) {
        Map<String, String> mqMap = new HashMap<>();
        mqMap.put("articleId", articleId);
        mqProduct.sendMessage(kgRewardSetRecommendMqConfig.getTopic(), kgRewardSetRecommendMqConfig.getTags(), articleId,
                JSONObject.toJSONString(mqMap));
    }

    /**
     * 推送消息
     *
     * @param userId
     * @param messageText
     * @param tags
     */
    private void rewardPush(String userId, String messageText) {
        logger.info("【推送消息】userId：" + userId + "  messageText：" + messageText);


        pushService.rewardPush(userId,"收到奖励",messageText,"publishArticleReward");
    }
}
