package com.kg.platform.common.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.kg.platform.common.mq.MQUtil;
import com.kg.platform.dao.write.KgArticleStatisticsWMapper;
import com.kg.platform.model.in.KgArticleStatisticsInModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Map;

public class MQConsumer implements MessageListener {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private KgArticleStatisticsWMapper kgArticleStatisticsWMapper;

    @Autowired
    private MQUtil mQUtil;

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {

        log.info("kg-MQConsumer 启动 ......");
        log.info("mqUrl:" + message.getBornHost());
        log.info("topic:" + message.getTopic());
        log.info("tags:" + message.getTag());
        try {
            log.info("new message comming ....");
            log.info("msgId:" + message.getMsgID());
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
            KgArticleStatisticsInModel statinModel = new KgArticleStatisticsInModel();
            statinModel.setArticleId(Long.valueOf(articleId));
            log.info("添加文章浏览数入参：{}", JSONObject.toJSONString(statinModel));
            int result = kgArticleStatisticsWMapper.addBrowseNum(statinModel);
            log.info("添加文章浏览数 - " + (result > 0 ? "成功" : "失败(文章不存在)") + " result:" + result);
            return Action.CommitMessage;
        } catch (Exception e) {
            log.error(e.getMessage());
            //消费失败
            return Action.ReconsumeLater;
        }
    }
}
