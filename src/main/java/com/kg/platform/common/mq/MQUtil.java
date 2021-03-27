package com.kg.platform.common.mq;

import com.kg.platform.common.lock.Lock;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Y50
 * @version $Id: MQUtil.java, v 0.1 2016年5月18日 下午9:06:45 Y50 Exp $
 */
@Component("mqUtil")
public class MQUtil {


    private final static Logger logger = LoggerFactory.getLogger(MQUtil.class);

    @Autowired
    protected JedisUtils jedisUtils;


    /**
     * 校验消息是否重复
     *
     * @param key
     * @return
     */
    public boolean isRepeat(String key) {
        Lock lock = new Lock();
        try {
            lock.lock(key);
            String value = jedisUtils.get(key);
            logger.info("【校验消息是否重复】value:{},key:{}", value, key);
            if (StringUtils.isEmpty(value)) {
                jedisUtils.set(key, "isRepeat", 10);
                return false;
            }
            if ("isRepeat".equals(value)) {
                logger.info("消息重复 - > key:" + key);
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        } finally {
            lock.unLock();
        }
    }
}
