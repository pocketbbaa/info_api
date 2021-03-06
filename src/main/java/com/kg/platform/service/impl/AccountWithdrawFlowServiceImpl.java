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
     * ????????????
     */
    @Override
    public boolean updateByUserSelective(AccountWithdrawFlowRequest request) {
        logger.info("???????????????????????????AccountWithdrawFlowRequest={}", JSON.toJSONString(request));

        boolean SUCCESS;
        // ?????????
        UserkgOutModel umodel = userRMapper.getUserDetails(request.getUserId());
        // ???????????????
        AccountWithdrawFlowInModel withdrawFlowInModel = new AccountWithdrawFlowInModel();
        withdrawFlowInModel.setWithdrawFlowId(request.getWithdrawFlowId());
        AccountWithdrawFlowOutModel outModel = accountWithdrawFlowRMapper.getUserWithdraw(withdrawFlowInModel);
        // ??????????????????????????? ????????????????????? ???????????????????????????????????????
        if (outModel.getStatus().intValue() == AccountWithdrawStatusEnum.FAIL.getStatus()) {
            throw new ApiMessageException("???????????????????????????????????????????????????????????????");
        }
        if (outModel.getStatus().intValue() == AccountWithdrawStatusEnum.PASS.getStatus()) {
            throw new ApiMessageException("????????????????????????????????????????????????????????????");
        }

        AccountWithdrawFlowInModel inModel = new AccountWithdrawFlowInModel();
        inModel.setWithdrawFlowId(request.getWithdrawFlowId());
        inModel.setStatus(request.getStatus());
        // ?????????????????????
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
            accinModel.setFlowDetail("????????????");
            // ???????????????
            SUCCESS = accountFlowWMapper.insertFlowSelective(accinModel) > 0;

            if (SUCCESS) {
                AccountInModel auntinModel = new AccountInModel();
                auntinModel.setUserId(request.getUserId());
                auntinModel.setBalance(outModel.getWithdrawAmount());
                // ???????????????????????????+???????????????????????????-????????????
                Lock lock = new Lock();
                String key = USER_CANCELED_LOCK_KEY + request.getUserId();
                try {
                    logger.info("???????????????????????????????????????AccountWithdrawFlowRequest={}", JSON.toJSONString(auntinModel));
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
