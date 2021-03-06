/**
 * piaozhijia.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.kg.platform.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Mf-CX05
 * @version $Id: HtmlUtil.java, v 0.1 2016年10月25日 上午11:19:03 Mf-CX05 Exp $
 */
public class HtmlUtil {
    private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式

    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式

    public static final String regEx_iframe = "<iframe[^>]*?>[\\s\\S]*?<\\/iframe>";// 定义iframe的正则表达式

    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

    private static final String regEx_space = "\\s*|\t|\r|\n"; // 定义空格回车换行符

    /**
     * @param htmlStr
     * @return 删除Html标签
     */
    public static String delHTMLTag(String htmlStr) {
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
        return htmlStr.trim(); // 返回文本字符串
    }

    public static String getTextFromHtml(String htmlStr) {
        htmlStr = delHTMLTag(htmlStr);
        htmlStr = htmlStr.replaceAll("&nbsp;", "");
        return htmlStr;
    }

    public static boolean ifHaveHTMLTag(String htmlStr, String regEx){
        Pattern p_html = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        return m_html.find();
    }

    public static void main(String[] args) {
        String str = "asdrwer<iframe style='text-align:center;'> 整治“四风”</iframe>jkjklsjdfkl"   ;
        System.out.println(ifHaveHTMLTag(str,regEx_iframe));

        // System.out.println(getTextFromHtml("<p>&nbsp;</p><p><span
        // style="color: #333333; font-family: arial, 宋体,
        // sans-serif;">中国</span><a style="color: #136ec2; text-decoration:
        // none; font-family: arial, 宋体, sans-serif;"
        // href="http://baike.baidu.com/view/7627.htm"
        // target="_blank">四川</a><span style="color: #333333; font-family:
        // arial, 宋体, sans-serif;">省乐山市</span><a style="color: #136ec2;
        // text-decoration: none; font-family: arial, 宋体, sans-serif;"
        // href="http://baike.baidu.com/view/399446.htm"
        // target="_blank">峨眉山市</a></p>"));
    }
}
