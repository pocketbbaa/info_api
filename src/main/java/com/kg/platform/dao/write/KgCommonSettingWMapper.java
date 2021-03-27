package com.kg.platform.dao.write;

import com.kg.platform.model.in.KgCommonSettingInModel;

public interface KgCommonSettingWMapper {


    int deleteByPrimaryKey(Integer settingId);

    int insert(KgCommonSettingInModel record);

    int insertSelective(KgCommonSettingInModel record);

    KgCommonSettingInModel selectByPrimaryKey(Integer settingId);

    int updateByPrimaryKeySelective(KgCommonSettingInModel record);

    int updateByPrimaryKey(KgCommonSettingInModel record);

    int upateBySettingKey(KgCommonSettingInModel record);

}