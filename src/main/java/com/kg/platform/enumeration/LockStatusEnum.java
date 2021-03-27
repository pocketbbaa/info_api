package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

/**
 * 锁定状态枚举
 */
public enum LockStatusEnum {

    /**
     * 未锁定
     */
    UNLOCK(1, "未锁定"),
    /**
     * 锁定
     */
    LOCKED(2, "已锁定");

    private int status;

    private String display;

    LockStatusEnum(int status, String display) {
        this.status = status;
        this.display = display;
    }

    public int getStatus() {
        return status;
    }

    public String getDisplay() {
        return display;
    }

    public static LockStatusEnum getLockStatusEnum(int status) {
        LockStatusEnum statuses[] = LockStatusEnum.values();
        Optional<LockStatusEnum> optional = Arrays.stream(statuses).filter(item -> item.status == status).findFirst();
        return optional.isPresent() ? optional.get() : LockStatusEnum.UNLOCK;
    }
}
