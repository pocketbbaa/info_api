package com.kg.platform.service.app.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.aop.Cacheable;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ApiException;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.mongo.MongoTables;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.mongo.Seach;
import com.kg.platform.common.mq.MQConfig;
import com.kg.platform.common.mq.MQProduct;
import com.kg.platform.common.utils.*;
import com.kg.platform.dao.entity.PushMessage;
import com.kg.platform.dao.read.*;
import com.kg.platform.dao.write.UserCommentWMapper;
import com.kg.platform.enumeration.BusinessTypeEnum;
import com.kg.platform.enumeration.DateEnum;
import com.kg.platform.enumeration.RegisterOriginEnum;
import com.kg.platform.enumeration.UserLogEnum;
import com.kg.platform.model.in.*;
import com.kg.platform.model.mongoTable.UserCollectTable;
import com.kg.platform.model.out.*;
import com.kg.platform.model.request.*;
import com.kg.platform.model.response.*;
import com.kg.platform.service.*;
import com.kg.platform.service.app.AppAdverService;
import com.kg.platform.service.app.ArticleAppService;
import com.kg.platform.service.app.PersonalCenterService;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCursor;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Decimal128;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ArticleAppServiceImpl implements ArticleAppService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String PUBLIC_STATUS = "1";

    private static final int original = 1; // ??????

    private static final int UN_AUTHED = 0; // ?????????

    @Autowired
    private UserCommentRMapper userCommentRMapper;

    @Autowired
    private ArticleRMapper articleRMapper;

    @Autowired
    private KgArticleStatisticsRMapper kgArticleStatisticsRMapper;

    @Autowired
    private PersonalCenterService personalCenterService;

    @Autowired
    private AppAdverService appAdverService;

    @Inject
    private IDGen idGenerater;

    @Inject
    private UserCommentWMapper userCommentWMapper;

    @Inject
    private TokenManager manager;

    @Inject
    private MQProduct mqProduct;

    @Autowired
    private MQConfig articleSyncMqConfig;

    @Autowired
    private MQConfig mqConfig;

    @Inject
    private UserProfileRMapper userProfileRMapper;

    @Autowired
    private KgInfoSwitchRMapper kgInfoSwitchRMapper;

    @Autowired
    private AccountFlowRMapper accountFlowRMapper;

    @Autowired
    private SiteinfoService siteinfoService;

    @Autowired
    private UserRMapper userRMapper;

    @Autowired
    private UsercollectService usercollectService;

    @Autowired
    private PlatformBonusService platformBonusService;

    @Autowired
    private UserConcernRMapper userConcernRMapper;

    @Autowired
    private PushService pushService;

    @Override
    public PageModel<UserCommentResponse> getUserCommentForApp(UserCommentRequest request,
                                                               PageModel<UserCommentResponse> page) {
        UserCommentInModel inModel = new UserCommentInModel();
        inModel.setStart((page.getCurrentPage() - 1) * page.getPageSize());
        inModel.setLimit(page.getPageSize());
        inModel.setUserId(Long.parseLong(request.getUserId()));
        List<UserCommentOutModel> list = userCommentRMapper.getUserCommentForApp(inModel);
        List<UserCommentResponse> responses = buildUserCommenResponse(list);
        long count = userCommentRMapper.getUserCommentCount(inModel);
        page.setData(responses);
        page.setTotalNumber(count);
        return page;
    }

    @Override
    public PageModel<ArticleAppResponse> selectArticleAppAll(ArticleRequest request,
                                                             PageModel<ArticleAppResponse> page) {
        ArticleInModel inModel = initInModel(request, page);
        List<ArticleOutModel> listArticle = buildListArticle(inModel);
        List<ArticleAppResponse> listrResponse = buildArticleAppResponse(listArticle);
        long count = articleRMapper.selectCountArticle(inModel);
        page.setData(listrResponse);
        page.setTotalNumber(count);
        return page;
    }

    @Override
    public ArticleDetailAppResponse getDetailById(ArticleRequest request, HttpServletRequest servletRequest) {
        logger.info("????????????????????????-> ???????????????{}" + JSONObject.toJSONString(request));
        // ????????????
        Result<ArticleResponse> response = getDetails(request, servletRequest);
        if (response == null) {
            return null;
        }
        if (response.getData() == null) {
            return null;
        }
        ArticleDetailAppResponse articleDetailAppResponse = ArticleDetailAppResponse.buildBaseDetail(response.getData(),
                request);
        // ????????????
        if (StringUtils.isNotEmpty(response.getData().getCreateUser())) {
            articleDetailAppResponse.setCreateUserId(Long.valueOf(response.getData().getCreateUser()));
            UserProfileAppResponse userProfileAppResponse = getUserProfileApp(request.getArticleId(),
                    response.getData().getCreateUser());
            logger.info("??????????????????,?????????0?????????");
            userProfileAppResponse.setConcernedStatus(0);
            userProfileAppResponse.setConcernUserStatus(0);
            if (StringUtils.isNotEmpty(request.getUserId())) {

                UserConcernInModel userConcernInModel = new UserConcernInModel();
                List<Long> authIds = new ArrayList<Long>();
                authIds.add(Long.valueOf(response.getData().getCreateUser()));
                userConcernInModel.setAuthIds(authIds);
                userConcernInModel.setUserId(Long.valueOf(request.getUserId()));
                List<UserConcernListOutModel> l = userConcernRMapper.getConcernDetails(userConcernInModel);
                if (l != null && l.size() > 0) {
                    logger.info("????????????>>>>>>>>>>>>>>>>>>:", JSONObject.toJSONString(l));
                    userProfileAppResponse.setConcernUserStatus(l.get(0).getConcernUserStatus());
                    userProfileAppResponse.setConcernedStatus(l.get(0).getConcernedStatus());
                }
            }
            articleDetailAppResponse.setProfileResponse(userProfileAppResponse);
        }
        logger.info("???????????????????????? -> ??????-ArticleDetailAppResponse:{}", JSONObject.toJSONString(articleDetailAppResponse));

        //[1.3.5]?????????????????? ????????????????????????????????????????????????????????????????????????????????????
        Integer totalCount = userCommentRMapper.getCommentTotalCount(request.getArticleId());
        articleDetailAppResponse.setComments(totalCount);
        return articleDetailAppResponse;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param request
     * @return
     */
    @Cacheable(category = "ArticleAppServiceImpl/getArticleOutModelText", key = "#{request.articleId}", expire = 30, dateType = DateEnum.MINUTES)
    @Override
    public ArticleDetailAppResponse getArticleDetailText(ArticleRequest request) {
        ArticleInModel inModel = getArticleInModel(request);
        ArticleOutModel outModel = articleRMapper.selectTextDetailsForApp(inModel);
        ArticleDetailAppResponse response = new ArticleDetailAppResponse();
        response.setArticleTitle(outModel.getArticleTitle());
        response.setArticleDescription(outModel.getArticleDescription());
        response.setArticleText(outModel.getArticleText());
        return response;
    }

    @Override
    public PageModel getUserArticleAll(ArticleRequest request, PageModel page, HttpServletRequest servletRequest) {
        ArticleInModel inModel = buildArticleInModelForGetUserArticleAll(request, page);
        List<ArticleOutModel> listArticle = articleRMapper.getUserArticleAll(inModel);
        List<ArticleAppResponse> listrResponse = buildArticleAppResponse(listArticle);
        long count = articleRMapper.selectCountArticle(inModel);
        page.setData(listrResponse);
        page.setTotalNumber(count);

        //????????????????????????  ???????????????????????????
        SiteimageRequest siteimageRequest = new SiteimageRequest();
        siteimageRequest.setCurrentPage(page.getCurrentPage());
        siteimageRequest.setPageSize(2);
        siteimageRequest.setNavigator_pos(4);
        siteimageRequest.setImage_pos(41);
        siteimageRequest.setUserId(request.getCreateUser());
        siteimageRequest.setDisPlayPort(2);
        siteimageRequest.setOsVersion(servletRequest.getIntHeader("os_version"));
        page.setExtraData(this.getadverResp(siteimageRequest));
        return page;
    }

    public List<AdverListResponse> getadverResp(SiteimageRequest siteimageRequest) {
        logger.info("??????--------------???" + JsonUtil.writeValueAsString(siteimageRequest));
        logger.info("---------------navigator_pos-------:" + siteimageRequest.getNavigator_pos() + "image_pos:" + siteimageRequest.getImage_pos());
        List<AdverListResponse> advers = new ArrayList<AdverListResponse>();
        List<AdverResponse> s = appAdverService.getAdvs(siteimageRequest);
        logger.info("????????????--------------???" + JsonUtil.writeValueAsString(s));
        if (s == null || s.size() == 0) {
            return advers;
        }
        logger.info("????????????--------------???" + JsonUtil.writeValueAsString(s));
        String jsonStr = JsonUtil.writeValueAsString(s);
        List<AdverResponse> newList = JsonUtil.readJson(jsonStr, List.class, AdverResponse.class);
        int i = 1;
        for (AdverResponse adverResponse : newList) {
            String position = "";
            AdverListResponse adverListResponse = new AdverListResponse();
            AdverResponse ar1 = new AdverResponse();
            ar1.setAdverId(adverResponse.getAdverId());
            ar1.setAdverTitle(adverResponse.getAdverTitle());
            ar1.setImageUrl(adverResponse.getImageUrl());
            ar1.setAdverOwner(adverResponse.getAdverOwner());
            ar1.setCreateDate(adverResponse.getCreateDate());
            ar1.setAdverImgType(adverResponse.getAdverImgType());
            if (StringUtils.isNotEmpty(adverResponse.getImageUrl())) {
                ar1.setImageUrls(adverResponse.getImageUrl().split(","));
            }
            ar1.setSkipType(adverResponse.getSkipType());
            ar1.setSkipTo(adverResponse.getSkipTo());
            adverListResponse.setAdInfo(ar1);
            if ((siteimageRequest.getNavigator_pos() == 4 && siteimageRequest.getImage_pos() == 41) ||
                    (siteimageRequest.getNavigator_pos() == 1 && siteimageRequest.getImage_pos() == 12)) {
                if (siteimageRequest.getCurrentPage() == 1) {
                    if (i == 1) {
                        position = "3";
                    } else {
                        position = "12";
                    }
                } else {
                    if (i == 1) {
                        position = "2";
                    } else {
                        position = "12";
                    }
                }
            } else {
                if (i == 1) {
                    position = "2";
                } else {
                    position = "4";
                }

            }
            adverListResponse.setLocation(position);
            advers.add(adverListResponse);
            i++;
        }
        logger.info("-------?????????????????????--------------???" + JsonUtil.writeValueAsString(advers));
        return advers;
    }


    @Override
    public Long addComment(UserCommentRequest request) {
        UserCommentInModel inModel = new UserCommentInModel();
        inModel.setCommentId(idGenerater.nextId());
        inModel.setArticleId(Long.parseLong(request.getArticleId()));
        inModel.setCommentDate(new Date());
        inModel.setCommentDesc(request.getCommentDesc());
        inModel.setUserId(Long.parseLong(request.getUserId()));
        inModel.setCommentStatus(request.getCommentStatus());
        inModel.setDisplayStatus(1);
        logger.info("[addComment] request:" + JSONObject.toJSONString(request));
        if (userCommentWMapper.insertSelective(inModel) > 0) {
            // ???????????????????????????ID
            if (request.getCommentStatus() != null && request.getCommentStatus() == 1) {
                request.setCommentId(String.valueOf(inModel.getCommentId()));
                pushService.addComment(request);
            }
            return inModel.getCommentId();
        } else {
            return 0L;
        }
    }

    @Override
    public Result<List<ArticleResponse>> recommendForYou(ArticleRequest request) {
        Long articleId = request.getArticleId();
        List<ArticleResponse> responses = new ArrayList<>();
        ArticleInModel articleIn = new ArticleInModel();
        articleIn.setArticleId(articleId);
        ArticleOutModel boutModel = articleRMapper.selectArticleBase(articleIn);
        if (null == boutModel) {
            return new Result<>(responses);
        }
        ArticleInModel cInModel = new ArticleInModel();
        cInModel.setColumnId(String.valueOf(boutModel.getColumnId()));
        cInModel.setUpdateDate(boutModel.getUpdateDate());
        cInModel.setArticleId(articleId);
        List<ArticleOutModel> listArticle = articleRMapper.selectRecommend(cInModel);
        List<ArticleResponse> listrResponse = new ArrayList<>();
        for (ArticleOutModel articleOutModel : listArticle) {
            ArticleResponse response = new ArticleResponse();
            BeanUtils.copyProperties(articleOutModel, response);
            response.setCreateUser(articleOutModel.getCreateUser().toString());
            response.setUpdateDate(DateUtils.calculateTime(articleOutModel.getUpdateDate()));
            response.setDisplayStatus(String.valueOf(articleOutModel.getDisplayStatus()));
            if (articleOutModel.getArticleMark() == 1) {
                // ????????????
                response.setArticleType(1);
            } else {
                response.setArticleType(null);
            }
            if (articleOutModel.getAuditDate() != null) {
                response.setUpdateTimestamp(String.valueOf(articleOutModel.getAuditDate().getTime()));
            } else {
                response.setUpdateTimestamp(String.valueOf(articleOutModel.getUpdateDate().getTime()));
            }
            response.setArticleImgSize(articleOutModel.getArticleImgSize());
            listrResponse.add(response);
        }
        if (CollectionUtils.isNotEmpty(listrResponse)) {
            if (listrResponse.size() > 3) {
                listrResponse = listrResponse.subList(0, 3);
            }
        }
        return new Result<>(listrResponse);
    }

    @Override
    public Map recommendArticle(ArticleRequest request, HttpServletRequest servletRequest) {
        Map map = new HashMap();
        map.put("recommendList", this.getArticleRecom(request));
        map.put("advers", this.getArticleRecomAdv(request,servletRequest));
        //???????????????????????? ???????????????????????????
        return map;
    }

    public List<AdverListResponse> getArticleRecomAdv(ArticleRequest request, HttpServletRequest servletRequest) {
        SiteimageRequest siteimageRequest = new SiteimageRequest();
        siteimageRequest.setArticleId(request.getArticleId());
        siteimageRequest.setCurrentPage(1);
        siteimageRequest.setPageSize(2);
        siteimageRequest.setNavigator_pos(2);
        siteimageRequest.setImage_pos(22);
        siteimageRequest.setDisPlayPort(2);
        siteimageRequest.setOsVersion(servletRequest.getIntHeader("os_version"));
        List<AdverListResponse> s = this.getadverResp(siteimageRequest);
        return s;
    }

    @Cacheable(category = "/kgApp/article/v12/articleRecommend", key = "#{request.articleId}", expire = 2, dateType = DateEnum.HOURS)
    public List<ArticleResponse> getArticleRecom(ArticleRequest request) {
        Long articleId = request.getArticleId();
        List<ArticleResponse> responses = new ArrayList<>();
        ArticleInModel articleIn = new ArticleInModel();
        articleIn.setArticleId(articleId);
        ArticleOutModel boutModel = articleRMapper.selectArticleBase(articleIn);
        if (null == boutModel) {
            return null;
        }
        ArticleInModel cInModel = new ArticleInModel();
        cInModel.setColumnId(String.valueOf(boutModel.getColumnId()));
        cInModel.setUpdateDate(boutModel.getUpdateDate());
        cInModel.setArticleId(articleId);
        List<ArticleOutModel> listArticle = articleRMapper.selectRecommend(cInModel);
        List<ArticleResponse> listrResponse = new ArrayList<>();
        for (ArticleOutModel articleOutModel : listArticle) {
            ArticleResponse response = new ArticleResponse();
            BeanUtils.copyProperties(articleOutModel, response);
            response.setCreateUser(articleOutModel.getCreateUser().toString());
            response.setUpdateDate(DateUtils.calculateTime(articleOutModel.getUpdateDate()));
            response.setDisplayStatus(String.valueOf(articleOutModel.getDisplayStatus()));
            if (articleOutModel.getArticleMark() == 1) {
                // ????????????
                response.setArticleType(1);
            } else {
                response.setArticleType(null);
            }
            if (articleOutModel.getAuditDate() != null) {
                response.setUpdateTimestamp(String.valueOf(articleOutModel.getAuditDate().getTime()));
            } else {
                response.setUpdateTimestamp(String.valueOf(articleOutModel.getUpdateDate().getTime()));
            }
            response.setArticleImgSize(articleOutModel.getArticleImgSize());

            response.setBlockchainUrl(articleOutModel.getBlockchainUrl());

            listrResponse.add(response);
        }
        if (CollectionUtils.isNotEmpty(listrResponse)) {
            if (listrResponse.size() > 3) {
                listrResponse = listrResponse.subList(0, 3);
            }
        }
        return listrResponse;
    }


    @Override
    public void addCollectBonusToMongo(UserCollectRequest request) {
        // ????????????????????????????????????????????????????????????7???
        ArticleInModel ainModel = new ArticleInModel();
        ainModel.setArticleId(request.getArticleId());
        ainModel.setDays(7);
        ArticleOutModel articleOutModel = articleRMapper.checkArticleUnfrezee(ainModel);
        if (articleOutModel == null) {
            return;
        }
        // ????????????????????????????????????10???
        Document filter = new Document();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        // String nowDate = sdf.format(new Date());
        // String startDate = nowDate + "000000";
        // String endDate = nowDate + "235959";
        // filter.append("createDate",
        // BasicDBObjectBuilder.start(Seach.GTE.getOperStr(), startDate)
        // .add(Seach.LTE.getOperStr(), endDate).get());

        filter.append("deviceId", request.getDeviceId());
        filter.append("articleId", request.getArticleId());

        BigDecimal bonus = BigDecimal.ZERO;
        if (request.getOperType() == 3) {
            filter.append("operType", 3);
            logger.info("--------------???????????????????????????????????????----------" + filter);
            long count = MongoUtils.count(MongoTables.KG_COLLECT_TABLE, filter);
            logger.info("--------------???????????????????????????????????????----------" + count);
            if (count >= Long.valueOf(Constants.PALTFORMMAXSHARECOUNT).longValue()) {
                return;
            }
            bonus = Constants.PUBLISG_BONUS_SHARE_TV;
        } else if (request.getOperType() == 4) {
            filter.append("operType", 4);
            long count = MongoUtils.count(MongoTables.KG_COLLECT_TABLE, filter);
            logger.info("--------------???????????????????????????????????????----------" + count);
            if (count >= Long.valueOf(Constants.PALTFORMMAXBROWERCOUNT).longValue()) {
                return;
            }

            bonus = Constants.PUBLISG_BONUS_BROWER__TV;
        }

        // ??????mongo??????
        UserCollectTable userCollectTable = new UserCollectTable();
        userCollectTable.set_id(idGenerater.nextId());
        if (request.getUserId() != null) {
            userCollectTable.setUserId(request.getUserId());
        }
        userCollectTable.setDeviceId(request.getDeviceId());
        userCollectTable.setArticleId(request.getArticleId());
        sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        userCollectTable.setCreateDate(sdf.format(new Date()));
        userCollectTable.setPlatfrom(request.getOsVersion());
        userCollectTable.setOperType(request.getOperType());
        userCollectTable.setBonusStatus(0);
        userCollectTable.setArticleBonus(bonus);
        userCollectTable.setReaderBonus(BigDecimal.ZERO);
        MongoUtils.insertOne(MongoTables.KG_COLLECT_TABLE, new Document(Bean2MapUtil.bean2map(userCollectTable)));
    }


    @Override
    public void platformReward(UserkgResponse kguser, AccountRequest accountRequest,
                               HttpServletRequest servletRequest) {

        // ?????????????????????????????????
        UserCollectRequest userCollectRequest = new UserCollectRequest();
        userCollectRequest.setUserId(accountRequest.getUserId());
        userCollectRequest.setArticleId(accountRequest.getArticleId());
        userCollectRequest.setOperType(3);
        userCollectRequest.setSource(2);
        logger.info("????????????????????????-> userCollectRequest:{}", JSONObject.toJSONString(userCollectRequest));
        boolean insert = usercollectService.insertSelective(userCollectRequest);
        if (!insert) {
            throw new ApiException("????????????????????????");
        }
        if (accountRequest.getUserId() == null) {
            logger.info("AccountRequest???" + JSONObject.toJSONString(accountRequest) + "  ??????ID??????????????????????????????");
            return;
        }
        logger.info("-------------- ??????????????????----------------");
        AccountRequest request = new AccountRequest();
        request.setUserId(accountRequest.getUserId());
        request.setArticleId(accountRequest.getArticleId());
        platformBonusService.getShareBonus(request);
    }


    /**
     * ???????????????????????????
     *
     * @return
     */
    private UserProfileAppResponse getUserProfileApp(Long articleId, String createUser) {

        Long userId = Long.valueOf(createUser); // ????????????ID
        UserProfileRequest userProfileRequest = new UserProfileRequest();
        userProfileRequest.setArticleId(articleId);
        userProfileRequest.setUserId(userId);
        UserProfileColumnAppResponse userProfileColumnResponse = personalCenterService
                .selectByuserprofileId(userProfileRequest);
        if (userProfileColumnResponse == null) {
            return new UserProfileAppResponse();
        }
        userProfileColumnResponse = initTags(userProfileColumnResponse);

        UserkgOutModel userkgOutModel = userRMapper.selectByPrimaryKey(userId);
        if (userkgOutModel == null) {
            return new UserProfileAppResponse();
        }
        // ????????????
        String amount = getArticleAmount(userId, articleId);
        userProfileColumnResponse
                .setRewardIncome(StringUtils.isEmpty(amount) ? "0.000" : String.format("%.3f", new BigDecimal(amount)));

        // ????????????
        Integer userRole = userkgOutModel.getUserRole();
        if (userRole == 1) {
            long realAuthed = userkgOutModel.getRealnameAuthed();
            if (realAuthed == UN_AUTHED) {
                return UserProfileAppResponse.initModel(userProfileColumnResponse);
            }
            userProfileColumnResponse.setRealAuthedTag(1);
            return UserProfileAppResponse.initModel(userProfileColumnResponse);
        }
        int columnAuthed = userkgOutModel.getColumnAuthed();
        // ???????????????
        if (columnAuthed == UN_AUTHED) {
            String identityTag = UserTagsUtil.buildIdentityTagWithOutColumnAuthed(userRole);
            userProfileColumnResponse.setIdentityTag(identityTag);
            return UserProfileAppResponse.initModel(userProfileColumnResponse);
        }
        // ???????????????
        userProfileColumnResponse.setVipTag(columnAuthed);
        userProfileColumnResponse.setIdentityTag(getIdentityTagWithColumnAuthed(userId));
        return UserProfileAppResponse.initModel(userProfileColumnResponse);
    }

    /**
     * ????????????????????????
     *
     * @param userId
     * @return
     */
    private String getIdentityTagWithColumnAuthed(Long userId) {
        return userProfileRMapper.getIdentityByUserId(userId);
    }

    private UserProfileColumnAppResponse initTags(UserProfileColumnAppResponse userProfileColumnResponse) {
        userProfileColumnResponse.setIdentityTag("");
        userProfileColumnResponse.setRealAuthedTag(0);
        userProfileColumnResponse.setVipTag(0);
        return userProfileColumnResponse;
    }

    private PushMessage getPushMessage(String messageText, List<String> receive, UserProfileOutModel outModel,
                                       String title, String tags) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.set_id(idGenerater.nextId());
        pushMessage.setMessageText(messageText);
        pushMessage.setCreateDate(new Date().getTime());
        pushMessage.setReadState(0);
        pushMessage.setTitle(title);
        pushMessage.setReceive(receive);
        pushMessage.setType(2);
        pushMessage.setPushPlace(1);
        pushMessage.setMessageAvatar(outModel.getAvatar());
        pushMessage.setUserId(outModel.getUserId());
        pushMessage.setTag(tags);
        pushMessage.setCommentUserId(outModel.getUserId() + "");
        pushMessage.setClassify(2);
        pushMessage.setSecondClassify(2);
        return pushMessage;
    }

    private ArticleInModel buildArticleInModelForGetUserArticleAll(ArticleRequest request,
                                                                   PageModel<ArticleAppResponse> page) {
        ArticleInModel inModel = new ArticleInModel();
        inModel.setStart((page.getCurrentPage() - 1) * page.getPageSize());
        inModel.setLimit(page.getPageSize());
        inModel.setCreateUser(request.getCreateUser());
        inModel.setPublishStatus(PUBLIC_STATUS);
        return inModel;
    }

    /**
     * ?????????????????????????????????
     *
     * @param request
     * @param page
     * @return
     */
    private ArticleInModel initInModel(ArticleRequest request, PageModel<ArticleAppResponse> page) {
        ArticleInModel inModel = new ArticleInModel();
        inModel.setStart((page.getCurrentPage() - 1) * page.getPageSize());
        inModel.setLimit(page.getPageSize());
        inModel.setDisplayStatus(request.getDisplayStatus());
        return inModel;
    }

    /**
     * ????????????????????????
     *
     * @param inModel
     * @return
     */
    private List<ArticleOutModel> buildListArticle(ArticleInModel inModel) {
        return articleRMapper.selectArticleAppRecommend(inModel);
    }

    /**
     * ??????????????????????????????
     *
     * @param listArticle
     * @return
     */
    private List<ArticleAppResponse> buildArticleAppResponse(List<ArticleOutModel> listArticle) {
        List<ArticleAppResponse> listrResponse = new ArrayList<>();
        for (ArticleOutModel articleOutModel : listArticle) {
            ArticleAppResponse response = new ArticleAppResponse();
            int mark = articleOutModel.getArticleMark();
            int type = articleOutModel.getArticleType();
            response.setArticleId(articleOutModel.getArticleId());
            response.setArticleTitle(articleOutModel.getArticleTitle());
            response.setUsername(articleOutModel.getUsername());
            response.setDisplayStatus("1"); // ????????????????????????
            response.setArticleImage(articleOutModel.getArticleImage());
            response.setCreateUser(articleOutModel.getCreateUser().toString());
            response.setUpdateDate(DateUtils.calculateTime(articleOutModel.getUpdateDate()));
            if (articleOutModel.getAuditDate() != null) {
                response.setUpdateTimestamp(String.valueOf(articleOutModel.getAuditDate().getTime()));
            } else {
                response.setUpdateTimestamp(String.valueOf(articleOutModel.getUpdateDate().getTime()));
            }
            response.setArticleImgSize(articleOutModel.getArticleImgSize());
            response.setArticlefrom(articleOutModel.getArticlefrom());
            response.setBrowseNum(getBrowseNum(Long.valueOf(articleOutModel.getArticleId())));
            if (type == original) {
                if (mark == original) {
                    response.setArticleType(original);
                } else {
                    response.setArticleType(0);
                }
            }
            response.setPublishKind(articleOutModel.getPublishKind());

            response.setBlockchainUrl(articleOutModel.getBlockchainUrl());
            listrResponse.add(response);
        }
        return listrResponse;
    }

    /**
     * ?????????????????????(??????????????????)
     *
     * @return
     */
    private Integer getBrowseNum(Long articleId) {
        KgArticleStatisticsInModel model = new KgArticleStatisticsInModel();
        model.setArticleId(articleId);
        KgArticleStatisticsOutModel outModel = kgArticleStatisticsRMapper.selectByPrimaryKey(model);
        return outModel == null ? 0 : outModel.getBrowseNum();
    }

    /**
     * ??????????????????????????????
     *
     * @param list
     * @return
     */
    private List<UserCommentResponse> buildUserCommenResponse(List<UserCommentOutModel> list) {
        List<UserCommentResponse> responses = new ArrayList<>();
        for (UserCommentOutModel userCommentOutModel : list) {
            UserCommentResponse response = new UserCommentResponse();
            response.setUserName(userCommentOutModel.getUserName());
            response.setAvatar(userCommentOutModel.getAvaTar());
            response.setCommentId(userCommentOutModel.getCommentId().toString());
            response.setArticleId(String.valueOf(userCommentOutModel.getArticleId()));
            response.setArticleImage(userCommentOutModel.getArticleImage());
            response.setArticleTitle(userCommentOutModel.getArticleTitle());
            response.setCommentDate(DateUtils.calculateTime(userCommentOutModel.getCommentDate()));
            response.setCommentDateTimestamp(String.valueOf(userCommentOutModel.getCommentDate().getTime()));
            response.setCommentDesc(userCommentOutModel.getCommentDesc());
            response.setDisplayStatus(userCommentOutModel.getDisplayStatus());
            response.setCommentStatus(userCommentOutModel.getCommentStatus());
            response.setPublishKind(userCommentOutModel.getPublishKind());
            responses.add(response);
        }
        return responses;
    }

    private ArticleInModel getArticleInModel(ArticleRequest request) {
        ArticleInModel inModel = new ArticleInModel();
        inModel.setArticleId(request.getArticleId());
        inModel.setUserId(request.getUserId());
        return inModel;
    }

    /**
     * ??????????????????
     *
     * @param request
     * @return
     */
    private Result<ArticleResponse> getDetails(ArticleRequest request, HttpServletRequest servletRequest) {
        ArticleInModel inModel = getArticleInModel(request);

        ArticleOutModel articleOutModel = getArticleOutModel(inModel);
        if (articleOutModel == null) {
            logger.error("????????????????????? inModel:" + JSONObject.toJSONString(inModel));
            return null;
        }
        UserCollectInModel collectInModel = new UserCollectInModel();
        collectInModel.setArticleId(Long.parseLong(articleOutModel.getArticleId()));
        //AccountFlowInModel accountFlowInModel = new AccountFlowInModel();
        //accountFlowInModel.setUserId(articleOutModel.getCreateUser());
        //accountFlowInModel.setBusinessTypeId(BusinessTypeEnum.TIPSIN.getStatus());
        //accountFlowInModel.setArticleId(request.getArticleId());
        // ??????????????????
        //int tipsinCount = accountFlowRMapper.getTipsinCount(accountFlowInModel);
        //??????????????????????????????
        int tipsinCount = 0;

        // app1.1.0??????????????????
        // ??????????????????????????????
        // KgArticleBonusInModel bonusInModel = new KgArticleBonusInModel();
        // bonusInModel.setArticleId(request.getArticleId());
        // List<KgArticleBonusOutModel> bonusOutModelsList =
        // kgArticleBonusRMapper.getArticleBonus(bonusInModel);


        ArticleResponse response = new ArticleResponse();

        BeanUtils.copyProperties(articleOutModel, response);
        response.setCreateDate(articleOutModel.getUpdateDate());
        if (articleOutModel.getAuditDate() != null) {
            response.setUpdateTimestamp(String.valueOf(articleOutModel.getAuditDate().getTime()));
        } else {
            response.setUpdateTimestamp(String.valueOf(articleOutModel.getUpdateDate().getTime()));
        }

        // int mark = articleOutModel.getArticleMark();
        // int type = articleOutModel.getArticleType();
        // if (type == original) {
        // if (mark == original) {
        // response.setArticleType(original);
        // } else {
        // response.setArticleType(0);
        // }
        // }

        KgArticleStatisticsInModel statinModel = new KgArticleStatisticsInModel();
        statinModel.setArticleId(request.getArticleId());
        statinModel.setTypes(1);
        KgArticleStatisticsOutModel statisticsOutModel = kgArticleStatisticsRMapper.selectByPrimaryKey(statinModel);
        if (statisticsOutModel != null) {
            response.setBrowseNum(statisticsOutModel.getBrowseNum());
            response.setCollect(statisticsOutModel.getCollectNum());
            response.setThumbupNum(statisticsOutModel.getThumbupNum());
        }

        mqProduct.sendMessage(mqConfig.getTopic(), mqConfig.getTags(),
                "detail_" + String.valueOf(request.getArticleId()), JSONObject.toJSONString(statinModel));

        UserCollectInMongo userCollectInModel = new UserCollectInMongo();
        userCollectInModel.setCollectId(idGenerater.nextId());
        if (StringUtils.isNotBlank(request.getUserId())) {
            userCollectInModel.setUserId(Long.valueOf(request.getUserId()));
        }
        userCollectInModel.setArticleId(request.getArticleId());
        userCollectInModel.setOperType(4);
        if (servletRequest.getIntHeader("os_version") == 1 || servletRequest.getIntHeader("os_version") == 2) {
            userCollectInModel.setSource(2);
        }
        if (servletRequest.getIntHeader("os_version") == RegisterOriginEnum.M.getOrigin()) {
            userCollectInModel.setSource(3);
        }

        Date now = new Date();
        userCollectInModel.setCollectDate(now);
        userCollectInModel.setTime(now.getTime());
        userCollectInModel.setPublishKind(articleOutModel.getPublishKind());
        MongoUtils.insertOne(UserLogEnum.KG_USER_BROWSE.getTable(), new Document(Bean2MapUtil.bean2map(userCollectInModel)));

        sendArticleSyncMessage(request.getArticleId());
        response.setBonusState(0);
        response.setTipsinCount(tipsinCount + "");
        response.setCreateUser(String.valueOf(articleOutModel.getCreateUser()));
        response.setColumnname(articleOutModel.getColumnname());
        response.setArticleTagnames(articleOutModel.getArticleTagnames());
        response.setUserColumn(articleOutModel.getUserColumn());
        response.setColumnAvatar(articleOutModel.getColumnAvatar());

        if (StringUtils.isEmpty(request.getUserId())) {
            response.setCollectstatus(0);
            response.setPraisestatus(1);
            logger.info("???????????????????????????????????????????????? userId == null ");
        } else {
            BasicDBObject query = new BasicDBObject();
            query.put("userId", Long.valueOf(request.getUserId()));
            query.put("articleId", request.getArticleId());
            long countCollect = MongoUtils.count(UserLogEnum.KG_USER_COLLECT.getTable(), query);
            long countLike = MongoUtils.count(UserLogEnum.KG_USER_LIKE.getTable(), query);

            response.setPraisestatus(countLike > 0 ? 1 : 0);
            response.setCollectstatus(countCollect > 0 ? 1 : 0);
            logger.info("???????????????????????????????????????????????? userId???" + request.getUserId() + "  articleId???" + request.getArticleId() + "  countCollect???" + countCollect + "  countLike???" + countLike);
        }
        Result<SiteinfoResponse> siteinfoResponse = siteinfoService.selectSiteInfo();
        response.setCommentAudit(siteinfoResponse.getData().getCommentAudit());
        List<KgArticleBonusResponse> listArtBonus = new ArrayList<>();
        // ??????????????????

        // app1.1.0??????????????????
        // if (CollectionUtils.isNotEmpty(bonusOutModelsList)) {
        // response.setBonusState(1);
        // List<KgArticleBonusResponse> listArtBonus = new ArrayList<>();
        // for (KgArticleBonusOutModel kgArBOutModel : bonusOutModelsList) {
        // // ????????????????????????????????????????????????????????????????????????
        // if (overMaxPeople(kgArBOutModel)) {
        // continue;
        // }
        // KgArticleBonusResponse articleBonusResponse = new
        // KgArticleBonusResponse();
        // BeanUtils.copyProperties(kgArBOutModel, articleBonusResponse);
        // articleBonusResponse.setBonusId(kgArBOutModel.getBonusId().toString());
        // articleBonusResponse.setArticleId(kgArBOutModel.getArticleId().toString());
        // listArtBonus.add(articleBonusResponse);
        // }
        // response.setListArtBonus(listArtBonus);
        // }

        return new Result<>(response);
    }

    /**
     * ?????????????????????????????????????????????????????????
     */
    // app1.1.0??????????????????
    // private boolean overMaxPeople(KgArticleBonusOutModel kgArBOutModel) {
    // Integer bonusType = kgArBOutModel.getBonusType();
    // logger.info("??????????????????bonusType = " + bonusType);
    // if (bonusType != null && bonusType == 1) {
    // Integer maxPeople = kgArBOutModel.getMaxPeople();
    // if (maxPeople == null || maxPeople <= 0) {
    // return false;
    // }
    // AccountFlowInModel inModel = new AccountFlowInModel();
    // inModel.setArticleId(kgArBOutModel.getArticleId());
    // inModel.setBusinessTypeId(310);
    // int bonusNum = accountFlowRMapper.getBusinessTypeCount(inModel);
    // logger.info("??????????????????????????????:{},????????????:{}", bonusNum, maxPeople);
    // if (bonusNum >= maxPeople) {
    // logger.info("????????????????????????????????????????????? .. bonusNum:{}???maxPeople:{}", bonusNum,
    // maxPeople);
    // return true;
    // }
    // }
    // return false;
    // }
    private boolean bussinessAward(BusinessTypeEnum businessTypeEnum, Long userId, Long articleId, int limit) {
        //??????????????????
        Long start = DateUtils.getDateStart(new Date()).getTime();
        Long end = DateUtils.getDateEnd(new Date()).getTime();

        BasicDBObject querry = new BasicDBObject();
        querry.append("article_id", new BasicDBObject(Seach.EQ.getOperStr(), articleId));
        querry.append("user_id", new BasicDBObject(Seach.EQ.getOperStr(), userId));
        querry.append("business_type_id", new BasicDBObject(Seach.EQ.getOperStr(), businessTypeEnum.getStatus()));
        querry.append("amount", new BasicDBObject(Seach.GT.getOperStr(), 0));
        querry.append("flow_date", BasicDBObjectBuilder.start(Seach.GTE.getOperStr(), start).add(Seach.LTE.getOperStr(), end).get());
        Long bonusCount = MongoUtils.count(MongoTables.ACCOUNT_FLOW_KG, querry);
        // ???????????????????????????????????????
        if (bonusCount != null && bonusCount > 0) {
            return false;
        }
        // ??????????????????????????????limit???????????????
        querry = new BasicDBObject();
        querry.append("user_id", new BasicDBObject(Seach.EQ.getOperStr(), userId));
        querry.append("business_type_id", new BasicDBObject(Seach.EQ.getOperStr(), businessTypeEnum.getStatus()));
        querry.append("amount", new BasicDBObject(Seach.GT.getOperStr(), 0));
        querry.append("flow_date", BasicDBObjectBuilder.start(Seach.GTE.getOperStr(), start).add(Seach.LTE.getOperStr(), end).get());
        bonusCount = MongoUtils.count(MongoTables.ACCOUNT_FLOW_KG, querry);
        if (bonusCount != null && bonusCount >= limit) {
            return false;
        }
        return true;
    }


    /**
     * ???????????????????????????
     *
     * @param inModel
     * @return
     */
    private ArticleOutModel getArticleOutModel(ArticleInModel inModel) {
        if (inModel == null || inModel.getArticleId() == null || inModel.getArticleId() <= 0) {
            return null;
        }
        // ????????????????????????
        ArticleOutModel articleOutModelBase = articleRMapper.selectBaseDetailsForApp(inModel);
        logger.info("articleOutModelBase" + JsonUtil.writeValueAsString(articleOutModelBase));
        if (articleOutModelBase == null) {
            return null;
        }
        return articleOutModelBase;
    }

    /**
     * ??????????????????
     *
     * @param userId
     * @return
     */
    public String getArticleAmount(Long userId, Long articleId) {
        BigDecimal amountPlatform = new BigDecimal("0.000");
        // ??????????????????
        MongoCursor<Document> cursor = MongoUtils.aggregate(MongoTables.KG_COLLECT_TABLE, getDBObjectList(articleId));
        if (cursor != null) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                Integer type = document.getInteger("_id");
                if (type == null || type == 1 || type == 2) {
                    continue;
                }
                Decimal128 totalDec = (Decimal128) document.get("total");
                String total = totalDec.toString();
                BigDecimal amountType = new BigDecimal(total);
                amountPlatform = amountPlatform.add(amountType);
            }
        }
        // ????????????+????????????
        String amount = accountFlowRMapper.getAmountByUserId(userId, articleId);
        logger.info("????????????????????????????????????:{},????????????+????????????:{}", amountPlatform.toString(), amount);
        return amountPlatform.add(new BigDecimal(StringUtils.isEmpty(amount) ? "0.000" : amount)).toString();
    }

    /**
     * ??????mongdb????????????
     *
     * @param articleId
     * @return
     */
    private static List<Bson> getDBObjectList(Long articleId) {
        List<Bson> ops = new ArrayList<>();
        DBObject query = new BasicDBObject();
        query.put("articleId", articleId);
        query.put("bonusStatus", 0);
        Bson match = new BasicDBObject("$match", query);
        ops.add(match);
        Bson group = new BasicDBObject("$group",
                new BasicDBObject("_id", "$operType").append("total", new BasicDBObject("$sum", "$articleBonus")));
        ops.add(group);
        return ops;
    }

    @Override
    public int getShareCount(AccountFlowRequest accountFlowRequest) {
        // ????????????????????????????????????10???
//        AccountFlowInModel inModel = new AccountFlowInModel();
//        inModel.setArticleId(accountFlowRequest.getArticleId());
//        inModel.setUserId(accountFlowRequest.getUserId());
//        inModel.setBusinessTypeId(BusinessTypeEnum.SHAREARTICLETKGAWARD.getStatus());
//        logger.info("???????????????????????????????????????????????????");
//        int bonusCount = accountFlowTxbRMapper.checkArticleBonus(inModel);
//        if (bonusCount > 0) {
//            return 0;
//        }
//
//        logger.info("??????????????????????????????6??????????????? ");
//        bonusCount = accountFlowTxbRMapper.selectTypeFlowCount(inModel);
//        if (bonusCount >= Constants.PALTFORMSHARECOUNT) {
//            return 0;
//        }
        Long startDate = null;
        Long endDate = null;
        BasicDBObject querry = new BasicDBObject();
        querry.append("business_type_id", new BasicDBObject(Seach.EQ.getOperStr(), BusinessTypeEnum.SHAREARTICLETKGAWARD.getStatus()));
        querry.append("user_id", new BasicDBObject(Seach.EQ.getOperStr(), accountFlowRequest.getUserId()));
        querry.append("article_id", new BasicDBObject(Seach.EQ.getOperStr(), accountFlowRequest.getArticleId()));
        startDate = DateUtils.getDateStart(new Date()).getTime();
        endDate = DateUtils.getDateEnd(new Date()).getTime();
        querry.append("flow_date", BasicDBObjectBuilder.start(Seach.GTE.getOperStr(), startDate)
                .add(Seach.LTE.getOperStr(), endDate).get());
        Long bonusCount = MongoUtils.count(MongoTables.ACCOUNT_FLOW_KG, querry);
        logger.info("???????????????????????????????????????????????????");
        if (bonusCount > 0) {
            return 0;
        }

        logger.info("??????????????????????????????6??????????????? ");
        //bonusCount = accountFlowTxbRMapper.selectTypeFlowCount(inModel);
        querry.remove("article_id");
        bonusCount = MongoUtils.count(MongoTables.ACCOUNT_FLOW_KG, querry);
        if (bonusCount >= Constants.PALTFORMSHARECOUNT) {
            return 0;
        }
        return 1;
    }

    /**
     * ????????????????????????
     *
     * @param article
     */
    private void sendArticleSyncMessage(Long articleId) {
        Map<String, Object> map = new HashMap<>();
        map.put("types", "0");
        map.put("articleId", articleId);
        logger.info("ES????????????MQ------topic:{},  map:{}", articleSyncMqConfig.getTopic(), JSONObject.toJSON(map));
        mqProduct.sendMessage(articleSyncMqConfig.getTopic(), articleSyncMqConfig.getTags(), null, JSON.toJSONString(map));
    }
}
