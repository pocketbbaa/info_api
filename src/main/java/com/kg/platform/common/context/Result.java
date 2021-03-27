package com.kg.platform.common.context;

import com.kg.platform.common.exception.ExceptionEnum;

import java.io.Serializable;

public class Result<T> implements Serializable {

    private static final long serialVersionUID = -6599004046123162578L;

    public Result() {
        this(10000, "ok");
    }

    public Result(T data) {
        super();
        this.errorCode = Result.OK;
        this.data = data;
    }

    public Result(int errorCode, String errorMsg) {
        super();
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public Result(int errorCode, String errorMsg, T data) {
        super();
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    public Result(ExceptionEnum exceptionEnum) {
        super();
        this.errorCode = Integer.valueOf(exceptionEnum.getCode());
        this.errorMsg = exceptionEnum.getMessage();
    }

    private static final int OK = 10000;

    private int errorCode;

    private String errorMsg;

    private T data;

    public boolean isOk() {
        return errorCode == Result.OK;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public Result<T> setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public Result<T> setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
