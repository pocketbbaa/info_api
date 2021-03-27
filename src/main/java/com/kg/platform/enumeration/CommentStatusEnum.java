package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

/**
 * 评论审核状态枚举
 */
public enum CommentStatusEnum {

    /**
     * 待审核
     */
    WAIT(0, "审核中"),
    /**
     * 无需审核
     */
    AUDIT(1, "已通过"),
    /**
     * 审核拒绝
     */
    REFUSE(2, "已拒绝");

    private int status;

    private String display;

    CommentStatusEnum(int status, String display) {
        this.status = status;
        this.display = display;
    }

    public int getStatus() {
        return status;
    }

    public String getDisplay() {
        return display;
    }

    public static CommentStatusEnum getCommentStatusEnum(int status) {
        CommentStatusEnum statuses[] = CommentStatusEnum.values();
        Optional<CommentStatusEnum> optional = Arrays.stream(statuses).filter(item -> item.status == status)
                .findFirst();
        return optional.isPresent() ? optional.get() : CommentStatusEnum.AUDIT;
    }
}
