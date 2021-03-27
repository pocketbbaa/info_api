package com.kg.platform.common.exception;

/**
 * 数据检查错误父类
 * 
 * @author MF-CX-2
 * @version $Id: JsonBindException.java, v 0.1 2017年1月3日 下午12:03:33 MF-CX-2 Exp
 *          $
 */
public class JsonBindException extends RuntimeException {

    /**
     * Signals that an error occurred while attempting to bind a socket to a
     * local address and port. Typically, the port is in use, or the requested
     * local address could not be assigned.
     *
     * @since JDK1.1
     */

    private static final long serialVersionUID = -5945005768251722951L;

    /**
     * Constructs a new BindException with the specified detail message as to
     * why the bind error occurred. A detail message is a String that gives a
     * specific description of this error.
     * 
     * @param msg
     *            the detail message
     */
    public JsonBindException(String msg) {
        super(msg);
    }

    /**
     * Construct a new BindException with no detailed message.
     */
    public JsonBindException() {
    }
}
