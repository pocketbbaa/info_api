package com.kg.platform.common.mq;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * MQ消息处理器标志
 * 
 * @author hesimin 16-12-29
 */
@Component
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MQEngine {
    String topic();

    String subExpression();
}
