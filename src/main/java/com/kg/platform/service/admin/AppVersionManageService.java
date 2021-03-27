package com.kg.platform.service.admin;

import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.request.admin.AppVersionManageRequest;
import com.kg.platform.model.response.admin.AppVersionManageResponse;

public interface AppVersionManageService {

    /**
     * 创建版本
     *
     * @param request
     * @return
     */
    boolean create(AppVersionManageRequest request);

    /**
     * 根据ID删除版本
     *
     * @param request
     * @return
     */
    boolean deleteById(AppVersionManageRequest request);

    /**
     * 根据ID查询详情
     *
     * @param request
     * @return
     */
    AppVersionManageResponse getDetailById(AppVersionManageRequest request);

    /**
     * 分页获取版本列表
     *
     * @param request
     * @param page
     * @return
     */
    PageModel<AppVersionManageResponse> getList(AppVersionManageRequest request, PageModel<AppVersionManageResponse> page);

    /**
     * 根据条件获取版本信息
     *
     * @param request
     * @return
     */
    AppVersionManageResponse getByVersionAndSysType(AppVersionManageRequest request);
}
