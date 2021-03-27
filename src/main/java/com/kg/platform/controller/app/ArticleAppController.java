package com.kg.platform.controller.app;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.kg.platform.common.utils.*;
import com.kg.platform.model.request.*;
import com.kg.platform.model.response.*;
import com.kg.platform.service.*;
import com.kg.platform.service.app.AppAdverService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.aop.Cacheable;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.sensitivefilter.SensitiveFilter;
import com.kg.platform.enumeration.CommentAuditStatusEnum;
import com.kg.platform.enumeration.CommentStatusEnum;
import com.kg.platform.enumeration.DateEnum;
import com.kg.platform.model.UserTagBuild;
import com.kg.platform.service.app.ArticleAppService;
import org.wltea.analyzer.filter.Filter;

/**
 * 资讯相关
 * <p>
 * by wangyang 2018/03/21
 */
@Controller
@RequestMapping("/kgApp/article")
public class ArticleAppController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    private ArticlekgService articlekgService;

    @Inject
    private UserCommentService commentService;

    @Inject
    private SiteinfoService siteinfoService;

    @Inject
    private SensitiveWordsService sensitiveWordsService;

    @Inject
    private ArticleAppService articleAppService;

    @Inject
    private UsercollectService usercollectService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PlatformBonusService platformBonusService;

    @Autowired
    private UserTagsUtil userTagsUtil;

    @Autowired
    private AppAdverService appAdverService;

    @Autowired
    protected JedisUtils jedisUtils;

    /**
     * 获取推荐咨询列表
     *
     * @param request
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("/recommendArticles")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, isLogin = false, beanClazz = ArticleRequest.class)
    public AppJsonEntity recommendArticles(@RequestAttribute ArticleRequest request,
                                           PageModel<ArticleAppResponse> page) {
        if (request == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        page.setCurrentPage(request.getCurrentPage());
        page.setPageSize(request.getPageSize());
        page = articleAppService.selectArticleAppAll(request, page);
        if (page != null) {
            return AppJsonEntity.makeSuccessJsonEntity(page);
        }
        return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
    }

    /**
     * 资讯详情（√.）
     */
    @ResponseBody
    @RequestMapping("detailArticle")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ArticleRequest.class)
    public AppJsonEntity detailArticle(@RequestAttribute ArticleRequest request, HttpServletRequest servletRequest) {
        setBrowKey(request);
        ArticleDetailAppResponse articleDetailAppResponse = buildArticleDetailAppResponse(request, servletRequest);
        if (null != articleDetailAppResponse) {
            return AppJsonEntity.makeSuccessJsonEntity(articleDetailAppResponse);
        }
        return AppJsonEntity.makeExceptionJsonEntity("10003", "无文章信息");
    }


    /**
     * 为你推荐V1.0.0
     *
     * @param request
     * @return
     */
    @Cacheable(category = "/kgApp/article/recommendForYou", key = "#{request.articleId}", expire = 2, dateType = DateEnum.HOURS)
    @ResponseBody
    @RequestMapping("recommendForYou")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ArticleRequest.class)
    public JSONObject recommendForYou(@RequestAttribute ArticleRequest request) {
        Result<List<ArticleResponse>> response = articlekgService.selectRelatedArticle(request);
        if (null != response) {
            List<ArticleResponse> articleResponseList = response.getData();
            if (CollectionUtils.isNotEmpty(articleResponseList)) {
                if (articleResponseList.size() > 3) {
                    articleResponseList = articleResponseList.subList(0, 3);
                }
                return AppJsonEntity.jsonFromObject(AppJsonEntity.makeSuccessJsonEntity(articleResponseList));
            }
        }
        return AppJsonEntity.jsonFromObject(AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR));
    }

    /**
     * 为你推荐V1.0.1(√.)
     *
     * @param request
     * @return
     */
    @Cacheable(category = "/kgApp/article/articleRecommend", key = "#{request.articleId}", expire = 2, dateType = DateEnum.HOURS)
    @ResponseBody
    @RequestMapping("articleRecommend")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ArticleRequest.class)
    public JSONObject articleRecommend(@RequestAttribute ArticleRequest request) {
        Result<List<ArticleResponse>> response = articleAppService.recommendForYou(request);
        if (response != null) {
            return AppJsonEntity.jsonFromObject(AppJsonEntity.makeSuccessJsonEntity(response.getData()));
        }
        return AppJsonEntity.jsonFromObject(AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR));
    }


    /**
     * 为你推荐V1.2.0(√.)
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/v12/articleRecommend")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ArticleRequest.class)
    public JSONObject articleRecomm(@RequestAttribute ArticleRequest request, HttpServletRequest servletRequest) {
        Map response = articleAppService.recommendArticle(request, servletRequest);
        if (response != null) {
            return AppJsonEntity.jsonFromObject(AppJsonEntity.makeSuccessJsonEntity(response));
        }
        return AppJsonEntity.jsonFromObject(AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR));
    }


    /**
     * 文章评论列表
     *
     * @param request
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("getComments")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserCommentRequest.class)
    public AppJsonEntity getComments(@RequestAttribute UserCommentRequest request,
                                     PageModel<UserCommentResponse> page) {
        if (null == request.getArticleId()) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        page.setCurrentPage(request.getCurrentPage());
        page.setPageSize(request.getPageSize());
        page = commentService.getCommentArtAll(request, page);
        if (page != null) {
            return AppJsonEntity.makeSuccessJsonEntity(page);
        }
        return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);

    }

    /**
     * 发表评论
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("commentArticle")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, isLogin = true, beanClazz = UserCommentRequest.class)
    public AppJsonEntity commentArticle(@RequestAttribute UserCommentRequest request,
                                        @RequestAttribute UserkgResponse kguser) {
        log.info("[发表评论] 入参 ：request：" + JSONObject.toJSONString(request) + "  kguser:" + JSONObject.toJSONString(kguser));
        Result<SiteinfoResponse> response = siteinfoService.selectSiteInfo();
        if (response == null || response.getData() == null || kguser == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        request.setUserId(kguser.getUserId());
        try {
            if (response.getData().getCommentAudit() == CommentAuditStatusEnum.NEED.getStatus()) {
                request.setCommentStatus(CommentStatusEnum.WAIT.getStatus());
            } else {
                request.setCommentStatus(CommentStatusEnum.AUDIT.getStatus());
            }
            if (StringUtils.isEmpty(request.getCommentDesc())) {
                return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(), "评论不能为空");
            }
            if (request.getCommentDesc().length() > 500) {
                return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(), "评论内容不能超过500个字");
            }
            org.wltea.analyzer.result.Result<List<String>> filterResult = Filter.doFilter(request.getCommentDesc());
            if (filterResult.getCode().equals("100")) {
                List<String> words = filterResult.getData();
                return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.SENSITIVE_COMMENT_ERROR.getCode(), words.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (null == request.getUserId() || null == request.getArticleId()) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        Long commentId = articleAppService.addComment(request);
        if (commentId != null && commentId > 0) {
            if (request.getCommentStatus() == CommentStatusEnum.WAIT.getStatus()) {
                return AppJsonEntity.makeExceptionJsonEntity("10030", "评论成功，需要审核，请耐心等待");
            }
            Map<String, Object> map = new HashMap<>();
            Long authorId = commentService.getAuthorId(Long.valueOf(request.getArticleId()));
            UserTagBuild userTagBuild = userTagsUtil.buildTags(Long.valueOf(kguser.getUserId()));
            map.put("commentId", String.valueOf(commentId));
            map.put("userTagBuild", userTagBuild);
            if (Objects.equals(authorId, Long.valueOf(kguser.getUserId()))) {
                map.put("isAuthor", 1);
            } else {
                map.put("isAuthor", 0);
            }
            return AppJsonEntity.makeSuccessJsonEntity(map);
        }
        return AppJsonEntity.makeExceptionJsonEntity("", "失败");

    }

    /**
     * 获取对应资讯发布者的资讯列表(√.)
     *
     * @param request
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("getArticlesWithUserId")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ArticleRequest.class)
    public AppJsonEntity getArticlesWithUserId(@RequestAttribute ArticleRequest request,
                                               PageModel page, HttpServletRequest servletRequest) {
        page.setCurrentPage(request.getCurrentPage());
        page = articleAppService.getUserArticleAll(request, page, servletRequest);
        if (page != null) {
            return AppJsonEntity.makeSuccessJsonEntity(page);
        }
        return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
    }

    /**
     * 我的评论列表
     *
     * @param request
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("getCommentsWithUserId")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserCommentRequest.class)
    public AppJsonEntity getCommentsWithUserId(@RequestAttribute UserCommentRequest request,
                                               PageModel<UserCommentResponse> page) {
        if (null == request.getUserId()) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        page.setCurrentPage(request.getCurrentPage());
        page = articleAppService.getUserCommentForApp(request, page);
        if (page != null) {
            return AppJsonEntity.makeSuccessJsonEntity(page);
        }
        return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);

    }

    /**
     * 保存点赞收藏分享浏览
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("addCollect")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserCollectRequest.class)
    public JSONObject addCollect(@RequestAttribute UserCollectRequest request, HttpServletRequest httpServletRequest) {
        request.setSource(2);
        int osVersion = httpServletRequest.getIntHeader("os_version");
        request.setOsVersion(osVersion);
        String deviceId = httpServletRequest.getHeader("device_id");
        request.setDeviceId(deviceId);
        Integer operType = request.getOperType();

        // 校验用户是否已点赞或已收藏
        if (operType != null) {
            if (operType == 1 || operType == 2) {
                Result<Integer> result = usercollectService.getCollectByUserIdAndArticleId(request);
                if (result != null) {
                    if (result.isOk()) {
                        String message = "你已点过赞";
                        if (result.getData() == 1) {
                            message = "你已收藏";
                        }
                        return AppJsonEntity.jsonFromObject(AppJsonEntity.makeExceptionJsonEntity("", message));
                    }
                }
            }
        }

        if (request.getOperType() != null && request.getOperType() == 3) {
            log.info("-------------- mongo没登录记录平台发文奖励分享记录奖励----------------");
            UserCollectRequest userCollectRequest = new UserCollectRequest();
            userCollectRequest.setDeviceId(deviceId);
            userCollectRequest.setOperType(3);
            userCollectRequest.setArticleId(request.getArticleId());
            if (request.getUserId() != null) {
                Long userId = Long.valueOf(request.getUserId());
                userCollectRequest.setUserId(userId);
            }
            userCollectRequest.setOsVersion(osVersion);
            articleAppService.addCollectBonusToMongo(userCollectRequest);
        }

        BigDecimal success = usercollectService.addCollect(request);
        Map<String, Object> map = new HashMap<>();
        map.put("rewardNum", success);
        map.put("rewardCoinType", "TV");
        return AppJsonEntity.jsonFromObject(AppJsonEntity.makeSuccessJsonEntity(map));

    }

    /**
     * 发放文章点赞、收藏、分享、浏览奖励
     *
     * @param request
     * @return
     */
    // app老版本阅读奖励接口
    @ResponseBody
    @RequestMapping("updateUserbalance")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = AccountRequest.class)
    public AppJsonEntity updateUserbalance(@RequestAttribute AccountRequest request,
                                           @RequestAttribute UserkgResponse kguser, HttpServletRequest servletRequest) {
        if (request == null || kguser == null) {
            log.info("updateUserbalance -> request == null || kguser == null !!!");
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        request.setUserId(Long.valueOf(kguser.getUserId()));
        if (request.getBonusType() != null && "4".equals(request.getBonusType())) {
            articleAppService.platformReward(kguser, request, servletRequest);
        }

        log.info("【发放文章点赞、收藏、分享、浏览奖励】-> 传入参数：" + JSONObject.toJSONString(request));
        BigDecimal success = accountService.updateUserbalance(request);
        log.info("【发放文章点赞、收藏、分享、浏览奖励】 -> 返回值:{}", success);
        Map<String, Object> map = new HashMap<>();
        map.put("rewardNum", 0);
        map.put("rewardCoinType", "TV");
        return AppJsonEntity.makeSuccessJsonEntity(map);
    }

    /**
     * 资讯详情组合
     *
     * @param request
     * @return
     */
    private ArticleDetailAppResponse buildArticleDetailAppResponse(ArticleRequest request,
                                                                   HttpServletRequest servletRequest) {


        ArticleDetailAppResponse articleDetailAppResponse = articleAppService.getDetailById(request, servletRequest);
        if (articleDetailAppResponse == null) {
            return null;
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
        articleDetailAppResponse.setHaveShareBonus(0);
        String articleId = articleDetailAppResponse.getArticleId();
        // 验证当前分享是否超过6次
        if (StringUtils.isNotEmpty(request.getUserId()) && StringUtils.isNotEmpty(articleId)) {
            AccountFlowRequest accountFlowRequest = new AccountFlowRequest();
            accountFlowRequest.setArticleId(request.getArticleId());
            accountFlowRequest.setUserId(Long.valueOf(request.getUserId()));
//            int shareCount = articleAppService.getShareCount(accountFlowRequest);
            articleDetailAppResponse.setHaveShareBonus(1);
        }

        //获取广告详情 需要指定投放的作者
        SiteimageRequest siteimageRequest = new SiteimageRequest();
        siteimageRequest.setNavigator_pos(2);
        siteimageRequest.setImage_pos(21);
        siteimageRequest.setCurrentPage(1);
        siteimageRequest.setPageSize(1);
        siteimageRequest.setDisPlayPort(2);
        siteimageRequest.setUserId(articleDetailAppResponse.getCreateUserId());
        siteimageRequest.setOsVersion(servletRequest.getIntHeader("os_version"));
        List<AdverResponse> s = appAdverService.getAdvs(siteimageRequest);
        articleDetailAppResponse.setAdvers(s);

        return articleDetailAppResponse;
    }

    /**
     * 阅读奖励
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("readBonus")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = AccountRequest.class)
    public AppJsonEntity readBonus(@RequestAttribute AccountRequest request, HttpServletRequest servletRequest) {
//        if (request.getArticleId() == null) {
//            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
//        }
//        UserCollectRequest userCollectRequest = new UserCollectRequest();
//        BigDecimal b = BigDecimal.ZERO;
//        int osVersion = servletRequest.getIntHeader("os_version");
//        if (request.getUserId() != null) {
//            if (!checkBrowKey(String.valueOf(request.getUserId()), String.valueOf(request.getArticleId()), osVersion)) {
//                log.info("访问阅读奖励接口过快/没有浏览文章直接获取奖励，无法获取奖励 request：" + JSONObject.toJSONString(request));
//                return AppJsonEntity.makeExceptionJsonEntity("", "无法获取奖励，访问阅读奖励接口过快/没有浏览文章直接获取奖励");
//            }
//            log.info("--------------发放平台阅读奖励----------------");
//            userCollectRequest.setUserId(request.getUserId());
//            AccountRequest accountRequest = new AccountRequest();
//            accountRequest.setUserId(request.getUserId());
//            accountRequest.setArticleId(request.getArticleId());
//            b = platformBonusService.getReadBonus(accountRequest);
//            if (b == null) {
//                b = BigDecimal.ZERO;
//            }
//        }
//        Map<String, Object> map = new HashMap<>();
//        map.put("readbonus", b);
//        map.put("readBonusUnit", "KG");
//        return AppJsonEntity.makeSuccessJsonEntity(map);
        return AppJsonEntity.makeExceptionJsonEntity("", "获取奖励已达上限");
    }

    /**
     * 添加平台分享奖励
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("shareBonus")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = AccountRequest.class)
    public AppJsonEntity shareBonus(@RequestAttribute AccountRequest request, HttpServletRequest servletRequest) {
        if (request == null) {
            log.info("updateUserbalance -> request == null");
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        articleAppService.platformReward(null, request, servletRequest);
        return AppJsonEntity.makeSuccessJsonEntity("");
    }


    /**
     * 设置浏览key
     *
     * @param request
     */
    private void setBrowKey(ArticleRequest request) {
        String userId = request.getUserId();
        Long articleId = request.getArticleId();
        if (StringUtils.isEmpty(userId) || articleId == null || articleId < 0) {
            return;
        }
        log.info("【设置浏览key】 userId:" + userId + "  articleId：" + articleId);
        jedisUtils.set(JedisKey.brownKey(userId, String.valueOf(articleId)), new Date().getTime());
    }

    /**
     * 校验浏览key
     *
     * @param userId
     * @param articleId
     * @return
     */
    private boolean checkBrowKey(String userId, String articleId, int osVersion) {
        if (osVersion != 1 && osVersion != 2) {
            return false;
        }
        String value = jedisUtils.get(JedisKey.brownKey(userId, articleId));
        log.info("【校验浏览key】 value:" + value + "  userId:" + userId + "  articleId:" + articleId);
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        if (value.contains("\"")) {
            value = value.replace("\"", "");
        }
        long time = Long.valueOf(value);
        long now = new Date().getTime();
        log.info("【校验浏览key】 browtTime:" + time + "   nowTime:" + now);
        if ((now - time) < 4 * 1000) {
            jedisUtils.del(JedisKey.brownKey(userId, articleId));
            return false;
        }
        return true;
    }

}
