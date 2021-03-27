package com.kg.platform.dao.write;

import com.kg.platform.model.in.KgActivityGuessInModel;

public interface KgActivityGuessWMapper {

    int deleteByPrimaryKey(Integer guessId);

    int insert(KgActivityGuessInModel record);

    int insertSelective(KgActivityGuessInModel record);

    int updateByPrimaryKeySelective(KgActivityGuessInModel record);

    int updateByPrimaryKey(KgActivityGuessInModel record);
}