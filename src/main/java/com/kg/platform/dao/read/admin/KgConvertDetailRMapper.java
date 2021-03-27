package com.kg.platform.dao.read.admin;

import com.kg.platform.dao.entity.KgConvertDetail;

public interface KgConvertDetailRMapper {
    int deleteByPrimaryKey(Long id);

    int insert(KgConvertDetail record);

    int insertSelective(KgConvertDetail record);

    KgConvertDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(KgConvertDetail record);

    int updateByPrimaryKey(KgConvertDetail record);
}