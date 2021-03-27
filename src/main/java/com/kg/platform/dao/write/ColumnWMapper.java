package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.Column;

public interface ColumnWMapper {
    int deleteByPrimaryKey(Integer columnId);

    int insert(Column record);

    int insertSelective(Column record);

    int updateByPrimaryKeySelective(Column record);

    int updateByPrimaryKey(Column record);
}
