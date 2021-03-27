package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.BaseInfo;

public interface BaseInfoWMapper {
    int deleteByPrimaryKey(Integer infoId);

    int insert(BaseInfo record);

    int insertSelective(BaseInfo record);

    int updateByPrimaryKeySelective(BaseInfo record);

    int updateByPrimaryKey(BaseInfo record);
}
