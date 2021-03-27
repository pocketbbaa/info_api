package com.kg.platform.common.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.kg.platform.model.ApiBaseModel;
import com.kg.platform.model.GroupDefault;

/**
 * /**
 *        @Target:注解的作用目标
　　　　　　　　@Target(ElementType.TYPE)   //接口、类、枚举、注解
　　　　　　　　@Target(ElementType.FIELD) //字段、枚举的常量
　　　　　　　　@Target(ElementType.METHOD) //方法
　　　　　　　　@Target(ElementType.PARAMETER) //方法参数
　　　　　　　　@Target(ElementType.CONSTRUCTOR)  //构造函数
　　　　　　　　@Target(ElementType.LOCAL_VARIABLE)//局部变量
　　　　　　　　@Target(ElementType.ANNOTATION_TYPE)//注解
　　　　　　　　@Target(ElementType.PACKAGE) ///包

 *  @Retention：注解的保留位置　RetentionPolicy.RUNTIME　 注解会在class字节码文件中存在，在运行时可以通过反射获取到
 *  @Retention(RetentionPolicy.SOURCE)   //注解仅存在于源码中，在class字节码文件中不包含
　　　@Retention(RetentionPolicy.CLASS)     // 默认的保留策略，注解会在class字节码文件中存在，但运行时无法获得，
 @Documented 说明该注解将被包含在javadoc中

 *
 * @author MF-CX-2
 * @version $Id: ActionControllerLog.java, v 0.1 2016年12月28日 下午5:06:57 MF-CX-2 Exp $
 */
/**
 * 基础controller 注解 （ needCheckToken = true 检查用户是否登录 needCheckParameter = true
 * 检查参数 beanClazz = 对应入参实体 ）
 *
 * @author MF-CX-2
 * @version $Id: baseControllerNote.java, v 0.1 2017年1月3日 下午3:09:39 MF-CX-2 Exp
 *          $
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BaseControllerNote {
    /** 操作类似 */
    // String operation() default "";
    //
    // /** 动作的名称 */
    // String action() default "";
    //
    // /** 是否保存请求的参数 */
    // boolean isSaveRequestData() default false;
    //
    /**
     * 是否检查有无签名 （当为false 表示过滤器生效）
     *
     * @return
     */
    boolean needCheckToken()

    default true;

    /**
     * 是否登陆验证 （当为false 表示过滤器生效）
     *
     * @return
     */
    boolean isLogin()

    default false;

    /**
     * 是否需要检查入参 默认需要
     *
     * @return
     */
    boolean needCheckParameter()

    default true;

    /**
     * 入参检查bean class
     *
     * @return
     */
    Class<?> beanClazz() default ApiBaseModel.class;

    Class<?> groups() default GroupDefault.class;
    /**
     * 参数名称
     * 
     * @return
     */
    // String content() default "";
    /**
     * 参数名称
     * 
     * @return
     */
    // String parameter() default "" ;
}
