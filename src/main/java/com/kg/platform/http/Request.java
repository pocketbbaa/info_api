package com.kg.platform.http;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.kg.platform.weixin.Algorithm;
import com.kg.platform.weixin.MessageDigestUtils;

public final class Request {

    // 专门用于存储请求参数
    private Map<String, Object> params = new HashMap<String, Object>();

    // 专门用于存储请求头
    private Map<String, Object> headers = new HashMap<String, Object>();

    private int retry = 3;// 重试次数（默认为3次）

    private String url;// 请求的url地址

    private int timeout = 30 * 1000;// 连接有效时间，默认为10s

    private Method method = Method.GET;// 请求方法，默认为get

    private File file;// 要上传的文件

    private InputStream inputStream;

    private Cookie cookie;

    private String appkey;

    private String secret;

    private String json;

    public static class Builder {

        // 专门用于存储请求参数
        private Map<String, Object> params = new HashMap<String, Object>();

        // 专门用于存储请求头
        private Map<String, Object> headers = new HashMap<String, Object>();

        private int retry = 3;// 重试次数（默认为3次）

        private String url;// 请求的url地址

        private int timeout = 30 * 1000;// 连接有效时间，默认为30s

        private Method method = Method.GET;// 请求方法，默认为get

        private File file;// 要上传的文件

        private InputStream inputStream;

        private Cookie cookie;

        private String appkey;

        private String secret;

        private String json;

        public Builder setAppkey(String appkey) {
            this.appkey = appkey;
            return this;
        }

        public Builder setSecret(String secret) {
            this.secret = secret;
            return this;
        }

        public Builder addParameter(String key, Object value) {
            params.put(key, value);
            return this;
        }

        public Builder addParameter(Map<String, Object> params) {
            this.params.putAll(params);
            return this;
        }

        public Builder addHeader(String key, Object value) {
            headers.put(key, value);
            return this;
        }

        public Builder setRetry(int retry) {
            this.retry = retry;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setTimeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder setJson(String json) {
            this.json = json;
            return this;
        }

        public Builder setMethod(Method method) {
            this.method = method;
            return this;
        }

        public Builder setFile(File file) {
            this.file = file;
            return this;
        }

        public Builder setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
            return this;
        }

        public Builder setCookie(String name, String value) {
            this.cookie = new Cookie(name, value);
            return this;
        }

        public Request build() {
            return new Request(this);
        }
    }

    public static Builder options() {
        return new Builder();
    }

    private Request(Builder builder) {
        this.params = builder.params;
        this.headers = builder.headers;
        this.retry = builder.retry;
        this.timeout = builder.timeout;
        this.url = builder.url;
        this.method = builder.method;
        this.file = builder.file;
        this.inputStream = builder.inputStream;
        this.cookie = builder.cookie;
        this.appkey = builder.appkey;
        this.secret = builder.secret;
        this.json = builder.json;
    }

    public String getAppkey() {
        return appkey;
    }

    public String getSecret() {
        return secret;
    }

    public String getJson() {
        return json;
    }

    public Map<String, Object> getParameters() {
        return this.params;
    }

    public Map<String, Object> getHeaders() {
        try {
            long timestamp = System.currentTimeMillis();
            String signatrue = MessageDigestUtils.encrypt(appkey + "_" + secret + "_" + timestamp, Algorithm.SHA1);
            headers.put("appkey", appkey);
            headers.put("timestamp", timestamp);
            headers.put("signature", signatrue);
        } catch (Exception e) {

        }
        return this.headers;
    }

    public int getRetry() {
        return this.retry;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public String getUrl() {
        return this.url;
    }

    public Method getMethod() {
        return this.method;
    }

    public File getFile() {
        return file;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public Cookie getCookie() {
        return cookie;
    }
}
