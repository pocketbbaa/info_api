package com.kg.platform.service.app.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.kg.platform.common.aop.Cacheable;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.MJsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.mongo.Seach;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.common.utils.JedisKey;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.common.utils.JsonUtil;
import com.kg.platform.dao.entity.PushMessage;
import com.kg.platform.dao.read.*;
import com.kg.platform.enumeration.DateEnum;
import com.kg.platform.model.in.ArticleInModel;
import com.kg.platform.model.in.KgArticleStatisticsInModel;
import com.kg.platform.model.in.UserConcernInModel;
import com.kg.platform.model.in.UserProfileInModel;
import com.kg.platform.model.out.*;
import com.kg.platform.model.request.*;
import com.kg.platform.model.response.*;
import com.kg.platform.service.ArticlekgService;
import com.kg.platform.service.app.AppAdverService;
import com.kg.platform.service.app.HomePageService;
import com.kg.search.model.result.BaseResult;
import com.kg.search.service.ArticleSearchService;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Administrator on 2018/3/22.
 */
@Service
public class HomePageServiceImpl implements HomePageService {
    private static final Logger logger = LoggerFactory.getLogger(HomePageServiceImpl.class);

    @Autowired
    private ColumnRMapper columnRMapper;

    @Autowired
    private ArticleRMapper articleRMapper;

    @Autowired
    private KgArticleStatisticsRMapper kgArticleStatisticsRMapper;

    @Autowired
    private UserRMapper userRMapper;

    @Autowired
    private UserProfileRMapper userProfileRMapper;

    @Autowired
    private ArticlekgService articlekgService;

    @Autowired
    private UserConcernRMapper userConcernRMapper;

    @Autowired
    private AppAdverService appAdverService;

    @Autowired
    private JedisUtils jedisUtils;

    @Autowired
    private ArticleSearchService articleSearchService;

    @Autowired
    private UserCommentRMapper userCommentRMapper;

    @Autowired
    private KgArticleExtendRMapper kgArticleExtendRMapper;

    private void getMenus(List<ColumnResponse> responses) {

        List<ColumnOutModel> fistColumn = columnRMapper.appSelectFistColumn();
        List<ColumnOutModel> secondColumn = columnRMapper.appSelectSecondColumn();
        for (ColumnOutModel first : fistColumn) {
            ColumnResponse columnResponse = new ColumnResponse();
            columnResponse.setColumnId(first.getColumnId());
            columnResponse.setColumnName(first.getColumnName());
            columnResponse.setColumnUrlName(first.getColumnUrlName());
            responses.add(columnResponse);
        }
        for (ColumnOutModel second : secondColumn) {
            ColumnResponse response = new ColumnResponse();
            response.setColumnId(second.getColumnId());
            response.setPrevColumn(second.getPrevColumn());
            response.setColumnName(second.getColumnName());
            response.setColumnUrlName(second.getColumnUrlName());
            responses.add(response);
        }
    }

    /**
     * ????????????????????????????????????
     *
     * @return
     */
    @Override
    @Cacheable(category = "/kgApp/homePage/getTopMenus", key = "", expire = 60, dateType = DateEnum.SECONDS)
    public AppJsonEntity getTopMenus() {

        List<ColumnOutModel> fistColumn = columnRMapper.appSelectFistColumn();
        List<ColumnOutModel> secondColumn = columnRMapper.appSelectSecondColumn();
        List<ColumnResponse> responses = new ArrayList<>();

        ColumnResponse res = new ColumnResponse();
        res.setColumnId(-2);
        res.setColumnName("??????");
        responses.add(res);

        res = new ColumnResponse();
        res.setColumnId(-1);
        res.setColumnName("??????");
        responses.add(res);

        getMenus(responses);
        /*
         * for (ColumnOutModel first : fistColumn) { for (ColumnOutModel second
         * : secondColumn) { if (first.getColumnId().intValue() ==
         * second.getPrevColumn().intValue()) {
         * 
         * ColumnResponse response = new ColumnResponse();
         * response.setColumnId(second.getColumnId());
         * response.setPrevColumn(second.getPrevColumn());
         * response.setColumnName(second.getColumnName());
         * responses.add(response); } } ColumnResponse columnResponse = new
         * ColumnResponse(); columnResponse.setColumnId(first.getColumnId());
         * columnResponse.setColumnName(first.getColumnName());
         * responses.add(columnResponse); }
         */
        Map<String, Object> map = new HashMap<>();
        map.put("indexColumns", responses);
        return AppJsonEntity.makeSuccessJsonEntity(map);
    }

