package com.kg.platform.service.admin.impl;

import com.kg.platform.common.easyexcel.ArticleExportExcel;
import com.kg.platform.common.easyexcel.AsverageUserExportExcel;
import com.kg.platform.common.easyexcel.AuthorExportExcel;
import com.kg.platform.common.exception.ApiMessageException;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.mongo.Seach;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.common.utils.ExcelUtil;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.dao.entity.SysUser;
import com.kg.platform.dao.entity.UserRelation;
import com.kg.platform.dao.read.ArticleRMapper;
import com.kg.platform.dao.read.UserCommentRMapper;
import com.kg.platform.dao.read.UserRMapper;
import com.kg.platform.dao.read.UserRelationRMapper;
import com.kg.platform.enumeration.RegistOriginEnum;
import com.kg.platform.enumeration.UserActivityEnum;
import com.kg.platform.enumeration.UserLogEnum;
import com.kg.platform.model.TokenModel;
import com.kg.platform.model.request.admin.ExportRequest;
import com.kg.platform.model.request.admin.SysUserQueryRequest;
import com.kg.platform.service.TokenManager;
import com.kg.platform.service.admin.DateStatisticalExportService;
import com.kg.platform.service.admin.SysUserService;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.client.MongoCursor;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Service
public class DateStatisticalExportServiceImpl implements DateStatisticalExportService {


    private static final Logger log = LoggerFactory.getLogger(DateStatisticalExportServiceImpl.class);

    @Autowired
    private ArticleRMapper articleRMapper;

    @Autowired
    private UserCommentRMapper userCommentRMapper;

    @Autowired
    private UserRMapper userRMapper;

    @Autowired
    private UserRelationRMapper userRelationRMapper;

    @Inject
    private TokenManager manager;

    @Inject
    private SysUserService sysUserService;

