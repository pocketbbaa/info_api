package com.kg.platform.service.m.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.MJsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.enumeration.RegisterOriginEnum;
import com.kg.platform.model.request.*;
import com.kg.platform.model.response.*;
import com.kg.platform.model.response.m.MArticleResponse;
import com.kg.platform.service.*;
import com.kg.platform.service.app.ArticleAppService;
import com.kg.platform.service.app.HomePageService;
import com.kg.platform.service.m.MArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/10/11.
 */
@Service("mArticleService")
@SuppressWarnings({"rawtypes","unchecked"})
public class MArticleServiceImpl implements MArticleService {

	@Autowired
	private ArticlekgService articlekgService;
	@Autowired
	private HomePageService homePageService;
	@Autowired
	private KeywordsService keywordsService;
	@Autowired
	private NewsFlashService newsFlashService;
	@Autowired
	private ArticleAppService articleAppService;
	@Autowired
	private UserCommentService commentService;

	@Override
	public MJsonEntity selectArticleRecomm(ArticleRequest request) {

		PageModel pageModel = articlekgService.selectArticleAll(request,
				buildPageModel(request.getCurrentPage(), request.getPageSize()),
				buildMHttpRequest(request.getUserIp()));
		List outModels = pageModel.getData();
		if (outModels == null || outModels.size() == 0) {
			return MJsonEntity.makeSuccessJsonEntity(Lists.newArrayList());
		}
		List<ArticleResponse> outs = JSONArray.parseArray(JSON.toJSONString(outModels), ArticleResponse.class);
		List<MArticleResponse> mResponses = buildMArticleResponses(outs);
		Map<String, Object> map = new HashMap<>();
		map.put("articleList", pageModel);

		if (mResponses.size() == 0) {
			return MJsonEntity.makeExceptionJsonEntity(ExceptionEnum.NO_DATA);
		}
		return MJsonEntity.makeSuccessJsonEntity(map);
	}

	@Override
	public MJsonEntity getTopMenus() {
		MJsonEntity mJsonEntity = homePageService.getMTopMenus();
		if (mJsonEntity.getData() != null) {
			return MJsonEntity.makeSuccessJsonEntity(mJsonEntity.getData());
		} else {
			return MJsonEntity.makeExceptionJsonEntity(ExceptionEnum.NO_DATA);
		}

	}

	@Override
	public MJsonEntity searchArticle(ArticleRequest request) {

		PageModel pageModel = articlekgService.getSearchArticle(request,
				buildPageModel(request.getCurrentPage(), request.getPageSize()));
		List outModels = pageModel.getData();
		if (outModels == null || outModels.size() == 0) {
			return MJsonEntity.makeSuccessJsonEntity(Lists.newArrayList());
		}
		List<ArticleResponse> outs = JSONArray.parseArray(JSON.toJSONString(outModels), ArticleResponse.class);
		List<MArticleResponse> responses = buildMArticleResponses(outs);

		pageModel.setData(responses);
		if (responses.size() == 0) {
			return MJsonEntity.makeExceptionJsonEntity(ExceptionEnum.NO_DATA);
		}
		return MJsonEntity.makeSuccessJsonEntity(pageModel);
	}

	@Override
	public MJsonEntity getHotSearch() {
		KeywordsRequest request = new KeywordsRequest();
		request.setIfExecute("0");
		Result<List<KeywordsResponse>> result = keywordsService.getHotSearch(request);
		List outModels = result.getData();
		if (outModels.size() > 0) {
			List<KeywordsResponse> responses = JSONArray.parseArray(JSON.toJSONString(outModels),
					KeywordsResponse.class);
			return MJsonEntity.makeSuccessJsonEntity(responses);
		} else {
			return MJsonEntity.makeExceptionJsonEntity(ExceptionEnum.NO_DATA);
		}
	}

	@Override
	public MJsonEntity getChannelArt(ArticleRequest request) {
		PageModel pageModel = articlekgService.getChannelArt(request,
				buildPageModel(request.getCurrentPage(), request.getPageSize()),
				buildMHttpRequest(request.getUserIp()));
		List outModels = pageModel.getData();
		if (outModels == null || outModels.size() == 0) {
			return MJsonEntity.makeSuccessJsonEntity(Lists.newArrayList());
		}
		List<ArticleResponse> outs = JSONArray.parseArray(JSON.toJSONString(outModels), ArticleResponse.class);
		List<MArticleResponse> mResponses = buildMArticleResponses(outs);
		pageModel.setData(mResponses);
		return MJsonEntity.makeSuccessJsonEntity(pageModel);
	}

