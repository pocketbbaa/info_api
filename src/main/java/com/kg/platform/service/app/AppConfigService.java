package com.kg.platform.service.app;

import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.model.request.admin.AppVersionManageRequest;
import com.kg.platform.model.response.InviteRuleRsponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/19.
 */
public interface AppConfigService {
    Map getAppConfig(AppVersionManageRequest request,HttpServletRequest servletRequest);

    /**
     * 邀新收徒奖励规则
     * @return
     */
    List<InviteRuleRsponse> getInviteRule();

	/**
	 * 获取APP首页悬浮图标配置信息
	 * @return
	 */
	AppJsonEntity getSuspendIcon(HttpServletRequest servletRequest);
}
