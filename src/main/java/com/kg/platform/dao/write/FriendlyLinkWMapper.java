package com.kg.platform.dao.write;

import com.kg.platform.model.in.FriendlyLinkInModel;

public interface FriendlyLinkWMapper {
    int deleteByPrimaryKey(Integer linkId);

    int insert(FriendlyLinkInModel record);

    int insertSelective(FriendlyLinkInModel record);

    int updateByPrimaryKeySelective(FriendlyLinkInModel record);

    int updateByPrimaryKey(FriendlyLinkInModel record);
}
