package com.kg.platform.common.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.kg.platform.common.exception.ApiException;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.mongo.MongoTables;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.utils.Bean2MapUtil;
import com.kg.platform.enumeration.BusinessTypeEnum;
import com.kg.platform.enumeration.FlowStatusEnum;
import com.kg.platform.model.in.admin.RitExchangeInModel;
import com.kg.platform.model.mongoTable.AccountFlowRitMongo;
import com.kg.platform.model.response.UserkgResponse;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Map;

/**
 * 兑换流水记录Consumer
 */
public class ConvertFlowMQConsumer implements MessageListener {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IDGen idGenerater;

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {

        log.info("mqUrl:" + message.getBornHost() + "  topic:" + message.getTopic() + "  tags:" + message.getTag());
        Long flowId = 0L, accountRitFlowId = 0L, accountKgFlowId = 0L;
        try {
            log.info("【兑换流水记录Consumer】 new message comming ....");
            log.info("msgId:" + message.getMsgID());
            String body = new String(message.getBody());
            log.info("【兑换流水记录Consumer】message body : " + body);
            if (StringUtils.isEmpty(body)) {
                log.info("消息体为空 !!!");
                return Action.CommitMessage;
            }
            Map map = (Map) JSONObject.parse(body);
            String kguserJson = URLDecoder.decode((String) map.get("kguser"), "utf-8");

            UserkgResponse kguser = JSONObject.parseObject(kguserJson, UserkgResponse.class);
            BigDecimal ritAmount = new BigDecimal(String.valueOf(map.get("ritAmount")));
            BigDecimal reduceKgBalance = new BigDecimal(String.valueOf(map.get("reduceKgBalance")));
            String rate = String.valueOf(map.get("rate"));
            String[] rateArr = rate.split("-");

            log.info("【兑换流水记录Consumer】kguser:" + JSONObject.toJSONString(kguser));
            log.info("【兑换流水记录Consumer】ritAmount:" + ritAmount.toString());
            log.info("【兑换流水记录Consumer】reduceKgBalance:" + reduceKgBalance.toString());
            log.info("【兑换流水记录Consumer】rate:" + rate);
            flowId = insertRitExchaneFlow(kguser, ritAmount, reduceKgBalance);

            accountRitFlowId = insertAccountRitFlow(kguser, ritAmount, flowId, rateArr);
            accountKgFlowId = insertAccountKgFlow(kguser, reduceKgBalance, flowId, rateArr);

            insertSysAccountRitFlow(kguser, ritAmount, flowId);
            insertSysAccountKgFlow(kguser, reduceKgBalance, rateArr, flowId);
            log.info("【兑换流水记录Consumer】记录兑换流水成功");
            return Action.CommitMessage;
        } catch (Exception e) {
            e.printStackTrace();
            delFlow(flowId, accountRitFlowId, accountKgFlowId);
            return Action.CommitMessage;
        }
    }

    /**
     * 添加系统账户兑换KG流水
     *
     * @param kguser
     * @param reduceKgBalance
     * @param rateArr
     */
    private void insertSysAccountKgFlow(UserkgResponse kguser, BigDecimal reduceKgBalance, String[] rateArr, Long flowId) {
        long now = new Date().getTime();
        AccountFlowRitMongo accountFlowRitMongo = new AccountFlowRitMongo();
        accountFlowRitMongo.setAccount_flow_id(idGenerater.nextId());
        accountFlowRitMongo.setRelation_flow_id(flowId);
        accountFlowRitMongo.setUser_id((long) -1);
        accountFlowRitMongo.setUser_name("平台账户");
        accountFlowRitMongo.setAmount(reduceKgBalance);
        accountFlowRitMongo.setBusiness_type_id(BusinessTypeEnum.CONVERT_KG_OUT.getStatus());
        accountFlowRitMongo.setAccount_amount(reduceKgBalance);
        accountFlowRitMongo.setFlow_status(FlowStatusEnum.NOSTATUS.getStatus());
        accountFlowRitMongo.setFlow_date(now);
        accountFlowRitMongo.setAccount_date(now);
        accountFlowRitMongo.setFlow_detail("兑换RIT，汇率 " + rateArr[0] + ":" + rateArr[1]);
        accountFlowRitMongo.setRemark("KG消耗");
        Map<String, Object> map = Bean2MapUtil.bean2map(accountFlowRitMongo);
        map.put("_id", accountFlowRitMongo.getAccount_flow_id());
        MongoUtils.insertOne(MongoTables.ACCOUNT_FLOW_KG, new Document(map));
        log.info("【兑换流水记录Consumer】添加系统账户兑换KG流水成功");
    }

