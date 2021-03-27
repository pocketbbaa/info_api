package com.kg.platform.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;

import com.kg.platform.common.mongo.MongoTables;
import com.kg.platform.common.mongo.Seach;
import com.kg.platform.common.mq.MQConfig;
import com.kg.platform.common.utils.*;
import com.kg.platform.dao.entity.*;
import com.kg.platform.dao.read.*;
import com.kg.platform.dao.write.*;
import com.kg.platform.enumeration.CoinEnum;
import com.kg.platform.model.mongoTable.AccountFlowKgMongo;
import com.kg.platform.service.PushService;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;
import com.kg.framework.toolkit.Check;

import com.kg.platform.common.context.Result;
import com.kg.platform.common.exception.ApiMessageException;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.lock.Lock;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.mq.MQProduct;
import com.kg.platform.enumeration.BusinessTypeEnum;
import com.kg.platform.enumeration.FlowStatusEnum;
import com.kg.platform.enumeration.LoginTypeEnum;
import com.kg.platform.model.in.AccountFlowInModel;
import com.kg.platform.model.in.AccountInModel;
import com.kg.platform.model.in.AccountWithdrawFlowInModel;
import com.kg.platform.model.in.ArticleInModel;
import com.kg.platform.model.out.AccountFlowOutModel;
import com.kg.platform.model.out.AccountOutModel;
import com.kg.platform.model.out.AccountWithdrawFlowOutModel;
import com.kg.platform.model.out.ArticleOutModel;
import com.kg.platform.model.out.KgInfoSwitchOutModel;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.AccountRequest;
import com.kg.platform.model.request.UserCollectRequest;
import com.kg.platform.model.response.AccountFlowResponse;
import com.kg.platform.model.response.AccountResponse;
import com.kg.platform.service.AccountService;
import com.kg.platform.service.TokenManager;
import com.kg.platform.service.UsercollectService;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private static final String USER_ACCOUNT_LOCK_KEY = "kguser::AccountService::updateAddUbalance::";

    @Inject
    AccountWMapper accountWMapper;

    @Inject
    AccountRMapper accountRMapper;

    @Inject
    UserRMapper userRMapper;

    @Inject
    IDGen idGenerater;

    @Inject
    AccountFlowWMapper accountFlowWMapper;

    @Inject
    ArticleRMapper articleRMapper;

    @Inject
    AccountFlowRMapper accountFlowRMapper;

    @Inject
    AccountWithdrawFlowRMapper accountWithdrawFlowRMapper;

    @Inject
    UsercollectService usercollectService;

    @Inject
    MQProduct mqProduct;

    @Inject
    TokenManager manager;

    @Autowired
    KgInfoSwitchRMapper kgInfoSwitchRMapper;

    @Autowired
    UserRelationRMapper userRelationRMapper;

    @Resource(name = "propertyLoader")
    protected PropertyLoader propertyLoader;

    @Autowired
    private BalanceDeductedWMapper balanceDeductedWMapper;

    @Autowired
    private PushService pushService;

    /**
     * 打赏文章，文章作者账号+金额
     */
    @Override
    public boolean updateAddUbalance(AccountRequest request) {
        // logger.info("打赏文章，文章作者账号+金额：{}", JSON.toJSONString(request));
        boolean success;
        Lock lock = new Lock();

        String key = USER_ACCOUNT_LOCK_KEY + request.getUserId() + request.getArticleId();
        try {
            lock.lock(key);
            ArticleInModel ainModel = new ArticleInModel();
            ainModel.setArticleId(request.getArticleId());
            ArticleOutModel articleOutModel = articleRMapper.selectByIdDetails(ainModel);

            // 查文章作者
            UserkgOutModel umodel = userRMapper.getUserDetails(articleOutModel.getCreateUser());
            if (null == umodel) {
                return false;
            }
            // 查当前用户
            UserkgOutModel userkgModel = userRMapper.getUserDetails(request.getUserId());
            AccountFlowInModel accountFlowInModel = new AccountFlowInModel();
            long flowid = idGenerater.nextId();
            accountFlowInModel.setAccountFlowId(flowid);
            accountFlowInModel.setRelationFlowId(flowid);
            accountFlowInModel.setUserId(Long.parseLong(umodel.getUserId()));
            accountFlowInModel.setUserPhone(umodel.getUserMobile());
            accountFlowInModel.setUserEmail(umodel.getUserEmail());
            accountFlowInModel.setAmount(request.getBalance());
            accountFlowInModel.setAccountAmount(request.getBalance());
            accountFlowInModel.setFlowDate(new Date());
            accountFlowInModel.setFlowDetail(
                    userkgModel.getUserName() + "打赏了我的文章" + "《" + articleOutModel.getArticleTitle() + "》");
            accountFlowInModel.setBusinessTypeId(BusinessTypeEnum.TIPSIN.getStatus());
            accountFlowInModel.setFlowStatus(FlowStatusEnum.NOSTATUS.getStatus());
            accountFlowInModel.setArticleId(request.getArticleId());
            accountFlowWMapper.insertFlowSelective(accountFlowInModel);

            logger.info("用户打赏作者，作者账号金额增加接口：AccountRequest={}", JSON.toJSONString(accountFlowInModel));

            AccountInModel inModel = new AccountInModel();
            inModel.setArticleId(request.getArticleId());
            inModel.setBalance(request.getBalance());
            // 加锁

            success = accountWMapper.updateAddUbalance(inModel) > 0;

            success = this.updateReductionUbalance(request, flowid);

            // 推送消息APP 入库
            // 获取被审核的用户TOKEN，用于推送手机APP
            String token = manager.getTokenByUserId(Long.valueOf(umodel.getUserId()));
            String messageText = "您的《" + articleOutModel.getArticleTitle() + "》收到文章打赏" + request.getBalance() + "TV";



            pushService.updateAddUbalance(umodel.getUserId(),"系统通知",messageText,"articleReward",1);

        } finally {
            lock.unLock();
        }
        return success;
    }

    /**
     * 打赏文章，用户账号-金额
     */
    @Override
    public boolean updateReductionUbalance(AccountRequest request, long flowid) {

        ArticleInModel ainModel = new ArticleInModel();
        ainModel.setArticleId(request.getArticleId());
        ArticleOutModel articleOutModel = articleRMapper.selectByIdDetails(ainModel);
        // 查用户
        UserkgOutModel umodel = userRMapper.getUserDetails(request.getUserId());
        // 查文章作者
        UserkgOutModel outModel = userRMapper.getUserDetails(articleOutModel.getCreateUser());
        AccountFlowInModel accountFlowInModel = new AccountFlowInModel();
        long aFlowid = idGenerater.nextId();
        accountFlowInModel.setAccountFlowId(aFlowid);
        accountFlowInModel.setRelationFlowId(flowid);
        accountFlowInModel.setUserId(Long.parseLong(umodel.getUserId()));
        accountFlowInModel.setUserPhone(umodel.getUserMobile());
        accountFlowInModel.setUserEmail(umodel.getUserEmail());
        accountFlowInModel.setAmount(request.getBalance().multiply(new BigDecimal(-1)));
        accountFlowInModel.setAccountAmount(request.getBalance().multiply(new BigDecimal(-1)));
        accountFlowInModel.setFlowDate(new Date());
        accountFlowInModel.setFlowDetail(
                "我打赏了" + outModel.getUserName() + "的文章" + "《" + articleOutModel.getArticleTitle() + "》");
        accountFlowInModel.setBusinessTypeId(BusinessTypeEnum.TIPSOUT.getStatus());
        accountFlowInModel.setFlowStatus(FlowStatusEnum.NOSTATUS.getStatus());
        accountFlowInModel.setArticleId(request.getArticleId());
        accountFlowWMapper.insertFlowSelective(accountFlowInModel);

        logger.info("用户打赏作者，当前用户账号金额减少接口：AccountRequest={}", JSON.toJSONString(accountFlowInModel));

        AccountInModel inModel = new AccountInModel();
        inModel.setUserId(request.getUserId());
        inModel.setBalance(request.getBalance());

        accountWMapper.updateReductionUbalance(inModel);

        return true;
    }

    @Override
    public boolean loginBonus(Long userId, Integer loginType) {
        logger.info("获取登录奖励：{}", JSON.toJSONString(userId));
        boolean success;
        Lock lock = new Lock();
        // Long userId = request.getUserId();

        String key = USER_ACCOUNT_LOCK_KEY + "loginBonus" + userId;

        final String mloginBonusTXB = propertyLoader.getProperty("common", "global.mloginBonusTXB");
        final String mloginBonusSTATUS = propertyLoader.getProperty("common", "global.mloginBonusSTATUS");
        final String wloginBonusTXB = propertyLoader.getProperty("common", "global.wloginBonusTXB");
        final String wloginBonusSTATUS = propertyLoader.getProperty("common", "global.wloginBonusSTATUS");
        BigDecimal loginBonus = BigDecimal.ZERO;
        int loginBonusStatus = 0;
        if (loginType == LoginTypeEnum.MOBILE_LOGIN.getStatus()) {
            loginBonus = new BigDecimal(mloginBonusTXB);
            loginBonusStatus = Integer.parseInt(mloginBonusSTATUS);
        } else if (loginType == LoginTypeEnum.USERPASSWORD_LOGIN.getStatus()) {
            loginBonus = new BigDecimal(wloginBonusTXB);
            loginBonusStatus = Integer.parseInt(wloginBonusSTATUS);
        }

        try {
            lock.lock(key);
            // 查当前用户
            UserkgOutModel userkgModel = userRMapper.getUserDetails(userId);
            if (userId == null || userkgModel == null) {
                logger.error("获取登录奖励收到的用户id {}", userId);
                throw new ApiMessageException(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                        ExceptionEnum.PARAMEMPTYERROR.getMessage());
            }

//            AccountFlowInModel accountFlowInModel = new AccountFlowInModel();
//            accountFlowInModel.setUserId(userId);
//            accountFlowInModel.setBusinessTypeId(BusinessTypeEnum.LOGINBONUS.getStatus());
            //List<AccountFlowOutModel> flowOutModel = accountFlowTxbRMapper.selectUserTypeFlow(accountFlowInModel);
            BasicDBObject querry = new BasicDBObject();
            querry.append("business_type_id", new BasicDBObject(Seach.EQ.getOperStr(), BusinessTypeEnum.LOGINBONUS.getStatus()));
            querry.append("user_id", new BasicDBObject(Seach.EQ.getOperStr(), userId));
            MongoCursor<Document> s = MongoUtils.findByFilter(MongoTables.ACCOUNT_FLOW_KG, querry);
            // 領取過就不能再領
            if ((loginBonusStatus == 0 || loginBonus.compareTo(BigDecimal.ZERO) == 0)
                    || s.hasNext()) {
                return false;
            }

            AccountFlowKgMongo accountFlowKgMongo = new AccountFlowKgMongo();

            long flowid = idGenerater.nextId();
            accountFlowKgMongo.setAccount_flow_id(flowid);
            accountFlowKgMongo.setRelation_flow_id(flowid);
            accountFlowKgMongo.setUser_id(userId);
            accountFlowKgMongo.setUser_phone(userkgModel.getUserMobile());
            accountFlowKgMongo.setUser_email(userkgModel.getUserEmail());
            accountFlowKgMongo.setUser_name(userkgModel.getUserName());
            accountFlowKgMongo.setAmount(loginBonus);
            accountFlowKgMongo.setAccount_amount(loginBonus);
            accountFlowKgMongo.setFlow_date(new Date().getTime());
            accountFlowKgMongo.setFlow_detail("获取登录奖励");
            accountFlowKgMongo.setBusiness_type_id(BusinessTypeEnum.LOGINBONUS.getStatus());
            accountFlowKgMongo.setFlow_status(FlowStatusEnum.NOSTATUS.getStatus());
            MongoUtils.insertOne(MongoTables.ACCOUNT_FLOW_KG, new Document(Bean2MapUtil.bean2map(accountFlowKgMongo)));


            AccountInModel inModel = new AccountInModel();
            inModel.setUserId(userId);
            inModel.setBalance(loginBonus);
            success = accountWMapper.addTxbBalance(inModel) > 0;

            accountFlowKgMongo = new AccountFlowKgMongo();
            accountFlowKgMongo.setAccount_flow_id(idGenerater.nextId());
            accountFlowKgMongo.setRelation_flow_id(flowid);
            accountFlowKgMongo.setUser_id(Constants.PLATFORM_USER_ID);
            accountFlowKgMongo.setAmount(loginBonus.negate());
            accountFlowKgMongo.setAccount_amount(loginBonus.negate());
            accountFlowKgMongo.setFlow_date(new Date().getTime());
            accountFlowKgMongo.setFlow_detail("发放登录奖励");
            accountFlowKgMongo.setBusiness_type_id(BusinessTypeEnum.LOGINBONUS.getStatus());
            accountFlowKgMongo.setFlow_status(FlowStatusEnum.NOSTATUS.getStatus());
            MongoUtils.insertOne(MongoTables.ACCOUNT_FLOW_KG, new Document(Bean2MapUtil.bean2map(accountFlowKgMongo)));

            inModel.setUserId(Constants.PLATFORM_USER_ID);
            success = accountWMapper.reduceTxbBalance(inModel) > 0;

        } finally {
            lock.unLock();
        }
        return success;
    }

    @Override
    public boolean realnameBonus(Long userId) {
        logger.info("获取实名认证奖励：{}", JSON.toJSONString(userId));
        boolean success;
        Lock lock = new Lock();
        // Long userId = request.getUserId();

        String key = USER_ACCOUNT_LOCK_KEY + "realnameBonus" + userId;
        try {
            lock.lock(key);
            // 查当前用户
            UserkgOutModel userkgModel = userRMapper.getUserDetails(userId);
            if (userId == null || userkgModel == null) {
                logger.error("获取实名认证奖励收到的用户id {}", userId);
                throw new ApiMessageException(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                        ExceptionEnum.PARAMEMPTYERROR.getMessage());
            }

            AccountFlowInModel accountFlowInModel = new AccountFlowInModel();
            accountFlowInModel.setUserId(userId);
            accountFlowInModel.setBusinessTypeId(BusinessTypeEnum.REALNAMEBONUS.getStatus());
            List<AccountFlowOutModel> flowOutModel = accountFlowRMapper.selectUserTypeFlow(accountFlowInModel);
            // 領取過就不能再領
            if (null != flowOutModel && flowOutModel.size() > 0) {
                return false;
            }

            AccountFlow accountFlow = new AccountFlow();

            long flowid = idGenerater.nextId();
            accountFlow.setAccountFlowId(flowid);
            accountFlow.setRelationFlowId(flowid);
            accountFlow.setUserId(userId);
            accountFlow.setUserPhone(userkgModel.getUserMobile());
            accountFlow.setUserEmail(userkgModel.getUserEmail());
            accountFlow.setAmount(Constants.REALNAME_BONUS_TV);
            accountFlow.setAccountAmount(Constants.REALNAME_BONUS_TV);
            accountFlow.setFlowDate(new Date());
            accountFlow.setFlowDetail("获取实名认证奖励");
            accountFlow.setBusinessTypeId(BusinessTypeEnum.REALNAMEBONUS.getStatus());
            accountFlow.setFlowStatus(FlowStatusEnum.NOSTATUS.getStatus());
            accountFlowWMapper.insertSelective(accountFlow);

            AccountInModel inModel = new AccountInModel();
            inModel.setUserId(userId);
            inModel.setBalance(Constants.REALNAME_BONUS_TV);
            success = accountWMapper.addUserbalance(inModel) > 0;

            accountFlow = new AccountFlow();
            accountFlow.setAccountFlowId(idGenerater.nextId());
            accountFlow.setRelationFlowId(flowid);
            accountFlow.setUserId(Constants.PLATFORM_USER_ID);
            accountFlow.setAmount(Constants.REALNAME_BONUS_TV.negate());
            accountFlow.setAccountAmount(Constants.REALNAME_BONUS_TV.negate());
            accountFlow.setFlowDate(new Date());
            accountFlow.setFlowDetail("发放实名认证奖励");
            accountFlow.setBusinessTypeId(BusinessTypeEnum.REALNAMEBONUS.getStatus());
            accountFlow.setFlowStatus(FlowStatusEnum.NOSTATUS.getStatus());
            accountFlowWMapper.insertSelective(accountFlow);

            inModel.setUserId(Constants.PLATFORM_USER_ID);
            success = accountWMapper.updateReductionUbalance(inModel) > 0;

        } finally {
            lock.unLock();
        }
        return success;
    }

    @Override
    public boolean userColumnBonus(Long userId) {
        logger.info("获取专栏申请通过奖励：{}", JSON.toJSONString(userId));
        boolean success;
        Lock lock = new Lock();
        // Long userId = request.getUserId();

        String key = USER_ACCOUNT_LOCK_KEY + "userColumnBonus" + userId;
        try {
            lock.lock(key);
            // 查当前用户
            UserkgOutModel userkgModel = userRMapper.getUserDetails(userId);
            if (userId == null || userkgModel == null) {
                logger.error("获取专栏申请通过奖励收到的用户id {}", userId);
                throw new ApiMessageException(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                        ExceptionEnum.PARAMEMPTYERROR.getMessage());
            }

            AccountFlowInModel accountFlowInModel = new AccountFlowInModel();
            accountFlowInModel.setUserId(userId);
            accountFlowInModel.setBusinessTypeId(BusinessTypeEnum.USERCOLUMNBONUS.getStatus());
            List<AccountFlowOutModel> flowOutModel = accountFlowRMapper.selectUserTypeFlow(accountFlowInModel);
            // 領取過就不能再領
            if (null != flowOutModel && flowOutModel.size() > 0) {
                return false;
            }

            AccountFlow accountFlow = new AccountFlow();

            long flowid = idGenerater.nextId();
            accountFlow.setAccountFlowId(flowid);
            accountFlow.setRelationFlowId(flowid);
            accountFlow.setUserId(userId);
            accountFlow.setUserPhone(userkgModel.getUserMobile());
            accountFlow.setUserEmail(userkgModel.getUserEmail());
            accountFlow.setAmount(Constants.USERCOLUMN_BONUS_TV);
            accountFlow.setAccountAmount(Constants.USERCOLUMN_BONUS_TV);
            accountFlow.setFlowDate(new Date());
            accountFlow.setFlowDetail("获取专栏申请奖励");
            accountFlow.setBusinessTypeId(BusinessTypeEnum.USERCOLUMNBONUS.getStatus());
            accountFlow.setFlowStatus(FlowStatusEnum.NOSTATUS.getStatus());
            accountFlowWMapper.insertSelective(accountFlow);

            AccountInModel inModel = new AccountInModel();
            inModel.setUserId(userId);
            inModel.setBalance(Constants.USERCOLUMN_BONUS_TV);
            success = accountWMapper.addUserbalance(inModel) > 0;

            accountFlow = new AccountFlow();
            accountFlow.setAccountFlowId(idGenerater.nextId());
            accountFlow.setRelationFlowId(flowid);
            accountFlow.setUserId(Constants.PLATFORM_USER_ID);
            accountFlow.setAmount(Constants.USERCOLUMN_BONUS_TV.negate());
            accountFlow.setAccountAmount(Constants.USERCOLUMN_BONUS_TV.negate());
            accountFlow.setFlowDate(new Date());
            accountFlow.setFlowDetail("发放专栏申请奖励");
            accountFlow.setBusinessTypeId(BusinessTypeEnum.USERCOLUMNBONUS.getStatus());
            accountFlow.setFlowStatus(FlowStatusEnum.NOSTATUS.getStatus());
            accountFlowWMapper.insertSelective(accountFlow);

            inModel.setUserId(Constants.PLATFORM_USER_ID);
            success = accountWMapper.updateReductionUbalance(inModel) > 0;

        } finally {
            lock.unLock();
        }
        return success;
    }

    public static void main(String[] args) {
        System.out.println(MD5Util.md5Hex(Constants.SALT));
    }

    /**
     * 验证打赏交易密码
     */
    public Result<AccountResponse> validationPwd(AccountRequest request) {
        // logger.info("验证打赏交易密码：AccountRequest={}",
        // JSON.toJSONString(request));
        // 校验账号
        AccountInModel inModel = new AccountInModel();
        inModel.setUserId(request.getUserId());
        inModel.setTxPassword(MD5Util.md5Hex(request.getTxPassword() + Constants.SALT));
        AccountOutModel outModel = accountRMapper.validationPwd(inModel);
        System.out.println("获取到的交易密码:" + JSON.toJSONString(outModel));
        AccountResponse response = new AccountResponse();
        if (outModel != null) {
            response.setAccountId(outModel.getAccountId());
            response.setBalance(outModel.getBalance().toString());
        } else {
            return null;
        }

        return new Result<AccountResponse>(response);

    }

    /**
     * 我的钛值
     */
    @Override
    public Result<AccountResponse> selectByUserbalance(AccountRequest request) {
        logger.info("我的钛值：AccountRequest={}", JSON.toJSONString(request));

        AccountInModel inModel = new AccountInModel();
        inModel.setUserId(request.getUserId());
        // 当前可用额度
        AccountOutModel outModel = accountRMapper.selectByUserbalance(inModel);
        AccountFlowInModel model = new AccountFlowInModel();
        model.setUserId(request.getUserId());
        // 累积
        AccountFlowOutModel clatIveOutModel = accountFlowRMapper.cumulativeIncome(model);
        // 今日所得，今日消耗
        AccountFlowOutModel todayOutModel = accountFlowRMapper.Today(model);
        // 昨日所得
        AccountFlowOutModel beforeOutModel = accountFlowRMapper.BeforeIncome(model);
        AccountResponse response = new AccountResponse();

        if (null != outModel) {
            response.setBalance(NumberUtils.formatBigDecimal(outModel.getBalance()));
            response.setFrozenBalance(NumberUtils.formatBigDecimal(outModel.getFrozenBalance()));
        } else {
            response.setBalance("0.000");
            response.setFrozenBalance("0.000");
        }

        if (null != clatIveOutModel) {
            response.setIncome(NumberUtils.formatBigDecimal(clatIveOutModel.getIncome()));
        } else {
            response.setIncome("0.000");
        }

        if (null != todayOutModel) {
            response.setTodayIncome(NumberUtils.formatBigDecimal(todayOutModel.getTodayIncome()));
            response.setReduce(NumberUtils.formatBigDecimal(todayOutModel.getReduce()));
        } else {
            response.setTodayIncome("0.000");
            response.setReduce("0.000");
        }

        if (null != beforeOutModel) {
            response.setBeforeIncome(NumberUtils.formatBigDecimal(beforeOutModel.getBeforeIncome()));
        } else {
            response.setBeforeIncome("0.000");
        }

        return new Result<AccountResponse>(response);
    }

    @Override
    public Result<AccountResponse> selectUserTxbBalance(AccountRequest request) {
        logger.info("我的氪金：AccountRequest={}", JSON.toJSONString(request));

        AccountInModel inModel = new AccountInModel();
        inModel.setUserId(request.getUserId());
        // 当前可用额度
        AccountOutModel outModel = accountRMapper.selectByUserbalance(inModel);
        AccountFlowInModel model = new AccountFlowInModel();
        model.setUserId(request.getUserId());
        // 累积
        // AccountFlowOutModel clatIveOutModel = accountFlowRMapper.cumulativeIncomeTxb(model);
        BigDecimal total = this.cumulativeIncomeTxb(model);

        // 今日所得，今日消耗
        //AccountFlowOutModel todayOutModel = accountFlowRMapper.TodayTxb(model);
        AccountFlowOutModel todayOutModel = this.todayTxb(model);

        // 昨日所得
        //AccountFlowOutModel beforeOutModel = accountFlowRMapper.BeforeIncomeTxb(model);
        BigDecimal beforeIncome = this.beforeIncomeTxb(model);

        AccountResponse response = new AccountResponse();

        if (null != outModel) {
            response.setBalance(NumberUtils.formatBigDecimal(outModel.getTxbBalance()));
            response.setFrozenBalance(NumberUtils.formatBigDecimal(outModel.getTxbFrozenBalance()));
        } else {
            response.setBalance("0.000");
        }

        if (BigDecimal.ZERO.compareTo(total) < 0) {
            response.setIncome(NumberUtils.formatBigDecimal(total));
        } else {
            response.setIncome("0.000");
        }

        if (null != todayOutModel) {
            response.setTodayIncome(NumberUtils.formatBigDecimal(todayOutModel.getTodayIncome()));
            response.setReduce(NumberUtils.formatBigDecimal(todayOutModel.getReduce()));
        } else {
            response.setReduce("0.000");
            response.setTodayIncome("0.000");
        }

        if (BigDecimal.ZERO.compareTo(beforeIncome) < 0) {
            response.setBeforeIncome(NumberUtils.formatBigDecimal(beforeIncome));
        } else {
            response.setBeforeIncome("0.000");
        }

        return new Result<AccountResponse>(response);
    }

    private BigDecimal beforeIncomeTxb(AccountFlowInModel model) {
        BigDecimal total = BigDecimal.ZERO;
        Date previousDate = DateUtils.returnPreviousDay(new Date());
        model.setStartTime(DateUtils.getDateStart(previousDate).getTime() + "");
        model.setEndTime(DateUtils.getDateEnd(previousDate).getTime() + "");
        List<Bson> querryBy = this.conditionList(model);
        //查询数据
        MongoCursor<Document> cursor = MongoUtils.aggregate(MongoTables.ACCOUNT_FLOW_KG, querryBy);
        if (cursor != null) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                total = document.get("total") == null ? BigDecimal.ZERO : new BigDecimal(document.get("total") + "");
            }
        }
        return total;
    }

    private AccountFlowOutModel todayTxb(AccountFlowInModel model) {
        AccountFlowOutModel accountFlowOutModel = new AccountFlowOutModel();
        logger.info("------获取氪金今日消耗获得累积所得-----");
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal reduce = BigDecimal.ZERO;
        //组装条件
        BasicDBObject querryBy = this.todayTxbQuerryBy(model);
        //查询数据
        MongoCursor<Document> cursor = MongoUtils.findByFilter(MongoTables.ACCOUNT_FLOW_KG, querryBy);
        if (cursor != null) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                BigDecimal todayAmount = document.get("amount") == null ? BigDecimal.ZERO : new BigDecimal(document.get("amount") + "");
                logger.info(">>>>>>>todayTxbTotal>>>>>>>>>" + todayAmount);
                if (BigDecimal.ZERO.compareTo(todayAmount) != 0) {
                    if (BigDecimal.ZERO.compareTo(todayAmount) < 0) {
                        amount = amount.add(todayAmount);
                    } else {
                        reduce = reduce.add(todayAmount);
                    }
                }

            }
        }
        accountFlowOutModel.setTodayIncome(amount);
        accountFlowOutModel.setReduce(reduce);
        return accountFlowOutModel;
    }

    public BasicDBObject todayTxbQuerryBy(AccountFlowInModel model) {
        BasicDBObject querryBy = new BasicDBObject(); // 查询条件
        querryBy.append("user_id", model.getUserId());
        int typeId[] = {1010, 1020};
        querryBy.append("business_type_id", new BasicDBObject(Seach.NIN.getOperStr(), typeId));
        //获取开始时间结束时间
        Date date = new Date();
        Date startDate = DateUtils.getDateStart(date);
        Date endDate = DateUtils.getDateEnd(date);
        querryBy.append("flow_date", BasicDBObjectBuilder.start(Seach.GTE.getOperStr(), startDate.getTime())
                .add(Seach.LTE.getOperStr(), endDate.getTime()).get());
        logger.info("----查询今日消耗、获得条件------" + querryBy.toString());
        return querryBy;
    }


    public List<Bson> conditionList(AccountFlowInModel model) {
        List<Bson> conditionList = new ArrayList<>();
        BasicDBObject querryBy = new BasicDBObject(); // 查询条件
        querryBy.append("user_id", model.getUserId());
        querryBy.append("amount", new BasicDBObject(Seach.GT.getOperStr(), 0));
        int typeId[] = {1010, 1020};
        querryBy.append("business_type_id", new BasicDBObject(Seach.NIN.getOperStr(), typeId));
        if (StringUtils.isNotEmpty(model.getStartTime()) && StringUtils.isNotEmpty(model.getEndTime())) {
            querryBy.put("flow_date", BasicDBObjectBuilder.start(Seach.GTE.getOperStr(), Long.valueOf(model.getStartTime()))
                    .add(Seach.LTE.getOperStr(), Long.valueOf(model.getEndTime())).get());
        }
        Bson querryCondition = new BasicDBObject("$match", querryBy);
        conditionList.add(querryCondition);

        BasicDBObject groupBy = new BasicDBObject();
        groupBy.append("_id", "$user_id");
        groupBy.append("total", new BasicDBObject("$sum", "$amount"));
        Bson group = new BasicDBObject("$group", groupBy);
        conditionList.add(group);
        logger.info("--------conditionList--------" + conditionList.toString());
        return conditionList;
    }


    private BigDecimal cumulativeIncomeTxb(AccountFlowInModel model) {
        logger.info("------获取氪金奖励累积所得-----");
        BigDecimal amount = BigDecimal.ZERO;
        List<Bson> conditions = this.conditionList(model);
        logger.info("----查询cumulativeIncomeTxb条件------" + conditions.toString());
        MongoCursor<Document> cursor = MongoUtils.aggregate(MongoTables.ACCOUNT_FLOW_KG, conditions);
        if (cursor != null) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                amount = document.get("total") == null ? BigDecimal.ZERO : new BigDecimal(document.get("total").toString());
            }
        }
        return amount;
    }

    @Override
    public Result<AccountResponse> selectUserRITBalance(AccountRequest request) {
        logger.info("selectUserRITBalance -> params:" + JSONObject.toJSONString(request));
        AccountResponse response = initBalance();
        response = buildUserbalance(request, response);
        AccountFlowInModel model = buildAccountFlowInModel(request);
        response = buildCumulativeIncomeRit(response, model);
        response = buildTodayRit(response, model);
        response = buildBeforeIncomeRit(response, model);
        logger.info("selectUserRITBalance -> response:" + JSONObject.toJSONString(response));
        return new Result<>(response);
    }


    public List<Bson> getQuerryRitBy(AccountFlowInModel model) {
        //查询条件
        List<Bson> condition = new ArrayList<>();
        BasicDBObject querryBy = new BasicDBObject();
        if (StringUtils.isNotEmpty(model.getStartTime()) && StringUtils.isNotEmpty(model.getEndTime())) {
            querryBy.put("flow_date", BasicDBObjectBuilder.start(Seach.GTE.getOperStr(), Long.valueOf(model.getStartTime()))
                    .add(Seach.LTE.getOperStr(), Long.valueOf(model.getEndTime())).get());
        }
        querryBy.append("user_id", model.getUserId());
        querryBy.append("amount", new BasicDBObject(Seach.GT.getOperStr(), 0));
        BasicDBObject querry = new BasicDBObject("$match", querryBy);
        condition.add(querry);

        //分组条件
        BasicDBObject groupBy = new BasicDBObject();
        groupBy.append("_id", "$user_id");
        groupBy.append("total", new BasicDBObject("$sum", "$amount"));
        Bson group = new BasicDBObject("$group", groupBy);
        condition.add(group);
        return condition;
    }

    /**
     * 昨日所得
     *
     * @param response
     * @param model
     * @return
     */
