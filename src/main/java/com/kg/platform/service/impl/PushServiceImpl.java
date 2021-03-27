package com.kg.platform.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.dao.entity.Article;
import com.kg.platform.dao.read.ArticleRMapper;
import com.kg.platform.dao.read.UserCommentRMapper;
import com.kg.platform.dao.read.UserProfileRMapper;
import com.kg.platform.dao.read.UserRMapper;
import com.kg.platform.enumeration.PushClassifyEnum;
import com.kg.platform.enumeration.PushMessageEnum;
import com.kg.platform.enumeration.PushPlaceEnum;
import com.kg.platform.enumeration.PushTypeEnum;
import com.kg.platform.model.in.*;
import com.kg.platform.model.out.ArticleOutModel;
import com.kg.platform.model.out.UserCommentOutModel;
import com.kg.platform.model.out.UserProfileOutModel;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.UserCommentRequest;
import com.kg.platform.model.request.admin.ArticleEditRequest;
import com.kg.platform.model.response.CommentUser;
import com.kg.platform.service.PushMessageService;
import com.kg.platform.service.PushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * api推送服务
 */
@Service
public class PushServiceImpl implements PushService {

    private static final Logger log = LoggerFactory.getLogger(PushServiceImpl.class);

    @Autowired
    private ArticleRMapper articleRMapper;

    @Autowired
    private UserRMapper userRMapper;

    @Autowired
    private UserCommentRMapper userCommentRMapper;

    @Autowired
    private PushMessageService pushMessageService;

    @Autowired
    private UserProfileRMapper userProfileRMapper;

    /**
     * 1: 收到到回复评论时，立即推送
     * 2: 作者回复评论时，立即推送
     *
     * @param inModel
     */
    @Override
    public void commentReplyPush(CommentReplyInModel inModel) {
        log.info("【推送消息】子回复推送 inModel：" + JSONObject.toJSONString(inModel));
        String title, message;

        String commentDesc = inModel.getCommentDesc();
        Long userId = inModel.getUserId();
        Long toUserId = inModel.getToUserId();
        Long articleId = inModel.getArticleId();
        Long parentCommentId = inModel.getParentCommentId();

        if (StringUtils.isEmpty(commentDesc) || userId == null || articleId == null) {
            log.info("【push message 】 未执行推送，数据不完整 ：" + JSONObject.toJSONString(inModel));
            return;
        }
        ArticleOutModel articleOutModel = getArticleOutModel(articleId);
        if (articleOutModel == null) {
            log.info("【push message 】 没有获取到文章信息 articleId：" + articleId);
            return;
        }

        String messageImg = getMessageImg(userId);
        String articleTitle = articleOutModel.getArticleTitle();
        Long createUser = articleOutModel.getCreateUser();
        String recieve = getRecieve(toUserId, parentCommentId);

        //评论回复推送额外参数
        Map<String, Object> extra = new HashMap<>();
        extra.put("type", 6);
        extra.put("articleId", inModel.getArticleId());
        extra.put("body", inModel.getParentCommentId());

        if (userId.equals(createUser)) {
            title = PushMessageEnum.GET_AUTHOR_REPLY.getType();
            message = PushMessageEnum.GET_AUTHOR_REPLY.getMessage().replace("{1}", articleTitle).replace("{2}", commentDesc);
            if (StringUtils.isEmpty(recieve)) {
                return;
            }
            log.info("【push message 】recieve:" + recieve + "  title:" + title + "  message:" + message);

            //推送消息
            pushMessageService.pushMessge(MessageInModel.buildMessage(recieve,
                    title,
                    message,
                    extra,
                    PushClassifyEnum.DYNAMIC_MESSAGE,
                    PushTypeEnum.PUSH_SPECIFIED,
                    "commentReply",
                    1,
                    PushPlaceEnum.ALL.getCode()).buildMessageAvatar(messageImg));
            return;
        }

        String userName = getUserName(userId);
        message = PushMessageEnum.GET_REPLY.getMessage().replace("{1}", userName).replace("{2}", articleTitle).replace("{3}", commentDesc);
        log.info("【push message 】recieve:" + recieve + "  title:" + PushMessageEnum.GET_REPLY.getType() + "  message:" + message);

        //推送消息
        pushMessageService.pushMessge(MessageInModel.buildMessage(recieve,
                PushMessageEnum.GET_REPLY.getType(),
                message,
                extra,
                PushClassifyEnum.DYNAMIC_MESSAGE,
                PushTypeEnum.PUSH_SPECIFIED,
                "commentReply",
                1,
                PushPlaceEnum.ALL.getCode()).buildMessageAvatar(messageImg));
    }


    @Override
    public void ColumnApproval(String title, String messageText, String userId, String tag) {
        log.info("【推送消息】专栏审核通过通知推送 userId：" + userId);
        pushMessageService.pushMessge(MessageInModel.buildMessage(userId,
                PushMessageEnum.SYSTEM_TITLE.getType(),
                messageText,
                null,
                PushClassifyEnum.SYSTEM_MESSAGE,
                PushTypeEnum.PUSH_SPECIFIED,
                "columnApproval",
                1,
                PushPlaceEnum.ALL.getCode()));
    }

