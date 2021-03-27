package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

/**
 * 流水状态枚举
 */
public enum FlowStatusEnum {

    /**
     * 充值中
     */
    RECHARGING(1, "充值中"),
    /**
     * 充值成功
     */
    RECHARGED(2, "充值成功"),
    /**
     * 审核中
     */
    AUDITING(3, "审核中"),
    /**
     * 转出中
     */
    TRANSFERING(4, "转出中"),
    /**
     * 转出失败
     */
    TRANSFER_FAIL(5, "转出失败"),
    /**
     * 转出成功
     */
    TRANSFER_SUCCESS(6, "转出成功"),
    /**
     * 已撤销
     */
    CANCELED(7, "已撤销"),
    /**
     * 未生效
     */
    NOTVALID(8, "未生效"),
    /**
     * 已生效
     */
    VALID(9, "已生效"),
    /**
     * 暂停中
     */
    PAUSED(10, "暂停中"),
    /**
     * 已终止
     */
    TERMINATED(11, "已终止"),
    /**
     * 已结束
     */
    FINISHED(12, "已结束"),
    /**
     * 保证金充值成功
     */
    DEPOSIT_SUCCESSED(13, "保证金充值成功"),
    /**
     * 已发放
     */
    PAYED(14, "已发放"),
    /**
     * 已终止
     */
    FREEZED(15, "已冻结"),
    /**
     * 已结束
     */
    UNFREEZED(16, "已解冻"),

    NOPASS(17,"审核不通过"),

	CONVERTING(18,"兑换中"),

	CONVERT_SUCCESS(19,"兑换成功"),

	CONVERT_FAIL(20,"兑换失败"),

	SIGNED_SUCCESS(21,"签到"),
    /**
     * 无状态流水
     */
    NOSTATUS(-1, "直接生效");

    private int status;

    private String display;

    FlowStatusEnum(int status, String display) {
        this.status = status;
        this.display = display;
    }

    public int getStatus() {
        return status;
    }

    public String getDisplay() {
        return display;
    }

    public static FlowStatusEnum getFlowStatusEnum(int status) {
        FlowStatusEnum statuses[] = FlowStatusEnum.values();
        Optional<FlowStatusEnum> optional = Arrays.stream(statuses).filter(item -> item.status == status).findFirst();
        return optional.isPresent() ? optional.get() : FlowStatusEnum.NOSTATUS;
    }
}
