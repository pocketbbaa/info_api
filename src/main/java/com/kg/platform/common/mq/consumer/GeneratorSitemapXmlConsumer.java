package com.kg.platform.common.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.kg.platform.service.SiteMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Map;

public class GeneratorSitemapXmlConsumer implements MessageListener {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private SiteMapService siteMapService;

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {

        log.info("kg-GeneratorSitemapXmlConsumer 启动 ......");
        log.info("mqUrl:" + message.getBornHost());
        log.info("topic:" + message.getTopic());
        log.info("tags:" + message.getTag());
        try {
            log.info("new message comming ....");
            log.info("msgId:" + message.getMsgID());
            String body = new String(message.getBody());
            log.info("message body : " + body);
            if (StringUtils.isEmpty(body)) {
                log.info("消息体为空 !!!");
                return Action.CommitMessage;
            }
            Map maps = (Map) JSONObject.parse(body);
            Integer types = Integer.parseInt(maps.get("types").toString());
            log.info("types : " + types);
            if(types==1){
                log.info("开始执行生成网站地图------");
                long now = System.currentTimeMillis();
                siteMapService.generatorSiteMapXmlFile();
                log.info("生成网站地图完毕，共耗时{}ms------",(System.currentTimeMillis()-now));
            }
            return Action.CommitMessage;
        } catch (Exception e) {
            log.error(e.getMessage());
            //消费失败
            return Action.ReconsumeLater;
        }
    }
}