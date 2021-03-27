package com.kg.platform.service.app.impl;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.JedisKey;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.common.utils.PropertyLoader;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.dao.read.KgCommonSettingRMapper;
import com.kg.platform.dao.read.KgInfoSwitchRMapper;
import com.kg.platform.enumeration.CoinEnum;
import com.kg.platform.enumeration.InviteBonusEnum;
import com.kg.platform.enumeration.PushTypeEnum;
import com.kg.platform.model.CoinBgModel;
import com.kg.platform.model.in.KgCommonSettingInModel;
import com.kg.platform.model.out.KgCommonSettingOutModel;
import com.kg.platform.model.out.KgInfoSwitchOutModel;
import com.kg.platform.model.out.SuspendIconOutModel;
import com.kg.platform.model.request.admin.AppVersionManageRequest;
import com.kg.platform.model.response.AdhubConfigResponse;
import com.kg.platform.model.response.AppCoinTypeResponse;
import com.kg.platform.model.response.InviteRuleRsponse;
import com.kg.platform.model.response.SuspendIconResponse;
import com.kg.platform.service.app.AppConfigService;
import com.kg.platform.service.app.UserKgAppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/19.
 */
@Service
public class AppConfigServiceImpl implements AppConfigService {
    private static final Logger logger = LoggerFactory.getLogger(AppConfigServiceImpl.class);
    @Resource(name = "propertyLoader")
    protected PropertyLoader propertyLoader;
    @Autowired
    private UserKgAppService userKgAppService;
    @Autowired
    private KgInfoSwitchRMapper kgInfoSwitchRMapper;
    @Autowired
    private JedisUtils jedisUtils;
    @Autowired
    private CoinBgModel coinBgModel;
    @Autowired
    private KgCommonSettingRMapper kgCommonSettingRMapper;

    @Override
    public Map getAppConfig(AppVersionManageRequest request, HttpServletRequest servletRequest) {

        if (StringUtils.isNotBlank(request.getUserId())) {
            //当有用户ID时需要查询该用户是否关闭新闻的推送 进行相应的tag操作
            KgInfoSwitchOutModel outModel = kgInfoSwitchRMapper.selectByPrimaryKey(Long.valueOf(request.getUserId()));
            if (outModel == null || outModel.getHotNewsSwitch() == null || outModel.getHotNewsSwitch() == 1) {
                //打开状态
                userKgAppService.mqPush(servletRequest, null, PushTypeEnum.BINDING_TAG.getCode(), "newsTagIfBind");
            } else if (outModel.getHotNewsSwitch() == 0) {
                //关闭状态
                userKgAppService.mqPush(servletRequest, null, PushTypeEnum.UNBINDING_TAG.getCode(), "newsTagIfBind");
            }
        } else {
            //无用户ID直接进行绑定
            userKgAppService.mqPush(servletRequest, null, PushTypeEnum.BINDING_TAG.getCode(), "newsTagIfBind");
        }

        //从缓存中取出
        Map result = jedisUtils.get(JedisKey.getAppConfigInterface(), Map.class);
        if (result != null) {
            logger.info("appconfig从缓存中获取------");
            return result;
        }
        Map<String, Object> map = new HashMap<>();
        map = buildOfficialContact(map);
        map = buildTabs(map);
        map = buildGlobalProperty(map);
        map = buildAppCoinType(map);
        map = buildAdhubConfig(map);
        map.put("compassUrl",coinBgModel.getCompassUrl());
        //存入缓存
        jedisUtils.set(JedisKey.getAppConfigInterface(), new Gson().toJson(map), 600);
        logger.info("appconfig存入缓存------");
        return map;
    }

