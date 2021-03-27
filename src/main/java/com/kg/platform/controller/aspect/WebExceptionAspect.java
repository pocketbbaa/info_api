package com.kg.platform.controller.aspect;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ApiMessageException;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.exception.TokenException;
import com.kg.platform.common.utils.StringUtils;

@Aspect
public class WebExceptionAspect {

    private static final Logger logger = LoggerFactory.getLogger(WebExceptionAspect.class);

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    private void webPointcut() {
    }

    /**
     * 拦截web层异常，记录异常日志，并返回友好信息到前端 目前只拦截Exception，是否要拦截Error需再做考虑
     *
     * @param e
     *            异常对象
     */
    @AfterThrowing(pointcut = "webPointcut()", throwing = "e")
    public void handleThrowing(Exception e) {
        //e.printStackTrace();
        logger.error("发现异常！" + e.getMessage());
        logger.error(JSON.toJSONString(e.getStackTrace()));
        if (StringUtils.isBlank(e.getMessage())) {
            writeContent(JSON.toJSONString(JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SYS_EXCEPTION.getCode(),
                    ExceptionEnum.SYS_EXCEPTION.getMessage())));
        } else {
            // 如果需要给前端返回具体报错信息，抛出 ApiMessageException
            if (ApiMessageException.class.isAssignableFrom(e.getClass())) {
                writeContent(JSON.toJSONString(
                        JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SYS_EXCEPTION.getCode(), e.getMessage())));
            }
            if (TokenException.class.isAssignableFrom(e.getClass())) {
                writeContent(JSON.toJSONString(
                        JsonEntity.makeExceptionJsonEntity(ExceptionEnum.TOKENRERROR.getCode(), e.getMessage())));
            } else {
                writeContent(JSON.toJSONString(JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SYS_EXCEPTION.getCode(),
                        ExceptionEnum.SYS_EXCEPTION.getMessage())));
            }
        }
    }

    /**
     * 将内容输出到浏览器
     *
     * @param content
     *            输出内容
     */
    private void writeContent(String content) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getResponse();
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "text/plain;charset=UTF-8");
        response.setHeader("icop-content-type", "exception");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();

            writer.print((content == null) ? "" : content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
