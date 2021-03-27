package com.kg.platform.dao.read.admin;

import java.util.List;

import com.kg.platform.dao.entity.Siteimage;
import com.kg.platform.model.request.admin.AdminSiteimageQueryRequest;

public interface AdminSiteimageRMapper {

    Siteimage selectByPrimaryKey(Integer imageId);

    List<Siteimage> selectImagesByParam(AdminSiteimageQueryRequest bParam);

    Integer countImagesByParam(AdminSiteimageQueryRequest bParam);

}
