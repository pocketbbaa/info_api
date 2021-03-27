package com.kg.platform.service.app;

import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.model.request.SiteimageRequest;
import com.kg.platform.model.response.*;
import java.util.List;

/**
 * 广告相关
 * <p>
 * by wangyang
 */
public interface AppAdverService {

    /**
     * 获取启动页面广告
     *
     * @return
     */
    AppJsonEntity getStartPageAdv(SiteimageRequest siteimageAppRequest);

    /**
     * 获取广告列表
     *
     * @return
     */
    List<AdverResponse> getAdvs(SiteimageRequest siteimageAppRequest);


    /**
     * 获取推荐列表广告
     *
     * @return
     */
    List<AdverListResponse> getRecommendAdvs(SiteimageRequest siteimageAppRequest);


    /**
     * 获取个人中心广告
     *
     * @return
     */
   List<PersonalAdvResponse>  getPersonalAdv(SiteimageRequest siteimageAppRequest);


}
