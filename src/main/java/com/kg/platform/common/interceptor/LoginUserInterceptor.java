package com.kg.platform.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.exception.ApiMessageException;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.exception.TokenException;
import com.kg.platform.common.support.ApiRequestSupport;
import com.kg.platform.common.support.HttpSupport;
import com.kg.platform.common.utils.CheckUtils;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.dao.entity.SysUser;
import com.kg.platform.model.TokenModel;
import com.kg.platform.model.request.UserkgRequest;
import com.kg.platform.model.request.admin.SysUserQueryRequest;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.TokenManager;
import com.kg.platform.service.UserkgService;
import com.kg.platform.service.admin.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author
 * @version $Id: LoginUserInterceptor.java
 */
public class LoginUserInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(LoginUserInterceptor.class);

    @Autowired
    private TokenManager manager;

    @Autowired
    private UserkgService userkgService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * preHandle方法是进行处理器拦截用的，顾名思义，该方法将在Controller处理之前进行调用，
     * SpringMVC中的Interceptor拦截器是链式的，可以同时存在
     * 多个Interceptor，然后SpringMVC会根据声明的前后顺序一个接一个的执行，
     * 而且所有的Interceptor中的preHandle方法都会在
     * Controller方法调用之前调用。SpringMVC的这种Interceptor链式结构也是可以进行中断的，
     * 这种中断方式是令preHandle的返 回值为false，当preHandle的返回值为false的时候整个请求就结束了。
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod) {
            try {
                HashMap<String, String> header = HttpSupport.getHeader(request);
                String data = request.getParameter("data");
                String token = header.get("token");
                int osVersion = request.getIntHeader("os_version"); //1 IOS 2 Android 3 h5 4 pc 5 APPH5 7 m
                // String resellerId = header.get("rid");
                String url = request.getRequestURI().toString();
                boolean needCheckToken = true; // 检查
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                Method method = handlerMethod.getMethod();
                BaseControllerNote annotation = method.getAnnotation(BaseControllerNote.class);

                if (annotation != null && !annotation.isLogin()) {
                    logger.info("=getMethod=" + request.getMethod() + "++getContentType++" + request.getContentType());
                    logger.info("url=={},data=={},token=={},sign=={}", url, data, token, request.getParameter("sign"));
                }
                if (annotation != null && needCheckToken) {
                    needCheckToken = annotation.needCheckToken();
                    // 检查如果需要 检查 但是token没传的情况
                    if (needCheckToken && StringUtils.isEmpty(token)) {
                        logger.info("防问： {} token为空:{}", url, data);
                        throw new TokenException(ExceptionEnum.TOKENRERROR);
                    }
                }

                // token 未传 并且为非必要检查的 跳过后续检查token及签名（sign）动作
                if (!needCheckToken) {
                    return true;
                }

                logger.info("【请求用户来源】 osVersion：{}", osVersion);

                TokenModel model = manager.getToken(token);
                Long userId = model.getUserId();
                if (osVersion == 1 || osVersion == 2 || osVersion == 5) {

                    if (!manager.checkAppToken(model)) {
                        logger.info("防问：{} token失效:{},data：{}", url, token, data);
                        throw new TokenException(ExceptionEnum.TOKENRERROR);
                    }
                } else {
                    if (!manager.checkToken(model)) {
                        logger.info("防问：{} token失效:{},data：{}", url, token, data);
                        throw new TokenException(ExceptionEnum.TOKENRERROR);
                    }
                }

                if (request.getParameter("sign") == null) {
                    throw new TokenException(ExceptionEnum.SIGNERROR);
                }
                // 检查签名
                ApiRequestSupport.checkLogin(data, String.valueOf(request.getParameter("sign")), token);
                // if (request.getAttribute("customer") == null) {
                // request.setAttribute("customer", customer);
                // }

                UserkgRequest ur = new UserkgRequest();
                ur.setUserId(model.getUserId());
                Result<UserkgResponse> userResult = userkgService.getUserDetails(ur);
                logger.info("==getUserDetails:{}" + JSONObject.toJSONString(userResult));
                CheckUtils.checkUserResult(userResult);
                if (annotation != null && !annotation.isLogin()) {
                    logger.info("==tokenModel{}", JSON.toJSONString(model));
                    logger.info("===kguser{}", JSON.toJSONString(userResult));
                }
                UserkgResponse ukr = userResult.getData();
                //如果在kg_user中没有找到用户在sysUser中寻找用户
                if (ukr == null) {
                    SysUserQueryRequest sysUserQueryRequest = new SysUserQueryRequest();
                    sysUserQueryRequest.setUserId(model.getUserId());
                    SysUser sysUser = sysUserService.getSysUserById(sysUserQueryRequest);
                    if (sysUser == null || sysUser.getSysUserId() == null) {
                        logger.error("没有获取到用户信息 -> userId：{}", model.getUserId());
                        throw new TokenException(ExceptionEnum.TOKENRERROR);
                    }
                }
                userkgService.checkLockStatus(ukr,userId);
                request.setAttribute("kguser", ukr);

            } catch (ApiMessageException e) {
                logger.error(e.getMessage(), e);
                ApiRequestSupport.invokeExceptionWrapper(response, e.getCode(), e.getMessage());
                return false;
            } catch (TokenException e) {
                logger.error(e.getMessage(), e);
                ApiRequestSupport.invokeExceptionWrapper(response, e.getCode(), e.getMessage());
                return false;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                ApiRequestSupport.invokeExceptionWrapper(response, ExceptionEnum.SERVERERROR.getCode(),
                        ExceptionEnum.SERVERERROR.getMessage());
                return false;
            }
        }
        return true;
    }

}
