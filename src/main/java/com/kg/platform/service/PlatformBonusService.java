package com.kg.platform.service;

import java.math.BigDecimal;

import com.kg.platform.model.request.AccountRequest;

public interface PlatformBonusService {

    /**
     * 获取阅读奖励
     */
    BigDecimal getReadBonus(AccountRequest request);

    /**
     * 获取分享奖励
     */
    Boolean getShareBonus(AccountRequest request);

    /**
     * 获取阅读氪金奖励
     */
    Boolean getPlatformReadKgBonus(AccountRequest request);

    /**
     * 获取分享氪金奖励
     */
    Boolean getPlatformShareKgBonus(AccountRequest request);

    /**
     * 验证浏览奖励是否10次
     */
    Boolean checkBrowerCount(AccountRequest request);

}
