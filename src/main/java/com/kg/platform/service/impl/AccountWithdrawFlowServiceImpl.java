package com.kg.platform.service.impl;

import java.util.Date;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.exception.ApiMessageException;
import com.kg.platform.common.lock.Lock;
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
import com.kg.platform.model.out.AccountWithdrawFlowOutModel;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.AccountWithdrawFlowRequest;
import com.kg.platform.model.response.AccountWithdrawFlowResponse;
import com.kg.platform.service.AccountWithdrawFlowService;

@Service
@Transactional
public class AccountWithdrawFlowServiceImpl implements AccountWithdrawFlowService {
    private static final String USER_CANCELED_LOCK_KEY = "kguser:: AccountWithdrawFlowService::canceled::";

    private static final Logger logger = LoggerFactory.getLogger(AccountWithdrawFlowServiceImpl.class);

    @Inject
    AccountWithdrawFlowWMapper accountWithdrawFlowWMapperr;

    @Inject
    UserRMapper userRMapper;

    @Inject
    AccountFlowWMapper accountFlowWMapper;

    @Inject
    IDGen idGenerater;

    @Inject
    AccountWithdrawFlowRMapper accountWithdrawFlowRMapper;

    @Inject
    AccountWMapper accountWMapper;

    /**
     * 撤销提现
     */
    @Override
    public boolean updateByUserSelective(AccountWithdrawFlowRequest request) {
        logger.info("用户撤销提现接口：AccountWithdrawFlowRequest={}", JSON.toJSONString(request));

        boolean SUCCESS;
        // 查用户
        UserkgOutModel umodel = userRMapper.getUserDetails(request.getUserId());
        // 查提现金额
        AccountWithdrawFlowInModel withdrawFlowInModel = new AccountWithdrawFlowInModel();
        withdrawFlowInModel.setWithdrawFlowId(request.getWithdrawFlowId());
        AccountWithdrawFlowOutModel outModel = accountWithdrawFlowRMapper.getUserWithdraw(withdrawFlowInModel);
        // 提现申请撤销先判断 该提现申请是否 在提现中或提现通过两种状态
        if (outModel.getStatus().intValue() == AccountWithdrawStatusEnum.FAIL.getStatus()) {
            throw new ApiMessageException("该条记录已审核不通过，无法进行“撤销”操作");
        }
        if (outModel.getStatus().intValue() == AccountWithdrawStatusEnum.PASS.getStatus()) {
            throw new ApiMessageException("该条记录已审核通过，无法进行“撤销”操作");
        }

        AccountWithdrawFlowInModel inModel = new AccountWithdrawFlowInModel();
        inModel.setWithdrawFlowId(request.getWithdrawFlowId());
        inModel.setStatus(request.getStatus());
        // 修改提现表状态
        SUCCESS = accountWithdrawFlowWMapperr.updateByUserSelective(inModel) > 0;
        if (SUCCESS) {
            AccountFlowInModel accinModel = new AccountFlowInModel();
            long flowid = idGenerater.nextId();
            accinModel.setAccountFlowId(flowid);
            accinModel.setRelationFlowId(request.getWithdrawFlowId());
            accinModel.setUserId(Long.parseLong(umodel.getUserId()));
            accinModel.setUserPhone(umodel.getUserMobile());
            accinModel.setUserEmail(umodel.getUserEmail());
            accinModel.setAmount(outModel.getWithdrawAmount());
            accinModel.setAccountAmount(outModel.getWithdrawAmount());
            accinModel.setBusinessTypeId(BusinessTypeEnum.WITHDRAW.getStatus());
            accinModel.setFlowDate(new Date());
            accinModel.setFlowStatus(FlowStatusEnum.CANCELED.getStatus());
            accinModel.setFlowDetail("撤销提现");
            // 交易流水表
            SUCCESS = accountFlowWMapper.insertFlowSelective(accinModel) > 0;

            if (SUCCESS) {
                AccountInModel auntinModel = new AccountInModel();
                auntinModel.setUserId(request.getUserId());
                auntinModel.setBalance(outModel.getWithdrawAmount());
                // 用户账号，账户余额+撤销金额，冻结余额-撤销金额
                Lock lock = new Lock();
                String key = USER_CANCELED_LOCK_KEY + request.getUserId();
                try {
                    logger.info("用户撤销提现账号操作入参：AccountWithdrawFlowRequest={}", JSON.toJSONString(auntinModel));
                    lock.lock(key);
                    SUCCESS = accountWMapper.updateAddBalance(auntinModel) > 0;
                } finally {
                    lock.unLock();
                }
            }

        }
        return SUCCESS;

    }

    @Override
    public Result<AccountWithdrawFlowResponse> selectByUserFlow(AccountWithdrawFlowRequest request) {
        AccountWithdrawFlowInModel withdrawFlowInModel = new AccountWithdrawFlowInModel();
        withdrawFlowInModel.setUserId(request.getUserId());
        withdrawFlowInModel.setWithdrawFlowId(request.getWithdrawFlowId());

        AccountWithdrawFlowOutModel outModel = accountWithdrawFlowRMapper.selectByUserFlow(withdrawFlowInModel);
        AccountWithdrawFlowResponse response = new AccountWithdrawFlowResponse();
        if (outModel != null) {
            response.setStatus(outModel.getStatus());
        }
        return new Result<AccountWithdrawFlowResponse>(response);
    }

}
