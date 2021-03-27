package com.kg.platform.dao.read;

import com.kg.platform.dao.entity.VoteUser;

public interface VoteUserRMapper {

    VoteUser selectByPrimaryKey(Long voteUserId);

}
