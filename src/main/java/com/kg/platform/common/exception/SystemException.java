package com.kg.platform.common.exception;

public class SystemException extends RuntimeException {
    private static final long serialVersionUID = 5362638549651827865L;

    private String errorCode;

    public SystemException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public SystemException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}
