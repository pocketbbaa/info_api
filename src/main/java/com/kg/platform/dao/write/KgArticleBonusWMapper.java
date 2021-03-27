package com.kg.platform.dao.write;

import com.kg.platform.model.in.KgArticleBonusInModel;

public interface KgArticleBonusWMapper {

    int deleteByPrimaryKey(Long bonusId);

    int insert(KgArticleBonusInModel record);

    int insertSelective(KgArticleBonusInModel inModel);

    int updateByPrimaryKeySelective(KgArticleBonusInModel inModel);

    int updateByPrimaryKey(KgArticleBonusInModel record);

    int updateBonusStatusByArticleId(KgArticleBonusInModel record);

    int pauseBonusStatusByArticleId(KgArticleBonusInModel record);

    int startBonusStatusByArticleId(KgArticleBonusInModel record);

    int deleteByArticle(KgArticleBonusInModel inModel);

}