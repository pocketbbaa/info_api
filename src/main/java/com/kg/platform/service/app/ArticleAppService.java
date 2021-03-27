package com.kg.platform.service.app;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.request.AccountFlowRequest;
import com.kg.platform.model.request.AccountRequest;
import com.kg.platform.model.request.ArticleRequest;
import com.kg.platform.model.request.UserCollectRequest;
import com.kg.platform.model.request.UserCommentRequest;
import com.kg.platform.model.response.ArticleAppResponse;
import com.kg.platform.model.response.ArticleDetailAppResponse;
import com.kg.platform.model.response.ArticleResponse;
import com.kg.platform.model.response.UserCommentResponse;
import com.kg.platform.model.response.UserkgResponse;

public interface ArticleAppService {

    /**
     * 获取用户的评论列表
     *
     * @param request
     * @param page
     * @return
     */
    PageModel<UserCommentResponse> getUserCommentForApp(UserCommentRequest request,
            PageModel<UserCommentResponse> page);

    /**
     * 获取推荐咨询列表
     *
     * @param request
     * @param page
     * @return
     */
    PageModel<ArticleAppResponse> selectArticleAppAll(ArticleRequest request, PageModel<ArticleAppResponse> page);

    /**
     * 获取资讯详情信息
     *
     * @param request
     * @return
     */
    ArticleDetailAppResponse getDetailById(ArticleRequest request, HttpServletRequest servletRequest);

    /**
     * 获取内容缓存
     * 
     * @param inModel
     * @return
     */
    ArticleDetailAppResponse getArticleDetailText(ArticleRequest request);

    /**
     * 专栏我的文章
     *
     * @param request
     * @param page
     * @return
     */
    PageModel getUserArticleAll(ArticleRequest request, PageModel page, HttpServletRequest servletRequest);

    /**
     * 添加评论返回评论ID
     * 
     * @param request
     * @return
     */
    Long addComment(UserCommentRequest request);

    /**
     * 资讯推荐
     * 
     * @param request
     * @return
     */
    Result<List<ArticleResponse>> recommendForYou(ArticleRequest request);


    /**
     * 咨询推荐1.2
     *
     * @param request
     * @return
     */
    Map  recommendArticle(ArticleRequest request, HttpServletRequest servletRequest);

    /**
     * 插入mongo浏览 分享奖励进入
     */
    void addCollectBonusToMongo(UserCollectRequest request);

    /**
     * 插入分享奖励哦
     * 
     * @param kguser
     * @param request
     * @param servletRequest
     */
    void platformReward(UserkgResponse kguser, AccountRequest request, HttpServletRequest servletRequest);

    /**
     * 验证当前分享是否超过6次
     *
     */
    int getShareCount(AccountFlowRequest accountFlowRequest);

    /**
     * 资讯奖励收入
     *
     * @param userId
     * @return
     */
    String getArticleAmount(Long userId, Long articleId);

}
