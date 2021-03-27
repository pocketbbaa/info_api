package com.kg.platform.service;

import com.kg.platform.model.request.KgArticleStatisticsRequest;

public interface KgArticleStatisticsService {

    boolean updateByPrimaryKeySelective(KgArticleStatisticsRequest request);

}
