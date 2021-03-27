package com.kg.platform.service;

import java.util.List;

import com.kg.platform.dao.entity.UserRelation;
import com.kg.platform.model.request.UserRelationRequest;
import com.kg.platform.model.request.UserkgRequest;
import com.kg.platform.model.response.InviteUserResponse;

public interface UserRelationService {

    Long getInviteCount(Long userId);

    /**
     * 绑定师傅
     * 
     * @param request
     * @return
     */
    Boolean bindTeacher(UserRelationRequest request);

    /**
     * 查询我的师傅信息
     * 
     * @param request
     * @return
     */
    UserRelation getMyTeacher(Long userId);

    /**
     * 查询10条周杰伦活动邀请用户列表
     * 
     * @param request
     * @return
     */
    List<InviteUserResponse> getJayActivityInviteUsers(UserkgRequest request);

}
