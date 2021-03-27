package com.kg.platform.controller.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.aop.Cacheable;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.JsonUtil;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.dao.read.KgCommonSettingRMapper;
import com.kg.platform.enumeration.DateEnum;
import com.kg.platform.model.in.KgCommonSettingInModel;
import com.kg.platform.model.out.KgCommonSettingOutModel;
import com.kg.platform.model.request.*;
import com.kg.platform.model.response.*;
import com.kg.platform.service.ArticlekgService;
import com.kg.platform.service.KeywordsService;
import com.kg.platform.service.app.AppAdverService;
import com.kg.platform.service.app.HomePageService;
import com.kg.platform.service.app.UserConcernService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 首页相关
 * <p>
 * by wangyang 2018/03/20
 */
@Controller
@RequestMapping("/kgApp/homePage")
public class HomePageController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final int BANNER_TYPE = 1; // 首页

    @Autowired
    private ArticlekgService articlekgService;

    @Autowired
    private HomePageService homePageService;

    @Autowired
    private KeywordsService keywordsService;

    @Autowired
    private KgCommonSettingRMapper kgCommonSettingRMapper;

    @Autowired
    private AppAdverService appAdverService;

    @Autowired
    private UserConcernService userConcernService;

    /**
     * banner推荐位
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getBanner", method = RequestMethod.POST)
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = SiteimageAppRequest.class)
    public JSONObject getBanner(@RequestAttribute SiteimageAppRequest request, HttpServletRequest servletRequest) {
        log.info("【/kgApp/homePage/getBanner】: request params = {}", JSONObject.toJSON(request));
        request.setNavigator_pos(BANNER_TYPE);
        Result<List<SiteimageResponse>> rlist = homePageService.getAllColumn(request, servletRequest);
        if (rlist.getData() != null) {
            log.info("【/kgApp/homePagegetBanner】: response = {}", JSONObject.toJSON(rlist.getData()));
            return AppJsonEntity.jsonFromObject(AppJsonEntity.makeSuccessJsonEntity(rlist.getData()));
        }
        log.info("【/kgApp/homePagegetBanner】: rlist.getData() == null !!!");
        return AppJsonEntity.jsonFromObject(AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR));
    }

    /**
     * 文章搜索(√.)
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/searchArticle")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ArticleRequest.class)
    public AppJsonEntity searchArticle(@RequestAttribute ArticleRequest request, PageModel<ArticleAppResponse> page) {
        log.info("【/kgApp/homePage/searchArticle】: request params - {}", JSONObject.toJSONString(request));
        page.setCurrentPage(request.getCurrentPage());
        page = homePageService.getSearchArticle(request, page);
        if (page != null) {
            log.info("【/kgApp/homePage/searchArticle】: response success ...");
            return AppJsonEntity.makeSuccessJsonEntity(page);
        }
        log.info("【/kgApp/homePage/searchArticle】: page == null !!!");
        return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
    }

    /**
     * 1.3.8 搜索(√.)
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/search")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = SearchRequest.class)
    public AppJsonEntity search(@RequestAttribute SearchRequest request) {
        log.info("【/kgApp/homePage/search】: request params - {}", JSONObject.toJSONString(request));

        if (request == null || request.getType() == null || StringUtils.isEmpty(request.getSearchStr())) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }

        int type = request.getType();
        if (type == SearchRequest.USER) {
            UserConcernRequest userConcernRequest = new UserConcernRequest();
            userConcernRequest.setSearchStr(request.getSearchStr());
            userConcernRequest.setCurrentPage(request.getCurrentPage());
            userConcernRequest.setPageSize(request.getPageSize());
            Long userId = request.getUserId();
            if (userId != null) {
                userConcernRequest.setUserId(userId);
            }
            PageModel<UserConcernListResponse> page = new PageModel<>();
            PageModel<UserConcernListResponse> pageModel = userConcernService.searchAuthorList(userConcernRequest, page);
            if (pageModel != null) {
                return AppJsonEntity.makeSuccessJsonEntity(page);
            }
        }
        PageModel<ArticleAppResponse> page = homePageService.getSearchArticleWihtES(request);
        if (page != null) {
            log.info("【/kgApp/homePage/searchArticle】: response success ...");
            return AppJsonEntity.makeSuccessJsonEntity(page);
        }
        log.info("【/kgApp/homePage/searchArticle】: page == null !!!");
        return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
    }

    /**
     * 获取首页顶部栏目导航列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/getTopMenus")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = false)
    public JSONObject getTopMenus() {
        return AppJsonEntity.jsonFromObject(homePageService.getTopMenus());
    }

    /**
     * 【v1.1.0以前的版本接口】 栏目/标签文章列表 【v1.1.0以前的版本接口】(√.) 栏目/标签文章列表
     *
     * @param request
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("getArticlesWithType")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ArticleRequest.class)
    public JSONObject getArticlesWithType(@RequestAttribute ArticleRequest request, PageModel<ArticleResponse> page,
                                          HttpServletRequest httpServletRequest) {
        page.setCurrentPage(request.getCurrentPage());
        if (request.getPageSize() != null) {
            page.setPageSize(request.getPageSize());
        }
        if (StringUtils.isEmpty(request.getColumnId())) {
            // 如果tagname不为空，返回标签结果
            if (StringUtils.isNotEmpty(request.getArticleTagnames())) {
                page = articlekgService.getChannelArt(request, page, httpServletRequest);
                return AppJsonEntity.jsonFromObject(AppJsonEntity.makeSuccessJsonEntity(page));
            }
            // 查询首页推荐文章
            page = articlekgService.selectArticleRecomm(request, page, httpServletRequest);
            if (page != null) {
                return AppJsonEntity.jsonFromObject(AppJsonEntity.makeSuccessJsonEntity(page));
            }
        } else {
            page = articlekgService.getChannelArt(request, page, httpServletRequest);
            if (page != null) {
                return AppJsonEntity.jsonFromObject(AppJsonEntity.makeSuccessJsonEntity(page));
            }
        }
        return AppJsonEntity.jsonFromObject(AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                ExceptionEnum.DATAERROR.getMessage()));
    }

    /**
     * 【v1.1.0->当前的版本接口】(√.)
     * <p>
     * 栏目/标签文章列表
     *
     * @param request
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("/V2/getArticlesWithType")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ArticleRequest.class)
    public JSONObject getV2ArticlesWithType(@RequestAttribute ArticleRequest request, PageModel<ArticleResponse> page,
                                            HttpServletRequest httpServletRequest) {
        if ("-2".equals(request.getColumnId()) && StringUtils.isEmpty(request.getUserId())) {
            return AppJsonEntity.jsonFromObject(AppJsonEntity.makeExceptionJsonEntity(
                    ExceptionEnum.PARAMEMPTYERROR.getCode(), ExceptionEnum.PARAMEMPTYERROR.getMessage()));
        }
        page.setCurrentPage(request.getCurrentPage());
        if (request.getPageSize() != null) {
            page.setPageSize(request.getPageSize());
        }
        if (StringUtils.isEmpty(request.getColumnId())) {
            // 如果tagname不为空，返回标签结果
            if (StringUtils.isNotEmpty(request.getArticleTagnames())) {
                return AppJsonEntity.jsonFromObject(homePageService.getChannelArt(request, page, httpServletRequest));
            }
            // 查询首页推荐文章
            AppJsonEntity res = homePageService.selectArticleRecomm(request, page, httpServletRequest);
            Map map = (Map) res.getData();

            UserkgRequest userkgRequest = new UserkgRequest();
            if (org.apache.commons.lang3.StringUtils.isNotBlank(request.getUserId())) {
                userkgRequest.setUserId(Long.valueOf(request.getUserId()));
            }
            if (page.getCurrentPage() == 1) {
                // 第一页才查询热门作者信息
                List<UserProfileResponse> userProfileResponses = homePageService.hotAuthorInfo(userkgRequest);

                // 查询用户关联关系
                if (StringUtils.isNotEmpty(request.getUserId())) {
                    log.info("json参数:" + JsonUtil.writeValueAsString(userProfileResponses));
                    List<UserProfileResponse> ups = JsonUtil.readJson(JsonUtil.writeValueAsString(userProfileResponses),
                            List.class, UserProfileResponse.class);
                    userProfileResponses = homePageService.getConnectInfo(ups, Long.valueOf(request.getUserId()));
                }
                map.put("hotAuthorList", userProfileResponses);

                // 获取热门作者在APP首页的插入位置
                KgCommonSettingInModel inModel = new KgCommonSettingInModel();
                inModel.setSettingKey(HotAuthorInfoResponse.STTING_KEY);
                KgCommonSettingOutModel outModel = kgCommonSettingRMapper.selectBySettingKey(inModel);

                HotAuthorInfoResponse hotAuthorInfoResponse = JSON.parseObject(outModel.getSettingValue(),
                        HotAuthorInfoResponse.class);
                map.put("hotAuthorListLocation", hotAuthorInfoResponse.getHotAuthorLocation());
            }

            // 获取广告信息
            SiteimageRequest siteimageAppRequest = new SiteimageRequest();
            siteimageAppRequest.setCurrentPage(page.getCurrentPage());
            siteimageAppRequest.setPageSize(4);
            siteimageAppRequest.setNavigator_pos(1);
            siteimageAppRequest.setImage_pos(12);
            siteimageAppRequest.setDisPlayPort(2);
            siteimageAppRequest.setOsVersion(httpServletRequest.getIntHeader("os_version"));
            map.put("advers", appAdverService.getRecommendAdvs(siteimageAppRequest));

            return AppJsonEntity.jsonFromObject(AppJsonEntity.makeSuccessJsonEntity(map));
        } else {
            return AppJsonEntity.jsonFromObject(homePageService.getChannelArt(request, page, httpServletRequest));
        }
    }

    /**
     * 获取热门搜索词
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("getkeywords")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = KeywordsRequest.class)
    public JSONObject getkeywords(@RequestAttribute KeywordsRequest request) {
        log.info("【/kgApp/homePage/getkeywords】: request params = {}", request);
        Result<List<KeywordsResponse>> listresponse = keywordsService.getHotSearch(request);
        if (null != listresponse.getData()) {
            log.info("【/kgApp/homePage/getkeywords】: response success ... ");
            return AppJsonEntity.jsonFromObject(AppJsonEntity.makeSuccessJsonEntity(listresponse));
        }
        log.info("【/kgApp/homePage/getkeywords】: listresponse.getData() == null !!!");
        return AppJsonEntity.jsonFromObject(AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR));
    }

    @ResponseBody
    @RequestMapping("ifHaveNotRead")
    @BaseControllerNote(needCheckToken = true)
    public JSONObject ifHaveNotRead(@RequestAttribute UserkgResponse kguser) {
        return AppJsonEntity.jsonFromObject(homePageService.ifHaveNotRead(kguser));
    }

    @ResponseBody
    @RequestMapping("getPushedInfo")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = PageModel.class)
    public JSONObject getPushedInfo(@RequestAttribute UserkgResponse kguser, @RequestAttribute PageModel request) {
        return AppJsonEntity.jsonFromObject(homePageService.getPushedInfo(kguser, request));
    }

    @ResponseBody
    @RequestMapping("getVideoTabInfo")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ArticleRequest.class)
    public JSONObject getVideoTabInfo(@RequestAttribute ArticleRequest request, PageModel<ArticleResponse> page, HttpServletRequest servletRequest) {
        return AppJsonEntity.jsonFromObject(homePageService.getVideoTabInfo(request, page, servletRequest));
    }
}