    @Override
    public void pushBindInfo(String deviceId, int osVersion, String userId) {
        log.info("【推送消息】设备绑定消息 userId：" + userId);
        pushMessageService.pushMessge(MessageInModel.buildPushBindInfoMessage(deviceId,
                osVersion,
                userId,
                "bindMobile",
                PushTypeEnum.BINDING_ALIAS
                , PushPlaceEnum.PHOINE.getCode()));
    }

    @Override
    public void auditUserCert(String title, String messageText, String userId, String tags) {
        log.info("【推送消息】实名认证通知推送 userId：" + userId);
        pushMessageService.pushMessge(MessageInModel.buildMessage(userId,
                title,
                messageText,
                null,
                PushClassifyEnum.SYSTEM_MESSAGE,
                PushTypeEnum.PUSH_SPECIFIED,
                tags,
                0,
                PushPlaceEnum.ALL.getCode()));
    }

    @Override
    public void publishArticleBonus(String userId, String messageText, String tags, Integer pushPlace) {
        log.info("【推送消息】发文奖励/进贡奖励通知 userId：" + userId);
        pushMessageService.pushMessge(MessageInModel.buildMessage(userId,
                PushMessageEnum.SYSTEM_TITLE.getType(),
                messageText,
                null,
                PushClassifyEnum.SYSTEM_MESSAGE,
                PushTypeEnum.PUSH_SPECIFIED,
                tags,
                0,
                PushPlaceEnum.ALL.getCode()));
    }

    @Override
    public void addComment(UserCommentRequest request) {
        log.info("【推送消息】主回复通知推送 request：" + JSONObject.toJSONString(request));
        ArticleOutModel articleOutModel = articleRMapper.selectByPrimaryKey(Long.valueOf(request.getArticleId()));
        if (articleOutModel == null) {
            log.info("【推送消息】主回复通知推送 没获取到文章信息 request.getArticleId()：" + request.getArticleId());
            return;
        }
        UserProfileInModel userProfileInModel = new UserProfileInModel();
        userProfileInModel.setUserId(Long.valueOf(request.getUserId()));
        UserProfileOutModel outModel = userProfileRMapper.getPersonal(userProfileInModel);
        if (outModel == null) {
            log.info("【推送消息】主回复通知推送 没获取到用户信息 request.getUserId()：" + request.getUserId());
            return;
        }
        String userName = outModel.getUsername();
        userName = StringUtils.isEmpty(userName) ? "" : userName;
        String messageText = PushMessageEnum.GET_COMMENT.getMessage().replace("{1}", userName)
                .replace("{2}", articleOutModel.getArticleTitle())
                .replace("{3}", request.getCommentDesc());

        //评论回复推送额外参数
        Map<String, Object> extra = new HashMap<>();
        extra.put("type", 6);
        extra.put("articleId", request.getArticleId());
        extra.put("body", request.getCommentId());

        String messageImg = getMessageImg(Long.valueOf(request.getUserId()));

        pushMessageService.pushMessge(MessageInModel.buildMessage(articleOutModel.getCreateUser().toString(),
                PushMessageEnum.GET_COMMENT.getType(),
                messageText,
                extra,
                PushClassifyEnum.DYNAMIC_MESSAGE,
                PushTypeEnum.PUSH_SPECIFIED,
                "addComment",
                1,
                PushPlaceEnum.ALL.getCode()).buildMessageAvatar(messageImg));

    }

    @Override
    public void rewardPush(String userId, String title, String messageText, String tag) {
        log.info("【推送消息】获得奖励通知推送 userId：" + userId);
        pushMessageService.pushMessge(MessageInModel.buildMessage(userId,
                title,
                messageText,
                null,
                PushClassifyEnum.REWARD_MESSAGE,
                PushTypeEnum.PUSH_SPECIFIED,
                tag,
                0,
                PushPlaceEnum.ALL.getCode()));
    }

    @Override
    public void publishArticle(Article article, ArticleEditRequest request, Integer serviceType) {
        log.info("【推送消息】发文推送 article：" + JSONObject.toJSONString(article));
        String messageText = article.getArticleDescription();
        String title = article.getArticleTitle();
        String pushMessage = request.getPushMessage();
        String pushTitle = request.getPushTitle();
        if (StringUtils.isNotEmpty(pushMessage)) {
            messageText = pushMessage;
        }
        if (StringUtils.isNotEmpty(pushTitle)) {
            title = pushTitle;
        }

        Map<String, Object> extra = new HashMap<>();
        extra.put("type", 3);
        extra.put("articleId", request.getArticleId().toString());
        extra.put("articleImage", request.getImage());
        extra.put("serviceType", serviceType);// 1.推送文章和视频 2.标记为优质

        pushMessageService.pushMessge(MessageInModel.buildMessage(null,
                title,
                messageText,
                extra,
                PushClassifyEnum.HOT_ARTICLE_MESSAGE,
                PushTypeEnum.PUSH_ALL,
                "addHotNews",
                0,
                PushPlaceEnum.PHOINE.getCode()));
    }

