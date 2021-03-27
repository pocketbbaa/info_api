package com.kg.platform.common.utils;

public class Threads {

    /**
     * 获取异常发生处的 exception : e.getMessage() ON 类名【方法名】at line 代码行数
     * exception : / by zero ON com.kg.platform.common.utils.Threads【main】at line 41
     */
    public static String getExceptionLocation(Exception e) {
        try {
            StackTraceElement[] stackTraceElements = e.getStackTrace();
//            for (StackTraceElement s : stackTraceElements) {
//                System.err.println("exception : (" + e.toString() + ") ON " + s.getClassName() + "【" + s.getMethodName() + "】at line " + s.getLineNumber());
//            }
            StackTraceElement ste = stackTraceElements[stackTraceElements.length - 1];
            return "exception : (" + e.toString() + ") ON " + ste.getClassName() + "【" + ste.getMethodName() + "】at line " + ste.getLineNumber();
        } catch (Exception ex) {
            return StringUtils.EMPTY;
        }
    }

    public static void main(String[] args) {
        try {
            Long.valueOf(null);
        } catch (Exception e) {
            System.out.println(getExceptionLocation(e));
        }
    }

}
