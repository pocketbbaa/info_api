package com.kg.platform.dao.read.admin;

import com.kg.platform.dao.entity.KgRitRollout;
import org.apache.ibatis.annotations.Param;

public interface KgRitRolloutRMapper {
    int deleteByPrimaryKey(Long id);

    int insert(KgRitRollout record);

    int insertSelective(KgRitRollout record);

    KgRitRollout selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(KgRitRollout record);

    int updateByPrimaryKey(KgRitRollout record);

    KgRitRollout selectByPrimaryUserType(@Param("userType") Integer userType);
}