    @Override
    public void exportArticleData(ExportRequest request, HttpServletResponse response, HttpServletRequest servletRequest) {

        String start = request.getStartTime();
        String end = request.getEndTime();
        checkToken(request.getToken());
        log.info("in exportArticleData start:" + start + "  end:" + end);
        if (StringUtils.isEmpty(start) || StringUtils.isEmpty(end)) {
            throw new ApiMessageException("入参有误");
        }
        List<ArticleExportExcel> articleExportList = articleRMapper.getArticleDetailByApprovedDay(
                getStartday(start),
                getEndday(end));
        articleExportList = initArticleExportExcel(articleExportList);
        if (CollectionUtils.isEmpty(articleExportList)) {
            throw new ApiMessageException("没有获取到资讯数据");
        }
        try {

            ExcelUtil.writeBrower(articleExportList, response, "article.csv", ArticleExportExcel.class);
            log.info("exportArticleData success");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void exportAsverageUserData(ExportRequest request, HttpServletResponse response, HttpServletRequest servletRequest) {
        String start = request.getStartTime();
        String end = request.getEndTime();
        checkToken(request.getToken());
        if (StringUtils.isEmpty(start) || StringUtils.isEmpty(end)) {
            throw new ApiMessageException("入参有误");
        }
        log.info("in exportAsverageUserData start:" + start + "  end:" + end);
        Date startDay = getStartday(start);
        Date endDay = getEndday(end);

        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.append("collectDate", BasicDBObjectBuilder.start(Seach.GTE.getOperStr(), startDay)
                .add(Seach.LTE.getOperStr(), endDay).get());

        List<Long> list = getUserList(basicDBObject, startDay, endDay);

        log.info("用户总数：" + list.size());
        if (CollectionUtils.isEmpty(list)) {
            log.info("没有查询到用户");
            throw new ApiMessageException("没有查询到用户信息");
        }
        List<AsverageUserExportExcel> asverageUserExportExcels = userRMapper.getExportList(list, startDay, endDay);
        if (CollectionUtils.isEmpty(asverageUserExportExcels)) {
            log.info("userRMapper.getExportList(list) result is null ");
            throw new ApiMessageException("没有查询到用户信息");
        }
        log.info("userRMapper.getExportList(list) 查询数: " + asverageUserExportExcels.size());
        List<AsverageUserExportExcel> asverageUserExportExcelList = initAsverageUserExportExcel(asverageUserExportExcels, basicDBObject);
        try {
            log.info("待导出数据 ：" + asverageUserExportExcelList.size());
            ExcelUtil.writeBrower(asverageUserExportExcelList, response, "asverageUser.csv", AsverageUserExportExcel.class);
            log.info("exportAsverageUserData success");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void exportAuthorData(ExportRequest request, HttpServletResponse response, HttpServletRequest servletRequest) {
        String start = request.getStartTime();
        String end = request.getEndTime();
        checkToken(request.getToken());
        if (StringUtils.isEmpty(start) || StringUtils.isEmpty(end)) {
            throw new ApiMessageException("入参有误");
        }
        log.info("in exportAuthorData start:" + start + "  end:" + end);
        Date startDay = getStartday(start);
        Date endDay = getEndday(end);
        List<Long> userIds = getActivityUserIds(startDay, endDay);
        if (CollectionUtils.isEmpty(userIds)) {
            throw new ApiMessageException("没有查询到用户信息");
        }
        List<AuthorExportExcel> authorExportExcels = userRMapper.getAuthorExportExcelList(userIds, startDay, endDay);
        try {
            ExcelUtil.writeBrower(authorExportExcels, response, "author.csv", AuthorExportExcel.class);
            log.info("exportArticleData success");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Long> getActivityUserIds(Date startDay, Date endDay) {
        Set<Long> userIds = new HashSet<>();
        List<Long> postUserIds = userRMapper.getPostUserIds(startDay, endDay);
        List<Long> newFansUserIds = userRMapper.getNewFansUserIds(startDay, endDay);
        List<Long> loginUserIds = userRMapper.getLoginUserIds(startDay, endDay);
        userIds.addAll(postUserIds);
        userIds.addAll(newFansUserIds);
        userIds.addAll(loginUserIds);
        return new ArrayList<>(userIds);
    }

    private List<Long> getUserList(BasicDBObject basicDBObject, Date startDay, Date endDay) {
        Set<Long> useIds = new HashSet<>();
        useIds.addAll(getUserIds(basicDBObject, UserLogEnum.KG_USER_BROWSE.getTable()));
        useIds.addAll(getUserIds(basicDBObject, UserLogEnum.KG_USER_COLLECT.getTable()));
        useIds.addAll(getUserIds(basicDBObject, UserLogEnum.KG_USER_LIKE.getTable()));
        useIds.addAll(getUserIds(basicDBObject, UserLogEnum.KG_USER_SHARE.getTable()));

        List<Long> userIdList = userCommentRMapper.getCommentUserIdsByTime(startDay, endDay);
        useIds.addAll(userIdList);
        if (CollectionUtils.isEmpty(useIds)) {
            return new ArrayList<>();
        }

        return new ArrayList<>(useIds);
    }

    private List<AsverageUserExportExcel> initAsverageUserExportExcel(List<AsverageUserExportExcel> asverageUserExportExcels, BasicDBObject basicDBObject) {
        List<AsverageUserExportExcel> asverageUserExportExcelList = new ArrayList<>();
        for (AsverageUserExportExcel asverageUserExportExcel : asverageUserExportExcels) {
            String userId = asverageUserExportExcel.getUserId();
            basicDBObject.put("userId", Long.valueOf(userId));
            asverageUserExportExcel.setBrowseNum(getBehaviorNum(UserLogEnum.KG_USER_BROWSE.getTable(), basicDBObject));
            asverageUserExportExcel.setCollectNum(getBehaviorNum(UserLogEnum.KG_USER_COLLECT.getTable(), basicDBObject));
            asverageUserExportExcel.setThumbupNum(getBehaviorNum(UserLogEnum.KG_USER_LIKE.getTable(), basicDBObject));
            asverageUserExportExcel.setShareNum(getBehaviorNum(UserLogEnum.KG_USER_SHARE.getTable(), basicDBObject));
            asverageUserExportExcel = buildLastReadTime(basicDBObject, asverageUserExportExcel);
            asverageUserExportExcel = getRegisterOriginContent(asverageUserExportExcel);
            asverageUserExportExcelList.add(asverageUserExportExcel);

        }
        return asverageUserExportExcelList;
    }

    private AsverageUserExportExcel buildLastReadTime(BasicDBObject basicDBObject, AsverageUserExportExcel asverageUserExportExcel) {
        MongoCursor<Document> cursor = MongoUtils.aggregate(UserLogEnum.KG_USER_BROWSE.getTable(), conditionList(basicDBObject));
        if (cursor == null) {
            return asverageUserExportExcel;
        }
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            Date lastDay = doc.getDate("collectDate");
            if (lastDay == null) {
                continue;
            }
            asverageUserExportExcel.setLastReadTime(DateUtils.formatDate(lastDay, DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
        }
        return asverageUserExportExcel;
    }


    private List<Bson> conditionList(BasicDBObject basicDBObject) {
        List<Bson> conditionList = new ArrayList<>();
        Bson querryCondition = new BasicDBObject("$match", basicDBObject);
        conditionList.add(querryCondition);
        BasicDBObject sortBy = new BasicDBObject("$sort", new BasicDBObject("collectDate", -1));
        conditionList.add(sortBy);
        BasicDBObject limitBy;
        limitBy = new BasicDBObject("$limit", 1);
        conditionList.add(limitBy);

        return conditionList;
    }

    private String getBehaviorNum(String table, BasicDBObject basicDBObject) {
        return String.valueOf(MongoUtils.count(table, basicDBObject));
    }

    /**
     * 构建注册来源
     *
     * @param user
     * @return
     */
    private AsverageUserExportExcel getRegisterOriginContent(AsverageUserExportExcel asverageUserExportExcel) {

        String registFrom = asverageUserExportExcel.getRegistFrom();
        RegistOriginEnum registOriginEnum = RegistOriginEnum.getByCode(StringUtils.isEmpty(registFrom) ? 3 : Integer.valueOf(registFrom));
        UserRelation userRelation = userRelationRMapper.selectByRelUserId(Long.valueOf(asverageUserExportExcel.getUserId()));
        if (userRelation == null || userRelation.getActivityId() == null) {
            asverageUserExportExcel.setRegistFrom(registOriginEnum.getMessage());
            return asverageUserExportExcel;
        }
        UserActivityEnum userActivityEnum = UserActivityEnum.getByCode(userRelation.getActivityId());
        if (userActivityEnum.equals(UserActivityEnum.WORLD_CUP) || userActivityEnum.equals(UserActivityEnum.ZHOU_CONCERT)) {
            asverageUserExportExcel.setActivityChannel(userActivityEnum.getMessage());
            return asverageUserExportExcel;
        }
        return asverageUserExportExcel;
    }


    private Set<Long> getUserIds(BasicDBObject basicDBObject, String table) {
        Set<Long> userSet = new HashSet<>();
        MongoCursor<Document> cursor = MongoUtils.findByFilter(table, basicDBObject);
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            Long userId = doc.getLong("userId");
            if (userId == null || userId <= 0) {
                continue;
            }
            userSet.add(userId);
        }
        log.info("表：" + table + "  用户数：" + userSet.size());
        return userSet;
    }


    private List<ArticleExportExcel> initArticleExportExcel(List<ArticleExportExcel> articleExportList) {
        if (CollectionUtils.isEmpty(articleExportList)) {
            return new ArrayList<>();
        }
        List<ArticleExportExcel> articleExportLists = new ArrayList<>();
        for (ArticleExportExcel articleExportExcel : articleExportList) {
            articleExportExcel = ArticleExportExcel.initModel(articleExportExcel);
            BasicDBObject basicDBObject = new BasicDBObject("articleId", Long.valueOf(articleExportExcel.getArticleId()));
            articleExportExcel.setBrowseNum(String.valueOf(MongoUtils.count(UserLogEnum.KG_USER_BROWSE.getTable(), basicDBObject)));
            articleExportExcel.setCollectNum(String.valueOf(MongoUtils.count(UserLogEnum.KG_USER_COLLECT.getTable(), basicDBObject)));
            articleExportExcel.setThumbupNum(String.valueOf(MongoUtils.count(UserLogEnum.KG_USER_LIKE.getTable(), basicDBObject)));
            articleExportExcel.setShareNum(String.valueOf(MongoUtils.count(UserLogEnum.KG_USER_SHARE.getTable(), basicDBObject)));
            articleExportLists.add(articleExportExcel);
        }
        return articleExportLists;
    }


    private Date getStartday(String start) {
        return DateUtils.parseDate(start + " 00:00:00", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
    }

    private Date getEndday(String end) {
        return DateUtils.parseDate(end + " 23:59:59", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * token校验
     *
     * @param token
     */
    private void checkToken(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new ApiMessageException(ExceptionEnum.TOKENRERROR);
        }
        TokenModel model = manager.getToken(token);
        SysUserQueryRequest req = new SysUserQueryRequest();
        if (model != null) {
            req.setUserId(model.getUserId());
            SysUser sysUser = sysUserService.getSysUserById(req);
            if (sysUser == null || !sysUser.getStatus()) {
                throw new ApiMessageException(ExceptionEnum.LOCKERROR.getCode(), "账号已被禁用");
            }
        }
    }
}
