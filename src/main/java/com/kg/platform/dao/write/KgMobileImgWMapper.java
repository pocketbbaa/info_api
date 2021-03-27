package com.kg.platform.dao.write;

import com.kg.platform.model.in.KgMobileImgInModel;

public interface KgMobileImgWMapper {

    int deleteByPrimaryKey(Integer imgId);

    int insert(KgMobileImgInModel record);

    int insertSelective(KgMobileImgInModel record);

    int updateByPrimaryKeySelective(KgMobileImgInModel record);

    int updateByPrimaryKey(KgMobileImgInModel record);
}