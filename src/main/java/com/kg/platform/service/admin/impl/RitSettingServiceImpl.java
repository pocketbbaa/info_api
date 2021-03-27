package com.kg.platform.service.admin.impl;

import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.Constants;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.dao.entity.KgConvertDetail;
import com.kg.platform.dao.entity.KgRitConvert;
import com.kg.platform.dao.entity.KgRitRollout;
import com.kg.platform.dao.entity.SysUser;
import com.kg.platform.dao.read.admin.KgRitConvertRMapper;
import com.kg.platform.dao.read.admin.KgRitRolloutRMapper;
import com.kg.platform.dao.write.admin.KgConvertDetailWMapper;
import com.kg.platform.dao.write.admin.KgRitConvertWMapper;
import com.kg.platform.dao.write.admin.KgRitRolloutWMapper;
import com.kg.platform.model.in.admin.RitSettingModel;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.admin.RitSettingService;
import net.bytebuddy.asm.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

@Service
public class RitSettingServiceImpl implements RitSettingService {
    private static final Logger log = LoggerFactory.getLogger(RitSettingServiceImpl.class);

    @Autowired
    private KgRitConvertRMapper kgRitConvertRMapper;

    @Autowired
    private KgRitConvertWMapper kgRitConvertWMapper;

    @Autowired
    private KgConvertDetailWMapper kgConvertDetailWMapper;

    @Autowired
    private KgRitRolloutWMapper kgRitRolloutWMapper;

    @Autowired
    private KgRitRolloutRMapper kgRitRolloutRMapper;

    private static final String DEFAULT_NOTICE = "未做任何参数变动";

    @Override
    public JsonEntity updateRitConvertSetting(SysUser sysUser, RitSettingModel ritSettingModel) {
        if(sysUser == null ||sysUser.getSysUserId() == null){
            log.warn("【接口描述:{设置rit提现参数} 登录信息失效】");
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.TOKENRERROR);
        }
        if(ritSettingModel.getUserType() == null || ritSettingModel.getPushTimeHour() == null || ritSettingModel.getPushTimeMinute() == null){
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if(ritSettingModel.getKgRate() == null || ritSettingModel.getRitRate() == null){
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        Boolean flag = insertSettingParams(sysUser,ritSettingModel);
        if(!flag){
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SYS_EXCEPTION);
        }
        return JsonEntity.makeSuccessJsonEntity();
    }

    @Override
    public JsonEntity getRitConvertProfile(SysUser sysUser, RitSettingModel ritSettingModel) {
        if(sysUser == null ||sysUser.getSysUserId() == null){
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.TOKENRERROR);
        }
        if(ritSettingModel.getUserType() == null){
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        //时间转换成24小时制
/*        formatDateStr(ritSettingModel);*/
        HashMap<String,Object> rows = new HashMap<>();
        KgRitConvert kgRitConvert = kgRitConvertRMapper.selectByUserType(ritSettingModel.getUserType());
        if(kgRitConvert == null){
            return JsonEntity.makeSuccessJsonEntity(rows);
        }else{
            String rate = kgRitConvert.getRitRate();
            rows.put("ritRate",rate.split("-")[1]);
            rows.put("kgRate",rate.split("-")[0]);
            rows.put("dayLimit",kgRitConvert.getNextDayLimit());
            rows.put("dayCnt",kgRitConvert.getNextDayCnt());
            if(kgRitConvert.getNextPushTime() == null){
                rows.put("pushTimeHour",-1);
                rows.put("pushTimeMinute",-1);
            }else{
                rows.put("pushTimeHour",transDate(kgRitConvert.getNextPushTime(),"HH"));
                rows.put("pushTimeMinute",transDate(kgRitConvert.getNextPushTime(),"mm"));
            }
        }
        return JsonEntity.makeSuccessJsonEntity(rows);
    }

    private void formatDateStr(RitSettingModel ritSettingModel) {

        if(Integer.parseInt(ritSettingModel.getPushTimeMinute())<10){
            ritSettingModel.setPushTimeMinute("0"+ritSettingModel.getPushTimeMinute());
        }
        if(Integer.parseInt(ritSettingModel.getPushTimeHour())<10){
            ritSettingModel.setPushTimeHour("0"+ritSettingModel.getPushTimeHour());
        }
    }

    @Override
    public JsonEntity getRitSettingLogList(UserkgResponse kguser) {
        //wait develop
        return null;
    }

