package com.kg.platform.dao.write;

import com.kg.platform.model.in.KgSeoTdkInModel;


public interface KgSeoTdkWMapper {

    int deleteByPrimaryKey(Integer tdkId);

    int insert(KgSeoTdkInModel record);

    int insertSelective(KgSeoTdkInModel record);

    int updateByPrimaryKeySelective(KgSeoTdkInModel record);

    int updateByPrimaryKey(KgSeoTdkInModel record);

    int editTdk(KgSeoTdkInModel record);
}