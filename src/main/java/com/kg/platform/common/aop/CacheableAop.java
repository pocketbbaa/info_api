package com.kg.platform.common.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.utils.AopUtils;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.common.utils.SpringExpressionUtils;
import com.kg.platform.enumeration.DateEnum;

@Aspect
@Component
public class CacheableAop {

    private static Logger log = LoggerFactory.getLogger(CacheableAop.class);

    @Autowired
    protected JedisUtils jedisUtils;

    @Around("@annotation(cache)")
    public Object cached(final ProceedingJoinPoint pjp, Cacheable cache) throws Throwable {
        log.info("in aop cache .......................................................");
        Object result = null;
        Method method = AopUtils.getMethod(pjp);

        String key = cache.key();
        String ifExecute = cache.ifExecute();
        String ifExe = SpringExpressionUtils.parseKey(ifExecute, method, pjp.getArgs());
		if("0".equals(ifExe)){
			log.info("out aop cache .......................................................");
			result = pjp.proceed();
			return result;

		}
		// 用SpEL解释key值
		String keyVal = SpringExpressionUtils.parseKey(key, method, pjp.getArgs());
		if (!StringUtils.isEmpty(cache.category())) {
            keyVal = cache.category() + ":" + keyVal;
        } else {
            // 如果cacheable的注解中category为空取 类名+方法名

            keyVal = pjp.getTarget().getClass().getSimpleName() + "/" + method.getName() + ":" + keyVal;
        }
        Class returnType = ((MethodSignature) pjp.getSignature()).getReturnType();

        // 从redis读取keyVal，并且转换成returnType的类型
        result = jedisUtils.get(keyVal, returnType);

        if (result == null) {
            try {
                // 如果redis没有数据则执行拦截的方法体
                result = pjp.proceed();
                int expireSeconds = 0;
                // 如果Cacheable注解中的expire为默认(默认值为-1)
                if (cache.expire() == -1) {
                    expireSeconds = 3600;
                } else {
                    expireSeconds = getExpireSeconds(cache);
                }
                // 把拦截的方法体得到的数据设置进redis，过期时间为计算出来的expireSeconds
                jedisUtils.set(keyVal, result, expireSeconds);
                log.info("已缓存缓存:key=" + keyVal);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return result;
        }
        log.info("========从缓存中读取");
        log.info("=======:key  = " + key);
        log.info("=======:keyVal= " + keyVal);

        String resultStr = JSONObject.toJSONString(result);
        if (StringUtils.isEmpty(resultStr)) {
            log.info("=======:val  = " + result);
            return result;
        }
        if (resultStr.length() > 200) {
            log.info("=======:val  = " + JSONObject.toJSONString(result).substring(0, 200) + " ......");
        }

        return result;
    }

    /**
     * 计算根据Cacheable注解的expire和DateUnit计算要缓存的秒数
     *
     * @param cacheable
     * @return
     */
    private int getExpireSeconds(Cacheable cacheable) {
        int expire = cacheable.expire();
        DateEnum unit = cacheable.dateType();
        if (expire <= 0) {
            return 0;
        }
        if (unit == DateEnum.MINUTES) {
            return expire * 60;
        } else if (unit == DateEnum.HOURS) {
            return expire * 60 * 60;
        } else if (unit == DateEnum.DAYS) {
            return expire * 60 * 60 * 24;
        } else if (unit == DateEnum.MONTHS) {
            return expire * 60 * 60 * 24 * 30;
        } else if (unit == DateEnum.YEARS) {
            return expire * 60 * 60 * 24 * 365;
        }
        return expire;
    }

}
