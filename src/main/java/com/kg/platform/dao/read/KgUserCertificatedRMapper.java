package com.kg.platform.dao.read;

import com.kg.platform.dao.entity.KgUserCertificated;

public interface KgUserCertificatedRMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(KgUserCertificated record);

    int insertSelective(KgUserCertificated record);

    KgUserCertificated selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(KgUserCertificated record);

    int updateByPrimaryKey(KgUserCertificated record);
}