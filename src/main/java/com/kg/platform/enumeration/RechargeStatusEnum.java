package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

/**
 * 充值状态枚举
 */
public enum RechargeStatusEnum {

    /**
     * 充值中
     */
    RECHARGING(0, "充值中"),
    /**
     * 充值成功
     */
    SUCCESSED(1, "充值成功");

    private int status;

    private String display;

    RechargeStatusEnum(int status, String display) {
        this.status = status;
        this.display = display;
    }

    public int getStatus() {
        return status;
    }

    public String getDisplay() {
        return display;
    }

    public static RechargeStatusEnum getRechargeStatusEnum(int status) {
        RechargeStatusEnum statuses[] = RechargeStatusEnum.values();
        Optional<RechargeStatusEnum> optional = Arrays.stream(statuses).filter(item -> item.status == status)
                .findFirst();
        return optional.isPresent() ? optional.get() : RechargeStatusEnum.RECHARGING;
    }
}
