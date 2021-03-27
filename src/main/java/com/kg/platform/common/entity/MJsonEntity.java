package com.kg.platform.common.entity;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.kg.platform.common.exception.ExceptionEnum;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MJsonEntity implements Serializable {

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

    public MJsonEntity() {
    }

    public MJsonEntity(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public MJsonEntity(ExceptionEnum exceptionEnum) {
        this.message = exceptionEnum.getMessage();
        this.code = exceptionEnum.getCode();
    }

    public MJsonEntity(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return this.code.equals(ExceptionEnum.SUCCESS.getCode());
    }

    public static MJsonEntity makeSuccessJsonEntity(Object data) {
        MJsonEntity json = new MJsonEntity();
        json.setCode(ExceptionEnum.SUCCESS.getCode());
        json.setMessage(ExceptionEnum.SUCCESS.getMessage());
        json.setData(data);
        json.setResponseTime(String.valueOf(System.currentTimeMillis()));
        return json;
    }

    public static MJsonEntity makeSuccessJsonEntity(String key, Object data) {
        MJsonEntity json = new MJsonEntity();
        json.setCode(ExceptionEnum.SUCCESS.getCode());
        json.setMessage(ExceptionEnum.SUCCESS.getMessage());
        json.setData(data);
        Map<String, Object> map = new HashMap<>();
        map.put(key, data);
        json.setResponseTime(String.valueOf(System.currentTimeMillis()));
        return json;
    }

    public static MJsonEntity makeExceptionJsonEntity(String code, String message) {
        MJsonEntity json = new MJsonEntity();
        json.setCode(code);
        json.setMessage(message);
        json.setResponseTime(String.valueOf(System.currentTimeMillis()));
        return json;
    }

    public static MJsonEntity makeExceptionJsonEntity(int code, String message) {
        MJsonEntity json = new MJsonEntity();
        json.setCode(code + "");
        json.setMessage(message);
        json.setResponseTime(String.valueOf(System.currentTimeMillis()));
        return json;
    }

    public static MJsonEntity makeExceptionJsonEntity(ExceptionEnum codeHandle) {
        MJsonEntity json = new MJsonEntity();
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
    public static JSONObject jsonFromObject(MJsonEntity result) {
        JSONObject jsons = JSONObject.parseObject(new Gson().toJson(result));
        return jsons;
    }

}
