package com.kg.platform.enumeration;

import com.kg.platform.common.utils.Constants;

import java.util.Arrays;
import java.util.Optional;

public enum CallMethodEnum {

    FREEZE(Constants.FREEZE,0),
    RELVE(Constants.RELVE,1);

    private String method;

    private Integer type;

    CallMethodEnum(String method,Integer type) {
        this.method = method;
        this.type = type;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    public static CallMethodEnum getType(String method) {
        CallMethodEnum roles[] = CallMethodEnum.values();
        Optional<CallMethodEnum> optional = Arrays.stream(roles).filter(item -> item.method.equals(method)).findFirst();
        return optional.orElse(CallMethodEnum.FREEZE);
    }

}
