package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

/**
 * 评论审核状态枚举
 */
public enum CommentAuditStatusEnum {

    /**
     * 不需要审核
     */
    NO_NEED(0, "不需要审核"),

    /**
     * 需要审核
     */
    NEED(1, "需要审核");

    private int status;

    private String display;

    CommentAuditStatusEnum(int status, String display) {
        this.status = status;
        this.display = display;
    }

    public int getStatus() {
        return status;
    }

    public String getDisplay() {
        return display;
    }

    public static CommentAuditStatusEnum getCommentAuditStatusEnum(int status) {
        CommentAuditStatusEnum statuses[] = CommentAuditStatusEnum.values();
        Optional<CommentAuditStatusEnum> optional = Arrays.stream(statuses).filter(item -> item.status == status)
                .findFirst();
        return optional.isPresent() ? optional.get() : CommentAuditStatusEnum.NO_NEED;
    }
}
