package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

public enum ColumnLevelEnum {

    TRAINEE(0, "见习作者"),
    ONE_STAR(1, "一星作者"),
    TWO_STAR(2, "二星作者"),
    THREE_STAR(3, "三星作者"),
    FOUR_STAR(4, "四星作者"),
    FIVE_STAR(5, "五星作者");

    private int code;
    private String des;

    ColumnLevelEnum(int code, String des) {
        this.code = code;
        this.des = des;
    }

    public static ColumnLevelEnum getByCode(int code) {
        ColumnLevelEnum roles[] = ColumnLevelEnum.values();
        Optional<ColumnLevelEnum> optional = Arrays.stream(roles).filter(item -> item.code == code).findFirst();
        return optional.orElse(ColumnLevelEnum.TRAINEE);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
