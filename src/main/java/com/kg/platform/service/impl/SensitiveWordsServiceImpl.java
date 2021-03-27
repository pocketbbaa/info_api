package com.kg.platform.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.utils.JedisKey;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.common.utils.sensitivefilter.SensitiveFilter;
import com.kg.platform.dao.entity.SensitiveWords;
import com.kg.platform.dao.read.SensitiveWordsRMapper;
import com.kg.platform.service.SensitiveWordsService;

@Service
public class SensitiveWordsServiceImpl implements SensitiveWordsService {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveWordsServiceImpl.class);

    @Resource(name = "sensitiveWordsRMapper")
    private SensitiveWordsRMapper sensitiveWordsRMapper;

    @Autowired
    protected JedisUtils jedisUtils;

    @Override
    public Result<SensitiveWords> getOneSensitiveWord(Integer wordId) {

        SensitiveWords oneWord = sensitiveWordsRMapper.selectByPrimaryKey(wordId);

        logger.info(JSON.toJSONString(oneWord));

        return new Result<SensitiveWords>(oneWord);
    }

    @Override
    public Result<List<String>> getAllSensitiveWords() {

        List<String> allWords = jedisUtils.get(JedisKey.sensitiveWordsKey(), List.class, String.class);

        if (CollectionUtils.isEmpty(allWords)) {

            allWords = sensitiveWordsRMapper.selectAllWords();

            this.jedisUtils.set(JedisKey.sensitiveWordsKey(), allWords, JedisKey.ONE_HOUR);
        }
        // logger.info(JSON.toJSONString(allWords));

        return new Result<List<String>>(allWords);
    }

    @Override
    public Result<SensitiveFilter> getSensitiveFilter() {

        SensitiveFilter filter = new SensitiveFilter();

        Result<List<String>> allSensitiveWords = this.getAllSensitiveWords();

        if (allSensitiveWords.isOk() && allSensitiveWords.getData() != null) {
            for (String sw : allSensitiveWords.getData()) {
                filter.put(sw);
            }
        }

        return new Result<SensitiveFilter>(filter);
    }

}
