package com.kg.platform.dao.read;

import java.util.List;

import com.kg.platform.dao.entity.Vote;
import com.kg.platform.model.in.VoteInModel;
import com.kg.platform.model.response.VoteResponse;

public interface VoteRMapper {

    Vote selectByPrimaryKey(Long voteId);

    /**
     * 投票列表
     * 
     * @return
     */
    List<VoteResponse> getVoteList(VoteInModel voteInModel);

    /**
     * 投票
     * 
     * @return
     */
    Integer vote();

    /**
     * 查询投票数量
     * 
     * @return
     */
    Long getVoteNum(VoteInModel voteInModel);

    /**
     * 查询被投票人是否存在
     * 
     * @return
     */
    VoteResponse checkVoteExsits(VoteInModel voteInModel);

}
