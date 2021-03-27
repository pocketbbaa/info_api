package com.kg.platform.service;

import java.util.List;

import com.kg.platform.model.in.VoteInModel;
import com.kg.platform.model.response.VoteResponse;

public interface VoteService {

    /**
     * 投票
     * 
     * @param voted
     * @return
     */
    Boolean vote(VoteInModel voteInModel);

    /**
     * 投票列表
     * 
     * @param voted
     * @return
     */
    List<VoteResponse> getVoteList(VoteInModel voteInModel);

    /**
     * 查询凌晨4点到第二天凌晨4点投票数是否超过1万
     * 
     * @param voted
     * @return
     */
    Boolean checkVoteNum(VoteInModel voteInModel);

    /**
     * 验证投票的公司是否存在
     * 
     * @param voted
     * @return
     */
    VoteResponse checkVoteDataExsits(VoteInModel voteInModel);

}