//    private AccountResponse buildBeforeIncomeRit(AccountResponse response, AccountFlowInModel model) {
//        AccountFlowOutModel beforeOutModel = accountFlowRitRMapper.BeforeIncomeRit(model);
//        if (beforeOutModel != null) {
//            response.setBeforeIncome(NumberUtils.formatBigDecimal(beforeOutModel.getBeforeIncome()));
//        }
//        return response;
//    }
    private AccountResponse buildBeforeIncomeRit(AccountResponse response, AccountFlowInModel model) {
        //获取昨日开始时间、结束时间
        Date previousDate = DateUtils.returnPreviousDay(new Date());
        model.setStartTime(DateUtils.getDateStart(previousDate).getTime() + "");
        model.setEndTime(DateUtils.getDateEnd(previousDate).getTime() + "");
        List<Bson> searchInfo = this.getQuerryRitBy(model);
        logger.info("-------rit昨日所得查询条件---------" + searchInfo.toString());
        MongoCursor<Document> cursor = MongoUtils.aggregate(MongoTables.ACCOUNT_FLOW_RIT, searchInfo);
        BigDecimal beforeIncome = BigDecimal.ZERO;
        if (cursor != null) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                beforeIncome = document.get("total") == null ? BigDecimal.ZERO : new BigDecimal(document.get("total").toString());
            }
        }
        response.setBeforeIncome(NumberUtils.formatBigDecimal(beforeIncome));
        return response;
    }

    /**
     * 今日所得，今日消耗
     *
     * @param response
     * @param model
     * @return
     */
    //    private AccountResponse buildTodayRit(AccountResponse response, AccountFlowInModel model) {
