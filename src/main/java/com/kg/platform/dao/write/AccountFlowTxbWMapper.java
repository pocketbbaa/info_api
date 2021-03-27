package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.AccountFlowTxb;

public interface AccountFlowTxbWMapper {
    int deleteByPrimaryKey(Long accountFlowId);

    int insert(AccountFlowTxb record);

    int insertSelective(AccountFlowTxb record);

    int updateByPrimaryKeySelective(AccountFlowTxb record);

    int updateByPrimaryKey(AccountFlowTxb record);
}
