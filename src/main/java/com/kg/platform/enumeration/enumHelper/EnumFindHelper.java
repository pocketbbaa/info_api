package com.kg.platform.enumeration.enumHelper;

import org.apache.log4j.Logger;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 逆向查找枚举的处理器
 * @param <T>
 * @param <K>
 */
public class EnumFindHelper<T extends Enum<T>,K> {
    private static Logger logger = Logger.getLogger(EnumFindHelper.class);

    protected Map<K, T> map = new HashMap<K, T>();

    public EnumFindHelper(Class<T> clazz, EnumKeyGetter<T, K> keyGetter) {
        try {
            for (T enumValue : EnumSet.allOf(clazz)) {
                map.put(keyGetter.getKey(enumValue), enumValue);
            }
        } catch (Exception e) {
            logger.error("init EnumFindHelper fail e:"+e.getMessage());
        }
    }

    public T find(K key, T defautValue) {
        T value = map.get(key);
        if (value == null) {
            value = defautValue;
        }
        return value;
    }
}