    @Override
    public void logOut(String userId, String deviceId, int osVersion, String tag) {
        log.info("【推送消息】登出解绑别名消息 userId：" + userId);
        pushMessageService.pushMessge(MessageInModel.buildPushBindInfoMessage(deviceId,
                osVersion,
                userId,
                tag,
                PushTypeEnum.UNBINDING_ALIAS
                , PushPlaceEnum.PHOINE.getCode()));
    }

    @Override
    public void getShareKgBonus(String userId, String title, String messageText, String tags, int pushPlace) {
        log.info("【推送消息】分享奖励消息 userId：" + userId);
        pushMessageService.pushMessge(MessageInModel.buildMessage(userId,
                title,
                messageText,
                null,
                PushClassifyEnum.SYSTEM_MESSAGE,
                PushTypeEnum.PUSH_SPECIFIED,
                tags,
                0,
                pushPlace));

    }

    @Override
    public void rewardDisciple(String userId, String title, String messageText, int pushPlace, String tags) {
        log.info("【推送消息】打赏徒弟消息推送 userId：" + userId);
        pushMessageService.pushMessge(MessageInModel.buildMessage(userId,
                title,
                messageText,
                null,
                PushClassifyEnum.SYSTEM_MESSAGE,
                PushTypeEnum.PUSH_SPECIFIED,
                tags,
                0,
                pushPlace));
    }

    @Override
    public void updateAddUbalance(String userId, String title, String messageText, String tags, int pushPlace) {
        log.info("【推送消息】打赏文章消息 userId：" + userId);
        pushMessageService.pushMessge(MessageInModel.buildMessage(userId,
                title,
                messageText,
                null,
                PushClassifyEnum.SYSTEM_MESSAGE,
                PushTypeEnum.PUSH_SPECIFIED,
                tags,
                0,
                pushPlace));
    }

    @Override
    public void autoPublishArticle(String articleTitle, String messageText, Map<String, Object> extra, int pushPlace) {
        log.info("【推送消息】定时发文消息推送 articleTitle：" + articleTitle);
        pushMessageService.pushMessge(MessageInModel.buildMessage(null,
                articleTitle,
                messageText,
                extra,
                PushClassifyEnum.HOT_ARTICLE_MESSAGE,
                PushTypeEnum.PUSH_ALL,
                "addHotNews",
                0,
                pushPlace));

    }

    @Override
    public void tagBind(String userId, String deviceId, int osVersion, int type, String tags) {
        log.info("【推送消息】绑定解绑tag userId：" + userId);
        pushMessageService.pushMessge(MessageInModel.buildPushBindInfoMessage(deviceId,
                osVersion,
                userId,
                tags,
                PushTypeEnum.getByCode(type),
                PushPlaceEnum.PHOINE.getCode()));
    }

    @Override
    public void addNewsFlash(String title, String newsflashText, String newFlashId) {

        log.info("【推送消息】快讯消息推送 title：" + title);
        Map<String, Object> extra = new HashMap<>();
        extra.put("type", 4);
        extra.put("newsFlashId", newFlashId);
        extra.put("serviceType", 1);

        pushMessageService.pushMessge(MessageInModel.buildMessage(null,
                title,
                newsflashText,
                extra,
                PushClassifyEnum.FLASH_UPDATE_MESSAGE,
                PushTypeEnum.PUSH_ALL,
                "push",
                0,
                PushPlaceEnum.PHOINE.getCode()));


    }


    /**
     * 根据文章ID获取文章基本信息
     *
     * @param articleId
     * @return
     */
    private ArticleOutModel getArticleOutModel(Long articleId) {
        ArticleInModel articleInModel = new ArticleInModel();
        articleInModel.setArticleId(articleId);
        return articleRMapper.getArticleCreateuser(articleInModel);
    }

    /**
     * 根据用户ID获取用户名
     *
     * @param userId
     * @return
     */
    private String getUserName(Long userId) {
        CommentUser commentUser = userRMapper.getCommentUserByUserId(userId);
        return commentUser.getUserName();
    }

    /**
     * 获取子评论推送对象
     *
     * @param toUserId
     * @param parentCommentId
     * @return
     */
    private String getRecieve(Long toUserId, Long parentCommentId) {
        if (toUserId == null) {
            if (parentCommentId > 0) {
                UserCommentOutModel userComment = userCommentRMapper.selectByPrimaryKey(parentCommentId);
                if (userComment == null) {
                    return null;
                }
                return String.valueOf(userComment.getUserId());
            }
        }
        return String.valueOf(toUserId);
    }

    /**
     * 获取推送头像
     *
     * @param userId
     * @return
     */
    private String getMessageImg(Long userId) {
        UserInModel model = new UserInModel();
        model.setUserId(userId);
        UserkgOutModel userkgOutModel = userRMapper.getUserProfiles(model);
        if (userkgOutModel == null) {
            log.info("【push message 】 没有获取到用户信息 userId：" + userId);
            return "";
        }
        return userkgOutModel.getAvatar();
    }
}
