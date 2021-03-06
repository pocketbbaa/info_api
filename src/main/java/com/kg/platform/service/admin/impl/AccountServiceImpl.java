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

    private static String EXCEL_NAME = "RIT????????????-??????.csv";

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
                    item.setStatusDisplay("?????????");
                } else {
                    item.setStatusDisplay("????????????");
                }
                item.setLevelDisplay("??????");
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
                item.setLevelDisplay("??????");
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
        logger.info(">>>>>>>kg????????????>>>>." + searchQuery.toString());

        BasicDBObject sort = new BasicDBObject();
        sort.put("flow_date", -1);
        logger.info(">>>>>>>kg????????????>>>>." + sort.toString());

        MongoCursor<Document> cursor = MongoUtils.findByPage(MongoTables.ACCOUNT_FLOW_KG, searchQuery, sort, request.getCurrentPage(), request.getPageSize());
        if (cursor != null) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                System.out.println(">>>>>>rit????????????>>>>>>>" + JsonUtil.writeValueAsString(document));
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
//                item.setLevelDisplay("??????");
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
        //????????????
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
        //????????????
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
        //??????id
        if (request.getFlowId() != null) {
            searchQuery.append("account_flow_id", request.getFlowId());
        }
        //??????
        if (request.getNickName() != null) {
            Pattern queryPattern = Pattern.compile(request.getNickName(), Pattern.CASE_INSENSITIVE);
            searchQuery.append("user_name", queryPattern);
        }
        //????????????
        if (request.getBusinessTypeId() != null) {
            searchQuery.append("business_type_id", new BasicDBObject(Seach.EQ.getOperStr(), request.getBusinessTypeId()));
        }
        //?????????????????????????????????????????????
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
        accountQueryResponse.setLevelDisplay("??????");
        accountQueryResponse.setFlowDetail(document.get("flow_detail") == null ? null : document.getString("flow_detail"));
        if(new BigDecimal(amount).compareTo(BigDecimal.ZERO)==0){
        	accountQueryResponse.setAmount(freezeAmount);
		}
        //????????????????????????
        Integer flowStatus = document.get("flow_status") == null ? null : document.getInteger("flow_status");
        if (flowStatus != null) {
            accountQueryResponse.setStatus(FlowStatusEnum.getFlowStatusEnum(flowStatus).getDisplay());
        }
        //????????????????????????
        if (accountQueryResponse.getBusinessTypeId() != null) {
            accountQueryResponse.setBusinessTypeName(BusinessTypeEnum.getBusinessTypeEnum(accountQueryResponse.getBusinessTypeId()).getDisplay());
        }
        //??????????????????
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
        logger.info(">>>>>>>rit????????????>>>>." + searchQuery.toString());

        BasicDBObject sort = new BasicDBObject();
        sort.put("flow_date", -1);
        logger.info(">>>>>>>rit????????????>>>>." + sort.toString());

        MongoCursor<Document> cursor = MongoUtils.findByPage(MongoTables.ACCOUNT_FLOW_RIT, searchQuery, sort, request.getCurrentPage(), request.getPageSize());
        if (cursor != null) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                System.out.println(">>>>>>rit????????????>>>>>>>" + JsonUtil.writeValueAsString(document));
                AccountQueryResponse accountQueryResponse = this.getAccountQueryResponse(document);
                data.add(accountQueryResponse);
            }
        }
        //long count= = adminAccountRMapper.selectRitCount(request);
        //???????????????
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
        //????????????
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
        //????????????
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
        //??????id
        if (request.getFlowId() != null) {
            searchQuery.append("account_flow_id", request.getFlowId());
        }
        //??????
        if (request.getNickName() != null) {
            Pattern queryPattern = Pattern.compile(request.getNickName(), Pattern.CASE_INSENSITIVE);
            searchQuery.append("user_name", queryPattern);
        }
        //?????????????????????????????????????????????
        if (request.getMobile() != null || request.getSearchType() != null) {
            Pattern queryPattern = Pattern.compile(request.getMobile(), Pattern.CASE_INSENSITIVE);
            searchQuery.append("user_phone", queryPattern);
        }
        if (request.getSearchType() != null) {
            int typeId[] = {2000, 2100, 2102, 2104, 2003, 2004, 2005};
            searchQuery.append("business_type_id", new BasicDBObject(Seach.IN.getOperStr(), typeId));
        }
        //????????????
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
        accountQueryResponse.setLevelDisplay("??????");
        accountQueryResponse.setFlowDetail(document.get("flow_detail") == null ? null : document.getString("flow_detail"));
        //????????????????????????
        Integer flowStatus = document.get("flow_status") == null ? null : document.getInteger("flow_status");
        if (flowStatus != null) {
            accountQueryResponse.setStatus(FlowStatusEnum.getFlowStatusEnum(flowStatus).getDisplay());
        }
        //????????????????????????
        if (accountQueryResponse.getBusinessTypeId() != null) {
            accountQueryResponse.setBusinessTypeName(BusinessTypeEnum.getBusinessTypeEnum(accountQueryResponse.getBusinessTypeId()).getDisplay());
        }
        //??????????????????
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
                item.setLevelDisplay("??????");
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
            // request.setRefuseReason("??????");
            // }
            // adminAccountWithdrawRMapper.auditAccountWithdraw(request);

        } else if (request.getStatus() == 2) {

            adminAccountWithdrawRMapper.auditAccountWithdraw(request);
            // ?????????????????????????????????????????????
            Account account = accountRMapper.selectByUserId(flow.getUserId());
            account.setBalance(account.getBalance().add(flow.getWithdrawAmount()));
            account.setFrozenBalance(account.getFrozenBalance().subtract(flow.getWithdrawAmount()));
            accountWMapper.updateByPrimaryKeySelective(account);
            // ????????????????????????
            AccountFlowInModel model = new AccountFlowInModel();
            model.setAccountAmount(flow.getWithdrawAmount());
            model.setAccountDate(new Date());
            model.setFlowDate(new Date());
            model.setFlowStatus(1);
            model.setAccountFlowId(generater.nextId());
            model.setAmount(flow.getWithdrawAmount());
            // model.setPoundageAmount(flow.getPoundageAmount());
            model.setFlowDetail("???????????? ?????????");
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
                item.setLevelDisplay("??????");
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

        // ??????????????????/?????? rit????????????
        TotalBonusQueryResponse totalBonus = adminAccountRMapper.sumUserBonus(request);

        totalBonusStr += totalBonus.getTotalTv() + "TV  ";

        //????????????
        List<Bson> condition = this.getUserTotalBonusQuerry(request, 1);
        MongoCursor<Document> cursor = MongoUtils.aggregate(MongoTables.ACCOUNT_FLOW_KG, condition);
        logger.info(">>>>>>>??????kg????????????>>>>>>>>>" + condition.toString());
        totalBonusStr += this.buiidTotalBonus(cursor) + "KG  ";

        //??????rit
        condition = this.getUserTotalBonusQuerry(request, 2);
        logger.info(">>>>>>>??????Rit????????????>>>>>>>>>" + condition.toString());
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
     * @param type    1?????? 2rit
     * @return
     */
    private List<Bson> getUserTotalBonusQuerry(AccountQueryRequest request, int type) {
        List<Bson> condition = new ArrayList<>();
        //????????????
        BasicDBObject querryBy = new BasicDBObject();
        querryBy.append("user_phone", new BasicDBObject(Seach.EQ.getOperStr(), request.getMobile()));
        if (type == 1) {
            //??????????????????
            int typeId[]={1000,1510,1520,1530,1560,1570};
            querryBy.append("business_type_id", new BasicDBObject(Seach.IN.getOperStr(), typeId));
        }else  if(type==2){
            //rit????????????
            int typeId[]={2000,2100,2102};
            querryBy.append("business_type_id",new BasicDBObject(Seach.IN.getOperStr(),typeId));
        }
        BasicDBObject querry = new BasicDBObject("$match", querryBy);
        condition.add(querry);

        //????????????
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
        logger.info("????????????RIT??????????????????------request:{}", JSON.toJSONString(request));
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
            //??????
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
            //???????????? ???????????????????????????????????????????????????????????????????????????????????????
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

        //????????????
        long count = MongoUtils.count(MongoTables.ACCOUNT_WITHDRAW_FLOW_RIT, filter);
        pageModel.setTotalNumber(count);
        logger.info("????????????RIT??????????????????------??????:{}ms", (System.currentTimeMillis() - now));
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
        //??????????????????
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

        //???????????????????????????
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
        logger.info("RIT???????????????????????????{}ms", (System.currentTimeMillis() - now));
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
        //?????????????????????????????????????????????
        if(commonService.judgeRitRollout(Long.parseLong(request.getUserId())) && request.getCallMethod().equals(Constants.RELVE)){
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.GOING_WORK_ORDER);
        }
        Account account = accountRMapper.selectByUserId(Long.parseLong(request.getUserId()));
        //??????????????????????????????/??????
        if (invalidBalance(request, account)) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACCOUNT_BALANCE_ERROR);
        }
        Account updateModel = getUpdateModel(account, request);
        try {
            accountWMapper.updateByPrimaryKeySelective(updateModel);
            //??????????????????
            recordParticulars(request);
            recordFreezeLog(sysUser, request);
        } catch (Exception e) {
            logger.error("?????????/??????????????????????????????????????? e:{}???", e.getMessage());
            throw new RuntimeException("???type:{" + request.getCallMethod() + "} ???????????????");
        }
        return JsonEntity.makeSuccessJsonEntity();
    }

    private void recordParticulars(FreezeInModel request) {
        //?????????????????????????????????
        Account account = accountRMapper.selectByUserId(Long.parseLong(request.getUserId()));
        if (account == null) {
            logger.error("??????????????????????????????,?????????????????????????????????,???????????? user_id:{}???", request.getUserId());
            throw new RuntimeException("???????????????,???????????? ????????????:{?????????????????????????????? userId-->" + request.getUserId() + "}");
        }
        //??????????????????
        UserkgOutModel userModel = userRMapper.selectByPrimaryKey(Long.parseLong(request.getUserId()));
        if (userModel == null) {
            throw new RuntimeException("???????????????,???????????? ????????????:{?????????????????????????????? userId-->" + request.getUserId() + "}");
        }
        List<AccountFlowRitMongo> ritMongoModels = new RitAssectFlowRecord(1, userModel, account)
                .setFreezeInModel(request)
                .invoke()
                .getRitFlowList();
        if (ritMongoModels == null || ritMongoModels.isEmpty()) {
            throw new RuntimeException("???????????????,???????????? ????????????:{????????????????????????????????? userId-->" + request.getUserId() + "}");
        }
        List<Document> insertData = transDocument(ritMongoModels);
        try {
            MongoUtils.insertMany(MongoTables.ACCOUNT_FLOW_RIT, insertData);
        } catch (Exception e) {
            logger.error("??????????????????,???????????? ????????????:{?????????????????????mongo??????????????? ????????????e:{" + e.getMessage() + "}}???");
            throw new RuntimeException("???????????????,???????????? ????????????:????????????:{?????????????????????mongo??????????????? ????????????e:{" + e.getMessage() + "}}");
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
                    logger.error("?????????????????? type:{" + type + "}???");
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
                    logger.warn("????????????callMethod:{" + this.callMethod + "}???");
            }
        }

        private void generateRevelFlowRecord() {
            //??????????????????(????????????????????????)
            ritFlowList.add(generateRevelAmountFlowRecord());
            //?????????????????? (??????????????????)
            ritFlowList.add(generateRevelFreezeFlowRecord());
        }

        private void generateFreezeFlowRecordDetail() {
            //?????????????????? (????????????????????????)
            ritFlowList.add(generateFreezeAmountFlowRecord());
            //??????????????????(??????????????????)
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
         * ??????????????????????????????
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
         * ??????????????????????????????(??????)
         */
        private AccountFlowRitMongo generateFreezeFlowRecord() {
            AccountFlowRitMongo accountFlowRitMongo = genBaseFlowModel();
            BigDecimal amount = CommonService.setScaleBig(request.getAmount(),retain,BigDecimal.ROUND_DOWN);
            accountFlowRitMongo.setAccount_flow_id(idGenerater.nextId());  //????????????ID
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
        //???????????????
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
        if (Constants.FREEZE.equals(request.getCallMethod())) {  //??????
            updateModel.setRitFrozenBalance(CommonService.setScaleBig(account.getRitFrozenBalance().add(request.getAmount()),retain,BigDecimal.ROUND_DOWN));
            updateModel.setRitBalance(CommonService.setScaleBig(account.getRitBalance().subtract(request.getAmount()),retain,BigDecimal.ROUND_DOWN));
        } else if (Constants.RELVE.equals(request.getCallMethod())) {  //??????
            updateModel.setRitFrozenBalance(CommonService.setScaleBig(account.getRitFrozenBalance().subtract(request.getAmount()),retain,BigDecimal.ROUND_DOWN));
            updateModel.setRitBalance(CommonService.setScaleBig(account.getRitBalance().add(request.getAmount()),retain,BigDecimal.ROUND_DOWN));
        } else {
            logger.warn("???????????????????????????????????? type???{}???", request.getCallMethod());
        }
        return updateModel;
    }

    /**
     * ??????????????????????????????
     */
    private Boolean invalidBalance(FreezeInModel request, Account account) {
        if (account == null) {
            return false;
        }
        BigDecimal free = request.getAmount();
        //1.3.0???????????????RIT????????????
        if (Constants.FREEZE.equals(request.getCallMethod())) {
            return !(free.compareTo(account.getRitBalance()) <= 0);
        } else if (Constants.RELVE.equals(request.getCallMethod())) {
            return !(free.compareTo(account.getRitFrozenBalance()) <= 0);
        }
        return false;
    }

    @Override
    public JsonEntity withDrawSearchConditions(AccountWithDrawRequest request) {
        logger.info("withDrawSearchConditions?????????{}", JSON.toJSONString(request));
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
                //??????
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
        //????????????
        if (request.getAuditRole() == null || request.getStatus() == null || StringUtils.isBlank(request.getWithdrawFlowId())) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if (StringUtils.isNotBlank(request.getAuditRemark()) && request.getAuditRemark().length() > 500) {
            return JsonEntity.makeExceptionJsonEntity("", "????????????????????????500????????????");
        }
        if (StringUtils.isNotBlank(request.getTxId()) && request.getTxId().length() > 100) {
            return JsonEntity.makeExceptionJsonEntity("", "?????????????????????100????????????");
        }
        //???????????????IP
        checkAuditUserIp(httpServletRequest);
        //????????????
        return doRitAudit(request, sysUser, now);
    }

    @Override
    public JsonEntity batchAuditRitWithdraw(AccountWithDrawRequest request, SysUser sysUser, HttpServletRequest httpServletRequest) {

        long now = System.currentTimeMillis();
        //????????????
        if (request.getAuditRole() == null || request.getStatus() == null || StringUtils.isBlank(request.getWithdrawFlowIds())) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if (StringUtils.isNotBlank(request.getAuditRemark()) && request.getAuditRemark().length() > 500) {
            return JsonEntity.makeExceptionJsonEntity("", "????????????????????????500????????????");
        }
        if (StringUtils.isNotBlank(request.getTxId()) && request.getTxId().length() > 100) {
            return JsonEntity.makeExceptionJsonEntity("", "?????????????????????100????????????");
        }
        //???????????????IP
        checkAuditUserIp(httpServletRequest);
        //????????????
        List<Long> withdrawFlowIdList = JSON.parseArray(request.getWithdrawFlowIds(), Long.class);
        //?????????????????????????????????????????????
        Document filter = new Document();
        filter.append("withdrawFlowId", new Document(Seach.IN.getOperStr(), withdrawFlowIdList));
        int state = MongoUtils.updateByFilter(MongoTables.ACCOUNT_WITHDRAW_FLOW_RIT, filter, new Document("ifOper", 1));
        if (state > 0) {
            //??????????????????RIT?????????MQ
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
        logger.info("RIT????????????------?????????{}ms",(System.currentTimeMillis()-now));
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
        //????????????????????????????????????
        Document filter = new Document();
        filter.append("withdrawFlowId", new Document(Seach.IN.getOperStr(), withdrawFlowIds));
        MongoUtils.updateByFilter(MongoTables.ACCOUNT_WITHDRAW_FLOW_RIT, filter, new Document("ifOper", 0));
        logger.info("RIT??????????????????{}?????????????????????{}???------?????????{}ms", sum, count, (System.currentTimeMillis() - now1));
    }

	@Override
	public JsonEntity updateAsset(AccountRequest request,HttpServletRequest servletRequest) {
    	checkAuditUserIp(servletRequest);
		AccountInModel inModel = new AccountInModel();
		inModel.setUserId(request.getUserId());
		inModel.setBalance(request.getBalance());
//    	CoinEnum coinEnum = CoinEnum.getByCode(request.getCoinType());

    	if(request.getCoinType()==4){
    		//????????????BTC??????
			int state = accountWMapper.reduceBtcBalance(inModel);
			if(state<=0){
				return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.TRANS_OUT_FAIL);
			}
		}
		return JsonEntity.makeSuccessJsonEntity();
	}

	@Override
    public void excelOfRitWithdrawFlowList(AccountWithDrawRequest request, HttpServletResponse response, HttpServletRequest servletRequest) {
        logger.info("????????????RIT????????????EXCEL?????????????????????????????????????????????");
        long start = System.currentTimeMillis();
        //??????token?????????
        checkTokenSign(request.getToken(), servletRequest);

        request.setCurrentPage(1);
        request.setPageSize(REQUEST_SIZE);
        request.setAuditRole(2);//??????
        JsonEntity result = getRitWithdrawList(request, new PageModel<>());

        long result1;
        long result2;
        int pageSize = REQUEST_SIZE;
        long requestCount;
        List<AccountWithdrawFlowRitResponse> responses = Lists.newArrayList();
        if (result.getCode().equals(ExceptionEnum.SUCCESS.getCode())) {
            PageModel<AccountWithdrawFlowRitResponse> pageModel = (PageModel<AccountWithdrawFlowRitResponse>) result.getResponseBody();
            //?????????????????????
            buildExcelList(result, responses);
            //???????????????
            long count = pageModel.getTotalNumber();
            if (count >= EXCELLIMIT) {
                //??????????????????????????????????????????
                result1 = EXCELLIMIT / pageSize;
                result2 = EXCELLIMIT % pageSize > 0 ? 1 : 0;
                requestCount = result1 + result2;
                for (int i = 2; i <= requestCount; i++) {
                    request.setCurrentPage(i);
                    buildExcelList(getRitWithdrawList(request, new PageModel<>()), responses);
                }
            } else {
                //??????????????????????????????
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
            //??????EXCEL
            try {
                generateExcel(responses, response);
            } catch (IOException e) {
                String requet = JSON.toJSONString(request);
                logger.error("????????????" + requet + "--------???????????????" + JSON.toJSONString(e.getStackTrace()));
                logger.error("????????????" + requet + "--------???????????????" + e.getMessage());
            }
            logger.info("??????RIT????????????EXCEL?????????????????????:????????????????????????????????????" + (System.currentTimeMillis() - start) + "MS");
        }
    }


    @Override
    public JsonEntity manualTransfer(AccountWithDrawRequest request, SysUser sysUser, HttpServletRequest servletRequest) {

        //???????????????IP
        checkAuditUserIp(servletRequest);

        if (StringUtils.isBlank(request.getUserId()) || StringUtils.isBlank(request.getTxAddress()) || request.getTxAmount() == null || request.getCoinType() == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if (request.getTxAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return JsonEntity.makeExceptionJsonEntity("", "???????????????0????????????????????????????????????");
        }

        CoinEnum coinEnum = CoinEnum.getByCode(request.getCoinType());
        //??????????????????
        Account account = accountRMapper.selectByUserId(Long.valueOf(request.getUserId()));
        BigDecimal fee = BigDecimal.ZERO;
        //??????????????????
        UserInModel userInModel = new UserInModel();
        userInModel.setUserId(Long.valueOf(request.getUserId()));
        UserkgOutModel userkgOutModel = userRMapper.selectByUser(userInModel);
        //?????????????????? ??????????????????
        Integer userRole = userkgOutModel.getUserRole() == 1 ? 0 : 1;
        KgRitRollout kgRitRolloutOutModel = kgRitRolloutRMapper.selectByPrimaryUserType(userRole);

        if (CoinEnum.RIT.equals(coinEnum)) {
            //????????????????????????
            if (request.getTxAmount().compareTo(account.getRitBalance()) > 0) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACCOUNT_BALANCE_ERROR);
            }
            //??????????????????RIT????????????
            BigDecimal tranLimit = new BigDecimal(propertyLoader.getProperty("common", "manualTransfer.rit.limit"));
            if (request.getTxAmount().compareTo(tranLimit) > 0) {
                return JsonEntity.makeExceptionJsonEntity("", "???????????????" + tranLimit.stripTrailingZeros().toPlainString() + "RIT");
            }
            //???????????????
            BigDecimal feePercent = new BigDecimal(kgRitRolloutOutModel.getRate().toString()).divide(new BigDecimal("100"));
            fee = request.getTxAmount().multiply(feePercent);
        }
        //???????????????????????? ??????????????????????????????
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
            //??????
            try {
                ApiRequestSupport.checkLogin(data, sign, token);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * //RIT????????????
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
            //??????????????????
            AccountWithdrawFlowRit accountWithdrawFlowRit = appAccountService.getByWithdrawFlowId(request.getWithdrawFlowId());
            if (accountWithdrawFlowRit == null) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.NO_DATA);
            }
            String memo = accountWithdrawFlowRit.getMemo();
            accountWithdrawFlowRit.setMemo(request.getAuditRemark());
            //????????????????????????
            UserInModel userInModel = new UserInModel();
            userInModel.setUserId(accountWithdrawFlowRit.getUserId());
            UserkgOutModel userkgOutModel = userRMapper.selectByUser(userInModel);
            //????????????????????????
            OperTypeEnum operTypeEnum = OperTypeEnum.getBusinessTypeEnum(accountWithdrawFlowRit.getStatus());
            //??????????????????????????????
            OperTypeEnum auditOperTypeEnum = OperTypeEnum.getBusinessTypeEnum(request.getStatus());
            Document updateDocument = new Document().append("auditUserId", sysUser.getSysUserId()).append("auditUserName", sysUser.getSysUserName());

            //?????????????????? ????????????????????????????????????
            if (request.getAuditRole() == 1 && OperTypeEnum.WITHDRAW_PENDING.equals(operTypeEnum)) {
                //?????? ?????????????????????????????????????????????
                if (OperTypeEnum.WITHDRAW_NO_PASS.equals(auditOperTypeEnum)) {
                    //??????????????? ?????????RIT???????????????????????????????????????
                    //??????RIT????????????,??????RIT????????????
                    AccountInModel accountInModel = new AccountInModel();
                    accountInModel.setUserId(accountWithdrawFlowRit.getUserId());
                    accountInModel.setBalance(accountWithdrawFlowRit.getWithdrawAmount());
                    int state = accountWMapper.addRitBalanceReduceRitFrozenBalance(accountInModel);
                    if (state > 0) {
                        //????????????????????????????????????
                        int check = MongoUtils.updateByFilter(MongoTables.ACCOUNT_WITHDRAW_FLOW_RIT, filter, updateDocument.append("status", auditOperTypeEnum.getStatus()).append("operatorAuditRemark", request.getAuditRemark()));
                        if (check > 0) {
                            //?????????????????????????????????
                            appAccountService.addRitBalanceReduceRitFrozen(accountWithdrawFlowRit, userkgOutModel, sysUser, account_flow_id1, account_flow_id2, operId, now,
                                    "????????????????????????", BusinessTypeEnum.WITHDRAW_NO_PASS, BusinessTypeEnum.WITHDRAW_NO_PASS_FROZEN, FlowStatusEnum.NOPASS, auditOperTypeEnum);
                        } else {
                            throw new BusinessException(ExceptionEnum.SERVICEERROR);
                        }
                    } else {
                        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SERVICEERROR);
                    }
                } else if (OperTypeEnum.WITHDRAW_WATING.equals(auditOperTypeEnum)) {
                    MongoUtils.updateByFilter(MongoTables.ACCOUNT_WITHDRAW_FLOW_RIT, filter, updateDocument.append("status", auditOperTypeEnum.getStatus()).append("operatorAuditRemark", request.getAuditRemark()));
                    //????????????????????????
                    addOperRecord(operId, accountWithdrawFlowRit, sysUser, auditOperTypeEnum, now);
                } else {
                    return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
                }
                return JsonEntity.makeSuccessJsonEntity(ExceptionEnum.SUCCESS);
            } else if (request.getAuditRole() == 2 && OperTypeEnum.WITHDRAW_WATING.equals(operTypeEnum)) {
                //?????? ????????????????????????????????????????????????
                if (OperTypeEnum.WITHDRAW_FAIL.equals(auditOperTypeEnum)) {
                    //????????????
                    //??????RIT????????????,??????RIT????????????
                    AccountInModel accountInModel = new AccountInModel();
                    accountInModel.setUserId(accountWithdrawFlowRit.getUserId());
                    accountInModel.setBalance(accountWithdrawFlowRit.getWithdrawAmount());
                    int state = accountWMapper.addRitBalanceReduceRitFrozenBalance(accountInModel);
                    if (state > 0) {
                        //?????????????????????????????????
                        int check = MongoUtils.updateByFilter(MongoTables.ACCOUNT_WITHDRAW_FLOW_RIT, filter, updateDocument.append("status", auditOperTypeEnum.getStatus()).append("financialAuditRemark", request.getAuditRemark()).append("txId", request.getTxId()));
                        if (check > 0) {
                            //?????????????????????????????????
                            accountWithdrawFlowRit.setTxId(request.getTxId());
                            appAccountService.addRitBalanceReduceRitFrozen(accountWithdrawFlowRit, userkgOutModel, sysUser, account_flow_id1, account_flow_id2, operId, now,
                                    "????????????????????????", BusinessTypeEnum.WITHDRAW_FAIL, BusinessTypeEnum.WITHDRAW_FAIL_FROZEN, FlowStatusEnum.TRANSFER_FAIL, auditOperTypeEnum);
                        } else {
                            throw new BusinessException(ExceptionEnum.SERVICEERROR);
                        }
                    } else {
                        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SERVICEERROR);
                    }

                } else if (OperTypeEnum.WITHDRAW_SUCCESS.equals(auditOperTypeEnum)) {
                    //????????????
                    //?????????????????????????????? ??????????????????????????????
                    AccountInModel accountInModel = new AccountInModel();
                    accountInModel.setUserId(accountWithdrawFlowRit.getUserId());
                    accountInModel.setBalance(accountWithdrawFlowRit.getWithdrawAmount());
                    int s1 = accountWMapper.reduceRitFrozenBalance(accountInModel);
                    if (s1 > 0) {
                        accountInModel.setUserId(Constants.PLATFORM_USER_ID);
                        accountInModel.setBalance(accountWithdrawFlowRit.getPoundageAmount());
                        int s2 = accountWMapper.addRitBalance(accountInModel);
                        if (s2 > 0) {
                            //????????????????????????????????? ??????????????????????????????
                            int check = MongoUtils.updateByFilter(MongoTables.ACCOUNT_WITHDRAW_FLOW_RIT, filter, updateDocument.append("status", auditOperTypeEnum.getStatus()).append("financialAuditRemark", request.getAuditRemark()).append("txId", request.getTxId()).append("accountTime", now));
                            if (check > 0) {
                                //???????????????????????????????????? ??????????????????
                                //?????????????????????????????????
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
                                accountFlowRitMongo_balance.setFlow_detail(accountWithdrawFlowRit.getUserId() + "???????????????????????????");
                                accountFlowRitMongo_balance.setRemark(accountWithdrawFlowRit.getMemo());
                                //????????????????????????
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
                                //????????????????????????
                                accountWithdrawFlowRit.setTxId(request.getTxId());
                                addOperRecord(operId, accountWithdrawFlowRit, sysUser, auditOperTypeEnum, now);
                            } else {
                                logger.error("??????RIT??????????????????????????????------");
                                throw new BusinessException(ExceptionEnum.SERVICEERROR);
                            }
                        } else {
                            logger.error("??????RIT??????????????????????????????------");
                            throw new BusinessException(ExceptionEnum.SERVICEERROR);
                        }
                    } else {
                        logger.error("??????RIT??????????????????????????????------");
                        throw new BusinessException(ExceptionEnum.SERVICEERROR);
                    }

                } else {
                    return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
                }
                return JsonEntity.makeSuccessJsonEntity(ExceptionEnum.SUCCESS);
            } else {
                return JsonEntity.makeExceptionJsonEntity("", "??????????????????????????????????????????");
            }

        } catch (Exception e) {
            //????????????????????????MONGO????????????
            logger.error("??????RIT?????????????????????{},___????????????????????????{}", JSON.toJSONString(e.getStackTrace()), JSON.toJSONString(request));
            logger.error("??????RIT?????????????????????{},___????????????????????????{}", e.getMessage(), JSON.toJSONString(request));
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
        //???????????????IP????????????????????????IP
        Result<SiteinfoResponse> siteinfoResponse = siteinfoService.selectSiteInfo();
        CheckUtils.checkRetInfo(siteinfoResponse, true);

        SiteinfoResponse siteInfo = siteinfoResponse.getData();
        if (!Check.NuNString(siteInfo.getLimitIp())) {
            String userIp = HttpUtil.getIpAddr(httpServletRequest);
            logger.info("====??????????????????ip" + userIp);

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
     * ??????RIT????????????
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
     * ????????????????????????
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
     * ??????kg??????????????????
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
     * ??????rit??????????????????
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
