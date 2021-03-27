package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.SensitiveWords;

public interface SensitiveWordsWMapper {
    int deleteByPrimaryKey(Integer wordId);

    int insert(SensitiveWords record);

    int insertSelective(SensitiveWords record);

    int updateByPrimaryKeySelective(SensitiveWords record);

    int updateByPrimaryKey(SensitiveWords record);
}