package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.VoteUser;

public interface VoteUserWMapper {
    int deleteByPrimaryKey(Long voteUserId);

    int insert(VoteUser record);

    int insertSelective(VoteUser record);

    int updateByPrimaryKeySelective(VoteUser record);

    int updateByPrimaryKey(VoteUser record);

}
