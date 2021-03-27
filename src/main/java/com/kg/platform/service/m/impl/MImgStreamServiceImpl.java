package com.kg.platform.service.m.impl;


import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.MJsonEntity;
import com.kg.platform.model.request.SiteimageAppRequest;
import com.kg.platform.model.request.UserkgRequest;
import com.kg.platform.model.response.SiteimageResponse;
import com.kg.platform.service.app.HomePageService;
import com.kg.platform.service.m.MArticleService;
import com.kg.platform.service.m.MImgStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/10/27.
 */
@Service("mImgStreamService")
public class MImgStreamServiceImpl implements MImgStreamService {

	@Autowired
	private HomePageService homePageService;
	@Autowired
	private MArticleService mArticleService;

	@Override
	public MJsonEntity getBanner(UserkgRequest userkgRequest) {
		SiteimageAppRequest request = new SiteimageAppRequest();
		request.setColumnId(-1);
		Result<List<SiteimageResponse>> result = homePageService.getAllColumn(request,mArticleService.buildMHttpRequest(userkgRequest.getUserIp()));
		return MJsonEntity.makeSuccessJsonEntity(result.getData());
	}
}
