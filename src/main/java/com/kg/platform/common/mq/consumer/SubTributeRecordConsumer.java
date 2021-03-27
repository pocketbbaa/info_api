package com.kg.platform.common.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.kg.platform.common.mongo.MongoTables;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.utils.Bean2MapUtil;
import com.kg.platform.common.utils.JsonUtil;
import com.kg.platform.model.mongoTable.AccountFlowKgMongo;
import com.kg.platform.model.mongoTable.SubTributeRecordMongo;
import com.kg.platform.service.RefreshColumnArticle;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class SubTributeRecordConsumer implements MessageListener {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        log.info("kg-subTributeRecordConsumer 启动 ......");
        log.info("mqUrl:" + message.getBornHost());
        log.info("topic:" + message.getTopic());
        log.info("tags:" + message.getTag());
        try {
            log.info("new message comming ....");
            log.info("msgId:" + message.getMsgID());
            String body = new String(message.getBody());
            log.info("插入monggo记录message body : " + body);
            SubTributeRecordMongo subTributeRecordMongo= JsonUtil.readJson(body, SubTributeRecordMongo.class);
            log.info("--------插入subTributeRecordMongo--------"+JsonUtil.writeValueAsString(subTributeRecordMongo));
            MongoUtils.insertOne(MongoTables.SUB_TRIBUTE_RECORD,new Document(Bean2MapUtil.bean2map(subTributeRecordMongo)));
            return Action.CommitMessage;
        } catch (Exception e) {
            log.error(e.getMessage());
            //消费失败
            return Action.ReconsumeLater;
        }
    }
}