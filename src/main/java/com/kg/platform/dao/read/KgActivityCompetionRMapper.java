package com.kg.platform.dao.read;

import com.kg.platform.model.in.KgActivityCompetionInModel;
import com.kg.platform.model.out.KgActivityCompetionOutModel;

import java.util.List;

public interface KgActivityCompetionRMapper {

    KgActivityCompetionInModel selectByPrimaryKey(Integer competionId);

    int updateByPrimaryKeySelective(KgActivityCompetionInModel record);

    int updateByPrimaryKey(KgActivityCompetionInModel record);

    /**
     * 查询当天范围内是否有比赛
     *
     * @param record
     * @return
     */
    int checkTodayMatch();

    List<KgActivityCompetionOutModel> worldCupCompetionList(KgActivityCompetionInModel inModel);
}