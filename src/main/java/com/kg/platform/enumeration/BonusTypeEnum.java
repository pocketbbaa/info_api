package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

/**
 * 奖励类型枚举 奖励类型 1 首次浏览文章 2 首次点赞文章 3 首次收藏文章 4 首次分享文章 41 首次分享成功至微信或微博 42 首次分享成功至微信
 * 43 首次分享成功至微博
 */
public enum BonusTypeEnum {

    /**
     * 首次浏览文章
     */
    BROWSE(1, "首次浏览文章"),
    /**
     * 首次点赞文章
     */
    THUMBUP(2, "首次点赞文章"),
    /**
     * 首次收藏文章
     */
    COLLECT(3, "首次收藏文章"),
    /**
     * 首次分享文章
     */
    SHARE(4, "首次分享文章"),
    /**
     * 首次分享成功至微信或微博
     */
    SHARE_BOTH(41, "首次分享成功至微信或微博"),
    /**
     * 首次分享成功至微信
     */
    SHARE_WEIXIN(42, "首次分享成功至微信"),
    /**
     * 首次分享成功至微博
     */
    SHARE_WEIBO(43, "首次分享成功至微博");

    private int type;

    private String display;

    BonusTypeEnum(int type, String display) {
        this.type = type;
        this.display = display;
    }

    public int getType() {
        return type;
    }

    public String getDisplay() {
        return display;
    }

    public static BonusTypeEnum getBonusTypeEnum(int type) {
        BonusTypeEnum statuses[] = BonusTypeEnum.values();
        Optional<BonusTypeEnum> optional = Arrays.stream(statuses).filter(item -> item.type == type).findFirst();
        return optional.isPresent() ? optional.get() : BonusTypeEnum.BROWSE;
    }
}
