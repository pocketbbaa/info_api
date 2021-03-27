package com.kg.platform.dao.write;

import com.kg.platform.model.in.KgInfoSwitchInModel;

public interface KgInfoSwitchWMapper {

    int deleteByPrimaryKey(Long userId);

    int insert(KgInfoSwitchInModel record);

    int insertSelective(KgInfoSwitchInModel record);

    int updateByPrimaryKeySelective(KgInfoSwitchInModel record);

    int updateByPrimaryKey(KgInfoSwitchInModel record);
}