package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.Siteimage;

public interface SiteimageWMapper {
    int deleteByPrimaryKey(Long imageId);

    int insert(Siteimage record);

    int insertSelective(Siteimage record);

    int updateByPrimaryKeySelective(Siteimage record);

    int updateByPrimaryKey(Siteimage record);
}
