package com.kg.platform.service.impl;

import java.math.BigDecimal;
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
import com.kg.platform.dao.entity.AccountDepositFlow;
import com.kg.platform.dao.entity.AccountFlow;
import com.kg.platform.dao.read.UserRMapper;
import com.kg.platform.dao.write.AccountDepositFlowWMapper;
import com.kg.platform.dao.write.AccountFlowWMapper;
import com.kg.platform.enumeration.BusinessTypeEnum;
import com.kg.platform.enumeration.FlowStatusEnum;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.UserDepositRequest;
import com.kg.platform.service.UserDepositService;

@Service
@Transactional
public class UserDepositServiceImpl implements UserDepositService {
    private static final Logger logger = LoggerFactory.getLogger(UserDepositServiceImpl.class);

    private static final String USER_DEPOSIT_LOCK_KEY = "kguser::UserDepositService::deposit::";

    @Resource(name = "propertyLoader")
    protected PropertyLoader propertyLoader;

    @Inject
    private IDGen idGenerater;

    @Inject
    private UserRMapper userRMapper;

    @Inject
    private AccountDepositFlowWMapper accountDepositFlowWMapper;

    @Inject
    private AccountFlowWMapper accountFlowWMapper;

    /**
     * 用户保证金充值接口
     * 
     */
    @Override
    public boolean deposit(UserDepositRequest request) {
        logger.info("用户保证金充值接口：UserDepositRequest={}", JSON.toJSONString(request));
        final String userDepositAmount = propertyLoader.getProperty("common", "global.userDepositAmount");

        Lock lock = new Lock();

        Long userId = Long.parseLong(request.getUserId());
        BigDecimal balance = request.getAccountAmount();

        if (balance == null || balance.doubleValue() <= 0) {
            logger.error("用户保证金充值接口收到的balance {}", balance);
            throw new BusinessException(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }

        String key = USER_DEPOSIT_LOCK_KEY + userId + request.getTxId();

        try {

            // 加锁
            lock.lock(key);

            // 用户不存在直接抛错
            UserkgOutModel user = userRMapper.selectByPrimaryKey(userId);
            if (user == null) {
                logger.error("用户保证金充值接口收到的用户id {}", userId);
                throw new BusinessException(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                        ExceptionEnum.PARAMEMPTYERROR.getMessage());
            }

            AccountDepositFlow userDepositFlow = new AccountDepositFlow();
            Date accountTime = new Date();
            long flowId = idGenerater.nextId();

            userDepositFlow.setDepositFlowId(idGenerater.nextId());
            userDepositFlow.setAccountFlowId(flowId);
            userDepositFlow.setFromAddress(request.getFromAddress());
            userDepositFlow.setDepositAmount(new BigDecimal(userDepositAmount));
            userDepositFlow.setAccountAmount(request.getAccountAmount());
            userDepositFlow.setToAddress(request.getToAddress());
            userDepositFlow.setAccountTime(accountTime);
            userDepositFlow.setRechargeTime(accountTime);
            userDepositFlow.setTxId(request.getTxId());
            userDepositFlow.setUserId(userId);
            userDepositFlow.setUserPhone(user.getUserMobile());

            int success = accountDepositFlowWMapper.insertSelective(userDepositFlow);

            logger.info("======插入保证金记录表返回：" + success);

            // 写入交易记录
            AccountFlow accountFlow = new AccountFlow();

            accountFlow.setAccountFlowId(flowId);
            accountFlow.setRelationFlowId(flowId);
            accountFlow.setUserId(userId);
            accountFlow.setUserPhone(user.getUserMobile());
            accountFlow.setUserEmail(user.getUserEmail());
            accountFlow.setAmount(request.getAccountAmount());
            accountFlow.setBusinessTypeId(BusinessTypeEnum.DEPOSITIN.getStatus());
            accountFlow.setTxId(request.getTxId());
            accountFlow.setTxAddress(request.getToAddress());
            accountFlow.setAccountAmount(request.getAccountAmount());
            accountFlow.setAccountDate(accountTime);
            accountFlow.setFlowStatus(FlowStatusEnum.DEPOSIT_SUCCESSED.getStatus());

            success = accountFlowWMapper.insertSelective(accountFlow);

            logger.info("======插入流水表返回：" + success);

        } finally {
            lock.unLock();
        }

        return true;
    }

}