//        AccountFlowOutModel todayOutModel = accountFlowRitRMapper.TodayRit(model);
//        if (todayOutModel != null) {
//            response.setTodayIncome(NumberUtils.formatBigDecimal(todayOutModel.getTodayIncome()));
//            response.setReduce(NumberUtils.formatBigDecimal(todayOutModel.getReduce()));
//        }
//        return response;
//    }
    private AccountResponse buildTodayRit(AccountResponse response, AccountFlowInModel model) {
        logger.info("------获取rit今日消耗获得累积所得-----");
        BigDecimal todayIncome = BigDecimal.ZERO;
        BigDecimal reduce = BigDecimal.ZERO;
        //组装条件
        BasicDBObject querryBy = this.getQuerryTodayRitBy(model);
        //查询数据
        MongoCursor<Document> cursor = MongoUtils.findByFilter(MongoTables.ACCOUNT_FLOW_RIT, querryBy);
        if (cursor != null) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                BigDecimal amount = document.get("amount") == null ? BigDecimal.ZERO : new BigDecimal(document.get("amount") + "");
                logger.info(">>>>>>>todayRitTotal>>>>>>>>>" + amount);
                if (BigDecimal.ZERO.compareTo(amount) != 0) {
                    if (BigDecimal.ZERO.compareTo(amount) < 0) {
                        todayIncome = amount.add(amount);
                    } else {
                        reduce = reduce.add(amount);
                    }
                }
            }
        }
        response.setTodayIncome(NumberUtils.formatBigDecimal(todayIncome));
        response.setReduce(NumberUtils.formatBigDecimal(reduce));
        return response;
    }

    public BasicDBObject getQuerryTodayRitBy(AccountFlowInModel model) {
        //查询条件
        BasicDBObject querryBy = new BasicDBObject();
        if (StringUtils.isNotEmpty(model.getStartTime()) && StringUtils.isNotEmpty(model.getEndTime())) {
            querryBy.put("flow_date", BasicDBObjectBuilder.start(Seach.GTE.getOperStr(), Long.valueOf(model.getStartTime()))
                    .add(Seach.LTE.getOperStr(), Long.valueOf(model.getEndTime())).get());
        }
        querryBy.append("user_id", model.getUserId());
        return querryBy;
    }

    /**
     * 累积余额
     *
     * @param response
     * @return
     */
