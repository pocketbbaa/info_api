package com.kg.platform.service.admin;

import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.model.request.KgSeoTdkRequest;
import com.kg.platform.model.request.admin.TDKEditRequest;

public interface TDKService {

    boolean editHomeTDK(TDKEditRequest request);

	/**
	 * 获取TDK详情
	 * @param request
	 * @return
	 */
	JsonEntity detailTdk(KgSeoTdkRequest request);

	/**
	 * 编辑TDK
	 * @param request
	 * @return
	 */
	JsonEntity editTdk(KgSeoTdkRequest request);
}