    @Override
    public JsonEntity updateRolloutProfileService(SysUser sysUser, RitSettingModel request) {
        if(sysUser == null || sysUser.getSysUserId() == null){
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.TOKENRERROR);
        }
        if(request.getUserType() == null){
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        Boolean flag = insertSettingRolloutParams(sysUser,request);
        if(!flag){
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SYS_EXCEPTION);
        }
        return JsonEntity.makeSuccessJsonEntity();
    }

    @Override
    public JsonEntity getRitRolloutProfileService(SysUser sysUser, RitSettingModel request) {
        if(sysUser == null || sysUser.getSysUserId() == null){
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.TOKENRERROR);
        }
        if(request.getUserType() == null){
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        KgRitRollout kgRitRollout = kgRitRolloutRMapper.selectByPrimaryUserType(request.getUserType());
        return JsonEntity.makeSuccessJsonEntity(kgRitRollout);
    }

    private Boolean insertSettingRolloutParams(SysUser sysUser, RitSettingModel request) {
        KgRitRollout rollout = kgRitRolloutRMapper.selectByPrimaryUserType(request.getUserType());
        KgConvertDetail kgConvertDetail;
        try {
            if(rollout == null){
                rollout = getRolloutModel(request);
                kgRitRolloutWMapper.insert(rollout);
            }else{
                kgConvertDetail = genRolloutLog(sysUser,request,rollout);
                updateRitRolloutModel(request,rollout);
                kgRitRolloutWMapper.updateByPrimaryKeySelective(rollout);
                kgConvertDetailWMapper.insert(kgConvertDetail);
            }
        } catch (Exception e) {
            log.error("【保存RIT提现参数时发生异常 e:{}】",e.getMessage());
            return false;
        }
        return true;
    }

    private void updateRitRolloutModel(RitSettingModel request, KgRitRollout rollout) {
        rollout.setMonthLimit(request.getMonthLimit());
        rollout.setMinAmount(request.getMinAmount());
        rollout.setDayLimit(request.getDayLimit());
        rollout.setRate(Float.parseFloat(request.getRate()));
    }

    private KgConvertDetail genRolloutLog(SysUser sysUser, RitSettingModel request, KgRitRollout rollout) {
        KgConvertDetail kgConvertDetail =getDetailModel(sysUser,request,Constants.ZERO);
        kgConvertDetail.setUpdateInfo(assembleRolloutUpdateInfo(request,rollout));
        return kgConvertDetail;
    }

    private String assembleRolloutUpdateInfo(RitSettingModel request, KgRitRollout rollout) {
        StringBuilder sb = new StringBuilder();
        if(!rollout.getDayLimit().equals(request.getDayLimit())){
            sb.append(formatInfo("每日可提现额度由",rollout.getDayLimit()+"","调整为",request.getDayLimit()+""));
            sb.append("  ");
        }
        if(!rollout.getMinAmount().equals(request.getMinAmount())){
            sb.append(formatInfo("最少转出由",rollout.getMinAmount()+"","调整为",request.getMinAmount()+""));
            sb.append("  ");
        }
        if(!rollout.getMonthLimit().equals(request.getMonthLimit())){
            sb.append(formatInfo("每月可提现额度由",rollout.getMonthLimit()+"","调整为",request.getMonthLimit()+""));
            sb.append("  ");
        }
        if(!giveupZero(rollout.getRate().toString()).equals(request.getRate())){
            sb.append(formatInfo("费率由",rollout.getRate()+"","调整为",request.getRate()+""));
        }
        if(sb.toString().equals("")){
            sb.append(DEFAULT_NOTICE);
        }
        return sb.toString();
    }

    private String giveupZero(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉后面无用的零
            s = s.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
        }
        return s;
    }

    private StringBuilder formatInfo(String...args){
        StringBuilder sb = new StringBuilder();
        for (String param:args){
            sb.append(param);
        }
        return sb;
    }

    private KgConvertDetail getDetailModel(SysUser sysUser,RitSettingModel request,Integer operType){
        KgConvertDetail kgConvertDetail = new KgConvertDetail();
        kgConvertDetail.setUserNick(sysUser.getSysUserName());
        kgConvertDetail.setUserType(request.getUserType());
        kgConvertDetail.setUserName(sysUser.getUserRealname());
        kgConvertDetail.setUserId(sysUser.getSysUserId());
        kgConvertDetail.setUpdateTime(DateUtils.formatDate(new Date(),DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
        kgConvertDetail.setOperationType(operType);
        return kgConvertDetail;
    }

    private KgRitRollout getRolloutModel(RitSettingModel request) {
        KgRitRollout kgRitRollout = new KgRitRollout();
        kgRitRollout.setUserType(request.getUserType());
        kgRitRollout.setMonthLimit(request.getMonthLimit());
        kgRitRollout.setMinAmount(request.getMinAmount());
        kgRitRollout.setDayLimit(request.getDayLimit());
        kgRitRollout.setRate(Float.parseFloat(request.getRate()));
        return kgRitRollout;
    }

    private String transDate(String dateStr,String pattern){
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
        try {
            Date date = sdf.parse(dateStr);
            return sf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Boolean insertSettingParams(SysUser sysUser, RitSettingModel ritSettingModel) {

        KgRitConvert kgRitConvert = kgRitConvertRMapper.selectByUserType(ritSettingModel.getUserType());
        KgConvertDetail kgConvertDetail;
        try {
            if(kgRitConvert == null){
                kgRitConvert = getRitSettingModel(ritSettingModel);
                kgRitConvertWMapper.insert(kgRitConvert);
            }else{
                //生成操作日志对象
                kgConvertDetail = genLogModel(sysUser,kgRitConvert,ritSettingModel);
                UpdateSettingModel(kgRitConvert,ritSettingModel);
                //插入
                kgRitConvertWMapper.updateByPrimaryKeySelective(kgRitConvert);
                kgConvertDetailWMapper.insert(kgConvertDetail);
            }
        } catch (Exception e) {
            log.error("【更新RIT兑换相关参数出现异常 e:{}】",e.getMessage());
            return false;
        }
        return true;
    }

    private KgConvertDetail genLogModel(SysUser kguser, KgRitConvert kgRitConvert, RitSettingModel ritSettingModel) {
        KgConvertDetail kgConvertDetail = getDetailModel(kguser,ritSettingModel,Constants.ONE);
        kgConvertDetail.setUpdateInfo(assembleUpdateInfo(kgRitConvert,ritSettingModel));
        return kgConvertDetail;
    }

    /**
     * 组装兑换RIT相关参数更新日志
     */
    private String assembleUpdateInfo(KgRitConvert kgRitConvert, RitSettingModel ritSettingModel) {
        StringBuilder sb = new StringBuilder();
        if(!kgRitConvert.getNextDayLimit().equals(ritSettingModel.getNextDayLimit())){
            sb.append("每日可兑换额度由")
                    .append(kgRitConvert.getNextDayLimit())
                    .append("调整为")
                    .append(ritSettingModel.getNextDayLimit())
                    .append("  ");
        }
        if(!kgRitConvert.getNextDayCnt().equals(ritSettingModel.getNextDayCnt())){
            sb.append("每日可兑换次数由")
                    .append(kgRitConvert.getNextDayCnt())
                    .append("调整为")
                    .append(ritSettingModel.getNextDayCnt())
                    .append("  ");
        }
        if(!kgRitConvert.getNextPushTime().equals(getFormatTime(ritSettingModel))){
            sb.append("兑换时间由")
                    .append(kgRitConvert.getNextPushTime())
                    .append("调整为")
                    .append(getFormatTime(ritSettingModel));
      /*              .append(ritSettingModel.getPushTimeHour())
                    .append(":")
                    .append(ritSettingModel.getPushTimeMinute())
                    .append(":00");*/
        }
        if(sb.toString().equals("")){
            sb.append(DEFAULT_NOTICE);
        }
        return sb.toString();
    }

    private void UpdateSettingModel(KgRitConvert kgRitConvert, RitSettingModel ritSettingModel) {
        kgRitConvert.setNextDayLimit(ritSettingModel.getNextDayLimit());
        kgRitConvert.setNextDayCnt(ritSettingModel.getNextDayCnt());
        kgRitConvert.setNextPushTime(getFormatTime(ritSettingModel));
        kgRitConvert.setRitRate(ritSettingModel.getKgRate()+"-"+ritSettingModel.getRitRate());
    }

    private KgRitConvert getRitSettingModel(RitSettingModel ritSettingModel) {
        KgRitConvert kgRitConvert = new KgRitConvert();
        kgRitConvert.setUserType(ritSettingModel.getUserType());
        kgRitConvert.setNextDayLimit(ritSettingModel.getNextDayLimit());
        kgRitConvert.setNextDayCnt(ritSettingModel.getNextDayCnt());
        kgRitConvert.setNextPushTime(getFormatTime(ritSettingModel));

        kgRitConvert.setDayLimit(ritSettingModel.getNextDayLimit());
        kgRitConvert.setDayCnt(ritSettingModel.getNextDayCnt());
        kgRitConvert.setPushTime(getFormatTime(ritSettingModel));
        kgRitConvert.setRitRate(Constants.RIT_RATE);
        return kgRitConvert;
    }

    private String getFormatTime(RitSettingModel ritSettingModel){
        return DateUtils.formatDate(DateUtils.getNextDay(new Date()),DateUtils.DATE_FORMAT_YYYY_MM_DD)+" "+ritSettingModel.getPushTimeHour()+":"+ritSettingModel.getPushTimeMinute()+":00";
    }
}
