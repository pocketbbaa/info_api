package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.Vote;
import com.kg.platform.model.in.VoteInModel;

public interface VoteWMapper {
    int deleteByPrimaryKey(Long voteId);

    int insert(Vote record);

    int insertSelective(Vote record);

    int updateByPrimaryKeySelective(Vote record);

    int updateByPrimaryKey(Vote record);

    /**
     * 更新投票
     * 
     * @param record
     * @return
     */
    int addVoteNum(VoteInModel voteInModel);
}
