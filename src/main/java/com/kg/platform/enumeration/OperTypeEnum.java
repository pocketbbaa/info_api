package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

/**
 * 提现操作类型枚举
 */
public enum OperTypeEnum {

    WITHDRAW_PENDING(0, "待审核"),

    WITHDRAW_WATING(1, "等待转出"),

    WITHDRAW_NO_PASS(2, "不通过"),

    WITHDRAW_SUCCESS(3, "已转出"),

    WITHDRAW_FAIL(4, "转出失败"),

    WITHDRAW_CANCEL(5, "已撤销"),

    WITHDRAW_OPER(6,"操作中");

    private int status;

    private String display;

    OperTypeEnum(int status, String display) {
        this.status = status;
        this.display = display;
    }

    public int getStatus() {
        return status;
    }

    public String getDisplay() {
        return display;
    }

    public static OperTypeEnum getBusinessTypeEnum(int status) {
        OperTypeEnum statuses[] = OperTypeEnum.values();
        Optional<OperTypeEnum> optional = Arrays.stream(statuses).filter(item -> item.status == status).findFirst();
        return optional.isPresent() ? optional.get() : OperTypeEnum.WITHDRAW_PENDING;
    }
}
