package com.kg.platform.common.utils;


public class EmojiUtil {


    public static String filterUtf8mb4(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        final int LAST_BMP = 0xFFFF;
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            int codePoint = str.codePointAt(i);
            if (codePoint < LAST_BMP) {
                sb.appendCodePoint(codePoint);
            } else {
                i++;
            }
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        String str = "\uD83D\uDC68\uD83C\uDF90\uD83D\uDCFA\uD83D\uDCFA\uD83D\uDCEF\uD83C\uDF14\uD83C\uDF12\uD83C\uDF1A阿大胆可视对讲\uD83D\uDC68";
        String str2 = "\uD83D\uDC66爱是滴啊US很大声丢安徽省对哈\uD83D\uDC67\uD83D\uDC68\uD83D\uDC69\uD83D\uDC6B\uD83D\uDC6E\uD83D\uDC71\uD83D\uDC72\uD83D\uDC73\uD83D\uDC74";
//        System.out.println(str);
        System.out.println(filterUtf8mb4(str2));
    }
}
