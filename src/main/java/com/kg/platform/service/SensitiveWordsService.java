package com.kg.platform.service;

import java.util.List;

import com.kg.platform.common.context.Result;
import com.kg.platform.common.utils.sensitivefilter.SensitiveFilter;
import com.kg.platform.dao.entity.SensitiveWords;

public interface SensitiveWordsService {

    Result<List<String>> getAllSensitiveWords();

    Result<SensitiveWords> getOneSensitiveWord(Integer wordId);

    Result<SensitiveFilter> getSensitiveFilter();

}
