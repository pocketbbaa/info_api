package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.Keywords;

public interface KeywordsWMapper {
    int deleteByPrimaryKey(Integer keywordId);

    int insert(Keywords record);

    int insertSelective(Keywords record);

    int updateByPrimaryKeySelective(Keywords record);

    int updateByPrimaryKey(Keywords record);
}
