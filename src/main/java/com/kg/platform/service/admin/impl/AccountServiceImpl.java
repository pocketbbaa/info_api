package com.kg.platform.service.admin.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.kg.framework.toolkit.Check;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.easyexcel.AccountWithdrawFlowRitExcel;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ApiMessageException;
import com.kg.platform.common.exception.BusinessException;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.exception.TokenException;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.lock.Lock;
import com.kg.platform.common.mongo.MongoDao;
import com.kg.platform.common.mongo.MongoTables;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.mongo.Seach;
import com.kg.platform.common.mq.MQConfig;
import com.kg.platform.common.mq.MQProduct;
import com.kg.platform.common.support.ApiRequestSupport;
import com.kg.platform.common.utils.*;
import com.kg.platform.dao.entity.*;
import com.kg.platform.dao.read.AccountRMapper;
import com.kg.platform.dao.read.AccountWithdrawFlowRMapper;
import com.kg.platform.dao.read.UserRMapper;
import com.kg.platform.dao.read.admin.*;
import com.kg.platform.dao.write.AccountFlowWMapper;
import com.kg.platform.dao.write.AccountWMapper;
import com.kg.platform.dao.write.admin.KgRitFreezeLogWMapper;
import com.kg.platform.enumeration.*;
import com.kg.platform.model.in.*;
import com.kg.platform.model.mongoTable.AccountFlowRitMongo;
import com.kg.platform.model.mongoTable.AccountWithdrawFlowRit;
import com.kg.platform.model.mongoTable.WithdrawOperRecord;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.AccountRequest;
import com.kg.platform.model.request.AccountWithDrawRequest;
import com.kg.platform.model.request.BatchAuditRitWithdrawConsumerMqRequest;
import com.kg.platform.model.request.admin.*;
import com.kg.platform.model.response.AccountWithdrawFlowRitResponse;
import com.kg.platform.model.response.SiteinfoResponse;
import com.kg.platform.model.response.WithDrawSearchConditionsResponse;
import com.kg.platform.model.response.WithdrawOperRecordResponse;
import com.kg.platform.model.response.admin.*;
import com.kg.platform.service.SiteinfoService;
import com.kg.platform.service.TokenManager;
import com.kg.platform.service.UserWithdrawService;
import com.kg.platform.service.admin.AccountService;
import com.kg.platform.service.admin.BuryingPointService;
import com.kg.platform.service.app.AppAccountService;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCursor;
import org.apache.commons.collections.map.HashedMap;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Decimal128;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Service("AdminAccountService")
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private static final String USER_ADDUSERTIMES_LOCK_KEY = "kguser:: AccountService::appRitWithdraw::";

    private static Integer EXCELLIMIT = 30000;

    private static Integer REQUEST_SIZE = 1000;

    private static String EXCEL_NAME = "RIT提现工单-财务.csv";

    @Autowired
    private AdminAccountRechargeRMapper adminAccountRechargeRMapper;

    @Autowired
    private AdminAccountRMapper adminAccountRMapper;

    @Autowired
    private AdminAccountWithdrawRMapper adminAccountWithdrawRMapper;

    @Autowired
    private AdminAccountDipositRMapper adminAccountDipositRMapper;

    @Autowired
    private AdminBusinessTypeRMapper adminBusinessTypeRMapper;

    @Autowired
    private UserWithdrawService userWithdrawService;

    @Autowired
    private AccountRMapper accountRMapper;

    @Autowired
    private AccountWMapper accountWMapper;

    @Autowired
    private AccountWithdrawFlowRMapper accountWithdrawFlowRMapper;

    @Inject
    private IDGen generater;

    @Autowired
    private UserRMapper userRMapper;

    @Autowired
    private AccountFlowWMapper accountFlowWMapper;

    @Autowired
    private AdminUserBonusRMapper adminUserBonusRMapper;

    @Autowired
    private AdminUserBonusDetailRMapper adminUserBonusDetailRMapper;

    @Autowired
    private KgRitFreezeLogWMapper kgRitFreezeLogWMapper;

    @Autowired
    private SiteinfoService siteinfoService;

    @Autowired
    private AppAccountService appAccountService;

    @Autowired
    private IDGen idGen;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private BuryingPointService buryingPointService;

    @Autowired
    private KgRitRolloutRMapper kgRitRolloutRMapper;

    @Autowired
    private MQProduct mqProduct;

    @Autowired
    private MQConfig batchAuditRitWithdrawMqConfig;

    @Autowired
    private IDGen idGenerater;

    @Resource(name = "propertyLoader")
    private PropertyLoader propertyLoader;

    @Autowired
    private CommonService commonService;

    @Override
    public PageModel<AccountRechargeQueryResponse> getAccountRecharge(AccountRechargeQueryRequest request) {
        request.setLimit(request.getPageSize());
        request.setStart((request.getCurrentPage() - 1) * request.getPageSize());
        Date startDate = request.getStartDate();
        if (null != startDate) {
            DateUtils.getDateStart(startDate);
        }
        request.setStartDate(startDate);
        Date endDate = request.getEndDate();
        if (null != endDate) {
            DateUtils.getDateEnd(endDate);
        }
        request.setEndDate(endDate);
        List<AccountRechargeQueryResponse> data = adminAccountRechargeRMapper.selectByCondition(request);
        if (null != data && data.size() > 0) {
            data.stream().forEach(item -> {
                if (item.getStatus() == 0) {
                    item.setStatusDisplay("充值中");
                } else {
                    item.setStatusDisplay("充值成功");
                }
                item.setLevelDisplay("初级");
                item.setUserRoleDisplay(
                        UserRoleEnum.getUserRoleEnum(NumberUtils.intValue(item.getUserRole())).getDisplay());
            });
        }
        long count = adminAccountRechargeRMapper.selectCountByCondition(request);
        PageModel<AccountRechargeQueryResponse> pageModel = new PageModel<>();
        pageModel.setCurrentPage(request.getCurrentPage());
        pageModel.setPageSize(request.getPageSize());
        pageModel.setData(data);
        pageModel.setTotalNumber(count);
        return pageModel;
    }

    @Override
    public PageModel<AccountQueryResponse> getAccount(AccountQueryRequest request) {
        request.setLimit(request.getPageSize());
        request.setStart((request.getCurrentPage() - 1) * request.getPageSize());
        Date startDate = request.getStartDate();
        if (null != startDate) {
            startDate = DateUtils.getDateStart(startDate);
            request.setStartDate(startDate);
        }
        Date endDate = request.getEndDate();
        if (null != endDate) {
            endDate = DateUtils.getDateEnd(endDate);
            request.setEndDate(endDate);
        }
        List<AccountQueryResponse> data = adminAccountRMapper.selectByCondition(request);
        final Double sum = adminAccountRMapper.selectSumByCondition(request);
        DecimalFormat df = new DecimalFormat("0.000");
        df.setRoundingMode(RoundingMode.DOWN);
        if (null != data && data.size() > 0) {
            for (AccountQueryResponse item : data) {
                item.setLevelDisplay("初级");
                item.setUserRoleDisplay(
                        UserRoleEnum.getUserRoleEnum(NumberUtils.intValue(item.getUserRole())).getDisplay());
                item.setAllAmount(df.format(sum));
            }
        }
        long count = adminAccountRMapper.selectCountByCondition(request);
        PageModel<AccountQueryResponse> pageModel = new PageModel<>();
        pageModel.setCurrentPage(request.getCurrentPage());
        pageModel.setPageSize(request.getPageSize());
        pageModel.setData(data);
        pageModel.setTotalNumber(count);
        return pageModel;
    }

    @Override
    public PageModel<AccountQueryResponse> getTxbAccount(AccountQueryRequest request) {

        List<AccountQueryResponse> data = new ArrayList<>();

        BasicDBObject searchQuery = this.getKgSeachInfo(request);
        logger.info(">>>>>>>kg查询条件>>>>." + searchQuery.toString());

        BasicDBObject sort = new BasicDBObject();
        sort.put("flow_date", -1);
        logger.info(">>>>>>>kg排序条件>>>>." + sort.toString());

        MongoCursor<Document> cursor = MongoUtils.findByPage(MongoTables.ACCOUNT_FLOW_KG, searchQuery, sort, request.getCurrentPage(), request.getPageSize());
        if (cursor != null) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                System.out.println(">>>>>>rit流水数据>>>>>>>" + JsonUtil.writeValueAsString(document));
                AccountQueryResponse accountQueryResponse = this.getKgAccountQueryResponse(document);
                data.add(accountQueryResponse);
            }
        }
        long count = MongoUtils.count(MongoTables.ACCOUNT_FLOW_KG, searchQuery);
        PageModel<AccountQueryResponse> pageModel = new PageModel<>();
        pageModel.setCurrentPage(request.getCurrentPage());
        pageModel.setPageSize(request.getPageSize());
        pageModel.setData(data);
        pageModel.setTotalNumber(count);
        return pageModel;

