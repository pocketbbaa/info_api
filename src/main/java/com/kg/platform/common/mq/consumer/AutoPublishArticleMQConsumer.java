package com.kg.platform.common.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.kg.platform.common.mq.MQUtil;
import com.kg.platform.service.QuartzService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/8.
 */
public class AutoPublishArticleMQConsumer implements MessageListener {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MQUtil mQUtil;

    @Autowired
    private QuartzService quartzService;


    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        log.info("kg-autopublisharticleMQConsumer 收到新消息 ......");
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
            Integer types = Integer.parseInt(maps.get("type").toString());
            log.info("types : " + types);
            if (types == 8) {
                // 查找所有定时发布的文章并且在草稿中的文章，并且发布时间和当前时间匹配，则将状态设置为已发布
                quartzService.autoPublishArticle();
            }
            return Action.CommitMessage;
        }catch (Exception e){
            log.error(e.getMessage());
            //消费失败
            return Action.ReconsumeLater;
        }
    }
}
