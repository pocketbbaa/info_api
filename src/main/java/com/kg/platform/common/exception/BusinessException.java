package com.kg.platform.common.exception;

/**
 * Service层异常
 * 
 * @author dqzhai
 * @version 1.0
 */
public class BusinessException extends SystemException {
    private static final long serialVersionUID = 5362638549651827865L;

    public BusinessException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public BusinessException(String errorCode, String message) {
        super(errorCode, message);
    }

    public BusinessException(Integer errorCode, String message) {
        super(errorCode + "", message);
    }

    public BusinessException(ExceptionEnum exception) {
        super(exception.getCode(), exception.getMessage());
    }

}
