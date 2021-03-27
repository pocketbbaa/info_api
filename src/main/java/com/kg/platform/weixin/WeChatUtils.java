package com.kg.platform.weixin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.ServiceException;
import com.google.common.collect.Maps;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

public class WeChatUtils {

    /**
     * 根据code获取微信授权access_token
     * 
     * @param code
     */
    public static AuthToken getTokenByAuthCode(String code) {
        AuthToken authToken;
        StringBuilder json = new StringBuilder();
        try {
            URL url = new URL(WeChatConstant.GET_AUTHTOKEN_URL + "appid=" + WeChatConstant.APP_ID + "&secret="
                    + WeChatConstant.APP_SECRET + "&code=" + code + "&grant_type=authorization_code");
            URLConnection uc = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
            // 将json字符串转成javaBean
            // ObjectMapper om = new ObjectMapper();
            authToken = JSONObject.parseObject(json.toString(), AuthToken.class);
        } catch (Exception e) {
            throw new ServiceException("微信工具类:根据授权code获取access_token异常", e);
        }
        return authToken;
    }

    /**
     * 获取微信签名
     * 
     * @param map
     *            请求参数集合
     * @return 微信请求签名串
     */
    public static String getSign(SortedMap<String, Object> map) {
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<String, Object>> set = map.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            // 参数中sign、key不参与签名加密
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + WeChatConstant.KEY);
        String sign = MD5.MD5Encode(sb.toString()).toUpperCase();
        return sign;
    }

    /**
     * 获取微信配置签名 SHA1
     * 
     * @param map
     * @return
     */
    public static String getSignBySHA1(SortedMap<String, Object> map) throws Exception {
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<String, Object>> set = map.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            // 参数中sign、key不参与签名加密
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        // sb.append("key=" + WeChatConstant.KEY);
        String fianlStr = sb.toString().substring(0, sb.toString().length() - 1);
        System.out.println("-----------------" + fianlStr);
        String sign = MessageDigestUtils.encrypt(fianlStr, Algorithm.SHA1);
        return sign;
    }

    /**
     * 解析微信服务器发来的请求
     * 
     * @param inputStream
     * @return 微信返回的参数集合
     */
    public static SortedMap<String, Object> parseXml(InputStream inputStream) {
        SortedMap<String, Object> map = Maps.newTreeMap();
        try {
            // 获取request输入流
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
            // 得到xml根元素
            Element root = document.getRootElement();
            // 得到根元素所有节点
            @SuppressWarnings("unchecked")
            List<Element> elementList = root.elements();
            // 遍历所有子节点
            for (Element element : elementList) {
                map.put(element.getName(), element.getText());
            }
            // 释放资源
            inputStream.close();
        } catch (Exception e) {
            throw new ServiceException("微信工具类:解析xml异常", e);
        }
        return map;
    }

    /**
     * 扩展xstream,使其支持name带有"_"的节点
     */
    public static XStream xStream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));

    /**
     * 请求参数转换成xml
     * 
     * @param data
     * @return xml字符串
     */
    public static String sendDataToXml(WxPaySendData data) {
        xStream.autodetectAnnotations(true);
        xStream.alias("xml", WxPaySendData.class);
        return xStream.toXML(data);
    }

    /**
     * 获取当前时间戳
     * 
     * @return 当前时间戳字符串
     */
    public static long getTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取指定长度的随机字符串
     * 
     * @param length
     * @return 随机字符串
     */
    public static String getRandomStr(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}