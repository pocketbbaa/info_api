package com.kg.platform.controller.admin;

import com.alibaba.fastjson.JSON;
import com.kg.platform.controller.BaseTest;
import com.kg.platform.dao.entity.*;
import com.kg.platform.dao.read.admin.KgConvertDetailRMapper;
import com.kg.platform.dao.read.admin.KgRitConvertRMapper;
import com.kg.platform.dao.write.admin.KgConvertDetailWMapper;
import com.kg.platform.dao.write.admin.KgRitConvertWMapper;
import com.kg.platform.dao.write.admin.KgRitFreezeLogWMapper;
import com.kg.platform.dao.write.admin.KgRitRolloutWMapper;
import com.kg.platform.model.in.admin.RitSettingModel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RateProfileControllerTest extends BaseTest {

    @Autowired
    private KgRitConvertWMapper kgRitConvertWMapper;

    @Autowired
    private KgRitConvertRMapper kgRitConvertRMapper;

    @Autowired
    private KgConvertDetailRMapper kgConvertDetailRMapper;

    @Autowired
    private KgConvertDetailWMapper kgConvertDetailWMapper;

    @Autowired
    private KgRitRolloutWMapper kgRitRolloutWMapper;

    @Autowired
    private KgRitFreezeLogWMapper kgRitFreezeLogWMapper;

    @Autowired
    private RitSettingController ritSettingController;

    @Test
    public void testWriteConnection(){
        KgRitConvert kgRitConvert = new KgRitConvert();
        kgRitConvert.setDayCnt(2);
        kgRitConvert.setDayLimit(10000L);
        kgRitConvert.setNextDayCnt(3);
        kgRitConvert.setNextDayLimit(20000L);
        kgRitConvert.setPushTime("2018-09-07 09:00:00");
        kgRitConvert.setUserType(0);
        kgRitConvert.setRitRate("150-1");
        kgRitConvert.setNextPushTime("2018-09-07 09:00:00");
        kgRitConvertWMapper.insert(kgRitConvert);
    }

    @Test
    public void testReadConnection(){
        System.out.println(kgRitConvertRMapper.selectByPrimaryKey(1L));
    }

    @Test
    public void testDetailConnection(){
        KgConvertDetail kgConvertDetail = new KgConvertDetail();
        kgConvertDetail.setOperationType(1);
        kgConvertDetail.setUpdateInfo("将LP改为了WY");
        kgConvertDetail.setUpdateTime("2018-09-06 15:03:00");
        kgConvertDetail.setUserId(21432423L);
        kgConvertDetail.setUserName("chen");
        kgConvertDetail.setUserNick("陈");
        kgConvertDetail.setUserType(1);
        kgConvertDetailWMapper.insert(kgConvertDetail);
    }

    @Test
    public void testRollConnection(){
        KgRitRollout kgRitRollout = new KgRitRollout();
        kgRitRollout.setDayLimit(16666L);
        kgRitRollout.setKgRate("1-150");
        kgRitRollout.setMinAmount(4000L);
        kgRitRollout.setMonthLimit(500000L);
        kgRitRollout.setUserType(1);
        kgRitRolloutWMapper.insert(kgRitRollout);
    }

    @Test
    public void testLog(){
        KgRitFreezeLog kgRitFreezeLog = new KgRitFreezeLog();
        kgRitFreezeLog.setCause("test");
        kgRitFreezeLog.setFreezeCnt(2L);
        kgRitFreezeLog.setOperUserId(57382748L);
        kgRitFreezeLog.setType(1);
        kgRitFreezeLog.setUserId(5743895478L);
        kgRitFreezeLogWMapper.insert(kgRitFreezeLog);
    }

    @Test
    public void testRitSetting(){
        SysUser kgUser = new SysUser();
        kgUser.setSysUserId(438071067798544384L);
        kgUser.setSysUserName("chen");

        RitSettingModel ritSettingModel = new RitSettingModel();
        ritSettingModel.setNextDayCnt(42);
        ritSettingModel.setNextDayLimit(100002L);
//        ritSettingModel.setPushTime("2018-09-12 12:00:00");
        ritSettingModel.setKgRate("150");
        ritSettingModel.setRitRate("1");
        ritSettingModel.setUserType(0);
        ritSettingModel.setPushTimeHour(14+"");
        ritSettingModel.setPushTimeMinute("02");
        ritSettingModel.setRate("10");

        System.err.println(ritSettingController.updateRitConvert(kgUser,ritSettingModel));
    }

    @Test
    public void testGetRitSetting(){
        SysUser kgUser = new SysUser();
        kgUser.setSysUserId(438071067798544384L);
        kgUser.setSysUserName("chen");
        RitSettingModel ritSettingModel = new RitSettingModel();
        ritSettingModel.setUserType(0);
        System.out.println(JSON.toJSONString(ritSettingController.getRitConvertProfile(kgUser,ritSettingModel)));
    }

    @Test
    public void testUpdateRolloutProfile(){
        SysUser kgUser = new SysUser();
        kgUser.setSysUserId(438071067798544384L);
        kgUser.setSysUserName("chen");
        RitSettingModel ritSettingModel = new RitSettingModel();
        ritSettingModel.setUserType(1);
        ritSettingModel.setMinAmount(30L);
        ritSettingModel.setDayLimit(17777L);
        ritSettingModel.setRate("10");
        ritSettingModel.setMonthLimit(900000L);
        System.err.println(JSON.toJSONString(ritSettingController.updateRolloutProfile(kgUser,ritSettingModel)));
    }

    @Test
    public void testgetRitRolloutProfile(){
        SysUser kgUser = new SysUser();
        kgUser.setSysUserId(438071067798544384L);
        kgUser.setSysUserName("chen");
        RitSettingModel ritSettingModel = new RitSettingModel();
        ritSettingModel.setUserType(1);
        System.err.println(JSON.toJSONString(ritSettingController.getRitRolloutProfile(kgUser,ritSettingModel)));
    }
}
