package com.kg.platform.common.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.kg.platform.common.mq.MQUtil;
import com.kg.platform.model.request.BatchAuditRitWithdrawConsumerMqRequest;
import com.kg.platform.service.admin.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URLDecoder;

/**
 * Created by Administrator on 2018/9/17.
 */
public class BatchAuditRitWithdrawConsumer implements MessageListener {
    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private MQUtil mQUtil;

    @Autowired
    private AccountService accountService;

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {

        log.info("kg-batchAuditRitWithdrawConsumer 启动 ......");
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
			body = URLDecoder.decode(body, "UTF-8");
            log.info("message body : " + body);
            BatchAuditRitWithdrawConsumerMqRequest mqRequest = JSON.parseObject(body,BatchAuditRitWithdrawConsumerMqRequest.class);
            accountService.doBatchAuditRitWithdraw(mqRequest.getWithdrawFlowIdList(),mqRequest.getRequest(),mqRequest.getSysUser(),mqRequest.getNow());
            return Action.CommitMessage;
        } catch (Exception e) {
            log.error(e.getMessage());
            //消费失败
            return Action.ReconsumeLater;
        }
    }
}
