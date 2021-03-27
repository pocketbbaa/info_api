package com.kg.platform.service;

import com.kg.platform.dao.entity.Article;
import com.kg.platform.model.in.CommentReplyInModel;
import com.kg.platform.model.request.UserCommentRequest;
import com.kg.platform.model.request.admin.ArticleEditRequest;
import com.kg.platform.model.response.admin.CommentQueryResponse;

import java.util.Map;

public interface PushService {

    /**
     * 子回复通知推送
     *
     * @param inModel
     */
    void commentReplyPush(CommentReplyInModel inModel);

    /**
     * 专栏审核通过通知推送
     *
     * @param s
     * @param title
     * @param messageText
     * @param string
     */
    void ColumnApproval(String title, String messageText, String userId, String tag);

    /**
     * 设备绑定消息
     *
     * @param servletRequest
     * @param userId
     */
    void pushBindInfo(String deviceId, int osVersion, String userId);

    /**
     * 实名认证通知推送
     *
     * @param title
     * @param messageText
     * @param receive
     * @param tags
     */
    void auditUserCert(String title, String messageText, String userId, String tags);

    /**
     * 发文奖励/进贡奖励通知
     *
     * @param userId
     * @param messageText
     * @param tags
     * @param pushPlace
     */
    void publishArticleBonus(String userId, String messageText, String tags, Integer pushPlace);

    /**
     * 主回复通知推送
     *
     * @param userId
     * @param l
     * @param addComment
     */
    void addComment(UserCommentRequest request);

    /**
     * 获得奖励通知推送
     *
     * @param userId
     * @param title
     * @param messageText
     * @param publishArticleReward
     */
    void rewardPush(String userId, String title, String messageText, String tag);

    /**
     * 发文推送
     *
     * @param article
     * @param request
     * @param serviceType
     */
    void publishArticle(Article article, ArticleEditRequest request, Integer serviceType);

    /**
     * 登出解绑别名消息
     *
     * @param userId
     * @param device_id
     * @param os_version
     * @param unBindMobile
     */
    void logOut(String userId, String device_id, int os_version, String unBindMobile);

    /**
     * 分享奖励消息
     *
     * @param userId
     * @param 系统通知
     * @param messageText
     * @param tags
     * @param i
     */
    void getShareKgBonus(String userId, String title, String messageText, String tags, int pushPlace);

    /**
     * 打赏徒弟消息推送
     *
     * @param userId
     * @param 系统通知
     * @param messageText
     * @param i
     * @param discipleReward
     */
    void rewardDisciple(String userId, String title, String messageText, int pushPlace, String tags);

    /**
     * 打赏文章消息
     *
     * @param userId
     * @param 系统通知
     * @param messageText
     * @param articleReward
     * @param i
     */
    void updateAddUbalance(String userId, String title, String messageText, String tags, int pushPlace);

    /**
     * 定时发文消息推送
     *
     * @param articleTitle
     * @param messageText
     * @param extra
     * @param i
     */
    void autoPublishArticle(String articleTitle, String messageText, Map<String, Object> extra, int pushPlace);

    /**
     * 绑定解绑tag
     *
     * @param userId
     * @param deviceId
     * @param osVersion
     * @param type
     * @param tags
     */
    void tagBind(String userId, String deviceId, int osVersion, int type, String tags);

    /**
     * 快讯消息推送
     *
     * @param title
     * @param newsflashText
     * @param string
     */
    void addNewsFlash(String title, String newsflashText, String string);
}