    /**
     * 添加系统账户RIT兑换流水
     *
     * @param kguser
     * @param ritAmount
     * @param flowId
     */
    private void insertSysAccountRitFlow(UserkgResponse kguser, BigDecimal ritAmount, Long flowId) {
        ritAmount = new BigDecimal("-" + ritAmount.stripTrailingZeros().toPlainString());
        long now = new Date().getTime();
        AccountFlowRitMongo accountFlowRitMongo = new AccountFlowRitMongo();
        accountFlowRitMongo.setAccount_flow_id(idGenerater.nextId());
        accountFlowRitMongo.setRelation_flow_id(flowId);
        accountFlowRitMongo.setUser_id((long) -1);
        accountFlowRitMongo.setUser_name("平台账户");
        accountFlowRitMongo.setAmount(ritAmount);
        accountFlowRitMongo.setBusiness_type_id(BusinessTypeEnum.CONVERT_RIT_IN.getStatus());
        accountFlowRitMongo.setAccount_amount(ritAmount);
        accountFlowRitMongo.setFlow_status(FlowStatusEnum.NOSTATUS.getStatus());
        accountFlowRitMongo.setFlow_date(now);
        accountFlowRitMongo.setAccount_date(now);
        accountFlowRitMongo.setFlow_detail("发放氪金兑换RIT奖励给" + kguser.getUserId());
        accountFlowRitMongo.setRemark("氪金兑换RIT");
        Map<String, Object> map = Bean2MapUtil.bean2map(accountFlowRitMongo);
        map.put("_id", accountFlowRitMongo.getAccount_flow_id());
        MongoUtils.insertOne(MongoTables.ACCOUNT_FLOW_RIT, new Document(map));
        log.info("【兑换流水记录Consumer】添加系统账户RIT兑换流水成功 ");
    }

    /**
     * 删除已保存流水
     *
     * @param flowId
     * @param accountRitFlowId
     * @param accountKgFlowId
     */
    private void delFlow(Long flowId, Long accountRitFlowId, Long accountKgFlowId) {
        if (flowId > 0L) {
            MongoUtils.deleteById(MongoTables.KG_RIT_EXCHANGE_FLOW, String.valueOf(flowId));
        }
        if (accountRitFlowId > 0L) {
            MongoUtils.deleteById(MongoTables.ACCOUNT_FLOW_RIT, String.valueOf(accountRitFlowId));
        }
        if (accountKgFlowId > 0L) {
            MongoUtils.deleteById(MongoTables.ACCOUNT_FLOW_KG, String.valueOf(accountKgFlowId));
        }
    }


    /**
     * 增加兑换流水
     *
     * @param userId
     * @param ritAmount
     * @param reduceKgBalance
     * @throws Exception
     */
    private Long insertRitExchaneFlow(UserkgResponse kguser, BigDecimal ritAmount, BigDecimal reduceKgBalance) throws Exception {
        RitExchangeInModel response = new RitExchangeInModel();
        response.setId(idGenerater.nextId());
        response.setUserId(Long.valueOf(kguser.getUserId()));
        response.setUserName(kguser.getUserName());
        response.setUserPhone(kguser.getMobIle());
        response.setUserRole(kguser.getUserRole());
        response.setRitAmount(ritAmount);
        response.setKgAmount(reduceKgBalance);
        response.setCreateTime(new Date().getTime());
        Map<String, Object> map = Bean2MapUtil.bean2map(response);
        map.put("_id", response.getId());
        MongoUtils.insertOne(MongoTables.KG_RIT_EXCHANGE_FLOW, new Document(map));
        log.info("[kgToRit 兑换] -> 增加兑换流水成功 userId:" + kguser.getUserId() + "  RitExchangeInModel:" + JSONObject.toJSONString(response));
        return response.getId();
    }

