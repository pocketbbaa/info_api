package com.kg.platform.service.app.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.BusinessException;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.lock.Lock;
import com.kg.platform.common.mongo.MongoTables;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.mongo.Seach;
import com.kg.platform.common.utils.*;
import com.kg.platform.dao.cloud.CloudOrderMapper;
import com.kg.platform.dao.entity.Account;
import com.kg.platform.dao.entity.SysUser;
import com.kg.platform.dao.read.*;
import com.kg.platform.dao.write.AccountWMapper;
import com.kg.platform.enumeration.*;
import com.kg.platform.model.CoinBgModel;
import com.kg.platform.model.UserTagBuild;
import com.kg.platform.model.in.AccountFlowInModel;
import com.kg.platform.model.in.AccountInModel;
import com.kg.platform.model.in.UserConcernInModel;
import com.kg.platform.model.in.UserInModel;
import com.kg.platform.model.mongoTable.AccountFlowRitMongo;
import com.kg.platform.model.mongoTable.AccountWithdrawFlowRit;
import com.kg.platform.model.mongoTable.WithdrawOperRecord;
import com.kg.platform.model.out.*;
import com.kg.platform.model.request.AccountFlowAppRequest;
import com.kg.platform.model.request.AccountRequest;
import com.kg.platform.model.request.AccountWithDrawRequest;
import com.kg.platform.model.request.SiteimageRequest;
import com.kg.platform.model.response.*;
import com.kg.platform.service.AccountService;
import com.kg.platform.service.UserAccountService;
import com.kg.platform.service.UserkgService;
import com.kg.platform.service.app.AppAccountService;
import com.kg.platform.service.app.AppAdverService;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Decimal128;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Service
public class AppAccountServiceImpl implements AppAccountService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String USER_ADDUSERTIMES_LOCK_KEY = "kguser:: AccountService::appRitWithdraw::";

    @Inject
    private AccountRMapper accountRMapper;

    @Inject
    private UserProfileRMapper userProfileRMapper;

    @Inject
    private UserRelationRMapper userRelationRMapper;

    @Inject
    private AccountFlowRMapper accountFlowRMapper;

    @Autowired
    private AccountFlowRitRMapper accountFlowRitRMapper;

    @Inject
    private UserRMapper userRMapper;

    @Inject
    private UserTagsUtil userTagsUtil;

    @Inject
    private UserConcernRMapper userConcernRMapper;

    @Inject
    private AppAdverService appAdverService;

    @Autowired
    JedisUtils jedisUtils;

    @Autowired
    private RitRolloutRMapper ritRolloutRMapper;

    @Autowired
    private UserkgService userkgService;

    @Autowired
    private AccountWMapper accountWMapper;

    @Autowired
    private IDGen idGen;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private CoinBgModel coinBgModel;

    @Inject
    private UserCollectRMapper userCollectRMapper;

    @Inject
    private UserCommentRMapper userCommentRMapper;

    @Autowired
    private CloudOrderMapper cloudOrderMapper;


    @Override
    public UserBaseInfoResponse getUserBaseInfoById(UserkgResponse request, HttpServletRequest servletRequest) {
        logger.info("getUserBaseInfoById 请求参数：{}", JSONObject.toJSONString(request));
        if (request == null) {
            return null;
        }
        UserBaseInfoResponse userBaseInfoResponse = new UserBaseInfoResponse();
        Long userId = Long.valueOf(request.getUserId());
        userBaseInfoResponse.setUserId(request.getUserId());
        userBaseInfoResponse = buildBalance(userId, userBaseInfoResponse);
        userBaseInfoResponse = buildBaseInfo(userId, userBaseInfoResponse);
        userBaseInfoResponse = buildFansData(userId, userBaseInfoResponse);
        userBaseInfoResponse = buildTags(userBaseInfoResponse);
        userBaseInfoResponse = buildSiteimage(userBaseInfoResponse, servletRequest);
        userBaseInfoResponse = buildYesterdayEarningsBtc(userId, userBaseInfoResponse);

        //APP1.3.0改版后 需要增加点赞 分享 评论 收藏数据
        appendCommentInfo(userId, userBaseInfoResponse);
        userBaseInfoResponse.setCompassUrl(coinBgModel.getCompassUrl());
        logger.info("getUserBaseInfoById 响应数据：{}", JSONObject.toJSONString(userBaseInfoResponse));
        return userBaseInfoResponse;
    }

    private void appendCommentInfo(Long userId, UserBaseInfoResponse userBaseInfoResponse) {
        //获取收藏数 点赞数
        fetchCollectAndPraiseCnt(userId, userBaseInfoResponse);
        //获取评论数
        fetchCommentCnt(userId, userBaseInfoResponse);
        //获取关注数
        fetchAttentionCnt(userId, userBaseInfoResponse);

        userBaseInfoResponse.setApprenticeCnt(userRelationRMapper.selectInviteCountForApp(userId));
    }

    private void fetchAttentionCnt(Long userId, UserBaseInfoResponse userBaseInfoResponse) {
        if (userId == null) {
            logger.warn("【userId为空,不执行数据库查询】{获取关注数}");
            return;
        }
        UserConcernInModel inModel = new UserConcernInModel();
        inModel.setUserId(userId);
        try {
            userBaseInfoResponse.setAttentionCnt(userConcernRMapper.getConcernCount(inModel));
        } catch (Exception e) {
            logger.error("【接口:/myProfile  查询kg_user_concern获取关注数出现异常 e:】" + e.getMessage());
        }
    }

    private void fetchCommentCnt(Long userId, UserBaseInfoResponse userBaseInfoResponse) {
        if (userId == null) {
            logger.warn("【userId为空,不执行数据库查询】{获取评论数}");
            return;
        }
        try {
            userBaseInfoResponse.setCommentCnt(userCommentRMapper.getCommentTotalCountByUserId(userId));
        } catch (Exception e) {
            logger.error("【接口:/myProfile  查询kg_user_comment获取评论数出现异常 e:】" + e.getMessage());
        }
    }

    private void fetchCollectAndPraiseCnt(Long userId, UserBaseInfoResponse userBaseInfoResponse) {
        if (userId == null) {
            logger.warn("【userId为空,不执行数据库查询】{获取点赞数和收藏数}");
            return;
        }

        BasicDBObject object = new BasicDBObject("userId", userId);
        try {
            //获取收藏
            long count = MongoUtils.count(UserLogEnum.KG_USER_COLLECT.getTable(), object);
            userBaseInfoResponse.setCollectCnt((int) count);
            //获取点赞
            long countlike = MongoUtils.count(UserLogEnum.KG_USER_LIKE.getTable(), object);
            userBaseInfoResponse.setPraiseCnt((int) countlike);
        } catch (Exception e) {
            logger.error("【接口:/myProfile  获取点赞数和收藏数出现异常 e:】" + e.getMessage());
        }
    }


    /**
     * 构建个人中心广告
     *
     * @param userBaseInfoResponse
     * @return
     */
    private UserBaseInfoResponse buildSiteimage(UserBaseInfoResponse userBaseInfoResponse, HttpServletRequest servletRequest) {
        SiteimageRequest siteimageAppRequest = new SiteimageRequest();
        siteimageAppRequest.setNavigator_pos(3);
        siteimageAppRequest.setImage_pos(31);
        siteimageAppRequest.setPageSize(5);
        siteimageAppRequest.setCurrentPage(1);
        siteimageAppRequest.setDisPlayPort(2);
        siteimageAppRequest.setOsVersion(servletRequest.getIntHeader("os_version"));
        List<PersonalAdvResponse> ads = appAdverService.getPersonalAdv(siteimageAppRequest);
        if (ads != null && ads.size() > 0) {
            userBaseInfoResponse.setAdvers(ads);
        }
        return userBaseInfoResponse;
    }

    /**
     * 构建昨日收益
     *
     * @param userId
     * @param userBaseInfoResponse
     * @return
     */
    private UserBaseInfoResponse buildYesterdayEarningsBtc(Long userId, UserBaseInfoResponse userBaseInfoResponse) {
        List<Bson> listFilter = Lists.newArrayList();

        BasicDBObject filter = new BasicDBObject();
        filter.append("user_id", userId);
        filter.append("business_type_id", BusinessTypeEnum.KG_CLOUD_BTC_EARNINGS.getStatus());
        Map<String, Date> map = new HashMap<>();
        DateUtils.getYesterdayRange(map);
        filter.append("flow_date", new BasicDBObject(Seach.GTE.getOperStr(), map.get("startTime").getTime())
                .append(Seach.LT.getOperStr(), map.get("stopTime").getTime()));

        BasicDBObject match = new BasicDBObject(Seach.MATCH.getOperStr(), filter);

        BasicDBObject group = new BasicDBObject(Seach.GROUP.getOperStr(),
                new BasicDBObject("sum", new Document(Seach.SUM.getOperStr(), "$amount")).append("_id", "$user_id"));

        listFilter.add(match);
        listFilter.add(group);

        MongoCursor<Document> documentMongoCursor = MongoUtils.aggregate(MongoTables.KG_CLOUD_BTC_EARNINGS, listFilter);
        BigDecimal sum = BigDecimal.ZERO;
        while (documentMongoCursor != null && documentMongoCursor.hasNext()) {
            Document document = documentMongoCursor.next();
            sum = ((Decimal128) document.get("sum")).bigDecimalValue();
        }
        userBaseInfoResponse.setYesterdayEarningsBtc(sum.stripTrailingZeros().toPlainString());
        return userBaseInfoResponse;
    }

    /**
     * 构建粉丝相关数据
     *
     * @param userId
     * @param response
     * @param
     * @return
     */
    private UserBaseInfoResponse buildFansData(Long userId, UserBaseInfoResponse response) {
        // 查询粉丝数量
        UserConcernInModel userConcernInModel = new UserConcernInModel();
        userConcernInModel.setUserId(userId);
        long fansCount = userConcernRMapper.getFansCount(userConcernInModel);
        // 验证是否有新粉丝
        UserConcernInModel userInModel = new UserConcernInModel();
        String lastTime = jedisUtils.get(JedisKey.getFansTime(String.valueOf(userId)));
        if (StringUtils.isEmpty(lastTime)) {
            lastTime = "1900-01-01 00:00:00";
        }
        logger.info("==========新粉丝获取上次获取时间======" + lastTime);
        userInModel.setUserId(Long.valueOf(String.valueOf(userId)));
        userInModel.setLastTime(lastTime);
        int newFansCount = userConcernRMapper.concernReminder(userInModel);
        long inviteCount = userRelationRMapper.selectInviteCountForApp(userId); // 邀请人数
        response.setInviteCount(inviteCount);
        response.setFansCount(fansCount);
        response.setFansNew(newFansCount);
        return response;
    }

    /**
     * 构建头像等基本信息
     *
     * @param userId
     * @param response
     * @return
     */
    private UserBaseInfoResponse buildBaseInfo(Long userId, UserBaseInfoResponse response) {
        // 获取用户基本信息
        UserProfileOutModel userProfileOutModel = userProfileRMapper.selectBaseInfoByPrimaryKey(userId);
        logger.info("[获取用户基本信息] userProfileOutModel：{}", JSONObject.toJSONString(userProfileOutModel));

        if (userProfileOutModel == null) {
            return response;
        }
        String userName = userProfileOutModel.getUsername(); // 用户名
        String avatar = userProfileOutModel.getAvatar(); // 头像
        String resume = userProfileOutModel.getResume(); // 个人简介
        // 如果是专栏用户用户
        UserkgOutModel userkgOutModel = userRMapper.selectByPrimaryKey(userId);
        if (userkgOutModel.getUserRole() != UserRoleEnum.NORMAL.getRole()) {
            resume = userProfileOutModel.getColumnIntro(); // 个人简介
            avatar = userProfileOutModel.getColumnAvatar();
            userName = userProfileOutModel.getColumnName();
        }
        response.setInmagelink(avatar);
        response.setResume(resume);
        response.setUserId(String.valueOf(userId));
        response.setUserName(userName);
        response.setUserMobile(userkgOutModel.getUserMobile());
        response.setUserRole(userkgOutModel.getUserRole());
        response.setInviteCode(userkgOutModel.getInviteCode());
        return response;

    }

    /**
     * 构建余额信息
     *
     * @param userId
     * @param response
     * @return
     */
    private UserBaseInfoResponse buildBalance(Long userId, UserBaseInfoResponse response) {
        AccountOutModel accountOutModel = getBalaceInfo(userId);

        String balance = NumberUtils.formatBigDecimal(accountOutModel == null ? null : accountOutModel.getBalance());
        String txbBalance = NumberUtils.formatBigDecimal(accountOutModel == null ? null : accountOutModel.getTxbBalance());
        String ritBalance = NumberUtils.formatBigDecimal(accountOutModel == null ? null : accountOutModel.getRitBalance().add(accountOutModel.getRitFrozenBalance()));

        response.setBalance(StringUtils.isEmpty(balance) ? "0.000" : balance);
        response.setTxbBalance(StringUtils.isEmpty(txbBalance) ? "0.000" : txbBalance);
        response.setRitBalance(StringUtils.isEmpty(ritBalance) ? "0.000" : ritBalance);
        return response;
    }

    @Override
    public PageModel<AccountFlowAppResponse> selectUserTzflow(AccountFlowAppRequest request,
                                                              PageModel<AccountFlowAppResponse> page) {
        logger.info("我的钛值交易明细：{},分页入参：{}", JSON.toJSONString(request), JSON.toJSONString(page));
        AccountFlowInModel inModel = buildAccountFlowInModel(request, page);
        List<AccountFlowOutModel> list = accountFlowRMapper.selectByUserflowForApp(inModel);
        List<AccountFlowAppResponse> responses = buildAccountFlowAppResponseTzListOld(list);
        long count = accountFlowRMapper.selectByUserflowCountForApp(inModel);
        page.setData(responses);
        page.setTotalNumber(count);
        return page;
    }

