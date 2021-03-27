package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.Siteinfo;

public interface SiteinfoWMapper {
    int deleteByPrimaryKey(Integer siteId);

    int insert(Siteinfo record);

    int insertSelective(Siteinfo record);

    int updateByPrimaryKeySelective(Siteinfo record);

    int updateByPrimaryKey(Siteinfo record);
}
