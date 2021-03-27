package com.kg.platform.common.exception;

/**
 * 
 * @author Mark
 * @version $Id: ParamException.java, v 0.1 2016年7月13日 下午9:51:32 pengliqing Exp
 *          $
 */
public class ParamException extends ApiException {

    /**  */
    private static final long serialVersionUID = 1L;

    public ParamException(ExceptionEnum exception) {
        super(exception);
    }

    public ParamException(String code, String message) {
        super(code, message);
    }
}
