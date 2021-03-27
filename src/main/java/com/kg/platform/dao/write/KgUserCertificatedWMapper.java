package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.KgUserCertificated;

public interface KgUserCertificatedWMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(KgUserCertificated record);

    int insertSelective(KgUserCertificated record);

    KgUserCertificated selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(KgUserCertificated record);

    int updateByPrimaryKey(KgUserCertificated record);
}