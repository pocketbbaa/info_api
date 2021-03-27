package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.Tags;
import com.kg.platform.model.in.TagsInModel;

public interface TagsWMapper {
    int deleteByPrimaryKey(Long tagId);

    int insert(Tags record);

    int insertSelective(TagsInModel inModel);

    int updateByPrimaryKeySelective(Tags record);

    int updateByPrimaryKey(Tags record);

    int UpdateNum(TagsInModel inModel);
}
