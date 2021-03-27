package com.kg.platform.common.exception;

/**
 * 
 * @author
 * @version $Id: ApiMessageException.java $
 */
public class ApiMessageException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -6642853129794871992L;

    protected String code;

    public ApiMessageException() {
        super();
    }

    public ApiMessageException(String message) {
        super(message);
        this.code = ExceptionEnum.SERVERERROR.getCode();
    }

    public ApiMessageException(ExceptionEnum exception) {
        super(exception.getMessage());
        this.code = exception.getCode();
    }

    public ApiMessageException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
