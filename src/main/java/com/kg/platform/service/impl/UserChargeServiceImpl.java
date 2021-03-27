package com.kg.platform.service.impl;

import java.util.Date;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.exception.BusinessException;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.lock.Lock;
import com.kg.platform.common.utils.PropertyLoader;
import com.kg.platform.dao.entity.AccountFlow;
import com.kg.platform.dao.entity.AccountRechargeFlow;
import com.kg.platform.dao.read.AccountRechargeFlowRMapper;
import com.kg.platform.dao.read.UserRMapper;
import com.kg.platform.dao.write.AccountFlowWMapper;
import com.kg.platform.dao.write.AccountRechargeFlowWMapper;
import com.kg.platform.enumeration.BusinessTypeEnum;
import com.kg.platform.enumeration.FlowStatusEnum;
import com.kg.platform.enumeration.RechargeStatusEnum;
import com.kg.platform.model.in.AccountInModel;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.UserChargeRequest;
import com.kg.platform.service.UserAccountService;
import com.kg.platform.service.UserChargeService;

@Service
@Transactional
public class UserChargeServiceImpl implements UserChargeService {
    private static final Logger logger = LoggerFactory.getLogger(UserChargeServiceImpl.class);

    private static final String USER_RECHARGE_LOCK_KEY = "kguser::UserChargeService::recharge::";

    @Resource(name = "propertyLoader")
    protected PropertyLoader propertyLoader;

    @Inject
    private IDGen idGenerater;

    @Inject
    private UserRMapper userRMapper;

    @Inject
    private AccountRechargeFlowRMapper accountRechargeFlowRMapper;

    @Inject
    private AccountRechargeFlowWMapper accountRechargeFlowWMapper;

    @Inject
    private AccountFlowWMapper accountFlowWMapper;

    @Inject
    private UserAccountService userAccountService;

    /**
     * 用户充值接口 1. 查询是否存在用户，不存在直接抛错 2. 查询是否存在充值记录，存在就更新，不存在就新增 3.
     * 查询是否存在账户，存在就更新余额，不存在就初始化账户，并初始化余额 4. 充值成功后写入交易记录
     * 
     */
    @Override
    public boolean recharge(UserChargeRequest request) {
        logger.info("用户充值接口：UserChargeRequest={}", JSON.toJSONString(request));

        Lock lock = new Lock();

        Long userId = Long.parseLong(request.getUserId());
        String key = USER_RECHARGE_LOCK_KEY + userId + request.getTxId();

        try {

            // 加锁
            lock.lock(key);

            // 用户不存在直接抛错
            UserkgOutModel user = userRMapper.selectByPrimaryKey(userId);
            if (user == null) {
                logger.error("用户充值接口收到的用户id {}", userId);
                throw new BusinessException(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                        ExceptionEnum.PARAMEMPTYERROR.getMessage());
            }

            AccountRechargeFlow accountRechargeFlow = accountRechargeFlowRMapper.selectByTxid(request.getTxId());
            AccountRechargeFlow userRechargeFlow = new AccountRechargeFlow();
            Date accountTime = new Date();
            long flowId = idGenerater.nextId();
            int success = 0;
            if (accountRechargeFlow != null) {

                userRechargeFlow.setAccountAmount(request.getAccountAmount());
                userRechargeFlow.setAccountTime(accountTime);
                userRechargeFlow.setStatus(request.getStatus());
                userRechargeFlow.setTxId(request.getTxId());

                // 更新充值记录表状态
                success = accountRechargeFlowWMapper.updateByTxid(userRechargeFlow);

                logger.info("======更新充值记录表返回：" + success);

            } else {

                userRechargeFlow.setRechargeFlowId(idGenerater.nextId());
                userRechargeFlow.setAccountFlowId(flowId);
                userRechargeFlow.setFromAddress(request.getFromAddress());
                userRechargeFlow.setRechargeAmount(request.getRechargeAmount());
                userRechargeFlow.setToAddress(request.getToAddress());
                userRechargeFlow.setStatus(request.getStatus());
                userRechargeFlow.setTxId(request.getTxId());
                userRechargeFlow.setUserId(userId);
                userRechargeFlow.setUserPhone(user.getUserMobile());
                userRechargeFlow.setUserEmail(user.getUserEmail());

                success = accountRechargeFlowWMapper.insertSelective(userRechargeFlow);

                logger.info("======插入充值记录表返回：" + success);

            }

            if (request.getStatus().intValue() == RechargeStatusEnum.SUCCESSED.getStatus()) {

                AccountInModel accountIn = new AccountInModel();
                accountIn.setUserId(userId);
                accountIn.setBalance(request.getAccountAmount());
                userAccountService.init(accountIn);

                // 写入交易记录
                AccountFlow accountFlow = new AccountFlow();

                accountFlow.setAccountFlowId(flowId);
                accountFlow.setRelationFlowId(flowId);
                accountFlow.setUserId(userId);
                accountFlow.setUserPhone(user.getUserMobile());
                accountFlow.setUserEmail(user.getUserEmail());
                accountFlow.setAmount(request.getAccountAmount());
                accountFlow.setBusinessTypeId(BusinessTypeEnum.RECHARGE.getStatus());
                accountFlow.setTxId(request.getTxId());
                accountFlow.setTxAddress(request.getToAddress());
                accountFlow.setAccountAmount(request.getAccountAmount());
                accountFlow.setAccountDate(accountTime);
                accountFlow.setFlowDetail("充值成功");
                accountFlow.setFlowStatus(FlowStatusEnum.RECHARGED.getStatus());

                success = accountFlowWMapper.insertSelective(accountFlow);

                logger.info("======插入流水表返回：" + success);

            }
        } finally {
            lock.unLock();
        }

        return true;
    }

}
