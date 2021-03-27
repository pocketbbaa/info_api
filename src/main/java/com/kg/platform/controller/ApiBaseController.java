package com.kg.platform.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kg.platform.common.utils.PropertyLoader;

/**
 * api基础类
 * 
 */

public abstract class ApiBaseController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public final static String REDIS_PREFIV_KEY = "api_redis_";

    @Resource(name = "propertyLoader")
    protected PropertyLoader propertyLoader;

}
