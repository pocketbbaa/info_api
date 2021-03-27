package com.kg.platform.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.exception.ApiException;

/**
 * @author wang_yugong
 * @version v1.0
 * @date: 2012-04-15
 */
public class CheckUtils {
    private static final Logger logger = LoggerFactory.getLogger(CheckUtils.class);

    /**
     * 检查底层返回是否正确
     *
     * @param retInfo
     */
    public static boolean checkRetInfo(Result<?> retInfo) {
        return checkRetInfo(retInfo, false);
    }

    /**
     * 检查底层返回是否正确
     *
     * @param retInfo
     * @param isCheckDateNull 是否需要检查data为空
     */
    public static boolean checkRetInfo(Result<?> retInfo, boolean isCheckDataNull) {
        logger.debug("返回参数：" + JSON.toJSONString(retInfo));
        if (retInfo == null) {
            if (isCheckDataNull) {
                logger.error("返回参数为空" + JSON.toJSONString(retInfo));
                throw new ApiException("返回data为空");
            }
            return false;
        }
        if (!retInfo.isOk()) {
            logger.error("返回参数：" + JSON.toJSONString(retInfo));

            throw new ApiException(retInfo.getErrorMsg());
        }
        if (isCheckDataNull) {
            if (retInfo.getData() == null) {
                throw new ApiException("返回data为空");
            }
        }
        return true;
    }

    /**
     * 检查底层返回是否正确
     *
     * @param retInfo
     */
    public static boolean checkUserResult(Result<?> retInfo) {
        logger.debug("返回参数：" + JSON.toJSONString(retInfo));
        if (retInfo == null) {
            logger.error("返回参数为空" + JSON.toJSONString(retInfo));
            throw new ApiException("返回data为空");
        }
        if (!retInfo.isOk()) {
            logger.error("返回参数：" + JSON.toJSONString(retInfo));
            throw new ApiException(retInfo.getErrorMsg());
        }
        return true;
    }

    /**
     * 检查返回值是否为空并且返回data
     *
     * @param retInfo
     * @param isCheckDateNull
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T checkNullAndRetData(Result<?> retInfo) {
        checkRetInfo(retInfo, true);
        return (T) retInfo.getData();
    }

    /**
     * 检查是否为空并且是否需要抛错
     *
     * @param retInfo
     * @param isError 如果为false 表示 对结果集data 做非空检查 不做异常处理 为 true 表示 做为空检查 并且抛出返回异常
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T checkNullAndRetData(Result<?> retInfo, boolean isError) {
        if (!isError) {
            if (retInfo != null && retInfo.isOk() && retInfo.getData() != null) {
                return (T) retInfo.getData();
            } else {
                logger.error("返回报错：" + JSON.toJSONString(retInfo));
            }
            return null;
        } else {
            return checkNullAndRetData(retInfo);
        }
    }

    public static boolean isEmpty(Object input) {
        if (null == input) {
            return true;
        }
        if (input instanceof String) {
            if ("".equals(input.toString().trim())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotEmpty(Object input) {
        return !isEmpty(input);
    }

    public static boolean isNotNull(String input) {
        return null != input && !input.trim().equals("") && !input.trim().equals("null")
                && !input.trim().equals("undefined") && !input.trim().equals("-1");
    }

    public static boolean isNotNull(Long input) {
        return null != input;
    }

    public static boolean isNotNull(Integer input) {
        return null != input;
    }

    public static boolean isNotNull(Date input) {
        return null != input;
    }

    public static boolean isNotNull(Object input) {
        return null != input;
    }

    public static boolean isNotNull(StringBuffer input) {
        return null != input && !input.toString().trim().equals("");
    }

    public static boolean isNotNull(List<?> input) {
        return null != input && input.size() > 0;
    }

    public static boolean isNotNull(Map<?, ?> input) {
        return null != input && !input.isEmpty();
    }

    public static boolean isNotNull(Set<?> input) {
        return null != input && !input.isEmpty();
    }

    public static boolean isNotNull(String[] input) {
        return null != input && input.length > 0;
    }

    public static boolean isNotNull(Object[] input) {
        return null != input && input.length > 0;
    }

    public static boolean isNotNull(Class<?> input) {
        return null != input;
    }

    public static boolean isNull(String arg) {
        boolean isNull = true;
        if (arg != null && !"".equals(arg.trim())) {
            isNull = false;
        }
        return isNull;
    }

    public static boolean isNull(int arg) throws Exception {
        boolean isNull = true;
        if (arg != -1) {
            isNull = false;
        }
        return isNull;
    }

    public static boolean isNull(long arg) throws Exception {
        boolean isNull = true;
        if (arg != -1) {
            isNull = false;
        }
        return isNull;
    }

    public static boolean isNull(Map<?, ?> arg) throws Exception {
        boolean isNull = true;
        if (arg != null && !arg.isEmpty()) {
            isNull = false;
        }
        return isNull;
    }

    public static boolean isNull(Object arg) {
        boolean isNull = true;
        if (arg != null) {
            isNull = false;
        }
        return isNull;
    }

    public static boolean uniqueResult(Object uniqueResult, int target) throws Exception {
        boolean isNull = false;
        if (null == uniqueResult || uniqueResult.toString().trim().equals("")
                || Integer.parseInt(uniqueResult.toString().trim()) == target) {
            isNull = true;
        }
        return isNull;
    }

    public static boolean uniqueResult(Object uniqueResult) throws Exception {
        boolean isNull = false;
        if (null == uniqueResult || uniqueResult.toString().trim().equals("")) {
            isNull = true;
        }
        return isNull;
    }

    /**
     * 全角转半角
     *
     * @param input
     * @return String
     * @author zhaolu add
     * @date 2012-04-20
     */
    public static String QtoB(String input) {
        input = input.trim();
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    /**
     * 半角转全角
     *
     * @param input
     * @return String
     * @author zhaolu add
     * @date 2012-04-20
     */
    public static String BtoQ(String input) {
        input = input.trim();
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }

    /**
     * 判断字符串长度是否在指定的数值范围内
     *
     * @param String str,int len 需判断的字符串和限制的长度
     * @return boolean 布尔值
     * @author zhaolu add
     * @date 2012-04-20
     */
    public static boolean checkStringLengrth(String str, int len) {
        return str.length() <= len;
    }

    /**
     * 日期格式的字符串转换为日期
     *
     * @param dateStr   日期格式的字符串
     * @param formatStr 需转换后的格式
     * @return Date 日期
     * @throws ParseException
     * @author zhaolu add
     * @date 2012-04-25
     */
    public static Date formatDate(String dateStr, String formatStr) throws ParseException {
        if (isNotNull(dateStr) && isNotNull(formatStr)) {
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
            date = formatter.parse(dateStr);
            return date;
        } else {
            return null;
        }

    }

    /**
     * 日期转换为日期格式的字符串
     *
     * @param dateStr   日期格式的字符串
     * @param formatStr 需转换后的格式
     * @return Date 日期
     * @throws ParseException
     * @author zhaolu add
     * @date 2012-04-25
     */
    public static String getStringDate(Date date, String formatStr) {
        String dateStr = "";
        if (isNotNull(date) && isNotNull(formatStr)) {
            SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
            dateStr = formatter.format(date);
        }
        return dateStr;
    }

    public static boolean isDate(String date) {
        boolean flag = false;
        try {
            if (date != null && !date.equals("")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                sdf.parse(date);
                flag = true;
            }
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }
}
