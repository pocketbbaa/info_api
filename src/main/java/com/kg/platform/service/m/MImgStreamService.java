package com.kg.platform.service.m;

import com.kg.platform.common.entity.MJsonEntity;
import com.kg.platform.model.request.UserkgRequest;

/**
 * Created by Administrator on 2018/10/27.
 */
public interface MImgStreamService {

	/**
	 * 获取首页BANNER
	 * @return
	 */
	MJsonEntity getBanner(UserkgRequest userkgRequest);
}
