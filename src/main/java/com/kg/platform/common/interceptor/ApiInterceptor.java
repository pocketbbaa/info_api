package com.kg.platform.common.interceptor;

import java.lang.reflect.Method;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.aop.ContextEntry;
import com.kg.platform.common.exception.ApiException;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.exception.JsonBindException;
import com.kg.platform.common.support.ApiRequestSupport;
import com.kg.platform.common.support.ControllerToModeConstant;
import com.kg.platform.common.utils.LogUtil;
import com.kg.platform.model.GroupDefault;

/**
 *
 * @author Mark
 * @version $Id: AppApiInterceptor.java, Exp $
 */
public class ApiInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ApiInterceptor.class);

    /**
     * preHandle方法是进行处理器拦截用的，该方法将在Controller处理之前进行调用，
     * SpringMVC中的Interceptor拦截器是链式的，可以同时存在
     * 多个Interceptor，然后SpringMVC会根据声明的前后顺序一个接一个的执行，
     * 而且所有的Interceptor中的preHandle方法都会在
     * Controller方法调用之前调用。SpringMVC的这种Interceptor链式结构也是可以进行中断的，
     * 这种中断方式是令preHandle的返 回值为false，当preHandle的返回值为false的时候整个请求就结束了。
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");

        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;

            Method method = hm.getMethod();
            BaseControllerNote annotation = method.getAnnotation(BaseControllerNote.class);
            try {
                String data = request.getParameter("data");
                if (StringUtils.isEmpty(data)) {
                    return true;
                }

                if (annotation != null && !annotation.isLogin()) {
                    logger.info("url = " + request.getRequestURI() + " 入参是：" + data + " ");
                }

                ApiRequestSupport.checkData(data);
                JSONObject requestObject = ApiRequestSupport.getRequestData(data);

                if (annotation != null && !annotation.isLogin()) {
                    logger.info("url = " + request.getRequestURI() + " 执行后是：" + data + " ");
                    logger.info(LogUtil.logFormat(ContextEntry.getMonitor().getTraceId(),
                            "http request by rest param: " + requestObject.toJSONString()));
                }

                // 如果参数中带了rid 表示来微店请求当前用户信息 （）
                // Long rid = requestObject.getLong("rid");
                // if (rid != null) {
                // Customer customer = customerService.getCustomerById(rid);
                // request.setAttribute("customer", customer);
                // }

                request.setAttribute("requestObject", requestObject);
                // json串
                String jsonString = new String(Base64.getDecoder().decode(data), "UTF-8");
                // request.setAttribute("json", requestObject.toJSONString());

                if (annotation != null && !annotation.isLogin()) {
                    logger.info("url = " + request.getRequestURI() + " 入参是：" + jsonString + " ");
                }

                if (annotation != null && annotation.needCheckParameter()) { // 注解不为空
                                                                             // (并且需要检查)
                    if (!annotation.groups().equals(GroupDefault.class)) { // 是否使用分组

                        request.setAttribute("request", ControllerToModeConstant.getModel(jsonString,
                                annotation.beanClazz(), annotation.groups()));
                    } else {
                        request.setAttribute("request",
                                ControllerToModeConstant.getModel(jsonString, annotation.beanClazz()));
                    }

                }

            } catch (JsonBindException e) {
                logger.error("参数检查出错", e);
                throw new JsonBindException(e.getMessage());
            } catch (ApiException e) {
                logger.error("请求数据预处理出现Api异常", e);
                ApiRequestSupport.invokeExceptionWrapper(response, e.getCode(), e.getMessage());
                return false;
            } catch (Exception e) {
                logger.error("请求数据预处理错误", e);
                ApiRequestSupport.invokeExceptionWrapper(response, ExceptionEnum.SERVERERROR.getCode(),
                        ExceptionEnum.SERVERERROR.getMessage() + "[" + e.getMessage() + "]");
                return false;
            }
        }
        return true;
    }

    /**
     * 这个方法只会在当前这个Interceptor的preHandle方法返回值为true的时候才会执行。postHandle是进行处理器拦截用的，
     * 它的执行时间是在处理器进行处理之
     * 后，也就是在Controller的方法调用之后执行，但是它会在DispatcherServlet进行视图的渲染之前执行，
     * 也就是说在这个方法中你可以对ModelAndView进行操
     * 作。这个方法的链式结构跟正常访问的方向是相反的，也就是说先声明的Interceptor拦截器该方法反而会后调用。
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

        ContextEntry.removeMonitor();// 清除监控路由
    }

    /**
     * 该方法也是需要当前对应的Interceptor的preHandle方法的返回值为true时才会执行。该方法将在整个请求完成之后，
     * 也就是DispatcherServlet渲染了视图执行，
     * 这个方法的主要作用是用于清理资源的，当然这个方法也只能在当前这个Interceptor的preHandle方法的返回值为true时才会执行。
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }

}
