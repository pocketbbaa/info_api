package com.kg.platform.dao.write;

import com.kg.platform.model.in.KgArticleExtendInModel;

public interface KgArticleExtendWMapper {


    int deleteByPrimaryKey(Long articleId);

    int insert(KgArticleExtendInModel record);

    int insertSelective(KgArticleExtendInModel record);

    int updateByPrimaryKeySelective(KgArticleExtendInModel record);

    int updateByPrimaryKey(KgArticleExtendInModel record);

}