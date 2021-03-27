package com.kg.platform.common.aop;

import com.kg.platform.enumeration.DateEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {

    String category() default "";

    String key();

	/**
	 * 是否执行AOP注解缓存 0：关闭 1：开启
	 * @return
	 */
	String ifExecute() default "1";

    /**
     * 过期时间数值，默认-1为永久
     *
     * @return
     */
    int expire() default -1;

    /**
     * 时间单位，默认为秒
     *
     * @return
     */
    DateEnum dateType() default DateEnum.SECONDS;
}
