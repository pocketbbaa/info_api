package com.kg.platform.dao.write;

import com.kg.platform.model.in.LotteryInModel;

public interface LotterWMapper {

    /**
     * 添加中奖记录
     * 
     * @param record
     * @return
     */
    int addPrizeRecord(LotteryInModel lotteryInModel);
}
