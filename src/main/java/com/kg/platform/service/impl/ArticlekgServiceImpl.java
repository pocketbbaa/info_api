package com.kg.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.kg.platform.common.aop.Cacheable;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ApiMessageException;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.lock.Lock;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.mongo.Seach;
import com.kg.platform.common.mq.MQConfig;
import com.kg.platform.common.mq.MQProduct;
import com.kg.platform.common.utils.*;
import com.kg.platform.dao.read.*;
import com.kg.platform.dao.write.ArticleWMapper;
import com.kg.platform.dao.write.TagsWMapper;
import com.kg.platform.enumeration.*;
import com.kg.platform.model.in.*;
import com.kg.platform.model.out.*;
import com.kg.platform.model.request.ArticleRequest;
import com.kg.platform.model.request.BaseRequest;
import com.kg.platform.model.response.*;
import com.kg.platform.service.ArticlekgService;
import com.kg.platform.service.SiteinfoService;
import com.kg.platform.service.app.ArticleAppService;
import com.kg.platform.service.app.HomePageService;
import com.kg.search.model.result.BaseResult;
import com.kg.search.service.ArticleSearchService;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.client.MongoCursor;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Service
@Transactional
public class ArticlekgServiceImpl implements ArticlekgService {
    private static final Logger logger = LoggerFactory.getLogger(ArticlekgServiceImpl.class);

    private static final String USER_REWARD_LOCK_KEY = "kguser:: ArticlekgService::reward::";

    private static final Integer ARTICLE_RANDOM_LIMIT = 300;

    @Inject
    private ArticleRMapper articleRMapper;

    @Autowired
    protected JedisUtils jedisUtils;

    @Inject
    ArticleWMapper articleWMapper;

    @Inject
    private IDGen idGenerater;

    @Inject
    TagsRMapper tagsRMapper;

    @Inject
    TagsWMapper tagsWMapper;

    @Inject
    AccountRMapper accountRMapper;

    @Inject
    KgArticleStatisticsRMapper kgArticleStatisticsRMapper;

    @Inject
    UserRMapper userRMapper;

    @Inject
    ColumnRMapper columnRMapper;

    @Inject
    UserConcernRMapper userConcernRMapper;

    @Inject
    SiteinfoService siteinfoService;
    @Autowired
    ArticleAppService articleAppService;
    @Inject
    HomePageService homePageService;

    @Inject
    MQProduct mqProduct;

    @Autowired
    MQConfig countArticleMqConfig;

    @Autowired
    private MQConfig articleSyncMqConfig;

    @Autowired
    private ArticleSearchService articleSearchService;

    @Autowired
    private UserProfileRMapper userProfileRMapper;

    @Autowired
    private UserCommentRMapper userCommentRMapper;

    @Autowired
    private KgSeoTdkRMapper kgSeoTdkRMapper;

    /**
     * 我的专栏我的文章
     */

    @Override
    public PageModel<ArticleResponse> getUserArticleAll(ArticleRequest request, PageModel<ArticleResponse> page) {
        logger.info("我的专栏我的文章前端入参：{},分页入参：{}", JSON.toJSONString(request), JSON.toJSONString(page));
        ArticleInModel inModel = new ArticleInModel();
        inModel.setStart((page.getCurrentPage() - 1) * page.getPageSize());
        inModel.setLimit(page.getPageSize());
        inModel.setCreateUser(request.getCreateUser());
        inModel.setPublishStatus(request.getPublishStatus());

        List<ArticleOutModel> listArticle = articleRMapper.getUserArticleAll(inModel);
        List<ArticleResponse> listrResponse = new ArrayList<ArticleResponse>();
        for (ArticleOutModel articleOutModel : listArticle) {
            ArticleResponse response = new ArticleResponse();
            response.setArticleId(articleOutModel.getArticleId());
            response.setArticleTitle(articleOutModel.getArticleTitle());
            response.setArticleTags(articleOutModel.getArticleTags());
            response.setArticleImage(articleOutModel.getArticleImage());
            response.setArticleImgSize(articleOutModel.getArticleImgSize());
            response.setPublishBonusStatus(articleOutModel.getPublishBonusStatus());
            response.setArticleMark(articleOutModel.getArticleMark());
            response.setFreezeReason(articleOutModel.getFreezeReason());
            response.setIsPublishBonus(articleOutModel.getIsPublishBonus());
            response.setColumnname(articleOutModel.getColumnname());
            response.setSecondColumn(articleOutModel.getSecondColumn());
            if (null != articleOutModel.getSecondColumn()) {
                response.setColumnId(articleOutModel.getSecondColumn());
            } else {
                response.setColumnId(articleOutModel.getColumnId());
            }

            response.setSecondcolumnname(articleOutModel.getSecondcolumnname());

            response.setColumnAvatar(articleOutModel.getColumnAvatar());

            response.setUpdateDate(DateUtils.calculateTime(articleOutModel.getUpdateDate()));
            response.setArticleTagnames(articleOutModel.getArticleTagnames());
            response.setPublishStatus(articleOutModel.getPublishStatus());
            response.setDisplayStatus(articleOutModel.getDisplayStatus() + "");
            response.setRefuseReason(articleOutModel.getRefuseReason());
            response.setArticlefrom(articleOutModel.getArticlefrom());
            response.setCreateUser(articleOutModel.getCreateUser().toString());
            response.setTextnum(articleOutModel.getTextnum());
            response.setUsername(articleOutModel.getUsername());
            response.setPublishKind(articleOutModel.getPublishKind());
            response.setVideoUrl(articleOutModel.getVideoUrl());
            response.setVideoFilename(articleOutModel.getVideoFilename());

            response.setArticleDescription(articleOutModel.getArticleDescription());
            response.setArticleLink(articleOutModel.getArticleLink());
            response.setArticleSource(articleOutModel.getArticleSource());
            if (null != articleOutModel.getPublishStatus() && articleOutModel.getPublishStatus().intValue() != 1) {
                response.setArticleText(articleOutModel.getArticleText());
            }
            if (articleOutModel.getArticleMark() == 1) {
                // 优质文章
                response.setArticleType(1);
            } else {
                response.setArticleType(null);
            }
            if (articleOutModel.getAuditDate() != null) {
                response.setUpdateTimestamp(String.valueOf(articleOutModel.getAuditDate().getTime()));
            } else {
                response.setUpdateTimestamp(String.valueOf(articleOutModel.getUpdateDate().getTime()));
            }

            KgArticleStatisticsInModel model = new KgArticleStatisticsInModel();
            model.setArticleId(Long.parseLong(articleOutModel.getArticleId()));
            KgArticleStatisticsOutModel outModel = kgArticleStatisticsRMapper.selectByPrimaryKey(model);

            if (outModel != null) {
                response.setBrowseNum(outModel.getBrowseNum());
            }
            listrResponse.add(response);
        }
        long count = articleRMapper.selectCountArticle(inModel);

        page.setData(listrResponse);
        page.setTotalNumber(count);
        return page;
    }

    /**
     * 频道页热门作者，栏目的热门文章 ，共用一个接口，专栏下的文章传入文章ID
     */
    @Override
    public PageModel<ArticleResponse> getChannelArt(ArticleRequest request, PageModel<ArticleResponse> page, HttpServletRequest httpServletRequest) {
        logger.info("频道页热门作者：{},分页入参：{}", JSON.toJSONString(request), JSON.toJSONString(page));
        ArticleInModel inModel = new ArticleInModel();
        inModel.setColumnId(request.getColumnId());
        if (StringUtils.isNotEmpty(request.getUserId())) {
            inModel.setUserId(request.getUserId());
        }
        inModel.setArticleTagnames(request.getArticleTagnames());
        inModel.setSearchword(request.getArticleTagnames());

        // 添加我的关注作者ID
        if ("-2".equals(request.getColumnId()) && StringUtils.isNotEmpty(request.getUserId())) {
            UserConcernInModel userConcernInModel = new UserConcernInModel();
            userConcernInModel.setUserId(Long.valueOf(request.getUserId()));
            logger.info("我关注的作者ID参数：" + JsonUtil.writeValueAsString(userConcernInModel));
            List<Long> authorIds = userConcernRMapper.getConcernListIds(userConcernInModel);
            logger.info("我关注的作者ID：" + JsonUtil.writeValueAsString(authorIds));
            if (authorIds == null || authorIds.size() == 0) {
                page.setData(null);
                return page;
            }
            inModel.setAuthorIds(authorIds);
        }

        String tagnames = StringUtils.isBlank(request.getArticleTagnames()) ? "" : request.getArticleTagnames();
        //根据栏目ID从Redis获取文章此时的总量（从缓存中获取）
        long count = 0;
        String scount = jedisUtils.get(JedisKey.getCountArticleNum(request.getColumnId() + tagnames));
        if (StringUtils.isNotBlank(scount)) {
            logger.info("从缓存中获取栏目下文章总量");
            count = Long.parseLong(scount);
        } else {
            //缓存没有才从数据库中获取
            logger.info("从数据库中获取栏目下文章总量");
            count = articleRMapper.countChannelArt(inModel);
            if (!"-2".equals(request.getColumnId()) && request.getColumnId() != null) {
                jedisUtils.set(JedisKey.getCountArticleNum(request.getColumnId() + tagnames), String.valueOf(count));
            }
        }


        long totalCount = getArticleColumnTotalCount(httpServletRequest, page.getCurrentPage(), request.getColumnId() + tagnames, count);
        page = buildGetChannelArt(page, totalCount, request, inModel, 1);


        return page;
    }

