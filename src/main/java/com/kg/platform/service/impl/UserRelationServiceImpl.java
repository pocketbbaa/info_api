package com.kg.platform.service.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.dao.entity.UserRelation;
import com.kg.platform.dao.read.UserRelationRMapper;
import com.kg.platform.dao.write.UserRelationWMapper;
import com.kg.platform.model.in.UserRelationInModel;
import com.kg.platform.model.request.UserRelationRequest;
import com.kg.platform.model.request.UserkgRequest;
import com.kg.platform.model.response.InviteUserResponse;
import com.kg.platform.service.UserRelationService;

@Service
@Transactional
public class UserRelationServiceImpl implements UserRelationService {

    private static final Logger logger = LoggerFactory.getLogger(UserRelationServiceImpl.class);

    @Inject
    UserRelationRMapper userRelationRMapper;

    @Inject
    UserRelationWMapper userRelationWMapper;

    @Inject
    protected JedisUtils jedisUtils;

    @Inject
    IDGen idGenerater;

    @Override
    public Long getInviteCount(Long userId) {
        logger.info("获取邀请数入参：{}", JSON.toJSONString(userId));

        return userRelationRMapper.selectInviteCount(userId);
    }

    @Override
    public Boolean bindTeacher(UserRelationRequest request) {
        UserRelation userRel = new UserRelation();
        userRel.setRelId(idGenerater.nextId());
        userRel.setUserId(request.getUserId());
        userRel.setRelUserId(request.getRelUserId());
        userRel.setRelTime(new Date());
        userRel.setBonusStatus(1);
        userRel.setRelType(2);
        return userRelationWMapper.insertSelective(userRel) > 0;
    }

    @Override
    public UserRelation getMyTeacher(Long userId) {
        return userRelationRMapper.selectParentUser(userId);
    }

    @Override
    public List<InviteUserResponse> getJayActivityInviteUsers(UserkgRequest request) {
        UserRelationInModel userRelationInModel = new UserRelationInModel();
        userRelationInModel.setUserId(request.getUserId());
        userRelationInModel.setActivityId(request.getActivityId());
        userRelationInModel.setStartTime(request.getStartTime());
        userRelationInModel.setEndTime(request.getEndTime());
        return userRelationRMapper.getJayActivityInviteUser(userRelationInModel);
    }

}
