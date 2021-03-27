package com.kg.platform.service.admin.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.Constants;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.dao.entity.KgConvertDetail;
import com.kg.platform.dao.entity.SysUser;
import com.kg.platform.dao.read.KgCommonSettingRMapper;
import com.kg.platform.dao.write.KgCommonSettingWMapper;
import com.kg.platform.dao.write.admin.KgConvertDetailWMapper;
import com.kg.platform.model.in.KgCommonSettingInModel;
import com.kg.platform.model.out.KgCommonSettingOutModel;
import com.kg.platform.model.request.admin.RevenueRankingSetRequest;
import com.kg.platform.model.response.admin.RevenueRankingSetResponse;
import com.kg.platform.service.admin.RevenueRankingSetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RevenueRankingSetServiceImpl implements RevenueRankingSetService {

    private static final Logger log = LoggerFactory.getLogger(RevenueRankingSetServiceImpl.class);

    @Autowired
    private KgCommonSettingRMapper kgCommonSettingRMapper;

    @Autowired
    private KgCommonSettingWMapper kgCommonSettingWMapper;

    @Autowired
    private KgConvertDetailWMapper kgConvertDetailWMapper;


    @Override
    public JsonEntity getSet(String key) {
        log.info("获取配置 -> key:" + key);
        KgCommonSettingOutModel outModel = kgCommonSettingRMapper.selectBySettingKey(buildKgCommonSettingInModel(key));
        if (outModel == null) {
            return JsonEntity.makeExceptionJsonEntity("", "没有获取到数据");
        }
        String settingValue = outModel.getSettingValue();
        RevenueRankingSetResponse response = JSON.parseObject(settingValue, new TypeReference<RevenueRankingSetResponse>() {
        });
        log.info("返回配置 -> response:" + JSONObject.toJSONString(response));
        return JsonEntity.makeSuccessJsonEntity(response);
    }

    @Override
    public JsonEntity updateSet(String key, RevenueRankingSetRequest request, SysUser sysUser) {
        if (request == null) {
            return JsonEntity.makeSuccessJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        String settingValue = JSONObject.toJSONString(request);
        KgCommonSettingInModel inModel = new KgCommonSettingInModel();
        inModel.setSettingKey(key);
        inModel.setSettingValue(settingValue);
        try {
            //新增操作日志
            recordSetLog(inModel,sysUser,request);
        }catch (Exception e){
            log.error("【记录系统参数设置日志时发生异常 e:{}】",e.getMessage());
        }
        int success = kgCommonSettingWMapper.upateBySettingKey(inModel);
        return JsonEntity.makeSuccessJsonEntity(success);

    }

    private void recordSetLog(KgCommonSettingInModel inModel,SysUser sysUser,RevenueRankingSetRequest request) {

        KgCommonSettingOutModel outModel = kgCommonSettingRMapper.selectBySettingKey(inModel);

        if(outModel == null){
            outModel = new KgCommonSettingOutModel();
        }
        KgConvertDetail kgConvertDetail = new KgConvertDetail();
        kgConvertDetail.setOperationType(Constants.FIRST);
        kgConvertDetail.setUpdateTime(DateUtils.formatDate(new Date(),DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
        kgConvertDetail.setUserId(sysUser.getSysUserId());
        kgConvertDetail.setUserName(sysUser.getSysUserName());
        kgConvertDetail.setUserType(-1);
        kgConvertDetail.setUserNick(sysUser.getUserRealname());
        kgConvertDetail.setUpdateInfo(assembleUpdateInfo(outModel,request));
        kgConvertDetailWMapper.insert(kgConvertDetail);
    }

    private static final String SHOW= "展示";
    private static final String NOSHOW= "不展示";
    private String assembleUpdateInfo(KgCommonSettingOutModel outModel,RevenueRankingSetRequest request){
        StringBuilder sb = new StringBuilder();
        if(outModel.getSettingValue().isEmpty()){
            return sb.append("初始化为")
                    .append(request.getShow()==1?SHOW:NOSHOW).toString();
        }else if((request.getShow()+"").equals(JSON.parseObject(outModel.getSettingValue()).getString("show"))){
            return sb.append("无调整").toString();
        }else{
            return sb.append("由")
                    .append(JSON.parseObject(outModel.getSettingValue()).getString("show").equals("1")?SHOW:NOSHOW)
                    .append("调整为")
                    .append(request.getShow()==1?SHOW:NOSHOW)
                    .toString();
        }
    }

    private KgCommonSettingInModel buildKgCommonSettingInModel(String key) {
        KgCommonSettingInModel inModel = new KgCommonSettingInModel();
        inModel.setSettingKey(key);
        return inModel;
    }

}
