package com.kg.platform.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;

import com.kg.platform.common.mongo.MongoTables;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.mongo.Seach;
import com.kg.platform.common.utils.*;
import com.kg.platform.model.response.AccountFlowAppNewResponse;
import com.kg.platform.model.response.AccountFlowResponse130;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.types.Decimal128;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ApiMessageException;
import com.kg.platform.common.lock.Lock;
import com.kg.platform.dao.read.AccountFlowRMapper;
import com.kg.platform.dao.read.AccountRMapper;
import com.kg.platform.dao.read.AccountWithdrawFlowRMapper;
import com.kg.platform.dao.read.UserRMapper;
import com.kg.platform.dao.write.AccountFlowWMapper;
import com.kg.platform.dao.write.AccountWMapper;
import com.kg.platform.dao.write.AccountWithdrawFlowWMapper;
import com.kg.platform.enumeration.AccountWithdrawStatusEnum;
import com.kg.platform.enumeration.BusinessTypeEnum;
import com.kg.platform.enumeration.FlowStatusEnum;
import com.kg.platform.model.in.AccountFlowInModel;
import com.kg.platform.model.in.AccountInModel;
import com.kg.platform.model.in.AccountWithdrawFlowInModel;
import com.kg.platform.model.out.AccountFlowOutModel;
import com.kg.platform.model.out.AccountWithdrawFlowOutModel;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.AccountFlowRequest;
import com.kg.platform.model.response.AccountFlowResponse;
import com.kg.platform.service.AccountFlowService;
import com.kg.platform.service.UserAccountService;

@Service
@Transactional
public class AccountFlowServiceImpl implements AccountFlowService {
    private static final String USER_ADDUSERTIMES_LOCK_KEY = "kguser:: AccountService::addUserTimes::";

    private static final Logger logger = LoggerFactory.getLogger(AccountFlowServiceImpl.class);

    @Inject
    AccountFlowRMapper accountFlowRMapper;

    @Inject
    UserRMapper userRMapper;

    @Inject
    IDGen idGenerater;

    @Inject
    AccountFlowWMapper accountFlowWMapper;

    @Inject
    AccountWithdrawFlowRMapper accountWithdrawFlowRMapper;

    @Inject
    AccountWithdrawFlowWMapper accountWithdrawFlowWMapper;

    @Resource(name = "propertyLoader")
    protected PropertyLoader propertyLoader;

    @Inject
    AccountWMapper accountWMapper;

    @Inject
    AccountRMapper accountRMapper;

    @Inject
    UserAccountService userAccountService;

    /**
     * 我的钛值交易明细
     */
    @Override
    public PageModel<AccountFlowResponse> selectByUserflow(AccountFlowRequest request,
            PageModel<AccountFlowResponse> page) {
        logger.info("我的钛值交易明细：{},分页入参：{}", JSON.toJSONString(request), JSON.toJSONString(page));

        AccountFlowInModel inModel = new AccountFlowInModel();
        inModel.setStart((page.getCurrentPage() - 1) * page.getPageSize());
        inModel.setLimit(page.getPageSize());
        inModel.setUserId(request.getUserId());
        inModel.setType(request.getType());
        inModel.setStartTime(request.getStartTime());
        inModel.setEndTime(request.getEndTime());
        inModel.setBusinessTypeId(request.getBusinessTypeId());
        List<AccountFlowOutModel> list = accountFlowRMapper.selectByUserflow(inModel);
        List<AccountFlowResponse> responses = new ArrayList<AccountFlowResponse>();
        for (AccountFlowOutModel outModel : list) {
            AccountFlowResponse flowResponse = new AccountFlowResponse();
            flowResponse.setAccountFlowId(outModel.getAccountFlowId().toString());
            flowResponse.setTypename(outModel.getTypename());
            flowResponse.setFlowDate(DateUtils.formatDate(outModel.getFlowDate(), DateUtils.DISPLAY_FORMAT));
            flowResponse.setFlowDetail(outModel.getFlowDetail());
            flowResponse.setFreezeAmountString(outModel.getFreezeAmount() + "");
            flowResponse.setAmount(outModel.getAmount());
            flowResponse.setFreezeAmount(outModel.getFreezeAmount());
            flowResponse.setAccountAmount(outModel.getAccountAmount());
            responses.add(flowResponse);
        }
        long count = accountFlowRMapper.selectByUserCount(inModel);
        page.setData(responses);
        page.setTotalNumber(count);
        return page;
    }

