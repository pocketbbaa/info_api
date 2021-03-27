package com.kg.platform.dao.read;

import com.kg.platform.dao.entity.UserLog;

public interface UserLogRMapper {

    UserLog selectByPrimaryKey(Long logId);

}
