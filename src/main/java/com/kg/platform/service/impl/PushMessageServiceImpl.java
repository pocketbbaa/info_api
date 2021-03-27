package com.kg.platform.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.utils.Threads;
import com.kg.platform.enumeration.PushTypeEnum;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.mq.MQConfig;
import com.kg.platform.common.mq.MQProduct;
import com.kg.platform.common.utils.Bean2MapUtil;
import com.kg.platform.common.utils.JsonUtil;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.dao.entity.PushMessage;
import com.kg.platform.dao.read.KgInfoSwitchRMapper;
import com.kg.platform.model.in.MessageInModel;
import com.kg.platform.model.out.KgInfoSwitchOutModel;
import com.kg.platform.service.PushMessageService;
import com.kg.platform.service.TokenManager;

@Service
@Transactional
public class PushMessageServiceImpl implements PushMessageService {
    private static final Logger logger = LoggerFactory.getLogger(PushMessageServiceImpl.class);

    @Autowired
    private KgInfoSwitchRMapper kgInfoSwitchRMapper;

    @Autowired
    private IDGen idGen;

    @Autowired
    private MQConfig awardMqConfig;

    @Autowired
    private MQProduct mqProduct;

    @Inject
    private TokenManager manager;

    @Override
    public void pushMessge(MessageInModel messageInModel) {

        try {
            logger.info("【APP消息推送】messageInModel：" + messageInModel);
            //如果指定用户推送，校验推送对象信息+用户的推送开关+记录log
            if (messageInModel.getType() == PushTypeEnum.PUSH_SPECIFIED.getCode()) {

                if (StringUtils.isEmpty(messageInModel.getRecieve())) {
                    logger.info("【消息推送】没有用户信息");
                    return;
                }

                //消息log
                List<String> receive = new ArrayList<>();
                receive.add(messageInModel.getRecieve());
                PushMessage pushMessage = buildPushMessage(messageInModel, receive);
                logger.info("【APP消息推送】消息入库 pushMessage：" + JSONObject.toJSONString(pushMessage));
                MongoUtils.insertOne(PushMessage.mongoTable, new Document(Bean2MapUtil.bean2map(pushMessage)));

                //手机内部消息不进行推送
                if (messageInModel.getPushPlace() == 1) {
                    return;
                }

                //校验推送开关
                if (!checkSwitch(messageInModel)) {
                    return;
                }
            }

            // 发送消息
            mqProduct.sendMessage(messageInModel.getTags(), "push", JSON.toJSONString(messageInModel));

        } catch (Exception e) {
            logger.info(Threads.getExceptionLocation(e));
        }

    }


    /**
     * 校验推送开关
     *
     * @param messageInModel
     * @return
     */
    private boolean checkSwitch(MessageInModel messageInModel) {
        KgInfoSwitchOutModel kgInfoSwitchOutModel = kgInfoSwitchRMapper
                .selectByPrimaryKey(Long.valueOf(messageInModel.getRecieve()));
        if (kgInfoSwitchOutModel == null) {
            return false;
        }
        if (messageInModel.getCheckLogin() == 1) {
            logger.info("------------验证登录------------");
            String token = manager.getTokenByUserId(Long.valueOf(messageInModel.getRecieve()));
            if (StringUtils.isEmpty(token)) {
                logger.info("------------用户未登陆------------");
                return false;
            }
        }
        logger.info("------------开关信息------------" + JsonUtil.writeValueAsString(kgInfoSwitchOutModel));
        if (kgInfoSwitchOutModel.getSystemInfoSwitch() == 0) {
            logger.info("----------总开关已关闭------------");
            return false;
        }

        int switchResult = kgInfoSwitchOutModel.switchResult(messageInModel.getClassify());
        if (switchResult == 0) {
            logger.info("------------开关已关闭------------" + messageInModel.getClassify());
            return false;
        }
        return true;
    }

    /**
     * 构建消息入库model
     *
     * @param messageInModel
     * @return
     */
    private PushMessage buildPushMessage(MessageInModel messageInModel, List<String> receive) {
        PushMessage pushMessage = new PushMessage();
        BeanUtils.copyProperties(messageInModel, pushMessage);
        pushMessage.set_id(idGen.nextId());
        pushMessage.setMessageText(messageInModel.getMessage());
        pushMessage.setCreateDate(new Date().getTime());
        pushMessage.setReadState(0);
        pushMessage.setReceive(receive);
        return pushMessage;
    }

}
