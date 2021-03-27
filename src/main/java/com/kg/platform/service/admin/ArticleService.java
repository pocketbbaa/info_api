package com.kg.platform.service.admin;

import java.util.List;

import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.dao.entity.Article;
import com.kg.platform.model.request.AccountRequest;
import com.kg.platform.model.request.admin.ArticleBatchReviewRequest;
import com.kg.platform.model.request.admin.ArticleEditRequest;
import com.kg.platform.model.request.admin.ArticleQueryRequest;
import com.kg.platform.model.request.admin.DeleteArticleRequest;
import com.kg.platform.model.response.PushArticleResponse;
import com.kg.platform.model.response.admin.*;

public interface ArticleService {

    /**
     * 获得文章列表
     *
     * @param request
     * @return
     */
    PageModel<ArticleQueryResponse> getArticleList(ArticleQueryRequest request, PageModel<ArticleQueryResponse> page);

    /**
     * 设置文章的排序字段
     *
     * @param articleId
     * @param displayOrder
     * @return
     */
    boolean setDisplayOrder(Long articleId, Integer displayOrder);

    /**
     * 审核文章
     *
     * @param articleId
     * @param auditUser
     * @param columnId
     * @param secondColumn
     * @param refuseReason
     * @return
     */
    boolean auditArticle(Long articleId, Long auditUser, Integer columnId, Integer secondColumn, Integer auditStatus,
                         String refuseReason);

    /**
     * 设置显示状态
     *
     * @param articleId
     * @param updateUser
     * @param displayStatus
     * @return
     */
    boolean setDisplayStatus(String articleId, Integer updateUser, Integer displayStatus);

    boolean setBlockchainUrl(String articleId, String blockchainUrl);

    /**
     * 发布文章 默认状态为通过
     *
     * @param request
     * @return
     */
    String publishArticle(ArticleEditRequest request);

    /**
     * 删除文章
     *
     * @param request
     * @return
     */
    boolean deleteArticle(DeleteArticleRequest request);

    /**
     * 获得文章详情
     *
     * @param articleId
     * @return
     */
    Article getArticleById(Long articleId);

    List<BonusQueryResponse> getBonus(ArticleEditRequest request);

    ArticleStatQueryResponse getArticleStat(ArticleEditRequest request);

    /**
     * 冻结发文奖励
     *
     * @param request
     * @return
     */
    Boolean freezePublishBonus(ArticleQueryRequest request);

    /**
     * 冻结发文奖励
     *
     * @param request
     * @return
     */
    Boolean markHighQualityArticles(ArticleQueryRequest request);

    /**
     * 查询文章是否已经获得额外钛值奖励
     *
     * @param request
     * @return
     */
    Boolean getArticleAddedTvBonus(ArticleQueryRequest request);

    /**
     * 查询文章是否已经获得额外氪金奖励
     *
     * @param request
     * @return
     */
    Boolean getArticleAddedTxbBonus(ArticleQueryRequest request);

    /**
     * 发文基础奖励入口 作者5tv 师傅50kg
     *
     * @param request
     * @return
     */
    Boolean publishArticleBonus(AccountRequest request);

    /**
     * 发文用户基础奖励
     *
     * @param request
     * @return
     */
    Boolean publishTvBonus(AccountRequest request);

    /**
     * 发文奖励进贡奖励
     *
     * @param request
     * @return
     */
    Boolean publishKGBonus(AccountRequest request);

    /**
     * 发送发文基础奖励推送MQ
     *
     * @param articleId
     */
    void pushPublishTvBonus(Long articleId);

    /**
     * 推送文章进行今日推送数量记录
     */
    void checkPushArticle();

    /**
     * 获取当日文章推送上限以及当日文章推送数量
     *
     * @return
     */
    PushArticleResponse getPushAticleInfo();

    /**
     * 批量审核
     *
     * @param request
     * @return
     */
    JsonEntity batchReviewArticle(ArticleBatchReviewRequest request) throws Exception;

    /**
     * 模糊查询文章列表
     *
     * @return
     */
    List<ArticleQueryResponse> getArticles(ArticleQueryRequest request);

    /**
     * 删除redis中根据分页和文章总量保存的文章列表信息
     *
     * @param columnId
     */
    void delArticlePageRedis(Integer columnId);

    void insertCountArticleRecord(String articleId, Integer columnId, Integer secondColumn, Long createDate, Integer type);

    /**
     * 待处理的内容举报列表
     *
     * @param page
     * @return
     */
    PageModel<AdminArticleReportResponse> toAuditList(PageModel<AdminArticleReportResponse> page);

    /**
     * 编辑文章页面举报信息列表
     *
     * @param page
     * @param request
     * @return
     */
    PageModel<AdminArticleReportByArticleIdResponse> toAuditListByArticleId(PageModel<AdminArticleReportByArticleIdResponse> page, ArticleEditRequest request);

    /**
     * 处理举报
     *
     * @param request
     * @return
     */
    JsonEntity disposeReport(ArticleEditRequest request);
}
