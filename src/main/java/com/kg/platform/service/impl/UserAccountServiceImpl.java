package com.kg.platform.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.lock.Lock;
import com.kg.platform.common.utils.PropertyLoader;
import com.kg.platform.dao.entity.Account;
import com.kg.platform.dao.read.AccountRMapper;
import com.kg.platform.dao.write.AccountWMapper;
import com.kg.platform.model.in.AccountInModel;
import com.kg.platform.service.UserAccountService;

@Service
@Transactional
public class UserAccountServiceImpl implements UserAccountService {
    private static final Logger logger = LoggerFactory.getLogger(UserAccountServiceImpl.class);

    private static final String USER_ACCOUNT_INIT_LOCK_KEY = "kguser::UserAccountService::init::";

    @Resource(name = "propertyLoader")
    protected PropertyLoader propertyLoader;

    @Inject
    private IDGen idGenerater;

    @Inject
    private AccountRMapper accountRMapper;

    @Inject
    private AccountWMapper accountWMapper;

    /**
     * 初始化账户接口
     * 
     */
    @Override
    public boolean init(AccountInModel accountIn) {
        logger.info("用户初始化账户接口 ：AccountInModel={}", JSON.toJSONString(accountIn));

        Lock lock = new Lock();

        Long userId = accountIn.getUserId();
        BigDecimal balance = accountIn.getBalance();
        if(userId == null){
            return false;
        }
        if (balance == null) {
            balance = BigDecimal.valueOf(0.000d);
        }
        BigDecimal txbBalance = accountIn.getTxbBalance();
        if (txbBalance == null) {
            txbBalance = BigDecimal.valueOf(0.000d);
        }
        String key;
        if(accountIn.getType() != null && accountIn.getType() == 0){
			key = USER_ACCOUNT_INIT_LOCK_KEY + accountIn.getUserMobile();
		}else {
        	key = USER_ACCOUNT_INIT_LOCK_KEY + userId;
		}
        final String userAddress = propertyLoader.getProperty("common", "global.userTxAddress");

        try {

            // 加锁
            lock.lock(key);

            Account account = accountRMapper.selectByUserId(userId);
            if (account != null) {
                Account userAccount = new Account();

                userAccount.setUserId(userId);
                userAccount.setBalance(balance);

                accountWMapper.updateByUserId(userAccount);

            } else {
                // 初始化账户

                Account userAccount = new Account();

                userAccount.setAccountId(idGenerater.nextId());
                userAccount.setUserId(userId);
                userAccount.setBalance(balance);
                userAccount.setFrozenBalance(BigDecimal.valueOf(0.000d));
                userAccount.setTxbBalance(txbBalance);
                userAccount.setTxbFrozenBalance(BigDecimal.valueOf(0.000d));
                userAccount.setTxAddress(userAddress);

                accountWMapper.insertSelective(userAccount);
            }

        } finally {
            lock.unLock();
        }

        return true;
    }

}
