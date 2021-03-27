package com.kg.platform.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.kg.platform.common.aop.Cacheable;
import com.kg.platform.dao.read.HotsearchRMapper;
import com.kg.platform.enumeration.DateEnum;
import com.kg.platform.model.out.HotsearchOutModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.context.Result;
import com.kg.platform.dao.read.KeywordsRMapper;
import com.kg.platform.model.in.KeywordsInModel;
import com.kg.platform.model.out.KeywordsOutModel;
import com.kg.platform.model.request.KeywordsRequest;
import com.kg.platform.model.response.KeywordsResponse;
import com.kg.platform.service.KeywordsService;

@Service
public class KeywordsServiceImpl implements KeywordsService {
    private static final Logger logger = LoggerFactory.getLogger(KeywordsServiceImpl.class);

    @Autowired
    private KeywordsRMapper keywordsRMapper;

    @Autowired
    private HotsearchRMapper hotsearchRMapper;

    /**
     * 热门搜索
     */
    @Cacheable(key = "KeywordsServiceImpl/getKeywordsAll/hotSearch", expire = 2, dateType = DateEnum.HOURS , ifExecute = "#{request.ifExecute}")
    @Override
    public Result<List<KeywordsResponse>> getHotSearch(KeywordsRequest request) {
        logger.info("热门关键词前端入参：{}", JSON.toJSONString(request));
        List<HotsearchOutModel> hotsearchOutModels = hotsearchRMapper.selectHotAll();
        List<KeywordsResponse> listResponse = new ArrayList<>();
        for (HotsearchOutModel outModel : hotsearchOutModels) {
            KeywordsResponse response = new KeywordsResponse();
            response.setKeywordId(outModel.getSearchwordId());
            response.setKeywordDesc(outModel.getSearchwordDesc());
            response.setKeywordLink("");
            response.setKeywordInsite(1);
            listResponse.add(response);
        }
        return new Result<>(listResponse);
    }

    /**
     * 热门关键词
     */
    @Cacheable(key = "KeywordsServiceImpl/getKeywords", expire = 2, dateType = DateEnum.HOURS)
    @Override
    public Result<List<KeywordsResponse>> getKeywordsAll(KeywordsRequest request) {
        logger.info("热门关键词前端入参：{}", JSON.toJSONString(request));
        KeywordsInModel inModel = new KeywordsInModel();
        inModel.setSecondChannel(request.getSecondChannel());
        List<KeywordsOutModel> KeywordsOutModels = keywordsRMapper.getKeywordsAll(inModel);
        List<KeywordsResponse> listResponse = new ArrayList<>();
        for (KeywordsOutModel outModel : KeywordsOutModels) {
            KeywordsResponse response = new KeywordsResponse();
            BeanUtils.copyProperties(outModel, response);
            listResponse.add(response);
        }
        return new Result<>(listResponse);
    }

}
