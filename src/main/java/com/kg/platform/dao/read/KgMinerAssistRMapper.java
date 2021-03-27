package com.kg.platform.dao.read;

import java.util.List;

import com.kg.platform.model.in.KgMinerAssistInModel;
import com.kg.platform.model.out.KgMinerAssistOutModel;

public interface KgMinerAssistRMapper {

    KgMinerAssistOutModel selectByPrimaryKey(Long assistId);

    List<KgMinerAssistOutModel> assistList(KgMinerAssistInModel inModel);

    /**
     * 统计已蓄力人数和已蓄力金额
     */
    KgMinerAssistOutModel getTotalAssistInfo(KgMinerAssistInModel inModel);

    /**
     * 统计用户当天已经助力的抢单的次数
     */
    int checkRobMinnerTimes(KgMinerAssistInModel inModel);

    /**
     * 验证用户当天是否已经助力过该抢单
     */
    int checkUserAssitStatus(KgMinerAssistInModel inModel);

    /**
     * 验证用户是否是第一次助力
     */
    int checkUserFirstAssit(KgMinerAssistInModel inModel);

    /**
     * 查询已助力人数
     */
    int getTotalAssist(KgMinerAssistInModel inModel);

    long countAssistList(KgMinerAssistInModel inModel);

}