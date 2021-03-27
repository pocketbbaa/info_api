package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

/**
 * 业务类型枚举
 */
public enum BusinessTypeEnum {

    /**
     * 充值
     */
    RECHARGE(10, "充值"),
    /**
     * 提币
     */
    WITHDRAW(20, "提币"),
    /**
     * 发放奖励
     */
    AWARD(30, "发放奖励"),
    /**
     * 打赏-收入
     */
    TIPSIN(40, "打赏-收入"),
    /**
     * 打赏-支出
     */
    TIPSOUT(50, "打赏-支出"),
    /**
     * 缴纳保证金
     */
    DEPOSITIN(60, "缴纳保证金"),
    /**
     * 获取实名认证奖励
     */
    REALNAMEBONUS(70, "获取实名认证奖励"),
    /**
     * 获取专栏作者奖励
     */
    USERCOLUMNBONUS(80, "成为专栏作者获得奖励"),

    /**
     * 邀新奖励
     */
    INVITEBONUS(90, "邀新奖励"),

    /**
     * 阅读奖励-浏览
     */
    BROWSEAWARD(310, "阅读奖励-浏览"),
    /**
     * 阅读奖励-点赞
     */
    THUMBUPAWARD(320, "阅读奖励-点赞"),

    /**
     * 阅读奖励-分享
     */
    SHAREAWARD(330, "阅读奖励-分享"),

    /**
     * 阅读奖励-收藏
     */
    COLLECTAWARD(340, "阅读奖励-收藏"),
    /**
     * 发放阅读奖励
     */
    READAWARD(350, "发放阅读奖励"),

    /**
     * 阅读文章氪金奖励
     */
    READARTICLETTVAWARD(510, "获得平台阅读文章奖励"),

    /**
     * 获取分享文章钛值奖励
     */
    SHAREARTICLETVAWARD(520, "获取分享奖励"),

    /**
     * 获得徒弟进贡Tv
     */
    SUBCONTRITV(530, "获得徒弟进贡"),

    /**
     * 获取发文奖励
     */
    PUBLISHAWARD(540, "获取发文奖励"),

    /**
     * 获取原创奖励
     */
    CREATERAWARD(550, "获取原创奖励"),

    /**
     * 平台文章钛值额外奖励
     */
    ADDEDTVBONUS(560, "平台文章钛值额外奖励"),

    /**
     * 平台千氪钛值奖励
     */
    PLATFORMTVBONUS(570, "平台千氪钛值奖励"),

    /**
     * 师徒打赏-收入
     */
    TPREWARDINCOME(640, "获取发文奖励"),

    /**
     * 师徒打赏-支付
     */
    TPREWARDPAY(650, "获取发文奖励"),

    /**
     * 消耗钛值-支付
     */
    REDUCETV(660, "消耗钛值"),

    /**
     * 获取登录奖励
     */
    LOGINBONUS(1000, "获取登录奖励"),
    /**
     * 氪金充值
     */
    RECHARGETXB(1010, "氪金充值"),
    /**
     * 氪金提币
     */
    WITHDRAWTXB(1020, "氪金提币"),

    /**
     * 阅读文章氪金奖励
     */
    READARTICLETKGAWARD(1510, "获得平台阅读文章奖励"),

    /**
     * 分享文章氪金奖励
     */
    SHAREARTICLETKGAWARD(1520, "获取分享氪金奖励"),

    /**
     * 获得徒弟进贡
     */
    SUBCONTRI(1530, "获得徒弟进贡"),

    /**
     * 平台文章氪金额外奖励
     */
    ADDEDTXBBONUS(1560, "平台文章氪金额外奖励"),

    /**
     * 平台千氪氪金奖励
     */
    PLATFORMKGBONUS(1570, "平台千氪奖励(KG)"),

    /**
     * 消耗氪金-支付
     */
    REDUCEKG(1660, "消耗氪金"),

    /**
     * 千氪奖励-rit
     */
    PLATFORMRITBONUS(2000, "平台千氪RIT奖励"),


    WITHDRAW_NEW(2001, "转出"),

    WITHDRAW_FROZEN(2002, "转出（冻结）"),

    WITHDRAW_NO_PASS(2003, "转出申请不通过"),

    WITHDRAW_CANCEL(2004, "撤销转出申请"),

    WITHDRAW_FAIL(2005, "转出失败"),

    WITHDRAW_SUCCESS(2006, "转出成功（冻结）"),

    WITHDRAW_FEE(2007, "转出手续费"),

    WITHDRAW_CANCEL_FROZEN(2008, "撤销转出申请（冻结）"),

    WITHDRAW_FAIL_FROZEN(2009, "转出失败（冻结）"),

    WITHDRAW_NO_PASS_FROZEN(2010, "转出申请不通过（冻结）"),

    CONVERT_RIT_IN(2100, "兑换"),

    CONVERT_KG_OUT(2101, "兑换"),

    PLATFORMFRZEERITBONUS(2102, "平台千氪RIT奖励(冻结)"),

    UNFREZEE_FROZEN(2103, "解冻冻结余额(冻结)"),

    UNFREZEE_BANLANCE(2104, "解冻冻结余额"),

    FREZEE_FROZEN(2105, "冻结可用余额(冻结)"),

    FREZEE_BANLANCE(2106, "冻结可用余额"),

    CONVERT_DUIBA_KG_OUT(2107, "兑换"),

    CONVERT_DUIBA_KG_OUT_FROZEN(2108, "兑换(冻结)"),

    CONVERT_DUIBA_KG_OUT_FAIL(2109, "兑换失败"),

    CONVERT_DUIBA_KG_OUT_FAIL_FROZEN(2110, "兑换失败(冻结)"),

    SIGNED_KG_BALANCE(2111, "获取签到奖励"),

    SIGNED_KG_PLATFORM_BALANCE(2112, "签到奖励"),

    KG_REWARD_FIRST_POST(2113, "首次发文奖励"),

    KG_REWARD_SET_QUALITY(2114, "文章设置优质原创奖励"),

    KG_REWARD_SET_TOP(2115, "文章设置置顶奖励"),

    KG_REWARD_SET_RECOMMEND(2116, "文章设置推荐奖励"),

    KG_CLOUD_BTC_EARNINGS(3000, "云算力收益");


    private int status;

    private String display;

    BusinessTypeEnum(int status, String display) {
        this.status = status;
        this.display = display;
    }

    public int getStatus() {
        return status;
    }

    public String getDisplay() {
        return display;
    }

    public static BusinessTypeEnum getBusinessTypeEnum(int status) {
        BusinessTypeEnum statuses[] = BusinessTypeEnum.values();
        Optional<BusinessTypeEnum> optional = Arrays.stream(statuses).filter(item -> item.status == status).findFirst();
        return optional.isPresent() ? optional.get() : BusinessTypeEnum.RECHARGE;
    }
}
