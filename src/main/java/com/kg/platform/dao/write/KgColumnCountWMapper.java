package com.kg.platform.dao.write;

import com.kg.platform.model.in.KgColumnCountInModel;

import java.util.List;

public interface KgColumnCountWMapper {

    int deleteByPrimaryKey(String columnKey);

    int insert(KgColumnCountInModel record);

    int insertSelective(KgColumnCountInModel record);

    int updateByPrimaryKeySelective(KgColumnCountInModel record);

    int updateByPrimaryKey(KgColumnCountInModel record);

    int deleteAll();

    int insertList(List<KgColumnCountInModel> list);
}