    /**
     * ??????M???????????????????????????
     *
     * @return
     */
    @Override
    @Cacheable(category = "/kgApp/homePage/getMTopMenus", key = "", expire = 60, dateType = DateEnum.SECONDS)
    public MJsonEntity getMTopMenus() {
        List<ColumnResponse> responses = Lists.newArrayList();
        ColumnResponse columnResponse = new ColumnResponse();
        columnResponse.setColumnId(-1);
        columnResponse.setColumnName("??????");
        getMenus(responses);
        Map<String, Object> map = new HashMap<>();
        map.put("indexColumns", responses);
        return MJsonEntity.makeSuccessJsonEntity(map);
    }

    /**
     * ????????????,DB
     */
    @Cacheable(category = "/kgApp/homePage/searchArticle", key = "#{request.articleTitle}_#{request.currentPage}", expire = 60, dateType = DateEnum.SECONDS)
    @Override
    public PageModel<ArticleAppResponse> getSearchArticle(ArticleRequest request, PageModel<ArticleAppResponse> page) {
        logger.info("?????????????????????ArticleRequest={}", JSON.toJSONString(request));
        ArticleInModel inModel = new ArticleInModel();
        inModel.setStart((page.getCurrentPage() - 1) * page.getPageSize());
        inModel.setLimit(page.getPageSize());
        inModel.setArticleTitle(request.getArticleTitle());
        List<ArticleOutModel> listArticle = articleRMapper.getSearchArticleForApp(inModel);
        List<ArticleAppResponse> listrResponse = buildArticleResponseList(listArticle);
        long count = articleRMapper.SearchCount(inModel);
        page.setData(listrResponse);
        page.setTotalNumber(count);
        return page;
    }

    /**
     * ????????????,ES
     */
    @Cacheable(category = "/kgApp/homePage/search", key = "#{request.searchStr}_#{request.type}_#{request.currentPage}", expire = 60, dateType = DateEnum.SECONDS)
    @Override
    public PageModel<ArticleAppResponse> getSearchArticleWihtES(SearchRequest request) {
        logger.info("?????????????????????ArticleRequest={}", JSON.toJSONString(request));
        PageModel<ArticleAppResponse> page = new PageModel<>();
        int pageIndex = (page.getCurrentPage() - 1) * page.getPageSize();
        int pageSize = page.getPageSize();
        BaseResult result = articleSearchService.searchArticle(request.getSearchStr(), request.getType(), pageIndex, pageSize);
        SearchHits hits = (SearchHits) result.getResponseBody();
        List<ArticleAppResponse> listrResponse = buildSearchArticleResponseList(hits);
        page.setData(listrResponse);
        page.setTotalNumber(hits.getTotalHits());
        return page;
    }

    @Override
    public Result<List<SiteimageResponse>> getAllColumn(SiteimageAppRequest request, HttpServletRequest servletRequest) {
        // SiteimageInModel model = new SiteimageInModel();
        // model.setNavigator_pos(request.getNavigator_pos());
        // model.setOs_version(servletRequest.getIntHeader("os_version"));
        // List<SiteimageOutModel> list = siteimageRMapper.selectAllForApp(model);
        SiteimageRequest siteimageRequest = new SiteimageRequest();
        siteimageRequest.setNavigator_pos(1);
        siteimageRequest.setImage_pos(11);
        siteimageRequest.setPageSize(5);
        siteimageRequest.setCurrentPage(1);
        siteimageRequest.setDisPlayPort(2);
        siteimageRequest.setOsVersion(servletRequest.getIntHeader("os_version"));
        List<AdverResponse> list = appAdverService.getAdvs(siteimageRequest);
        logger.info(">>>>>>>>>>>>>>>>>>>>" + JsonUtil.writeValueAsString(list));
        if (CollectionUtils.isEmpty(list)) {
            return new Result<>(new ArrayList<>());
        }
        List<SiteimageResponse> responsesList = new ArrayList<>();
        // ??????????????????????????????banner???
        if (request.getColumnId() != null && request.getColumnId().equals(-1)) {
            responsesList = buildSiteimageResponseList(list);
        }
        if (CollectionUtils.isNotEmpty(responsesList)) {
            if (responsesList.size() > 5) {
                return new Result<>(responsesList.subList(0, 5));
            }
        }
        return new Result<>(responsesList);
    }

