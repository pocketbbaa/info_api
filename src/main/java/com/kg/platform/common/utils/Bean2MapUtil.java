package com.kg.platform.common.utils;

import net.sf.cglib.beans.BeanMap;

import java.util.Map;

/**
 * Created by Administrator on 2018/3/26.
 */

/**
 * 将BEAN转为MAP
 */
public class Bean2MapUtil {
    public static Map<String,Object> bean2map(Object o){
        if(o!=null){
            return BeanMap.create(o);
        } else {
            return null;
        }
    }

}
