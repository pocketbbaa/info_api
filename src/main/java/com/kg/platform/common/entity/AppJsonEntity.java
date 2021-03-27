package com.kg.platform.common.entity;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.kg.platform.common.exception.ExceptionEnum;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AppJsonEntity implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    // 响应流水号
    private String responseReq;

    // 响应时间
    private String responseTime;

    private String message;

    private String code;

    private String accountStr;

    private Object data = null;

    public AppJsonEntity() {
    }

    public AppJsonEntity(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public AppJsonEntity(ExceptionEnum exceptionEnum) {
        this.message = exceptionEnum.getMessage();
        this.code = exceptionEnum.getCode();
    }

    public AppJsonEntity(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return this.code.equals(ExceptionEnum.SUCCESS.getCode());
    }

    public static AppJsonEntity makeSuccessJsonEntity(Object data) {
        AppJsonEntity json = new AppJsonEntity();
        json.setCode(ExceptionEnum.SUCCESS.getCode());
        json.setMessage(ExceptionEnum.SUCCESS.getMessage());
        json.setData(data);
        json.setResponseTime(String.valueOf(System.currentTimeMillis()));
        return json;
    }

    public static AppJsonEntity makeSuccessJsonEntity(String key, Object data) {
        AppJsonEntity json = new AppJsonEntity();
        json.setCode(ExceptionEnum.SUCCESS.getCode());
        json.setMessage(ExceptionEnum.SUCCESS.getMessage());
        json.setData(data);
        Map<String, Object> map = new HashMap<>();
        map.put(key, data);
        json.setResponseTime(String.valueOf(System.currentTimeMillis()));
        return json;
    }

    public static AppJsonEntity makeExceptionJsonEntity(String code, String message,Object data) {
        AppJsonEntity json = new AppJsonEntity();
        json.setCode(code);
        json.setMessage(message);
        json.setData(data);
        json.setResponseTime(String.valueOf(System.currentTimeMillis()));
        return json;
    }

    public static AppJsonEntity makeExceptionJsonEntity(String code, String message) {
        AppJsonEntity json = new AppJsonEntity();
        json.setCode(code);
        json.setMessage(message);
        json.setResponseTime(String.valueOf(System.currentTimeMillis()));
        return json;
    }

    public static AppJsonEntity makeExceptionJsonEntity(int code, String message) {
        AppJsonEntity json = new AppJsonEntity();
        json.setCode(code + "");
        json.setMessage(message);
        json.setResponseTime(String.valueOf(System.currentTimeMillis()));
        return json;
    }

    public static AppJsonEntity makeExceptionJsonEntity(ExceptionEnum codeHandle) {
        AppJsonEntity json = new AppJsonEntity();
        json.setCode(codeHandle.getCode());
        json.setMessage(codeHandle.getMessage());
        json.setResponseTime(String.valueOf(System.currentTimeMillis()));
        return json;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAccountStr() {
        return accountStr;
    }

    public void setAccountStr(String accountStr) {
        this.accountStr = accountStr;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getResponseReq() {
        return responseReq;
    }

    public void setResponseReq(String responseReq) {
        this.responseReq = responseReq;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    @Override
    public String toString() {
        return "JsonEntity [responseReq=" + responseReq + ", responseTime=" + responseTime + ", message=" + message
                + ", code=" + code + ", accountStr=" + accountStr + ", data=" + data + "]";
    }

    /**
     * @param @param  result
     * @param @return 设定文件
     * @return JSONObject    返回类型
     * @throws
     * @Title: jsonFromObject
     * @Description:去掉为空或者Null的字段，不初始化
     */
    public static JSONObject jsonFromObject(AppJsonEntity result) {
        JSONObject jsons = JSONObject.parseObject(new Gson().toJson(result));
        return jsons;
    }

}
