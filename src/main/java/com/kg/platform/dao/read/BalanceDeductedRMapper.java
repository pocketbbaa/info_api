package com.kg.platform.dao.read;

import com.kg.platform.dao.entity.BalanceDeducted;
import org.apache.ibatis.annotations.Param;

public interface BalanceDeductedRMapper {

    BalanceDeducted getBalanceByTime(@Param("start") String start, @Param("end") String end);

}