    public PageModel<ArticleResponse> buildGetChannelArt(PageModel<ArticleResponse> page, long totalCount, ArticleRequest request, ArticleInModel inModel, Integer types) {
        page.setTotalNumber(totalCount);
        //判断是否超过总页数
        if (page.getCurrentPage() > page.getTotalPage()) {
            page.setData(null);
            return page;
        }
        String key = JedisKey.getArticleWithPage(request.getColumnId(), request.getArticleTagnames(), totalCount, page.getCurrentPage());
        if ("-2".equals(request.getColumnId()) && StringUtils.isNotBlank(request.getUserId())) {
            //关注栏目的Key
            key = JedisKey.getArticleWithPage(request.getColumnId(), "userId_" + request.getUserId(), totalCount, page.getCurrentPage());
        }

        if (types != 2) {
            PageModel<ArticleResponse> redisPagemodel = JSON.parseObject(jedisUtils.get(key), PageModel.class);
            if (redisPagemodel != null) {
                logger.info("栏目列表分页信息从缓存中获取-------");
                return redisPagemodel;
            }
        }


        //计算limit下标
        int startIndex = getArticleRecommStartIndex(page.getCurrentPage(), Integer.parseInt(String.valueOf(totalCount)), page.getPageSize());
        inModel.setStart(startIndex);
        if (startIndex < 0) {
            inModel.setStart(0);
            inModel.setLimit((int) totalCount % page.getPageSize());
        } else {
            inModel.setLimit(page.getPageSize());
        }

        List<ArticleOutModel> list = Lists.newArrayList();
        if (StringUtils.isNotBlank(request.getArticleTagnames())) {
            //根据tag查询
            logger.info("根据tag查询:{}", request.getArticleTagnames());
            list = articleRMapper.getArticleByTag(inModel);
        } else {
            list = articleRMapper.getChannelArt(inModel);
        }
        //将列表顺讯转换
        Collections.reverse(list);
        List<ArticleResponse> listresponse = new ArrayList<ArticleResponse>();
        for (ArticleOutModel articleOutModel : list) {
            ArticleResponse response = new ArticleResponse();
            response.setArticleId(articleOutModel.getArticleId());
            response.setArticleDescription(articleOutModel.getArticleDescription());
            response.setArticleTitle(articleOutModel.getArticleTitle());
            response.setArticleTags(articleOutModel.getArticleTags());
            response.setArticleMark(articleOutModel.getArticleMark());
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
            response.setProfileavatar(articleOutModel.getProfileavatar());
            if (articleOutModel.getArticleMark() == 1) {
                // 优质文章
                response.setArticleType(1);
            } else {
                response.setArticleType(null);
            }
            if (articleOutModel.getDisplayStatus() == 1 || articleOutModel.getDisplayStatus() == 4) {
                response.setDisplayStatus(String.valueOf(articleOutModel.getDisplayStatus()));
            }

            response.setColumnname(articleOutModel.getColumnname());
            response.setSecondColumn(articleOutModel.getSecondColumn());
            if (null != articleOutModel.getSecondColumn()) {
                response.setColumnId(articleOutModel.getSecondColumn());
            } else {
                response.setColumnId(articleOutModel.getColumnId());
            }
            // if(StringUtils.isNoneBlank(articleOutModel.getSecondcolumnname()))
            // {
            // response.setColumnname(articleOutModel.getSecondcolumnname());
            // }else{
            response.setSecondcolumnname(articleOutModel.getSecondcolumnname());
            // }
            response.setArticlefrom(articleOutModel.getArticlefrom());
            response.setSearchword(articleOutModel.getSearchword());
            response.setUsername(articleOutModel.getUsername());
            if (articleOutModel.getPublishKind() == 2) {
                Integer comments = userCommentRMapper.getCommentTotalCount(Long.valueOf(articleOutModel.getArticleId()));
                response.setComments(comments);
            }
            response.setVideoUrl(articleOutModel.getVideoUrl());

            KgArticleStatisticsInModel model = new KgArticleStatisticsInModel();
            model.setArticleId(Long.parseLong(articleOutModel.getArticleId()));
            response.setBrowseNum(articleOutModel.getBrowseNum());

            response.setBlockchainUrl(articleOutModel.getBlockchainUrl());

            listresponse.add(response);

        }
        page.setData(listresponse);
        if (listresponse.size() > 0) {
            if (StringUtils.isNotBlank(request.getArticleTagnames())) {
                jedisUtils.set(key, JSON.toJSONString(page), JedisKey.TEN_MINUTE);
            } else {
                jedisUtils.set(key, JSON.toJSONString(page), JedisKey.ONE_WEEK);
            }
            logger.info("栏目列表分页信息已存入缓存-------");
        }
        return page;
    }


    public void pushCountArticleMq(String columnId) {
        if (!"-2".equals(columnId)) {
            Map<String, String> map = new HashMap();
            map.put("types", "1");
            logger.info("发送统计每个栏目的文章总量MQ------topic:{}", countArticleMqConfig.getTopic());
            mqProduct.sendMessage(countArticleMqConfig.getTopic(), countArticleMqConfig.getTags(), null, JSON.toJSONString(map));
        }
    }

    /**
     * 首页热门文章
     */
    @Override
    public PageModel<ArticleResponse> selectArticleAll(ArticleRequest request, PageModel<ArticleResponse> page, HttpServletRequest httpServletRequest) {
        logger.info("首页热门文章：{},分页入参：{}", JSON.toJSONString(request), JSON.toJSONString(page));
        logger.info("请求开始=========");
        long start = System.currentTimeMillis();
        logger.info("查询一结束，耗时：{}", System.currentTimeMillis() - start);

        //根据栏目ID从Redis获取文章此时的总量（从缓存中获取）
        long count = 0;
        ArticleInModel inModel = new ArticleInModel();
        String scount = jedisUtils.get(JedisKey.getCountArticleNum("-1"));
        if (StringUtils.isNotBlank(scount)) {
            logger.info("从缓存中获取栏目下文章总量");
            count = Long.parseLong(scount);
        } else {
            logger.info("从数据库中获取栏目下文章总量");
            //缓存没有才从数据库中获取
            count = articleRMapper.selectCountArticleRecomm(inModel);
            jedisUtils.set(JedisKey.getCountArticleNum("-1"), String.valueOf(count));
        }

        long totalCount = getArticleColumnTotalCount(httpServletRequest, page.getCurrentPage(), "-1", count);

        page = buildSelectArticleAllResponse(page, totalCount, 1);
        logger.info("查询结束，耗时：{}", System.currentTimeMillis() - start);
        return page;
    }

