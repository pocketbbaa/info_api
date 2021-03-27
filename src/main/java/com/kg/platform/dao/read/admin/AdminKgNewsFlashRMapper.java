package com.kg.platform.dao.read.admin;

import com.kg.platform.model.in.KgNewsFlashInModel;
import com.kg.platform.model.out.KgNewsFlashOutModel;

import java.util.List;

public interface AdminKgNewsFlashRMapper {

   List<KgNewsFlashOutModel> selectByCondition(KgNewsFlashInModel inModel);

   long selectCountByCondition(KgNewsFlashInModel inModel);

   KgNewsFlashOutModel detailNewsFlash(Long newsflashId);
}