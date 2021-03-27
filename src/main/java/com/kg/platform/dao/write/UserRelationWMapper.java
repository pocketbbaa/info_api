package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.UserRelation;

public interface UserRelationWMapper {
    int deleteByPrimaryKey(Long relId);

    int deleteByUserAndRelUser(UserRelation record);

    int insert(UserRelation record);

    int insertSelective(UserRelation record);

    int updateByPrimaryKeySelective(UserRelation record);

    int updateByPrimaryKey(UserRelation record);

    /**
     * 更新邀新状态
     * 
     * @param record
     * @return
     */
    int updateBonusStatus(UserRelation record);

}
