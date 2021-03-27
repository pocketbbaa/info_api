package com.kg.platform.service.admin;

import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.dao.entity.SysUser;
import com.kg.platform.model.in.admin.RitSettingModel;
import com.kg.platform.model.response.UserkgResponse;

public interface RitSettingService {

    /**
     * 设置rit兑换参数
     * @param ritSettingModel
     * @return
     */
    JsonEntity updateRitConvertSetting(SysUser sysUser, RitSettingModel ritSettingModel);

    /**
     * 获取rit兑换参数
     */
    JsonEntity getRitConvertProfile(SysUser sysUser, RitSettingModel ritSettingModel);

    /**
     * rit参数设置日志列表
     */
    JsonEntity getRitSettingLogList(UserkgResponse kguser);

    /**
     * 设置RIT提现参数
     */
    JsonEntity updateRolloutProfileService(SysUser sysUser, RitSettingModel request);

    /**
     * 获取RIT提现参数接口
     */
    JsonEntity getRitRolloutProfileService(SysUser sysUser, RitSettingModel request);
}
