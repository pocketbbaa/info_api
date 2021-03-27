package com.kg.platform.dao.read;

import com.kg.platform.model.in.KgCommonSettingInModel;
import com.kg.platform.model.out.KgCommonSettingOutModel;

public interface KgCommonSettingRMapper {

    KgCommonSettingInModel selectByPrimaryKey(Integer settingId);

    KgCommonSettingOutModel selectBySettingKey(KgCommonSettingInModel inModel);

}