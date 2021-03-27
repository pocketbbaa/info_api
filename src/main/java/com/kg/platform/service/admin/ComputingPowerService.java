package com.kg.platform.service.admin;

import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.model.request.admin.CloudOrderRequest;

/**
 * Created by Administrator on 2019/2/22.
 */
public interface ComputingPowerService {
	JsonEntity list(CloudOrderRequest request);

	JsonEntity detail(CloudOrderRequest request);

	JsonEntity update(CloudOrderRequest request);
}
