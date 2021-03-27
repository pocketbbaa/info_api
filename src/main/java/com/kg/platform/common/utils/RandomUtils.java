package com.kg.platform.common.utils;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.google.common.collect.Lists;

/**
 * ClassName: RandomUtils
 * 
 * @Description: 随机信息处理类
 * @author dengjie
 * @date 2016年9月14日
 */
public final class RandomUtils {
    /** 符号 */
    private static final char[] SYMBOL = "\"`~!@#$%^&*()_+-={}[]|:;'<>?,./'\\".toCharArray();

    /** 字母 */
    private static final char[] LETTER = "23456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ".toCharArray();

    public static String genRandomPassword() {
        return shuffle(getRandomString(6) + getRandomNum(6) + getRandomSymbol(6) + getRandLetter(6));
    }

    /**
     * @Title: getRandomString
     * @Description: 生成随机字符串
     * @param length
     *            生成后的字符串长度
     * @return 随机字符串
     */
    public static String getRandomString(int length) {
        if (length < 2) {
            throw new RuntimeException("the length of random string must greater 2");
        }
      //  int num = length / 5;
      //  if (num == 0) {
      //      num = 1;
      //  }

       // String numStr = getRandomNum(num);
        String letterStr = getRandLetter(length);
        return shuffle(letterStr);
    }

    /**
     * @Title: getRandomNum
     * @Description: 生成随机数字
     * @param length
     *            生成后的字符串长度
     * @return 随机数字
     */
    public static String getRandomNum(int length) {
        StringBuilder generateRandStr = new StringBuilder(length);
        Random rand = new Random();
        for (int i = length; i > 0; i--) {
            generateRandStr.append(rand.nextInt(9));
        }
        return generateRandStr.toString();
    }

    /**
     * @Title: getRandomNum
     * @Description: 生成随机数字
     * @param length
     *            生成后的字符串长度
     * @return 随机数字
     */
    public static String getRandomClub(int length) {
        StringBuilder generateRandStr = new StringBuilder(length);
        Random rand = new Random();
        for (int i = length; i > 0; i--) {
            generateRandStr.append(rand.nextInt(9));

        }
        return generateRandStr.toString();
    }

    /**
     * @Title: getRandomSymbol
     * @Description: 随机生成符号串
     * @param length
     *            生成后的字符串长度
     * @return 生成的符号串
     */
    public static String getRandomSymbol(int length) {
        StringBuilder generateRandStr = new StringBuilder(length);
        Random rand = new Random();
        for (int i = length; i > 0; i--) {
            int randNum = rand.nextInt(SYMBOL.length - 1);
            generateRandStr.append(SYMBOL[randNum]);
        }
        return generateRandStr.toString();
    }

    /**
     * @Title: getRandLetter
     * @Description: 随机生成字母串
     * @param length
     *            生成后的字符串长度
     * @return 字母串
     */
    public static String getRandLetter(int length) {
        StringBuilder generateRandStr = new StringBuilder(length);
        Random rand = new Random();
        for (int i = length; i > 0; i--) {
            int randNum = rand.nextInt(LETTER.length - 1);
            generateRandStr.append(LETTER[randNum]);
        }
        return generateRandStr.toString();
    }

    /**
     * @Title: getRandom
     * @Description: 随机生成8位以上包含字母, 数字, 符号的字符串
     * @param length
     *            生成后的字符串长度
     * @return 8位以上包含字母, 数字, 符号的字符串
     */
    public static String getRandom(int length) {
        if (length < 8) {
            length = 8;
        }

        StringBuilder builder = new StringBuilder(length);

        // 随机数字
        int num = new Random().nextInt(length / 2);
        if (num == 0) {
            num = 2;
        }
        builder.append(getRandomNum(num));

        // 3个符号
        int symbol = new Random().nextInt(length / 2);
        if (symbol == 0) {
            symbol = 3;
        }
        builder.append(getRandomSymbol(symbol));

        // length-num-symbol个字母
        builder.append(getRandLetter(length - num - symbol));
        return shuffle(builder.toString());
    }

    /**
     * @Title: shuffle
     * @Description: 随机排序字符串
     * @param str
     *            字符串
     * @return 排序后字符串
     */
    private static String shuffle(String str) {
        char[] chars = str.toCharArray();
        Random rnd = new Random();

        for (int i = chars.length - 1; i > 0; i--) {
            swap(chars, i, rnd.nextInt(i));
        }

        for (int i = chars.length - 1; i > 0; i--) {
            swap(chars, i, rnd.nextInt(i));
        }

        return new String(chars);
    }

    /** Swaps the two specified elements in the specified array. */
    private static void swap(char[] arr, int i, int j) {
        char tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    // 登录注册验证生成6位随机码
    public static String generationCode() {
        // StringBuffer s = new StringBuffer();
        // while (s.length() < 6) {
        // s.append((int) (Math.random() * 10) + "");
        // }
        // return s.toString();
        return String.format("%06d", randomInt(000000, 999999));
    }

    public static int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static double randomDouble(double min, double max) {
        if (max <= min) {
            max = min + 0.001d;
        }
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public static void main(String[] args) {
        ArrayList<String> strs = Lists.newArrayList();
        for (int i = 0; i < 1000000; i++) {
            // System.out.println(NumberUtils.getBigDecimal(BigDecimal.valueOf(randomDouble(0.001,
            // 0.002))));
            String str = getRandomString(6);
            if (strs.contains(str)) {
                System.err.println(str + " 重复");
            } else {
                strs.add(getRandomString(6));
            }

        }
    }
}
