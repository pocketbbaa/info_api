package com.kg.platform.dao.write;

import com.kg.platform.model.in.KgMinerRobInModel;

public interface KgMinerRobWMapper {

    int deleteByPrimaryKey(Long robId);

    int insert(KgMinerRobInModel record);

    int insertSelective(KgMinerRobInModel record);

    int updateByPrimaryKeySelective(KgMinerRobInModel record);

    int updateByPrimaryKey(KgMinerRobInModel record);
}