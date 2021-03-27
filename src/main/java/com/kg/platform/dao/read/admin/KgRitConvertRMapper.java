package com.kg.platform.dao.read.admin;

import com.kg.platform.dao.entity.KgRitConvert;
import java.util.List;

public interface KgRitConvertRMapper {
    int deleteByPrimaryKey(Long id);

    int insert(KgRitConvert record);

    int insertSelective(KgRitConvert record);

    KgRitConvert selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(KgRitConvert record);

    int updateByPrimaryKey(KgRitConvert record);

    KgRitConvert selectByUserType(Integer userType);

    List<KgRitConvert> selectAll();
}