   /* @Override
    public PageModel<AccountFlowResponse> selectUserTxbflow(AccountFlowRequest request,
            PageModel<AccountFlowResponse> page) {
        logger.info("我的氪金交易明细：{},分页入参：{}", JSON.toJSONString(request), JSON.toJSONString(page));

        AccountFlowInModel inModel = new AccountFlowInModel();
        inModel.setStart((page.getCurrentPage() - 1) * page.getPageSize());
        inModel.setLimit(page.getPageSize());
        inModel.setUserId(request.getUserId());
        inModel.setType(request.getType());
        inModel.setStartTime(request.getStartTime());
        inModel.setEndTime(request.getEndTime());
        inModel.setBusinessTypeId(request.getBusinessTypeId());
        List<AccountFlowOutModel> list = accountFlowRMapper.selectUserTxbflow(inModel);
        List<AccountFlowResponse> responses = new ArrayList<AccountFlowResponse>();
        for (AccountFlowOutModel outModel : list) {
            AccountFlowResponse flowResponse = new AccountFlowResponse();
            flowResponse.setAccountFlowId(outModel.getAccountFlowId().toString());
            flowResponse.setTypename(outModel.getTypename());
            flowResponse.setFlowDate(DateUtils.formatDate(outModel.getFlowDate(), DateUtils.DISPLAY_FORMAT));
            flowResponse.setFlowDetail(outModel.getFlowDetail());
            flowResponse.setAmount(outModel.getAmount());
            flowResponse.setTxAddress(outModel.getTxAddress());
            responses.add(flowResponse);
        }
        long count = accountFlowRMapper.selectUserTxbCount(inModel);
        page.setData(responses);
        page.setTotalNumber(count);
        return page;
    }*/

    @Override
    public PageModel<AccountFlowResponse130> selectUserTxbflow(AccountFlowRequest request,
                                                            PageModel<AccountFlowResponse130> page) {
        logger.info("我的氪金交易明细：{},分页入参：{}", JSON.toJSONString(request), JSON.toJSONString(page));
        BasicDBObject querryBy = this.getTxbflowQuerry(request);
        BasicDBObject sortBy = new BasicDBObject();
        sortBy.put("flow_date",-1);
        List<AccountFlowResponse130> responses = new ArrayList<AccountFlowResponse130>();
        MongoCursor<Document> cursors = MongoUtils.findByPage(MongoTables.ACCOUNT_FLOW_KG, querryBy, sortBy, page.getCurrentPage(), page.getPageSize());
         logger.info(">>>>>>>>cursors>>>>>>>>>"+page.getCurrentPage());
        logger.info(">>>>>>>>selectUserTxbflow>>>>>>>>>"+querryBy.toString());
        if (cursors != null) {
            while (cursors.hasNext()) {
                AccountFlowResponse130 flowResponse = new AccountFlowResponse130();
                Document document = cursors.next();
                System.out.println(">>>>>>kg流水数据>>>>>>>" + JsonUtil.writeValueAsString(document));

                String accountFlowId=document.get("account_flow_id")==null?"":document.get("account_flow_id").toString();
                flowResponse.setAccountFlowId(accountFlowId);

                Integer typeName=document.get("business_type_id")==null?null:document.getInteger("business_type_id");
                if(typeName!=null){
                    flowResponse.setTypeName(BusinessTypeEnum.getBusinessTypeEnum(typeName).getDisplay());
                }

                Long flowDate=document.get("flow_date")==null?null:document.getLong("flow_date");
                if(flowDate!=null){
                    Date date=new Date();
                    date.setTime(flowDate);
                    flowResponse.setFlowDate(DateUtils.formatDate(date, DateUtils.DISPLAY_FORMAT));
                }

                String amount=document.get("amount")==null?null:((Decimal128) document.get("amount")).bigDecimalValue().stripTrailingZeros().toPlainString();
                flowResponse.setAmount(amount);

                String txAddress=document.get("tx_address")==null?"":document.getString("tx_address");
                flowResponse.setTxAddress(txAddress);

                String flowDetail=document.get("flow_detail")==null?"":document.getString("flow_detail");
                flowResponse.setFlowDetail(flowDetail);

                String remark=document.get("remark")==null?"":document.getString("remark");
                flowResponse.setRemark(remark);

                responses.add(flowResponse);
            }
        }
        logger.info(">>>>>>>>>>>>>>"+JsonUtil.writeValueAsString(responses));
        Long count=MongoUtils.count(MongoTables.ACCOUNT_FLOW_KG,querryBy);
        page.setData(responses);
        page.setTotalNumber(count);
        return page;
    }

