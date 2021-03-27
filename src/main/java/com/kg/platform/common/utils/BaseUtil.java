package com.kg.platform.common.utils;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

public class BaseUtil {

    public String checkNull(String obj) {
        if (obj == null) {
            return "";
        } else {
            return obj;
        }
    }

    public static int stringLength(String value) {
        int valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        if (value != null && !"".equals(value)) {
            for (int i = 0; i < value.length(); i++) {
                String temp = value.substring(i, i + 1);
                if (temp.matches(chinese)) {
                    valueLength += 2;
                } else {
                    valueLength += 1;
                }
            }
        }
        return valueLength;
    }

    // 生成6位随机码
    public static String generationCode() {
        StringBuffer s = new StringBuffer();
        while (s.length() < 6) {
            s.append((int) (Math.random() * 10) + "");
        }
        return s.toString();
    }

    // 生成8位随机码
    public static String generationCodePwd() {
        StringBuffer s = new StringBuffer();
        while (s.length() < 8) {
            s.append((int) (Math.random() * 10) + "");
        }
        return s.toString();
    }

    // 验证非空去空格
    public static String isNullReplace(String value) {
        if (value == null) {
            return "";
        } else {
            if (value != null && !"".equals(value)) {
                value = value.replace(" ", "");
            }
            return value;
        }
    }

    // 验证非空去空格
    public static String replace(String value) {
        if (value != null && !"".equals(value)) {
            value = value.replace(" ", "");
        }
        return value;
    }

    // 字符串转换成字节数组
    public static byte[] stringToByte(String str) {
        byte[] destObj = null;
        try {
            if (null == str || str.trim().equals("")) {
                destObj = new byte[0];
                return destObj;
            } else {
                destObj = str.getBytes("UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return destObj;
    }

    /**
     * Url Base64编码
     * 
     * @param data
     *            待编码数据
     * @return String 编码数据
     * @throws Exception
     */
    public static String encode(String data) throws Exception {
        // 执行编码
        byte[] b = Base64.encodeBase64URLSafe(data.getBytes("UTF-8"));
        return new String(b, "UTF-8");
    }

    public static String decode(String data) throws Exception {
        // 执行解码
        byte[] b = Base64.decodeBase64(data.getBytes("UTF-8"));
        return new String(b, "UTF-8");
    }

}
