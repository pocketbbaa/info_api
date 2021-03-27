package com.kg.platform.common.mq;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class MQProduct {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MQConfig pushMQProductConfig;
    @Autowired
    private Producer producer;

    //发送APP推送消息
    public SendResult sendMessage(String tag, String key, String body){

        Message message = new Message(pushMQProductConfig.getTopic(),tag,body.getBytes());
        message.setKey(key);
        logger.info("topic:" + pushMQProductConfig.getTopic() + "---tag:" + pushMQProductConfig.getTags());
        SendResult sendResult = producer.send(message);
        if(sendResult!=null){
            logger.info("send success:"+"--------"+sendResult.getMessageId());
            return sendResult;
        }
        return null;

    }

    //发送其他topic的消息
    public SendResult sendMessage(String topic,String tag, String key, String body){

        Message message = new Message(topic,tag,body.getBytes());
        message.setKey(key);
        logger.info("topic:" + topic + "---tag:" + tag);
        SendResult sendResult = producer.send(message);
        if(sendResult!=null){
            logger.info("send success:"+"--------"+sendResult.getMessageId());
            return sendResult;
        }
        return null;

    }
}
