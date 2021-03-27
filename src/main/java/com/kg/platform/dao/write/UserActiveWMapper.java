package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.UserActive;
import com.kg.platform.model.in.UserActiveInModel;

public interface UserActiveWMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(UserActive record);

    int insertSelective(UserActive record);

    int updateByPrimaryKeySelective(UserActive record);

    int updateByPrimaryKey(UserActive record);

    int insertUserSelective(UserActiveInModel inModel);

    int updateUserSelective(UserActiveInModel inModel);
}
