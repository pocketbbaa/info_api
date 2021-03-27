package com.kg.platform.common.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.kg.platform.common.utils.JsonUtil;
import com.kg.platform.dao.entity.AccountFlowTxb;
import com.kg.platform.dao.read.AccountFlowTxbRMapper;
import com.kg.platform.dao.write.AccountFlowTxbWMapper;
import com.kg.platform.model.in.AccountFlowInModel;
import com.kg.platform.model.mongoTable.AccountFlowKgMongo;
import com.kg.platform.service.RefreshColumnArticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class AccountFlowKgConsumer implements MessageListener {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AccountFlowTxbWMapper accountFlowTxbWMapper;

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {

        log.info("kg-accountFlowKgConsumer 启动 ......");
        log.info("mqUrl:" + message.getBornHost());
        log.info("topic:" + message.getTopic());
        log.info("tags:" + message.getTag());
        try {
            log.info("new message comming ....");
            log.info("msgId:" + message.getMsgID());
            String body = new String(message.getBody());
            log.info("message body : " + body);
            AccountFlowKgMongo accountFlowKgMongo =JsonUtil.readJson(body, AccountFlowKgMongo.class);
            log.info("插入氪金流水到数据库 : " + body);
            AccountFlowTxb accountFlowInModel=new AccountFlowTxb();
            accountFlowInModel.setAccountFlowId(accountFlowKgMongo.getAccount_flow_id());
            accountFlowInModel.setBusinessTypeId(accountFlowKgMongo.getBusiness_type_id());
            accountFlowInModel.setUserId(accountFlowKgMongo.getUser_id());
            accountFlowInModel.setArticleId(accountFlowKgMongo.getArticle_id());
            accountFlowInModel.setAccountAmount(accountFlowKgMongo.getAccount_amount());
            accountFlowInModel.setAmount(accountFlowKgMongo.getAmount());
            Date date=new Date();
            date.setTime(accountFlowKgMongo.getFlow_date());
            accountFlowInModel.setFlowDate(date);
            accountFlowInModel.setFlowDetail(accountFlowKgMongo.getFlow_detail());
            accountFlowInModel.setFlowStatus(accountFlowKgMongo.getFlow_status());
            accountFlowInModel.setRelationFlowId(accountFlowKgMongo.getRelation_flow_id());
            accountFlowInModel.setUserEmail(accountFlowKgMongo.getUser_email());
            accountFlowInModel.setUserPhone(accountFlowKgMongo.getUser_phone());
            date=new Date();
            date.setTime(accountFlowKgMongo.getAccount_date());
            accountFlowInModel.setAccountDate(date);
            accountFlowInModel.setPoundageAmount(accountFlowKgMongo.getPoundage_amount());
            accountFlowInModel.setTxAddress(accountFlowKgMongo.getTx_address());
            accountFlowInModel.setTxId(accountFlowKgMongo.getTx_id());
            accountFlowInModel.setRemark(accountFlowKgMongo.getRemark());
            accountFlowTxbWMapper.insertSelective(accountFlowInModel);
            return Action.CommitMessage;
        } catch (Exception e) {
            log.error(e.getMessage());
            //消费失败
            return Action.ReconsumeLater;
        }
    }
}