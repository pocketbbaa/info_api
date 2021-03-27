package com.kg.platform.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class XssFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse responses = (HttpServletResponse) response;
        responses.setHeader("Access-Control-Allow-Origin", "*");
        responses.setHeader("Access-Control-Allow-Methods", "GET, POST, HEAD, PUT, DELETE, OPTIONS");
        responses.setHeader("Access-Control-Max-Age", "3600");
        responses.setHeader("Access-Control-Allow-Headers",
                "Accept, Origin, X-Requested-With, Content-Type, Last-Modified, token, rid, os_version");
        chain.doFilter(new XSSRequestWrapper((HttpServletRequest) request), response);
    }
}