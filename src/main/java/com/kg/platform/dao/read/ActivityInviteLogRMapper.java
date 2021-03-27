package com.kg.platform.dao.read;

import java.util.List;

import com.kg.platform.model.out.ActivityInviteLogOutModel;

public interface ActivityInviteLogRMapper {

    /**
     * 检查我是否有资格参加世界杯活动
     * 
     * @param userId
     * @return
     */
    List<ActivityInviteLogOutModel> checkJoinWordCup(Long userId);

}
