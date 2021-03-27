package com.kg.platform.service.app;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.MJsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.request.ArticleRequest;
import com.kg.platform.model.request.SearchRequest;
import com.kg.platform.model.request.SiteimageAppRequest;
import com.kg.platform.model.request.UserkgRequest;
import com.kg.platform.model.response.ArticleAppResponse;
import com.kg.platform.model.response.ArticleResponse;
import com.kg.platform.model.response.SiteimageResponse;
import com.kg.platform.model.response.UserProfileResponse;
import com.kg.platform.model.response.UserkgResponse;

/**
 * Created by Administrator on 2018/3/22.
 */
public interface HomePageService {
    /**
     * 获取首页顶部栏目导航列表
     */
    AppJsonEntity getTopMenus();

	/**
	 * 获取M站顶部栏目导航列表
	 * @return
	 */
	MJsonEntity getMTopMenus();
    /**
     * 搜索文章
     *
     * @param request
     * @param page
     * @return
     */
    PageModel<ArticleAppResponse> getSearchArticle(ArticleRequest request, PageModel<ArticleAppResponse> page);

    PageModel<ArticleAppResponse> getSearchArticleWihtES(SearchRequest request);

    /**
     * 获取banner推荐位
     *
     * @param request
     * @return
     */
    Result<List<SiteimageResponse>> getAllColumn(SiteimageAppRequest request,HttpServletRequest servletRequest);

    /**
     * 是否有未读推送消息
     * 
     * @return
     */
    AppJsonEntity ifHaveNotRead(UserkgResponse kguser);

    /**
     * 获取历史推送消息列表
     *
     * @param kguser
     * @param page
     * @return
     */
    AppJsonEntity getPushedInfo(UserkgResponse kguser, PageModel page);

    /**
     * APP首页热门作者
     *
     * @return
     */
    List<UserProfileResponse> hotAuthorInfo(UserkgRequest request);

    /**
     * 获取热门文章(翻页重复修复方案)
     *
     * @param request
     * @param page
     * @return
     */
    AppJsonEntity selectArticleRecomm(ArticleRequest request, PageModel<ArticleResponse> page,
            HttpServletRequest httpServletRequest);

    /**
     *
     * 频道页热门作者，栏目的热门文章 ，共用一个接口，专栏下的文章传入文章ID
     */
    AppJsonEntity getChannelArt(ArticleRequest request, PageModel<ArticleResponse> page,HttpServletRequest httpServletRequest);

    /**
     * 获取视频底部TAB列表
     * 
     * @return
     */
    AppJsonEntity getVideoTabInfo(ArticleRequest request, PageModel<ArticleResponse> page,HttpServletRequest servletRequest);

    /**
     * 获取热门作者关注信息
     * 
     * @return
     */
    List<UserProfileResponse> getConnectInfo(List<UserProfileResponse> l, Long userId);

    AppJsonEntity buildGetVideoTabInfo(PageModel<ArticleResponse> page,long totalCount,Integer types);

}
