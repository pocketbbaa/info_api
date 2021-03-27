package com.kg.platform.service.app;

import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.model.request.admin.AppVersionManageRequest;

import javax.servlet.http.HttpServletRequest;

public interface AppVersionService {

    /**
     * 获取最新版本信息
     *
     * @param request
     * @return
     */
    AppJsonEntity getLastVersion(AppVersionManageRequest request);

    /**
     * 根据版本号获取版本
     *
     * @param request
     * @return
     */
    AppJsonEntity getVersion(AppVersionManageRequest request);
}
