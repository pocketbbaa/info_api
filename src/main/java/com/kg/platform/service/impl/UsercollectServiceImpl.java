package com.kg.platform.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.List;

import javax.inject.Inject;

import com.kg.platform.common.mongo.MongoTables;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.mongo.Seach;
import com.kg.platform.common.utils.Bean2MapUtil;
import com.kg.platform.common.utils.JsonUtil;
import com.kg.platform.dao.entity.PushMessage;
import com.kg.platform.dao.read.ArticleRMapper;
import com.kg.platform.enumeration.BusinessTypeEnum;
import com.kg.platform.enumeration.UserLogEnum;
import com.kg.platform.model.in.ArticleInModel;
import com.kg.platform.model.in.UserCollectInMongo;
import com.kg.platform.model.out.ArticleOutModel;
import com.kg.platform.model.response.AccountFlowResponse130;
import com.kg.platform.service.ArticlekgService;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.types.Decimal128;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.dao.read.KgArticleStatisticsRMapper;
import com.kg.platform.dao.read.UserCollectRMapper;
import com.kg.platform.dao.write.KgArticleStatisticsWMapper;
import com.kg.platform.dao.write.UserCollectWMapper;
import com.kg.platform.model.in.KgArticleStatisticsInModel;
import com.kg.platform.model.in.UserCollectInModel;
import com.kg.platform.model.mongoTable.UserCollectTable;
import com.kg.platform.model.out.KgArticleStatisticsOutModel;
import com.kg.platform.model.out.UserCollectOutModel;
import com.kg.platform.model.request.AccountRequest;
import com.kg.platform.model.request.UserCollectRequest;
import com.kg.platform.model.response.UserCollectResponse;
import com.kg.platform.service.AccountService;
import com.kg.platform.service.UsercollectService;

@Service
@Transactional
public class UsercollectServiceImpl implements UsercollectService {
    private static final Logger logger = LoggerFactory.getLogger(UsercollectServiceImpl.class);

    @Inject
    UserCollectRMapper userCollectRMapper;

    @Inject
    UserCollectWMapper userCollectWMapper;

    @Inject
    protected JedisUtils jedisUtils;

    @Inject
    IDGen idGenerater;

    @Inject
    KgArticleStatisticsWMapper kgArticleStatisticsWMapper;

    @Inject
    KgArticleStatisticsRMapper kgArticleStatisticsRMapper;

    @Inject
    AccountService accountService;

    @Autowired
    private ArticleRMapper articleRMapper;

