package com.kg.platform.service;

import java.util.List;

import com.kg.platform.model.in.LotteryInModel;
import com.kg.platform.model.in.PrizeInModel;
import com.kg.platform.model.out.LotteryOutModel;
import com.kg.platform.model.request.UserkgRequest;

public interface ActivityService {

    Integer getPrize(UserkgRequest request);

    int getPrizeIndex(List<PrizeInModel> prizes);

    LotteryOutModel getLotteryOut(LotteryInModel lotteryInModel);

}
