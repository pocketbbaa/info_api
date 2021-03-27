package com.kg.platform.weixin;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * 使用HTTPClient发送各种请求 get post
 * 
 * @author Administrator
 *
 */
public class HttpClientUtil {

    /**
     * 参数：url 返回值：json格式字符串
     * 
     * @return
     */
    public static String httpGet(String url) {
        try {
            // 创建执行对象
            HttpClient execution = HttpClientBuilder.create().build();
            // 创建请求对象
            HttpGet httpGet = new HttpGet(url);
            // httpGet.setHeader();
            // 通过执行对象传入请求对象执行请求，获取响应对象
            HttpResponse response = execution.execute(httpGet);
            System.out.println("statusCode:" + response.getStatusLine().getStatusCode());
            // 通过响应对象获取响应实体，并转化为json字符串
            HttpEntity responseEntity = response.getEntity();
            return EntityUtils.toString(responseEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 参数： url json数据 传递到服务器json数据 返回值：json字符串
     *
     * @param url
     * @return
     */
    public static String httpPost(String url, String paramJsonStr) {
        try {
            // 创建执行对象
            HttpClient execution = HttpClientBuilder.create().build();
            // 创建请求对象
            HttpPost httpPost = new HttpPost(url);
            // 设置参数给请求对象
            if (paramJsonStr != null)
                httpPost.setEntity(new StringEntity(paramJsonStr, "utf-8"));
            // 通过执行对象传入请求对象执行请求，获取响应对象
            HttpResponse response = execution.execute(httpPost);
            System.out.println("statusCode:" + response.getStatusLine().getStatusCode());
            // 通过响应对象获取响应实体，并转化为json字符串
            HttpEntity responseEntity = response.getEntity();
            return EntityUtils.toString(responseEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
