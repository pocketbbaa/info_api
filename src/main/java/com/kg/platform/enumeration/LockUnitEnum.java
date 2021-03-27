package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

/**
 * 锁定单位枚举 时长单位 1 年 2 月 3 周 4 天 5 小时 0 永久锁定
 */
public enum LockUnitEnum {

    /**
     * 年
     */
    YEAR(1, "年"),
    /**
     * 月
     */
    MONTH(2, "月"),
    /**
     * 周
     */
    WEEK(3, "周"),
    /**
     * 天
     */
    DAY(4, "天"),
    /**
     * 小时
     */
    HOUR(5, "小时"),
    /**
     * 永久锁定
     */
    FOREVER(0, "永久锁定");

    private int unit;

    private String display;

    LockUnitEnum(int unit, String display) {
        this.unit = unit;
        this.display = display;
    }

    public int getUnit() {
        return unit;
    }

    public String getDisplay() {
        return display;
    }

    public static LockUnitEnum getLockStatusEnum(int unit) {
        LockUnitEnum units[] = LockUnitEnum.values();
        Optional<LockUnitEnum> optional = Arrays.stream(units).filter(item -> item.unit == unit).findFirst();
        return optional.isPresent() ? optional.get() : LockUnitEnum.FOREVER;
    }
}
