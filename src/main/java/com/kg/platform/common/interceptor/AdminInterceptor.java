package com.kg.platform.common.interceptor;

import java.util.HashMap;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.kg.framework.toolkit.Check;
import com.kg.platform.common.aop.ContextEntry;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.exception.ApiMessageException;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.support.ApiRequestSupport;
import com.kg.platform.common.support.HttpSupport;
import com.kg.platform.common.utils.CheckUtils;
import com.kg.platform.common.utils.HttpUtil;
import com.kg.platform.dao.entity.SysUser;
import com.kg.platform.model.TokenModel;
import com.kg.platform.model.request.admin.SysUserQueryRequest;
import com.kg.platform.model.response.SiteinfoResponse;
import com.kg.platform.service.SiteinfoService;
import com.kg.platform.service.TokenManager;
import com.kg.platform.service.admin.SysUserService;

/**
 *
 * @author
 * @version $Id: AdminInterceptor.java, Exp $
 */
public class AdminInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AdminInterceptor.class);

    @Inject
    private TokenManager manager;

    @Inject
    SiteinfoService siteinfoService;

    @Inject
    private SysUserService sysUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");

        if (handler instanceof HandlerMethod) {

            try {
                HashMap<String, String> header = HttpSupport.getHeader(request);
                String token = header.get("token");

                Result<SiteinfoResponse> siteinfoResponse = siteinfoService.selectSiteInfo();
                CheckUtils.checkRetInfo(siteinfoResponse, true);

                SiteinfoResponse siteInfo = siteinfoResponse.getData();
                if (!Check.NuNString(siteInfo.getLimitIp())) {
                    String userIp = HttpUtil.getIpAddr(request);

                    logger.info("===获取到的远程IP:{}", userIp);
                    logger.info("===系统配置的IP:{}", siteInfo.getLimitIp());

                    // if (siteInfo.getLimitIp().indexOf(userIp) == -1) {
                    // throw new
                    // ApiMessageException(ExceptionEnum.ADMINIPERROR);
                    // }
                }

                TokenModel model = manager.getToken(token);
                SysUserQueryRequest req = new SysUserQueryRequest();
                if (model != null) {
                    req.setUserId(model.getUserId());
                    SysUser sysUser = sysUserService.getSysUserById(req);

                    if (sysUser == null || sysUser.getStatus() == false) {
                        throw new ApiMessageException(ExceptionEnum.LOCKERROR.getCode(), "账号已被禁用");
                    }

                    request.setAttribute("sysUser", sysUser);
                }

            } catch (ApiMessageException e) {
                logger.error(e.getMessage(), e);
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
