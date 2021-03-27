package com.kg.platform.service.app.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.utils.HttpUtil;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.dao.entity.AppVersionManage;
import com.kg.platform.dao.read.admin.AppVersionManageRMapper;
import com.kg.platform.model.request.admin.AppVersionManageRequest;
import com.kg.platform.model.response.AppVersionResponse;
import com.kg.platform.service.app.AppVersionService;
import org.redisson.api.RQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppVersionServiceImpl implements AppVersionService {

    private static final int forced = 1; //强制更新
    private static final Logger logger = LoggerFactory.getLogger(AppVersionServiceImpl.class);

    private static final String IOS_APP_STORE_URL = "https://itunes.apple.com/cn/lookup?id=1381042034";

    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36";

    @Autowired
    private AppVersionManageRMapper versionManageRMapper;

    @Override
    public AppJsonEntity getLastVersion(AppVersionManageRequest request) {
        AppVersionManage appVersionManage = versionManageRMapper.getLastVersion(request);
        if (appVersionManage == null) {
            return AppJsonEntity.makeExceptionJsonEntity("", "没有相关版本信息");
        }
        AppVersionResponse appVersionResponse = new AppVersionResponse();
        BeanUtils.copyProperties(appVersionManage, appVersionResponse);
        return AppJsonEntity.makeSuccessJsonEntity(appVersionResponse);
    }

    @Override
    public AppJsonEntity getVersion(AppVersionManageRequest request) {

        AppVersionManage appVersionManage = versionManageRMapper.getVersion(request);
        if (appVersionManage == null) {
            logger.info("非法版本 -> 入参:" + JSONObject.toJSONString(request));
            return AppJsonEntity.makeSuccessJsonEntity("");
        }
        AppVersionManage appVersionLast = getlastVersion(request);

        if (appVersionLast == null) {
            logger.info("appVersionManageNew == null -> 没有相关版本信息");
            return AppJsonEntity.makeExceptionJsonEntity("", "没有相关版本信息");
        }

        if (appVersionManage.getVersionNum().equals(appVersionLast.getVersionNum())) {
            logger.info("你的版本已是最新版本" + appVersionManage.getVersionNum());
            return AppJsonEntity.makeSuccessJsonEntity("");
        }

        appVersionLast = setForced(appVersionManage, appVersionLast, request);

        AppVersionResponse appVersionResponse = new AppVersionResponse();
        BeanUtils.copyProperties(appVersionLast, appVersionResponse);

        if (checkIosForAppStore(appVersionResponse)) {
            logger.info("IOS版本还没有上传到 APP store ..." + JSONObject.toJSONString(appVersionResponse));
            return AppJsonEntity.makeSuccessJsonEntity("");
        }
        logger.info("【APP版本更新】 return :" + JSONObject.toJSONString(appVersionResponse));
        return AppJsonEntity.makeSuccessJsonEntity(appVersionResponse);
    }

    private AppVersionManage getlastVersion(AppVersionManageRequest request) {
        AppVersionManage appVersionLast;
        Integer systemType = request.getSystemType();
        String channel = request.getChannel();
        if (systemType == 1) {
            if (StringUtils.isEmpty(channel)) {
                request.setChannel("defalut_channel");
            }
            //获取该渠道最后版本
            appVersionLast = versionManageRMapper.getLastVersionWithChannel(request);
        } else {
            appVersionLast = versionManageRMapper.getLastVersion(request);
        }
        return appVersionLast;
    }

    /**
     * 查询两个版本之间是否有强制更新版本
     *
     * @param appVersionManage
     * @param appVersionManageNew
     * @param request
     * @return
     */
    private AppVersionManage setForced(AppVersionManage appVersionManage, AppVersionManage appVersionLast, AppVersionManageRequest request) {
        Integer systemType = request.getSystemType();
        String channel = request.getChannel();
        if (systemType == 2) {
            channel = null;
        }
        Long idUser = appVersionManage.getId();
        Long idNew = appVersionLast.getId();
        if (idUser != null && idNew != null) {
            if (idUser < idNew) {
                Integer count = versionManageRMapper.getForcedVersion(idUser, idNew, systemType, channel);
                if (count > 0) {
                    appVersionLast.setForced(forced);
                }
            }
        }
        return appVersionLast;
    }


    /**
     * 校验appStore版本和库中返回版本是否一致，不一致则不返回更新版本信息
     *
     * @param appVersionResponse
     * @return
     */
    private boolean checkIosForAppStore(AppVersionResponse appVersionResponse) {
        if (appVersionResponse.getSystemType() == 1) {
            return false;
        }
        String versionNum = appVersionResponse.getVersionNum();
        if (StringUtils.isEmpty(versionNum)) {
            return true;
        }
        String response = HttpUtil.get(IOS_APP_STORE_URL, USER_AGENT);
        if (StringUtils.isEmpty(response)) {
            return true;
        }
        JSONObject jsonObject = JSON.parseObject(response);
        if (jsonObject == null) {
            return true;
        }
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        if (jsonArray == null || jsonArray.size() <= 0) {
            return true;
        }
        JSONObject json = jsonArray.getJSONObject(0);
        String version = json.getString("version");
        return StringUtils.isEmpty(version) || !versionNum.equals(version);
    }


}
