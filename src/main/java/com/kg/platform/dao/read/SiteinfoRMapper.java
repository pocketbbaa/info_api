package com.kg.platform.dao.read;

import com.kg.platform.model.out.SiteinfoOutModel;

public interface SiteinfoRMapper {

    SiteinfoOutModel selectByPrimaryKey(Integer siteId);

    SiteinfoOutModel selectSiteinfo();

}
