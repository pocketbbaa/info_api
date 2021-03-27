package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

/**
 * 文章显示状态枚举
 */
public enum ArticleDisplayStatusEnum {

    /**
     * 正常显示
     */
    NORMAL(1, "正常显示"),

    /**
     * 首页置顶
     */
    HOME_TOP(2, "首页置顶"),
    /**
     * 首页推荐
     */
    HOME_RECOMMEND(3, "首页推荐"),
    /**
     * 前台隐藏
     */
    FRONT_HIDDEN(4, "前台隐藏");

    private int status;

    private String display;

    ArticleDisplayStatusEnum(int status, String display) {
        this.status = status;
        this.display = display;
    }

    public int getStatus() {
        return status;
    }

    public String getDisplay() {
        return display;
    }

    public static ArticleDisplayStatusEnum getArticleDisplayStatusEnum(int status) {
        ArticleDisplayStatusEnum statuses[] = ArticleDisplayStatusEnum.values();
        Optional<ArticleDisplayStatusEnum> optional = Arrays.stream(statuses).filter(item -> item.status == status)
                .findFirst();
        return optional.isPresent() ? optional.get() : ArticleDisplayStatusEnum.NORMAL;
    }
}
