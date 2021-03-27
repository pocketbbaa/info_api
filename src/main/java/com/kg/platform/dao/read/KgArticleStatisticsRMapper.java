package com.kg.platform.dao.read;

import com.kg.platform.model.in.KgArticleStatisticsInModel;
import com.kg.platform.model.out.KgArticleStatisticsOutModel;

public interface KgArticleStatisticsRMapper {

    KgArticleStatisticsOutModel selectByPrimaryKey(KgArticleStatisticsInModel inModel);

    long getThumbupNum(KgArticleStatisticsInModel inModel);

}