    public BasicDBObject getTxbflowQuerry(AccountFlowRequest request){
        BasicDBObject querryBy=new BasicDBObject();
        querryBy.append("user_id",new BasicDBObject(Seach.EQ.getOperStr(),request.getUserId()));
        if(request.getBusinessTypeId()!=null){
            querryBy.append("business_type_id",new BasicDBObject(Seach.EQ.getOperStr(),request.getBusinessTypeId()));
        }
        if(StringUtils.isNotEmpty(request.getType())){
            int  typeId[] =null;
            if("1".equals(request.getType())){
                 typeId=new int[]{1000,1510,1520,1560,1570};
            }
            if("2".equals(request.getType())){
                typeId=new int[]{1530};
            }
            querryBy.append("business_type_id",new BasicDBObject(Seach.IN.getOperStr(),typeId));
        }
        	querryBy.append("amount",new BasicDBObject(Seach.NE.getOperStr(),BigDecimal.ZERO));
        //时间查询
        if (StringUtils.isNotEmpty(request.getStartTime())) {
            Date startDate=DateUtils.parseDate(request.getStartTime(),"yyyy-MM-dd");
            querryBy.put("flow_date", BasicDBObjectBuilder.start(Seach.GTE.getOperStr(), startDate.getTime()));
        }

        if (StringUtils.isNotEmpty(request.getEndTime())) {
            Date endDate=DateUtils.parseDate(request.getEndTime(),"yyyy-MM-dd");
            querryBy.put("flow_date", BasicDBObjectBuilder.start(Seach.LTE.getOperStr(), endDate.getTime()));
        }
        if(StringUtils.isNotEmpty(request.getStartTime())&&StringUtils.isNotEmpty(request.getEndTime())){
            Date startDate=DateUtils.parseDate(request.getStartTime(),"yyyy-MM-dd");
            Date endDate=DateUtils.parseDate(request.getEndTime(),"yyyy-MM-dd");
            querryBy.put("flow_date", BasicDBObjectBuilder.start(Seach.GTE.getOperStr(), startDate.getTime())
                    .add(Seach.LTE.getOperStr(), endDate.getTime()).get());
        }
        return querryBy;
    }





