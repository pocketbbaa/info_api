package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

/**
 * 审核状态枚举
 */
public enum AuditStatusEnum {

    /**
     * 审核中
     */
    AUDITING(0, "审核中"),
    /**
     * 通过
     */
    PASS(1, "通过"),
    /**
     * 不通过
     */
    REFUSE(2, "不通过");

    private int status;

    private String display;

    AuditStatusEnum(int status, String display) {
        this.status = status;
        this.display = display;
    }

    public int getStatus() {
        return status;
    }

    public String getDisplay() {
        return display;
    }

    public static AuditStatusEnum getAuditStatusEnum(int status) {
        AuditStatusEnum statuses[] = AuditStatusEnum.values();
        Optional<AuditStatusEnum> optional = Arrays.stream(statuses).filter(item -> item.status == status).findFirst();
        return optional.isPresent() ? optional.get() : AuditStatusEnum.PASS;
    }
}
