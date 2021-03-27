package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.AccountFlowRit;

public interface AccountFlowRitWMapper {
    int deleteByPrimaryKey(Long accountFlowId);

    int insert(AccountFlowRit record);

    int insertSelective(AccountFlowRit record);

    int updateByPrimaryKeySelective(AccountFlowRit record);

    int updateByPrimaryKey(AccountFlowRit record);
}
