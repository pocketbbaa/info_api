package com.kg.platform.dao.write;

import com.kg.platform.model.in.KgMinerAssistInModel;

public interface KgMinerAssistWMapper {

    int deleteByPrimaryKey(Long assistId);

    int insert(KgMinerAssistInModel record);

    int insertSelective(KgMinerAssistInModel record);

    int updateByPrimaryKeySelective(KgMinerAssistInModel record);

    int updateByPrimaryKey(KgMinerAssistInModel record);
}