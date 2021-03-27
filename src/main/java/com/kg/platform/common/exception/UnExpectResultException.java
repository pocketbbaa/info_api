package com.kg.platform.common.exception;

/**
 * 
 * @author Mark
 * @version $Id: UnExpectResultException.java, v 0.1 2016年7月15日 下午6:00:06
 *          pengliqing Exp $
 */
public class UnExpectResultException extends ApiException {

    /**  */
    private static final long serialVersionUID = 1L;

    public UnExpectResultException(ExceptionEnum exception) {
        super(exception);
    }

    public UnExpectResultException(String code, String message) {
        super(code, message);
    }
}
