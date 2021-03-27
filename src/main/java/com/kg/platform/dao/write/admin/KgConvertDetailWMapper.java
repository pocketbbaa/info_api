package com.kg.platform.dao.write.admin;

import com.kg.platform.dao.entity.KgConvertDetail;

public interface KgConvertDetailWMapper {
    int deleteByPrimaryKey(Long id);

    int insert(KgConvertDetail record);

    int insertSelective(KgConvertDetail record);

    KgConvertDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(KgConvertDetail record);

    int updateByPrimaryKey(KgConvertDetail record);
}