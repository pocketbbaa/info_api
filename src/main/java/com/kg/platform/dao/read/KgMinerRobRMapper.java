package com.kg.platform.dao.read;

import java.util.List;

import com.kg.platform.model.in.KgMinerRobInModel;
import com.kg.platform.model.out.KgMinerRobOutModel;

public interface KgMinerRobRMapper {

    KgMinerRobOutModel selectByPrimaryKey(Long robId);

    long countRobedByMinerId(KgMinerRobInModel inModel);

    int checkCode(String code);

    List<KgMinerRobOutModel> myRobList(KgMinerRobInModel inModel);

    KgMinerRobOutModel selectByCode(String code);

    KgMinerRobOutModel detailRob(KgMinerRobInModel inModel);

    List<KgMinerRobOutModel> topTenList(KgMinerRobInModel inModel);

    /**
     * 查询是否已经抢购过该矿机
     * 
     * @param inModel
     * @return
     */
    KgMinerRobOutModel checkIsRob(KgMinerRobInModel inModel);
}