package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

/**
 * 提现状态 0 审核中 1 已通过 2 已撤销
 */
public enum AccountWithdrawStatusEnum {

    /**
     * 审核中
     */
    AUDITING(0, "审核中"),
    /**
     * 已通过
     */
    PASS(1, "已通过"),
    /**
     * 已撤销
     */
    FAIL(2, "已撤销");

    private int status;

    private String display;

    AccountWithdrawStatusEnum(int status, String display) {
        this.status = status;
        this.display = display;
    }

    public int getStatus() {
        return status;
    }

    public String getDisplay() {
        return display;
    }

    public static AccountWithdrawStatusEnum getAccountWithdrawStatusEnum(int status) {
        AccountWithdrawStatusEnum statuses[] = AccountWithdrawStatusEnum.values();
        Optional<AccountWithdrawStatusEnum> optional = Arrays.stream(statuses).filter(item -> item.status == status)
                .findFirst();
        return optional.isPresent() ? optional.get() : AccountWithdrawStatusEnum.AUDITING;
    }
}
