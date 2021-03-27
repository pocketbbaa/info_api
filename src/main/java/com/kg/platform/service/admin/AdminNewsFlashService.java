package com.kg.platform.service.admin;

import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.request.KgNewsFlashRequest;
import com.kg.platform.model.response.KgNewsFlashResponse;

/**
 * Created by Administrator on 2018/5/31.
 */
public interface AdminNewsFlashService {

    /**
     * （快讯管理）快讯搜索
     * @param request
     * @param page
     * @return
     */
    Result<PageModel<KgNewsFlashResponse>>  getNewsFlashListByCondition(KgNewsFlashRequest request, PageModel<KgNewsFlashResponse> page);

    /**
     * 快讯详情
     * @param request
     * @return
     */
    Result<KgNewsFlashResponse> detailNewsFlash(KgNewsFlashRequest request);

    /**
     * 新增快讯
     * @param request
     * @return
     */
    JsonEntity addNewsFlash(KgNewsFlashRequest request);

    /**
     * 删除快讯
     * @param request
     * @return
     */
    JsonEntity delNewsFlash(KgNewsFlashRequest request);

    /**
     * 修改快讯
     * @param request
     * @return
     */
    JsonEntity updateNewsFlash(KgNewsFlashRequest request);

    /**
     * 获取快讯顶部栏目导航列表
     * @return
     */
    JsonEntity getNewsFlashTopMenus();

    /**
     * 获取当日快讯推送上限以及当日快讯推送数量
     * @return
     */
    JsonEntity getPushAticleInfo();

}
