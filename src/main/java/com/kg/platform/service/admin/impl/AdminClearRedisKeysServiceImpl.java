package com.kg.platform.service.admin.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.common.utils.MD5Util;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.dao.read.KgCommonSettingRMapper;
import com.kg.platform.model.in.KgCommonSettingInModel;
import com.kg.platform.model.out.AdminRedisUtilOutModel;
import com.kg.platform.model.out.KgCommonSettingOutModel;
import com.kg.platform.model.request.admin.AdminRedisUtilRequest;
import com.kg.platform.service.admin.AdminClearRedisKeysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/8/3.*/


@Service
public class AdminClearRedisKeysServiceImpl implements AdminClearRedisKeysService{
    public static final String SALT = "_KG";

    @Autowired
    private KgCommonSettingRMapper kgCommonSettingRMapper;

    @Autowired
    private JedisUtils jedisUtils;

    @Override
    public Boolean clearRedisKeys(AdminRedisUtilRequest request) {
        //校验口令是否正确
        String userId = request.getUserId();
        String requestAccess = request.getAccess();
        String access = MD5Util.md5Hex(userId+SALT);
        if(access.equals(requestAccess)){
            //口令正确
            //获取配置的用户权限
            KgCommonSettingInModel inModel = new KgCommonSettingInModel();
            inModel.setSettingKey(AdminRedisUtilOutModel.SETTING_KEY);
            KgCommonSettingOutModel outModel = kgCommonSettingRMapper.selectBySettingKey(inModel);
            if(outModel!=null&& StringUtils.isNotBlank(outModel.getSettingValue())){
                List<AdminRedisUtilOutModel> outModels = JSON.parseArray(outModel.getSettingValue(),AdminRedisUtilOutModel.class);
                //根据传入的userId匹配该用户能进行操作
                for (AdminRedisUtilOutModel adminRedisUtilOutModel:outModels) {
                    if(userId.equals(adminRedisUtilOutModel.getUserId())){
                        //匹配到了 去除能够删除的keys
                        List<String> redisKeys = adminRedisUtilOutModel.getRedisKeys();
                        for (String key:redisKeys) {
                            jedisUtils.delKeys(key, Lists.newArrayList());
                        }
                        return true;
                    }
                }
            }

        }
        return false;
    }
}