    private Map<String,Object> buildAdhubConfig(Map<String, Object> map){
    	KgCommonSettingInModel inModel = new KgCommonSettingInModel();
    	inModel.setSettingKey(AdhubConfigResponse.SETTINT_KEY);
    	KgCommonSettingOutModel outModel = kgCommonSettingRMapper.selectBySettingKey(inModel);
    	if(outModel!=null && StringUtils.isNotBlank(outModel.getSettingValue())){
			AdhubConfigResponse response = JSON.parseObject(outModel.getSettingValue(), AdhubConfigResponse.class);
			Field[] fields = response.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				try {
					map.put(field.getName(), field.get(response));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}
    private Map<String, Object> buildGlobalProperty(Map<String, Object> map) {
        map.put("mloginBonusTXB", propertyLoader.getProperty("common", "global.mloginBonusTXB"));
        map.put("apkDownloadUrl", propertyLoader.getProperty("common", "app.apkDownloadUrl"));
        return map;
    }


    /**
     * 导航图片,目前没有使用,暂留
     *
     * @param map
     * @return
     */
    private Map<String, Object> buildTabs(Map<String, Object> map) {
//        List<KgMobileImgOutModel> outModels = kgMobileImgRMapper.selectTabs();
//        List<KgMobileImgResponse> tabs = new ArrayList<>();
//        for (KgMobileImgOutModel outModel : outModels) {
//            KgMobileImgResponse response = new KgMobileImgResponse();
//            response.setNormalImg(outModel.getNormalImg());
//            response.setChoiseImg(outModel.getChoiseImg());
//            tabs.add(response);
//        }
        map.put("tabs", null);
        return map;
    }

    /**
     * 官方联系方式
     *
     * @param map
     * @return
     */
    private Map<String, Object> buildOfficialContact(Map<String, Object> map) {
        String customServiceTelephone = propertyLoader.getProperty("common", "global.CustomerServicePhone");
        String reportTelephone = propertyLoader.getProperty("common", "global.ReportPhone");
        map.put("customServiceTelephone", customServiceTelephone);
        map.put("reportTelephone", reportTelephone);
        return map;
    }

    @Override
    public List<InviteRuleRsponse> getInviteRule() {
        List<InviteRuleRsponse> response = new ArrayList<>();
        InviteBonusEnum[] inviteBonusEnums = InviteBonusEnum.values();
        for (InviteBonusEnum i : inviteBonusEnums) {
            InviteRuleRsponse res = new InviteRuleRsponse();
            res.set_count(i.get_count());
            res.set_bonus(i.get_bonus());
            response.add(res);
        }
        return response;
    }

	@Override
	public AppJsonEntity getSuspendIcon(HttpServletRequest servletRequest) {
		KgCommonSettingInModel inModel = new KgCommonSettingInModel();
		inModel.setSettingKey(SuspendIconResponse.SETTING_KEY);
		KgCommonSettingOutModel outModel = kgCommonSettingRMapper.selectBySettingKey(inModel);
		if(outModel==null||StringUtils.isBlank(outModel.getSettingValue())){
			//没有悬浮图数据
			return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
		}

		SuspendIconOutModel suspendIconOutModel = JSON.parseObject(outModel.getSettingValue(), SuspendIconOutModel.class);
		//判断悬浮图在IOS和Android是否开启
		if(servletRequest.getIntHeader("os_version")==1 && suspendIconOutModel.getIosSwitch()==0){
			//IOS没有悬浮图数据
			return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
		}
		if(servletRequest.getIntHeader("os_version")==2 && suspendIconOutModel.getAndroidSwitch()==0){
			//Android没有悬浮图数据
			return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
		}

		SuspendIconResponse response = new SuspendIconResponse();
		BeanUtils.copyProperties(suspendIconOutModel,response);
		Map<String,Object> map = new HashMap<>();
		map.put("suspendIcon", response);
		return AppJsonEntity.makeSuccessJsonEntity(map);
	}

	/**
     * 构架币种信息
     *
     * @return
     */
    private Map<String, Object> buildAppCoinType(Map<String, Object> map) {
        List<AppCoinTypeResponse> appCoinTypeResponseList = new ArrayList<>();
        appCoinTypeResponseList.add(AppCoinTypeResponse.buildByCoinType(CoinEnum.TV));
        appCoinTypeResponseList.add(AppCoinTypeResponse.buildByCoinType(CoinEnum.KG));
        appCoinTypeResponseList.add(AppCoinTypeResponse.buildByCoinType(CoinEnum.RIT));
        map.put("coinTypeList", appCoinTypeResponseList);
        return map;
    }
}
