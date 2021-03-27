package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.Hotsearch;

public interface HotsearchWMapper {
    int deleteByPrimaryKey(Integer searchwordId);

    int insert(Hotsearch record);

    int insertSelective(Hotsearch record);

    int updateByPrimaryKeySelective(Hotsearch record);

    int updateByPrimaryKey(Hotsearch record);
}
