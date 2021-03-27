package com.kg.platform.service;

import java.math.BigInteger;
import java.util.List;

import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.in.ArticleInModel;
import com.kg.platform.model.request.ArticleRequest;
import com.kg.platform.model.request.BaseRequest;
import com.kg.platform.model.response.ArticleResponse;
import com.kg.platform.model.response.SitemapResponse;
import com.kg.platform.model.response.UpdownArticleResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * 热门作者service
 *
 * @author think
 */
public interface ArticlekgService {
    /**
     * 前台发布文章
     *
     * @param request
     * @return
     */

    Result<ArticleResponse> AddArticle(ArticleRequest request);

    /**
     * 获取热门文章
     *
     * @param request
     * @param page
     * @return
     */
    PageModel<ArticleResponse> selectArticleAll(ArticleRequest request, PageModel<ArticleResponse> page, HttpServletRequest httpServletRequest);

    /**
     * 获取热门文章(翻页重复修复方案)
     *
     * @param request
     * @param page
     * @return
     */
    PageModel<ArticleResponse> selectArticleRecomm(ArticleRequest request, PageModel<ArticleResponse> page, HttpServletRequest httpServletRequest);

    ArticleInModel getArticleInModel(HttpServletRequest httpServletRequest, PageModel<ArticleResponse> page,
                                     ArticleInModel inModel);

    PageModel<SitemapResponse> sitemap(ArticleRequest request, PageModel<SitemapResponse> page);

    /**
     * 搜索文章
     */

    PageModel<ArticleResponse> getSearchArticle(ArticleRequest request, PageModel<ArticleResponse> page);

    /**
     * 文章搜索Es
     *
     * @param request
     * @param page
     * @return
     */
    PageModel<ArticleResponse> searchArticleByEs(ArticleRequest request, PageModel<ArticleResponse> page);

    /**
     * 频道页热门作者，栏目的热门文章 ，共用一个接口，专栏下的文章传入文章ID
     */
    PageModel<ArticleResponse> getChannelArt(ArticleRequest request, PageModel<ArticleResponse> page, HttpServletRequest httpServletRequest);

    /**
     * 24小时咨询
     *
     * @return
     */
    Result<List<ArticleResponse>> selectAdvisory();

    /**
     * top排行榜
     *
     * @return
     */
    Result<List<ArticleResponse>> selectTopArticle();

    /**
     * 资讯正文
     *
     * @param request
     * @return
     */
    ArticleResponse getArticleDetailText(ArticleRequest request);

    /**
     * 咨询详情
     *
     * @return
     */

    Result<ArticleResponse> selectByIdDetails(ArticleRequest request);

    /**
     * 感兴趣的文章
     *
     * @param request
     * @return
     */
    Result<List<ArticleResponse>> selectRelatedArticle(ArticleRequest request);

    /**
     * 上下两篇文章
     *
     * @param request
     * @return
     */
    Result<List<UpdownArticleResponse>> UpdownArticle(ArticleRequest request);

    /**
     * 专栏我的文章
     *
     * @param request
     * @param page
     * @return
     */
    PageModel<ArticleResponse> getUserArticleAll(ArticleRequest request, PageModel<ArticleResponse> page);

    /**
     * 二级频道页右侧 热门资讯
     *
     * @param request
     * @return
     */
    Result<List<ArticleResponse>> getChannelAll(ArticleRequest request);

    /**
     * 前台修改文章
     *
     * @param request
     * @return
     */
    boolean updateBySelective(ArticleRequest request);

    /**
     * 逻辑删除文章
     *
     * @param request
     * @return
     */
    boolean updateArticle(ArticleRequest request);

    /**
     * 查文章详情
     *
     * @param request
     * @return
     */
    Result<ArticleResponse> getArticleContent(ArticleRequest request);

    /**
     * 百科词条
     *
     * @return
     */
    JsonEntity encyclopediaList();

    /**
     * 首页视频
     *
     * @param request
     * @param page
     * @return
     */
    JsonEntity getVideoTabInfo(ArticleRequest request, PageModel<ArticleResponse> page, HttpServletRequest servletRequest);

    /**
     * 获取用户浏览对应文章列表的总量
     *
     * @param servletRequest
     * @param currentPage
     * @param columnId
     * @param count
     * @return
     */
    long getArticleColumnTotalCount(HttpServletRequest servletRequest, int currentPage, String columnId, long count);

    /**
     * 获取查询真实开始下标
     *
     * @param totalCount
     * @param pageSize
     * @return
     */
    int getArticleRecommStartIndex(int currentPage, int totalCount, int pageSize);

    /**
     * 发送统计栏目下文章总量MQ
     *
     * @param columnId
     */
    void pushCountArticleMq(String columnId);

    PageModel<ArticleResponse> buildSelectArticleAllResponse(PageModel<ArticleResponse> page, long totalCount, Integer types);

    PageModel<ArticleResponse> buildSelectArticleRecomm(PageModel<ArticleResponse> page, long totalCount, Integer types);

    PageModel<ArticleResponse> buildGetChannelArt(PageModel<ArticleResponse> page, long totalCount, ArticleRequest request, ArticleInModel inModel, Integer types);

    /**
     * 获取资讯hashcode
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    List<BigInteger> getArticleSimHashCode(BaseRequest request);

    /**
     * 获取HashCode总数
     *
     * @return
     */
    int getSimHashTotal();
}
