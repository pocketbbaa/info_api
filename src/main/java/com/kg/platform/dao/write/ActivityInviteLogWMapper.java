package com.kg.platform.dao.write;

import com.kg.platform.model.in.ActivityInviteLogInModel;

public interface ActivityInviteLogWMapper {

    int insertSelective(ActivityInviteLogInModel activityInviteLogInModel);

    int updateByPrimaryKeySelective(ActivityInviteLogInModel activityInviteLogInModel);

    /**
     * 更新所有邀新纪录
     * 
     * @param record
     * @return
     */
    int updateWordcupInviteStatus(ActivityInviteLogInModel activityInviteLogInModel);

}
