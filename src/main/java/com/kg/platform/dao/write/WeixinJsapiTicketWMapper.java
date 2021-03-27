package com.kg.platform.dao.write;

import java.util.List;

import com.kg.platform.dao.entity.WeixinJsapiTicket;

public interface WeixinJsapiTicketWMapper {

    List<WeixinJsapiTicket> selectAll();

    void insert(WeixinJsapiTicket ticket);

    void update(WeixinJsapiTicket ticket);

}