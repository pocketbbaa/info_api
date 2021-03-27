package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.AccountFlow;
import com.kg.platform.model.in.AccountFlowInModel;

public interface AccountFlowWMapper {
    int deleteByPrimaryKey(Long accountFlowId);

    int insert(AccountFlow record);

    int insertSelective(AccountFlow record);

    int updateByPrimaryKeySelective(AccountFlow record);

    int updateByPrimaryKey(AccountFlow record);

    int insertFlowSelective(AccountFlowInModel inModel);
}
