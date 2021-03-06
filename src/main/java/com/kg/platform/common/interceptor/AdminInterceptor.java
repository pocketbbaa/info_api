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

                    logger.info("===??????????????????IP:{}", userIp);
                    logger.info("===???????????????IP:{}", siteInfo.getLimitIp());

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
                        throw new ApiMessageException(ExceptionEnum.LOCKERROR.getCode(), "??????????????????");
                    }

                    request.setAttribute("sysUser", sysUser);
                }

            } catch (ApiMessageException e) {
                logger.error(e.getMessage(), e);
                ApiRequestSupport.invokeExceptionWrapper(response, e.getCode(), e.getMessage());
                return false;
            } catch (Exception e) {
                logger.error("???????????????????????????", e);
                ApiRequestSupport.invokeExceptionWrapper(response, ExceptionEnum.SERVERERROR.getCode(),
                        ExceptionEnum.SERVERERROR.getMessage() + "[" + e.getMessage() + "]");
                return false;
            }
        }
        return true;
    }

    /**
     * ?????????????????????????????????Interceptor???preHandle??????????????????true????????????????????????postHandle?????????????????????????????????
     * ????????????????????????????????????????????????
     * ??????????????????Controller?????????????????????????????????????????????DispatcherServlet????????????????????????????????????
     * ??????????????????????????????????????????ModelAndView?????????
     * ????????????????????????????????????????????????????????????????????????????????????????????????Interceptor???????????????????????????????????????
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

        ContextEntry.removeMonitor();// ??????????????????
    }

    /**
     * ????????????????????????????????????Interceptor???preHandle?????????????????????true????????????????????????????????????????????????????????????
     * ?????????DispatcherServlet????????????????????????
     * ????????????????????????????????????????????????????????????????????????????????????????????????Interceptor???preHandle?????????????????????true??????????????????
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }

}