//    private AccountResponse buildCumulativeIncomeRit(AccountResponse response, AccountFlowInModel model) {
//        AccountFlowOutModel clatIveOutModel = accountFlowRitRMapper.cumulativeIncomeRit(model);
//        if (clatIveOutModel != null) {
//            response.setIncome(NumberUtils.formatBigDecimal(clatIveOutModel.getIncome()));
//        }
//        return response;
//    }
    private AccountResponse buildCumulativeIncomeRit(AccountResponse response, AccountFlowInModel model) {
        logger.info("------获取rit奖励累积所得-----");
        BigDecimal amount = BigDecimal.ZERO;
        List<Bson> conditions = this.getQuerryRitBy(model);
        logger.info("----查询rit累积所得条件------" + conditions.toString());
        MongoCursor<Document> cursor = MongoUtils.aggregate(MongoTables.ACCOUNT_FLOW_KG, conditions);
        if (cursor != null) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                amount = document.get("total") == null ? BigDecimal.ZERO : new BigDecimal(document.get("total").toString());
            }
        }
        response.setIncome(NumberUtils.formatBigDecimal(amount));
        return response;
    }

    private AccountFlowInModel buildAccountFlowInModel(AccountRequest request) {
        AccountFlowInModel model = new AccountFlowInModel();
        model.setUserId(request.getUserId());
        return model;
    }


    /**
     * 当前可用额度
     *
     * @param request
     * @return
     */
    private AccountResponse buildUserbalance(AccountRequest request, AccountResponse response) {
        AccountInModel inModel = new AccountInModel();
        inModel.setUserId(request.getUserId());
        AccountOutModel outModel = accountRMapper.selectByUserbalance(inModel);
        if (outModel != null) {
            response.setBalance(NumberUtils.formatBigDecimal(outModel.getRitBalance()));
            response.setFrozenBalance(NumberUtils.formatBigDecimal(outModel.getRitFrozenBalance()));
        }
        return response;
    }

    private AccountResponse initBalance() {
        AccountResponse response = new AccountResponse();
        response.setBalance("0.000");
        response.setFrozenBalance("0.000");
        response.setTodayIncome("0.000");
        response.setReduce("0.000");
        response.setIncome("0.000");
        response.setBeforeIncome("0.000");
        return response;
    }

    /**
     * 提现查询查最大最小限制数量
     *
     * @param request
     * @return
     */
    public Result<AccountFlowResponse> getMaxMinTimes(AccountRequest request) {
        logger.info(" 提现查询查最大最小限制数量入参：AccountRequest={}", JSON.toJSONString(request));
        AccountFlowInModel model = new AccountFlowInModel();
        model.setUserId(request.getUserId());
        model.setAuthAction("6");
        // 查最大最小限制数
        AccountFlowOutModel timesModel = accountFlowRMapper.getMaxMinTimes(model);
        AccountFlowResponse response = new AccountFlowResponse();
        response.setActionTimes(timesModel.getActionTimes());
        response.setActionMinTimes(timesModel.getActionMinTimes());

        return new Result<AccountFlowResponse>(response);
    }

    /**
     * 查询在审核中的提现数据
     */
    public Result<AccountFlowResponse> auditAmount(AccountRequest request) {
        logger.info("查询在审核中的提现数据入参：AccountRequest={}", JSON.toJSONString(request));
        AccountFlowInModel model = new AccountFlowInModel();
        model.setUserId(request.getUserId());
        model.setAuthAction("6");
        // 查申请中的提现申请
        AccountWithdrawFlowInModel withdrawFlowInModel = new AccountWithdrawFlowInModel();
        withdrawFlowInModel.setUserId(request.getUserId());
        AccountWithdrawFlowOutModel outModel = accountWithdrawFlowRMapper.getauditAmount(withdrawFlowInModel);
        // 查最大最小限制数
        AccountFlowOutModel timesModel = accountFlowRMapper.getMaxMinTimes(model);
        AccountInModel inModel = new AccountInModel();
        inModel.setUserId(request.getUserId());
        UserkgOutModel useroutModel = userRMapper.getUserDetails(request.getUserId());
        // 查余额
        AccountOutModel balanceModel = accountRMapper.selectByUserbalance(inModel);
        AccountFlowResponse response = new AccountFlowResponse();
        // 存入提现申请数据
        if (outModel != null) {
            response.setFlowDate(DateUtils.formatDate(outModel.getWithdrawTime(), DateUtils.DISPLAY_FORMAT));
            response.setTxAddress(outModel.getToAddress());
            response.setAmount(outModel.getWithdrawAmount());
            response.setAccountAmount(outModel.getAccountAmount());
            response.setPoundageAmount(outModel.getPoundageAmount());
            response.setFlowStatus(Integer.parseInt(outModel.getStatus().toString()));
            response.setWithdrawFlowId(outModel.getWithdrawFlowId().toString());
        }

        response.setActionTimes(timesModel.getActionTimes());
        response.setActionMinTimes(timesModel.getActionMinTimes());
        response.setBalance(balanceModel.getBalance());
        response.setMobileArea(useroutModel.getMobileArea());
        response.setUserMobile(useroutModel.getUserMobile());

        return new Result<AccountFlowResponse>(response);
    }

    /**
     * 文章奖励发放 app1.1.0屏蔽阅读奖励
     */
    @Override
    public BigDecimal updateUserbalance(AccountRequest request) {

        if (null == request.getUserId()) {
            throw new ApiMessageException("参数错误");
        }
        // 查用户
        UserkgOutModel umodel = userRMapper.getUserDetails(request.getUserId());
        if (null == umodel) {
            throw new ApiMessageException("用户不存在");
        }

        if (null != request.getSource()) {
            UserCollectRequest req = new UserCollectRequest();
            req.setArticleId(request.getArticleId());
            req.setUserId(request.getUserId());
            req.setOperType(3);
            usercollectService.insertSelective(req);
        }

        return BigDecimal.ZERO;
    }

    /**
     * 查询是否有账号
     *
     * @param request
     * @return
     */
    @Override
    public Result<AccountResponse> selectOutUserId(AccountRequest request) {
        logger.info("查询是否有账号入参：AccountRequest={}", JSON.toJSONString(request));
        AccountInModel inModel = new AccountInModel();
        inModel.setUserId(request.getUserId());
        AccountOutModel outModel = accountRMapper.selectOutUserId(inModel);
        if (outModel == null) {
            return new Result<AccountResponse>();
        } else {
            AccountResponse response = new AccountResponse();
            response.setAccountId(outModel.getAccountId());
            return new Result<AccountResponse>(response);
        }
    }

    @Override
    public Boolean checkTvAmount(Long userId) {
        Boolean isOk = true;
        AccountInModel inModel = new AccountInModel();
        inModel.setUserId(userId);
        AccountOutModel outModel = accountRMapper.selectOutUserId(inModel);
        isOk = outModel.getBalance().compareTo(new BigDecimal(100)) >= 0;
        return isOk;
    }

    @Override
    public boolean validPlatformBalance(AccountInModel accountInModel) {
        return accountRMapper.validatePlatformBanlace(accountInModel) > 0;
    }

    /**
     * 分享钛值文章，用户账号+金额 平台-金额
     */
    @Override
    public boolean shareBonus(AccountRequest request) {

        Boolean isok = true;

        Long userId = request.getUserId();
        // 查询是否有师傅
        UserRelation userRelation = userRelationRMapper.selectParentUser(request.getUserId());

        // 发放用户钛值奖励
        long flowid = idGenerater.nextId();
        request.setFlowId(flowid);
        request.setBonusType("1");
        request.setUserId(userId);
        isok = this.shareTvBonus(request);

        // 发放师傅钛值奖励
        if (userRelation != null) {
            request.setRelationFlowId(flowid);
            flowid = idGenerater.nextId();
            request.setFlowId(flowid);
            request.setBonusType("2");
            request.setSubUserId(userRelation.getRelUserId());
            request.setUserId(userRelation.getUserId());
            isok = this.shareTvBonus(request);
        }

        // 发放用户氪金奖励
        flowid = idGenerater.nextId();
        request.setFlowId(flowid);
        request.setBonusType("1");
        request.setUserId(userId);
        isok = this.shareKgBonus(request);

        // 发放师傅氪金奖励
        if (userRelation != null) {
            request.setRelationFlowId(flowid);
            flowid = idGenerater.nextId();
            request.setFlowId(flowid);
            request.setBonusType("2");
            request.setSubUserId(userRelation.getRelUserId());
            request.setUserId(userRelation.getUserId());
            isok = this.shareKgBonus(request);
        }
        return isok;
    }

    @Override
    public boolean shareTvBonus(AccountRequest request) {
        logger.info("获取分享奖励TV：{}", JSON.toJSONString(request.getUserId()));
        boolean success;
        Lock lock = new Lock();
        String key = USER_ACCOUNT_LOCK_KEY + "SHARE_TV_BONUS" + request.getUserId() + request.getArticleId();
        try {
            lock.lock(key);

            // 查当前用户
            UserkgOutModel userkgModel = userRMapper.getUserDetails(request.getUserId());
            if (request.getUserId() == null || userkgModel == null) {
                logger.error("获取分享人用户id {}", request.getUserId());
                throw new ApiMessageException(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                        ExceptionEnum.PARAMEMPTYERROR.getMessage());
            }

            ArticleInModel ainModel = new ArticleInModel();
            ainModel.setArticleId(request.getArticleId());
            ArticleOutModel articleOutModel = articleRMapper.selectByIdDetails(ainModel);
            if (articleOutModel == null) {
                throw new ApiMessageException(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                        ExceptionEnum.PARAMEMPTYERROR.getMessage());
            }

            AccountFlowInModel accountFlowInModel = new AccountFlowInModel();
            accountFlowInModel.setAccountFlowId(request.getFlowId());
            if (request.getRelationFlowId() != null) {
                accountFlowInModel.setRelationFlowId(request.getRelationFlowId());
            } else {
                accountFlowInModel.setRelationFlowId(request.getFlowId());
            }
            accountFlowInModel.setUserId(Long.parseLong(userkgModel.getUserId()));
            accountFlowInModel.setUserPhone(userkgModel.getUserMobile());
            accountFlowInModel.setUserEmail(userkgModel.getUserEmail());
            if ("1".equals(request.getBonusType())) {
                accountFlowInModel.setAmount(Constants.SHARETVBONUS);
                accountFlowInModel.setAccountAmount(Constants.SHARETVBONUS);
                accountFlowInModel.setFlowDetail("我分享了《" + articleOutModel.getArticleTitle() + "》,收到平台分享奖励");
                accountFlowInModel.setBusinessTypeId(BusinessTypeEnum.SHAREARTICLETVAWARD.getStatus());
            } else if ("2".equals(request.getBonusType())) {
                UserkgOutModel subUserkgModel = userRMapper.getUserDetails(request.getSubUserId());
                accountFlowInModel.setAmount(Constants.SHAREMASTERTVBONUS);
                accountFlowInModel.setAccountAmount(Constants.SHAREMASTERTVBONUS);
                accountFlowInModel.setFlowDetail("获得徒弟" + subUserkgModel.getUserName() + "进贡");
                accountFlowInModel.setBusinessTypeId(BusinessTypeEnum.SUBCONTRITV.getStatus());
            }
            accountFlowInModel.setFlowDate(new Date());
            accountFlowInModel.setFlowStatus(FlowStatusEnum.NOSTATUS.getStatus());
            accountFlowInModel.setArticleId(request.getArticleId());
            success = accountFlowWMapper.insertFlowSelective(accountFlowInModel) > 0;

            logger.info("获得分享文章奖励：accountFlowInModel={}", JSON.toJSONString(accountFlowInModel));

            AccountInModel inModel = new AccountInModel();
            inModel.setUserId(request.getUserId());
            inModel.setBalance(request.getAmount());
            success = accountWMapper.updateAddUbalance(inModel) > 0;

            accountFlowInModel = new AccountFlowInModel();
            accountFlowInModel.setAccountFlowId(idGenerater.nextId());
            accountFlowInModel.setRelationFlowId(request.getFlowId());
            accountFlowInModel.setUserId(Constants.PLATFORM_USER_ID);
            accountFlowInModel.setRelationFlowId(request.getFlowId());
            if ("1".equals(request.getBonusType())) {
                accountFlowInModel.setAmount(Constants.SHARETVBONUS.negate());
                accountFlowInModel.setAccountAmount(Constants.SHARETVBONUS.negate());
                accountFlowInModel.setFlowDetail("发放userId:" + request.getUserId() + "分享奖励");
                accountFlowInModel.setBusinessTypeId(BusinessTypeEnum.SHAREARTICLETVAWARD.getStatus());
            } else if ("2".equals(request.getBonusType())) {
                accountFlowInModel.setAmount(Constants.SHAREMASTERTVBONUS.negate());
                accountFlowInModel.setAccountAmount(Constants.SHAREMASTERTVBONUS.negate());
                accountFlowInModel.setFlowDetail("发放userId:" + request.getUserId() + "徒弟进贡");
                accountFlowInModel.setBusinessTypeId(BusinessTypeEnum.SUBCONTRITV.getStatus());
            }
            accountFlowInModel.setFlowDate(new Date());
            accountFlowInModel.setFlowStatus(FlowStatusEnum.NOSTATUS.getStatus());
            accountFlowInModel.setArticleId(request.getArticleId());
            accountFlowWMapper.insertFlowSelective(accountFlowInModel);

            inModel.setUserId(Constants.PLATFORM_USER_ID);
            success = accountWMapper.updateReductionUbalance(inModel) > 0;

        } finally {
            lock.unLock();
        }

        return success;
    }

    @Override
    public boolean shareKgBonus(AccountRequest request) {
        logger.info("获取分享奖励KG：{}", JSON.toJSONString(request.getUserId()));
        boolean success;
        Lock lock = new Lock();
        // Long userId = request.getUserId();
        String key = USER_ACCOUNT_LOCK_KEY + "SHARE_KG_BONUS" + request.getUserId();
        try {
            lock.lock(key);
            // 查当前用户
            UserkgOutModel userkgModel = userRMapper.getUserDetails(request.getUserId());
            if (request.getUserId() == null || userkgModel == null) {
                logger.error("获取分享人用户id {}", request.getUserId());
                return false;
            }

            ArticleInModel ainModel = new ArticleInModel();
            ainModel.setArticleId(request.getArticleId());
            ArticleOutModel articleOutModel = articleRMapper.selectByIdDetails(ainModel);
            if (articleOutModel == null) {
                return false;
            }

            AccountFlowKgMongo accountFlowTxb = new AccountFlowKgMongo();
            accountFlowTxb.setAccount_flow_id(request.getFlowId());
            if (request.getRelationFlowId() != null) {
                accountFlowTxb.setRelation_flow_id(request.getRelationFlowId());
            } else {
                accountFlowTxb.setRelation_flow_id(request.getFlowId());
            }
            accountFlowTxb.setUser_id(request.getUserId());
            accountFlowTxb.setUser_phone(userkgModel.getUserMobile());
            accountFlowTxb.setUser_name(userkgModel.getUserName());
            accountFlowTxb.setUser_email(userkgModel.getUserEmail());
            if ("1".equals(request.getBonusType())) {
                accountFlowTxb.setAmount(Constants.SHAREKGBONUS);
                accountFlowTxb.setAccount_amount(Constants.SHAREKGBONUS);
                accountFlowTxb.setFlow_detail("我分享了《" + articleOutModel.getArticleTitle() + "》,收到平台分享奖励");
                accountFlowTxb.setBusiness_type_id(BusinessTypeEnum.SHAREARTICLETKGAWARD.getStatus());
            } else if ("2".equals(request.getBonusType())) {
                UserkgOutModel subUserkgModel = userRMapper.getUserDetails(request.getSubUserId());
                accountFlowTxb.setAmount(Constants.SHAREMASTERKGBONUS);
                accountFlowTxb.setAccount_amount(Constants.SHAREMASTERKGBONUS);
                accountFlowTxb.setFlow_detail("获得徒弟" + subUserkgModel.getUserName() + "进贡");
                accountFlowTxb.setBusiness_type_id(BusinessTypeEnum.SUBCONTRI.getStatus());
            }
            accountFlowTxb.setArticle_id(request.getArticleId());
            accountFlowTxb.setFlow_date(new Date().getTime());
            accountFlowTxb.setFlow_status(FlowStatusEnum.NOSTATUS.getStatus());
            MongoUtils.insertOne(MongoTables.ACCOUNT_FLOW_KG, new Document(Bean2MapUtil.bean2map(accountFlowTxb)));


            AccountInModel inModel = new AccountInModel();
            inModel.setUserId(request.getUserId());
            inModel.setBalance(Constants.SHAREMASTERKGBONUS);
            success = accountWMapper.addTxbBalance(inModel) > 0;

            accountFlowTxb = new AccountFlowKgMongo();
            accountFlowTxb.setAccount_flow_id(idGenerater.nextId());
            accountFlowTxb.setRelation_flow_id(request.getFlowId());
            accountFlowTxb.setUser_id(Constants.PLATFORM_USER_ID);
            if ("1".equals(request.getBonusType())) {
                accountFlowTxb.setAmount(Constants.SHAREKGBONUS.negate());
                accountFlowTxb.setAccount_amount(Constants.SHAREKGBONUS.negate());
                accountFlowTxb.setFlow_detail("发放userId:" + request.getUserId() + "分享奖励");
                accountFlowTxb.setBusiness_type_id(BusinessTypeEnum.SHAREARTICLETKGAWARD.getStatus());
            } else if ("2".equals(request.getBonusType())) {
                accountFlowTxb.setAmount(Constants.SHAREMASTERKGBONUS.negate());
                accountFlowTxb.setAccount_amount(Constants.SHAREMASTERKGBONUS.negate());
                accountFlowTxb.setFlow_detail("发放userId:" + request.getUserId() + "徒弟进贡");
                accountFlowTxb.setBusiness_type_id(BusinessTypeEnum.SUBCONTRI.getStatus());
            }
            accountFlowTxb.setArticle_id(request.getArticleId());
            accountFlowTxb.setFlow_date(new Date().getTime());
            accountFlowTxb.setFlow_status(FlowStatusEnum.NOSTATUS.getStatus());
            MongoUtils.insertOne(MongoTables.ACCOUNT_FLOW_KG, new Document(Bean2MapUtil.bean2map(accountFlowTxb)));

            inModel.setUserId(Constants.PLATFORM_USER_ID);

            BalanceDeducted balanceDeducted = BalanceDeducted.buildBalanceDeducted(inModel);
            balanceDeductedWMapper.insert(balanceDeducted);
//            success = accountWMapper.reduceTxbBalance(inModel) > 0;
        } finally {
            lock.unLock();
        }
        return success;
    }


}
