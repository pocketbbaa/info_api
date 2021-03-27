package com.kg.platform.common.support;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.utils.Validator;

/**
 * Json转 成对象 并且 检查对象属性
 * 
 * @author MF-CX-2
 * @version $Id: ControllerToModeConstant.java, v 0.1 2016年12月30日 上午10:29:04
 *          MF-CX-2 Exp $
 */
public class ControllerToModeConstant {

    public static <T> T getModel(String json, Class<T> clazz) {

        Validator.validate(JSON.parseObject(json, clazz));
        return JSON.parseObject(json, clazz);
    }

    public static <T> T getModel(String json, Class<T> clazz, Class<?>... groups) {

        Validator.validate(JSON.parseObject(json, clazz), groups);
        return JSON.parseObject(json, clazz);
    }

}
