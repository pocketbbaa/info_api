package com.kg.platform.model.strategyUtil;

import com.kg.platform.enumeration.StrategyEnum;

/**
 * 策略转换接口
 * @param <T>
 */
public interface ProcessHandler<T extends BaseStrategyCentre> {

    /**
     * 通知其他策略接口层
     */
    T changeStrategyProcess(StrategyEnum strategyEnum);
}
