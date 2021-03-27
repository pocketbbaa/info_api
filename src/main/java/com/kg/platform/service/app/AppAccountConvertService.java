package com.kg.platform.service.app;

import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.model.response.UserkgResponse;

/**
 * 转出相关
 */
public interface AppAccountConvertService {

    /**
     * KG兑换RIT页面数据
     *
     * @param kguser
     * @return
     */
    AppJsonEntity kgToRitIndex(UserkgResponse kguser);

    /**
     * KG兑换RIT
     *
     * @param kguser
     * @return
     */
    AppJsonEntity kgToRit(UserkgResponse kguser) throws Exception;

    /**
     * KG兑换RIT校验
     *
     * @param kguser
     * @return
     */
    AppJsonEntity kgToRitCheck(UserkgResponse kguser);
}
