package com.kg.platform.dao.write.admin;

import com.kg.platform.dao.entity.AppVersionManage;
import org.apache.ibatis.annotations.Param;

public interface AppVersionManageWMapper {

    /**
     * 创建版本
     *
     * @param appVersionManage
     * @return
     */
    Integer create(AppVersionManage appVersionManage);

    /**
     * 根据ID删除版本
     *
     * @param id
     * @return
     */
    Integer deleteById(@Param("id") Long id);
}
