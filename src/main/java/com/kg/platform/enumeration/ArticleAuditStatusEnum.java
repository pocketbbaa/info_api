package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

/**
 * 文章审核状态枚举
 */
public enum ArticleAuditStatusEnum {

    /**
     * 草稿
     */
    DRAFT(0, "草稿"),

    /**
     * 审核中
     */
    AUDITING(2, "审核中"),
    /**
     * 通过
     */
    PASS(1, "通过"),
    /**
     * 不通过
     */
    REFUSE(3, "不通过");

    private int status;

    private String display;

    ArticleAuditStatusEnum(int status, String display) {
        this.status = status;
        this.display = display;
    }

    public int getStatus() {
        return status;
    }

    public String getDisplay() {
        return display;
    }

    public static ArticleAuditStatusEnum getArticleAuditStatusEnum(int status) {
        ArticleAuditStatusEnum statuses[] = ArticleAuditStatusEnum.values();
        Optional<ArticleAuditStatusEnum> optional = Arrays.stream(statuses).filter(item -> item.status == status)
                .findFirst();
        return optional.isPresent() ? optional.get() : ArticleAuditStatusEnum.PASS;
    }
}
