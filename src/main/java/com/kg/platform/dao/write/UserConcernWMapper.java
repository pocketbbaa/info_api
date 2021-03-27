package com.kg.platform.dao.write;

import com.kg.platform.model.in.UserConcernInModel;

public interface UserConcernWMapper {
    /**
     * 插入关联数据
     * 
     * @param userConcernInModel
     * @return
     */
    int insertSelective(UserConcernInModel userConcernInModel);

    /**
     * 解除关系
     * 
     * @param userConcernInModel
     * @return
     */
    int deleteUserConcern(UserConcernInModel userConcernInModel);

    /**
     * 更新关注关系
     * 
     * @param userConcernInModel
     * @return
     */
    int updateByPrimaryKeySelective(UserConcernInModel userConcernInModel);

}
