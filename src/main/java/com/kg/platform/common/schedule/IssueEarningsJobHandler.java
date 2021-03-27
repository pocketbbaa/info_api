package com.kg.platform.common.schedule;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.mongo.MongoTables;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.utils.Bean2MapUtil;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.dao.cloud.CloudOrderMapper;
import com.kg.platform.dao.cloud.CloudPackageMapper;
import com.kg.platform.dao.entity.CloudOrder;
import com.kg.platform.dao.entity.CloudPackage;
import com.kg.platform.dao.write.AccountWMapper;
import com.kg.platform.enumeration.BusinessTypeEnum;
import com.kg.platform.enumeration.FlowStatusEnum;
import com.kg.platform.model.mongoTable.AccountFlowBTCMongo;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 发放收益
 */
@Component
@JobHander(value = "issueEarningsJobHandler")
public class IssueEarningsJobHandler extends IJobHandler {

    private static final int PAGE_SIZE = 100;

    public static final String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36";

    @Autowired
    private CloudOrderMapper cloudOrderMapper;

    @Autowired
    private IDGen idGenerater;

    @Autowired
    private AccountWMapper accountWMapper;

    @Autowired
    private CloudPackageMapper cloudPackageMapper;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        XxlJobLogger.log("发放收益 start ......");
        //1:获取可以计算收益订单
        int currentPage = 1;
        while (true) {
            int pageIndex = (currentPage - 1) * PAGE_SIZE;
            String time = DateUtils.formatYMD(DateUtils.getBeforeDay(new Date(), 1)) + " 00:00:00";
            XxlJobLogger.log("time:" + time);
            List<CloudOrder> cloudOrderList = cloudOrderMapper.getPaymentList(pageIndex, PAGE_SIZE, time);
            if (CollectionUtils.isEmpty(cloudOrderList)) {
                XxlJobLogger.log("没有待发放订单");
                break;
            }
            XxlJobLogger.log("发放收益待发放订单数 cloudOrderLis.size:" + cloudOrderList.size());
            cloudOrderList.forEach(cloudOrder -> {

                Date payTime = cloudOrder.getPayTime();
                Date now = new Date();
                CloudPackage cloudPackage = cloudPackageMapper.selectByPrimaryKey(cloudOrder.getPackageId());
                Integer timeLimit = cloudPackage.getTimeLimit();
                XxlJobLogger.log("判断订单是否过期 payTime：" + DateUtils.formatYMDHMS(payTime) + "  now:" + DateUtils.formatYMDHMS(now));
                if (DateUtils.getDaysByDate(payTime, now) > timeLimit) {
                    XxlJobLogger.log("该订单已过期 cloudOrder：" + JSONObject.toJSONString(cloudOrder));
                    CloudOrder order = new CloudOrder();
                    order.setId(cloudOrder.getId());
                    order.setState(1);
                    int update = cloudOrderMapper.updateByPrimaryKeySelective(order);
                    XxlJobLogger.log("设置订单过期状态 update：" + update);
                    return;
                }

                XxlJobLogger.log("发放收益 cloudOrder：" + JSONObject.toJSONString(cloudOrder));
                addBTCAccount(cloudOrder, cloudPackage);
                addOrderBTCEarnings(cloudOrder, cloudPackage);
                insertMongo(cloudOrder, cloudPackage);
                XxlJobLogger.log("发放收益完成");
            });
            currentPage++;
        }
        return ReturnT.SUCCESS;
    }

    /**
     * 订单累计收益
     *
     * @param cloudOrder
     */
    private void addOrderBTCEarnings(CloudOrder cloudOrder, CloudPackage cloudPackage) {
        if (cloudPackage == null) {
            return;
        }
        BigDecimal dailyEarningsBtcToOrder = getDailyEarningsBtcToOrder(cloudOrder, cloudPackage);
        cloudOrderMapper.issueBTCEarnings(cloudOrder.getId(), dailyEarningsBtcToOrder);
    }

    /**
     * 获取订单每天收益
     *
     * @param cloudOrder
     * @param cloudPackage
     * @return
     */
    private BigDecimal getDailyEarningsBtcToOrder(CloudOrder cloudOrder, CloudPackage cloudPackage) {
        BigDecimal dailyEarningsBtc = cloudPackage.getDailyEarningsBtc();
        Integer number = cloudOrder.getNumber();
        return dailyEarningsBtc.multiply(new BigDecimal(number));
    }


    /**
     * 用户账户加BTC收益
     *
     * @param cloudOrder
     */
    private void addBTCAccount(CloudOrder cloudOrder, CloudPackage cloudPackage) {
        if (cloudPackage == null) {
            return;
        }
        BigDecimal dailyEarningsBtcToOrder = getDailyEarningsBtcToOrder(cloudOrder, cloudPackage);
        accountWMapper.issueBTCEarnings(cloudOrder.getUserId(), dailyEarningsBtcToOrder);
    }

    /**
     * 记录收益流水
     *
     * @param cloudOrder
     */
    private void insertMongo(CloudOrder cloudOrder, CloudPackage cloudPackage) {
        BigDecimal dailyEarningsBtcToOrder = getDailyEarningsBtcToOrder(cloudOrder, cloudPackage);
        AccountFlowBTCMongo accountFlowBTCMongo = new AccountFlowBTCMongo();
        long flowid = idGenerater.nextId();
        accountFlowBTCMongo.setAccount_flow_id(flowid);
        accountFlowBTCMongo.setRelation_flow_id(flowid);
        accountFlowBTCMongo.setUser_id(cloudOrder.getUserId());
        accountFlowBTCMongo.setUser_phone(cloudOrder.getPhone());
        accountFlowBTCMongo.setUser_name(cloudOrder.getPayee());
        accountFlowBTCMongo.setAmount(dailyEarningsBtcToOrder);
        accountFlowBTCMongo.setAccount_amount(dailyEarningsBtcToOrder);
        accountFlowBTCMongo.setFlow_date(System.currentTimeMillis());
        accountFlowBTCMongo.setFlow_detail("云算力收益");
        accountFlowBTCMongo.setBusiness_type_id(BusinessTypeEnum.KG_CLOUD_BTC_EARNINGS.getStatus());
        accountFlowBTCMongo.setFlow_status(FlowStatusEnum.NOSTATUS.getStatus());
        MongoUtils.insertOne(MongoTables.KG_CLOUD_BTC_EARNINGS, new Document(Bean2MapUtil.bean2map(accountFlowBTCMongo)));
    }


}