    @Override
    public PageModel<UserCollectResponse> getCollectAll(UserCollectRequest request,
                                                        PageModel<UserCollectResponse> page) {
        logger.info("获取收藏数点赞数列表入参：{},分页入参：{}", JSON.toJSONString(request), JSON.toJSONString(page));
        UserCollectInModel inModel = new UserCollectInModel();
        inModel.setUserId(request.getUserId());
        inModel.setOperType(request.getOperType());

        int type = request.getOperType();
        UserLogEnum userLogEnum = UserLogEnum.getByCode(type);

        BasicDBObject basicDBObject = new BasicDBObject("userId", request.getUserId());
        BasicDBObject sort = new BasicDBObject("collectDate", -1);
        MongoCursor<Document> cursor = MongoUtils.findByPage(userLogEnum.getTable(), basicDBObject, sort,
                page.getCurrentPage(), page.getPageSize());

        long count = MongoUtils.count(userLogEnum.getTable(), basicDBObject);
        List<UserCollectResponse> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            Long collectId = (Long) document.get("collectId");
            Long articleId = (Long) document.get("articleId");
            if (collectId == null || collectId <= 0 || articleId == null || articleId <= 0) {
                continue;
            }
            UserCollectResponse response = new UserCollectResponse();
            response.setCollectId(collectId.toString());
            response.setArticleId(articleId.toString());
            response.setCollectDate(DateUtils.calculateTime(document.getDate("collectDate")));

            ArticleInModel articleInModel = new ArticleInModel();
            articleInModel.setArticleId(articleId);
            ArticleOutModel articleOutModel = articleRMapper.selectArticleBase(articleInModel);
            if (articleOutModel == null) {
                continue;
            }
            response.setArticleImage(articleOutModel.getArticleImage());
            response.setArticleTitle(articleOutModel.getArticleTitle());
            response.setArticleFrom(String.valueOf(articleOutModel.getArticlefrom()));
            response.setPublishKind(articleOutModel.getPublishKind());
            list.add(response);
        }
        page.setData(list);
        page.setTotalNumber(count);
        return page;
    }

    /**
     * 取消收藏,取消点赞
     */
    @Override
    public boolean deleteByPrimaryKey(UserCollectRequest request) {
        logger.info("取消收藏,取消点赞入参：{}", JSON.toJSONString(request));
        UserCollectInModel inModel = new UserCollectInModel();

        inModel.setCollectId(request.getCollectId());
        inModel.setUserId(request.getUserId());
        inModel.setArticleId(request.getArticleId());
        inModel.setOperType(request.getOperType());
        KgArticleStatisticsInModel astInModel = new KgArticleStatisticsInModel();
        astInModel.setArticleId(request.getArticleId());

        KgArticleStatisticsInModel inModelKg = new KgArticleStatisticsInModel();
        inModelKg.setArticleId(request.getArticleId());
        KgArticleStatisticsOutModel kgArticleStatisticsOutModel = kgArticleStatisticsRMapper
                .selectByPrimaryKey(inModelKg);
        logger.info("KgArticleStatisticsOutModel :" + JSONObject.toJSONString(kgArticleStatisticsOutModel));
        if (kgArticleStatisticsOutModel == null) {
            return false;
        }
        // 1 收藏
        if (request.getOperType() == 1) {
            Integer collectNum = kgArticleStatisticsOutModel.getCollectNum();
            if (collectNum == null || collectNum <= 0) {
                return false;
            }
            astInModel.setCollectNum(1);
            MongoUtils.deleteByFilter(UserLogEnum.KG_USER_COLLECT.getTable(), new BasicDBObject("userId", inModel.getUserId()).append("articleId", inModel.getArticleId()));
        }
        // 2 点赞
        if (request.getOperType() == 2) {
            Integer thumbupNum = kgArticleStatisticsOutModel.getThumbupNum();
            if (thumbupNum == null || thumbupNum <= 0) {
                return false;
            }
            astInModel.setThumbupNum(1);
            MongoUtils.deleteByFilter(UserLogEnum.KG_USER_LIKE.getTable(), new BasicDBObject("userId", inModel.getUserId()).append("articleId", inModel.getArticleId()));
        }
        return kgArticleStatisticsWMapper.updateArticleSelective(astInModel) > 0;
    }

    /**
     * 增加点赞 收藏 分享 浏览
     */
    @Override
    public boolean insertSelective(UserCollectRequest request) {
        logger.info("增加点赞 收藏 分享 浏览入参：{}", JSON.toJSONString(request));

        UserCollectInMongo inModel = buildUserCollectInMongo(request);

        KgArticleStatisticsInModel artinModel = new KgArticleStatisticsInModel();
        artinModel.setArticleId(request.getArticleId());
        Document doc = new Document(Bean2MapUtil.bean2map(inModel));

        UserLogEnum userLogEnum = UserLogEnum.getByCode(request.getOperType());
        MongoUtils.insertOne(userLogEnum.getTable(), doc);

        if (request.getOperType() == 1) {
            artinModel.setCollectNum(1);
        }
        if (request.getOperType() == 2) {
            artinModel.setThumbupNum(2);
        }
        if (request.getOperType() == 3) {
            artinModel.setShareNum(3);
        }
        if (request.getOperType() == 4) {
            artinModel.setBrowseNum(4);
        }
        return kgArticleStatisticsWMapper.updateByPrimaryKeySelective(artinModel) > 0;
    }

    private UserCollectInMongo buildUserCollectInMongo(UserCollectRequest request) {
        UserCollectInMongo inModel = new UserCollectInMongo();
        Long collectId = idGenerater.nextId();
        inModel.setCollectId(collectId);
        inModel.setUserId(request.getUserId());
        inModel.setOperType(request.getOperType());// 1 收藏 2 点赞 3 分享 4 浏览
        inModel.setArticleId(request.getArticleId());
        Date now = new Date();
        inModel.setCollectDate(now);
        inModel.setTime(now.getTime());
        inModel.setSource(request.getSource());
        inModel.setPublishKind(getPublishKind(request.getArticleId()));
        return inModel;
    }

    private Integer getPublishKind(Long articleId) {
        ArticleInModel inModel = new ArticleInModel();
        inModel.setArticleId(articleId);
        ArticleOutModel outModel = articleRMapper.selectArticleBase(inModel);
        return outModel == null ? 0 : outModel.getPublishKind();
    }

    @Override
    public BigDecimal addCollect(UserCollectRequest request) {

        this.insertSelective(request);

        AccountRequest accountRequest = new AccountRequest();
        // 1 收藏 2 点赞 3 分享
        if (request.getOperType() == 1) {
            // 2：首次点赞文章，3首次收藏文章
            accountRequest.setBonusType("3");
        }
        if (request.getOperType() == 2) {
            accountRequest.setBonusType("2");
        }
        // if (request.getOperType() == 4) {
        // accountRequest.setBonusType("1");
        // }
        accountRequest.setUserId(request.getUserId());
        accountRequest.setArticleId(request.getArticleId());

        BigDecimal retV = BigDecimal.ZERO;
        if (null != request.getUserId() && null != accountRequest.getBonusType()) {
            retV = accountService.updateUserbalance(accountRequest);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public boolean getShareStatus(UserCollectRequest request) {
        BasicDBObject query = new BasicDBObject();
        query.put("userId", request.getUserId());
        query.put("articleId", request.getArticleId());
        long count = MongoUtils.count(UserLogEnum.KG_USER_SHARE.getTable(), query);
        return count > 0;
    }

    @Override
    public boolean getBrowseStatus(UserCollectRequest request) {
        BasicDBObject query = new BasicDBObject();
        query.put("articleId", request.getArticleId());
        long count = MongoUtils.count(UserLogEnum.KG_USER_BROWSE.getTable(), query);
        return count > 0;
    }

    @Override
    public Result<Integer> getCollectByUserIdAndArticleId(UserCollectRequest request) {
        int type = request.getOperType();
        UserLogEnum userLogEnum = UserLogEnum.getByCode(type);
        BasicDBObject query = new BasicDBObject();
        query.put("userId", new BasicDBObject(Seach.EQ.getOperStr(), request.getUserId()));
        query.put("articleId", new BasicDBObject(Seach.EQ.getOperStr(), request.getArticleId()));
        long count = MongoUtils.count(userLogEnum.getTable(), query);
        if (count > 0) {
            return new Result<>(request.getOperType());
        }
        return new Result<>(404, "用户还没有收藏或点赞", 0);
    }
}
