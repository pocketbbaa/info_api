package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.WithdrawBonusFlow;

public interface WithdrawBonusFlowWMapper {
    int deleteByPrimaryKey(Long withdrawFlowId);

    int insert(WithdrawBonusFlow record);

    int insertSelective(WithdrawBonusFlow record);

    int updateByPrimaryKeySelective(WithdrawBonusFlow record);

    int updateByPrimaryKey(WithdrawBonusFlow record);
}
