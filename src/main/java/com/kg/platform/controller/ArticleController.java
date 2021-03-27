package com.kg.platform.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.kg.platform.common.utils.HtmlUtil;
import com.kg.search.model.result.BaseResult;
import com.kg.search.service.ArticleSearchService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.CheckUtils;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.common.utils.sensitivefilter.SensitiveFilter;
import com.kg.platform.enumeration.ArticleAuditStatusEnum;
import com.kg.platform.enumeration.BonusStatusEnum;
import com.kg.platform.model.request.AccountFlowRequest;
import com.kg.platform.model.request.AccountRequest;
import com.kg.platform.model.request.ArticleRequest;
import com.kg.platform.model.request.KgArticleBonusRequest;
import com.kg.platform.model.request.UserCollectRequest;
import com.kg.platform.model.response.ArticleResponse;
import com.kg.platform.model.response.KgArticleBonusResponse;
import com.kg.platform.model.response.SitemapResponse;
import com.kg.platform.model.response.UpdownArticleResponse;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.AccountFlowService;
import com.kg.platform.service.AccountService;
import com.kg.platform.service.ArticlekgService;
import com.kg.platform.service.SensitiveWordsService;
import com.kg.platform.service.UsercollectService;

@Controller
@RequestMapping("article")
public class ArticleController extends ApiBaseController {

    @Inject
    ArticlekgService articlekgService;

    @Inject
    AccountService accountService;

    @Inject
    UsercollectService usercollectService;

    @Inject
    AccountFlowService accountFlowService;

    @Inject
    private SensitiveWordsService sensitiveWordsService;

    @Inject
    private ArticleSearchService articleSearchService;

