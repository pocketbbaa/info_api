package com.kg.platform.common.mq.consumer;

import java.util.Map;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.mongodb.BasicDBObject;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.mq.ConsumeMessge;
import com.kg.platform.common.mq.MQUtil;
import com.kg.platform.common.utils.Bean2MapUtil;
import com.kg.platform.model.request.AccountRequest;
import com.kg.platform.service.admin.ArticleService;

/**
 * Created by Administrator on 2018/5/8.
 */
public class AwardMQConsumer implements MessageListener {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ArticleService articleService;

    @Autowired
    private IDGen idGenerater;

    @Autowired
    private MQUtil mQUtil;

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        log.info("kg-AwardMQConsumer 收到新消息 ......");
        log.info("mqUrl:" + message.getBornHost());
        log.info("topic:" + message.getTopic());
        log.info("tags:" + message.getTag());
        log.info("new message comming ....");

        try {
            if (mQUtil.isRepeat(message.getMsgID())) {
                return Action.CommitMessage;
            }
            String body = new String(message.getBody());
            log.info("message body : " + body);
            if (StringUtils.isEmpty(body)) {
                log.info("消息体为空 !!!");
                return Action.CommitMessage;
            }
            Map maps = (Map) JSONObject.parse(body);
            String articleId = String.valueOf(maps.get("articleId"));
            Integer types = Integer.parseInt(maps.get("types").toString());
            log.info("types : " + types);
            BasicDBObject basicBSONObject = new BasicDBObject("articleId", Long.valueOf(articleId)).append("serviceType",3);
            long count = MongoUtils.count(ConsumeMessge.mongoTable,basicBSONObject);
            if(count>0){
                log.info("业务重复....");
                return Action.CommitMessage;
            }
            if (types == 3) {
                AccountRequest accountRequest = new AccountRequest();
                accountRequest.setArticleId(Long.valueOf(articleId));
                //articleService.publishArticleBonus(accountRequest);
            }
            log.info("插入monggo消息 !!!");
            ConsumeMessge consumeMessge = new ConsumeMessge();
            consumeMessge.set_id(idGenerater.nextId());
            consumeMessge.setStatus(0);
            consumeMessge.setType(1);
            consumeMessge.setArticleId(Long.valueOf(articleId));
            consumeMessge.setServiceType(3);
            MongoUtils.insertOne(ConsumeMessge.mongoTable, new Document(Bean2MapUtil.bean2map(consumeMessge)));
            return Action.CommitMessage;
        }catch (Exception e){
            log.error(e.getMessage());
            //消费失败
            return Action.ReconsumeLater;
        }
    }
}
