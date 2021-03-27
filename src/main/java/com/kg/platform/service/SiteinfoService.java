package com.kg.platform.service;

import com.kg.platform.common.context.Result;
import com.kg.platform.model.response.SiteinfoResponse;

public interface SiteinfoService {

    /**
     * 查询TDk
     * 
     * @return
     */
    Result<SiteinfoResponse> selectSiteInfo();

}
