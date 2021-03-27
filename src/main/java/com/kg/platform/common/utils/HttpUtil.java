package com.kg.platform.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * httputil
 *
 * @author lvchaohua
 */
public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static final String CHARSET = "UTF-8";

    /**
     * http get请求
     *
     * @param url 请求地址
     * @param
     * @return
     */
    public static String get(String url) {
        HttpGet httpGet = new HttpGet(url);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpGet)) {

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String str = EntityUtils.toString(entity, CHARSET);
                return str;
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    /**
     * get请求，使用代理
     *
     * @param url
     * @param userAgent
     * @return
     */
    public static String get(String url, String userAgent) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", userAgent);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String str = EntityUtils.toString(entity, CHARSET);
                return str;
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    public static HttpResponse getResponse(String url) {
        HttpGet httpGet = new HttpGet(url);
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            return httpClient.execute(httpGet);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    // 从服务器获得一个输入流(本例是指从服务器获得一个image输入流)
    public static InputStream getInputStream(String imgurl) {
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try {
            URL url = new URL(imgurl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            // 设置网络连接超时时间
            httpURLConnection.setConnectTimeout(3000);
            // 设置应用程序要从网络连接读取数据
            httpURLConnection.setDoInput(true);

            httpURLConnection.setRequestMethod("GET");
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                // 从服务器返回一个输入流
                inputStream = httpURLConnection.getInputStream();

            }

        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return inputStream;

    }

    /**
     * http get请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return
     */
    public static String get(String url, Map<String, Object> params) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            url = url + "?";
            for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext(); ) {
                String key = iterator.next();
                String temp = key + "=" + params.get(key) + "&";
                url = url + temp;
            }
            url = url.substring(0, url.length() - 1);
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String str = EntityUtils.toString(entity, CHARSET);
                    return str;
                }
            } finally {
                response.close();
                httpClient.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * http post请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return
     */
    public static String post(String url, Map<String, Object> params) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext(); ) {
                String key = iterator.next();
                parameters.add(new BasicNameValuePair(key, params.get(key).toString()));
            }
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(parameters, CHARSET);
            httpPost.setEntity(uefEntity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String str = EntityUtils.toString(entity, CHARSET);
                    return str;
                }
            } finally {
                response.close();
                httpClient.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * http post请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return
     */
    public static String post(String url, String params) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            StringEntity sEntity = new StringEntity(params, CHARSET);
            httpPost.setEntity(sEntity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity, CHARSET);
                }
            } finally {
                response.close();
                httpClient.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 获取用户真实ip
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return "";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            logger.info("Proxy-Client-IP:" + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            logger.info("WL-Proxy-Client-IP:" + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            logger.info("RemoteAddr:" + ip);
        }
        logger.info("获取到的用户ip：{}", ip);
        return ip;
    }

}