    /**
     * 首页热门文章(√.)
     *
     * @param request
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("selectArticleAll")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ArticleRequest.class)
    public JsonEntity selectArticleAll(@RequestAttribute ArticleRequest request, PageModel<ArticleResponse> page, HttpServletRequest httpServletRequest) {
        page.setCurrentPage(request.getCurrentPage());
        page = articlekgService.selectArticleAll(request, page, httpServletRequest);
        if (page != null) {
            return JsonEntity.makeSuccessJsonEntity(page);
        }
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                ExceptionEnum.DATAERROR.getMessage());
    }

    @ResponseBody
    @RequestMapping("sitemap")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ArticleRequest.class)
    public JsonEntity sitemap(@RequestAttribute ArticleRequest request, PageModel<SitemapResponse> page) {
        page.setCurrentPage(request.getCurrentPage());
        if (null != request.getPageSize()) {
            page.setPageSize(request.getPageSize().intValue());
        }
        page = articlekgService.sitemap(request, page);
        if (page != null) {
            return JsonEntity.makeSuccessJsonEntity(page);
        }
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                ExceptionEnum.DATAERROR.getMessage());
    }

    /**
     * 频道页热门文章(√.)
     *
     * @param request
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("getChannelArt")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ArticleRequest.class)
    public JsonEntity getChannelArt(@RequestAttribute ArticleRequest request, PageModel<ArticleResponse> page, HttpServletRequest httpServletRequest) {
        page.setCurrentPage(request.getCurrentPage());
        page = articlekgService.getChannelArt(request, page, httpServletRequest);
        if (page != null) {
            return JsonEntity.makeSuccessJsonEntity(page);
        }
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                ExceptionEnum.DATAERROR.getMessage());
    }

    /**
     * 我的专栏文章(个人中心的文章列表不改变时间规则，但是返回了图片大小)
     *
     * @param request
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("getuserArticleAll")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ArticleRequest.class)
    public JsonEntity getUserArticleAll(@RequestAttribute ArticleRequest request, PageModel<ArticleResponse> page) {
        page.setCurrentPage(request.getCurrentPage());
        page = articlekgService.getUserArticleAll(request, page);
        if (page != null) {
            return JsonEntity.makeSuccessJsonEntity(page);
        }
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                ExceptionEnum.DATAERROR.getMessage());
    }

    /**
     * top排行榜(√.)
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("selecttoparticle")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ArticleRequest.class)
    public JsonEntity selectTopArticle() {
        Result<List<ArticleResponse>> listResponse = articlekgService.selectTopArticle();
        if (null != listResponse) {
            return JsonEntity.makeSuccessJsonEntity(listResponse.getData());
        }
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                ExceptionEnum.DATAERROR.getMessage());
    }

    /**
     * 资讯详情(√.)
     */
    @ResponseBody
    @RequestMapping("selectByiddetails")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ArticleRequest.class)
    public JsonEntity selectByIdDetails(@RequestAttribute ArticleRequest request,
                                        @RequestAttribute(required = false) UserkgResponse kguser) {

        logger.info("获取到的用户信息......." + JSON.toJSONString(kguser));
        if (kguser != null) {
            request.setUserId(kguser.getUserId());
        }

        Result<ArticleResponse> response = articlekgService.selectByIdDetails(request);
        response = buildDetail(response, articlekgService.getArticleDetailText(request));
        if (null != response && response.getData() != null) {
            CheckUtils.checkRetInfo(response, true);

            int publishStatus = response.getData().getPublishStatus();
            if (publishStatus != ArticleAuditStatusEnum.PASS.getStatus()) {
                return JsonEntity.makeExceptionJsonEntity("30002", "无法访问");
            }

            return JsonEntity.makeSuccessJsonEntity(response);
        }

        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SYS_EXCEPTION.getCode(),
                ExceptionEnum.SYS_EXCEPTION.getMessage());

    }

    private Result<ArticleResponse> buildDetail(Result<ArticleResponse> response, ArticleResponse articleDetailText) {
        ArticleResponse articleResponse = response.getData();
        if (articleResponse == null) {
            return null;
        }
        articleResponse.setArticleTitle(articleDetailText.getArticleTitle());
        articleResponse.setArticleDescription(articleDetailText.getArticleDescription());
        articleResponse.setArticleText(articleDetailText.getArticleText());
        return new Result<>(articleResponse);
    }

    /**
     * 感兴趣文章（√.）
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("relatedArticle")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ArticleRequest.class)
    public JsonEntity selectRelatedArticle(@RequestAttribute ArticleRequest request) {
        Result<List<ArticleResponse>> response = articlekgService.selectRelatedArticle(request);
        if (null != response) {
            return JsonEntity.makeSuccessJsonEntity(response);
        }
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                ExceptionEnum.DATAERROR.getMessage());
    }

    /**
     * 详情页上下文章（）
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("getUpdownArticle")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ArticleRequest.class)
    public JsonEntity UpdownArticle(@RequestAttribute ArticleRequest request) {
        Result<List<UpdownArticleResponse>> listresponse = articlekgService.UpdownArticle(request);

        if (null != listresponse) {
            return JsonEntity.makeSuccessJsonEntity(listresponse);
        }
        return JsonEntity.makeExceptionJsonEntity("", "获取数据有误！");
    }

    /**
     * 获取频道页右侧热门资讯(√.)
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("getChannelAll")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ArticleRequest.class)
    public JsonEntity getChannelAll(@RequestAttribute ArticleRequest request) {
        Result<List<ArticleResponse>> listresponse = articlekgService.getChannelAll(request);
        if (listresponse.getData() != null) {
            return JsonEntity.makeSuccessJsonEntity(listresponse);
        }
        return JsonEntity.makeExceptionJsonEntity("", "获取数据有误！");
    }

    /**
     * 前台发布文章。设置奖励
     *
     * @param request
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping("AddArticle")
    @BaseControllerNote(needCheckParameter = true, beanClazz = ArticleRequest.class)
    public JsonEntity AddArticle(@RequestAttribute ArticleRequest request,
                                 @RequestAttribute(required = false) UserkgResponse kguser) {

        if (null == request.getCreateUser()) {
            logger.info("获取到的用户信息.......", JSON.toJSONString(kguser));

            if (kguser != null && null != kguser.getUserId()) {
                request.setCreateUser(Long.parseLong(kguser.getUserId()));
            } else {
                return JsonEntity.makeExceptionJsonEntity("", "参数有误，请检查输入。");
            }
        }

        if (null != request.getList()) {
            /*
             * if (request.getCreateUser().longValue() != Constants.BIG_BOSS_ID)
             * { // 只有尚币哥账号才能设置随机奖励 for (KgArticleBonusRequest req :
             * request.getList()) { if (req.getBonusKind().intValue() == 2) {
             * return JsonEntity.makeExceptionJsonEntity("", "参数有误，请检查输入。"); } }
             * }
             */
            for (KgArticleBonusRequest req : request.getList()) {
                if (req.getBonusValue() != null && req.getBonusValue().compareTo(BigDecimal.valueOf(0.001)) < 0) {
                    return JsonEntity.makeExceptionJsonEntity("", "参数有误，请检查输入。");
                }
                if (req.getBonusKind() != null && req.getBonusKind().intValue() == 2) {
                    if (req.getBonusValue()
                            .divide(BigDecimal.valueOf(req.getMaxPeople().intValue()), 3, BigDecimal.ROUND_FLOOR)
                            .compareTo(BigDecimal.valueOf(0.001)) < 0) {
                        return JsonEntity.makeExceptionJsonEntity("", "参数有误，请检查输入。");
                    }
                }
            }
        }

        // 审核中、已通过的文章不能进任何编辑
        if (request.getArticleId() != null) {
            Result<ArticleResponse> result = articlekgService.getArticleContent(request);
            CheckUtils.checkRetInfo(result, true);
            int publishStatus = result.getData().getPublishStatus();
            if (publishStatus == ArticleAuditStatusEnum.AUDITING.getStatus()
                    || publishStatus == ArticleAuditStatusEnum.PASS.getStatus()) {
                return JsonEntity.makeExceptionJsonEntity("", "当前文章状态不能进行编辑");
            }
        }

        Result<SensitiveFilter> filterResult = sensitiveWordsService.getSensitiveFilter();
        if (CheckUtils.checkRetInfo(filterResult, true)) {
            SensitiveFilter filter = filterResult.getData();
            String words = filter.filter(request.getArticleTitle());
            if (StringUtils.isNotBlank(words)) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SENSITIVE_TITLE_ERROR.getCode(), words);
            }
            words = filter.filter(request.getArticleText());
            if (StringUtils.isNotBlank(words)) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SENSITIVE_CONTENT_ERROR.getCode(), words);
            }
            words = filter.filter(request.getArticleTagnames());
            if (StringUtils.isNotBlank(words)) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SENSITIVE_TAG_ERROR.getCode(), words);
            }
            words = filter.filter(request.getArticleDescription());
            if (StringUtils.isNotBlank(words)) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SENSITIVE_SUMMARY_ERROR.getCode(), words);
            }
            // words = filter.filter(request.getArticleLink());
            // if (StringUtils.isNotBlank(words)) {
            // return
            // JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SENSITIVE_LINK_ERROR.getCode(),
            // words);
            // }
            words = filter.filter(request.getArticleSource());
            if (StringUtils.isNotBlank(words)) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SENSITIVE_SOURCE_ERROR.getCode(), words);
            }

        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }

        Result<ArticleResponse> success = articlekgService.AddArticle(request);
        if (success != null) {

            return JsonEntity.makeSuccessJsonEntity(success.getData().getArticleId());
        }
        return JsonEntity.makeExceptionJsonEntity("", "保存失败");
    }

    /**
     * 前台修改发布文章
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("updateBySelective")
    @BaseControllerNote(needCheckParameter = true, beanClazz = ArticleRequest.class)
    public JsonEntity updateBySelective(@RequestAttribute ArticleRequest request) {

        boolean success = articlekgService.updateBySelective(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        }
        return JsonEntity.makeExceptionJsonEntity("", "保存失败");
    }

    /**
     * 搜索文章(√.)
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("getSearchArticle")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ArticleRequest.class)
    public JsonEntity getSearchArticle(@RequestAttribute ArticleRequest request, PageModel<ArticleResponse> page) {
        page.setCurrentPage(request.getCurrentPage());
        if (request.getPageSize() != null) {
            page.setPageSize(request.getPageSize());
        }
        page = articlekgService.searchArticleByEs(request, page);
        if (page != null) {
            return JsonEntity.makeSuccessJsonEntity(page);
        }
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                ExceptionEnum.DATAERROR.getMessage());
    }



    /**
     * 逻辑删除文章
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("updateArticle")
    @BaseControllerNote(needCheckParameter = true, beanClazz = ArticleRequest.class)
    public JsonEntity updateArticle(@RequestAttribute ArticleRequest request) {

        if (request.getArticleId() != null) {
            Result<ArticleResponse> result = articlekgService.getArticleContent(request);
            CheckUtils.checkRetInfo(result, true);
            int publishStatus = result.getData().getPublishStatus();
            if (publishStatus == ArticleAuditStatusEnum.AUDITING.getStatus()) {
                return JsonEntity.makeExceptionJsonEntity("", "当前文章状态不能删除");
            }

            List<KgArticleBonusResponse> articleBonus = result.getData().getListAllBonus();
            logger.info("======文章阅读奖励:{}", JSON.toJSONString(articleBonus));
            if (null != articleBonus && articleBonus.size() > 0) {
                for (KgArticleBonusResponse abr : articleBonus) {
                    int bonusStatus = abr.getBonusStatus().intValue();
                    if (bonusStatus == BonusStatusEnum.VALID.getStatus()
                            || bonusStatus == BonusStatusEnum.PAUSED.getStatus()) {
                        return JsonEntity.makeExceptionJsonEntity("30001",
                                "系统检测到您还有已生效/暂停中的阅读奖励，请终止奖励规则后，再删除文章，以免对您造成资产损失。");
                    }
                }
            }

            boolean success = articlekgService.updateArticle(request);
            if (success) {
                return JsonEntity.makeSuccessJsonEntity("删除成功");
            }
        }

        return JsonEntity.makeExceptionJsonEntity("", "删除失败");
    }

    /**
     * 获取文章内容(√.)
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("getArticleContent")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ArticleRequest.class)
    public JsonEntity getArticleContent(@RequestAttribute ArticleRequest request) {
        Result<ArticleResponse> result = articlekgService.getArticleContent(request);
        if (result.getData() != null) {
            return JsonEntity.makeSuccessJsonEntity(result);
        }
        return JsonEntity.makeExceptionJsonEntity("", "获取数据失败");
    }

    /**
     * 文章分享奖励
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("checkShareArticle")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = AccountRequest.class)
    public JsonEntity checkShareArticle(@RequestAttribute AccountRequest request) {
        // 查询文章分享奖励是否已被领取
        AccountFlowRequest accountFlowRequest = new AccountFlowRequest();
        accountFlowRequest.setUserId(request.getUserId());
        accountFlowRequest.setArticleId(request.getArticleId());
        Integer tips = accountFlowService.getShareBonusStatusCount(accountFlowRequest);
        // 查询是否是登陆状态自己在阅读
        if (request.getBrowseUserId() != null && (request.getBrowseUserId() == request.getUserId())) {
            logger.info("分享给自己在阅读");
            return null;
        }
        if (tips != null && tips >= 1) {
            logger.info("分享文章奖励已被领取");
            return null;
        }
        // 查询分享者是否当天获得分享奖励超过三次
        tips = accountFlowService.getShareBonusRecords(accountFlowRequest);
        if (tips != null && tips > 3) {
            logger.info("当天获得分享奖励超过三次");
            return null;
        }
        UserCollectRequest userCollectRequest = new UserCollectRequest();
        userCollectRequest.setArticleId(request.getArticleId());
        userCollectRequest.setUserId(request.getUserId());
        // 查询分享者是否分享过
        logger.info(">>>>>>>>>" + usercollectService.getShareStatus(userCollectRequest));
        if (!usercollectService.getShareStatus(userCollectRequest)) {
            logger.info("分享者未分享过该文章");
            return null;
        }
        // 查询文章是否阅读过
        if (!usercollectService.getBrowseStatus(userCollectRequest)) {
            logger.info("文章未读过");
            return null;
        }
        accountService.shareBonus(request);
        return JsonEntity.makeSuccessJsonEntity("检查成功");
    }


    /**
     * 百科词条
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("encyclopediaList")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = false)
    public JsonEntity encyclopediaList() {
        return articlekgService.encyclopediaList();
    }

    @ResponseBody
    @RequestMapping("getVideoTabInfo")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ArticleRequest.class)
    public JsonEntity getVideoTabInfo(@RequestAttribute ArticleRequest request, PageModel<ArticleResponse> page, HttpServletRequest servletRequest) {
        return articlekgService.getVideoTabInfo(request, page, servletRequest);
    }

	@ResponseBody
	@RequestMapping("getArticleForKS")
	@BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ArticleRequest.class)
	public JsonEntity getArticleForKS(@RequestAttribute ArticleRequest request) {
    	if(request.getCurrentPage()==0){
    		request.setCurrentPage(1);
		}
		if(request.getPageSize()==null){
    		request.setPageSize(15);
		}
    	int pageIndex = (request.getCurrentPage()-1) * request.getPageSize();
		BaseResult baseResult = articleSearchService.getArticleForKS(request.getType(), pageIndex, request.getPageSize());
		return JsonEntity.makeSuccessJsonEntity(baseResult.getResponseBody());
	}
}
