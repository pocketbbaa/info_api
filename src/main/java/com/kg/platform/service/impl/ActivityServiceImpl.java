package com.kg.platform.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.utils.Constants;
import com.kg.platform.dao.read.LotteryRMapper;
import com.kg.platform.dao.write.LotterWMapper;
import com.kg.platform.model.in.LotteryInModel;
import com.kg.platform.model.in.PrizeInModel;
import com.kg.platform.model.out.LotteryOutModel;
import com.kg.platform.model.request.UserkgRequest;
import com.kg.platform.service.ActivityService;

@Service
public class ActivityServiceImpl implements ActivityService {

    private static final Logger logger = LoggerFactory.getLogger(ActivityServiceImpl.class);

    @Inject
    LotteryRMapper lotteryRMapper;

    @Inject
    LotterWMapper lotteryWMapper;

    @Inject
    IDGen idGenerater;

    @Override
    @Transactional
    public Integer getPrize(UserkgRequest request) {
        List<PrizeInModel> prizes = new ArrayList<PrizeInModel>();

        // 查询二等奖剩余中奖次数
        PrizeInModel p1 = new PrizeInModel();
        p1.setPrizeName("BTC纪念币一枚");
        p1.setPrizeWeight(5);
        p1.setPrizeId(0);

        LotteryInModel lotteryInModel = new LotteryInModel();
        lotteryInModel.setActivityId(1);
        lotteryInModel.setPrizeId(0);
        Integer count0 = lotteryRMapper.getPrizeCount(lotteryInModel);
        p1.setPrizeAmount(Constants.SECONDPRIZE);
        if (count0 != null) {
            p1.setPrizeAmount(Constants.SECONDPRIZE - count0);
        }
        logger.info("查询BTC纪念已中奖次数" + count0);
        if (p1.getPrizeAmount() > 0) {
            prizes.add(p1);
        }

        // 查询四等奖剩余中奖次数
        PrizeInModel p2 = new PrizeInModel();
        p2.setPrizeName("非常谢谢");
        p2.setPrizeWeight(80);
        p2.setPrizeId(1);
        prizes.add(p2);

        // 查询三等奖剩余中奖次数
        PrizeInModel p3 = new PrizeInModel();
        p3.setPrizeName("手机支架");
        p3.setPrizeWeight(15);
        p3.setPrizeId(2);

        lotteryInModel = new LotteryInModel();
        lotteryInModel.setActivityId(1);
        lotteryInModel.setPrizeId(2);
        Integer count1 = lotteryRMapper.getPrizeCount(lotteryInModel);
        logger.info("查询三等奖已中奖次数" + count1);
        p3.setPrizeAmount(Constants.THIRDPRIZE);
        if (count1 != null) {
            p3.setPrizeAmount(Constants.THIRDPRIZE - count1);
        }
        if (p3.getPrizeAmount() > 0) {
            prizes.add(p3);
        }
        int selected = this.getPrizeIndex(prizes);
        // 如果数量不足则继续抽取

        PrizeInModel prize = prizes.get(selected);

        logger.info("中奖奖品>>>>>>>>" + selected + ":" + prize.getPrizeName());

        lotteryInModel = new LotteryInModel();
        lotteryInModel.setLotteryId(idGenerater.nextId());
        lotteryInModel.setPrizeId(prize.getPrizeId());
        lotteryInModel.setUserId(request.getUserId());
        lotteryInModel.setActivityId(1);
        lotteryInModel.setLotteryStatus(1);
        if (lotteryInModel.getPrizeId() == 1) {
            lotteryInModel.setLotteryStatus(0);
        }
        lotteryInModel.setCreateTime(new Date());
        lotteryWMapper.addPrizeRecord(lotteryInModel);
        return selected;
    }

    /**
     * 根据Math.random()产生一个double型的随机数，判断每个奖品出现的概率
     * 
     * @param prizes
     * @return random：奖品列表prizes中的序列（prizes中的第random个就是抽中的奖品）
     */
    @Override
    public int getPrizeIndex(List<PrizeInModel> prizes) {
        DecimalFormat df = new DecimalFormat("######0.00");
        int random = -1;
        try {
            // 计算总权重
            double sumWeight = 0;
            for (PrizeInModel p : prizes) {
                sumWeight += p.getPrizeWeight();
            }
            // 产生随机数
            double randomNumber;
            randomNumber = Math.random();

            // 根据随机数在所有奖品分布的区域并确定所抽奖品
            double d1 = 0;
            double d2 = 0;
            for (int i = 0; i < prizes.size(); i++) {
                d2 += Double.parseDouble(String.valueOf(prizes.get(i).getPrizeWeight())) / sumWeight;
                if (i == 0) {
                    d1 = 0;
                } else {
                    d1 += Double.parseDouble(String.valueOf(prizes.get(i - 1).getPrizeWeight())) / sumWeight;
                }
                if (randomNumber >= d1 && randomNumber <= d2) {
                    random = i;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("生成抽奖随机数出错，出错原因：" + e.getMessage());
        }
        return random;
    }

    @Override
    public LotteryOutModel getLotteryOut(LotteryInModel lotteryInModel) {
        return lotteryRMapper.getLottery(lotteryInModel);
    }

}
