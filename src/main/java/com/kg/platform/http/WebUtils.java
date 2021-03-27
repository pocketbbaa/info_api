package com.kg.platform.http;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kg.platform.common.utils.StringUtils;

public final class WebUtils {

    @Deprecated
    public static <T> Response<T> execute(Request request) {
        return execute(request, null, null);
    }

    @Deprecated
    public static <T> Response<T> execute(Request request, Class<T> clazz) {
        return execute(request, clazz, null);
    }

    @Deprecated
    public static <T> Response<T> execute(Request request, Type type) {
        return execute(request, null, type);
    }

    private static <T> Response<T> execute(Request request, Class<T> clazz, Type type) {
        int timeout = request.getTimeout();
        int retry = request.getRetry();
        CloseableHttpClient client = null;
        try {
            client = HttpClients.createDefault();
            for (int i = 1;; i++) {
                try {
                    RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(timeout)
                            .setConnectTimeout(timeout).setSocketTimeout(timeout).build();
                    Method method = request.getMethod();
                    String content = null;
                    switch (method) {
                    case GET: {
                        content = doGet(client, config, request);
                        break;
                    }
                    case POST: {
                        content = doPost(client, config, request);
                        break;
                    }
                    case UPLOAD: {

                    }
                    default: {
                        break;
                    }
                    }
                    Response<T> response = new Response<>();
                    // 如果转换失败，不影响数据的返回，只是data为null，客户端可以直接取得json字符串
                    try {
                        if (null != clazz) {
                            T data = JSON.parseObject(content, clazz);
                            response.setData(data);
                        } else if (null != type) {
                            T data = JSON.parseObject(content, type);
                            response.setData(data);
                        } else {
                            T data = JSON.parseObject(content, new TypeReference<T>() {
                            }.getType());
                            response.setData(data);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    response.setJsonString(content);
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    if (i >= retry) {
                        return null;
                    }
                }
            }
        } finally {
            if (null != client) {
                try {
                    client.close();
                    client = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("rawtypes")
    public static Response executeHttp(Request request) {
        int timeout = request.getTimeout();
        int retry = request.getRetry();
        CloseableHttpClient client = null;
        try {
            client = HttpClients.createDefault();
            for (int i = 1;; i++) {
                try {
                    RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(timeout)
                            .setConnectTimeout(timeout).setSocketTimeout(timeout).build();
                    Method method = request.getMethod();
                    String content = null;
                    switch (method) {
                    case GET: {
                        content = doGet(client, config, request);
                        break;
                    }
                    case POST: {
                        content = doPost(client, config, request);
                        break;
                    }
                    case UPLOAD: {

                    }
                    default: {
                        break;
                    }
                    }
                    Response response = new Response();
                    response.setJsonString(content);
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    if (i >= retry) {
                        return null;
                    }
                }
            }
        } finally {
            if (null != client) {
                try {
                    client.close();
                    client = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String doGet(CloseableHttpClient client, RequestConfig config, Request request) throws Exception {
        CloseableHttpResponse response = null;
        HttpGet get = null;
        try {
            URIBuilder builder = new URIBuilder(request.getUrl());
            builder.setCharset(Charset.forName("UTF-8"));
            Map<String, Object> params = request.getParameters();
            if (null != params && params.size() > 0) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    builder.addParameter(entry.getKey(), entry.getValue() + "");
                }
            }
            get = new HttpGet(builder.build());
            if (null != request.getCookie()) {
                get.addHeader(new BasicHeader("cookie",
                        request.getCookie().getName() + '=' + request.getCookie().getValue()));
            }
            addHeaders(get, request.getHeaders());
            get.setConfig(config);
            response = client.execute(get);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, "UTF-8");
        } finally {
            if (null != get) {
                get.releaseConnection();
            }
            if (null != response) {
                response.close();
            }
        }
    }

    private static String doPost(CloseableHttpClient client, RequestConfig config, Request request) throws Exception {
        CloseableHttpResponse response = null;
        HttpPost http = null;
        try {
            http = new HttpPost(request.getUrl());
            http.setConfig(config);
            if (null != request.getCookie()) {
                http.addHeader(new BasicHeader("cookie",
                        request.getCookie().getName() + '=' + request.getCookie().getValue()));
            }
            addHeaders(http, request.getHeaders());
            // Map<String,Object> params = request.getParameters();
            String json = request.getJson();
            http.setHeader("Content-Type", "application/json;charset=UTF-8");
            http.setHeader("Accept", "application/json");
            if (!StringUtils.isBlank(json)) {
                http.setEntity(new StringEntity(json, Charset.forName("UTF-8")));
                // List<BasicNameValuePair> pairList = new
                // ArrayList<BasicNameValuePair>();
                // for(Map.Entry<String, Object> entry : params.entrySet()){
                // pairList.add(new BasicNameValuePair(entry.getKey(),
                // entry.getValue()+""));
                // }
                // http.setEntity(new UrlEncodedFormEntity(pairList, "UTF-8"));
            }
            response = client.execute(http);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, "UTF-8");
        } finally {
            if (null != http) {
                http.releaseConnection();
            }
            if (null != response) {
                response.close();
            }
        }
    }

    /**
     * 添加请求头
     * 
     * @param request
     * @param headers
     */
    private static void addHeaders(HttpRequest request, Map<String, Object> headers) {
        if (null != headers && headers.size() > 0) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue() + "");
            }
        }
    }
}
