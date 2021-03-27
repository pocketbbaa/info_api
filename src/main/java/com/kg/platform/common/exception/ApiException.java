package com.kg.platform.common.exception;

/**
 * 
 * @author
 * @version $Id: ApiException.java $
 */
public class ApiException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 853323936341488949L;

    protected String code;

    public ApiException() {
        super();
    }

    public ApiException(String message) {
        super(message);
        this.code = ExceptionEnum.SERVERERROR.getCode();
    }

    public ApiException(ExceptionEnum exception) {
        super(exception.getMessage());
        this.code = exception.getCode();
    }

    public ApiException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
