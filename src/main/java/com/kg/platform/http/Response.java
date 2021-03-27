package com.kg.platform.http;

/**
 * Http响应
 * 
 * @author Lynn
 *
 */
public final class Response<T> {

    private T data;

    private String json;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setJsonString(String json) {
        this.json = json;
    }

    /**
     * 获得json字符串
     * 
     * @return
     */
    public String getJsonString() {
        return json;
    }

    @Override
    public String toString() {
        return this.getJsonString();
    }
}
