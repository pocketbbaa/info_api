package com.kg.platform.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.kg.platform.common.aop.Cacheable;
import com.kg.platform.enumeration.DateEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.context.Result;
import com.kg.platform.dao.read.BaseInfoRMapper;
import com.kg.platform.model.in.BaseInfoInModel;
import com.kg.platform.model.out.BaseInfoOutModel;
import com.kg.platform.model.request.BaseInfoRequest;
import com.kg.platform.model.response.BaseInfoResponse;
import com.kg.platform.service.BaseInfoService;

@Service
public class BaseInfoServiceImpl implements BaseInfoService {
    private static final Logger logger = LoggerFactory.getLogger(BaseInfoServiceImpl.class);

    @Inject
    BaseInfoRMapper baseInfoRMapper;

    /*
     * 
     * 首页右侧关于我们
     */

     @Cacheable(key = "getbaseinfoAll", expire = 2, dateType = DateEnum.HOURS)
     @Override
     public Result<List<BaseInfoResponse>> getbaseinfoAll() {
        List<BaseInfoOutModel> outModels = baseInfoRMapper.getbaseinfoAll();
        List<BaseInfoResponse> listresponses = new ArrayList<>();
        for (BaseInfoOutModel baseInfoOutModel : outModels) {
            BaseInfoResponse response = new BaseInfoResponse();
            response.setInfoName(baseInfoOutModel.getInfoName());
            response.setInfoType(baseInfoOutModel.getInfoType());

            listresponses.add(response);

        }
        return new Result<>(listresponses);
    }

    /**
     * 获取关于我们对应类型详情
     */
    @Cacheable(key = "#{request.infoType}", expire = 2, dateType = DateEnum.HOURS)
    @Override
    public Result<BaseInfoResponse> getbaseinfoType(BaseInfoRequest request) {
        logger.info("获取关于我们对应类型详情入参：{}", JSON.toJSONString(request));
        BaseInfoInModel inModel = new BaseInfoInModel();
        inModel.setInfoType(request.getInfoType());
        BaseInfoOutModel outModel = baseInfoRMapper.getbaseinfoType(inModel);
        BaseInfoResponse response = new BaseInfoResponse();
        response.setInfoName(outModel.getInfoName());
        response.setInfoType(outModel.getInfoType());
        response.setInfoDetail(outModel.getInfoDetail());
        return new Result<>(response);
    }

}
