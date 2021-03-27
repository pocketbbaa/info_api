package com.kg.platform.dao.write.admin;

import com.kg.platform.dao.entity.KgRitRollout;

public interface KgRitRolloutWMapper {
    int deleteByPrimaryKey(Long id);

    int insert(KgRitRollout record);

    int insertSelective(KgRitRollout record);

    KgRitRollout selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(KgRitRollout record);

    int updateByPrimaryKey(KgRitRollout record);
}