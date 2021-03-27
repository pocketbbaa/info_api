package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.BalanceDeducted;
import org.apache.ibatis.annotations.Param;

public interface BalanceDeductedWMapper {

    int insert(BalanceDeducted balanceDeducted);

    void updateState(@Param("state") int state, @Param("start") String start, @Param("end") String end);
}
