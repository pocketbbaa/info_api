package com.kg.platform.service;

import java.util.List;

import com.kg.platform.common.context.Result;
import com.kg.platform.dao.entity.Siteimage;
import com.kg.platform.model.request.SiteimageRequest;
import com.kg.platform.model.response.SiteimageResponse;

public interface SiteimageinService {

    Result<List<SiteimageResponse>> getAllColumn(SiteimageRequest request);

    Result<Siteimage> selectByPrimaryKey(Integer columnId);

}
