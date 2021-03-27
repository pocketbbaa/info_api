package com.kg.platform.dao.write.admin;

import com.kg.platform.dao.entity.KgRitFreezeLog;

public interface KgRitFreezeLogWMapper {
    int deleteByPrimaryKey(Long id);

    int insert(KgRitFreezeLog record);

    int insertSelective(KgRitFreezeLog record);

    KgRitFreezeLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(KgRitFreezeLog record);

    int updateByPrimaryKey(KgRitFreezeLog record);
}