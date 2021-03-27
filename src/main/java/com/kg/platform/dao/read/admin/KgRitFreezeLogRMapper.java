package com.kg.platform.dao.read.admin;

import com.kg.platform.dao.entity.KgRitFreezeLog;

public interface KgRitFreezeLogRMapper {
    int deleteByPrimaryKey(Long id);

    int insert(KgRitFreezeLog record);

    int insertSelective(KgRitFreezeLog record);

    KgRitFreezeLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(KgRitFreezeLog record);

    int updateByPrimaryKey(KgRitFreezeLog record);
}