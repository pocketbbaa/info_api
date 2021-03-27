package com.kg.platform.dao.read;

import com.kg.platform.dao.entity.WithdrawBonusFlow;
import com.kg.platform.model.in.WithdrawBonusInModel;
import com.kg.platform.model.out.WithdrawBonusOutModel;

public interface WithdrawBonusFlowRMapper {

    WithdrawBonusFlow selectByPrimaryKey(Long withdrawFlowId);
    
    /**
     * 查询是否申请过邀请奖励提现
     * @param withdrawFlowId
     * @return
     */
    WithdrawBonusOutModel getauditAmount(WithdrawBonusInModel withdrawBonusInModel);
    
    
    
    

}
