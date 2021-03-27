package com.kg.platform.common.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.kg.platform.service.RefreshColumnArticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class RefreshColumnArticleConsumer implements MessageListener {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RefreshColumnArticle refreshColumnArticle;

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {

        log.info("kg-RefreshColumnArticleConsumer 启动 ......");
        log.info("mqUrl:" + message.getBornHost());
        log.info("topic:" + message.getTopic());
        log.info("tags:" + message.getTag());
        try {
            log.info("new message comming ....");
            log.info("msgId:" + message.getMsgID());
            String body = new String(message.getBody());
            log.info("message body : " + body);
            Map maps = (Map) JSONObject.parse(body);
            Integer types = Integer.parseInt(maps.get("types").toString());
            List<Integer> columnIds = (List<Integer>) maps.get("columnIds");
            log.info("types : " + types);
            if(types==1||types==2){
                refreshColumnArticle.refreshColumnArticle(types,columnIds);
            }

            return Action.CommitMessage;
        } catch (Exception e) {
            log.error(e.getMessage());
            //消费失败
            return Action.ReconsumeLater;
        }
    }
}