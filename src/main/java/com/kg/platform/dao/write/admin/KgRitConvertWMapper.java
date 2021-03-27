package com.kg.platform.dao.write.admin;

import com.kg.platform.dao.entity.KgRitConvert;

public interface KgRitConvertWMapper {
    int deleteByPrimaryKey(Long id);

    int insert(KgRitConvert record);

    int insertSelective(KgRitConvert record);

    KgRitConvert selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(KgRitConvert record);

    int updateByPrimaryKey(KgRitConvert record);
}