//        request.setLimit(request.getPageSize());
//        request.setStart((request.getCurrentPage() - 1) * request.getPageSize());
//        Date startDate = request.getStartDate();
//        if (null != startDate) {
//            startDate = DateUtils.getDateStart(startDate);
//            request.setStartDate(startDate);
//        }
//        Date endDate = request.getEndDate();
//        if (null != endDate) {
//            endDate = DateUtils.getDateEnd(endDate);
//            request.setEndDate(endDate);
//        }
//        List<AccountQueryResponse> data = adminAccountRMapper.selectTxbFlow(request);
//        if (null != data && data.size() > 0) {
//            data.stream().forEach(item -> {
//                item.setLevelDisplay("初级");
//                item.setUserRoleDisplay(
//                        UserRoleEnum.getUserRoleEnum(NumberUtils.intValue(item.getUserRole())).getDisplay());
//            });
//        }
//        long count = adminAccountRMapper.selectTxbCount(request);
//        PageModel<AccountQueryResponse> pageModel = new PageModel<>();
//        pageModel.setCurrentPage(request.getCurrentPage());
//        pageModel.setPageSize(request.getPageSize());
//        pageModel.setData(data);
//        pageModel.setTotalNumber(count);
//        return pageModel;
    }


    public BasicDBObject getKgSeachInfo(AccountQueryRequest request) {
        BasicDBObject searchQuery = new BasicDBObject();
        //时间查询
        Date startDate = request.getStartDate();
        if (null != startDate) {
            startDate = DateUtils.getDateStart(startDate);
            searchQuery.put("flow_date", BasicDBObjectBuilder.start(Seach.GTE.getOperStr(), startDate.getTime()));
        }
        Date endDate = request.getEndDate();
        if (null != endDate) {
            endDate = DateUtils.getDateEnd(endDate);
            searchQuery.put("flow_date", BasicDBObjectBuilder.start(Seach.LTE.getOperStr(), endDate.getTime()));
        }
        if (null != endDate && null != startDate) {
            startDate = DateUtils.getDateStart(startDate);
            endDate = DateUtils.getDateEnd(endDate);
            searchQuery.put("flow_date", BasicDBObjectBuilder.start(Seach.GTE.getOperStr(), startDate.getTime())
                    .add(Seach.LTE.getOperStr(), endDate.getTime()).get());
        }
        //资金范围
        String minAmount = request.getMinAmount();
        if (null != minAmount) {
            searchQuery.append("amount", new BasicDBObject(Seach.GTE.getOperStr(), new BigDecimal(minAmount)));
        }
        String maxAmount = request.getMaxAmount();
        if (null != maxAmount) {
            searchQuery.append("amount", new BasicDBObject(Seach.LTE.getOperStr(), new BigDecimal(maxAmount)));
        }
        if (null != minAmount && null != maxAmount) {
            searchQuery.put("amount", BasicDBObjectBuilder.start(Seach.GTE.getOperStr(), new BigDecimal(minAmount))
                    .add(Seach.LTE.getOperStr(), new BigDecimal(maxAmount)).get());
        }
        //流水id
        if (request.getFlowId() != null) {
            searchQuery.append("account_flow_id", request.getFlowId());
        }
        //昵称
        if (request.getNickName() != null) {
            Pattern queryPattern = Pattern.compile(request.getNickName(), Pattern.CASE_INSENSITIVE);
            searchQuery.append("user_name", queryPattern);
        }
        //业务类型
        if (request.getBusinessTypeId() != null) {
            searchQuery.append("business_type_id", new BasicDBObject(Seach.EQ.getOperStr(), request.getBusinessTypeId()));
        }
        //手机号或者查询用户中心流水明细
        if (request.getMobile() != null || request.getSearchType() != null) {
            searchQuery.append("user_phone", request.getMobile());
        }

        return searchQuery;
    }

    public AccountQueryResponse getKgAccountQueryResponse(Document document) {
        AccountQueryResponse accountQueryResponse = new AccountQueryResponse();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        accountQueryResponse.setFlowId(document.get("account_flow_id") == null ? null : document.get("account_flow_id").toString());
        accountQueryResponse.setUserId(document.get("user_id") == null ? null : document.get("user_id").toString());
        accountQueryResponse.setUserName(document.get("user_name") == null ? null : document.get("user_name").toString());
        accountQueryResponse.setBusinessTypeId(document.get("business_type_id") == null ? null : document.getInteger("business_type_id"));
        accountQueryResponse.setMobile(document.get("user_phone") == null ? null : document.getString("user_phone"));
        accountQueryResponse.setEmail(document.get("user_email") == null ? null : document.getString("user_email"));
        String amount = document.get("amount") == null ? null : ((Decimal128) document.get("amount")).bigDecimalValue().stripTrailingZeros().toPlainString();
        accountQueryResponse.setAmount(amount);
        String poundageAmount = document.get("poundage_amount") == null ? null : ((Decimal128) document.get("poundage_amount")).bigDecimalValue().stripTrailingZeros().toPlainString();
        accountQueryResponse.setPoundageAmount(poundageAmount);
        String accountAmount = document.get("account_amount") == null ? null : ((Decimal128) document.get("account_amount")).bigDecimalValue().stripTrailingZeros().toPlainString();
        accountQueryResponse.setAccountAmount(accountAmount);
        String freezeAmount = document.get("freeze_amount") == null ? null : ((Decimal128) document.get("freeze_amount")).bigDecimalValue().stripTrailingZeros().toPlainString();
        accountQueryResponse.setFreezeAmount(freezeAmount);
        accountQueryResponse.setFlowDate(document.get("flow_date") == null ? null : sdf.format(new Date(document.getLong("flow_date"))));
        accountQueryResponse.setLevelDisplay("初级");
        accountQueryResponse.setFlowDetail(document.get("flow_detail") == null ? null : document.getString("flow_detail"));
        if(new BigDecimal(amount).compareTo(BigDecimal.ZERO)==0){
        	accountQueryResponse.setAmount(freezeAmount);
		}
        //查询流水状态信息
        Integer flowStatus = document.get("flow_status") == null ? null : document.getInteger("flow_status");
        if (flowStatus != null) {
            accountQueryResponse.setStatus(FlowStatusEnum.getFlowStatusEnum(flowStatus).getDisplay());
        }
        //查询流水业务信息
        if (accountQueryResponse.getBusinessTypeId() != null) {
            accountQueryResponse.setBusinessTypeName(BusinessTypeEnum.getBusinessTypeEnum(accountQueryResponse.getBusinessTypeId()).getDisplay());
        }
        //查询用户信息
        UserkgOutModel u = userRMapper.getUserDetails(Long.valueOf(accountQueryResponse.getUserId()));
        if (u != null) {
            accountQueryResponse.setUserRoleDisplay(
                    UserRoleEnum.getUserRoleEnum(NumberUtils.intValue(u.getUserRole())).getDisplay());
        }
        return accountQueryResponse;
    }


    @Override
    public PageModel<AccountQueryResponse> getRitAccount(AccountQueryRequest request) {

        // List<AccountQueryResponse> data = adminAccountRMapper.selectRitFlow(request);

        List<AccountQueryResponse> data = new ArrayList<>();

        BasicDBObject searchQuery = this.getSeachInfo(request);
        logger.info(">>>>>>>rit查询条件>>>>." + searchQuery.toString());

        BasicDBObject sort = new BasicDBObject();
        sort.put("flow_date", -1);
        logger.info(">>>>>>>rit排序条件>>>>." + sort.toString());

        MongoCursor<Document> cursor = MongoUtils.findByPage(MongoTables.ACCOUNT_FLOW_RIT, searchQuery, sort, request.getCurrentPage(), request.getPageSize());
        if (cursor != null) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                System.out.println(">>>>>>rit流水数据>>>>>>>" + JsonUtil.writeValueAsString(document));
                AccountQueryResponse accountQueryResponse = this.getAccountQueryResponse(document);
                data.add(accountQueryResponse);
            }
        }
        //long count= = adminAccountRMapper.selectRitCount(request);
        //查询总页数
        long count = MongoUtils.count(MongoTables.ACCOUNT_FLOW_RIT, searchQuery);
        PageModel<AccountQueryResponse> pageModel = new PageModel<>();
        pageModel.setCurrentPage(request.getCurrentPage());
        pageModel.setPageSize(request.getPageSize());
        pageModel.setData(data);
        pageModel.setTotalNumber(count);
        return pageModel;
    }


    public BasicDBObject getSeachInfo(AccountQueryRequest request) {
        BasicDBObject searchQuery = new BasicDBObject();
        //时间查询
        Date startDate = request.getStartDate();
        if (null != startDate) {
            startDate = DateUtils.getDateStart(startDate);
            searchQuery.put("flow_date", BasicDBObjectBuilder.start(Seach.GTE.getOperStr(), startDate.getTime()));
        }
        Date endDate = request.getEndDate();
        if (null != endDate) {
            endDate = DateUtils.getDateStart(endDate);
            searchQuery.put("flow_date", BasicDBObjectBuilder.start(Seach.LTE.getOperStr(), endDate.getTime()));
        }
        if (null != endDate && null != startDate) {
            startDate = DateUtils.getDateStart(startDate);
            endDate = DateUtils.getDateStart(endDate);
            searchQuery.put("flow_date", BasicDBObjectBuilder.start(Seach.GTE.getOperStr(), startDate.getTime())
                    .add(Seach.LTE.getOperStr(), endDate.getTime()).get());
        }
        //资金范围
        String minAmount = request.getMinAmount();
        if (null != minAmount) {
            searchQuery.append("amount", new BasicDBObject(Seach.GTE.getOperStr(), new BigDecimal(minAmount)));
        }
        String maxAmount = request.getMaxAmount();
        if (null != maxAmount) {
            searchQuery.append("amount", new BasicDBObject(Seach.LTE.getOperStr(), new BigDecimal(maxAmount)));
        }
        if (null != minAmount && null != maxAmount) {
            searchQuery.put("amount", BasicDBObjectBuilder.start(Seach.GTE.getOperStr(), new BigDecimal(minAmount))
                    .add(Seach.LTE.getOperStr(), new BigDecimal(maxAmount)).get());
        }
        //流水id
        if (request.getFlowId() != null) {
            searchQuery.append("account_flow_id", request.getFlowId());
        }
        //昵称
        if (request.getNickName() != null) {
            Pattern queryPattern = Pattern.compile(request.getNickName(), Pattern.CASE_INSENSITIVE);
            searchQuery.append("user_name", queryPattern);
        }
        //手机号或者查询用户中心流水明细
        if (request.getMobile() != null || request.getSearchType() != null) {
            Pattern queryPattern = Pattern.compile(request.getMobile(), Pattern.CASE_INSENSITIVE);
            searchQuery.append("user_phone", queryPattern);
        }
        if (request.getSearchType() != null) {
            int typeId[] = {2000, 2100, 2102, 2104, 2003, 2004, 2005};
            searchQuery.append("business_type_id", new BasicDBObject(Seach.IN.getOperStr(), typeId));
        }
        //业务类型
        if (request.getBusinessTypeId() != null) {
            searchQuery.append("business_type_id", new BasicDBObject(Seach.EQ.getOperStr(), request.getBusinessTypeId()));
        }
        return searchQuery;
    }


    public AccountQueryResponse getAccountQueryResponse(Document document) {
        AccountQueryResponse accountQueryResponse = new AccountQueryResponse();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        accountQueryResponse.setUserId(document.get("user_id") == null ? null : document.get("user_id").toString());
        accountQueryResponse.setFlowId(document.get("account_flow_id") == null ? null : document.get("account_flow_id").toString());
        accountQueryResponse.setUserName(document.get("user_name") == null ? null : document.get("user_name").toString());
        accountQueryResponse.setBusinessTypeId(document.get("business_type_id") == null ? null : document.getInteger("business_type_id"));
        accountQueryResponse.setMobile(document.get("user_phone") == null ? null : document.getString("user_phone"));
        accountQueryResponse.setEmail(document.get("user_email") == null ? null : document.getString("user_email"));

        String amount=document.get("amount") == null ? "0" : ((Decimal128) document.get("amount")).bigDecimalValue().stripTrailingZeros().toPlainString();
        accountQueryResponse.setAmount(amount);
        accountQueryResponse.setOperAccount(1);

        String poundageAmount = document.get("poundage_amount") == null ? "0" : ((Decimal128) document.get("poundage_amount")).bigDecimalValue().stripTrailingZeros().toPlainString();
        accountQueryResponse.setPoundageAmount(poundageAmount);

        String accountAmount = document.get("account_amount") == null ? "0" : ((Decimal128) document.get("account_amount")).bigDecimalValue().stripTrailingZeros().toPlainString();
        accountQueryResponse.setAccountAmount(accountAmount);

        String freezeAmount = document.get("freeze_amount") == null ? "0" : ((Decimal128) document.get("freeze_amount")).bigDecimalValue().stripTrailingZeros().toPlainString();
        accountQueryResponse.setFreezeAmount(freezeAmount);
        if (StringUtils.isNotEmpty(freezeAmount) && !"0".equals(freezeAmount)) {
            accountQueryResponse.setOperAccount(2);
            accountQueryResponse.setAmount(freezeAmount);
        }
        accountQueryResponse.setFlowDate(document.get("flow_date") == null ? null : sdf.format(new Date(document.getLong("flow_date"))));
        accountQueryResponse.setLevelDisplay("初级");
        accountQueryResponse.setFlowDetail(document.get("flow_detail") == null ? null : document.getString("flow_detail"));
        //查询流水状态信息
        Integer flowStatus = document.get("flow_status") == null ? null : document.getInteger("flow_status");
        if (flowStatus != null) {
            accountQueryResponse.setStatus(FlowStatusEnum.getFlowStatusEnum(flowStatus).getDisplay());
        }
        //查询流水业务信息
        if (accountQueryResponse.getBusinessTypeId() != null) {
            accountQueryResponse.setBusinessTypeName(BusinessTypeEnum.getBusinessTypeEnum(accountQueryResponse.getBusinessTypeId()).getDisplay());
        }
        //查询用户信息
        UserkgOutModel u = userRMapper.getUserDetails(Long.valueOf(accountQueryResponse.getUserId()));
        if (u != null) {
            accountQueryResponse.setUserRoleDisplay(
                    UserRoleEnum.getUserRoleEnum(NumberUtils.intValue(u.getUserRole())).getDisplay());
        }
        return accountQueryResponse;
    }


    @Override
    public PageModel<AccountWithdrawQueryResponse> getAccountWithdraw(AccountWithdrawQueryRequest request) {
        request.setLimit(request.getPageSize());
        request.setStart((request.getCurrentPage() - 1) * request.getPageSize());
        Date startDate = request.getStartDate();
        if (null != startDate) {
            startDate = DateUtils.getDateStart(startDate);
            request.setStartDate(startDate);
        }
        Date endDate = request.getEndDate();
        if (null != endDate) {
            endDate = DateUtils.getDateEnd(endDate);
            request.setEndDate(endDate);
        }
        List<AccountWithdrawQueryResponse> data = adminAccountWithdrawRMapper.selectByCondition(request);
        if (null != data && data.size() > 0) {
            data.stream().forEach(item -> {
                item.setLevelDisplay("初级");
                item.setUserRoleDisplay(
                        UserRoleEnum.getUserRoleEnum(NumberUtils.intValue(item.getUserRole())).getDisplay());
                item.setStatusDisplay(AccountWithdrawStatusEnum
                        .getAccountWithdrawStatusEnum(NumberUtils.intValue(item.getStatus())).getDisplay());
            });
        }
        long count = adminAccountWithdrawRMapper.selectCountByCondition(request);
        PageModel<AccountWithdrawQueryResponse> pageModel = new PageModel<>();
        pageModel.setCurrentPage(request.getCurrentPage());
        pageModel.setPageSize(request.getPageSize());
        pageModel.setData(data);
        pageModel.setTotalNumber(count);
        return pageModel;
    }

    @Override
    @Transactional
    public boolean auditAccountWithdraw(AccountWithdrawEditRequest request) {
        AccountWithdrawFlow flow = accountWithdrawFlowRMapper.selectByPrimaryKey(request.getFlowId());
        if (null == flow || flow.getStatus() != 0) {
            return false;
        }

        if (request.getStatus() == 1) {
            UserWithdrawInModel model = new UserWithdrawInModel();
            model.setWithdrawFlowId(request.getFlowId());

            userWithdrawService.withdraw(model);

            // if (null == request.getRefuseReason()) {
            // request.setRefuseReason("通过");
            // }
            // adminAccountWithdrawRMapper.auditAccountWithdraw(request);

        } else if (request.getStatus() == 2) {

            adminAccountWithdrawRMapper.auditAccountWithdraw(request);
            // 已撤销，生成流水，退换用户钛值
            Account account = accountRMapper.selectByUserId(flow.getUserId());
            account.setBalance(account.getBalance().add(flow.getWithdrawAmount()));
            account.setFrozenBalance(account.getFrozenBalance().subtract(flow.getWithdrawAmount()));
            accountWMapper.updateByPrimaryKeySelective(account);
            // 生成一条交易记录
            AccountFlowInModel model = new AccountFlowInModel();
            model.setAccountAmount(flow.getWithdrawAmount());
            model.setAccountDate(new Date());
            model.setFlowDate(new Date());
            model.setFlowStatus(1);
            model.setAccountFlowId(generater.nextId());
            model.setAmount(flow.getWithdrawAmount());
            // model.setPoundageAmount(flow.getPoundageAmount());
            model.setFlowDetail("提币申请 已撤销");
            model.setUserId(flow.getUserId());
            UserkgOutModel user = userRMapper.selectByPrimaryKey(flow.getUserId());
            if (null != user) {
                model.setUserEmail(user.getUserEmail());
                model.setUserPhone(user.getUserMobile());
            }
            model.setBusinessTypeId(20);
            accountFlowWMapper.insertFlowSelective(model);
        }
        return true;
    }

    @Override
    public PageModel<AccountDipositQueryResponse> getAccountDiposit(AccountDipositQueryRequest request) {
        request.setLimit(request.getPageSize());
        request.setStart((request.getCurrentPage() - 1) * request.getPageSize());
        Date startDate = request.getStartDate();
        if (null != startDate) {
            startDate = DateUtils.getDateStart(startDate);
            request.setStartDate(startDate);
        }
        Date endDate = request.getEndDate();
        if (null != endDate) {
            endDate = DateUtils.getDateEnd(endDate);
            request.setEndDate(endDate);
        }
        List<AccountDipositQueryResponse> data = adminAccountDipositRMapper.selectByCondition(request);
        if (null != data && data.size() > 0) {
            data.stream().forEach(item -> {
                item.setLevelDisplay("初级");
                item.setUserRoleDisplay(
                        UserRoleEnum.getUserRoleEnum(NumberUtils.intValue(item.getUserRole())).getDisplay());
            });
        }
        long count = adminAccountDipositRMapper.selectCountByCondition(request);
        PageModel<AccountDipositQueryResponse> pageModel = new PageModel<>();
        pageModel.setCurrentPage(request.getCurrentPage());
        pageModel.setPageSize(request.getPageSize());
        pageModel.setData(data);
        pageModel.setTotalNumber(count);
        return pageModel;
    }

    @Override
    public List<BusinessTypeQueryResponse> getBusinessType() {
        return adminBusinessTypeRMapper.selectAll();
    }

    @Override
    public PageModel<UserBonusQueryResponse> getUserBonusList(UserBonusQueryRequest request) {
        request.setLimit(request.getPageSize());
        request.setStart((request.getCurrentPage() - 1) * request.getPageSize());
        if (StringUtils.isNotEmpty(request.getStartTime())) {
            request.setStartTime(request.getStartTime() + " 00:00:00");
        }
        if (StringUtils.isNotEmpty(request.getEndTime())) {
            request.setEndTime(request.getEndTime() + " 23:59:59");
        }
        List<UserBonusQueryResponse> data = adminUserBonusRMapper.getUserBonusList(request);
        Long count = adminUserBonusRMapper.getUserBonusCount(request);
        PageModel<UserBonusQueryResponse> pageModel = new PageModel<>();
        pageModel.setCurrentPage(request.getCurrentPage());
        pageModel.setPageSize(request.getPageSize());
        pageModel.setData(data);
        pageModel.setTotalNumber(count);
        return pageModel;
    }

    @Override
    public UserBonusQueryResponse getUserBonusDetail(UserBonusQueryRequest request) {
        return adminUserBonusRMapper.getUserBonusDetail(request);
    }

    @Override
    public PageModel<UserBonusDetailQueryResponse> getUserBonusDetailList(UserBonusQueryRequest request) {
        request.setLimit(request.getPageSize());
        request.setStart((request.getCurrentPage() - 1) * request.getPageSize());
        if (StringUtils.isNotEmpty(request.getStartTime())) {
            request.setStartTime(request.getStartTime() + " 00:00:00");
        }
        if (StringUtils.isNotEmpty(request.getEndTime())) {
            request.setEndTime(request.getEndTime() + " 23:59:59");
        }
        List<UserBonusDetailQueryResponse> data = adminUserBonusDetailRMapper.getUserBonusDetailList(request);
        Long count = adminUserBonusDetailRMapper.getUserBonusDetailCount(request);
        PageModel<UserBonusDetailQueryResponse> pageModel = new PageModel<UserBonusDetailQueryResponse>();
        pageModel.setCurrentPage(request.getCurrentPage());
        pageModel.setPageSize(request.getPageSize());
        pageModel.setData(data);
        pageModel.setTotalNumber(count);
        return pageModel;
    }

    @Override
    public String getUserTotalBonus(AccountQueryRequest request) {
        String totalBonusStr = "";

        // 查询用户氪金/钛值 rit奖励统计
        TotalBonusQueryResponse totalBonus = adminAccountRMapper.sumUserBonus(request);

        totalBonusStr += totalBonus.getTotalTv() + "TV  ";

        //查询氪金
        List<Bson> condition = this.getUserTotalBonusQuerry(request, 1);
        MongoCursor<Document> cursor = MongoUtils.aggregate(MongoTables.ACCOUNT_FLOW_KG, condition);
        logger.info(">>>>>>>查询kg总额条件>>>>>>>>>" + condition.toString());
        totalBonusStr += this.buiidTotalBonus(cursor) + "KG  ";

        //查询rit
        condition = this.getUserTotalBonusQuerry(request, 2);
        logger.info(">>>>>>>查询Rit总额条件>>>>>>>>>" + condition.toString());
        cursor = MongoUtils.aggregate(MongoTables.ACCOUNT_FLOW_RIT, condition);
        totalBonusStr += this.buiidTotalBonus(cursor) + "RIT";
        return totalBonusStr;
    }

    private String buiidTotalBonus(MongoCursor<Document> cursor) {
        String total = "0";
        if (cursor != null) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                total = document.get("total") == null ? "0" : ((Decimal128) document.get("total")).bigDecimalValue().stripTrailingZeros().toPlainString();
            }
        }
        return total;
    }

    /**
     * @param request
     * @param type    1氪金 2rit
     * @return
     */
    private List<Bson> getUserTotalBonusQuerry(AccountQueryRequest request, int type) {
        List<Bson> condition = new ArrayList<>();
        //查询条件
        BasicDBObject querryBy = new BasicDBObject();
        querryBy.append("user_phone", new BasicDBObject(Seach.EQ.getOperStr(), request.getMobile()));
        if (type == 1) {
            //氪金业务条件
            int typeId[]={1000,1510,1520,1530,1560,1570};
            querryBy.append("business_type_id", new BasicDBObject(Seach.IN.getOperStr(), typeId));
        }else  if(type==2){
            //rit业务条件
            int typeId[]={2000,2100,2102};
            querryBy.append("business_type_id",new BasicDBObject(Seach.IN.getOperStr(),typeId));
        }
        BasicDBObject querry = new BasicDBObject("$match", querryBy);
        condition.add(querry);

        //统计条件
        BasicDBObject groupBy = new BasicDBObject();
        groupBy.append("_id", "$user_phone");
        groupBy.append("total", new BasicDBObject("$sum", "$amount"));
        BasicDBObject group = new BasicDBObject("$group", groupBy);
        condition.add(group);

        return condition;
    }

    @Override
    public JsonEntity getRitExchangeList(RitExchangeQueryRequest request) {
        int currentPage = request.getCurrentPage() <= 0 ? 1 : request.getCurrentPage();
        int pageSize = request.getPageSize() <= 0 ? 20 : request.getPageSize();
        request.setCurrentPage(currentPage);
        request.setPageSize(pageSize);
        String collName = MongoTables.KG_RIT_EXCHANGE_FLOW;
        BasicDBObject basicDBObject = buildQueryParamsForgetRitExchangeList(request);
        BasicDBObject sort = new BasicDBObject("createTime", -1);
        MongoCursor<Document> cursor = MongoUtils.findByPage(collName, basicDBObject, sort, currentPage, pageSize);
        List<RitExchangeQueryResponse> responsesList = new ArrayList<>();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            RitExchangeQueryResponse response = getRitExchangeQueryResponse(document);
            responsesList.add(response);
        }
        long count = MongoUtils.count(collName, basicDBObject);
        return JsonEntity.makeSuccessJsonEntity(buildPageModelForGetRitExchangeList(request, responsesList, count));
    }

    @Override
    public JsonEntity getRitExchangeAmount(RitExchangeQueryRequest request) {
        MongoCursor<Document> cursor = MongoUtils.aggregate(MongoTables.KG_RIT_EXCHANGE_FLOW, getDBObjectGroup(request));
        Map<String, String> map = new HashMap<>();
        if (cursor != null) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                Decimal128 totalRitAmount = (Decimal128) document.get("totalRitAmount");
                Decimal128 totalKgAmount = (Decimal128) document.get("totalKgAmount");
                String ritTotal = totalRitAmount.toString();
                String kgTotal = totalKgAmount.toString();
                map.put("totalRitAmount", ritTotal);
                map.put("totalKgAmount", kgTotal);
            }
        }
        return JsonEntity.makeSuccessJsonEntity(map);
    }

    @Override
    public JsonEntity getRitWithdrawList(AccountWithDrawRequest request, PageModel<AccountWithdrawFlowRitResponse> pageModel) {
        long now = System.currentTimeMillis();
        logger.info("后台获取RIT工单列表开始------request:{}", JSON.toJSONString(request));
        if (request.getCurrentPage() == null || request.getPageSize() == null || request.getAuditRole() == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        pageModel.setCurrentPage(request.getCurrentPage());
        pageModel.setPageSize(request.getPageSize());

        BasicDBObject filter = new BasicDBObject();
        if (StringUtils.isNotBlank(request.getUserPhone())) {
            filter.append("userPhone", new Document(Seach.REGEX.getOperStr(), request.getUserPhone()).append(Seach.OPTIONS.getOperStr(), "i"));
        }
        if (StringUtils.isNotBlank(request.getUserName())) {
            filter.append("userName", new Document(Seach.REGEX.getOperStr(), request.getUserName()).append(Seach.OPTIONS.getOperStr(), "i"));
        }

        if (request.getStartTime() != null && request.getEndTime() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(request.getEndTime()));
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            filter.append("withdrawTime", new Document(Seach.GTE.getOperStr(), request.getStartTime()).append(Seach.LTE.getOperStr(), calendar.getTimeInMillis()));
        }
        if (request.getAuditRole() == 1) {
            //运营
            if (request.getStatus() != null) {
                if (request.getStatus() == OperTypeEnum.WITHDRAW_OPER.getStatus()) {
                    filter.append("ifOper", 1);
                } else {
                    List<Integer> ifOpers = Lists.newArrayList();
                    ifOpers.add(0);
                    ifOpers.add(null);
                    filter.append("status", request.getStatus()).append("ifOper", new Document(Seach.IN.getOperStr(), ifOpers));
                }
            }
        }
        if (request.getAuditRole() == 2) {
            //财务角色 只需要获取状态为审核通过、已转出、转出失败这几个状态的工单
            List<Integer> statusList = Lists.newArrayList();
            statusList.add(OperTypeEnum.WITHDRAW_WATING.getStatus());
            statusList.add(OperTypeEnum.WITHDRAW_SUCCESS.getStatus());
            statusList.add(OperTypeEnum.WITHDRAW_FAIL.getStatus());
            Integer[] status = new Integer[]{1, 3, 4};
            if (request.getStatus() != null) {
                if (statusList.contains(request.getStatus())) {
                    filter.append("status", request.getStatus());
                } else {
                    pageModel.setData(Lists.newArrayList());
                    return JsonEntity.makeSuccessJsonEntity(pageModel);
                }
            } else {
                filter.append("status", new BasicDBObject(Seach.IN.getOperStr(), status));
            }
        }

        BasicDBObject sort = new BasicDBObject("withdrawTime", -1);
        MongoCursor<Document> cursor = MongoUtils.findByPage(MongoTables.ACCOUNT_WITHDRAW_FLOW_RIT, filter, sort, pageModel.getCurrentPage(), pageModel.getPageSize());
        List<AccountWithdrawFlowRitResponse> responses = Lists.newArrayList();
        while (cursor.hasNext()) {
            AccountWithdrawFlowRitResponse response = new AccountWithdrawFlowRitResponse();
            Document document = cursor.next();
            response.setWithdrawFlowId(document.getLong("withdrawFlowId").toString());
            response.setUserPhone(document.getString("userPhone"));
            response.setUserName(document.getString("userName"));
            response.setToAddress(document.getString("toAddress"));
            response.setWithdrawAmount(((Decimal128) document.get("withdrawAmount")).bigDecimalValue().stripTrailingZeros().toPlainString());
            response.setAccountAmount(((Decimal128) document.get("accountAmount")).bigDecimalValue().stripTrailingZeros().toPlainString());
            response.setWithdrawTime(document.getLong("withdrawTime").toString());
            response.setIfOper(document.getInteger("ifOper"));
            if (document.getLong("accountTime") != null) {
                response.setAccountTime(document.getLong("accountTime").toString());
            }
            OperTypeEnum operTypeEnum = OperTypeEnum.getBusinessTypeEnum(document.getInteger("status"));
            if (response.getIfOper() != null && response.getIfOper() == 1) {
                operTypeEnum = OperTypeEnum.WITHDRAW_OPER;
            }
            response.setStatus(operTypeEnum.getStatus());
            response.setStatusName(operTypeEnum.getDisplay());
            response.setAuditUserName(document.getString("auditUserName"));
            responses.add(response);
        }
        pageModel.setData(responses);

        //获取总数
        long count = MongoUtils.count(MongoTables.ACCOUNT_WITHDRAW_FLOW_RIT, filter);
        pageModel.setTotalNumber(count);
        logger.info("后台获取RIT工单列表结束------耗时:{}ms", (System.currentTimeMillis() - now));
        return JsonEntity.makeSuccessJsonEntity(pageModel);
    }

    @Override
    public JsonEntity detailRitWithdraw(AccountWithDrawRequest request) {

        long now = System.currentTimeMillis();
        if (StringUtils.isBlank(request.getWithdrawFlowId())) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }

        BasicDBObject filter = new BasicDBObject();
        filter.append("withdrawFlowId", Long.valueOf(request.getWithdrawFlowId()));
        //获取工单详情
        MongoCursor<Document> cursor = MongoUtils.findByFilter(MongoTables.ACCOUNT_WITHDRAW_FLOW_RIT, filter);
        if (!cursor.hasNext()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.NO_DATA);
        }
        AccountWithdrawFlowRitResponse response = new AccountWithdrawFlowRitResponse();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            response.setWithdrawFlowId(document.getLong("withdrawFlowId").toString());
            response.setUserId(document.getLong("userId").toString());
            response.setUserPhone(document.getString("userPhone"));
            response.setUserRole(document.getInteger("userRole"));
            response.setUserRoleName(document.getString("userRoleName"));
            response.setUserName(document.getString("userName"));
            response.setToAddress(document.getString("toAddress"));
            if (document.getLong("accountTime") != null) {
                response.setAccountTime(document.getLong("accountTime").toString());
            }
            response.setWithdrawAmount(((Decimal128) document.get("withdrawAmount")).bigDecimalValue().stripTrailingZeros().toPlainString());
            response.setAccountAmount(((Decimal128) document.get("accountAmount")).bigDecimalValue().stripTrailingZeros().toPlainString());
            response.setPoundageAmount(((Decimal128) document.get("poundageAmount")).bigDecimalValue().stripTrailingZeros().toPlainString());
            OperTypeEnum operTypeEnum = OperTypeEnum.getBusinessTypeEnum(document.getInteger("status"));
            response.setStatus(operTypeEnum.getStatus());
            response.setStatusName(operTypeEnum.getDisplay());
            response.setWithdrawTime(document.getLong("withdrawTime").toString());
        }

        //获取工单的操作日志
        List<WithdrawOperRecordResponse> withdrawOperRecordResponseList = Lists.newArrayList();
        cursor = MongoUtils.findByFilter(MongoTables.WITHDRAW_OPER_RECORD, filter);
        while (cursor.hasNext()) {
            WithdrawOperRecordResponse withdrawOperRecordResponse = new WithdrawOperRecordResponse();
            Document document = cursor.next();
            withdrawOperRecordResponse.setOperId(document.getLong("operId").toString());
            withdrawOperRecordResponse.setOperTypeName(document.getString("operTypeName"));
            withdrawOperRecordResponse.setOperUserName(document.getString("operUserName"));
            withdrawOperRecordResponse.setOperDate(document.getLong("operDate").toString());
            withdrawOperRecordResponse.setTxId(document.getString("txId"));
            withdrawOperRecordResponse.setRemark(document.getString("remark"));
            withdrawOperRecordResponseList.add(withdrawOperRecordResponse);
        }
        response.setWithdrawOperRecordResponseList(withdrawOperRecordResponseList);
        logger.info("RIT审核提现详情耗时：{}ms", (System.currentTimeMillis() - now));
        return JsonEntity.makeSuccessJsonEntity(response);
    }

    @Override
    public JsonEntity getUserAssetInfoInfo(AccountWithDrawRequest request) {
        if (request.getUserId() == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        Account account = accountRMapper.selectByUserId(Long.parseLong(request.getUserId()));
        if (account == null) {
            account = new Account();
        }
        List<HashMap<String, Object>> rows = new ArrayList<>();
        HashMap<String, Object> kgMap = new HashMap<>();
        HashMap<String, Object> tvMap = new HashMap<>();
        HashMap<String, Object> ritMap = new HashMap<>();
        kgMap.put("useable", account.getTxbBalance().stripTrailingZeros().toPlainString());
        kgMap.put("freeze", account.getTxbFrozenBalance().stripTrailingZeros().toPlainString());
        kgMap.put("typeName", "kg");

        tvMap.put("useable", account.getBalance().stripTrailingZeros().toPlainString());
        tvMap.put("freeze", account.getFrozenBalance().stripTrailingZeros().toPlainString());
        tvMap.put("typeName", "tv");

        ritMap.put("useable", account.getRitBalance().stripTrailingZeros().toPlainString());
        ritMap.put("freeze", account.getRitFrozenBalance().stripTrailingZeros().toPlainString());
        ritMap.put("typeName", "rit");
        rows.add(kgMap);
        rows.add(tvMap);
        rows.add(ritMap);
        return JsonEntity.makeSuccessJsonEntity(rows);
    }

    @Override
    @Transactional
    public JsonEntity assetOperation(SysUser sysUser, FreezeInModel request) {
        if (sysUser == null || sysUser.getSysUserId() == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.TOKENRERROR);
        }
        if (request.getUserId() == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if(request.getAmount().compareTo(BigDecimal.ZERO)<=0){
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.AMOUNTMINUS);
        }
        //检查是否存在转出或者审核的工单
        if(commonService.judgeRitRollout(Long.parseLong(request.getUserId())) && request.getCallMethod().equals(Constants.RELVE)){
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.GOING_WORK_ORDER);
        }
        Account account = accountRMapper.selectByUserId(Long.parseLong(request.getUserId()));
        //检查余额是否足够冻结/解冻
        if (invalidBalance(request, account)) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACCOUNT_BALANCE_ERROR);
        }
        Account updateModel = getUpdateModel(account, request);
        try {
            accountWMapper.updateByPrimaryKeySelective(updateModel);
            //记录流水信息
            recordParticulars(request);
            recordFreezeLog(sysUser, request);
        } catch (Exception e) {
            logger.error("【冻结/解冻账户资产信息时出现异常 e:{}】", e.getMessage());
            throw new RuntimeException("【type:{" + request.getCallMethod() + "} 操作异常】");
        }
        return JsonEntity.makeSuccessJsonEntity();
    }

    private void recordParticulars(FreezeInModel request) {
        //获取最新的账户余额信息
        Account account = accountRMapper.selectByUserId(Long.parseLong(request.getUserId()));
        if (account == null) {
            logger.error("【发生异常的操作顺序,出现了一个不存在的用户,回滚事务 user_id:{}】", request.getUserId());
            throw new RuntimeException("异常的操作,中断操作 异常原因:{获取到的账户信息为空 userId-->" + request.getUserId() + "}");
        }
        //获取用户信息
        UserkgOutModel userModel = userRMapper.selectByPrimaryKey(Long.parseLong(request.getUserId()));
        if (userModel == null) {
            throw new RuntimeException("异常的操作,中断操作 异常原因:{获取到的用户信息为空 userId-->" + request.getUserId() + "}");
        }
        List<AccountFlowRitMongo> ritMongoModels = new RitAssectFlowRecord(1, userModel, account)
                .setFreezeInModel(request)
                .invoke()
                .getRitFlowList();
        if (ritMongoModels == null || ritMongoModels.isEmpty()) {
            throw new RuntimeException("异常的操作,中断操作 异常原因:{生成流水信息时出现异常 userId-->" + request.getUserId() + "}");
        }
        List<Document> insertData = transDocument(ritMongoModels);
        try {
            MongoUtils.insertMany(MongoTables.ACCOUNT_FLOW_RIT, insertData);
        } catch (Exception e) {
            logger.error("【异常的操作,中断操作 异常原因:{插入流水数据到mongo时发生异常 异常原因e:{" + e.getMessage() + "}}】");
            throw new RuntimeException("异常的操作,中断操作 异常原因:异常原因:{插入流水数据到mongo时发生异常 异常原因e:{" + e.getMessage() + "}}");
        }
    }

    private List<Document> transDocument(List<AccountFlowRitMongo> ritMongoModels) {
        List<Document> result = new ArrayList<>();
        for (AccountFlowRitMongo accountFlowRitMongo : ritMongoModels) {
            result.add(new Document(Bean2MapUtil.bean2map(accountFlowRitMongo)));
        }
        return result;
    }

    class RitAssectFlowRecord {

        private Integer type;
        private UserkgOutModel userInfo;
        private String callMethod;
        private List<AccountFlowRitMongo> ritFlowList;
        private Long flowId;
        private FreezeInModel request;
        private Account account;

        public RitAssectFlowRecord(Integer type, UserkgOutModel userInfo, Account account) {
            this.type = type;
            this.userInfo = userInfo;
            this.ritFlowList = new ArrayList<>();
            this.flowId = idGenerater.nextId();
            this.account = account;
        }

        public RitAssectFlowRecord setFreezeInModel(FreezeInModel request) {
            this.request = request;
            this.callMethod = request.getCallMethod();
            return this;
        }

        public List<AccountFlowRitMongo> getRitFlowList() {
            return ritFlowList;
        }

        public RitAssectFlowRecord invoke() {
            switch (type) {
                case 1:  //RIT
                    discern();
                    return this;
                default:
                    logger.error("【错误的类型 type:{" + type + "}】");
            }
            return this;
        }

        private void discern() {

            switch (callMethod) {
                case Constants.FREEZE:
                    generateFreezeFlowRecordDetail();
                    return;
                case Constants.RELVE:
                    generateRevelFlowRecord();
                default:
                    logger.warn("【未知的callMethod:{" + this.callMethod + "}】");
            }
        }

        private void generateRevelFlowRecord() {
            //生成解冻流水(可用余额增加流水)
            ritFlowList.add(generateRevelAmountFlowRecord());
            //生成解冻流水 (冻结余额减少)
            ritFlowList.add(generateRevelFreezeFlowRecord());
        }

        private void generateFreezeFlowRecordDetail() {
            //生成冻结流水 (可用余额减少流水)
            ritFlowList.add(generateFreezeAmountFlowRecord());
            //生成冻结流水(冻结余额增加)
            ritFlowList.add(generateFreezeFlowRecord());
        }

        private AccountFlowRitMongo generateRevelFreezeFlowRecord() {
            BigDecimal amount = CommonService.setScaleBig(request.getAmount().multiply(new BigDecimal(-1)),retain,BigDecimal.ROUND_DOWN);
            AccountFlowRitMongo accountFlowRitMongo = genBaseFlowModel();
            accountFlowRitMongo.setAccount_flow_id(idGenerater.nextId());
            accountFlowRitMongo.setFreeze_amount(amount);
            accountFlowRitMongo.setFlow_detail(BusinessTypeEnum.UNFREZEE_FROZEN.getDisplay());
            accountFlowRitMongo.setFlow_status(FlowStatusEnum.FREEZED.getStatus());
            accountFlowRitMongo.setAccount_amount(amount.abs());
            accountFlowRitMongo.setRelation_flow_id(this.flowId);
            accountFlowRitMongo.setBusiness_type_id(BusinessTypeEnum.UNFREZEE_FROZEN.getStatus());
            return accountFlowRitMongo;
        }

        private AccountFlowRitMongo generateRevelAmountFlowRecord() {
            BigDecimal amount = CommonService.setScaleBig(request.getAmount(),retain,BigDecimal.ROUND_DOWN);
            AccountFlowRitMongo accountFlowRitMongo = genBaseFlowModel();
            accountFlowRitMongo.setAccount_flow_id(this.flowId);
            accountFlowRitMongo.setAmount(amount);
            accountFlowRitMongo.setFlow_detail(BusinessTypeEnum.UNFREZEE_BANLANCE.getDisplay());
            accountFlowRitMongo.setFlow_status(FlowStatusEnum.NOSTATUS.getStatus());
            accountFlowRitMongo.setAccount_amount(amount.abs());
            accountFlowRitMongo.setRelation_flow_id(this.flowId);
            accountFlowRitMongo.setBusiness_type_id(BusinessTypeEnum.UNFREZEE_BANLANCE.getStatus());
            return accountFlowRitMongo;
        }

        /**
         * 生成冻结可用余额流水
         */
        private AccountFlowRitMongo generateFreezeAmountFlowRecord() {
            AccountFlowRitMongo accountFlowRitMongo = genBaseFlowModel();
            BigDecimal amount = CommonService.setScaleBig(request.getAmount().multiply(new BigDecimal(-1)),retain,BigDecimal.ROUND_DOWN);
            accountFlowRitMongo.setAccount_flow_id(this.flowId);
            accountFlowRitMongo.setAmount(amount);
            accountFlowRitMongo.setFlow_detail(BusinessTypeEnum.FREZEE_BANLANCE.getDisplay());
            accountFlowRitMongo.setFlow_status(FlowStatusEnum.FREEZED.getStatus());
            accountFlowRitMongo.setAccount_amount(amount.abs());
            accountFlowRitMongo.setRelation_flow_id(this.flowId);
            accountFlowRitMongo.setBusiness_type_id(BusinessTypeEnum.FREZEE_BANLANCE.getStatus());
            return accountFlowRitMongo;
        }

        /**
         * 生成冻结可用余额流水(冻结)
         */
        private AccountFlowRitMongo generateFreezeFlowRecord() {
            AccountFlowRitMongo accountFlowRitMongo = genBaseFlowModel();
            BigDecimal amount = CommonService.setScaleBig(request.getAmount(),retain,BigDecimal.ROUND_DOWN);
            accountFlowRitMongo.setAccount_flow_id(idGenerater.nextId());  //生成新的ID
            accountFlowRitMongo.setFreeze_amount(amount);
            accountFlowRitMongo.setFlow_detail(BusinessTypeEnum.FREZEE_FROZEN.getDisplay());
            accountFlowRitMongo.setFlow_status(FlowStatusEnum.NOSTATUS.getStatus());
            accountFlowRitMongo.setRelation_flow_id(this.flowId);
            accountFlowRitMongo.setBusiness_type_id(BusinessTypeEnum.FREZEE_FROZEN.getStatus());
            accountFlowRitMongo.setAccount_amount(amount.abs());
            return accountFlowRitMongo;
        }

        private AccountFlowRitMongo genBaseFlowModel() {
            AccountFlowRitMongo accountFlowRitMongo = new AccountFlowRitMongo();
            accountFlowRitMongo.setUser_name(userInfo.getUserName());
            accountFlowRitMongo.setRemark(request.getCause());
            accountFlowRitMongo.setFlow_date(new Date().getTime());
            accountFlowRitMongo.setUser_id(Long.parseLong(userInfo.getUserId()));
            accountFlowRitMongo.setUser_phone(userInfo.getUserMobile());
            return accountFlowRitMongo;
        }
    }

    private List<AccountFlowRitMongo> getRitFlowMongoModel(Account account, FreezeInModel request, SysUser sysUser) {
        List<AccountFlowRitMongo> result = new ArrayList<>();
        AccountFlowRitMongo accountFlowRitMongo = new AccountFlowRitMongo();
        Long flowId = idGenerater.nextId();
        accountFlowRitMongo.setAccount_flow_id(flowId);
        accountFlowRitMongo.setRelation_flow_id(flowId);
        accountFlowRitMongo.setUser_id(Long.parseLong(request.getUserId()));
        //如果是冻结
        if (Constants.FREEZE.equals(request.getCallMethod())) {

        } else if (Constants.RELVE.equals(request.getCallMethod())) {

        }
        return null;
    }

    private void recordFreezeLog(SysUser sysUser, FreezeInModel request) {
        KgRitFreezeLog kgRitFreezeLog = new KgRitFreezeLog();
        kgRitFreezeLog.setOperUserId(sysUser.getSysUserId());
        kgRitFreezeLog.setUserId(Long.parseLong(request.getUserId()));
        kgRitFreezeLog.setType(CallMethodEnum.getType(request.getCallMethod()).getType());
        kgRitFreezeLog.setCause(request.getCause());

        kgRitFreezeLogWMapper.insert(kgRitFreezeLog);
    }

    private static final int retain = 8;

    private Account getUpdateModel(Account account, FreezeInModel request) {
        Account updateModel = new Account();
        updateModel.setAccountId(account.getAccountId());
        if (Constants.FREEZE.equals(request.getCallMethod())) {  //冻结
            updateModel.setRitFrozenBalance(CommonService.setScaleBig(account.getRitFrozenBalance().add(request.getAmount()),retain,BigDecimal.ROUND_DOWN));
            updateModel.setRitBalance(CommonService.setScaleBig(account.getRitBalance().subtract(request.getAmount()),retain,BigDecimal.ROUND_DOWN));
        } else if (Constants.RELVE.equals(request.getCallMethod())) {  //解冻
            updateModel.setRitFrozenBalance(CommonService.setScaleBig(account.getRitFrozenBalance().subtract(request.getAmount()),retain,BigDecimal.ROUND_DOWN));
            updateModel.setRitBalance(CommonService.setScaleBig(account.getRitBalance().add(request.getAmount()),retain,BigDecimal.ROUND_DOWN));
        } else {
            logger.warn("【未获取到对应的类型匹配 type：{}】", request.getCallMethod());
        }
        return updateModel;
    }

    /**
     * 检查余额是否足够冻结
     */
    private Boolean invalidBalance(FreezeInModel request, Account account) {
        if (account == null) {
            return false;
        }
        BigDecimal free = request.getAmount();
        //1.3.0版本只支持RIT冻结操作
        if (Constants.FREEZE.equals(request.getCallMethod())) {
            return !(free.compareTo(account.getRitBalance()) <= 0);
        } else if (Constants.RELVE.equals(request.getCallMethod())) {
            return !(free.compareTo(account.getRitFrozenBalance()) <= 0);
        }
        return false;
    }

    @Override
    public JsonEntity withDrawSearchConditions(AccountWithDrawRequest request) {
        logger.info("withDrawSearchConditions入参：{}", JSON.toJSONString(request));
        if (request.getAuditRole() == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        List<Integer> statusList = Lists.newArrayList();
        statusList.add(OperTypeEnum.WITHDRAW_WATING.getStatus());
        statusList.add(OperTypeEnum.WITHDRAW_SUCCESS.getStatus());
        statusList.add(OperTypeEnum.WITHDRAW_FAIL.getStatus());

        OperTypeEnum[] operTypeEnums = OperTypeEnum.values();
        List<WithDrawSearchConditionsResponse> responses = Lists.newArrayList();
        for (OperTypeEnum operTypeEnum : operTypeEnums) {
            if (request.getAuditRole() == 2 && !statusList.contains(operTypeEnum.getStatus())) {
                //财务
                continue;
            }
            WithDrawSearchConditionsResponse response = new WithDrawSearchConditionsResponse();
            response.setStatus(operTypeEnum.getStatus());
            response.setDisplay(operTypeEnum.getDisplay());
            responses.add(response);
        }
        HashedMap map = new HashedMap();
        map.put("withdrawStatus", responses);
        return JsonEntity.makeSuccessJsonEntity(map);
    }

    @Override
    @Transactional
    public JsonEntity auditRitWithdraw(AccountWithDrawRequest request, SysUser sysUser, HttpServletRequest httpServletRequest) {

        long now = System.currentTimeMillis();
        //检验入参
        if (request.getAuditRole() == null || request.getStatus() == null || StringUtils.isBlank(request.getWithdrawFlowId())) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if (StringUtils.isNotBlank(request.getAuditRemark()) && request.getAuditRemark().length() > 500) {
            return JsonEntity.makeExceptionJsonEntity("", "审核备注不能超过500个字符！");
        }
        if (StringUtils.isNotBlank(request.getTxId()) && request.getTxId().length() > 100) {
            return JsonEntity.makeExceptionJsonEntity("", "交易号不能超过100个字符！");
        }
        //检验审核人IP
        checkAuditUserIp(httpServletRequest);
        //审核操作
        return doRitAudit(request, sysUser, now);
    }

    @Override
    public JsonEntity batchAuditRitWithdraw(AccountWithDrawRequest request, SysUser sysUser, HttpServletRequest httpServletRequest) {

        long now = System.currentTimeMillis();
        //检验入参
        if (request.getAuditRole() == null || request.getStatus() == null || StringUtils.isBlank(request.getWithdrawFlowIds())) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if (StringUtils.isNotBlank(request.getAuditRemark()) && request.getAuditRemark().length() > 500) {
            return JsonEntity.makeExceptionJsonEntity("", "审核备注不能超过500个字符！");
        }
        if (StringUtils.isNotBlank(request.getTxId()) && request.getTxId().length() > 100) {
            return JsonEntity.makeExceptionJsonEntity("", "交易号不能超过100个字符！");
        }
        //检验审核人IP
        checkAuditUserIp(httpServletRequest);
        //批量审核
        List<Long> withdrawFlowIdList = JSON.parseArray(request.getWithdrawFlowIds(), Long.class);
        //将所有工单置为正在操作中的状态
        Document filter = new Document();
        filter.append("withdrawFlowId", new Document(Seach.IN.getOperStr(), withdrawFlowIdList));
        int state = MongoUtils.updateByFilter(MongoTables.ACCOUNT_WITHDRAW_FLOW_RIT, filter, new Document("ifOper", 1));
        if (state > 0) {
            //发送批量审核RIT操作的MQ
            BatchAuditRitWithdrawConsumerMqRequest mqRequest = new BatchAuditRitWithdrawConsumerMqRequest();
            mqRequest.setWithdrawFlowIdList(withdrawFlowIdList);
            mqRequest.setRequest(request);
            mqRequest.setSysUser(sysUser);
            mqRequest.setNow(now);
			try {
				mqProduct.sendMessage(batchAuditRitWithdrawMqConfig.getTopic(),batchAuditRitWithdrawMqConfig.getTags(),"",
						URLEncoder.encode(JSON.toJSONString(mqRequest),"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
        logger.info("RIT批量审核------耗时：{}ms",(System.currentTimeMillis()-now));
        return JsonEntity.makeSuccessJsonEntity();
    }

    @Override
	public void doBatchAuditRitWithdraw(List<Long> withdrawFlowIds, AccountWithDrawRequest request, SysUser sysUser, long now) {
        long now1 = System.currentTimeMillis();
        int count = 0;
        int sum = withdrawFlowIds.size();
        for (Long withdrawFlowId : withdrawFlowIds) {
            String sWithdrawFlowId = withdrawFlowId.toString();
            request.setWithdrawFlowId(sWithdrawFlowId);
            JsonEntity jsonEntity = doRitAudit(request, sysUser, now);
            if (jsonEntity.getCode().equals(ExceptionEnum.SUCCESS.getCode())) {
                count++;
            }
        }
        //将所有工单置为正常的状态
        Document filter = new Document();
        filter.append("withdrawFlowId", new Document(Seach.IN.getOperStr(), withdrawFlowIds));
        MongoUtils.updateByFilter(MongoTables.ACCOUNT_WITHDRAW_FLOW_RIT, filter, new Document("ifOper", 0));
        logger.info("RIT批量审核总共{}条，成功审核了{}条------耗时：{}ms", sum, count, (System.currentTimeMillis() - now1));
    }

	@Override
	public JsonEntity updateAsset(AccountRequest request,HttpServletRequest servletRequest) {
    	checkAuditUserIp(servletRequest);
		AccountInModel inModel = new AccountInModel();
		inModel.setUserId(request.getUserId());
		inModel.setBalance(request.getBalance());
//    	CoinEnum coinEnum = CoinEnum.getByCode(request.getCoinType());

    	if(request.getCoinType()==4){
    		//修改用户BTC余额
			int state = accountWMapper.reduceBtcBalance(inModel);
			if(state<=0){
				return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.TRANS_OUT_FAIL);
			}
		}
		return JsonEntity.makeSuccessJsonEntity();
	}

	@Override
    public void excelOfRitWithdrawFlowList(AccountWithDrawRequest request, HttpServletResponse response, HttpServletRequest servletRequest) {
        logger.info("收到生成RIT提现工单EXCEL的请求————————————");
        long start = System.currentTimeMillis();
        //检验token和验签
        checkTokenSign(request.getToken(), servletRequest);

        request.setCurrentPage(1);
        request.setPageSize(REQUEST_SIZE);
        request.setAuditRole(2);//财务
        JsonEntity result = getRitWithdrawList(request, new PageModel<>());

        long result1;
        long result2;
        int pageSize = REQUEST_SIZE;
        long requestCount;
        List<AccountWithdrawFlowRitResponse> responses = Lists.newArrayList();
        if (result.getCode().equals(ExceptionEnum.SUCCESS.getCode())) {
            PageModel<AccountWithdrawFlowRitResponse> pageModel = (PageModel<AccountWithdrawFlowRitResponse>) result.getResponseBody();
            //放入第一页数据
            buildExcelList(result, responses);
            //获取总条数
            long count = pageModel.getTotalNumber();
            if (count >= EXCELLIMIT) {
                //总条数大于导出的总条数限制时
                result1 = EXCELLIMIT / pageSize;
                result2 = EXCELLIMIT % pageSize > 0 ? 1 : 0;
                requestCount = result1 + result2;
                for (int i = 2; i <= requestCount; i++) {
                    request.setCurrentPage(i);
                    buildExcelList(getRitWithdrawList(request, new PageModel<>()), responses);
                }
            } else {
                //没有超过总条数限制时
                result1 = count / pageSize;
                result2 = count % pageSize > 0 ? 1 : 0;
                requestCount = result1 + result2;
                for (int i = 2; i <= requestCount; i++) {
                    int inPageSize = pageSize;
                    if (i == requestCount) {
                        inPageSize = (int) count % pageSize;
                    }
                    request.setCurrentPage(i);
                    request.setPageSize(inPageSize);
                    buildExcelList(getRitWithdrawList(request, new PageModel<>()), responses);
                }
            }
            //生成EXCEL
            try {
                generateExcel(responses, response);
            } catch (IOException e) {
                String requet = JSON.toJSONString(request);
                logger.error("请求为：" + requet + "--------发生异常：" + JSON.toJSONString(e.getStackTrace()));
                logger.error("请求为：" + requet + "--------发生异常：" + e.getMessage());
            }
            logger.info("生成RIT提现工单EXCEL完毕，总共耗时:————————————" + (System.currentTimeMillis() - start) + "MS");
        }
    }


    @Override
    public JsonEntity manualTransfer(AccountWithDrawRequest request, SysUser sysUser, HttpServletRequest servletRequest) {

        //检验审核人IP
        checkAuditUserIp(servletRequest);

        if (StringUtils.isBlank(request.getUserId()) || StringUtils.isBlank(request.getTxAddress()) || request.getTxAmount() == null || request.getCoinType() == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if (request.getTxAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return JsonEntity.makeExceptionJsonEntity("", "请输入大于0的数字，支持小数点后八位");
        }

        CoinEnum coinEnum = CoinEnum.getByCode(request.getCoinType());
        //获取用户资产
        Account account = accountRMapper.selectByUserId(Long.valueOf(request.getUserId()));
        BigDecimal fee = BigDecimal.ZERO;
        //获取用户信息
        UserInModel userInModel = new UserInModel();
        userInModel.setUserId(Long.valueOf(request.getUserId()));
        UserkgOutModel userkgOutModel = userRMapper.selectByUser(userInModel);
        //根据用户角色 获取提现配置
        Integer userRole = userkgOutModel.getUserRole() == 1 ? 0 : 1;
        KgRitRollout kgRitRolloutOutModel = kgRitRolloutRMapper.selectByPrimaryUserType(userRole);

        if (CoinEnum.RIT.equals(coinEnum)) {
            //校验余额是否足够
            if (request.getTxAmount().compareTo(account.getRitBalance()) > 0) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACCOUNT_BALANCE_ERROR);
            }
            //校验是否大于RIT转账上限
            BigDecimal tranLimit = new BigDecimal(propertyLoader.getProperty("common", "manualTransfer.rit.limit"));
            if (request.getTxAmount().compareTo(tranLimit) > 0) {
                return JsonEntity.makeExceptionJsonEntity("", "最多可转出" + tranLimit.stripTrailingZeros().toPlainString() + "RIT");
            }
            //计算手续费
            BigDecimal feePercent = new BigDecimal(kgRitRolloutOutModel.getRate().toString()).divide(new BigDecimal("100"));
            fee = request.getTxAmount().multiply(feePercent);
        }
        //经过重重检验过后 正式进行申请提现操作
        request.setUserId(request.getUserId());
        request.setFee(fee);
        request.setUserRole(userkgOutModel.getUserRole());
        AppJsonEntity appJsonEntity = appAccountService.doWithDraw(request, coinEnum, userkgOutModel, sysUser);
        if (appJsonEntity.getCode().equals(ExceptionEnum.SUCCESS.getCode())) {
            return JsonEntity.makeSuccessJsonEntity(appJsonEntity.getData());
        } else {
            return JsonEntity.makeExceptionJsonEntity(appJsonEntity.getCode(), appJsonEntity.getMessage());
        }
    }

    private void generateExcel(List<AccountWithdrawFlowRitResponse> resultList, HttpServletResponse response) throws IOException {
        List<AccountWithdrawFlowRitExcel> excelList = AccountWithdrawFlowRitExcel.MeaningOfConversion(resultList);
        buryingPointService.writeBrower(excelList, response, EXCEL_NAME, AccountWithdrawFlowRitExcel.class);
    }

    private void buildExcelList(JsonEntity result, List<AccountWithdrawFlowRitResponse> responses) {
        if (result.getCode().equals(ExceptionEnum.SUCCESS.getCode())) {
            PageModel<AccountWithdrawFlowRitResponse> pageModel = (PageModel<AccountWithdrawFlowRitResponse>) result.getResponseBody();
            responses.addAll(pageModel.getData());
        }
    }

    @Override
    public void checkTokenSign(String token, HttpServletRequest servletRequest) {
        String sign = servletRequest.getParameter("sign");
        String data = servletRequest.getParameter("data");
        boolean check = tokenManager.checkToken(tokenManager.getToken(token));
        if (!check) {
            throw new TokenException(ExceptionEnum.TOKENRERROR);
        } else {
            //验签
            try {
                ApiRequestSupport.checkLogin(data, sign, token);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * //RIT审核操作
     *
     * @param request
     * @param sysUser
     * @param now
     * @return
     */
    private JsonEntity doRitAudit(AccountWithDrawRequest request, SysUser sysUser, long now) {
        String key = USER_ADDUSERTIMES_LOCK_KEY + request.getWithdrawFlowId();
        Lock lock = new Lock();
        Long account_flow_id1 = idGen.nextId();
        Long account_flow_id2 = idGen.nextId();
        Long operId = idGen.nextId();
        try {
            lock.lock(key);

            BasicDBObject filter = new BasicDBObject();
            filter.append("withdrawFlowId", Long.valueOf(request.getWithdrawFlowId()));
            //获取工单信息
            AccountWithdrawFlowRit accountWithdrawFlowRit = appAccountService.getByWithdrawFlowId(request.getWithdrawFlowId());
            if (accountWithdrawFlowRit == null) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.NO_DATA);
            }
            String memo = accountWithdrawFlowRit.getMemo();
            accountWithdrawFlowRit.setMemo(request.getAuditRemark());
            //获取工单用户信息
            UserInModel userInModel = new UserInModel();
            userInModel.setUserId(accountWithdrawFlowRit.getUserId());
            UserkgOutModel userkgOutModel = userRMapper.selectByUser(userInModel);
            //获取工单当前状态
            OperTypeEnum operTypeEnum = OperTypeEnum.getBusinessTypeEnum(accountWithdrawFlowRit.getStatus());
            //获取审核人处理的状态
            OperTypeEnum auditOperTypeEnum = OperTypeEnum.getBusinessTypeEnum(request.getStatus());
            Document updateDocument = new Document().append("auditUserId", sysUser.getSysUserId()).append("auditUserName", sysUser.getSysUserName());

            //根据审核角色 判断工单是否符合审核状态
            if (request.getAuditRole() == 1 && OperTypeEnum.WITHDRAW_PENDING.equals(operTypeEnum)) {
                //运营 当工单处于待审核时才能进行审核
                if (OperTypeEnum.WITHDRAW_NO_PASS.equals(auditOperTypeEnum)) {
                    //审核不通过 用户的RIT可用余额增加、冻结余额减少
                    //扣除RIT冻结余额,增加RIT可用余额
                    AccountInModel accountInModel = new AccountInModel();
                    accountInModel.setUserId(accountWithdrawFlowRit.getUserId());
                    accountInModel.setBalance(accountWithdrawFlowRit.getWithdrawAmount());
                    int state = accountWMapper.addRitBalanceReduceRitFrozenBalance(accountInModel);
                    if (state > 0) {
                        //修改工单状态为审核不通过
                        int check = MongoUtils.updateByFilter(MongoTables.ACCOUNT_WITHDRAW_FLOW_RIT, filter, updateDocument.append("status", auditOperTypeEnum.getStatus()).append("operatorAuditRemark", request.getAuditRemark()));
                        if (check > 0) {
                            //写入相关流水和操作日志
                            appAccountService.addRitBalanceReduceRitFrozen(accountWithdrawFlowRit, userkgOutModel, sysUser, account_flow_id1, account_flow_id2, operId, now,
                                    "转出申请不通过。", BusinessTypeEnum.WITHDRAW_NO_PASS, BusinessTypeEnum.WITHDRAW_NO_PASS_FROZEN, FlowStatusEnum.NOPASS, auditOperTypeEnum);
                        } else {
                            throw new BusinessException(ExceptionEnum.SERVICEERROR);
                        }
                    } else {
                        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SERVICEERROR);
                    }
                } else if (OperTypeEnum.WITHDRAW_WATING.equals(auditOperTypeEnum)) {
                    MongoUtils.updateByFilter(MongoTables.ACCOUNT_WITHDRAW_FLOW_RIT, filter, updateDocument.append("status", auditOperTypeEnum.getStatus()).append("operatorAuditRemark", request.getAuditRemark()));
                    //插入操作日志记录
                    addOperRecord(operId, accountWithdrawFlowRit, sysUser, auditOperTypeEnum, now);
                } else {
                    return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
                }
                return JsonEntity.makeSuccessJsonEntity(ExceptionEnum.SUCCESS);
            } else if (request.getAuditRole() == 2 && OperTypeEnum.WITHDRAW_WATING.equals(operTypeEnum)) {
                //财务 当工单处于等待转出时才能进行审核
                if (OperTypeEnum.WITHDRAW_FAIL.equals(auditOperTypeEnum)) {
                    //转出失败
                    //扣除RIT冻结余额,增加RIT可用余额
                    AccountInModel accountInModel = new AccountInModel();
                    accountInModel.setUserId(accountWithdrawFlowRit.getUserId());
                    accountInModel.setBalance(accountWithdrawFlowRit.getWithdrawAmount());
                    int state = accountWMapper.addRitBalanceReduceRitFrozenBalance(accountInModel);
                    if (state > 0) {
                        //修改工单状态为转出失败
                        int check = MongoUtils.updateByFilter(MongoTables.ACCOUNT_WITHDRAW_FLOW_RIT, filter, updateDocument.append("status", auditOperTypeEnum.getStatus()).append("financialAuditRemark", request.getAuditRemark()).append("txId", request.getTxId()));
                        if (check > 0) {
                            //写入相关流水和操作日志
                            accountWithdrawFlowRit.setTxId(request.getTxId());
                            appAccountService.addRitBalanceReduceRitFrozen(accountWithdrawFlowRit, userkgOutModel, sysUser, account_flow_id1, account_flow_id2, operId, now,
                                    "转出申请不通过。", BusinessTypeEnum.WITHDRAW_FAIL, BusinessTypeEnum.WITHDRAW_FAIL_FROZEN, FlowStatusEnum.TRANSFER_FAIL, auditOperTypeEnum);
                        } else {
                            throw new BusinessException(ExceptionEnum.SERVICEERROR);
                        }
                    } else {
                        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SERVICEERROR);
                    }

                } else if (OperTypeEnum.WITHDRAW_SUCCESS.equals(auditOperTypeEnum)) {
                    //转出成功
                    //扣除用户对应冻结余额 将利息增加到平台余额
                    AccountInModel accountInModel = new AccountInModel();
                    accountInModel.setUserId(accountWithdrawFlowRit.getUserId());
                    accountInModel.setBalance(accountWithdrawFlowRit.getWithdrawAmount());
                    int s1 = accountWMapper.reduceRitFrozenBalance(accountInModel);
                    if (s1 > 0) {
                        accountInModel.setUserId(Constants.PLATFORM_USER_ID);
                        accountInModel.setBalance(accountWithdrawFlowRit.getPoundageAmount());
                        int s2 = accountWMapper.addRitBalance(accountInModel);
                        if (s2 > 0) {
                            //更改工单状态为转出成功 以及交易号和审批备注
                            int check = MongoUtils.updateByFilter(MongoTables.ACCOUNT_WITHDRAW_FLOW_RIT, filter, updateDocument.append("status", auditOperTypeEnum.getStatus()).append("financialAuditRemark", request.getAuditRemark()).append("txId", request.getTxId()).append("accountTime", now));
                            if (check > 0) {
                                //增加用户和平台的流水记录 以及操作日志
                                //平台可用余额增加的流水
                                List<Document> documents = Lists.newArrayList();
                                UserInModel platUser = new UserInModel();
                                platUser.setUserId(Constants.PLATFORM_USER_ID);
                                UserkgOutModel platUserOutModel = userRMapper.selectByUser(platUser);
                                AccountFlowRitMongo accountFlowRitMongo_balance = new AccountFlowRitMongo();
                                accountFlowRitMongo_balance.setAccount_flow_id(account_flow_id1);
                                accountFlowRitMongo_balance.setRelation_flow_id(accountWithdrawFlowRit.getWithdrawFlowId());
                                accountFlowRitMongo_balance.setUser_id(Long.valueOf(platUserOutModel.getUserId()));
                                accountFlowRitMongo_balance.setUser_name(platUserOutModel.getUserName());
                                accountFlowRitMongo_balance.setUser_phone(platUserOutModel.getUserMobile());
                                accountFlowRitMongo_balance.setUser_email(platUserOutModel.getUserEmail());
                                accountFlowRitMongo_balance.setAmount(accountWithdrawFlowRit.getPoundageAmount());
                                accountFlowRitMongo_balance.setBusiness_type_id(BusinessTypeEnum.WITHDRAW_FEE.getStatus());
                                accountFlowRitMongo_balance.setTx_address(accountWithdrawFlowRit.getToAddress());
                                accountFlowRitMongo_balance.setAccount_amount(accountWithdrawFlowRit.getPoundageAmount());
                                accountFlowRitMongo_balance.setPoundage_amount(BigDecimal.ZERO);
                                accountFlowRitMongo_balance.setFlow_status(FlowStatusEnum.TRANSFER_SUCCESS.getStatus());
                                accountFlowRitMongo_balance.setFlow_date(now);
                                accountFlowRitMongo_balance.setFlow_detail(accountWithdrawFlowRit.getUserId() + "成功转出收取手续费");
                                accountFlowRitMongo_balance.setRemark(accountWithdrawFlowRit.getMemo());
                                //冻结余额减少流水
                                AccountFlowRitMongo accountFlowRitMongo_fozen = new AccountFlowRitMongo();
                                BeanUtils.copyProperties(accountFlowRitMongo_balance, accountFlowRitMongo_fozen);
                                accountFlowRitMongo_fozen.setAccount_amount(accountWithdrawFlowRit.getAccountAmount());
                                accountFlowRitMongo_fozen.setPoundage_amount(accountWithdrawFlowRit.getPoundageAmount());
                                accountFlowRitMongo_fozen.setUser_id(accountWithdrawFlowRit.getUserId());
                                accountFlowRitMongo_fozen.setUser_name(userkgOutModel.getUserName());
                                accountFlowRitMongo_fozen.setUser_phone(userkgOutModel.getUserMobile());
                                accountFlowRitMongo_fozen.setUser_email(userkgOutModel.getUserEmail());
                                accountFlowRitMongo_fozen.setAccount_flow_id(account_flow_id2);
                                accountFlowRitMongo_fozen.setAmount(new BigDecimal("0.00000000"));
                                accountFlowRitMongo_fozen.setBusiness_type_id(BusinessTypeEnum.WITHDRAW_SUCCESS.getStatus());
                                accountFlowRitMongo_fozen.setFreeze_amount(accountWithdrawFlowRit.getWithdrawAmount().negate());
                                accountFlowRitMongo_fozen.setFlow_detail(memo);

                                documents.add(new Document(Bean2MapUtil.bean2map(accountFlowRitMongo_balance)));
                                documents.add(new Document(Bean2MapUtil.bean2map(accountFlowRitMongo_fozen)));
                                MongoUtils.insertMany(MongoTables.ACCOUNT_FLOW_RIT, documents);
                                //插入操作日志记录
                                accountWithdrawFlowRit.setTxId(request.getTxId());
                                addOperRecord(operId, accountWithdrawFlowRit, sysUser, auditOperTypeEnum, now);
                            } else {
                                logger.error("审核RIT提现修改工单状态失败------");
                                throw new BusinessException(ExceptionEnum.SERVICEERROR);
                            }
                        } else {
                            logger.error("审核RIT提现增加平台余额失败------");
                            throw new BusinessException(ExceptionEnum.SERVICEERROR);
                        }
                    } else {
                        logger.error("审核RIT提现扣除冻结余额失败------");
                        throw new BusinessException(ExceptionEnum.SERVICEERROR);
                    }

                } else {
                    return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
                }
                return JsonEntity.makeSuccessJsonEntity(ExceptionEnum.SUCCESS);
            } else {
                return JsonEntity.makeExceptionJsonEntity("", "该工单已被审核，请刷新页面。");
            }

        } catch (Exception e) {
            //发生异常时，删除MONGO中的记录
            logger.error("审核RIT提现发生异常：{},___异常时请求参数：{}", JSON.toJSONString(e.getStackTrace()), JSON.toJSONString(request));
            logger.error("审核RIT提现发生异常：{},___异常时请求参数：{}", e.getMessage(), JSON.toJSONString(request));
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

    private void addOperRecord(Long operId, AccountWithdrawFlowRit accountWithdrawFlowRit, SysUser sysUser, OperTypeEnum auditOperTypeEnum, long now) {
        WithdrawOperRecord withdrawOperRecord = new WithdrawOperRecord();
        withdrawOperRecord.setOperId(operId);
        withdrawOperRecord.setWithdrawFlowId(accountWithdrawFlowRit.getWithdrawFlowId());
        withdrawOperRecord.setOperUserId(sysUser.getSysUserId());
        withdrawOperRecord.setOperUserName(sysUser.getSysUserName());
        withdrawOperRecord.setOperType(auditOperTypeEnum.getStatus());
        withdrawOperRecord.setOperTypeName(auditOperTypeEnum.getDisplay());
        withdrawOperRecord.setTxId(accountWithdrawFlowRit.getTxId());
        withdrawOperRecord.setOperDate(now);
        withdrawOperRecord.setRemark(accountWithdrawFlowRit.getMemo());
        MongoUtils.insertOne(MongoTables.WITHDRAW_OPER_RECORD, new Document(Bean2MapUtil.bean2map(withdrawOperRecord)));
    }

    public void checkAuditUserIp(HttpServletRequest httpServletRequest) {
        //校验审核人IP是否属于可审核的IP
        Result<SiteinfoResponse> siteinfoResponse = siteinfoService.selectSiteInfo();
        CheckUtils.checkRetInfo(siteinfoResponse, true);

        SiteinfoResponse siteInfo = siteinfoResponse.getData();
        if (!Check.NuNString(siteInfo.getLimitIp())) {
            String userIp = HttpUtil.getIpAddr(httpServletRequest);
            logger.info("====审核提现来源ip" + userIp);

            if (siteInfo.getLimitIp().indexOf(userIp) == -1) {
                throw new ApiMessageException(ExceptionEnum.ADMINIPERROR);
            }
        }
    }

    private RitExchangeQueryResponse getRitExchangeQueryResponse(Document document) {
        RitExchangeQueryResponse response = new RitExchangeQueryResponse();
        response.setId(String.valueOf(document.getLong("id")));
        response.setUserId(String.valueOf(document.getLong("userId")));
        response.setUserName(document.getString("userName"));
        response.setUserPhone(document.getString("userPhone"));
        response.setUserRole(document.getInteger("userRole"));
        response.setRitAmount(document.get("ritAmount").toString());
        response.setKgAmount(document.get("kgAmount").toString());
        response.setCreateTime(String.valueOf(document.getLong("createTime")));
        return response;
    }

    private PageModel<RitExchangeQueryResponse> buildPageModelForGetRitExchangeList(RitExchangeQueryRequest request, List<RitExchangeQueryResponse> responsesList, long count) {
        PageModel<RitExchangeQueryResponse> pageModel = new PageModel<>();
        pageModel.setCurrentPage(request.getCurrentPage());
        pageModel.setPageSize(request.getPageSize());
        pageModel.setData(responsesList);
        pageModel.setTotalNumber(count);
        return pageModel;
    }

    /**
     * 构建RIT查询参数
     *
     * @param request
     * @return
     */
    private BasicDBObject buildQueryParamsForgetRitExchangeList(RitExchangeQueryRequest request) {
        BasicDBObject basicDBObject = new BasicDBObject();
        String userName = request.getUserName();
        if (StringUtils.isNotEmpty(userName)) {
            Pattern pattern = Pattern.compile(userName, Pattern.CASE_INSENSITIVE);
            basicDBObject.put("userName", pattern);
        }
        String userphone = request.getUserPhone();
        if (StringUtils.isNotEmpty(userphone)) {
            Pattern patternPhone = Pattern.compile(userphone, Pattern.CASE_INSENSITIVE);
            basicDBObject.put("userPhone", patternPhone);
        }
        Integer userRole = request.getUserRole();
        if (userRole != null) {
            basicDBObject.put("userRole", userRole);
        }

        basicDBObject = buildRitAmountQueryParams(request, basicDBObject);
        basicDBObject = buildKgAmountQueryParams(request, basicDBObject);
        basicDBObject = buildTimeQueryParams(request, basicDBObject);

        return basicDBObject;
    }

    /**
     * 构建时间查询参数
     *
     * @param request
     * @param basicDBObject
     * @return
     */
    private BasicDBObject buildTimeQueryParams(RitExchangeQueryRequest request, BasicDBObject basicDBObject) {
        String startTime = request.getExchangeStartTime();
        String endTime = request.getExchangeEndTime();
        if (StringUtils.isNotEmpty(startTime) && StringUtils.isEmpty(endTime)) {
            Date start = DateUtils.parseDate(startTime, DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
            if (start != null) {
                basicDBObject.put("createTime", new BasicDBObject(Seach.GTE.getOperStr(), start.getTime()));
            }
        }
        if (StringUtils.isNotEmpty(endTime) && StringUtils.isEmpty(startTime)) {
            Date end = DateUtils.parseDate(endTime, DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
            if (end != null) {
                basicDBObject.put("createTime", new BasicDBObject(Seach.LTE.getOperStr(), end.getTime()));
            }
        }
        if (StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
            Date start = DateUtils.parseDate(startTime, DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
            Date end = DateUtils.parseDate(endTime, DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
            if (start != null && end != null) {
                basicDBObject.put("createTime", new BasicDBObject(Seach.GTE.getOperStr(), start.getTime()).append(Seach.LTE.getOperStr(), end.getTime()));
            }
        }
        return basicDBObject;
    }

    /**
     * 构建kg金额查询参数
     *
     * @param request
     * @param basicDBObject
     * @return
     */
    private BasicDBObject buildKgAmountQueryParams(RitExchangeQueryRequest request, BasicDBObject basicDBObject) {
        String kgAmountMin = request.getKgAmountmMin();
        String kgAmountMax = request.getKgAmountMax();
        if (StringUtils.isNotEmpty(kgAmountMin) && StringUtils.isEmpty(kgAmountMax)) {
            BigDecimal kgMin = new BigDecimal(kgAmountMin);
            basicDBObject.put("kgAmount", new BasicDBObject(Seach.GTE.getOperStr(), kgMin));
        }
        if (StringUtils.isNotEmpty(kgAmountMax) && StringUtils.isEmpty(kgAmountMin)) {
            BigDecimal kgMax = new BigDecimal(kgAmountMax);
            basicDBObject.put("kgAmount", new BasicDBObject(Seach.LTE.getOperStr(), kgMax));
        }
        if (StringUtils.isNotEmpty(kgAmountMin) && StringUtils.isNotEmpty(kgAmountMax)) {
            BigDecimal kgMin = new BigDecimal(kgAmountMin);
            BigDecimal kgMax = new BigDecimal(kgAmountMax);
            basicDBObject.put("kgAmount", new BasicDBObject(Seach.GTE.getOperStr(), kgMin).append(Seach.LTE.getOperStr(), kgMax));
        }
        return basicDBObject;
    }

    /**
     * 构建rit金额查询参数
     *
     * @param request
     * @return
     */
    private BasicDBObject buildRitAmountQueryParams(RitExchangeQueryRequest request, BasicDBObject basicDBObject) {
        String ritAmountMin = request.getRitAmountmMin();
        String ritAmountMax = request.getRitAmountMax();
        if (StringUtils.isNotEmpty(ritAmountMin) && StringUtils.isEmpty(ritAmountMax)) {
            BigDecimal ritMin = new BigDecimal(ritAmountMin);
            basicDBObject.put("ritAmount", new BasicDBObject(Seach.GTE.getOperStr(), ritMin));
        }
        if (StringUtils.isNotEmpty(ritAmountMax) && StringUtils.isEmpty(ritAmountMin)) {
            BigDecimal ritMax = new BigDecimal(ritAmountMax);
            basicDBObject.put("ritAmount", new BasicDBObject(Seach.LTE.getOperStr(), ritMax));
        }
        if (StringUtils.isNotEmpty(ritAmountMax) && StringUtils.isNotEmpty(ritAmountMin)) {
            BigDecimal ritMin = new BigDecimal(ritAmountMin);
            BigDecimal ritMax = new BigDecimal(ritAmountMax);
            basicDBObject.put("ritAmount", new BasicDBObject(Seach.GTE.getOperStr(), ritMin).append(Seach.LTE.getOperStr(), ritMax));
        }
        return basicDBObject;
    }

    private List<Bson> getDBObjectGroup(RitExchangeQueryRequest request) {
        List<Bson> ops = new ArrayList<>();
        DBObject query = buildQueryParamsForgetRitExchangeList(request);
        Bson match = new BasicDBObject("$match", query);
        ops.add(match);
        Bson group = new BasicDBObject("$group",
                new BasicDBObject("_id", null)
                        .append("totalRitAmount", new BasicDBObject("$sum", "$ritAmount"))
                        .append("totalKgAmount", new BasicDBObject("$sum", "$kgAmount")));
        ops.add(group);
        return ops;
    }
}
