package com.kg.platform.model;

import java.io.Serializable;

/**
 * api基础实体对象
 * 
 * @author MF-CX-2
 * @version $Id: ApiBaseModel.java, v 0.1 2016年12月26日 上午11:47:04 MF-CX-2 Exp $
 */
public class ApiBaseModel implements Serializable {

    /**  */
    private static final long serialVersionUID = -4480741277257633290L;

    protected String version;

    /**
     * 微信openIdkey
     */
    protected String wxTempOpenIdkey;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getWxTempOpenIdkey() {
        return wxTempOpenIdkey;
    }

    public void setWxTempOpenIdkey(String wxTempOpenIdkey) {
        this.wxTempOpenIdkey = wxTempOpenIdkey;
    }

}
