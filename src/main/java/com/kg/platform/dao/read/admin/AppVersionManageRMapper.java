package com.kg.platform.dao.read.admin;

import com.kg.platform.dao.entity.AppVersionManage;
import com.kg.platform.model.request.admin.AppVersionManageRequest;
import com.kg.platform.model.response.admin.AppVersionManageResponse;
import org.apache.ibatis.annotations.Param;
import org.junit.Test;

import java.util.List;

public interface AppVersionManageRMapper {

    /**
     * 根据ID获取版本信息
     *
     * @param id
     * @return
     */
    AppVersionManage getById(@Param("id") Long id);

    /**
     * 分页获取版本列表
     *
     * @param request
     * @return
     */
    List<AppVersionManageResponse> getList(AppVersionManageRequest request);

    /**
     * 获取总数
     *
     * @return
     */
    Long getTotalCount();

    /**
     * 获取最新版本信息
     *
     * @param request
     * @return
     */
    AppVersionManage getLastVersion(AppVersionManageRequest request);

    /**
     * 根据版本号获取版本
     *
     * @param request
     * @return
     */
    AppVersionManage getVersion(AppVersionManageRequest request);

    /**
     * 查看两个版本ID之前是否有强制更新
     *
     * @param idUser
     * @param idNew
     * @return
     */
    Integer getForcedVersion(@Param("idUser") Long idUser, @Param("idNew") Long idNew, @Param("systemType") Integer systemType, @Param("channel") String channel);

    /**
     * 根据版本号和系统获取版本信息
     *
     * @param versionNum
     * @param systemType
     * @return
     */
    AppVersionManage getByVersionAndSysType(@Param("versionNum") String versionNum, @Param("systemType") Integer systemType, @Param("channel") String channel);

    /**
     * 获取安卓最后更新版本
     *
     * @param request
     * @return
     */
    AppVersionManage getLastVersionWithChannel(AppVersionManageRequest request);
}
