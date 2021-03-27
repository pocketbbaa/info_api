package com.kg.platform.dao.read;

import com.kg.platform.model.in.KgNewsFlashInModel;
import com.kg.platform.model.out.KgNewsFlashOutModel;

import java.util.List;

public interface KgNewsFlashRMapper {

    int deleteByPrimaryKey(Long newsflashId);

    int insert(KgNewsFlashInModel record);

    int insertSelective(KgNewsFlashInModel record);

    KgNewsFlashInModel selectByPrimaryKey(Long newsflashId);

    int updateByPrimaryKeySelective(KgNewsFlashInModel record);

    int updateByPrimaryKeyWithBLOBs(KgNewsFlashInModel record);

    int updateByPrimaryKey(KgNewsFlashInModel record);

    List<KgNewsFlashOutModel> selectByType(KgNewsFlashInModel inModel);

    long selectCountNewsFlash(KgNewsFlashInModel inModel);

    List<KgNewsFlashOutModel> selectIndexFlash(KgNewsFlashInModel inModel);

    KgNewsFlashOutModel selectNewsFlashDetail(Long newsflashId);

    long selectCountUnread(KgNewsFlashInModel inModel);

    List<KgNewsFlashOutModel> getThreeNewNewsFlash();
}