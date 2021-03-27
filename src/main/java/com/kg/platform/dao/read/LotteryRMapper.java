package com.kg.platform.dao.read;

import com.kg.platform.model.in.LotteryInModel;
import com.kg.platform.model.out.LotteryOutModel;

public interface LotteryRMapper {

    LotteryOutModel getLottery(LotteryInModel lotteryInModel);

    // 查询已经抽奖次数
    Integer getPrizeCount(LotteryInModel lotteryInModel);

}