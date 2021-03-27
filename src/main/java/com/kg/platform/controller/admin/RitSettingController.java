package com.kg.platform.controller.admin;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.dao.entity.SysUser;
import com.kg.platform.model.in.admin.RitSettingModel;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.admin.RitSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("admin/setting")
@RestController("AdminRitSettingController")
public class RitSettingController extends AdminBaseController{

    @Autowired
    private RitSettingService ritSettingService;

    /**
     * 设置RIT兑换参数
     */
    @RequestMapping(value = "updateRitConvert",method = RequestMethod.POST)
    @ResponseBody
    @BaseControllerNote(beanClazz = RitSettingModel.class)
    public JsonEntity updateRitConvert(@RequestAttribute SysUser sysUser, @RequestAttribute RitSettingModel request){
        return ritSettingService.updateRitConvertSetting(sysUser,request);
    }

    /**
     * 获取RIT兑换参数
     */
    @RequestMapping(value = "getRitConvertProfile",method = RequestMethod.POST)
    @ResponseBody
    @BaseControllerNote(beanClazz = RitSettingModel.class)
    public JsonEntity getRitConvertProfile(@RequestAttribute SysUser sysUser, @RequestAttribute RitSettingModel request){
        return ritSettingService.getRitConvertProfile(sysUser,request);
    }

    /**
     * rit参数设置日志列表
     */
    @RequestMapping(value = "getRitSettingLog",method = RequestMethod.POST)
    @ResponseBody
    public JsonEntity getRitSettingLog(@RequestAttribute UserkgResponse kguser){
        return ritSettingService.getRitSettingLogList(kguser);
    }

    /**
     * 设置RIT提现参数
     */
    @RequestMapping(value = "updateRolloutProfile",method = RequestMethod.POST)
    @ResponseBody
    @BaseControllerNote(beanClazz = RitSettingModel.class)
    public JsonEntity updateRolloutProfile(@RequestAttribute SysUser sysUser, @RequestAttribute RitSettingModel request){
        return ritSettingService.updateRolloutProfileService(sysUser,request);
    }

    /**
     * 获取RIT提现参数接口
     */
    @RequestMapping(value = "getRitRolloutProfile",method = RequestMethod.POST)
    @ResponseBody
    @BaseControllerNote(beanClazz = RitSettingModel.class)
    public JsonEntity getRitRolloutProfile(@RequestAttribute SysUser sysUser, @RequestAttribute RitSettingModel request ){
        return ritSettingService.getRitRolloutProfileService(sysUser,request);
    }
}