    /**
     * 提交提现申请
     */
    @Override
    public boolean addUserTimes(AccountFlowRequest request) {

        logger.info("提现申请入参：{}", JSON.toJSONString(request));
        boolean SUCCESS;
        Lock lock = new Lock();
        String key = USER_ADDUSERTIMES_LOCK_KEY + request.getUserId() + request.getTxAddress();

        try {
            lock.lock(key);

            // 查申请中的提现申请
            AccountWithdrawFlowInModel withdrawFlowInModel = new AccountWithdrawFlowInModel();
            withdrawFlowInModel.setUserId(request.getUserId());
            AccountWithdrawFlowOutModel outModel = accountWithdrawFlowRMapper.getauditAmount(withdrawFlowInModel);
            if (outModel != null) {
                throw new ApiMessageException("存在申请中的提现申请,请勿重复提交");
            }
            BigDecimal bignum = request.getAmount().multiply(Constants.POUNDAGE_RATE);

            // 查用户
            UserkgOutModel umodel = userRMapper.getUserDetails(request.getUserId());
            AccountWithdrawFlowInModel flowInModel = new AccountWithdrawFlowInModel();
            long flowId = idGenerater.nextId();
            flowInModel.setWithdrawFlowId(idGenerater.nextId());
            flowInModel.setAccountFlowId(flowId);
            flowInModel.setUserId(Long.parseLong(umodel.getUserId()));
            flowInModel.setUserEmail(umodel.getUserEmail());
            flowInModel.setUserPhone(umodel.getUserMobile());
            flowInModel.setWithdrawAmount(request.getAmount());
            flowInModel.setPoundageAmount(bignum);
            flowInModel.setAccountAmount(request.getAmount().subtract(bignum));
            flowInModel.setWithdrawTime(new Date());
            flowInModel.setFromAddress(propertyLoader.getProperty("common", "global.userTxAddress"));
            flowInModel.setToAddress(request.getTxAddress());
            flowInModel.setRemark(request.getRemark());
            flowInModel.setStatus(AccountWithdrawStatusEnum.AUDITING.getStatus());
            // 存提现流水表
            SUCCESS = accountWithdrawFlowWMapper.insertFlowSelective(flowInModel) > 0;
            if (SUCCESS) {

                AccountFlowInModel inModel = new AccountFlowInModel();

                inModel.setAccountFlowId(flowId);
                inModel.setRelationFlowId(flowId);
                inModel.setUserId(Long.parseLong(umodel.getUserId()));
                inModel.setUserPhone(umodel.getUserMobile());
                inModel.setUserEmail(umodel.getUserEmail());
                inModel.setAmount(request.getAmount().multiply(new BigDecimal(-1)));
                inModel.setBusinessTypeId(BusinessTypeEnum.WITHDRAW.getStatus());
                inModel.setPoundageAmount(bignum);
                inModel.setAccountAmount(request.getAmount().subtract(bignum));
                inModel.setFlowDate(new Date());
                inModel.setTxAddress(request.getTxAddress());
                inModel.setFlowStatus(FlowStatusEnum.AUDITING.getStatus());
                inModel.setFlowDetail("提币审核中");
                // 交易流水表
                SUCCESS = accountFlowWMapper.insertFlowSelective(inModel) > 0;
                if (SUCCESS) {

                    AccountInModel atinModel = new AccountInModel();
                    atinModel.setUserId(request.getUserId());
                    atinModel.setBalance(request.getAmount());

                    logger.info("用户提现接口：AccountFlowRequest={}", JSON.toJSONString(request));

                    // 修改账户余额减，冻结余额加
                    SUCCESS = accountWMapper.updateReduction(atinModel) > 0;

                } else {
                    logger.error("========保存交易流水失败：" + JSON.toJSONString(inModel));
                }
            } else {
                logger.error("========保存提现流水失败：" + JSON.toJSONString(flowInModel));
            }
        } finally {
            lock.unLock();
        }
        return SUCCESS;

    }

    /**
     * 充值，返回充值地址
     * 
     * @param \
     * @return
     */
    public Result<AccountFlowResponse> TopupTimes() {
        AccountFlowResponse response = new AccountFlowResponse();
        response.setTxAddress(propertyLoader.getProperty("common", "global.userTxAddress"));
        return new Result<AccountFlowResponse>(response);

    }

    /**
     * 统计当天用户对同一作者打赏次数
     */
    @Override
    public int getTipsout(AccountFlowRequest request) {
        AccountFlowInModel inModel = new AccountFlowInModel();
        inModel.setBusinessTypeId(BusinessTypeEnum.TIPSOUT.getStatus());
        inModel.setUserId(request.getUserId());
        inModel.setArticleId(request.getArticleId());
        return accountFlowRMapper.getTipsout(inModel);
    }

    @Override
    public int getRewardTips(AccountFlowRequest request) {
        AccountFlowInModel inModel = new AccountFlowInModel();
        inModel.setUserId(request.getUserId());
        inModel.setRewardUid(request.getRewardUid());
        return accountFlowRMapper.getRewardTips(inModel);
    }

    @Override
    public Integer getShareBonusRecords(AccountFlowRequest request) {
        AccountFlowInModel accountFlowInModel = new AccountFlowInModel();
        accountFlowInModel.setUserId(request.getUserId());
        accountFlowInModel.setBusinessTypeId(BusinessTypeEnum.SHAREARTICLETVAWARD.getStatus());
        return accountFlowRMapper.getShareBonusCount(accountFlowInModel);
    }

    @Override
    public Integer getShareBonusStatusCount(AccountFlowRequest request) {
        AccountFlowInModel accountFlowInModel = new AccountFlowInModel();
        accountFlowInModel.setArticleId(request.getArticleId());
        accountFlowInModel.setBusinessTypeId(BusinessTypeEnum.SHAREARTICLETVAWARD.getStatus());
        return accountFlowRMapper.getShareBonusStatusCount(accountFlowInModel);
    }

}
