package com.kg.platform.common.support;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Base64;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.BusinessException;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.exception.ParamException;
import com.kg.platform.common.utils.CheckUtils;
import com.kg.platform.common.utils.MD5Util;

/**
 * @author Mark
 * @version $Id: ApiRequestSupport.java, Exp $
 */
public class ApiRequestSupport {

    private static final Logger logger = LoggerFactory.getLogger(ApiRequestSupport.class);

    private final static Gson gson = new Gson();

    /**
     * 请求参数权限校验
     *
     * @param customer
     * @param data
     * @param sign
     * @param token
     * @throws UnsupportedEncodingException
     */
    public static final void checkData(String data) throws UnsupportedEncodingException {
        if (CheckUtils.isNull(data) || data.length() == 0) {
            throw new ParamException(ExceptionEnum.PARAMEMPTYERROR);
        }
    }

    /**
     * 请求参数权限校验
     *
     * @param customer
     * @param data
     * @param sign
     * @param token
     * @throws UnsupportedEncodingException
     */
    public static final void checkLogin(String data, String sign, String token) throws UnsupportedEncodingException {
        String sign1 = MD5Util.md5Hex(data + token);
        // logger.info("data==={},token==={},sign1=={}", data, token, sign1);

        if (CheckUtils.isNull(sign) || !sign.equals(sign1)) {
            throw new BusinessException(ExceptionEnum.SIGNERROR);
        }
    }

    /**
     * 获取请求参数实体
     *
     * @param data 请求参数字符串
     * @return
     * @throws UnsupportedEncodingException
     */
    public static final JSONObject getRequestData(String data) throws UnsupportedEncodingException {

        logger.info("input data:{}", data);
        if (CheckUtils.isNull(data)) {
            throw new ParamException(ExceptionEnum.PARAMEMPTYERROR);
        }
        String value;
        try {
            value = new String(Base64.getDecoder().decode(data), "UTF-8");
            // logger.info("value=={}", value);
        } catch (Exception e) {
            logger.info("解析出错=={}", e);
            value = new String(com.alibaba.fastjson.util.Base64.decodeFast(URLDecoder.decode(data, "UTF-8")));
            logger.info("errorvalue=={}", value);
        }
        return JSON.parseObject(value);
    }

    public static void invokeExceptionWrapper(HttpServletResponse response, String code, String message)
            throws IOException {
        JsonEntity json = new JsonEntity();
        json.setCode(code);
        json.setMessage(message);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(gson.toJson(json));
    }
}
