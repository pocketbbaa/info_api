package com.kg.platform.dao.write;

import com.kg.platform.model.in.KgActivityCompetionInModel;

public interface KgActivityCompetionWMapper {

    int deleteByPrimaryKey(Integer competionId);

    int insert(KgActivityCompetionInModel record);

    int insertSelective(KgActivityCompetionInModel record);

    int updateByPrimaryKeySelective(KgActivityCompetionInModel record);

    int updateByPrimaryKey(KgActivityCompetionInModel record);
}