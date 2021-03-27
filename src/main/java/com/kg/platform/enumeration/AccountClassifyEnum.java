package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

public enum AccountClassifyEnum {

    //分类 0：全部，1：奖励，2：打赏，3：转入，4：转出，5：进贡

    ALL(0, "全部"),
    REWARD(1, "奖励"),
    EXCEPTIONAL(2, "打赏"),
    INTO(3, "转入"),
    OUT(4, "转出"),
    TRIBUTE(5, "进贡");

    private int status;

    private String display;

    AccountClassifyEnum(int status, String display) {
        this.status = status;
        this.display = display;
    }

    public int getStatus() {
        return status;
    }

    public String getDisplay() {
        return display;
    }

    public static AccountClassifyEnum getByStatus(int status) {
        AccountClassifyEnum statuses[] = AccountClassifyEnum.values();
        Optional<AccountClassifyEnum> optional = Arrays.stream(statuses).filter(item -> item.status == status)
                .findFirst();
        return optional.isPresent() ? optional.get() : AccountClassifyEnum.ALL;
    }
}
