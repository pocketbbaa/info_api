package com.kg.platform.dao.read;

import com.kg.platform.model.in.KgMobileImgInModel;
import com.kg.platform.model.out.KgMobileImgOutModel;

import java.util.List;

public interface KgMobileImgRMapper {

    KgMobileImgInModel selectByPrimaryKey(Integer imgId);

    List<KgMobileImgOutModel> selectTabs();
}