package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.UserLog;

public interface UserLogWMapper {
    int deleteByPrimaryKey(Long logId);

    int insert(UserLog record);

    int insertSelective(UserLog record);

    int updateByPrimaryKeySelective(UserLog record);

    int updateByPrimaryKey(UserLog record);
}