    public PageModel<ArticleResponse> buildSelectArticleAllResponse(PageModel<ArticleResponse> page, long totalCount, Integer types) {

        ArticleInModel inModel = new ArticleInModel();
        page.setTotalNumber(totalCount);
        if (page.getCurrentPage() > page.getTotalPage()) {
            page.setData(null);
            return page;
        }

        String key = JedisKey.getArticleWithPage("-1", "WEB", totalCount, page.getCurrentPage());
        if (types != 2) {
            PageModel<ArticleResponse> redisPagemodel = JSON.parseObject(jedisUtils.get(key), PageModel.class);
            if (redisPagemodel != null) {
                logger.info("首页分页信息从缓存中获取-------");
                return redisPagemodel;
            }
        }

        int startIndex = getArticleRecommStartIndex(page.getCurrentPage(), Integer.parseInt(String.valueOf(totalCount)), page.getPageSize());
        inModel.setStart(startIndex);
        if (startIndex < 0) {
            inModel.setStart(0);
            inModel.setLimit((int) totalCount % page.getPageSize());
        } else {
            inModel.setLimit(page.getPageSize());
        }

        logger.info("【查询推荐列表】-> ArticleInModel：{}，totalCount：{}", JSONObject.toJSONString(inModel), totalCount);
        List<ArticleOutModel> listArticle = articleRMapper.selectArticleAll(inModel);
        Collections.reverse(listArticle);
        List<ArticleResponse> listrResponse = new ArrayList<ArticleResponse>();
        for (ArticleOutModel articleOutModel : listArticle) {
            ArticleResponse response = new ArticleResponse();
            response.setArticleId(articleOutModel.getArticleId());
            response.setArticleTitle(articleOutModel.getArticleTitle());
            response.setArticleTags(articleOutModel.getArticleTags());
            response.setArticleImage(articleOutModel.getArticleImage());
            response.setColumnId(articleOutModel.getColumnId());
            response.setCreateUser(articleOutModel.getCreateUser().toString());
            response.setUpdateDate(DateUtils.calculateTime(articleOutModel.getUpdateDate()));
            response.setBowseNum(articleOutModel.getBowseNum());
            response.setComments(articleOutModel.getComments());
            long count = MongoUtils.count(UserLogEnum.KG_USER_COLLECT.getTable(), new BasicDBObject("articleId", Long.valueOf(articleOutModel.getArticleId())));
            response.setCollect((int) count);
            response.setArticleTagnames(articleOutModel.getArticleTagnames());
            response.setDisplayOrder(articleOutModel.getDisplayOrder());
            response.setArticleDescription(articleOutModel.getArticleDescription());
            response.setColumnname(articleOutModel.getColumnname());
            response.setUsername(articleOutModel.getUsername());
            response.setSecondcolumnname(articleOutModel.getSecondcolumnname());
            response.setArticlefrom(articleOutModel.getArticlefrom());
            response.setBrowseNum(articleOutModel.getBrowseNum());
            response.setDisplayStatus(String.valueOf(articleOutModel.getDisplayStatus()));
            response.setPublishKind(articleOutModel.getPublishKind());
            response.setArticleType(articleOutModel.getArticleType());
            response.setProfileavatar(articleOutModel.getProfileavatar());
            if (articleOutModel.getArticleMark() == 1) {
                // 优质文章
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
            // }

            listrResponse.add(response);

        }

        page.setData(listrResponse);

        if (listrResponse.size() > 0) {
            jedisUtils.set(key, JSON.toJSONString(page), JedisKey.ONE_WEEK);
            logger.info("首页分页信息已存入缓存-------");
        }
        return page;
    }

    @Override
    public PageModel<ArticleResponse> selectArticleRecomm(ArticleRequest request, PageModel<ArticleResponse> page,
                                                          HttpServletRequest httpServletRequest) {
        logger.info("首页热门文章：{},分页入参：{}", JSON.toJSONString(request), JSON.toJSONString(page));
        logger.info("请求开始=========");

        //根据栏目ID从Redis获取文章此时的总量（从缓存中获取）
        long count = 0;
        ArticleInModel inModel = new ArticleInModel();
        String scount = jedisUtils.get(JedisKey.getCountArticleNum("-1"));
        if (StringUtils.isNotBlank(scount)) {
            logger.info("从缓存中获取栏目下文章总量");
            count = Long.parseLong(scount);
        } else {
            //缓存没有才从数据库中获取
            logger.info("从数据库中获取栏目下文章总量");
            count = articleRMapper.selectCountArticleRecomm(inModel);
            jedisUtils.set(JedisKey.getCountArticleNum("-1"), String.valueOf(count));
        }


        //获取文章此时的总量
        long totalCount = getArticleColumnTotalCount(httpServletRequest, page.getCurrentPage(), "-1", count);
        page = buildSelectArticleRecomm(page, totalCount, 1);

        return page;
    }

    @Override
	public PageModel<ArticleResponse> buildSelectArticleRecomm(PageModel<ArticleResponse> page, long totalCount, Integer types) {
		boolean ifRandom = ifRandom(page.getCurrentPage(), page.getPageSize());
        ArticleInModel inModel = new ArticleInModel();
        page.setTotalNumber(totalCount);
        //判断是否超过总页数
        if (page.getCurrentPage() > page.getTotalPage()) {
            page.setData(null);
            return page;
        }
        String key = JedisKey.getArticleWithPage("-1", "APP", totalCount, page.getCurrentPage());
        if (types != 2) {
            PageModel<ArticleResponse> redisPagemodel = JSON.parseObject(jedisUtils.get(key), PageModel.class);
            if (redisPagemodel != null) {
                logger.info("首页分页信息从缓存中获取-------");
                if(ifRandom){
					shuffleArticleResponse(JSON.parseArray(JSON.toJSONString(redisPagemodel.getData()), ArticleResponse.class), redisPagemodel);
				}
                return redisPagemodel;
            }
        }

        int startIndex = getArticleRecommStartIndex(page.getCurrentPage(), Integer.parseInt(String.valueOf(totalCount)), page.getPageSize());
        inModel.setStart(startIndex);
        if (startIndex < 0) {
            inModel.setStart(0);
            inModel.setLimit((int) totalCount % page.getPageSize());
        } else {
            inModel.setLimit(page.getPageSize());
        }

        logger.info("【查询推荐列表】-> ArticleInModel：{}，totalCount：{}", JSONObject.toJSONString(inModel), totalCount);
        List<ArticleOutModel> listArticle = articleRMapper.selectArticleRecomm(inModel);
        Collections.reverse(listArticle);
        List<ArticleResponse> listrResponse = new ArrayList<>();
        for (ArticleOutModel articleOutModel : listArticle) {
            ArticleResponse response = new ArticleResponse();
            BeanUtils.copyProperties(articleOutModel, response);
            response.setDisplayStatus(String.valueOf(articleOutModel.getDisplayStatus()));
            response.setCreateUser(articleOutModel.getCreateUser().toString());
            response.setUpdateDate(DateUtils.calculateTime(articleOutModel.getUpdateDate()));
            if (articleOutModel.getArticleMark() == 1) {
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
            response.setVideoUrl(articleOutModel.getVideoUrl());
            if (articleOutModel.getPublishKind() == 2) {
                // 当为视频时返回评论数
                Integer comments = userCommentRMapper.getCommentTotalCount(Long.valueOf(articleOutModel.getArticleId()));
                response.setComments(comments);
            } else {
                response.setComments(null);
            }

            response.setBlockchainUrl(articleOutModel.getBlockchainUrl());

            listrResponse.add(response);
        }

        page.setData(listrResponse);
        if (listrResponse.size() > 0) {
            jedisUtils.set(key, JSON.toJSONString(page), JedisKey.ONE_WEEK);
            logger.info("首页分页信息已存入缓存-------");
        }

		if(ifRandom){
			shuffleArticleResponse(listrResponse, page);
		}
        return page;
    }

    private boolean ifRandom(int currentPage, int pageSize){
		if(currentPage*pageSize <= ARTICLE_RANDOM_LIMIT){
			return true;
		}else {
			return false;
		}
	}

	private void shuffleArticleResponse(List<ArticleResponse> articleResponses, PageModel<ArticleResponse> pageModel){
		List<ArticleResponse> topArticleList = Lists.newArrayList();
		List<ArticleResponse> noTopArticleList = Lists.newArrayList();
		for (ArticleResponse response: articleResponses) {
			if(Integer.parseInt(response.getDisplayStatus()) == ArticleDisplayStatusEnum.HOME_TOP.getStatus()){
				topArticleList.add(response);
			}else {
				noTopArticleList.add(response);
			}
		}
		List<ArticleResponse> shuffleList = Lists.newArrayList();
		shuffleList.addAll(topArticleList);
		Collections.shuffle(noTopArticleList);
		shuffleList.addAll(noTopArticleList);
		pageModel.setData(shuffleList);
	}

    /**
     * 获取分页参数
     *
     * @param httpServletRequest
     * @param page
     * @param inModel
     * @return
     */
    public ArticleInModel getArticleInModel(HttpServletRequest httpServletRequest, PageModel<ArticleResponse> page,
                                            ArticleInModel inModel) {
        String key = getRedisKey(httpServletRequest);
        int currentPage = page.getCurrentPage();
        int pageSize = page.getPageSize();
        int totalCount = getArticleRecommTotalCount(currentPage, key, inModel);
        int startIndex = getArticleRecommStartIndex(currentPage, totalCount, pageSize);
        inModel.setStart(startIndex);
        inModel.setLimit(pageSize);
        inModel.setTotalCount(totalCount);
        return inModel;
    }

    /**
     * 获取查询真实开始下标
     *
     * @param totalCount
     * @param pageSize
     * @return
     */
    public int getArticleRecommStartIndex(int currentPage, int totalCount, int pageSize) {
        int endIndex = totalCount - (currentPage - 1) * pageSize;
        int startIndex = endIndex - pageSize;
        return startIndex;
    }

    /**
     * 获取推荐资讯总数 当为第一页时将查询出的总数添加到redis
     *
     * @param currentPage
     * @param key
     * @param inModel
     * @return
     */
    private int getArticleRecommTotalCount(int currentPage, String key, ArticleInModel inModel) {
        long count = articleRMapper.selectCountArticleRecomm(inModel);
        if (currentPage > 1) {
            String totalCountStr = jedisUtils.get(key);
            return StringUtils.isNotEmpty(totalCountStr) ? Integer.valueOf(totalCountStr.replace("\"", ""))
                    : (int) count;
        }
        if (currentPage == 1) {
            jedisUtils.set(key, String.valueOf(count), JedisKey.ONE_DAY);
            return (int) count;
        }
        return 0;
    }


    public long getArticleColumnTotalCount(HttpServletRequest servletRequest, int currentPage, String columnId, long count) {
        String key = JedisKey.getUserCurrentPage(getRedisKey(servletRequest) + "/columnId" + columnId);
        if (currentPage > 1) {
            String totalCountStr = jedisUtils.get(key);
            return StringUtils.isNotEmpty(totalCountStr) ? Integer.valueOf(totalCountStr.replace("\"", ""))
                    : count;
        }
        if (currentPage == 1) {
            logger.info("将最新的栏目总量与用户当前阅读关联------");
            jedisUtils.set(key, String.valueOf(count), JedisKey.ONE_DAY);
            return count;
        }
        return 0;
    }

    /**
     * 获取推荐列表rediskey
     *
     * @param httpServletRequest
     * @return
     */
    private String getRedisKey(HttpServletRequest httpServletRequest) {
        String deviceId = "";
        int os = httpServletRequest.getIntHeader("os_version");
        if (os == 1 || os == 2) {
            deviceId = httpServletRequest.getHeader("device_id");
        } else if (os == RegisterOriginEnum.M.getOrigin()) {
            deviceId = httpServletRequest.getParameter("userIp");
        } else {
            deviceId = HttpUtil.getIpAddr(httpServletRequest);
        }

        return deviceId + "/totalCount";
    }

    @Override
    public PageModel<SitemapResponse> sitemap(ArticleRequest request, PageModel<SitemapResponse> page) {
        long start = System.currentTimeMillis();
        ArticleInModel inModel = new ArticleInModel();
        inModel.setStart((page.getCurrentPage() - 1) * page.getPageSize());
        inModel.setLimit(page.getPageSize());
        inModel.setDisplayStatus(request.getDisplayStatus());
        List<ArticleOutModel> listArticle = articleRMapper.selectArticleAll(inModel);
        List<SitemapResponse> listrResponse = new ArrayList<SitemapResponse>();
        for (ArticleOutModel articleOutModel : listArticle) {
            SitemapResponse response = new SitemapResponse();
            response.setArticleId(articleOutModel.getArticleId());
            response.setArticleTitle(articleOutModel.getArticleTitle());

            listrResponse.add(response);

        }

        page.setData(listrResponse);

        logger.info("查询结束，耗时：{}", System.currentTimeMillis() - start);
        return page;
    }

    /**
     * 24h咨询
     */
    @Override
    public Result<List<ArticleResponse>> selectAdvisory() {

        List<ArticleOutModel> outModel = articleRMapper.selectAdvisory();
        List<ArticleResponse> listresponse = new ArrayList<ArticleResponse>();
        for (ArticleOutModel articleOutModel : outModel) {
            ArticleResponse response = new ArticleResponse();
            response.setArticleId(articleOutModel.getArticleId());
            response.setArticleTitle(articleOutModel.getArticleTitle());
            response.setArticleTags(articleOutModel.getArticleTags());
            response.setArticleImage(articleOutModel.getArticleImage());
            response.setArticleType(articleOutModel.getArticleType());
            response.setArticleSource(articleOutModel.getArticleSource());
            response.setArticleLink(articleOutModel.getArticleLink());
            response.setColumnId(articleOutModel.getColumnId());
            response.setSecondColumn(articleOutModel.getSecondColumn());

            response.setDisplayOrder(articleOutModel.getDisplayOrder());
            response.setCommentSet(articleOutModel.getCommentSet());
            response.setPublishSet(articleOutModel.getPublishSet());
            response.setPublishTime(articleOutModel.getPublishTime());
            response.setPublishStatus(articleOutModel.getPublishStatus());
            response.setCreateUser(articleOutModel.getCreateUser().toString());
            response.setCreateDate(articleOutModel.getCreateDate());
            response.setUpdateDate(DateUtils.calculateTime(articleOutModel.getUpdateDate()));
            response.setUpdateUser(articleOutModel.getUpdateUser());
            response.setSysUser(articleOutModel.getSysUser());
            response.setRefuseReason(articleOutModel.getRefuseReason());
            response.setArticlefrom(articleOutModel.getArticlefrom());

            response.setBowseNum(articleOutModel.getBowseNum());
            listresponse.add(response);
        }

        return new Result<List<ArticleResponse>>(listresponse);
    }

    /**
     * top排行榜
     */
    @Cacheable(key = "selectTopArticle", expire = 2, dateType = DateEnum.HOURS)
    @Override
    public Result<List<ArticleResponse>> selectTopArticle() {

        Date start = DateUtils.getBeforeDay(new Date(), 3);
        List<Bson> bsonList = conditionList(start);
        MongoCursor<Document> cursor = MongoUtils.aggregate(UserLogEnum.KG_USER_BROWSE.getTable(), bsonList);
        List<ArticleResponse> responses = new ArrayList<>();
        if (cursor != null) {
            while (cursor.hasNext()) {
                ArticleResponse response = new ArticleResponse();
                Document document = cursor.next();
                Long articleId = document.getLong("_id");
                ArticleInModel inModel = new ArticleInModel();
                inModel.setArticleId(articleId);
                ArticleOutModel outModel = articleRMapper.selectArticleBase(inModel);
                if (outModel == null) {
                    continue;
                }
                response.setArticleId(outModel.getArticleId());
                response.setArticleTitle(outModel.getArticleTitle());
                response.setArticleTags(outModel.getArticleTags());
                response.setArticleImage(outModel.getArticleImage());
                response.setUpdateDate(DateUtils.calculateTime(outModel.getUpdateDate()));
                response.setArticlefrom(outModel.getArticlefrom());
                response.setArticleImgSize(outModel.getArticleImgSize());
                responses.add(response);
            }
        }
        if (responses.size() > 10) {
            responses = responses.subList(0, 10);
        }
        return new Result<>(responses);
    }


    private List<Bson> conditionList(Date start) {
        List<Bson> conditionList = new ArrayList<>();
        BasicDBObject querryBy = new BasicDBObject();
        querryBy.put("collectDate", BasicDBObjectBuilder.start(Seach.GTE.getOperStr(), start).get());
        querryBy.put("publishKind", 1);

        Bson querryCondition = new BasicDBObject("$match", querryBy);
        conditionList.add(querryCondition);

        BasicDBObject groupBy = new BasicDBObject();
        groupBy.append("_id", "$articleId");
        groupBy.append("count", new BasicDBObject("$sum", 1));
        Bson group = new BasicDBObject("$group", groupBy);
        conditionList.add(group);
        conditionList.add(new BasicDBObject("$sort", new BasicDBObject("count", -1)));
        conditionList.add(new BasicDBObject("$limit", 100));
        return conditionList;
    }

    /**
     * 只获取文章文本信息，并缓存
     *
     * @param
     * @return
     */
    @Cacheable(category = "ArticleAppServiceImpl/getArticleOutModelText", key = "#{request.articleId}", expire = 2, dateType = DateEnum.HOURS)
    @Override
    public ArticleResponse getArticleDetailText(ArticleRequest request) {
        ArticleInModel inModel = new ArticleInModel();
        inModel.setArticleId(request.getArticleId());
        inModel.setUserId(request.getUserId());
        ArticleOutModel outModel = articleRMapper.selectTextDetailsForApp(inModel);
        ArticleResponse response = new ArticleResponse();
        response.setArticleTitle(outModel.getArticleTitle());
        response.setArticleDescription(outModel.getArticleDescription());
        response.setArticleText(outModel.getArticleText());
        return response;
    }

    /**
     * 文章详情
     */
    @Override
    public Result<ArticleResponse> selectByIdDetails(ArticleRequest request) {
        logger.info("文章详情：ArticleRequest={}", JSON.toJSONString(request));
        ArticleInModel inModel = new ArticleInModel();
        inModel.setArticleId(request.getArticleId());
        inModel.setUserId(request.getUserId());
        ArticleOutModel articleOutModel = articleRMapper.selectBaseDetailsForApp(inModel);

        //文章详情TDK获取
        KgSeoTdkInModel kgSeoTdkInModel = new KgSeoTdkInModel();
        kgSeoTdkInModel.setTargetId(request.getArticleId());
        kgSeoTdkInModel.setTargetType(3);
		KgSeoTdkOutModel kgSeoTdkOutModel = kgSeoTdkRMapper.detailTdk(kgSeoTdkInModel);
		KgSeoTdkResponse kgSeoTdkResponse = new KgSeoTdkResponse();
		if(kgSeoTdkOutModel!=null){
			BeanUtils.copyProperties(kgSeoTdkOutModel, kgSeoTdkResponse);
		}


        if (articleOutModel == null) {
            return new Result<>();
        }
        UserCollectInModel collectInModel = new UserCollectInModel();
        collectInModel.setArticleId(Long.parseLong(articleOutModel.getArticleId()));
        AccountFlowInModel accountFlowInModel = new AccountFlowInModel();
        accountFlowInModel.setUserId(articleOutModel.getCreateUser());
        accountFlowInModel.setBusinessTypeId(BusinessTypeEnum.TIPSIN.getStatus());
        accountFlowInModel.setArticleId(request.getArticleId());
        int tipsinCount = 0;

        ArticleResponse response = new ArticleResponse();
        response.setArticleId(articleOutModel.getArticleId());
        response.setArticleTitle(articleOutModel.getArticleTitle());
        response.setArticleText(articleOutModel.getArticleText());
        response.setArticleTags(articleOutModel.getArticleTags());
        response.setArticleImage(articleOutModel.getArticleImage());
        response.setArticleType(articleOutModel.getArticleType());
        response.setArticleSource(articleOutModel.getArticleSource());
        response.setArticleDescription(articleOutModel.getArticleDescription());
        response.setArticleLink(articleOutModel.getArticleLink());
        response.setColumnId(articleOutModel.getColumnId());
        response.setSecondColumn(articleOutModel.getSecondColumn());
        // app1.1.0屏蔽阅读奖励
        response.setBonusStatus(articleOutModel.getBonusStatus());

        response.setDisplayOrder(articleOutModel.getDisplayOrder());
        response.setCommentSet(articleOutModel.getCommentSet());
        response.setPublishSet(articleOutModel.getPublishSet());
        response.setPublishTime(articleOutModel.getPublishTime());
        response.setPublishStatus(articleOutModel.getPublishStatus());
        response.setCreateUser(articleOutModel.getCreateUser().toString());
        response.setCreateDate(articleOutModel.getCreateDate());
        response.setUpdateDate(DateUtils.calculateTime(articleOutModel.getUpdateDate()));
        if (articleOutModel.getAuditDate() != null) {
            response.setUpdateTimestamp(String.valueOf(articleOutModel.getAuditDate().getTime()));
        } else {
            response.setUpdateTimestamp(String.valueOf(articleOutModel.getUpdateDate().getTime()));
        }

        response.setUpdateUser(articleOutModel.getUpdateUser());
        response.setSysUser(articleOutModel.getSysUser());
        response.setRefuseReason(articleOutModel.getRefuseReason());
        response.setBowseNum(articleOutModel.getBowseNum());
        response.setComments(articleOutModel.getComments());
        response.setCommentSet(articleOutModel.getCommentSet());
        response.setGetStatus(articleOutModel.getGetStatus());// 阅读奖励领取状态
        response.setArticlefrom(articleOutModel.getArticlefrom());

        response.setPublishKind(articleOutModel.getPublishKind());
        response.setVideoUrl(articleOutModel.getVideoUrl());
        response.setVideoFilename(articleOutModel.getVideoFilename());

        KgArticleStatisticsInModel statinModel = new KgArticleStatisticsInModel();
        statinModel.setArticleId(request.getArticleId());
        KgArticleStatisticsOutModel statisticsOutModel = kgArticleStatisticsRMapper.selectByPrimaryKey(statinModel);

        if (statisticsOutModel != null) {
            response.setBrowseNum(statisticsOutModel.getBrowseNum());
            response.setCollect(statisticsOutModel.getCollectNum());
            response.setThumbupNum(statisticsOutModel.getThumbupNum());
        }

        response.setTipsinCount(tipsinCount + "");

        response.setColumnname(articleOutModel.getColumnname());


        response.setArticleTagnames(articleOutModel.getArticleTagnames());
        response.setUserColumn(articleOutModel.getUserColumn());
        response.setColumnAvatar(articleOutModel.getColumnAvatar());

        if (StringUtils.isEmpty(request.getUserId())) {
            response.setCollectstatus(0);
            response.setPraisestatus(0);
        } else {
            BasicDBObject query = new BasicDBObject();
            query.put("userId", Long.valueOf(request.getUserId()));
            query.put("articleId", request.getArticleId());
            long countCollect = MongoUtils.count(UserLogEnum.KG_USER_COLLECT.getTable(), query);
            long countLike = MongoUtils.count(UserLogEnum.KG_USER_LIKE.getTable(), query);
            response.setPraisestatus(countLike > 0 ? 1 : 0);
            response.setCollectstatus(countCollect > 0 ? 1 : 0);
            logger.info("【文章详情】查询用户是否收藏点赞 userId：" + request.getUserId() + "  articleId：" + request.getArticleId() + "  countCollect：" + countCollect + "  countLike：" + countLike);
        }
        Result<SiteinfoResponse> siteinfoResponse = siteinfoService.selectSiteInfo();
        response.setCommentAudit(siteinfoResponse.getData().getCommentAudit());

        // 奖励收入
        String amount = articleAppService.getArticleAmount(articleOutModel.getCreateUser(), request.getArticleId());
        response.setRewardIncome(StringUtils.isEmpty(amount) ? "0.000" : String.format("%.3f", new BigDecimal(amount)));
        response.setSeoTdk(kgSeoTdkResponse);

        sendArticleSyncMessage(request.getArticleId());
        return new Result<ArticleResponse>(response);
    }

    /**
     * 感兴趣的文章
     */
    @Cacheable(key = "#{request.articleId}", expire = 2, dateType = DateEnum.HOURS)
    @Override
    public Result<List<ArticleResponse>> selectRelatedArticle(ArticleRequest request) {
        logger.info("文章详情入参：ArticleRequest={}", JSON.toJSONString(request));

        Long articleId = request.getArticleId();

        List<ArticleResponse> responses = new ArrayList<ArticleResponse>();
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
        List<ArticleOutModel> outModel = articleRMapper.selectColumnid(cInModel);
        List<String> articleIds = Lists.newArrayList();
        for (ArticleOutModel articleOutModel : outModel) {
            ArticleResponse articleResponse = new ArticleResponse();
            articleResponse.setArticleImage(articleOutModel.getArticleImage());
            articleResponse.setArticleTitle(articleOutModel.getArticleTitle());
            articleResponse.setArticleId(articleOutModel.getArticleId());
            articleResponse.setColumnId(articleOutModel.getColumnId());
            articleResponse.setArticlefrom(articleOutModel.getArticlefrom());
            articleResponse.setPublishKind(articleOutModel.getPublishKind());
            articleResponse.setArticleImgSize(articleOutModel.getArticleImgSize());
            articleResponse.setUsername(articleOutModel.getUsername());
            if (articleOutModel.getArticleMark() == 1) {
                // 优质文章
                articleResponse.setArticleType(1);
            } else {
                articleResponse.setArticleType(null);
            }
            if (articleOutModel.getAuditDate() != null) {
                articleResponse.setUpdateTimestamp(String.valueOf(articleOutModel.getAuditDate().getTime()));
            } else {
                articleResponse.setUpdateTimestamp(String.valueOf(articleOutModel.getUpdateDate().getTime()));
            }

            responses.add(articleResponse);
            articleIds.add(articleOutModel.getArticleId());
        }

        return new Result<>(responses);
    }

    /**
     * 详情页上下两篇文章
     */
    @Override
    public Result<List<UpdownArticleResponse>> UpdownArticle(ArticleRequest request) {
        logger.info("详情页上下两篇文章入参：ArticleRequest={}", JSON.toJSONString(request));
        ArticleInModel inModel = new ArticleInModel();

        inModel.setArticleId(request.getArticleId());
        // 下一篇文章
        List<ArticleOutModel> listUnde = articleRMapper.UndeArticle(inModel);

        // 上一篇文章
        List<ArticleOutModel> listOn = articleRMapper.OnArticle(inModel);
        List<UpdownArticleResponse> listresponses = new ArrayList<UpdownArticleResponse>();
        UpdownArticleResponse responses = new UpdownArticleResponse();

        if (listUnde != null && listUnde.size() > 0) {
            responses.setDownArticleId(listUnde.get(0).getArticleId());
            responses.setDownArticleTitle(listUnde.get(0).getArticleTitle());
            responses.setDownArticleAuthor(listUnde.get(0).getCreateUser().toString());
            responses.setDownPublishKind(listUnde.get(0).getPublishKind());
        }
        if (listOn != null && listOn.size() > 0) {
            responses.setUpArticleId(listOn.get(0).getArticleId());
            responses.setUpArticleTitle(listOn.get(0).getArticleTitle());
            responses.setUpArticleAuthor(listOn.get(0).getCreateUser().toString());
            responses.setUpPublishKind(listOn.get(0).getPublishKind());
        }

        listresponses.add(responses);

        return new Result<List<UpdownArticleResponse>>(listresponses);
    }

    /**
     * 二级频道页右侧 热门资讯
     *
     * @param request
     * @return
     */
    @Override
    public Result<List<ArticleResponse>> getChannelAll(ArticleRequest request) {
        logger.info("二级频道页右侧 热门资讯入参：ArticleRequest={}", JSON.toJSONString(request));
        ArticleInModel inModel = new ArticleInModel();
        inModel.setColumnId(request.getColumnId());
        List<ArticleOutModel> list = articleRMapper.getChannelAll(inModel);
        List<ArticleResponse> listresponse = new ArrayList<ArticleResponse>();
        for (ArticleOutModel articleOutModel : list) {
            ArticleResponse response = new ArticleResponse();
            response.setArticleId(articleOutModel.getArticleId());
            response.setArticleTitle(articleOutModel.getArticleTitle());
            response.setArticleTags(articleOutModel.getArticleTags());
            response.setArticleImage(articleOutModel.getArticleImage());
            response.setUpdateDate(DateUtils.calculateTime(articleOutModel.getUpdateDate()));
            if (articleOutModel.getAuditDate() != null) {
                response.setUpdateTimestamp(String.valueOf(articleOutModel.getAuditDate().getTime()));
            } else {
                response.setUpdateTimestamp(String.valueOf(articleOutModel.getUpdateDate().getTime()));
            }

            response.setArticleImgSize(articleOutModel.getArticleImgSize());
            response.setArticleTagnames(articleOutModel.getArticleTagnames());
            response.setArticleTags(articleOutModel.getArticleTags());
            listresponse.add(response);

        }
        return new Result<List<ArticleResponse>>(listresponse);
    }

    /**
     * 前台发布文章
     */
    @Override
    @Transactional
    public Result<ArticleResponse> AddArticle(ArticleRequest request) {
        // logger.info("前台发布文章入参：ArticleRequest={}",
        // JSON.toJSONString(request));

        AccountInModel accountInModel = new AccountInModel();
        accountInModel.setUserId(request.getCreateUser());
        AccountOutModel outModel = accountRMapper.selectOutUserId(accountInModel);
        Lock lock = new Lock();
        String key = USER_REWARD_LOCK_KEY + accountInModel.getUserId() + outModel.getAccountId();
        ArticleResponse articleResponse = new ArticleResponse();
        try {
            lock.lock(key);

            // app1.1.0屏蔽阅读奖励
            // BigDecimal bonusValuesum = new BigDecimal(0.000);
            // int maxPeople = 0;

            ArticleInModel inModel = new ArticleInModel();
            if (request.getArticleId() == null) {
                inModel.setArticleId(idGenerater.nextId());
            } else {
                inModel.setArticleId(request.getArticleId());
            }

            inModel.setArticleTitle(request.getArticleTitle());
            inModel.setArticleText(request.getArticleText());
            inModel.setUserId(request.getCreateUser().toString());

            String str = new String();
            String[] strs = request.getArticleTagnames().split(",");
            String tagname = null;
            for (int i = 0, len = strs.length; i < len; i++) {
                if (null != tagname && tagname.equals(strs[i].toString())) {
                    throw new ApiMessageException("标签重复");
                }
                tagname = strs[i].toString();
                TagsInModel model = new TagsInModel();
                model.setTagName(tagname);
                // 关联Tags
                TagsOutModel tagsOutModel = tagsRMapper.getTags(model);
                if (null != tagsOutModel) {
                    model.setTagId(tagsOutModel.getTagId());
                    tagsWMapper.UpdateNum(model);
                    str = str + model.getTagId().toString() + ",";
                } else {
                    model.setTagId(idGenerater.nextId());
                    model.setCreateDate(new Date());
                    model.setCreateUser(request.getCreateUser());
                    String TagName = strs[i].toString();
                    model.setTagName(TagName);

                    tagsWMapper.insertSelective(model);
                    str = str + model.getTagId().toString() + ",";
                }
            }

            int indx = str.lastIndexOf(",");
            if (indx != -1) {
                str = str.substring(0, indx) + str.substring(indx + 1, str.length());
            }
            inModel.setArticleTags(str);
            inModel.setArticleTagnames(request.getArticleTagnames());
            inModel.setArticleDescription(request.getArticleDescription());
            inModel.setArticleImage(request.getArticleImage());
            inModel.setArticleType(request.getArticleType());
            inModel.setBonusStatus(request.getBonusStatus());

            inModel.setPublishStatus(request.getPublishStatus().toString());

            inModel.setArticleLink(request.getArticleLink());
            inModel.setArticleSource(request.getArticleSource());
            inModel.setTextnum(request.getTextnum());
            inModel.setPublishKind(request.getPublishKind());
            inModel.setVideoUrl(request.getVideoUrl());
            inModel.setVideoFilename(request.getVideoFilename());
            inModel.setIfPush(0);
            inModel.setIfPlatformPublishAward(0);

            // 保存文章
            if (request.getArticleId() == null) {
                inModel.setCreateUser(request.getCreateUser());
                inModel.setCreateDate(new Date());
                articleWMapper.AddArticle(inModel);
            } else {
                inModel.setUpdateUser(request.getCreateUser());
                inModel.setUpdateDate(new Date());
                inModel.setCreateDate(new Date());
                articleWMapper.updateBySelective(inModel);
            }

            // app1.1.0屏蔽阅读奖励
            // boolean bonusStatus = true;
            // logger.info("文章保存用户id {},文章Id{}", inModel.getUserId(),
            // inModel.getArticleId());
            // 修改文章时,修改统计表数据，删除阅读奖励表文章下的数据,生产新的流水
            // if (request.getArticleId() != null) {
            // // 查询文章下未生效的阅读奖励
            // KgArticleBonusInModel bonusInModel = new KgArticleBonusInModel();
            // bonusInModel.setArticleId(request.getArticleId());
            // List<KgArticleBonusOutModel> bonusOutModelsList =
            // kgArticleBonusRMapper
            // .getIneffectiveBonus(bonusInModel);
            //
            // if (bonusOutModelsList.size() == 0) {
            // bonusStatus = false;
            // List<KgArticleBonusRequest> list = new
            // ArrayList<KgArticleBonusRequest>();
            // for (KgArticleBonusOutModel kgArticleBonusOutModel :
            // bonusOutModelsList) {
            // KgArticleBonusRequest bonusRequest = new KgArticleBonusRequest();
            // bonusRequest.setArticleId(request.getArticleId());
            // bonusRequest.setBonusType(kgArticleBonusOutModel.getBonusType());
            // bonusRequest.setBonusSecondType(kgArticleBonusOutModel.getBonusSecondType());
            // bonusRequest.setBrowseTime(kgArticleBonusOutModel.getBrowseTime());
            // bonusRequest.setBonusStatus(kgArticleBonusOutModel.getBonusStatus());
            // bonusRequest.setCreateUser(request.getCreateUser());
            // bonusRequest.setCreateDate(new Date());
            // bonusRequest.setUpdateUser(request.getCreateUser());
            // bonusRequest.setBonusValue(kgArticleBonusOutModel.getBonusValue());
            // bonusRequest.setMaxPeople(kgArticleBonusOutModel.getMaxPeople());
            // bonusRequest.setBonusKind(kgArticleBonusOutModel.getBonusKind());
            // list.add(bonusRequest);
            // }
            // // request.setList(list);
            //
            // KgArticleStatisticsInModel aStInModel = new
            // KgArticleStatisticsInModel();
            // aStInModel.setArticleId(request.getArticleId());
            // // 查流水表数据
            // KgArticleStatisticsOutModel aStOutModel =
            // kgArticleStatisticsRMapper.selectByPrimaryKey(aStInModel);
            // if (aStOutModel != null) {
            // KgArticleStatisticsHisInModel hisInModel = new
            // KgArticleStatisticsHisInModel();
            // hisInModel.setArticleId(aStOutModel.getArticleId());
            // hisInModel.setBrowseNum(aStOutModel.getBrowseNum());
            // hisInModel.setShareNum(aStOutModel.getShareNum());
            // hisInModel.setThumbupNum(aStOutModel.getThumbupNum());
            // hisInModel.setCollectNum(aStOutModel.getCollectNum());
            // hisInModel.setBonusNum(aStOutModel.getBonusNum());
            // hisInModel.setBonusValue(aStOutModel.getBonusValue());
            // hisInModel.setBonusTotal(aStOutModel.getBonusTotal());
            // hisInModel.setCreateUser(aStOutModel.getCreateUser());
            // // 存文章统计历史表
            // kgArticleStatisticsHisWMapper.insertSelective(hisInModel);
            // // 修改文章统计表 奖励已领取人数，奖励已领取金额，奖励总金额 全部为0
            //
            // kgArticleStatisticsWMapper.initializeThe(aStInModel);
            //
            // }
            //
            // // 删除文章下的阅读奖励
            // KgArticleBonusInModel kgArInModel = new KgArticleBonusInModel();
            // kgArInModel.setArticleId(request.getArticleId());
            // kgArticleBonusWMapper.deleteByArticle(kgArInModel);
            // }
            // }

            // 设置阅读奖励,在文章保存成功和文章状态是审核中才设置阅读奖励
            // app1.1.0屏蔽阅读奖励
            // if (!"4".equals(request.getPublishStatus())) {
            // if (bonusStatus == false) {
            // if (request.getList() != null) {
            //
            // List<KgArticleBonusRequest> list = request.getList();
            // for (KgArticleBonusRequest kgArticleBonusRequest : list) {
            // if (request.getArticleId() != null || inModel.getArticleId() !=
            // null) {
            //
            // KgArticleBonusInModel inModelbonus = new KgArticleBonusInModel();
            // inModelbonus.setBonusId(idGenerater.nextId());
            // inModelbonus.setArticleId(inModel.getArticleId());
            // inModelbonus.setBonusType(kgArticleBonusRequest.getBonusType() +
            // "");
            // inModelbonus.setBonusSecondType(kgArticleBonusRequest.getBonusSecondType()
            // + "");
            // inModelbonus.setBonusKind(kgArticleBonusRequest.getBonusKind());
            // inModelbonus.setBrowseTime(kgArticleBonusRequest.getBrowseTime());
            // inModelbonus.setBonusStatus(kgArticleBonusRequest.getBonusStatus());
            // inModelbonus.setCreateUser(request.getCreateUser());
            // inModelbonus.setCreateDate(new Date());
            // inModelbonus.setUpdateUser(request.getCreateUser());
            // inModelbonus.setBonusValue(kgArticleBonusRequest.getBonusValue());
            // inModelbonus.setMaxPeople(kgArticleBonusRequest.getMaxPeople());
            // if (kgArticleBonusRequest.getMaxPeople() != null) {
            // maxPeople += kgArticleBonusRequest.getMaxPeople();
            // if (null != kgArticleBonusRequest.getBonusKind()
            // && kgArticleBonusRequest.getBonusKind().intValue() == 2) {
            // bonusValuesum =
            // bonusValuesum.add(kgArticleBonusRequest.getBonusValue());
            // } else {
            // bonusValuesum =
            // bonusValuesum.add(kgArticleBonusRequest.getBonusValue()
            // .multiply(new BigDecimal(kgArticleBonusRequest.getMaxPeople())));
            // }
            // }
            //
            // if ("0".equals(request.getPublishStatus())) {
            // inModelbonus.setBonusStatus(0);
            // } else {
            // inModelbonus.setBonusStatus(1);
            // }
            // kgArticleBonusWMapper.insertSelective(inModelbonus);
            // }
            // }
            //
            // int sum = outModel.getBalance().compareTo(bonusValuesum);
            //
            // if (sum == -1) {
            // throw new ApiMessageException("奖励额度大于账户余额，请重新设置您的阅读奖励");
            // }
            //
            // // 保存阅读奖励设置后，保存金额到流水表
            // UserkgOutModel umodel =
            // userRMapper.getUserDetails(Long.parseLong(inModel.getUserId()));
            // AccountFlowInModel accountFlowInModel = new AccountFlowInModel();
            // long flowid = idGenerater.nextId();
            // accountFlowInModel.setAccountFlowId(flowid);
            // accountFlowInModel.setRelationFlowId(flowid);
            // accountFlowInModel.setUserId(Long.parseLong(umodel.getUserId()));
            // accountFlowInModel.setUserPhone(umodel.getUserMobile());
            // accountFlowInModel.setUserEmail(umodel.getUserEmail());
            // accountFlowInModel.setAmount(bonusValuesum.multiply(new
            // BigDecimal(-1)));
            // accountFlowInModel.setFlowDate(new Date());
            // accountFlowInModel.setArticleId(inModel.getArticleId());
            // accountFlowInModel.setBonusTotalPerson(maxPeople);
            // accountFlowInModel.setAccountAmount(bonusValuesum.multiply(new
            // BigDecimal(-1)));
            // accountFlowInModel
            // .setFlowDetail("为文章" + "<<" + request.getArticleTitle() + ">>" +
            // "设置阅读奖励 已生效");
            // accountFlowInModel.setBusinessTypeId(BusinessTypeEnum.READAWARD.getStatus());
            // accountFlowInModel.setFlowStatus(FlowStatusEnum.VALID.getStatus());
            // accountFlowWMapper.insertFlowSelective(accountFlowInModel);
            //
            // logger.info("设置阅读奖励保存用户id {}，文章Id{}", accountInModel.getUserId(),
            // inModel.getArticleId());
            //
            // logger.info("设置阅读奖励用户id {}，文章Id{}", accountInModel.getUserId(),
            // inModel.getArticleId());
            //
            // accountInModel.setBalance(bonusValuesum);
            //
            // accountWMapper.updateOutUserId(accountInModel);
            //
            // }
            //
            // KgArticleStatisticsInModel statisticsInModel = new
            // KgArticleStatisticsInModel();
            // statisticsInModel.setArticleId(inModel.getArticleId());
            // KgArticleStatisticsOutModel statisticsOutModel =
            // kgArticleStatisticsRMapper
            // .selectByPrimaryKey(statisticsInModel);
            // statisticsInModel.setBonusTotal(bonusValuesum);
            // statisticsInModel.setCreateUser(request.getCreateUser());
            // // 文章统计表金额操作
            // if (statisticsOutModel == null) {
            // logger.info("统计表存入文章ID {}", statisticsInModel.getArticleId());
            // // statisticsInModel.setArticleId(idGenerater.nextId());
            // kgArticleStatisticsWMapper.insertSelective(statisticsInModel);
            // } else if (statisticsOutModel.getArticleId() != null) {
            // logger.info("统计表存入文章ixugaioID {}",
            // statisticsInModel.getArticleId());
            // kgArticleStatisticsWMapper.updateAddBonusTotal(statisticsInModel);
            // }
            //
            // }
            // }

            // 成功保存返回文章ID
            articleResponse.setArticleId(String.valueOf(inModel.getArticleId()));

            sendArticleSyncMessage(inModel);
        } finally {
            lock.unLock();
        }

        return new Result<ArticleResponse>(articleResponse);

    }

    /**
     * 发送数据同步消息
     *
     * @param article
     */
    private void sendArticleSyncMessage(ArticleInModel inModel) {
        Map<String, Object> map = new HashMap<>();
        map.put("types", "0");
        map.put("articleId", inModel.getArticleId());
        logger.info("ES同步文章MQ------topic:{},  map:{}", articleSyncMqConfig.getTopic(), JSONObject.toJSON(map));
        mqProduct.sendMessage(articleSyncMqConfig.getTopic(), articleSyncMqConfig.getTags(), null, JSON.toJSONString(map));
    }

    private void sendArticleSyncMessage(Long articleId) {
        Map<String, Object> map = new HashMap<>();
        map.put("types", "0");
        map.put("articleId", articleId);
        logger.info("ES同步文章MQ------topic:{},  map:{}", articleSyncMqConfig.getTopic(), JSONObject.toJSON(map));
        mqProduct.sendMessage(articleSyncMqConfig.getTopic(), articleSyncMqConfig.getTags(), null, JSON.toJSONString(map));
    }

    /*
     * 前台修改文章
     * 
     */
    @Override
    @Transactional
    public boolean updateBySelective(ArticleRequest request) {
        logger.info("前台修改文章入参：ArticleRequest={}", JSON.toJSONString(request));
        boolean success = false;
        ArticleInModel inModel = new ArticleInModel();

        inModel.setArticleId(request.getArticleId());
        inModel.setArticleTitle(request.getArticleTitle());
        inModel.setArticleText(request.getArticleText());
        String str = new String();
        String[] strs = request.getArticleTagnames().split(",");
        for (int i = 0, len = strs.length; i < len; i++) {
            String tagname = strs[i].toString();
            if (StringUtils.isBlank(tagname)) {
                continue;
            }
            TagsInModel model = new TagsInModel();
            model.setTagName(tagname);
            // 关联Tags
            TagsOutModel tagsOutModel = tagsRMapper.getTags(model);
            if (null != tagsOutModel) {
                model.setTagId(tagsOutModel.getTagId());
                tagsWMapper.UpdateNum(model);
                str = str + model.getTagId().toString() + ",";
            } else {
                model.setTagId(idGenerater.nextId());
                model.setCreateDate(new Date());
                model.setCreateUser(request.getCreateUser());
                String TagName = strs[i].toString();
                model.setTagName(TagName);

                tagsWMapper.insertSelective(model);
                str = str + model.getTagId().toString() + ",";
            }
        }

        int indx = str.lastIndexOf(",");
        if (indx != -1) {
            str = str.substring(0, indx) + str.substring(indx + 1, str.length());
        }
        inModel.setArticleTags(str);

        inModel.setArticleTagnames(request.getArticleTagnames());
        inModel.setArticleDescription(request.getArticleDescription());
        inModel.setArticleImage(request.getArticleImage());
        inModel.setArticleType(request.getArticleType());
        inModel.setArticleFrom(request.getArticleFrom());
        inModel.setBonusStatus(request.getBonusStatus());
        inModel.setCreateUser(request.getCreateUser());
        inModel.setPublishStatus(request.getPublishStatus().toString());
        inModel.setCreateDate(new Date());

        success = articleWMapper.updateBySelective(inModel) > 0;

        if (success) {
            sendArticleSyncMessage(inModel);
        }
        return success;

    }

    /**
     * 搜索文章
     */
    @Cacheable(category = "article/search/all", key = "#{request.articleTitle}_#{request.currentPage}_#{request.publishKind}", expire = 10, dateType = DateEnum.MINUTES)
    @Override
    public PageModel<ArticleResponse> getSearchArticle(ArticleRequest request, PageModel<ArticleResponse> page) {
        logger.info("搜索文章入参：ArticleRequest={}", JSON.toJSONString(request));
        ArticleInModel inModel = new ArticleInModel();
        inModel.setStart((page.getCurrentPage() - 1) * page.getPageSize());
        inModel.setLimit(page.getPageSize());
        inModel.setArticleTitle(request.getArticleTitle());
        inModel.setPublishKind(request.getPublishKind());
        List<ArticleOutModel> listArticle = articleRMapper.getSearchArticle(inModel);

        List<ArticleResponse> listrResponse = new ArrayList<ArticleResponse>();
        for (ArticleOutModel articleOutModel : listArticle) {
            ArticleResponse response = new ArticleResponse();
            response.setArticleId(articleOutModel.getArticleId());
            response.setArticleTitle(articleOutModel.getArticleTitle());
            response.setArticleDescription(articleOutModel.getArticleDescription());
            response.setArticleTags(articleOutModel.getArticleTags());
            response.setArticleImage(articleOutModel.getArticleImage());

            response.setCreateUser(articleOutModel.getCreateUser().toString());
            response.setUpdateDate(DateUtils.calculateTime(articleOutModel.getUpdateDate()));
            if (articleOutModel.getAuditDate() != null) {
                response.setUpdateTimestamp(String.valueOf(articleOutModel.getAuditDate().getTime()));
            } else {
                response.setUpdateTimestamp(String.valueOf(articleOutModel.getUpdateDate().getTime()));
            }
            if (articleOutModel.getArticleMark() == 1) {
                // 优质文章
                response.setArticleType(1);
            } else {
                response.setArticleType(null);
            }
            response.setArticleImgSize(articleOutModel.getArticleImgSize());
            response.setComments(articleOutModel.getComments());
            response.setArticleTagnames(articleOutModel.getArticleTagnames());

            response.setArticleDescription(articleOutModel.getArticleDescription());
            //是否是有质文章
            response.setArticleMark(articleOutModel.getArticleMark());
            response.setColumnname(articleOutModel.getColumnname());
            response.setSecondColumn(articleOutModel.getSecondColumn());
            if (null != articleOutModel.getSecondColumn()) {
                response.setColumnId(articleOutModel.getSecondColumn());
            } else {
                response.setColumnId(articleOutModel.getColumnId());
            }

            response.setSecondcolumnname(articleOutModel.getSecondcolumnname());

            response.setUsername(articleOutModel.getUsername());
            response.setArticlefrom(articleOutModel.getArticlefrom());
            response.setPublishKind(articleOutModel.getPublishKind());
            response.setColumnAvatar(articleOutModel.getColumnAvatar());
            KgArticleStatisticsInModel model = new KgArticleStatisticsInModel();
            model.setArticleId(Long.parseLong(articleOutModel.getArticleId()));
            KgArticleStatisticsOutModel outModel = kgArticleStatisticsRMapper.selectByPrimaryKey(model);
            if (outModel != null) {
                response.setBrowseNum(outModel.getBrowseNum());
            }

            // 文章所在二级栏目不等0 或者null ，查出二级栏目的名称赋值给 Columnname
            if (null != articleOutModel.getSecondColumn() && articleOutModel.getSecondColumn().intValue() != 0) {
                ColumnInModel columnInModel = new ColumnInModel();
                columnInModel.setColumnId(articleOutModel.getSecondColumn());
                ColumnOutModel columnOutModel = columnRMapper.selectByname(columnInModel);
                if (columnOutModel != null) {
                    response.setColumnname(columnOutModel.getColumnName());
                }

            }

            listrResponse.add(response);

        }
        long count = articleRMapper.SearchCount(inModel);

        page.setData(listrResponse);
        page.setTotalNumber(count);
        // InModel.se
        return page;

    }

    @Override
    public PageModel<ArticleResponse> searchArticleByEs(ArticleRequest request, PageModel<ArticleResponse> page) {
        String articleTitle = request.getArticleTitle();
        Integer publishKind = request.getPublishKind() == null ? 0 : request.getPublishKind();
        Integer pageIndex = (page.getCurrentPage() - 1) * page.getPageSize();
        Integer pageSize = page.getPageSize();
        BaseResult result = articleSearchService.searchArticle(articleTitle, publishKind, pageIndex, pageSize);
        SearchHits hits = (SearchHits) result.getResponseBody();

        List<ArticleResponse> listResponse = new ArrayList<>();
        for (SearchHit hit : hits) {
            Map<String, Object> map = hit.getSourceAsMap();
            String createUser = String.valueOf(map.get("createUser"));
            String updateTime = String.valueOf(map.get("updateDate"));
            String articleId = String.valueOf(map.get("articleId"));
            ArticleResponse response = buildBaseInfo(map, createUser, updateTime);
            response = buildUserInfo(createUser, response);
            response = buildStatistics(articleId, response);
            response = buildHighlightField(hit, response);
            listResponse.add(response);
        }
        page.setData(listResponse);
        page.setTotalNumber(hits.getTotalHits());
        return page;
    }

    private ArticleResponse buildHighlightField(SearchHit hit, ArticleResponse response) {
        Map<String, HighlightField> highlightFieldMap = hit.getHighlightFields();
        if (highlightFieldMap == null) {
            return response;
        }
        HighlightField highlightField1 = highlightFieldMap.get("articleTitle");
        HighlightField highlightField2 = highlightFieldMap.get("createUserName");
        if (highlightField1 != null) {
            Text[] texts = highlightField1.getFragments();
            if (texts != null && texts.length > 0) {
                String articleTitle = texts[0].toString();
                response.setArticleTitle(articleTitle);
            }
        }
        if (highlightField2 != null) {
            Text[] texts = highlightField2.getFragments();
            if (texts != null && texts.length > 0) {
                String createUserName = texts[0].toString();
                response.setUsername(createUserName);
            }
        }
        return response;
    }

    private ArticleResponse buildStatistics(String articleId, ArticleResponse response) {
        KgArticleStatisticsInModel model = new KgArticleStatisticsInModel();
        model.setArticleId(Long.parseLong(articleId));
        KgArticleStatisticsOutModel outModel = kgArticleStatisticsRMapper.selectByPrimaryKey(model);
        if (outModel == null) {
            return response;
        }
        response.setBrowseNum(outModel.getBrowseNum());
        return response;
    }

    private ArticleResponse buildUserInfo(String createUser, ArticleResponse response) {
        UserProfileOutModel userProfileOutModel = userProfileRMapper.selectBaseInfoByPrimaryKey(Long.valueOf(createUser));
        if (userProfileOutModel == null) {
            return response;
        }
        response.setUsername(userProfileOutModel.getColumnName());
        response.setColumnAvatar(userProfileOutModel.getColumnAvatar());
        return response;
    }

    private ArticleResponse buildBaseInfo(Map<String, Object> map, String createUser, String updateTime) {
        ArticleResponse response = new ArticleResponse();
        response.setArticleId(String.valueOf(map.get("articleId")));
        response.setArticleTitle(String.valueOf(map.get("articleTitle") == null ? "" : map.get("articleTitle")));
        response.setArticleDescription(String.valueOf(map.get("articleDescription") == null ? "" : map.get("articleDescription")));
        response.setArticleImage(String.valueOf(map.get("articleImage") == null ? "" : map.get("articleImage")));
        response.setCreateUser(createUser);
        if (StringUtils.isNotEmpty(updateTime) && !"null".equals(updateTime)) {
            Date updateDate = new Date(Long.parseLong(updateTime + "000"));
            response.setUpdateDate(DateUtils.calculateTime(updateDate));
            response.setUpdateTimestamp(updateTime + "000");
        }
        response.setArticleImgSize((int) (map.get("articleImgSize") == null ? 1 : map.get("articleImgSize")));
        response.setArticlefrom((int) map.get("articleFrom"));
        response.setPublishKind((int) map.get("publishKind"));
        return response;
    }

    /**
     * 逻辑删除文章，修改状态致隐藏
     */
    @Override
    public boolean updateArticle(ArticleRequest request) {
        logger.info("逻辑删除文章入参：ArticleRequest={}", JSON.toJSONString(request));
        ArticleInModel inModel = new ArticleInModel();
        inModel.setArticleId(request.getArticleId());
        inModel.setDisplayStatus(ArticleDisplayStatusEnum.FRONT_HIDDEN.getStatus());
        int count = articleWMapper.updateArticle(inModel);
        if (count > 0) {
            sendArticleSyncMessage(inModel);
            return true;
        }
        return false;
    }

    /**
     * 查文章内容
     *
     * @param request
     * @return
     */
    @Override
    public Result<ArticleResponse> getArticleContent(ArticleRequest request) {
        // logger.info("查文章内容：ArticleRequest={}", JSON.toJSONString(request));
        ArticleInModel inModel = new ArticleInModel();
        inModel.setArticleId(request.getArticleId());
        ArticleOutModel outModel = articleRMapper.getArticleContent(inModel);
        ArticleResponse response = new ArticleResponse();
        if (null == outModel) {
            return new Result<ArticleResponse>(response);
        }

        // 查询文章下的阅读奖励
        KgArticleBonusInModel bonusInModel = new KgArticleBonusInModel();
        bonusInModel.setArticleId(request.getArticleId());
        // app1.1.0屏蔽阅读奖励
        // List<KgArticleBonusOutModel> bonusOutModelsList =
        // kgArticleBonusRMapper.getArticleBonus(bonusInModel);
        // List<KgArticleBonusOutModel> allBonusOutModelsList =
        // kgArticleBonusRMapper.getAllArticleBonus(bonusInModel);
        response.setArticleId(outModel.getArticleId());
        response.setArticleTitle(outModel.getArticleTitle());
        response.setArticleText(outModel.getArticleText());
        response.setArticleTags(outModel.getArticleTags());
        response.setArticleImage(outModel.getArticleImage());
        response.setArticleType(outModel.getArticleType());
        response.setArticleSource(outModel.getArticleSource());
        response.setArticleDescription(outModel.getArticleDescription());
        response.setArticleLink(outModel.getArticleLink());
        response.setColumnId(outModel.getColumnId());
        response.setSecondColumn(outModel.getSecondColumn());
        response.setPublishStatus(outModel.getPublishStatus());
        response.setCreateUser(outModel.getCreateUser().toString());
        response.setCreateDate(outModel.getCreateDate());
        response.setUpdateDate(DateUtils.calculateTime(outModel.getUpdateDate()));
        if (outModel.getAuditDate() != null) {
            response.setUpdateTimestamp(String.valueOf(outModel.getAuditDate().getTime()));
        } else {
            response.setUpdateTimestamp(String.valueOf(outModel.getUpdateDate().getTime()));
        }

        response.setBonusStatus(outModel.getBonusStatus());
        response.setArticleTagnames(outModel.getArticleTagnames());
        response.setBonus(outModel.getBonus());
        response.setTextnum(outModel.getTextnum());
        response.setPublishKind(outModel.getPublishKind());
        response.setVideoUrl(outModel.getVideoUrl());
        response.setVideoFilename(outModel.getVideoFilename());

        // app1.1.0屏蔽阅读奖励
        // 阅读奖励状态
        // if (bonusOutModelsList.size() != 0) {
        // List<KgArticleBonusResponse> listArtBonus = new
        // ArrayList<KgArticleBonusResponse>();
        // for (KgArticleBonusOutModel kgArBOutModel : bonusOutModelsList) {
        // KgArticleBonusResponse articleBonusResponse = new
        // KgArticleBonusResponse();
        // articleBonusResponse.setBonusId(kgArBOutModel.getBonusId().toString());
        // articleBonusResponse.setArticleId(kgArBOutModel.getArticleId().toString());
        // articleBonusResponse.setBrowseTime(kgArBOutModel.getBrowseTime());
        // articleBonusResponse.setBonusValue(kgArBOutModel.getBonusValue());
        // articleBonusResponse.setMaxPeople(kgArBOutModel.getMaxPeople());
        // articleBonusResponse.setBonusStatus(kgArBOutModel.getBonusStatus());
        // articleBonusResponse.setBonusTypename(kgArBOutModel.getBonusTypename());
        // articleBonusResponse.setBonusType(kgArBOutModel.getBonusType());
        // articleBonusResponse.setBonusKind(kgArBOutModel.getBonusKind());
        // articleBonusResponse.setBonusSecondType(kgArBOutModel.getBonusSecondType());
        // listArtBonus.add(articleBonusResponse);
        // }
        // response.setListArtBonus(listArtBonus);
        // }
        //
        // if (allBonusOutModelsList.size() != 0) {
        // List<KgArticleBonusResponse> listArtBonus = new
        // ArrayList<KgArticleBonusResponse>();
        // for (KgArticleBonusOutModel kgArBOutModel : allBonusOutModelsList) {
        // KgArticleBonusResponse articleBonusResponse = new
        // KgArticleBonusResponse();
        // articleBonusResponse.setBonusId(kgArBOutModel.getBonusId().toString());
        // articleBonusResponse.setArticleId(kgArBOutModel.getArticleId().toString());
        // articleBonusResponse.setBrowseTime(kgArBOutModel.getBrowseTime());
        // articleBonusResponse.setBonusValue(kgArBOutModel.getBonusValue());
        // articleBonusResponse.setMaxPeople(kgArBOutModel.getMaxPeople());
        // articleBonusResponse.setBonusStatus(kgArBOutModel.getBonusStatus());
        // articleBonusResponse.setBonusTypename(kgArBOutModel.getBonusTypename());
        // articleBonusResponse.setBonusType(kgArBOutModel.getBonusType());
        // articleBonusResponse.setBonusKind(kgArBOutModel.getBonusKind());
        // articleBonusResponse.setBonusSecondType(kgArBOutModel.getBonusSecondType());
        // listArtBonus.add(articleBonusResponse);
        // }
        // response.setListAllBonus(listArtBonus);
        // }

        // logger.info("查询文章详情返回.....{}" + JSON.toJSONString(response));

        return new Result<ArticleResponse>(response);
    }

    @Override
    public JsonEntity encyclopediaList() {

        String key = "articlePage_columnId379:encyclopediaList";
        //从缓存获取
        JsonEntity result = jedisUtils.get(key, JsonEntity.class);
        if (result != null) {
            logger.info("百科词条从缓存获取------");
            return result;
        }

        List<ArticleOutModel> outModels = articleRMapper.encyclopediaList();
        List<ArticleResponse> responses = new ArrayList<>();
        for (ArticleOutModel outModel : outModels) {
            ArticleResponse response = new ArticleResponse();
            response.setArticleId(outModel.getArticleId());
            response.setArticleDescription(outModel.getArticleDescription());
            response.setArticleTitle(outModel.getArticleTitle());
            responses.add(response);
        }
        if (responses.size() > 0) {
            logger.info("百科词条存入缓存------");
            jedisUtils.set(key, JSON.toJSONString(JsonEntity.makeSuccessJsonEntity(responses)));
        }

        return JsonEntity.makeSuccessJsonEntity(responses);
    }

    @Override

    public JsonEntity getVideoTabInfo(ArticleRequest request, PageModel<ArticleResponse> page, HttpServletRequest servletRequest) {
        AppJsonEntity result = homePageService.getVideoTabInfo(request, page, servletRequest);
        Map map = (Map) result.getData();
        PageModel pageModel = new PageModel<>();
        Object o = map.get("articleList");
        if (o instanceof JSONObject) {
            pageModel = JSON.parseObject(JSON.toJSONString(o), PageModel.class);
        } else {
            pageModel = (PageModel) map.get("articleList");
        }
        return JsonEntity.makeSuccessJsonEntity(pageModel);
    }

    @Cacheable(category = "getArticleSimHashCode", key = "#{request.limit}_#{request.start}", expire = 2, dateType = DateEnum.HOURS)
    @Override
    public List<BigInteger> getArticleSimHashCode(BaseRequest request) {
        return articleRMapper.getArticleSimHashCode(request.getLimit(), request.getStart());
    }

    @Override
    public int getSimHashTotal() {
        return articleRMapper.getSimHashTotal();
    }

}