//    @Override
//    public PageModel<AccountFlowAppResponse> selectUserTxbflow(AccountFlowAppRequest request,
//                                                               PageModel<AccountFlowAppResponse> page) {
//        logger.info("我的钛小白交易明细：{},分页入参：{}", JSON.toJSONString(request), JSON.toJSONString(page));
//        AccountFlowInModel inModel = buildAccountFlowInModel(request, page);
//        List<AccountFlowOutModel> list = accountFlowRMapper.selectUserTxbflowForApp(inModel);
//        List<AccountFlowAppResponse> responses = buildAccountFlowAppResponseTzListOld(list);
//        long count = accountFlowRMapper.selectUserTxbflowCountForApp(inModel);
//        page.setData(responses);
//        page.setTotalNumber(count);
//        return page;
//
//    }

    @Override
    public PageModel<AccountFlowAppResponse> selectUserTxbflow(AccountFlowAppRequest request,
                                                               PageModel<AccountFlowAppResponse> page) {
        logger.info("我的钛小白交易明细：{},分页入参：{}", JSON.toJSONString(request), JSON.toJSONString(page));
        BasicDBObject querry = this.buildSelectUserTxbflowQuerry(request);
        logger.info(">>>>>>>>>>>selectUserTxbflow>>>>>>>>>>>>>>>", querry);

        BasicDBObject sort = new BasicDBObject();
        sort.put("flow_date", -1);

        MongoCursor<Document> list = MongoUtils.findByPage(MongoTables.ACCOUNT_FLOW_KG, querry, sort, page.getCurrentPage(), page.getPageSize());
        List<AccountFlowAppResponse> responses = buildAccountFlowAppResponseListOld(list);
        page.setData(responses);
        Long count = MongoUtils.count(MongoTables.ACCOUNT_FLOW_KG, querry);
        page.setTotalNumber(count);
        return page;
    }


    private BasicDBObject buildSelectUserTxbflowQuerry(AccountFlowAppRequest request) {
        BasicDBObject querry = new BasicDBObject();
        //收入或支出
        if (request.getType() == 1) {
            //奖励
            int typeId[] = {1000, 1510, 1520, 1560, 1570, 2113, 2114, 2115, 2116};
            querry.put("business_type_id", new BasicDBObject(Seach.IN.getOperStr(), typeId));
        } else if (request.getType() == 2) {
            //打赏
            int typeId[] = {0};
            querry.put("business_type_id", new BasicDBObject(Seach.IN.getOperStr(), typeId));
        } else if (request.getType() == 3) {
            //转入
            int typeId[] = {1010};
            querry.put("business_type_id", new BasicDBObject(Seach.IN.getOperStr(), typeId));
        } else if (request.getType() == 4) {
            //转出
            int typeId[] = {1020};
            querry.put("business_type_id", new BasicDBObject(Seach.IN.getOperStr(), typeId));
        } else if (request.getType() == 5) {
            //进贡
            int typeId[] = {1530};
            querry.put("business_type_id", new BasicDBObject(Seach.IN.getOperStr(), typeId));
        }
        //用户
        querry.put("user_id", new BasicDBObject(Seach.EQ.getOperStr(), request.getUserId()));
        querry = this.addTimeSerach130(querry, request);
        return querry;
    }

    @Override
    public AccountFlowResponse130 getDetailBillById(AccountFlowAppRequest request) {
        int type = request.getType();
        AccountFlowInModel inModel = buildAccountFlowInModelForDetail(request);
        if (CoinEnum.TV.getCode() == type) {
            AccountFlowOutModel outModel = accountFlowRMapper.getTzDetailBill(inModel);
            return buildAccountFlowResponse(outModel);
        }
        if (CoinEnum.KG.getCode() == type) {
            // outModel = accountFlowRMapper.getTxbDetailBill(inModel);
            AccountFlowOutModel130 outModel = this.getTxbDetailBill(inModel);
            return buildAccountFlowResponse130(outModel);
        }
        if (CoinEnum.RIT.getCode() == type) {
            //outModel = accountFlowRitRMapper.getRitDetailBill(inModel);
            AccountFlowOutModel130 outModel = this.getRitDetailBill(inModel);
            return buildAccountFlowResponse130(outModel);
        }
        return null;
    }

    private AccountFlowOutModel130 buidAccountFlowOut(MongoCursor<Document> cursor) {
        AccountFlowOutModel130 accountFlowOutModel = new AccountFlowOutModel130();
        if (cursor != null) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                String amount = doc.get("amount") == null ? null : ((Decimal128) doc.get("amount")).bigDecimalValue().stripTrailingZeros().toPlainString();
                String freezeAmount = doc.get("freeze_amount") == null ? null : ((Decimal128) doc.get("freeze_amount")).bigDecimalValue().stripTrailingZeros().toPlainString();

                Long flowDate = doc.get("flow_date") == null ? 0L : doc.getLong("flow_date");
                Long accountFlowId = doc.get("account_flow_id") == null ? 0L : doc.getLong("account_flow_id");
                String remark = doc.get("remark") == null ? null : doc.getString("remark");
                String flowDetail = doc.get("flow_detail") == null ? "" : doc.getString("flow_detail");
                if (StringUtils.isNotEmpty(remark)) {
                    flowDetail += "(" + remark + ")";
                }
                String txAddress = doc.get("tx_address") == null ? "" : doc.getString("tx_address");
                Integer businessTypeId = doc.get("business_type_id") == null ? null : doc.getInteger("business_type_id");
                String businessName = "";
                if (businessTypeId != null) {
                    businessName = BusinessTypeEnum.getBusinessTypeEnum(businessTypeId).getDisplay();
                }
                accountFlowOutModel.setAccountFlowId(accountFlowId);
                accountFlowOutModel.setAmount(amount);

                Date date = new Date();
                date.setTime(flowDate);
                accountFlowOutModel.setFlowDate(date);

                accountFlowOutModel.setFlowDetail(flowDetail);
                accountFlowOutModel.setTxAddress(txAddress);
                accountFlowOutModel.setTypename(businessName);
                accountFlowOutModel.setRemark(remark);
                accountFlowOutModel.setFreezeAmount(freezeAmount);

            }
        }
        return accountFlowOutModel;
    }


    private AccountFlowOutModel130 getTxbDetailBill(AccountFlowInModel inModel) {
        BasicDBObject querry = new BasicDBObject();
        querry.append("account_flow_id", new BasicDBObject(Seach.EQ.getOperStr(), inModel.getAccountFlowId()));
        MongoCursor<Document> cursor = MongoUtils.findByFilter(MongoTables.ACCOUNT_FLOW_KG, querry);
        return this.buidAccountFlowOut(cursor);
    }


    private AccountFlowOutModel130 getRitDetailBill(AccountFlowInModel inModel) {
        BasicDBObject querry = new BasicDBObject();
        querry.append("account_flow_id", new BasicDBObject(Seach.EQ.getOperStr(), inModel.getAccountFlowId()));
        MongoCursor<Document> cursor = MongoUtils.findByFilter(MongoTables.ACCOUNT_FLOW_RIT, querry);
        return this.buidAccountFlowOut(cursor);
    }

    /**
     * 判断用户是否存在审核中的提现工单
     *
     * @return
     */
    private MongoCursor<Document> checkWithdrawing(CoinEnum coinEnum, Long userId) {
        if (CoinEnum.RIT.equals(coinEnum)) {
            Integer[] status = new Integer[]{0, 1};
            BasicDBObject filter = new BasicDBObject("userId", userId).append("status", new Document(Seach.IN.getOperStr(), status));
            BasicDBObject sort = new BasicDBObject("withdrawTime", -1);
            MongoCursor<Document> cursor = MongoUtils.findByPage(MongoTables.ACCOUNT_WITHDRAW_FLOW_RIT, filter, sort, 1, 1);
            return cursor;
        }
        return null;
    }

    /**
     * 判断用户是否实名认证以及是否设置交易密码
     *
     * @param userkgOutModel
     * @return
     */
    private AppJsonEntity checkAuthAndIfSetTranpassword(UserkgOutModel userkgOutModel) {
        //判断用户是否实名认证
        if (userkgOutModel == null || userkgOutModel.getRealnameAuthed() != 1) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.NOT_CERTIFICATE);
        }
        //判断用户是否设置交易密码
        if (userkgOutModel.getTradepasswordSet() == 0) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.NOT_SET_TRANPASSWORD);
        }
        return null;
    }

    @Override
    public AppJsonEntity ritWithdrawPageData(UserkgResponse kguser) {
        long now = System.currentTimeMillis();
        logger.info("获取RIT转出页面数据开始------");
        Map<String, Object> map = new HashMap<>();
        //判断用户是否实名认证 以及是否设置交易密码
        UserInModel userInModel = new UserInModel();
        userInModel.setUserId(Long.valueOf(kguser.getUserId()));
        UserkgOutModel userkgOutModel = userRMapper.selectByUser(userInModel);
        AppJsonEntity appJsonEntity = checkAuthAndIfSetTranpassword(userkgOutModel);
        if (appJsonEntity != null) {
            return appJsonEntity;
        }
        //判断用户是否存在审核中或者转出中的提现工单
        MongoCursor<Document> cursor = checkWithdrawing(CoinEnum.RIT, Long.valueOf(kguser.getUserId()));
        if (cursor != null && cursor.hasNext()) {
            //存在审核中的 则返回工单信息
            AccountWithdrawFlowRitResponse response = new AccountWithdrawFlowRitResponse();
            while (cursor.hasNext()) {
                Document document = cursor.next();
                response.setWithdrawFlowId(document.getLong("withdrawFlowId").toString());
                response.setWithdrawAmount(((Decimal128) document.get("withdrawAmount")).bigDecimalValue().stripTrailingZeros().toPlainString());
                response.setAccountAmount(((Decimal128) document.get("accountAmount")).bigDecimalValue().stripTrailingZeros().toPlainString());
                response.setPoundageAmount(((Decimal128) document.get("poundageAmount")).bigDecimalValue().stripTrailingZeros().toPlainString());
                response.setWithdrawTime(document.getLong("withdrawTime").toString());
                response.setToAddress(document.getString("toAddress"));
                response.setStatus(document.getInteger("status"));
            }

            map.put("state", 1);//表示存在审核中的工单
            map.put("ritFlowInfo", response);
            logger.info("获取RIT转出页面数据结束，耗时：{}ms------", (System.currentTimeMillis() - now));
            return AppJsonEntity.makeSuccessJsonEntity(map);
        } else {
            //获取余额
            Account account = accountRMapper.selectByUserId(Long.valueOf(kguser.getUserId()));
            //不存在的时候 返回RIT提现配置信息 以及今日已转出 当月已转出RIT数量
            //根据用户角色 获取提现配置
            Integer userRole = userkgOutModel.getUserRole() == 1 ? 0 : 1;
            KgRitRolloutOutModel kgRitRolloutOutModel = ritRolloutRMapper.selectByUserType(userRole);
            if (kgRitRolloutOutModel == null) {
                return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
            }
            //查询该用户今日已转出  当月已转出
            AccountWithdrawFlowRit in = new AccountWithdrawFlowRit();
            in.setUserId(Long.valueOf(kguser.getUserId()));
            in.setStatus(3);
            BasicDBObject filter = new BasicDBObject("userId", in.getUserId()).append("status", in.getStatus());
            Long start = DateUtils.getCurrentDayTimestamp();
            Long end = new Date().getTime();
            //获取当日转出总额
            BigDecimal currentDaySum = getSumWithdrawAmount(filter, start, end);
            start = DateUtils.getCurrentMonthTimestamp();
            //获取当月转出总额
            BigDecimal currentMonthSum = getSumWithdrawAmount(filter, start, end);

            RitWithdrawPageResponse response = new RitWithdrawPageResponse();
            response.setRate(kgRitRolloutOutModel.getRate());
            response.setMinAmount(kgRitRolloutOutModel.getMinAmount().toString());
            response.setDayLimit(kgRitRolloutOutModel.getDayLimit().toString());
            response.setMonthLimit(kgRitRolloutOutModel.getMonthLimit().toString());
            response.setTodayOut(currentDaySum.stripTrailingZeros().toPlainString());
            response.setMonthOut(currentMonthSum.stripTrailingZeros().toPlainString());
            response.setBalance(account.getRitBalance().stripTrailingZeros().toPlainString());

            map.put("state", 2);//表示不存在审核中的工单
            map.put("withdrawPageInfo", response);
            logger.info("获取RIT转出页面数据结束，耗时：{}ms------", (System.currentTimeMillis() - now));
            return AppJsonEntity.makeSuccessJsonEntity(map);
        }
    }

    @Override
    @Transactional
    public AppJsonEntity withdrawByCoinType(UserkgResponse kguser, AccountWithDrawRequest request) {
        logger.info("APP提现申请入参：{}", JSON.toJSONString(request));
        //校验入参
        if (StringUtils.isBlank(request.getTxAddress()) || request.getTxAmount() == null || StringUtils.isBlank(request.getCode()) || StringUtils.isBlank(request.getTxPassword()) || request.getCoinType() == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if (request.getTxAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return AppJsonEntity.makeExceptionJsonEntity("", "请输入大于0的数字，支持小数点后八位");
        }
        if (request.getTxAddress().length() > 100) {
            return AppJsonEntity.makeExceptionJsonEntity("", "转出地址不能大于100个字符！");
        }
        if (StringUtils.isNotBlank(request.getMemo()) && request.getMemo().length() > 50) {
            return AppJsonEntity.makeExceptionJsonEntity("", "备注不能大于50个字符！");
        }

        CoinEnum coinEnum = CoinEnum.getByCode(request.getCoinType());
        //判断用户是否实名认证 以及是否设置交易密码
        UserInModel userInModel = new UserInModel();
        userInModel.setUserId(Long.valueOf(kguser.getUserId()));
        UserkgOutModel userkgOutModel = userRMapper.selectByUser(userInModel);
        AppJsonEntity appJsonEntity = checkAuthAndIfSetTranpassword(userkgOutModel);
        if (appJsonEntity != null) {
            return appJsonEntity;
        }
        //校验手机验证码
        Result<String> checkcode = userkgService.checkSmsEmailcode(userkgOutModel.getUserMobile());
        if (checkcode.getData() == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_VALIDATE.getCode(), "验证码有误！请重新输入");
        } else if (!checkcode.getData().equals(request.getCode())) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_VALIDATE.getCode(), "验证码有误！请重新输入");
        }
        jedisUtils.del(JedisKey.vatcodeKey(userkgOutModel.getUserMobile()));
        //校验交易密码是否正确
        AccountInModel accountInModel = new AccountInModel();
        accountInModel.setTxPassword(MD5Util.md5Hex(request.getTxPassword() + Constants.SALT));
        accountInModel.setUserId(Long.valueOf(kguser.getUserId()));
        AccountOutModel accountOutModel = accountRMapper.validationPwd(accountInModel);
        if (accountOutModel == null) {
            int txTimes = userkgService.checkTxpassLimit(String.valueOf(kguser.getUserId()));
            if (txTimes == 0) {
                return AppJsonEntity.makeExceptionJsonEntity(20014, "您已连续输错超过5次，将暂时无法进行相关操作");
            } else {
                return AppJsonEntity.makeExceptionJsonEntity(20014,
                        "交易密码错误，还有" + (5 - txTimes) + "次机会，连续输错超过5次，您将暂时无法进行相关操作。");
            }
        }

        //初始化账户
        initAccount(Long.valueOf(kguser.getUserId()));
        BigDecimal fee;//手续费

        if (CoinEnum.RIT.equals(coinEnum)) {
            //校验余额是否足够
            if (accountOutModel.getRitBalance().compareTo(request.getTxAmount()) < 0) {
                return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACCOUNT_BALANCE_ERROR);
            }

            //查询该用户今日已转出  当月已转出
            AccountWithdrawFlowRit in = new AccountWithdrawFlowRit();
            in.setUserId(Long.valueOf(kguser.getUserId()));
            in.setStatus(3);
            BasicDBObject filter = new BasicDBObject("userId", in.getUserId()).append("status", in.getStatus());
            Long start = DateUtils.getCurrentDayTimestamp();
            Long end = new Date().getTime();
            //获取当日转出总额
            BigDecimal currentDaySum = getSumWithdrawAmount(filter, start, end);
            start = DateUtils.getCurrentMonthTimestamp();
            //获取当月转出总额
            BigDecimal currentMonthSum = getSumWithdrawAmount(filter, start, end);
            //根据用户角色 获取提现配置
            Integer userRole = userkgOutModel.getUserRole() == 1 ? 0 : 1;
            KgRitRolloutOutModel kgRitRolloutOutModel = ritRolloutRMapper.selectByUserType(userRole);

            //校验提现配置设置的提现额等
            if (kgRitRolloutOutModel != null) {
                //计算当日剩余可转出数量
                BigDecimal accessCurrentDaySum = new BigDecimal(kgRitRolloutOutModel.getDayLimit().toString()).subtract(currentDaySum);
                //计算当月剩余可转出数量
                BigDecimal accessCurrentMonthSum = new BigDecimal(kgRitRolloutOutModel.getMonthLimit().toString()).subtract(currentMonthSum);

                if (request.getTxAmount().doubleValue() < kgRitRolloutOutModel.getMinAmount()) {
                    //提现数量小于最低转出
                    return AppJsonEntity.makeExceptionJsonEntity("", "每次最少转出" + kgRitRolloutOutModel.getMinAmount() + "RIT");
                }
                if (accessCurrentDaySum.compareTo(request.getTxAmount()) < 0) {
                    //转出数量大于今日剩余
                    return AppJsonEntity.makeExceptionJsonEntity("", "已超出今日转出上限");
                }
                if (accessCurrentMonthSum.compareTo(request.getTxAmount()) < 0) {
                    //当月转出总额超过了当月限额
                    return AppJsonEntity.makeExceptionJsonEntity("", "已超出当月转出上限");
                }
            } else {
                return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
            }

            //计算手续费
            BigDecimal feePercent = new BigDecimal(kgRitRolloutOutModel.getRate().toString()).divide(new BigDecimal("100"));
            fee = request.getTxAmount().multiply(feePercent);
        } else {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        //经过重重检验过后 正式进行申请提现操作
        request.setUserId(kguser.getUserId());
        request.setFee(fee);
        request.setUserRole(userkgOutModel.getUserRole());
        return doWithDraw(request, coinEnum, userkgOutModel, null);
    }


    @Transactional
    public AppJsonEntity doWithDraw(AccountWithDrawRequest request, CoinEnum coinEnum, UserkgOutModel kguser, SysUser sysUser) {

        Long now = System.currentTimeMillis();
        Lock lock = new Lock();
        String key = USER_ADDUSERTIMES_LOCK_KEY + request.getUserId();
        Long withdrawFlowId = idGen.nextId();
        Long account_flow_id1 = idGen.nextId();
        Long account_flow_id2 = idGen.nextId();
        Long operId = idGen.nextId();
        try {
            if (sysUser == null) {
                lock.lock(key);
            }

            //扣除用户对应币种余额  增加对应币种冻结余额
            if (CoinEnum.RIT.equals(coinEnum)) {

                // 查申请中的提现申请
                if (sysUser == null) {
                    MongoCursor<Document> cursor = checkWithdrawing(CoinEnum.RIT, Long.valueOf(request.getUserId()));
                    if (cursor != null && cursor.hasNext()) {
                        return AppJsonEntity.makeExceptionJsonEntity("", "存在申请中的提现申请,请勿重复提交");
                    }
                }
                //操作RIT资产
                AccountInModel accountInModel = new AccountInModel();
                accountInModel.setBalance(request.getTxAmount());
                accountInModel.setUserId(Long.valueOf(request.getUserId()));
                int state = accountWMapper.addRitFrozenBalanceReduceRitBalance(accountInModel);
                if (state > 0) {
                    //插入RIT提现记录
                    AccountWithdrawFlowRit accountWithdrawFlowRit = new AccountWithdrawFlowRit();
                    accountWithdrawFlowRit.setUserId(Long.valueOf(request.getUserId()));
                    accountWithdrawFlowRit.setWithdrawFlowId(withdrawFlowId);
                    accountWithdrawFlowRit.setUserName(kguser.getUserName());
                    accountWithdrawFlowRit.setUserPhone(kguser.getUserMobile());
                    accountWithdrawFlowRit.setWithdrawAmount(request.getTxAmount().setScale(8, BigDecimal.ROUND_HALF_UP));
                    accountWithdrawFlowRit.setAccountAmount(request.getTxAmount().subtract(request.getFee()).setScale(8, BigDecimal.ROUND_HALF_UP));
                    accountWithdrawFlowRit.setPoundageAmount(request.getFee().setScale(8, BigDecimal.ROUND_HALF_UP));
                    accountWithdrawFlowRit.setWithdrawTime(now);
                    accountWithdrawFlowRit.setFromAddress("");
                    accountWithdrawFlowRit.setToAddress(request.getTxAddress());
                    accountWithdrawFlowRit.setMemo(request.getMemo());
                    accountWithdrawFlowRit.setStatus(OperTypeEnum.WITHDRAW_PENDING.getStatus());
                    UserRoleEnum userRoleEnum = UserRoleEnum.getUserRoleEnum(request.getUserRole());
                    accountWithdrawFlowRit.setUserRole(userRoleEnum.getRole());
                    accountWithdrawFlowRit.setUserRoleName(userRoleEnum.getDisplay());
                    if (sysUser != null) {
                        accountWithdrawFlowRit.setManualTransferUserId(sysUser.getSysUserId());
                    }
                    MongoUtils.insertOne(MongoTables.ACCOUNT_WITHDRAW_FLOW_RIT, new Document(Bean2MapUtil.bean2map(accountWithdrawFlowRit)));
                    //插入RIT用户流水记录
                    List<Document> documents = Lists.newArrayList();
                    //可用余额减少流水
                    AccountFlowRitMongo accountFlowRitMongo_balance = new AccountFlowRitMongo();
                    accountFlowRitMongo_balance.setAccount_flow_id(account_flow_id1);
                    accountFlowRitMongo_balance.setRelation_flow_id(accountWithdrawFlowRit.getWithdrawFlowId());
                    accountFlowRitMongo_balance.setUser_id(accountWithdrawFlowRit.getUserId());
                    accountFlowRitMongo_balance.setUser_name(kguser.getUserName());
                    accountFlowRitMongo_balance.setUser_phone(accountWithdrawFlowRit.getUserPhone());
                    accountFlowRitMongo_balance.setUser_email(kguser.getUserEmail());
                    accountFlowRitMongo_balance.setAmount(request.getTxAmount().setScale(8, BigDecimal.ROUND_HALF_UP).negate());
                    accountFlowRitMongo_balance.setBusiness_type_id(BusinessTypeEnum.WITHDRAW_NEW.getStatus());
                    accountFlowRitMongo_balance.setTx_address(request.getTxAddress());
                    accountFlowRitMongo_balance.setAccount_amount(accountWithdrawFlowRit.getWithdrawAmount());
                    accountFlowRitMongo_balance.setPoundage_amount(BigDecimal.ZERO);
                    accountFlowRitMongo_balance.setFlow_status(FlowStatusEnum.AUDITING.getStatus());
                    accountFlowRitMongo_balance.setFlow_date(now);
                    accountFlowRitMongo_balance.setFlow_detail("提交转出申请。");
                    accountFlowRitMongo_balance.setRemark(request.getMemo());
                    //冻结余额增加流水
                    AccountFlowRitMongo accountFlowRitMongo_fozen = new AccountFlowRitMongo();
                    BeanUtils.copyProperties(accountFlowRitMongo_balance, accountFlowRitMongo_fozen);
                    accountFlowRitMongo_fozen.setAccount_flow_id(account_flow_id2);
                    accountFlowRitMongo_fozen.setAmount(new BigDecimal("0.00000000"));
                    accountFlowRitMongo_fozen.setBusiness_type_id(BusinessTypeEnum.WITHDRAW_FROZEN.getStatus());
                    accountFlowRitMongo_fozen.setFreeze_amount(request.getTxAmount().setScale(8, BigDecimal.ROUND_HALF_UP));

                    documents.add(new Document(Bean2MapUtil.bean2map(accountFlowRitMongo_balance)));
                    documents.add(new Document(Bean2MapUtil.bean2map(accountFlowRitMongo_fozen)));
                    MongoUtils.insertMany(MongoTables.ACCOUNT_FLOW_RIT, documents);
                    //插入操作日志记录
                    WithdrawOperRecord withdrawOperRecord = new WithdrawOperRecord();
                    withdrawOperRecord.setOperId(operId);
                    withdrawOperRecord.setWithdrawFlowId(accountWithdrawFlowRit.getWithdrawFlowId());
                    if (sysUser != null) {
                        withdrawOperRecord.setOperUserId(sysUser.getSysUserId());
                        withdrawOperRecord.setOperUserName(sysUser.getSysUserName());
                    } else {
                        withdrawOperRecord.setOperUserId(Long.valueOf(request.getUserId()));
                        withdrawOperRecord.setOperUserName(kguser.getUserName());
                    }
                    withdrawOperRecord.setOperType(OperTypeEnum.WITHDRAW_PENDING.getStatus());
                    withdrawOperRecord.setOperTypeName(OperTypeEnum.WITHDRAW_PENDING.getDisplay());
                    withdrawOperRecord.setOperDate(now);
                    withdrawOperRecord.setRemark(request.getMemo());
                    MongoUtils.insertOne(MongoTables.WITHDRAW_OPER_RECORD, new Document(Bean2MapUtil.bean2map(withdrawOperRecord)));
                    return AppJsonEntity.makeSuccessJsonEntity(ExceptionEnum.SUCCESS);
                } else {
                    return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.SERVICEERROR);
                }
            } else {
                return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
            }
        } catch (Exception e) {
            //发生异常时，删除MONGO中的记录
            logger.error("提现申请发生异常：{},___异常时请求参数：{}", JSON.toJSONString(e.getStackTrace()), JSON.toJSONString(request));
            logger.error("提现申请发生异常：{},___异常时请求参数：{}", e.getMessage(), JSON.toJSONString(request));
            BasicDBObject filter = new BasicDBObject();
            filter.append("withdrawFlowId", withdrawFlowId);
            MongoUtils.deleteByFilter(MongoTables.ACCOUNT_WITHDRAW_FLOW_RIT, filter);

            filter.clear();
            filter.append("account_flow_id", account_flow_id1);
            MongoUtils.deleteByFilter(MongoTables.ACCOUNT_FLOW_RIT, filter);

            filter.clear();
            filter.append("account_flow_id", account_flow_id2);
            MongoUtils.deleteByFilter(MongoTables.ACCOUNT_FLOW_RIT, filter);

            filter.clear();
            filter.append("operId", operId);
            MongoUtils.deleteByFilter(MongoTables.WITHDRAW_OPER_RECORD, filter);

            throw new BusinessException(ExceptionEnum.SERVERERROR);
        } finally {
            if (sysUser == null) {
                lock.unLock();
            }
        }
    }

    @Override
    public AppJsonEntity performanceEarnings(UserkgResponse kguser) {
        BigDecimal lastPriceCNY = lastPriceCNY();
        PerformanceEarningsResponse response = new PerformanceEarningsResponse();
        Long userId = Long.valueOf(kguser.getUserId());
        response.setPerformance(buildPerformance(userId));
        response.setBtcBalance(buildBtcBalance(userId));
        response.setRmbBalance(new BigDecimal(response.getBtcBalance()).multiply(lastPriceCNY).setScale(2, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());
        response.setYesterdayEarningsBtc(buildYesterdayEarnings(userId));
        response.setYesterdayEarningsRmb(new BigDecimal(response.getYesterdayEarningsBtc()).multiply(lastPriceCNY).setScale(2, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());
        response.setTotalEarningsBtc(buildTotalEarnings(userId));
        response.setTotalEarningsRmb(new BigDecimal(response.getTotalEarningsBtc()).multiply(lastPriceCNY).setScale(2, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());
        return AppJsonEntity.makeSuccessJsonEntity(response);
    }

    /**
     * 获取汇率
     *
     * @return
     */
    private BigDecimal lastPriceCNY() {
        String result = HttpUtil.get("https://appapi.ks.top/api/currency/appCurrencyList", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36");
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONArray jsonArray = jsonObject.getJSONArray("responseBody");
        JSONObject json = jsonArray.getJSONObject(0);
        CurrencyResponse currencyResponse = JSONObject.parseObject(JSONObject.toJSONString(json), CurrencyResponse.class);
        return new BigDecimal(currencyResponse.getLastPriceCNY());
    }

    /**
     * 获取总收益
     *
     * @param userId
     * @return
     */
    private String buildTotalEarnings(Long userId) {
        String time = DateUtils.formatYMD(DateUtils.getBeforeDay(new Date(), 1)) + " 00:00:00";
        Double totalEarnings = cloudOrderMapper.getTotalEarnings(userId, time);
        if (totalEarnings == null) {
            return "0";
        }
        return new BigDecimal(totalEarnings).setScale(8,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString();
    }

    /**
     * 获取昨日收益
     *
     * @param userId
     * @return
     */
    private String buildYesterdayEarnings(Long userId) {
        return buildYesterdayEarningsBtc(userId, new UserBaseInfoResponse()).getYesterdayEarningsBtc();
    }

    /**
     * 获取我的BTC余额
     *
     * @param userId
     * @return
     */
    private String buildBtcBalance(Long userId) {
        BigDecimal btcBalance = accountRMapper.getBtcBalance(userId);
        if (btcBalance == null) {
            return "0";
        }
        return btcBalance.stripTrailingZeros().toPlainString();
    }

    /**
     * 获取我的算力
     *
     * @param userId
     * @return
     */
    private String buildPerformance(Long userId) {
        String time = DateUtils.formatYMD(DateUtils.getDateStart(new Date())) + " 00:00:00";
        Double performance = cloudOrderMapper.getPerformance(userId, time);
        if (performance == null) {
            return "0";
        }
        return new BigDecimal(performance).stripTrailingZeros().toPlainString();
    }

    @Override
    @Transactional
    public AppJsonEntity cancelWithdraw(UserkgResponse kguser, AccountWithDrawRequest request) {

        Long now = System.currentTimeMillis();
        //参数校验
        if (StringUtils.isBlank(request.getWithdrawFlowId()) || request.getCoinType() == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }

        UserInModel userInModel = new UserInModel();
        userInModel.setUserId(Long.valueOf(kguser.getUserId()));
        UserkgOutModel userkgOutModel = userRMapper.selectByUser(userInModel);
        CoinEnum coinEnum = CoinEnum.getByCode(request.getCoinType());
        Lock lock = new Lock();
        String key = USER_ADDUSERTIMES_LOCK_KEY + request.getWithdrawFlowId();
        Long account_flow_id1 = idGen.nextId();
        Long account_flow_id2 = idGen.nextId();
        Long operId = idGen.nextId();
        try {
            BasicDBObject filter = new BasicDBObject("withdrawFlowId", Long.valueOf(request.getWithdrawFlowId()));
            AccountWithdrawFlowRit accountWithdrawFlowRit = getByWithdrawFlowId(request.getWithdrawFlowId());
            if (accountWithdrawFlowRit == null) {
                return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.NO_DATA);
            }

            lock.lock(key);
            //查询工单是否在审核中
            if (coinEnum.equals(CoinEnum.RIT)) {

                //判断该工单是否属于该用户
                if (!accountWithdrawFlowRit.getUserId().equals(Long.valueOf(kguser.getUserId()))) {
                    return AppJsonEntity.makeExceptionJsonEntity("", "该工单不属于当前用户！");
                }
                if (accountWithdrawFlowRit.getStatus() == OperTypeEnum.WITHDRAW_PENDING.getStatus()) {
                    //审核中的工单才能进行撤销
                    //扣除RIT冻结余额,增加RIT可用余额
                    AccountInModel accountInModel = new AccountInModel();
                    accountInModel.setUserId(Long.valueOf(kguser.getUserId()));
                    accountInModel.setBalance(accountWithdrawFlowRit.getWithdrawAmount());
                    int state = accountWMapper.addRitBalanceReduceRitFrozenBalance(accountInModel);
                    if (state > 0) {
                        //修改工单状态为撤销
                        int check = MongoUtils.updateByFilter(MongoTables.ACCOUNT_WITHDRAW_FLOW_RIT, filter, new Document("status", OperTypeEnum.WITHDRAW_CANCEL.getStatus()));
                        if (check > 0) {
                            addRitBalanceReduceRitFrozen(accountWithdrawFlowRit, userkgOutModel, null, account_flow_id1, account_flow_id2, operId, now, "撤销转出申请。", BusinessTypeEnum.WITHDRAW_CANCEL, BusinessTypeEnum.WITHDRAW_CANCEL_FROZEN, FlowStatusEnum.CANCELED, OperTypeEnum.WITHDRAW_CANCEL);
                            return AppJsonEntity.makeSuccessJsonEntity(ExceptionEnum.SUCCESS);
                        } else {
                            logger.error("撤销发生异常——修改工单状态失败------");
                            throw new BusinessException(ExceptionEnum.SERVICEERROR);
                        }

                    } else {
                        logger.error("撤销发生异常——操作账户资产失败------");
                        return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.SERVICEERROR);
                    }
                } else {
                    //不是待审核状态
                    if (accountWithdrawFlowRit.getStatus() == OperTypeEnum.WITHDRAW_WATING.getStatus()) {
                        return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.CANCEL_FAIL_PASSED);
                    } else if (accountWithdrawFlowRit.getStatus() == OperTypeEnum.WITHDRAW_NO_PASS.getStatus()) {
                        return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.CANCEL_FAIL_NO_PASSED);
                    } else {
                        return AppJsonEntity.makeExceptionJsonEntity("", "撤销失败");
                    }
                }

            } else {
                return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
            }

        } catch (Exception e) {

            //发生异常时，删除MONGO中的记录
            logger.error("撤销发生异常：{},___异常时请求参数：{}", JSON.toJSONString(e.getStackTrace()), JSON.toJSONString(request));
            logger.error("撤销发生异常：{},___异常时请求参数：{}", e.getMessage(), JSON.toJSONString(request));
            BasicDBObject filter = new BasicDBObject();

            filter.clear();
            filter.append("account_flow_id", account_flow_id1);
            MongoUtils.deleteByFilter(MongoTables.ACCOUNT_FLOW_RIT, filter);

            filter.clear();
            filter.append("account_flow_id", account_flow_id2);
            MongoUtils.deleteByFilter(MongoTables.ACCOUNT_FLOW_RIT, filter);

            filter.clear();
            filter.append("operId", operId);
            MongoUtils.deleteByFilter(MongoTables.WITHDRAW_OPER_RECORD, filter);
            throw new BusinessException(ExceptionEnum.SERVERERROR);
        } finally {
            lock.unLock();
        }
    }

    public AccountWithdrawFlowRit getByWithdrawFlowId(String withdrawFlowId) {
        BasicDBObject filter = new BasicDBObject("withdrawFlowId", Long.valueOf(withdrawFlowId));
        MongoCursor<Document> cursors = MongoUtils.findByFilter(MongoTables.ACCOUNT_WITHDRAW_FLOW_RIT, filter);
        AccountWithdrawFlowRit accountWithdrawFlowRit = new AccountWithdrawFlowRit();
        if (!cursors.hasNext()) {
            return null;
        } else {
            while (cursors.hasNext()) {
                Document document = cursors.next();
                accountWithdrawFlowRit.setUserId(document.getLong("userId"));
                accountWithdrawFlowRit.setStatus(document.getInteger("status"));
                accountWithdrawFlowRit.setWithdrawAmount(((Decimal128) document.get("withdrawAmount")).bigDecimalValue());
                accountWithdrawFlowRit.setWithdrawFlowId(document.getLong("withdrawFlowId"));
                accountWithdrawFlowRit.setUserPhone(document.getString("userPhone"));
                accountWithdrawFlowRit.setToAddress(document.getString("toAddress"));
                accountWithdrawFlowRit.setAccountAmount(((Decimal128) document.get("accountAmount")).bigDecimalValue());
                accountWithdrawFlowRit.setPoundageAmount(((Decimal128) document.get("poundageAmount")).bigDecimalValue());
                accountWithdrawFlowRit.setMemo(document.getString("memo"));
            }
        }
        return accountWithdrawFlowRit;
    }


    public void addRitBalanceReduceRitFrozen(AccountWithdrawFlowRit accountWithdrawFlowRit, UserkgOutModel kguser, SysUser operKguser,
                                             Long balance_account_flow_id, Long frozen_account_flow_id, Long operId, Long now, String flowDetail,
                                             BusinessTypeEnum balanceBusinessTypeEnum, BusinessTypeEnum frozenBusinessTypeEnum, FlowStatusEnum flowStatusEnum, OperTypeEnum operTypeEnum) {
        //插入RIT用户流水记录
        List<Document> documents = Lists.newArrayList();
        //可用余额增加流水
        AccountFlowRitMongo accountFlowRitMongo_balance = new AccountFlowRitMongo();
        accountFlowRitMongo_balance.setAccount_flow_id(balance_account_flow_id);
        accountFlowRitMongo_balance.setRelation_flow_id(accountWithdrawFlowRit.getWithdrawFlowId());
        accountFlowRitMongo_balance.setUser_id(accountWithdrawFlowRit.getUserId());
        accountFlowRitMongo_balance.setUser_name(kguser.getUserName());
        accountFlowRitMongo_balance.setUser_phone(kguser.getUserMobile());
        accountFlowRitMongo_balance.setUser_email(kguser.getUserEmail());
        accountFlowRitMongo_balance.setAmount(accountWithdrawFlowRit.getWithdrawAmount());
        accountFlowRitMongo_balance.setBusiness_type_id(balanceBusinessTypeEnum.getStatus());
        accountFlowRitMongo_balance.setTx_address(accountWithdrawFlowRit.getToAddress());
        accountFlowRitMongo_balance.setAccount_amount(accountWithdrawFlowRit.getWithdrawAmount());
        accountFlowRitMongo_balance.setPoundage_amount(BigDecimal.ZERO);
        accountFlowRitMongo_balance.setFlow_status(flowStatusEnum.getStatus());
        accountFlowRitMongo_balance.setFlow_date(now);
        accountFlowRitMongo_balance.setFlow_detail(flowDetail);
        accountFlowRitMongo_balance.setRemark(accountWithdrawFlowRit.getMemo());
        //冻结余额减少流水
        AccountFlowRitMongo accountFlowRitMongo_fozen = new AccountFlowRitMongo();
        BeanUtils.copyProperties(accountFlowRitMongo_balance, accountFlowRitMongo_fozen);
        accountFlowRitMongo_fozen.setAccount_flow_id(frozen_account_flow_id);
        accountFlowRitMongo_fozen.setAmount(new BigDecimal("0.00000000"));
        accountFlowRitMongo_fozen.setBusiness_type_id(frozenBusinessTypeEnum.getStatus());
        accountFlowRitMongo_fozen.setFreeze_amount(accountWithdrawFlowRit.getWithdrawAmount().negate());

        documents.add(new Document(Bean2MapUtil.bean2map(accountFlowRitMongo_balance)));
        documents.add(new Document(Bean2MapUtil.bean2map(accountFlowRitMongo_fozen)));
        MongoUtils.insertMany(MongoTables.ACCOUNT_FLOW_RIT, documents);

        //插入操作日志记录
        WithdrawOperRecord withdrawOperRecord = new WithdrawOperRecord();
        withdrawOperRecord.setOperId(operId);
        withdrawOperRecord.setWithdrawFlowId(accountWithdrawFlowRit.getWithdrawFlowId());
        if (operKguser == null) {
            withdrawOperRecord.setOperUserId(Long.valueOf(kguser.getUserId()));
            withdrawOperRecord.setOperUserName(kguser.getUserName());
        } else {
            withdrawOperRecord.setOperUserId(operKguser.getSysUserId());
            withdrawOperRecord.setOperUserName(operKguser.getSysUserName());
        }
        withdrawOperRecord.setOperType(operTypeEnum.getStatus());
        withdrawOperRecord.setOperTypeName(operTypeEnum.getDisplay());
        withdrawOperRecord.setTxId(accountWithdrawFlowRit.getTxId());
        withdrawOperRecord.setOperDate(now);
        withdrawOperRecord.setRemark(accountWithdrawFlowRit.getMemo());
        MongoUtils.insertOne(MongoTables.WITHDRAW_OPER_RECORD, new Document(Bean2MapUtil.bean2map(withdrawOperRecord)));
    }

    private BigDecimal getSumWithdrawAmount(BasicDBObject filter, Long start, Long end) {
        long now = System.currentTimeMillis();
        logger.info("获取一个时间段转出RIT总额开始------");
        List<Bson> listFilter = Lists.newArrayList();
        filter.append("withdrawTime", new BasicDBObject(Seach.GTE.getOperStr(), start).append(Seach.LTE.getOperStr(), end));
        BasicDBObject match = new BasicDBObject(Seach.MATCH.getOperStr(), filter);
        BasicDBObject group = new BasicDBObject(Seach.GROUP.getOperStr(), new BasicDBObject("sum", new Document(Seach.SUM.getOperStr(), "$withdrawAmount")).append("_id", "$userId"));
        listFilter.add(match);
        listFilter.add(group);
        MongoCursor<Document> documentMongoCursor = MongoUtils.aggregate(MongoTables.ACCOUNT_WITHDRAW_FLOW_RIT, listFilter);
        BigDecimal sum = BigDecimal.ZERO;
        while (documentMongoCursor != null && documentMongoCursor.hasNext()) {
            Document document = documentMongoCursor.next();
            sum = ((Decimal128) document.get("sum")).bigDecimalValue();
        }
        logger.info("获取一个时间段转出RIT总额结束，耗时：{}ms", (System.currentTimeMillis() - now));
        return sum.setScale(8, BigDecimal.ROUND_HALF_UP);
    }

//    @Override
//    public PageModel<AccountFlowAppResponse> selectUserRitflow(AccountFlowAppRequest request, PageModel<AccountFlowAppResponse> page) {
//        logger.info("我的RIT交易明细：{},分页入参：{}", JSON.toJSONString(request), JSON.toJSONString(page));
//        AccountFlowInModel inModel = buildAccountFlowInModel(request, page);
//        List<AccountFlowOutModel> list = accountFlowRitRMapper.selectUserRitflowForApp(inModel);
//        List<AccountFlowAppResponse> responses = buildAccountFlowAppResponseListOld(list);
//        long count = accountFlowRitRMapper.selectUserRitflowCountForApp(inModel);
//        page.setData(responses);
//        page.setTotalNumber(count);
//        return page;
//    }


    @Override
    public PageModel<AccountFlowAppResponse> selectUserRitflow(AccountFlowAppRequest request, PageModel<AccountFlowAppResponse> page) {
        logger.info("我的RIT交易明细：{},分页入参：{}", JSON.toJSONString(request), JSON.toJSONString(page));
        BasicDBObject querry = this.buildRitAccountFlowQuerry130(request);
        BasicDBObject sort = new BasicDBObject();
        sort.put("flow_date", -1);
        logger.info(">>>>>>>>>>>>>>>>>>selectUserRitflow130:" + querry.toString());
        MongoCursor<Document> list = MongoUtils.findByPage(MongoTables.ACCOUNT_FLOW_RIT, querry, sort, page.getCurrentPage(), page.getPageSize());
        List<AccountFlowAppResponse> responses = buildAccountFlowAppResponseListOld(list);
        page.setData(responses);
        Long count = MongoUtils.count(MongoTables.ACCOUNT_FLOW_RIT, querry);
        page.setTotalNumber(count);
        return page;
    }


    @Override
    public PageModel<AccountFlowAppNewResponse> selectUserTzflow125(AccountFlowAppRequest request, PageModel<AccountFlowAppNewResponse> page) {
        logger.info("我的钛值交易明细：{},分页入参：{}", JSON.toJSONString(request), JSON.toJSONString(page));
        AccountFlowInModel inModel = buildAccountFlowNewInModel(request, page);
        List<AccountFlowOutModel> list = accountFlowRMapper.selectByUserflowForApp125(inModel);
        List<AccountFlowAppNewResponse> responses = buildAccountFlowAppResponseList(list);
        long count = accountFlowRMapper.selectByUserflowCountForApp125(inModel);
        page.setData(responses);
        page.setTotalNumber(count);
        return page;
    }

//    @Override
//    public PageModel<AccountFlowAppNewResponse> selectUserTxbflow125(AccountFlowAppRequest request, PageModel<AccountFlowAppNewResponse> page) {
//        logger.info("我的钛小白交易明细：{},分页入参：{}", JSON.toJSONString(request), JSON.toJSONString(page));
//        AccountFlowInModel inModel = buildAccountFlowNewInModel(request, page);
//        List<AccountFlowOutModel> list = accountFlowRMapper.selectUserTxbflowForApp125(inModel);
//        List<AccountFlowAppNewResponse> responses = buildAccountFlowAppResponseList(list);
//        long count = accountFlowRMapper.selectUserTxbflowCountForApp125(inModel);
//        page.setData(responses);
//        page.setTotalNumber(count);
//        return page;
//    }

//    @Override
//    public PageModel<AccountFlowAppNewResponse> selectUserRitflow125(AccountFlowAppRequest request, PageModel<AccountFlowAppNewResponse> page) {
//        logger.info("我的RIT交易明细：{},分页入参：{}", JSON.toJSONString(request), JSON.toJSONString(page));
//        AccountFlowInModel inModel = buildAccountFlowNewInModel(request, page);
//        List<AccountFlowOutModel> list = accountFlowRitRMapper.selectUserRitflowForApp(inModel);
//        List<AccountFlowAppNewResponse> responses = buildAccountFlowAppResponseList(list);
//        long count = accountFlowRitRMapper.selectUserRitflowCountForApp(inModel);
//        page.setData(responses);
//        page.setTotalNumber(count);
//        return page;
//    }

    @Override
    public PageModel<AccountFlowAppNewResponse> selectUserTxbflow130(AccountFlowAppRequest request, PageModel<AccountFlowAppNewResponse> page) {
        BasicDBObject querry = this.buildKgAccountFlowQuerry130(request);
        BasicDBObject sort = new BasicDBObject();
        sort.put("flow_date", -1);
        logger.info(">>>>>>>>>>>>>>>>>>selectUserTxbflow130:" + querry.toString());
        MongoCursor<Document> list = MongoUtils.findByPage(MongoTables.ACCOUNT_FLOW_KG, querry, sort, page.getCurrentPage(), page.getPageSize());
        List<AccountFlowAppNewResponse> responses = buildAccountFlowTxbAppResponseList130(list);
        page.setData(responses);
        Long count = MongoUtils.count(MongoTables.ACCOUNT_FLOW_KG, querry);
        page.setTotalNumber(count);
        return page;
    }

    @Override
    public PageModel<AccountFlowAppNewResponse> selectUserRitflow130(AccountFlowAppRequest request, PageModel<AccountFlowAppNewResponse> page) {
        logger.info("我的RIT交易明细：{},分页入参：{}", JSON.toJSONString(request), JSON.toJSONString(page));
        BasicDBObject querry = this.buildRitAccountFlowQuerry130(request);
        BasicDBObject sort = new BasicDBObject();
        sort.put("flow_date", -1);
        logger.info(">>>>>>>>>>>>>>>>>>selectUserRitflow130:" + querry.toString());
        MongoCursor<Document> list = MongoUtils.findByPage(MongoTables.ACCOUNT_FLOW_RIT, querry, sort, page.getCurrentPage(), page.getPageSize());
        List<AccountFlowAppNewResponse> responses = buildAccountFlowTxbAppResponseList130(list);
        page.setData(responses);
        Long count = MongoUtils.count(MongoTables.ACCOUNT_FLOW_RIT, querry);
        page.setTotalNumber(count);
        return page;

    }

    private BasicDBObject buildKgAccountFlowQuerry130(AccountFlowAppRequest request) {
        BasicDBObject querry = new BasicDBObject();
        //收入或支出
        if (request.getClassify() == 1) {
            //收入
            int typeId[] = {1000, 1510, 1520, 1560, 1570, 1010, 1530, 2109, 2111, 2113, 2114, 2115, 2116};
            querry.put("business_type_id", new BasicDBObject(Seach.IN.getOperStr(), typeId));
        } else if (request.getClassify() == 2) {
            //支出
            int typeId[] = {0, 1020, 2101, 2107};
            querry.put("business_type_id", new BasicDBObject(Seach.IN.getOperStr(), typeId));
        } else {
            int typeId[] = {1000, 1510, 1520, 1560, 1570, 1010, 1530, 1020, 2101, 2107, 2109, 2111, 2113, 2114, 2115, 2116};
            querry.put("business_type_id", new BasicDBObject(Seach.IN.getOperStr(), typeId));
        }

        //时间查询
        querry = this.addTimeSerach130(querry, request);
        //用户
        querry.put("user_id", new BasicDBObject(Seach.EQ.getOperStr(), request.getUserId()));

        return querry;
    }


    /**
     * 构建返回值
     *
     * @param list
     * @return
     */
    private List<AccountFlowAppNewResponse> buildAccountFlowTxbAppResponseList130(MongoCursor<Document> list) {
        List<AccountFlowAppNewResponse> responses = new ArrayList<>();
        if (list != null) {
            while (list.hasNext()) {
                AccountFlowAppNewResponse flowResponse = new AccountFlowAppNewResponse();
                Document document = list.next();
                System.out.println(">>>>>>kg流水数据>>>>>>>" + JsonUtil.writeValueAsString(document));

                String accountFlowId = document.get("account_flow_id") == null ? "" : document.get("account_flow_id").toString();
                flowResponse.setAccountFlowId(accountFlowId);

                Integer typeName = document.get("business_type_id") == null ? null : document.getInteger("business_type_id");
                if (typeName != null) {
                    flowResponse.setTypeName(BusinessTypeEnum.getBusinessTypeEnum(typeName).getDisplay());
                }

                Long flowDate = document.get("flow_date") == null ? null : document.getLong("flow_date");
                flowResponse.setFlowDate(flowDate.toString());


                String amount = document.get("amount") == null ? null : ((Decimal128) document.get("amount")).bigDecimalValue().stripTrailingZeros().toPlainString();
                flowResponse.setAmount(amount);

                String freezeAmount = document.get("freeze_amount") == null ? null : ((Decimal128) document.get("freeze_amount")).bigDecimalValue().stripTrailingZeros().toPlainString();
                if (StringUtils.isNotEmpty(freezeAmount) && !"0".equals(freezeAmount)) {
                    flowResponse.setAmount(freezeAmount);
                    flowResponse.setFreezeState(1);
                } else {
                    flowResponse.setFreezeState(0);
                }
                String remark = document.get("remark") == null ? "" : document.get("remark").toString();
                flowResponse.setRemark(remark);

                String flowDetail = document.get("flow_detail") == null ? "" : document.get("flow_detail").toString();
                flowResponse.setFlowDetail(flowDetail);

                responses.add(flowResponse);
            }
        }
        return responses;
    }


    private BasicDBObject buildRitAccountFlowQuerry130(AccountFlowAppRequest request) {
        BasicDBObject querry = new BasicDBObject();

        //收入或支出
        if (request.getClassify() == 1) {
            //收入
            int typeId[] = {2000, 2100, 2102, 2104, 2003, 2004, 2005};
            querry.put("business_type_id", new BasicDBObject(Seach.IN.getOperStr(), typeId));

        } else if (request.getClassify() == 2) {
            //支出
            int typeId[] = {2001, 2106};
            querry.put("business_type_id", new BasicDBObject(Seach.IN.getOperStr(), typeId));
        } else {
            //全部
            int typeId[] = {2000, 2100, 2102, 2104, 2003, 2004, 2005, 2001, 2106};
            querry.put("business_type_id", new BasicDBObject(Seach.IN.getOperStr(), typeId));

        }
        //用户
        querry.put("user_id", new BasicDBObject(Seach.EQ.getOperStr(), request.getUserId()));
        querry = this.addTimeSerach130(querry, request);
        logger.info(">>>>>>>>>>>>>>>>querry:" + querry.toString());
        return querry;
    }

    private BasicDBObject addTimeSerach130(BasicDBObject querry, AccountFlowAppRequest request) {

        //时间查询
        if (StringUtils.isNotEmpty(request.getStartDate())) {
            Long st = DateUtils.parseDate(request.getStartDate(), "yyyy-MM-dd").getTime();
            querry.put("flow_date", new BasicDBObject(Seach.GTE.getOperStr(), st));

        }
        if (StringUtils.isNotEmpty(request.getEndDate())) {
            Long et = DateUtils.parseDate(request.getEndDate(), "yyyy-MM-dd").getTime();
            querry.put("flow_date", new BasicDBObject(Seach.LTE.getOperStr(), et));

        }
        if (StringUtils.isNotEmpty(request.getStartDate()) && StringUtils.isNotEmpty(request.getEndDate())) {
            Long st = DateUtils.parseDate(request.getStartDate(), "yyyy-MM-dd").getTime();
            Long et = DateUtils.parseDate(request.getEndDate(), "yyyy-MM-dd").getTime();
            querry.put("flow_date", BasicDBObjectBuilder.start(Seach.GTE.getOperStr(), st)
                    .add(Seach.LTE.getOperStr(), et).get());
        }

        return querry;

    }

    private AccountFlowResponse130 buildAccountFlowResponse(AccountFlowOutModel outModel) {
        AccountFlowResponse130 response = new AccountFlowResponse130();
        response.setTypename(outModel.getTypename());
        response.setFlowDate(String.valueOf(outModel.getFlowDate().getTime()));
        if (outModel.getAmount() != null) {
            response.setAmount(outModel.getAmount().toPlainString());
        } else {
            response.setAmount("0");
        }
        response.setFlowDetail(outModel.getFlowDetail());
        if (outModel.getFreezeAmount() != null && outModel.getFreezeAmount().compareTo(BigDecimal.ZERO) != 0) {
            response.setAmount(outModel.getFreezeAmount().stripTrailingZeros().toPlainString());
        }
        if (outModel.getFreezeAmount() != null) {
            response.setFreezeAmount(outModel.getFreezeAmount().toPlainString());
        } else {
            response.setFreezeAmount("0");
        }
        response.setTypeName(response.getTypename());
        return response;
    }

    private AccountFlowResponse130 buildAccountFlowResponse130(AccountFlowOutModel130 outModel) {
        AccountFlowResponse130 response = new AccountFlowResponse130();
        response.setTypename(outModel.getTypename());
        response.setFlowDate(String.valueOf(outModel.getFlowDate().getTime()));
        response.setAmount(outModel.getAmount());
        response.setFlowDetail(outModel.getFlowDetail());
        String freezeAmount = outModel.getFreezeAmount() == null ? "0" : outModel.getFreezeAmount();
        if (StringUtils.isNotEmpty(freezeAmount) && !"0".equals(freezeAmount)) {
            response.setAmount(freezeAmount);
        }
        response.setFreezeAmount(outModel.getFreezeAmount() == null ? "0" : outModel.getFreezeAmount());
        response.setTypeName(response.getTypename());
        return response;
    }


    private AccountFlowInModel buildAccountFlowInModelForDetail(AccountFlowAppRequest request) {
        AccountFlowInModel inModel = new AccountFlowInModel();
        inModel.setAccountFlowId(request.getAccountFlowId());
        return inModel;
    }

    /**
     * 构建返回值
     *
     * @param list
     * @return
     */
    private List<AccountFlowAppResponse> buildAccountFlowAppResponseTzListOld(List<AccountFlowOutModel> list) {
        List<AccountFlowAppResponse> responses = new ArrayList<>();
        for (AccountFlowOutModel outModel : list) {
            AccountFlowAppResponse flowResponse = new AccountFlowAppResponse();
            flowResponse.setAccountFlowId(String.valueOf(outModel.getAccountFlowId()));
            flowResponse.setTypeName(outModel.getTypename());
            flowResponse.setFlowDate(DateUtils.formatDate(outModel.getFlowDate(), DateUtils.DISPLAY_FORMAT));
            flowResponse.setAmount(outModel.getAmount() == null ? "0" : outModel.getAmount().stripTrailingZeros().toPlainString());
            BigDecimal freezeAmount = outModel.getFreezeAmount();
            if (freezeAmount != null) {
                flowResponse.setFreezeAmount(freezeAmount.stripTrailingZeros().toPlainString());
            } else {
                flowResponse.setFreezeAmount("0");
            }

            responses.add(flowResponse);
        }
        return responses;
    }

    private List<AccountFlowAppResponse> buildAccountFlowAppResponseListOld(MongoCursor<Document> list) {
        List<AccountFlowAppResponse> responses = new ArrayList<>();
        if (list != null) {
            while (list.hasNext()) {
                AccountFlowAppResponse flowResponse = new AccountFlowAppResponse();
                Document document = list.next();
                System.out.println(">>>>>>kg流水数据>>>>>>>" + JsonUtil.writeValueAsString(document));

                String accountFlowId = document.get("account_flow_id") == null ? "" : document.get("account_flow_id").toString();
                flowResponse.setAccountFlowId(accountFlowId);

                Integer typeName = document.get("business_type_id") == null ? null : document.getInteger("business_type_id");
                if (typeName != null) {
                    flowResponse.setTypeName(BusinessTypeEnum.getBusinessTypeEnum(typeName).getDisplay());
                }

                Long flowDateTimeStamp = document.get("flow_date") == null ? null : document.getLong("flow_date");
                Date date = new Date();
                date.setTime(flowDateTimeStamp);

                flowResponse.setFlowDate(DateUtils.formatDate(date, DateUtils.DISPLAY_FORMAT));

                String amount = document.get("amount") == null ? "0" : ((Decimal128) document.get("amount")).bigDecimalValue().stripTrailingZeros().toPlainString();
                flowResponse.setAmount(amount);

                String freezeAmount = document.get("freeze_amount") == null ? "0" : ((Decimal128) document.get("freeze_amount")).bigDecimalValue().stripTrailingZeros().toPlainString();
                flowResponse.setFreezeAmount(freezeAmount);
                responses.add(flowResponse);
            }
        }
        return responses;
    }


    /**
     * 构建返回值
     *
     * @param list
     * @return
     */
    private List<AccountFlowAppNewResponse> buildAccountFlowAppResponseList(List<AccountFlowOutModel> list) {
        List<AccountFlowAppNewResponse> responses = new ArrayList<>();
        for (AccountFlowOutModel outModel : list) {
            AccountFlowAppNewResponse flowResponse = new AccountFlowAppNewResponse();
            flowResponse.setAccountFlowId(String.valueOf(outModel.getAccountFlowId()));
            flowResponse.setTypeName(outModel.getTypename());
            flowResponse.setFlowDate(String.valueOf(outModel.getFlowDate().getTime()));
            flowResponse.setAmount(outModel.getAmount() == null ? "0" : outModel.getAmount().stripTrailingZeros().toPlainString());
            BigDecimal freezeAmount = outModel.getFreezeAmount();
            if (freezeAmount != null && freezeAmount.compareTo(BigDecimal.ZERO) != 0) {
                flowResponse.setAmount(freezeAmount.stripTrailingZeros().toPlainString());
                flowResponse.setFreezeState(1);
            } else {
                flowResponse.setFreezeState(0);
            }

            responses.add(flowResponse);
        }
        return responses;
    }


    /**
     * 构建流水入参
     *
     * @param request
     * @param page
     * @return
     */
    private AccountFlowInModel buildAccountFlowNewInModel(AccountFlowAppRequest request,
                                                          PageModel<AccountFlowAppNewResponse> page) {
        return buildAccountFlowModel(request, page.getCurrentPage(), page.getPageSize());
    }

    /**
     * 构建流水入参
     *
     * @param request
     * @param page
     * @return
     */
    private AccountFlowInModel buildAccountFlowInModel(AccountFlowAppRequest request,
                                                       PageModel<AccountFlowAppResponse> page) {
        return buildAccountFlowModel(request, page.getCurrentPage(), page.getPageSize());
    }

    private AccountFlowInModel buildAccountFlowModel(AccountFlowAppRequest request, int currentPage, int pageSize) {

        AccountFlowInModel inModel = new AccountFlowInModel();
        inModel.setStart((currentPage - 1) * pageSize);
        inModel.setLimit(pageSize);
        inModel.setUserId(request.getUserId());
        String startDate = request.getStartDate();
        String endDate = request.getEndDate();
        int classify = request.getClassify();
        AccountClassifyEnum type = AccountClassifyEnum.getByStatus(classify);
        if (type != null) {
            switch (type) {
                case REWARD:
                    inModel.setType(String.valueOf(AccountClassifyEnum.REWARD.getStatus()));
                    break;
                case EXCEPTIONAL:
                    inModel.setType(String.valueOf(AccountClassifyEnum.EXCEPTIONAL.getStatus()));
                    break;
                case INTO:
                    inModel.setType(String.valueOf(AccountClassifyEnum.INTO.getStatus()));
                    break;
                case OUT:
                    inModel.setType(String.valueOf(AccountClassifyEnum.OUT.getStatus()));
                    break;
                case TRIBUTE:
                    inModel.setType(String.valueOf(AccountClassifyEnum.TRIBUTE.getStatus()));
                    break;
                default:
            }
        }
        if (StringUtils.isNotEmpty(startDate)) {
            inModel.setStartTime(request.getStartDate() + " 00:00:00");
        }
        if (StringUtils.isNotEmpty(endDate)) {
            inModel.setEndTime(request.getEndDate() + " 23:59:59");
        }
        return inModel;
    }


    /**
     * 构建返回参数
     *
     * @param inviteCount
     * @return
     */
    private UserBaseInfoResponse buildUserBaseInfoResponse(String userName, String avatar, String resume, Long userId,
                                                           Long fansCount, String userMobile, int newFansCount, Integer userRole,
                                                           long inviteCount) {
        UserBaseInfoResponse response = new UserBaseInfoResponse();
        response.setInmagelink(avatar);
        response.setResume(resume);
        response.setUserId(String.valueOf(userId));
        response.setUserName(userName);
        response.setUserMobile(userMobile);
        response.setFansNew(newFansCount);
        response.setInviteCount(inviteCount);
        response.setFansCount(fansCount);
        response.setUserRole(userRole);
        response = buildTags(response);
        return response;

    }

    private UserBaseInfoResponse buildTags(UserBaseInfoResponse response) {
        Long userId = Long.valueOf(response.getUserId());
        UserTagBuild userTag = userTagsUtil.buildTags(userId);
        response.setRealAuthedTag(userTag.getRealAuthedTag());
        response.setIdentityTag(userTag.getIdentityTag());
        response.setVipTag(userTag.getVipTag());
        return response;
    }


    /**
     * 根据用户ID获取用户余额信息
     *
     * @param userId
     * @return
     */
    private AccountOutModel getBalaceInfo(Long userId) {
        if (userId == null || userId <= 0) {
            return null;
        }
        AccountInModel inModel = new AccountInModel();
        inModel.setUserId(userId);
        return accountRMapper.selectByUserbalance(inModel);
    }

    /**
     * 初始化账户
     *
     * @param
     */
    private void initAccount(Long userId) {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setUserId(userId);
        Result<AccountResponse> result = accountService.selectOutUserId(accountRequest);
        if (result.getData() == null) {
            AccountInModel accountInModel = new AccountInModel();
            accountInModel.setUserId(userId);
            userAccountService.init(accountInModel);
            logger.info("initAccount -> 账户初始化成功 -> userId:" + userId);
        }
    }
}
