package com.kg.platform.dao.read;

import com.kg.platform.dao.entity.KgRitConvert;
import org.apache.ibatis.annotations.Param;

public interface AppKgRitConvertRMapper {

    KgRitConvert getSet();

    KgRitConvert getSetByUserType(@Param("userType") Integer userType);
}