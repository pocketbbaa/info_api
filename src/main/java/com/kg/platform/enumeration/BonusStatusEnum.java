package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

/**
 * 奖励状态枚举 奖励状态 0 禁用（未生效） 1启用（已生效） 2 暂停中 3 已终止 4 已结束
 */
public enum BonusStatusEnum {

    /**
     * 未生效
     */
    NOTVALID(0, "未生效"),
    /**
     * 已生效
     */
    VALID(1, "已生效"),
    /**
     * 暂停中
     */
    PAUSED(2, "暂停中"),
    /**
     * 已终止
     */
    TERMINATED(3, "已终止"),
    /**
     * 已结束
     */
    FINISHED(4, "已结束");

    private int status;

    private String display;

    BonusStatusEnum(int status, String display) {
        this.status = status;
        this.display = display;
    }

    public int getStatus() {
        return status;
    }

    public String getDisplay() {
        return display;
    }

    public static BonusStatusEnum getBonusStatusEnum(int status) {
        BonusStatusEnum statuses[] = BonusStatusEnum.values();
        Optional<BonusStatusEnum> optional = Arrays.stream(statuses).filter(item -> item.status == status).findFirst();
        return optional.isPresent() ? optional.get() : BonusStatusEnum.NOTVALID;
    }
}
