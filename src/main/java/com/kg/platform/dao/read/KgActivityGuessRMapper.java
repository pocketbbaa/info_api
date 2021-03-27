package com.kg.platform.dao.read;

import com.kg.platform.model.in.KgActivityGuessInModel;
import com.kg.platform.model.out.KgActivityGuessOutModel;

public interface KgActivityGuessRMapper {

    KgActivityGuessInModel selectByPrimaryKey(Integer guessId);

    /**
     * 检查是否竞猜过
     * 
     * @param guessId
     * @return
     */
    KgActivityGuessOutModel checkGuess(KgActivityGuessInModel kgActivityGuessInModel);
}