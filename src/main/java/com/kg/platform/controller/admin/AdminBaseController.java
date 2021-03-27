package com.kg.platform.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 后台管理基础Controller
 */
@RequestMapping("admin")
public abstract class AdminBaseController {

    protected Logger logger = LoggerFactory.getLogger(getClass());
}
