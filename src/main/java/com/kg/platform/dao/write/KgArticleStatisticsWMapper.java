package com.kg.platform.dao.write;

import com.kg.platform.model.in.KgArticleStatisticsInModel;

public interface KgArticleStatisticsWMapper {

    int deleteByPrimaryKey(Long articleId);

    int insert(KgArticleStatisticsInModel inModel);

    int insertSelective(KgArticleStatisticsInModel inModel);

    int updateByPrimaryKeySelective(KgArticleStatisticsInModel inModel);

    int updateByPrimaryKey(KgArticleStatisticsInModel inModel);

    int updateAddBonusTotal(KgArticleStatisticsInModel inModel);

    int updateStatistics(KgArticleStatisticsInModel inModel);

    int updateReductionBonusTotal(KgArticleStatisticsInModel inModel);

    // 文章奖励发放修改，领取奖励人数，修改奖励总金额
    int updateBonusValue(KgArticleStatisticsInModel inModel);

    int updateArticleSelective(KgArticleStatisticsInModel inModel);

    // 阅读奖励终止后初始化该文章统计表
    int initializeThe(KgArticleStatisticsInModel inModel);

    int addBrowseNum(KgArticleStatisticsInModel inModel);
}