package com.kg.platform.service.admin;

import com.kg.platform.common.entity.PageModel;
import com.kg.platform.dao.entity.Siteimage;
import com.kg.platform.model.request.admin.AdminSiteimageQueryRequest;
import com.kg.platform.model.request.admin.SiteimageEditRequest;

public interface AdminSiteimageService {
    /**
     * 获取图片、广告列表
     *
     * @param request
     * @return
     */
    PageModel<Siteimage> listImage(AdminSiteimageQueryRequest request);

    /**
     * 添加图片
     *
     * @param request
     * @return
     */
    boolean addImage(SiteimageEditRequest request);

    /**
     * 添加广告
     *
     * @param request
     * @return
     */
    boolean addAdvertise(SiteimageEditRequest request);

    /**
     * 根据广告ID获取详情
     *
     * @param request
     * @return
     */
    Siteimage getAdvertiseById(AdminSiteimageQueryRequest request);

    /**
     * 删除图片
     * 
     * @param request
     * @return
     */
    boolean deleteImage(SiteimageEditRequest request);

    /**
     * 设置图片显示状态
     * 
     * @param idList
     * @param userId
     * @return
     */
    boolean setStatus(SiteimageEditRequest request);

}