    /**
     * 增加rit流水
     *
     * @param kguser
     * @param ritAmount
     * @param reduceKgBalance
     * @throws ApiException
     */
    private Long insertAccountRitFlow(UserkgResponse kguser, BigDecimal ritAmount, Long flowId, String[] rateArr) throws Exception {
        long now = new Date().getTime();
        AccountFlowRitMongo accountFlowRitMongo = new AccountFlowRitMongo();
        accountFlowRitMongo.setAccount_flow_id(idGenerater.nextId());
        accountFlowRitMongo.setRelation_flow_id(flowId);
        accountFlowRitMongo.setUser_id(Long.valueOf(kguser.getUserId()));
        accountFlowRitMongo.setUser_name(kguser.getUserName());
        accountFlowRitMongo.setUser_phone(kguser.getMobIle());
        accountFlowRitMongo.setUser_email(kguser.getUserEmail());
        accountFlowRitMongo.setAmount(ritAmount);
        accountFlowRitMongo.setBusiness_type_id(BusinessTypeEnum.CONVERT_RIT_IN.getStatus());
        accountFlowRitMongo.setAccount_amount(ritAmount);
        accountFlowRitMongo.setFlow_status(FlowStatusEnum.NOSTATUS.getStatus());
        accountFlowRitMongo.setFlow_date(now);
        accountFlowRitMongo.setAccount_date(now);
        accountFlowRitMongo.setFlow_detail("氪金兑换RIT，汇率 " + rateArr[0] + ":" + rateArr[1]);
        accountFlowRitMongo.setRemark("");
        Map<String, Object> map = Bean2MapUtil.bean2map(accountFlowRitMongo);
        map.put("_id", accountFlowRitMongo.getAccount_flow_id());
        MongoUtils.insertOne(MongoTables.ACCOUNT_FLOW_RIT, new Document(map));
        log.info("【兑换流水记录Consumer】增加rit流水成功 userId:" + kguser.getUserId());
        return accountFlowRitMongo.getAccount_flow_id();
    }

    /**
     * 增加KG流水
     *
     * @param kguser
     * @param ritAmount
     * @param reduceKgBalance
     * @param flowId
     */
    private Long insertAccountKgFlow(UserkgResponse kguser, BigDecimal reduceKgBalance, Long flowId, String[] rateArr) throws Exception {
        reduceKgBalance = new BigDecimal("-" + reduceKgBalance.stripTrailingZeros().toPlainString());
        long now = new Date().getTime();

        AccountFlowRitMongo accountFlowRitMongo = new AccountFlowRitMongo();
        accountFlowRitMongo.setAccount_flow_id(idGenerater.nextId());
        accountFlowRitMongo.setRelation_flow_id(flowId);
        accountFlowRitMongo.setUser_id(Long.valueOf(kguser.getUserId()));
        accountFlowRitMongo.setUser_name(kguser.getUserName());
        accountFlowRitMongo.setUser_phone(kguser.getMobIle());
        accountFlowRitMongo.setUser_email(kguser.getUserEmail());
        accountFlowRitMongo.setAmount(reduceKgBalance);
        accountFlowRitMongo.setBusiness_type_id(BusinessTypeEnum.CONVERT_KG_OUT.getStatus());
        accountFlowRitMongo.setAccount_amount(reduceKgBalance);
        accountFlowRitMongo.setFlow_status(FlowStatusEnum.NOSTATUS.getStatus());
        accountFlowRitMongo.setFlow_date(now);
        accountFlowRitMongo.setAccount_date(now);
        accountFlowRitMongo.setFlow_detail("兑换RIT，汇率 " + rateArr[0] + ":" + rateArr[1]);
        accountFlowRitMongo.setRemark("");

        Map<String, Object> map = Bean2MapUtil.bean2map(accountFlowRitMongo);
        map.put("_id", accountFlowRitMongo.getAccount_flow_id());

        MongoUtils.insertOne(MongoTables.ACCOUNT_FLOW_KG, new Document(map));
        log.info("【兑换流水记录Consumer】增加rit流水成功 userId:" + kguser.getUserId());
        return accountFlowRitMongo.getAccount_flow_id();
    }
}
