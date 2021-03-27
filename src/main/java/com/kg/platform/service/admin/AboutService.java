package com.kg.platform.service.admin;

import java.util.List;

import com.kg.platform.dao.entity.BaseInfo;
import com.kg.platform.model.response.admin.BaseinfoQueryResponse;

public interface AboutService {

    /**
     * 获得基础信息列表
     * 
     * @return
     */
    List<BaseinfoQueryResponse> getBaseinfoList();

    /**
     * 设置显示状态
     * 
     * @param id
     * @return
     */
    boolean setInfoStatus(Integer id, Integer updateUser);

    /**
     * 删除基础信息
     * 
     * @param id
     * @return
     */
    boolean deleteBaseinfo(Integer id);

    /**
     * 添加关于我们信息
     * 
     * @param baseInfo
     * @return
     */
    boolean addBaseinfo(BaseInfo baseInfo);

    /**
     * 设置排序
     * 
     * @param id
     * @param infoOrder
     * @param updateUser
     * @return
     */
    boolean setInfoOrder(Integer id, Integer infoOrder, Integer updateUser);
}
