package com.kg.platform.dao.read;

import com.kg.platform.dao.entity.UserActive;
import com.kg.platform.model.in.UserActiveInModel;
import com.kg.platform.model.out.UserActiveOutModel;

import java.util.List;
import java.util.Set;

public interface UserActiveRMapper {

    UserActive selectByPrimaryKey(Long userId);

    // 查询审核不通过原因
    UserActiveOutModel selectByUserKey(UserActiveInModel inModel);

    List<UserActive> selectMoreUserIdInfo(List<Long> userIds);

}
