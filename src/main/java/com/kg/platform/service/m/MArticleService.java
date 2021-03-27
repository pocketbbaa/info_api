package com.kg.platform.service.m;

import com.kg.platform.common.entity.MJsonEntity;
import com.kg.platform.model.request.ArticleRequest;
import com.kg.platform.model.request.KgNewsFlashRequest;
import com.kg.platform.model.request.UserCommentRequest;
import com.kg.platform.model.request.UserProfileRequest;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by Administrator on 2018/10/11.
 */
public interface MArticleService {

	/**
	 * 首页推荐文章列表
	 * @return
	 */
	MJsonEntity selectArticleRecomm(ArticleRequest request);

	/**
	 * 获取首页顶部栏目导航列表
	 */
	MJsonEntity getTopMenus();

	/**
	 * 搜索文章
	 * @param request
	 * @return
	 */
	MJsonEntity searchArticle(ArticleRequest request);

	/**
	 * 获取热门搜索词
	 * @return
	 */
	MJsonEntity getHotSearch();

	/**
	 * 获取栏目文章列表
	 * @return
	 */
	MJsonEntity getChannelArt(ArticleRequest request);

	/**
	 * 根据类型获取24H快讯
	 * @param request
	 * @return
	 */
	MJsonEntity getNewsFlashListByType(KgNewsFlashRequest request);

	/**
	 * 查看文章详情
	 * @param request
	 * @return
	 */
	MJsonEntity detailArticle(ArticleRequest request);

	/**
	 * 为你推荐
	 * @param request
	 * @return
	 */
	MJsonEntity recommendForYou(ArticleRequest request);

	/**
	 * 文章评论列表
	 * @param request
	 * @return
	 */
	MJsonEntity getCommentArtAll(UserCommentRequest request);

	/**
	 * 我的专栏我的文章
	 * @param request
	 * @return
	 */
	MJsonEntity getUserArticleAll(ArticleRequest request);

	HttpServletRequest buildMHttpRequest(String userIp);
}
