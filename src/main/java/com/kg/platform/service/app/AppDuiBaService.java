package com.kg.platform.service.app;

import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.model.request.DuiBaRequest;
import com.kg.platform.model.response.UserkgResponse;

/**
 * Created by Administrator on 2018/10/13.
 */
public interface AppDuiBaService {
	/**
	 * 获取兑吧免登录URL
	 * @param kguser
	 * @return
	 */
	AppJsonEntity generatorDuiBaUrl(UserkgResponse kguser,  DuiBaRequest request);
}
