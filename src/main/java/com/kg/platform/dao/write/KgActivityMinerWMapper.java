package com.kg.platform.dao.write;

import com.kg.platform.model.in.KgActivityMinerInModel;

public interface KgActivityMinerWMapper {

    int deleteByPrimaryKey(Long minerId);

    int insert(KgActivityMinerInModel record);

    int insertSelective(KgActivityMinerInModel record);

    int updateByPrimaryKeySelective(KgActivityMinerInModel record);

    int updateByPrimaryKey(KgActivityMinerInModel record);

    int addJoinNums(KgActivityMinerInModel record);

    int reduceMinerNumber(KgActivityMinerInModel record);
}