package com.kg.platform.controller.admin;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.CheckUtils;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.common.utils.sensitivefilter.SensitiveFilter;
import com.kg.platform.dao.entity.Article;
import com.kg.platform.enumeration.ArticleAuditStatusEnum;
import com.kg.platform.model.request.admin.*;
import com.kg.platform.model.response.PushArticleResponse;
import com.kg.platform.model.response.admin.ArticleQueryResponse;
import com.kg.platform.service.SensitiveWordsService;
import com.kg.platform.service.admin.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController("AdminArticleController")
@RequestMapping("admin/article")
public class ArticleController extends AdminBaseController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private SensitiveWordsService sensitiveWordsService;


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true)); // true:允许输入空值，false:不能为空值

    }

    /**
     * 文章列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "getArticleList", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = ArticleQueryRequest.class)
    public JsonEntity getArticleList(@RequestAttribute ArticleQueryRequest request) {
        PageModel<ArticleQueryResponse> page = new PageModel<>();
        page.setCurrentPage(request.getCurrentPage());
        page.setPageSize(request.getPageSize());
        page = articleService.getArticleList(request, page);
        return JsonEntity.makeSuccessJsonEntity(page);
    }

    @RequestMapping(value = "setDisplayOrder", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = DisplayOrderRequest.class)
    public JsonEntity setDisplayOrder(@RequestAttribute DisplayOrderRequest request) {
        if (null != request.getArticleId() && null != request.getDisplayOrder()) {
            boolean success = articleService.setDisplayOrder(request.getArticleId(), request.getDisplayOrder());
            if (success) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                        ExceptionEnum.SUCCESS.getMessage());
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                        ExceptionEnum.DATAERROR.getMessage());
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

    @RequestMapping(value = "auditArticle", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = AuditArticleRequest.class)
    public JsonEntity auditArticle(@RequestAttribute AuditArticleRequest request) {
        if (null != request.getArticleId() && null != request.getAuditUser() && null != request.getAuditStatus()) {
            Result<SensitiveFilter> filterResult = sensitiveWordsService.getSensitiveFilter();
            if (CheckUtils.checkRetInfo(filterResult, true)) {
                SensitiveFilter filter = filterResult.getData();
                Article article = articleService.getArticleById(request.getArticleId());
                String words = filter.filter(article.getArticleTitle());
                if (StringUtils.isNotBlank(words)) {
                    return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SENSITIVE_TITLE_ERROR.getCode(), words);
                }
                String articleText = article.getArticleText();
                if (StringUtils.isNotEmpty(articleText)) {
                    words = filter.filter(articleText.replaceAll("</?[a-zA-Z]+[^><]*>", "")); //正文做HTML标签过滤
                    if (StringUtils.isNotBlank(words)) {
                        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SENSITIVE_CONTENT_ERROR.getCode(), words);
                    }
                }
                words = filter.filter(article.getTagnames());
                if (StringUtils.isNotBlank(words)) {
                    return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SENSITIVE_TAG_ERROR.getCode(), words);
                }
                words = filter.filter(article.getArticleDescription());
                if (StringUtils.isNotBlank(words)) {
                    return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SENSITIVE_SUMMARY_ERROR.getCode(), words);
                }
                words = filter.filter(article.getArticleSource());
                if (StringUtils.isNotBlank(words)) {
                    return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SENSITIVE_SOURCE_ERROR.getCode(), words);
                }

                boolean success = articleService.auditArticle(request.getArticleId(), request.getAuditUser(),
                        request.getColumnId(), request.getSecondColumn(), request.getAuditStatus(),
                        request.getRefuseReason());
                if (success) {
                    return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                            ExceptionEnum.SUCCESS.getMessage());
                } else {
                    return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                            ExceptionEnum.DATAERROR.getMessage());
                }
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                        ExceptionEnum.PARAMEMPTYERROR.getMessage());
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

    @RequestMapping(value = "setDisplayStatus", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = DisplayStatusRequest.class)
    public JsonEntity setDisplayStatus(@RequestAttribute DisplayStatusRequest request) {
        if (!StringUtils.isBlank(request.getArticleId()) && null != request.getUpdateUser()
                && null != request.getDisplayStatus()) {
            boolean success = articleService.setDisplayStatus(request.getArticleId(), request.getUpdateUser(),
                    request.getDisplayStatus());
            if (success) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                        ExceptionEnum.SUCCESS.getMessage());
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                        ExceptionEnum.PARAMEMPTYERROR.getMessage());
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

    @RequestMapping(value = "setBlockchainUrl", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = BlockchainUrlRequest.class)
    public JsonEntity setBlockchainUrl(@RequestAttribute BlockchainUrlRequest request) {
        if (StringUtils.isNotBlank(request.getArticleId())) {
            boolean success = articleService.setBlockchainUrl(request.getArticleId(), request.getBlockchainUrl());
            if (success) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                        ExceptionEnum.SUCCESS.getMessage());
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(), "上链失败");
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

    @RequestMapping(value = "publishArticle", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = ArticleEditRequest.class)
    public JsonEntity publishArticle(@RequestAttribute ArticleEditRequest request) {
        if (request.getPublishStatus() != 0 || request.getPublishSet() == 1) {
            if (StringUtils.isBlank(request.getArticleTitle())) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                        ExceptionEnum.PARAMEMPTYERROR.getMessage());
            }
            String articleTitle = request.getArticleTitle();
            if (StringUtils.isEmpty(articleTitle) || articleTitle.length() > 200) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                        "文章标题过长，建议不要超过200字");
            }
            if (StringUtils.isBlank(request.getArticleText())) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                        ExceptionEnum.PARAMEMPTYERROR.getMessage());
            }
            if (StringUtils.isBlank(request.getTagnames())) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                        ExceptionEnum.PARAMEMPTYERROR.getMessage());
            }
            if (StringUtils.isBlank(request.getImage())) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                        ExceptionEnum.PARAMEMPTYERROR.getMessage());
            }
            if (null == request.getType()) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                        ExceptionEnum.PARAMEMPTYERROR.getMessage());
            }
            if (null == request.getColumnId()
                    && request.getPublishStatus() != ArticleAuditStatusEnum.REFUSE.getStatus()) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                        ExceptionEnum.PARAMEMPTYERROR.getMessage());
            }
            // if(null == request.getSecondColumn()){
            // return
            // JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
            // ExceptionEnum.PARAMEMPTYERROR.getMessage());
            // }
            if (null == request.getDisplayStatus()) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                        ExceptionEnum.PARAMEMPTYERROR.getMessage());
            }
            if (null == request.getCommentSet()) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                        ExceptionEnum.PARAMEMPTYERROR.getMessage());
            }
            if (null == request.getCreateUser()) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                        ExceptionEnum.PARAMEMPTYERROR.getMessage());
            }
            Result<SensitiveFilter> filterResult = sensitiveWordsService.getSensitiveFilter();
            if (CheckUtils.checkRetInfo(filterResult, true)) {
                SensitiveFilter filter = filterResult.getData();
                String words = filter.filter(request.getArticleTitle());
                if (StringUtils.isNotBlank(words)) {
                    return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SENSITIVE_TITLE_ERROR.getCode(), words);
                }
                String articleText = request.getArticleText();
                if (StringUtils.isNotEmpty(articleText)) {
                    words = filter.filter(articleText.replaceAll("</?[a-zA-Z]+[^><]*>", "")); //正文做HTML标签过滤
                    if (StringUtils.isNotBlank(words)) {
                        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SENSITIVE_CONTENT_ERROR.getCode(), words);
                    }
                }
                words = filter.filter(request.getTagnames());
                if (StringUtils.isNotBlank(words)) {
                    return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SENSITIVE_TAG_ERROR.getCode(), words);
                }
                words = filter.filter(request.getDescription());
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
                String articleId = articleService.publishArticle(request);
                if (null != articleId) {
                    return JsonEntity.makeSuccessJsonEntity(articleId);
                } else {
                    return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                            ExceptionEnum.PARAMEMPTYERROR.getMessage());
                }
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                        ExceptionEnum.PARAMEMPTYERROR.getMessage());
            }
        } else {
            String articleId = articleService.publishArticle(request);
            if (null != articleId) {
                return JsonEntity.makeSuccessJsonEntity(articleId);
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                        ExceptionEnum.PARAMEMPTYERROR.getMessage());
            }
        }
    }

    @RequestMapping("deleteArticle")
    @BaseControllerNote(beanClazz = DeleteArticleRequest.class)
    public JsonEntity deleteArticle(@RequestAttribute DeleteArticleRequest request) {
        if (null != request.getArticleId()) {
            boolean success = articleService.deleteArticle(request);
            if (success) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                        ExceptionEnum.SUCCESS.getMessage());
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                        ExceptionEnum.PARAMEMPTYERROR.getMessage());
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

    @RequestMapping("getArticleById")
    @BaseControllerNote(beanClazz = ArticleQueryRequest.class)
    public JsonEntity getArticleById(@RequestAttribute ArticleQueryRequest request) {
        if (!StringUtils.isBlank(request.getArticleId())) {
            Article article = articleService.getArticleById(Long.parseLong(request.getArticleId()));
            if (null != article) {
                return JsonEntity.makeSuccessJsonEntity(article);
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                        ExceptionEnum.PARAMEMPTYERROR.getMessage());
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

    @RequestMapping(value = "getBonus", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = ArticleEditRequest.class)
    public JsonEntity getBonus(@RequestAttribute ArticleEditRequest request) {
        return JsonEntity.makeSuccessJsonEntity(articleService.getBonus(request));
    }

    @RequestMapping(value = "getArticleStat", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = ArticleEditRequest.class)
    public JsonEntity getArticleStat(@RequestAttribute ArticleEditRequest request) {
        return JsonEntity.makeSuccessJsonEntity(articleService.getArticleStat(request));
    }

    /**
     * 冻结发文奖励
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "freezePublishBonus")
    @BaseControllerNote(beanClazz = ArticleQueryRequest.class)
    public JsonEntity freezePublishBonus(@RequestAttribute ArticleQueryRequest request) {
        if (request.getArticleId() == null || request.getAdminId() == null || request.getPublishStatus() == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        Boolean isok = articleService.freezePublishBonus(request);
        if (isok) {
            return JsonEntity.makeSuccessJsonEntity("冻结成功");
        }
        return JsonEntity.makeSuccessJsonEntity("冻结失败");
    }

    /**
     * 优质文章标记
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "markHighQualityArticles")
    @BaseControllerNote(beanClazz = ArticleQueryRequest.class)
    public JsonEntity markHighQualityArticles(@RequestAttribute ArticleQueryRequest request) {
        if (request.getArticleId() == null || request.getArticleMark() == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        Boolean isok = articleService.markHighQualityArticles(request);
        if (isok) {
            return JsonEntity.makeSuccessJsonEntity("优质文章标记成功");
        }
        return JsonEntity.makeSuccessJsonEntity("优质文章标记失败");
    }

//    /**
//     * 文章额外奖励
//     *
//     * @param request
//     * @return
//     */
//    @RequestMapping(value = "addedBonus")
//    @BaseControllerNote(beanClazz = ArticleQueryRequest.class)
//    public JsonEntity addedBonus(@RequestAttribute ArticleQueryRequest request, HttpServletRequest servletRequest) {
//        if (request == null && request.getArticleId() == null || request.getBonusType() == null
//                || request.getBonus().compareTo(BigDecimal.ZERO) < 0 || request.getAdminId() == null) {
//            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
//                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
//        }
//        logger.info("验证是否是当前ip在访问");
//        Result<SiteinfoResponse> siteinfoResponse = siteinfoService.selectSiteInfo();
//        CheckUtils.checkRetInfo(siteinfoResponse, true);
//        SiteinfoResponse siteInfo = siteinfoResponse.getData();
//        if (!Check.NuNString(siteInfo.getLimitIp())) {
//            String userIp = HttpUtil.getIpAddr(servletRequest);
//            logger.info("====发放来源ip" + userIp);
//            if (siteInfo.getLimitIp().indexOf(userIp) == -1) {
//                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.ADMINIPERROR.getCode(),
//                        ExceptionEnum.ADMINIPERROR.getMessage());
//            }
//        }
//        // 关闭氪金奖励 只发放钛值奖励
//        request.setBonusType(0);
//
//        // 判断文章是否已经有额外奖励
//        Boolean isOk = articleService.getArticleAddedTvBonus(request);
//
//        Boolean isOk1 = articleService.getArticleAddedTxbBonus(request);
//
//        if (isOk || isOk1) {
//            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.RE_ADDED_BONUS_ERROR.getCode(),
//                    ExceptionEnum.RE_ADDED_BONUS_ERROR.getMessage());
//        }
//
//        // 抓取 后台编辑上传的文章无法获取额外奖励
//        Article article = articleService.getArticleById(Long.valueOf(request.getArticleId()));
//        if (article.getSysUser() != null || article.getArticleFrom() != 1) {
//            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.ADDED_EX_BONUS_ERROR.getCode(),
//                    ExceptionEnum.ADDED_EX_BONUS_ERROR.getMessage());
//        }
//        // 判断钛值奖励是否超过50tv  1.2需求
//        if (request.getBonusType() == 0) {
//            if (request.getBonus().compareTo(Constants.MAXTVBOUNS) > 0) {
//                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.MAX_TV_BONUS_ERROR.getCode(),
//                        ExceptionEnum.MAX_TV_BONUS_ERROR.getMessage());
//            }
//        }
//        // 判断氪金奖励是否超过500kg
//        else if (request.getBonusType() == 1) {
//            if (request.getBonus().compareTo(Constants.MAXTXBBOUNS) > 0) {
//                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.MAX_TXB_BONUS_ERROR.getCode(),
//                        ExceptionEnum.MAX_TXB_BONUS_ERROR.getMessage());
//            }
//        } else {
//            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
//                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
//        }
//
//        Boolean isok = articleService.addedBonus(request);
//        if (isok) {
//            return JsonEntity.makeSuccessJsonEntity("额外奖励发放成功");
//        }
//        return JsonEntity.makeSuccessJsonEntity("额外奖励发放失败");
//    }

    @ResponseBody
    @RequestMapping("/getPushAticleInfo")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = false, isLogin = true)
    public JsonEntity getPushAticleInfo() {
        PushArticleResponse pushArticleResponse = articleService.getPushAticleInfo();
        return JsonEntity.makeSuccessJsonEntity(pushArticleResponse);
    }


    /**
     * 批量审核
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/batchReview")
    @BaseControllerNote(beanClazz = ArticleBatchReviewRequest.class)
    public JsonEntity batchReviewArticle(@RequestAttribute ArticleBatchReviewRequest request) {
        try {
            return articleService.batchReviewArticle(request);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonEntity.makeExceptionJsonEntity("", "批量审核失败" + e.getMessage());
        }
    }

    @RequestMapping(value = "getArticles", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = ArticleQueryRequest.class)
    public JsonEntity getArticles(@RequestAttribute ArticleQueryRequest request) {
        if (request == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if (StringUtils.isEmpty(request.getArticleTitle())) {
            return JsonEntity.makeSuccessJsonEntity("");
        }
        List<ArticleQueryResponse> l = articleService.getArticles(request);
        return JsonEntity.makeSuccessJsonEntity(l);
    }
}
