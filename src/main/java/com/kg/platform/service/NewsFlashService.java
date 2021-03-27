package com.kg.platform.service;

import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.request.KgNewsFlashRequest;
import com.kg.platform.model.response.KgNewsFlashResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/13.
 */
public interface NewsFlashService {
    /**
     * 根据类型获取24H快讯
     * @return
     */
    Map<String,Object> getNewsFlashListByType(KgNewsFlashRequest request, PageModel<KgNewsFlashResponse> page, HttpServletRequest servletRequest);

    /**
     * 首页24H快讯
     * @return
     */
    Map<String,Object> getNewsFlashList();

    /**
     * 24H快讯详情
     * @param request
     * @return
     */
    Result<KgNewsFlashResponse> getNewsFlashDetail(KgNewsFlashRequest request);

    /**
     * 24H快讯未读数量
     * @param request
     * @return
     */
    Result<Long> getNumberUnread(KgNewsFlashRequest request);

    /**
     * 获取快讯顶部栏目导航列表
     * @return
     */
    AppJsonEntity getNewsFlashTopMenus();

    /**
     * 用于前端第一次建立会话等待推送数据的空档期 直接调用接口去拿数据
     * @return
     */
    Result<List<KgNewsFlashResponse>> websocketNewsFlash();
}