	@Override
	public MJsonEntity getNewsFlashListByType(KgNewsFlashRequest request) {
		Map<String, Object> result = newsFlashService.getNewsFlashListByType(request,
				buildPageModel(request.getCurrentPage(), request.getPageSize()),
				buildMHttpRequest(request.getUserIp()));
		/*
		 * PageModel<KgNewsFlashResponse> page =
		 * (PageModel<KgNewsFlashResponse>)result.get("page");
		 * if(page.getData()==null||page.getData().size()==0){ return
		 * MJsonEntity.makeExceptionJsonEntity(ExceptionEnum.NO_DATA); }
		 */
		return MJsonEntity.makeSuccessJsonEntity(result);
	}

	@Override
	public MJsonEntity detailArticle(ArticleRequest request) {

		ArticleDetailAppResponse articleDetailAppResponse = articleAppService.getDetailById(request,
				buildMHttpRequest(request.getUserIp()));
		if (articleDetailAppResponse == null) {
			return MJsonEntity.makeExceptionJsonEntity("10003", "无文章信息");
		}
		// 如果是原创\默认，不显示来源和链接
		Integer articleType = articleDetailAppResponse.getArticleType();
		if (articleType == null || articleType == 1 || articleType == 0) {
			articleDetailAppResponse.setArticleSource("");
			articleDetailAppResponse.setArticleLink("");
		}
		ArticleDetailAppResponse articleDetailText = articleAppService.getArticleDetailText(request);
		articleDetailAppResponse.setArticleTitle(articleDetailText.getArticleTitle());
		articleDetailAppResponse.setArticleDescription(articleDetailText.getArticleDescription());
		articleDetailAppResponse.setArticleText(articleDetailText.getArticleText());

		return MJsonEntity.makeSuccessJsonEntity(articleDetailAppResponse);
	}

	@Override
	public MJsonEntity recommendForYou(ArticleRequest request) {

		Result<List<ArticleResponse>> result = articlekgService.selectRelatedArticle(request);
		return MJsonEntity.makeSuccessJsonEntity(result.getData());
	}

	@Override
	public MJsonEntity getCommentArtAll(UserCommentRequest request) {
		PageModel<UserCommentResponse> pageModel = new PageModel<>();
		pageModel.setCurrentPage(1);
		pageModel.setPageSize(3);
		pageModel = commentService.getCommentArtAll(request, pageModel);
		return MJsonEntity.makeSuccessJsonEntity(pageModel);
	}

	@Override
	public MJsonEntity getUserArticleAll(ArticleRequest request) {
		request.setCreateUser(Long.valueOf(request.getUserId()));
		request.setPublishStatus("1");
		PageModel<ArticleResponse> result = articlekgService.getUserArticleAll(request,
				buildPageModel(request.getCurrentPage(), request.getPageSize()));
		return MJsonEntity.makeSuccessJsonEntity(result);
	}

	public HttpServletRequest buildMHttpRequest(String userIp) {
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		httpServletRequest.addHeader("os_version", RegisterOriginEnum.M.getOrigin());
		httpServletRequest.addParameter("userIp", userIp);
		return httpServletRequest;
	}

	private <T> PageModel<T> buildPageModel(Integer currentPage, Integer pageSize) {
		PageModel<T> page = new PageModel<>();
		page.setCurrentPage(currentPage);
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		return page;
	}

	private List<MArticleResponse> buildMArticleResponses(List<ArticleResponse> outs) {
		List<MArticleResponse> mResponses = Lists.newArrayList();
		for (ArticleResponse outModel : outs) {
			MArticleResponse articleResponse = new MArticleResponse();
			articleResponse.setArticleTitle(outModel.getArticleTitle());
			articleResponse.setUpdateTimestamp(outModel.getUpdateTimestamp());
			articleResponse.setDisplayStatus(outModel.getDisplayStatus());
			articleResponse.setArticleType(outModel.getArticleType());
			articleResponse.setPublishKind(outModel.getPublishKind());
			articleResponse.setArticleImage(outModel.getArticleImage());
			articleResponse.setArticleImgSize(outModel.getArticleImgSize());
			articleResponse.setArticleId(outModel.getArticleId());
			articleResponse.setCreateUser(outModel.getCreateUser());
			articleResponse.setUsername(outModel.getUsername());
			articleResponse.setVideoUrl(outModel.getVideoUrl());
			mResponses.add(articleResponse);
		}
		return mResponses;
	}
}
