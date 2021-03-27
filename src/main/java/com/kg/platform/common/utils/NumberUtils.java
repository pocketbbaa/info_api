package com.kg.platform.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 安全的数字类型处理工具类
 */
public final class NumberUtils {

    private NumberUtils() {
        throw new AssertionError();
    }

    public static final int intValue(Integer value) {
        return null != value ? value.intValue() : 0;
    }

    public static final long longValue(Long value) {
        return null != value ? value.longValue() : 0;
    }

    public static final BigDecimal getBigDecimal(String value) {
        if (value == null) {
            return BigDecimal.valueOf(0.000d);
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(3, BigDecimal.ROUND_FLOOR);
        return bd;
    }

    public static final String formatBigDecimal(BigDecimal bd) {
        // BigDecimal bd = new BigDecimal(value);
        if (bd == null) {
            return "0.000";
        }
        bd = bd.setScale(3, BigDecimal.ROUND_FLOOR);
        return bd.toString();
    }

    public static final String formatBigDecimalForMyCoinList(BigDecimal bd) {
        // BigDecimal bd = new BigDecimal(value);
        if (bd == null) {
            return "0";
        }
        return bd.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
    }

    public static final String getBigDecimal(BigDecimal bd) {
        if (bd == null) {
            return "0.000";
        }
        bd = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
        return bd.toString();
    }

    public static final BigDecimal rBigDecimal(BigDecimal bd) {
        if (bd == null) {
            return BigDecimal.valueOf(0.000);
        }
        bd = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    public static void main(String[] args) {

        BigDecimal decimal = new BigDecimal("0.10000012312300");

        System.out.println(formatBigDecimalForMyCoinList(decimal));
    }
}
