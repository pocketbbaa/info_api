package com.kg.platform.common.schedule;

import com.kg.platform.dao.entity.KgRitConvert;
import com.kg.platform.dao.read.admin.KgRitConvertRMapper;
import com.kg.platform.dao.write.admin.KgRitConvertWMapper;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * rit系统参数设置定时任务
 * 每日0点刷新额度
 */
@Component
@JobHander(value = "pushRitSettingProfile")
public class JobSchedule extends IJobHandler {

    @Autowired
    private KgRitConvertWMapper kgRitConvertWMapper;

    @Autowired
    private KgRitConvertRMapper kgRitConvertRMapper;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        List<KgRitConvert> ritProfileList =  kgRitConvertRMapper.selectAll();
        if(ritProfileList == null || ritProfileList.isEmpty()){
            XxlJobLogger.log("【未获取到rit系统参数基础信息 中断当前进程】");
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"获取到的rit基础表为空");
        }
        Long startTime = System.currentTimeMillis();
        int retry = 0;
        while (retry<=3) {
            try {
                updateData(ritProfileList);
                break;
            } catch (Exception e) {
                XxlJobLogger.log("【更新RIT参数失败 异常原因:{}】",e.getMessage());
                retry++;
            }
        }
        XxlJobLogger.log("【RIT系统参数更新完毕 耗时:{"+(System.currentTimeMillis()-startTime)+"}】");
        return ReturnT.SUCCESS;
    }

    private void updateData(List<KgRitConvert> ritProfileList) {

        for (KgRitConvert kgRitConvert:ritProfileList){
            kgRitConvert.setDayCnt(kgRitConvert.getNextDayCnt());
            kgRitConvert.setDayLimit(kgRitConvert.getNextDayLimit());
            kgRitConvert.setPushTime(kgRitConvert.getNextPushTime());
            kgRitConvertWMapper.updateByPrimaryKeySelective(kgRitConvert);
        }
    }
}
