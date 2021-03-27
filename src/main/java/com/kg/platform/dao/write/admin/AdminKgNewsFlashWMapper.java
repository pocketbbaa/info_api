package com.kg.platform.dao.write.admin;

import com.kg.platform.model.in.KgNewsFlashInModel;

public interface AdminKgNewsFlashWMapper {

    int deleteByPrimaryKey(Long newsflashId);

    int insert(KgNewsFlashInModel record);

    int insertSelective(KgNewsFlashInModel record);

    KgNewsFlashInModel selectByPrimaryKey(Long newsflashId);

    int updateByPrimaryKeySelective(KgNewsFlashInModel record);

    int updateByPrimaryKeyWithBLOBs(KgNewsFlashInModel record);

    int updateByPrimaryKey(KgNewsFlashInModel record);
}