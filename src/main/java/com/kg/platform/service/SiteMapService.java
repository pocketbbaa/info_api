package com.kg.platform.service;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2018/8/21.
 */
public interface SiteMapService {
    /**
     * 生成网站地图XML
     */
    void generatorSiteMapXmlFile() throws UnsupportedEncodingException;

    /**
     * 提供获取网站地图XML
     * @param response
     * @param number
     */
    void getSitemapXml(HttpServletResponse response, Integer number);
}
