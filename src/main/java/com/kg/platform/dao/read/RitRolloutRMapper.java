package com.kg.platform.dao.read;

import com.kg.platform.model.out.KgRitRolloutOutModel;

public interface RitRolloutRMapper {

    KgRitRolloutOutModel selectByPrimaryKey(Long id);

    KgRitRolloutOutModel selectByUserType(Integer userType);
}