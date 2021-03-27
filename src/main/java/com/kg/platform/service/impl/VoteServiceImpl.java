package com.kg.platform.service.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.utils.Constants;
import com.kg.platform.dao.entity.VoteUser;
import com.kg.platform.dao.read.VoteRMapper;
import com.kg.platform.dao.read.VoteUserRMapper;
import com.kg.platform.dao.write.VoteUserWMapper;
import com.kg.platform.dao.write.VoteWMapper;
import com.kg.platform.model.in.VoteInModel;
import com.kg.platform.model.response.VoteResponse;
import com.kg.platform.service.VoteService;

@Service
@Transactional
public class VoteServiceImpl implements VoteService {

    @Inject
    VoteUserRMapper voteUserRMapper;

    @Inject
    VoteUserWMapper voteUserWMapper;

    @Inject
    VoteWMapper voteWMapper;

    @Inject
    IDGen idGenerater;

    @Inject
    VoteRMapper voteRMapper;

    @Override
    public Boolean vote(VoteInModel voteInModel) {
        Boolean sucess = true;
        // 插入用户投票
        VoteUser voteUser = new VoteUser();
        voteUser.setVoteUserId(idGenerater.nextId());
        voteUser.setVoteId(voteInModel.getVoteId());
        voteUser.setVoterId(voteInModel.getUserId());
        voteUser.setVoteNum(voteInModel.getVoteNum());
        voteUser.setVoteTime(new Date());
        sucess = voteUserWMapper.insertSelective(voteUser) > 0;

        // 更新投票榜投票
        sucess = voteWMapper.addVoteNum(voteInModel) > 0;
        return sucess;
    }

    @Override
    public List<VoteResponse> getVoteList(VoteInModel voteInModel) {
        return voteRMapper.getVoteList(voteInModel);
    }

    @Override
    public Boolean checkVoteNum(VoteInModel voteInModel) {
        Long num = voteRMapper.getVoteNum(voteInModel);
        if (num == null
                || (num.longValue() + Long.valueOf(voteInModel.getVoteNum())) <= Constants.MAXVOTENUM.longValue()) {
            return true;
        }
        return false;
    }

    @Override
    public VoteResponse checkVoteDataExsits(VoteInModel voteInModel) {
        return voteRMapper.checkVoteExsits(voteInModel);
    }
}