    @Override
    public AppJsonEntity ifHaveNotRead(UserkgResponse kguser) {
        Integer[] type = new Integer[]{1, 2};
        List<String> receive = new ArrayList<>();
        receive.add(kguser.getUserId());
        BasicDBObject basicBSONObject = new BasicDBObject("receive", receive).append("readState", 0).append("type",
                new BasicDBObject(Seach.IN.getOperStr(), type));
        MongoCursor cursor = MongoUtils.findByFilter(PushMessage.mongoTable, basicBSONObject);
        Map<String, Object> map = new HashMap<>();
        if (cursor.hasNext()) {
            map.put("ifHave", 1);
            return AppJsonEntity.makeSuccessJsonEntity(map);
        } else {
            map.put("ifHave", 0);
            return AppJsonEntity.makeSuccessJsonEntity(map);
        }
    }

    @Override
    public AppJsonEntity getPushedInfo(UserkgResponse kguser, PageModel page) {
        Integer[] type = new Integer[]{1, 2};
        List<String> receive = new ArrayList<>();
        receive.add(kguser.getUserId());
        BasicDBObject basicDBObject = new BasicDBObject("receive", receive).append("type",
                new BasicDBObject(Seach.IN.getOperStr(), type));
        BasicDBObject sort = new BasicDBObject("createDate", -1);
        MongoCursor<Document> cursor = MongoUtils.findByPage(PushMessage.mongoTable, basicDBObject, sort,
                page.getCurrentPage(), page.getPageSize());
        List<PushMessageResponse> responses = new ArrayList<>();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            PushMessageResponse response = new PushMessageResponse();
            response.setMessage_id(document.getLong("_id").toString());
            response.setMessageText(document.getString("messageText"));
            response.setCreateDate(DateUtils.calculateTime(new Date(document.getLong("createDate"))));
            response.setCreateDateTimestamp(document.getLong("createDate").toString());
            response.setReadState(document.getInteger("readState"));
            response.setTitle(document.getString("title"));
            response.setClassify(document.getInteger("secondClassify"));
            response.setMessageAvatar(document.getString("messageAvatar"));
            String remark = document.getString("remark");
            if (StringUtils.isNotEmpty(remark)) {
                JSONObject jsonObject = JSON.parseObject(remark);
                if (jsonObject != null) {
                    Integer pushType = jsonObject.getInteger("type");
                    response.setType(pushType);
                }
                response.setRemark(remark);
            }
            responses.add(response);
        }
        // ???????????????????????????
        BasicDBObject filter = new BasicDBObject("readState", 0).append("receive", receive).append("type",
                new Document(Seach.IN.getOperStr(), type));
        MongoUtils.updateByFilter(PushMessage.mongoTable, filter, new Document("readState", 1));
        Map<String, Object> map = new HashMap<>();
        map.put("pushMessageList", responses);
        return AppJsonEntity.makeSuccessJsonEntity(map);
    }

    @Cacheable(key = "hotAuthorInfo", expire = 1, dateType = DateEnum.HOURS)
    @Override
    public List<UserProfileResponse> hotAuthorInfo(UserkgRequest request) {
        UserProfileInModel userProfileInModel = new UserProfileInModel();
        userProfileInModel.setUserId(request.getUserId());
        List<UserProfileOutModel> list = userProfileRMapper.getAppUserproFile(userProfileInModel);

        List<UserProfileOutModel> listnum = null;
       /* if (list.size() < 10) {
            userProfileInModel.setLimit(10 - list.size());
            listnum = userProfileRMapper.getAppBowsnum(userProfileInModel);
        }*/

        List<UserProfileResponse> listresponse = new ArrayList<UserProfileResponse>();
        for (UserProfileOutModel userProfileOutModel : list) {
            UserProfileResponse response = new UserProfileResponse();
            response.setUserId(userProfileOutModel.getUserId().toString());
            response.setCreateUser(userProfileOutModel.getUserId().toString());
            response.setColumnName(userProfileOutModel.getColumnName());
            response.setColumnAvatar(userProfileOutModel.getColumnAvatar());
            response.setColumnIntro(userProfileOutModel.getColumnintro());
            response.setArtsum(userProfileOutModel.getArtsum());
            response.setCreateDate(DateUtils.calculateTime(userProfileOutModel.getCreateDate()));
            response.setColumnAuthed(userProfileOutModel.getColumnAuthed());
            response.setConcernedStatus(0);
            response.setConcernUserStatus(0);
            response.setVipTag(userProfileOutModel.getVipTag());
            response.setUserId(userProfileOutModel.getUserId().toString());
            response.setUserRole(userProfileOutModel.getUserRole());
            UserProfileInModel inModel = new UserProfileInModel();
            inModel.setUserId(userProfileOutModel.getUserId());
            logger.info("-------------??????????????????---------------");
            listresponse.add(response);
        }

        //        if (listnum != null) {
        //            for (UserProfileOutModel OutModel : listnum) {
        //                UserProfileResponse responseout = new UserProfileResponse();
        //
        //                responseout.setUserId(OutModel.getUserId().toString());
        //                responseout.setUserName(OutModel.getUsername());
        //                responseout.setCreateUser(OutModel.getUserId().toString());
        //                responseout.setColumnName(OutModel.getColumnName());
        //                responseout.setColumnAvatar(OutModel.getColumnAvatar());
        //                responseout.setColumnIntro(OutModel.getColumnintro());
        //                responseout.setArtsum(OutModel.getArtsum());
        //                responseout.setColumnAuthed(OutModel.getColumnAuthed());
        //                responseout.setConcernedStatus(0);
        //                responseout.setConcernUserStatus(0);
        //                responseout.setUserId(OutModel.getUserId().toString());
        //                UserProfileInModel inModel = new UserProfileInModel();
        //                inModel.setUserId(OutModel.getUserId());
        //                listresponse.add(responseout);
        //            }
        //        }
        return listresponse;
    }

    @Override
    public AppJsonEntity selectArticleRecomm(ArticleRequest request, PageModel<ArticleResponse> page,
                                             HttpServletRequest httpServletRequest) {
        PageModel<ArticleResponse> pageModel = articlekgService.selectArticleRecomm(request, page, httpServletRequest);
        UserkgRequest userkgRequest = new UserkgRequest();
        if (StringUtils.isNotBlank(request.getUserId())) {
            userkgRequest.setUserId(Long.valueOf(request.getUserId()));
        }
        Map<String, Object> map = new HashMap<>();
        /*
         * if (page.getCurrentPage() == 1) { // ????????????????????????????????????
         * List<UserProfileResponse> userProfileResponses =
         * hotAuthorInfo(userkgRequest); map.put("hotAuthorList",
         * userProfileResponses); // ?????????????????????APP????????????????????? KgCommonSettingInModel
         * inModel = new KgCommonSettingInModel();
         * inModel.setSettingKey(HotAuthorInfoResponse.STTING_KEY);
         * KgCommonSettingOutModel outModel =
         * kgCommonSettingRMapper.selectBySettingKey(inModel);
         * 
         * HotAuthorInfoResponse hotAuthorInfoResponse =
         * JSON.parseObject(outModel.getSettingValue(),
         * HotAuthorInfoResponse.class); map.put("hotAuthorListLocation",
         * hotAuthorInfoResponse.getHotAuthorLocation());
         * 
         * }
         */
        map.put("articleList", pageModel);
        return AppJsonEntity.makeSuccessJsonEntity(map);
    }

    @Override
    public AppJsonEntity getChannelArt(ArticleRequest request, PageModel<ArticleResponse> page, HttpServletRequest httpServletRequest) {
        Map<String, PageModel<ArticleResponse>> map = new HashMap<>();
        //?????????????????????????????????
        if ("-2".equals(request.getColumnId())) {
            if (request.getUserId() == null) {
                return AppJsonEntity.makeSuccessJsonEntity(map);
            }
            //??????????????????????????????
            UserConcernInModel userConcernInModel = new UserConcernInModel();
            userConcernInModel.setUserId(Long.valueOf(request.getUserId()));
            long count = userConcernRMapper.getConcernCount(userConcernInModel);
            if (count == 0) {
                return AppJsonEntity.makeSuccessJsonEntity(map);
            }
        }
        PageModel<ArticleResponse> pageModel = articlekgService.getChannelArt(request, page, httpServletRequest);
        map.put("articleList", pageModel);
        return AppJsonEntity.makeSuccessJsonEntity(map);
    }

    @Override
    public AppJsonEntity getVideoTabInfo(ArticleRequest request, PageModel<ArticleResponse> page, HttpServletRequest servletRequest) {
        if (request.getCurrentPage() == 0) {
            page.setCurrentPage(1);
        } else {
            page.setCurrentPage(request.getCurrentPage());
        }
        if (request.getPageSize() != null) {
            page.setPageSize(request.getPageSize());
        }


        //????????????ID???Redis???????????????????????????????????????????????????
        long count = 0;
        String scount = jedisUtils.get(JedisKey.getCountArticleNum("getVideoTabInfo"));
        if (StringUtils.isNotBlank(scount)) {
            logger.info("???????????????????????????????????????");
            count = Long.parseLong(scount);
        } else {
            //????????????????????????????????????
            logger.info("??????????????????????????????????????????");
            count = articleRMapper.countVideoArt();
        }

        long totalCount = articlekgService.getArticleColumnTotalCount(servletRequest, page.getCurrentPage(), "getVideoTabInfo", count);

        return buildGetVideoTabInfo(page, totalCount, 1);
    }

    public AppJsonEntity buildGetVideoTabInfo(PageModel<ArticleResponse> page, long totalCount, Integer types) {
        Map<String, PageModel<ArticleResponse>> map = new HashMap<>();
        page.setTotalNumber(totalCount);
        //???????????????????????????
        if (page.getCurrentPage() > page.getTotalPage()) {
            return AppJsonEntity.makeSuccessJsonEntity(map);
        }

        String key = JedisKey.getArticleWithPage(null, "getVideoTabInfo", totalCount, page.getCurrentPage());
        if (types != 2) {
            AppJsonEntity appJsonEntity = JSON.parseObject(jedisUtils.get(key), AppJsonEntity.class);
            if (appJsonEntity != null) {
                logger.info("??????TAB????????????????????????????????????-------");
                return appJsonEntity;
            }
        }

        //??????limit??????
        ArticleInModel inModel = new ArticleInModel();
        int startIndex = articlekgService.getArticleRecommStartIndex(page.getCurrentPage(), Integer.parseInt(String.valueOf(totalCount)), page.getPageSize());
        inModel.setStart(startIndex);
        if (startIndex < 0) {
            inModel.setStart(0);
            inModel.setLimit((int) totalCount % page.getPageSize());
        } else {
            inModel.setLimit(page.getPageSize());
        }

        List<ArticleOutModel> list = articleRMapper.getVideoArt(inModel);
        //?????????????????????
        Collections.reverse(list);

        List<ArticleResponse> listresponse = new ArrayList<ArticleResponse>();
        for (ArticleOutModel articleOutModel : list) {
            ArticleResponse response = new ArticleResponse();
            response.setArticleId(articleOutModel.getArticleId());
            response.setArticleDescription(articleOutModel.getArticleDescription());
            response.setArticleTitle(articleOutModel.getArticleTitle());
            response.setArticleTags(articleOutModel.getArticleTags());
            response.setArticleImage(articleOutModel.getArticleImage());
            response.setCreateUser(articleOutModel.getCreateUser().toString());
            response.setUpdateDate(DateUtils.calculateTime(articleOutModel.getUpdateDate()));
            if (articleOutModel.getAuditDate() != null) {
                response.setUpdateTimestamp(String.valueOf(articleOutModel.getAuditDate().getTime()));
            } else {
                response.setUpdateTimestamp(String.valueOf(articleOutModel.getUpdateDate().getTime()));
            }

            response.setArticleImgSize(articleOutModel.getArticleImgSize());
            response.setArticleTagnames(articleOutModel.getArticleTagnames());
            response.setArticleTags(articleOutModel.getArticleTags());
            response.setCollect(articleOutModel.getCollect());
            response.setDisplayOrder(articleOutModel.getDisplayOrder());
            response.setPublishKind(articleOutModel.getPublishKind());
            response.setArticleType(articleOutModel.getArticleType());
            if (articleOutModel.getArticleMark() == 1) {
                // ????????????
                response.setArticleType(1);
            } else {
                response.setArticleType(null);
            }
            if (articleOutModel.getDisplayStatus() == 1 || articleOutModel.getDisplayStatus() == 4) {
                response.setDisplayStatus(String.valueOf(articleOutModel.getDisplayStatus()));
            }

            response.setSecondColumn(articleOutModel.getSecondColumn());
            if (null != articleOutModel.getSecondColumn()) {
                response.setColumnId(articleOutModel.getSecondColumn());
            } else {
                response.setColumnId(articleOutModel.getColumnId());
            }
            response.setSecondcolumnname(articleOutModel.getSecondcolumnname());
            response.setArticlefrom(articleOutModel.getArticlefrom());
            response.setUsername(articleOutModel.getUsername());
            Integer comments = userCommentRMapper.getCommentTotalCount(Long.valueOf(articleOutModel.getArticleId()));
            response.setComments(comments);
            response.setVideoUrl(articleOutModel.getVideoUrl());
            response.setColumnAvatar(articleOutModel.getColumnAvatar());
            response.setBrowseNum(articleOutModel.getBrowseNum());

            response.setBlockchainUrl(articleOutModel.getBlockchainUrl());

            listresponse.add(response);
        }

        page.setData(listresponse);
        map.put("articleList", page);

        if (listresponse.size() > 0) {
            jedisUtils.set(key, JSON.toJSONString(AppJsonEntity.makeSuccessJsonEntity(map)), JedisKey.ONE_WEEK);
            logger.info("???????????????????????????????????????-------");
        }
        return AppJsonEntity.makeSuccessJsonEntity(map);
    }

    /**
     * ???????????????banner
     *
     * @param list
     * @return
     */
    private List<SiteimageResponse> buildSiteimageResponseList(List<AdverResponse> list) {
        List<SiteimageResponse> responselist = new ArrayList<>();
        String jsonStr = JsonUtil.writeValueAsString(list);
        list = JsonUtil.readJson(jsonStr, List.class, AdverResponse.class);
        for (AdverResponse siteimageOutModel : list) {
            SiteimageResponse response = new SiteimageResponse();
            response.setImageType(siteimageOutModel.getInmageType());
            response.setImageAddress(siteimageOutModel.getImageUrl());
            response.setImageId(Long.valueOf(siteimageOutModel.getAdverId()));
            response.setAdverId(Long.valueOf(siteimageOutModel.getAdverId()));
            response.setImageDetail(siteimageOutModel.getSkipTo());
            if (siteimageOutModel.getInmageType() == 1) {
                response = buildArticleForSiteimage(siteimageOutModel, response);
            }
            responselist.add(response);
        }
        return responselist;
    }

    private SiteimageResponse buildArticleForSiteimage(AdverResponse siteimageOutModel,
                                                       SiteimageResponse response) {
        ArticleInModel inModel = new ArticleInModel();
        inModel.setArticleId(Long.parseLong(siteimageOutModel.getSkipTo()));
        ArticleOutModel outModel = articleRMapper.getArticleContent(inModel);
        if (null != outModel) {
            response.setArticleId(outModel.getArticleId());
            response.setArticleTitle(outModel.getArticleTitle());
            response.setCreateUser(outModel.getCreateUser().toString());
        }
        return response;
    }

    private List<ArticleAppResponse> buildArticleResponseList(List<ArticleOutModel> listArticle) {
        List<ArticleAppResponse> listrResponse = new ArrayList<>();
        for (ArticleOutModel articleOutModel : listArticle) {
            ArticleAppResponse response = new ArticleAppResponse();
            response.setArticleId(articleOutModel.getArticleId());
            response.setArticleTitle(articleOutModel.getArticleTitle());
            response.setUsername(articleOutModel.getUsername());
            if (articleOutModel.getArticleMark() == 1) {
                // ????????????
                response.setArticleType(1);
            } else {
                response.setArticleType(null);
            }
            response.setArticleImage(articleOutModel.getArticleImage());
            response.setDisplayStatus(String.valueOf(articleOutModel.getDisplayStatus()));
            response.setCreateUser(articleOutModel.getCreateUser().toString());
            response.setUsername(articleOutModel.getUsername());
            response.setUpdateDate(DateUtils.calculateTime(articleOutModel.getUpdateDate()));
            response.setPublishKind(articleOutModel.getPublishKind());
            if (articleOutModel.getAuditDate() != null) {
                response.setUpdateTimestamp(String.valueOf(articleOutModel.getAuditDate().getTime()));
            } else {
                response.setUpdateTimestamp(String.valueOf(articleOutModel.getUpdateDate().getTime()));
            }
            response.setArticleImgSize(articleOutModel.getArticleImgSize());
            response.setArticlefrom(articleOutModel.getArticlefrom());
            KgArticleStatisticsOutModel outModel = buildKgArticleStatisticsInModel(articleOutModel);
            if (outModel != null) {
                response.setBrowseNum(outModel.getBrowseNum());
            }

            response.setBlockchainUrl(articleOutModel.getBlockchainUrl());

            listrResponse.add(response);
        }
        return listrResponse;
    }

    /**
     * ????????????????????????
     *
     * @param hits
     * @return
     */
    private List<ArticleAppResponse> buildSearchArticleResponseList(SearchHits hits) {
        List<ArticleAppResponse> listrResponse = new ArrayList<>();

        for (SearchHit hit : hits) {
            Map<String, Object> map = hit.getSourceAsMap();
            ArticleAppResponse response = new ArticleAppResponse();

            String createUser = String.valueOf(map.get("createUser"));
            String updateTime = String.valueOf(map.get("updateDate"));
            String articleId = String.valueOf(map.get("articleId"));

            response.setArticleId(articleId);
            response.setArticleTitle(String.valueOf(map.get("articleTitle")));
            response.setArticleImage(String.valueOf(map.get("articleImage")));
            response.setCreateUser(createUser);
            response.setUpdateDate(DateUtils.calculateTime(new Date(Long.valueOf(updateTime + "000"))));
            response.setArticleImgSize((int) map.get("articleImgSize"));
            response.setArticlefrom((int) map.get("articleFrom"));
            response.setPublishKind((int) map.get("publishKind"));
            response.setArticleType((int) map.get("articleType"));
            response.setDisplayStatus(String.valueOf(map.get("displayStatus")));
            response.setUpdateTimestamp(updateTime + "000");

            UserProfileOutModel userProfileOutModel = userProfileRMapper.selectBaseInfoByPrimaryKey(Long.valueOf(createUser));
            if (userProfileOutModel != null) {
                response.setUsername(userProfileOutModel.getColumnName());
            }
            ArticleOutModel articleOutModel = new ArticleOutModel();
            articleOutModel.setArticleId(articleId);
            KgArticleStatisticsOutModel outModel = buildKgArticleStatisticsInModel(articleOutModel);
            if (outModel != null) {
                response.setBrowseNum(outModel.getBrowseNum());
            }
            KgArticleExtendOutModel kgArticleExtendOutModel = kgArticleExtendRMapper.selectByPrimaryKey(Long.valueOf(articleId));
            if (kgArticleExtendOutModel != null) {
                String blockchainUrl = kgArticleExtendOutModel.getBlockchainUrl();
                response.setBlockchainUrl(StringUtils.isNotEmpty(blockchainUrl) ? blockchainUrl : "");
            }
//            response = buildHighlightField(hit, response);
            listrResponse.add(response);
        }
        return listrResponse;
    }

    private KgArticleStatisticsOutModel buildKgArticleStatisticsInModel(ArticleOutModel articleOutModel) {
        KgArticleStatisticsInModel model = new KgArticleStatisticsInModel();
        model.setArticleId(Long.parseLong(articleOutModel.getArticleId()));
        return kgArticleStatisticsRMapper.selectByPrimaryKey(model);
    }

    @Override
    public List<UserProfileResponse> getConnectInfo(List<UserProfileResponse> l, Long userId) {
        List<Long> authIds = new ArrayList<Long>();
        for (UserProfileResponse up : l) {
            authIds.add(Long.valueOf(up.getUserId()));
        }
        UserConcernInModel userConcernInModel = new UserConcernInModel();
        userConcernInModel.setAuthIds(authIds);
        userConcernInModel.setUserId(userId);
        logger.info("???????????????????????????+getConnectInfo:" + JsonUtil.writeValueAsString(userConcernInModel));
        List<UserConcernListOutModel> ups = userConcernRMapper.getConcernDetails(userConcernInModel);
        for (UserProfileResponse upp : l) {
            for (UserConcernListOutModel umo : ups) {
                if (upp.getUserId().equals(umo.getUserId())) {
                    upp.setConcernUserStatus(umo.getConcernUserStatus());
                    upp.setConcernedStatus(umo.getConcernedStatus());
                    break;
                }
            }
        }
        return l;
    }

}
