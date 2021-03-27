package com.kg.platform.service.admin;

import com.kg.platform.dao.entity.Article;

/**
 * 后台管理发放奖励相关
 */
public interface AdminRewardService {


    /**
     * 设置置顶推荐奖励
     *
     * @param displayStatus
     * @param s
     * @param article1
     * @param displayStatusOld
     * @param i
     */
    void setDisplayStatusReward(Integer displayStatus, String articleId, Article article1, Integer displayStatusOld, Integer publishStatus);

    /**
     * 设置首次发文奖励
     *
     * @param article
     */
    void setFirstPostReward(Article article);

    /**
     * 设置首次发文奖励 批量审核
     *
     * @param articleIds
     */
    void setFirstPostRewardForBatch(String articleIds);

    /**
     * 设置优质原创奖励
     *
     * @param articleId
     */
    void markHighQualityArticlesReward(String articleId);
}
