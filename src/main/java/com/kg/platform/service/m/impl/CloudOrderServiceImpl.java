package com.kg.platform.service.m.impl;

import com.kg.platform.common.entity.MJsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.dao.cloud.CloudOrderMapper;
import com.kg.platform.dao.cloud.CloudPackageMapper;
import com.kg.platform.dao.entity.CloudOrder;
import com.kg.platform.dao.entity.CloudPackage;
import com.kg.platform.dao.read.UserRMapper;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.CloudOrderRequest;
import com.kg.platform.service.m.CloudOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("cloudOrderService")
public class CloudOrderServiceImpl implements CloudOrderService {

    private static final Logger log = LoggerFactory.getLogger(CloudOrderServiceImpl.class);

    @Autowired
    private CloudOrderMapper cloudOrderMapper;

    @Autowired
    private UserRMapper userRMapper;

    @Autowired
    private IDGen idGenerater;

    @Autowired
    private CloudPackageMapper cloudPackageMapper;

    @Override
    public MJsonEntity submitOrder(CloudOrderRequest request) {

        long id = idGenerater.nextId();
        CloudOrder cloudOrder = buildCloudOrder(request, id);
        if (cloudOrder == null) {
            return MJsonEntity.makeExceptionJsonEntity("", "订单价格有误");
        }
        int success = cloudOrderMapper.insertSelective(cloudOrder);
        if (success > 0) {
            Map<String, String> map = new HashMap<>(5);
            map.put("orderId", String.valueOf(id));
            map.put("totalPrice", new BigDecimal(cloudOrder.getPrice()).setScale(3, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());
            return MJsonEntity.makeSuccessJsonEntity(map);
        }
        return MJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
    }

    private CloudOrder buildCloudOrder(CloudOrderRequest request, long id) {
        CloudOrder cloudOrder = new CloudOrder();
        cloudOrder.setChannelId(request.getChannelId());
        cloudOrder.setId(id);

        Long userId = request.getUserId();
        UserkgOutModel userkgOutModel = userRMapper.selectByPrimaryKey(userId);
        cloudOrder.setUserId(userId);
        cloudOrder.setPhone(userkgOutModel.getUserMobile());
        cloudOrder.setPayState(CloudOrder.PAYSTATE_NOTPAY);

        Integer packageId = request.getPackageId();
        cloudOrder.setPackageId(packageId);
        CloudPackage cloudPackage = cloudPackageMapper.selectByPrimaryKey(packageId);
        cloudOrder.setPackageName(cloudPackage.getName());

        Integer number = request.getNumber();
        cloudOrder.setNumber(number);
        String totalPrice = request.getTotalPrice();

        double relPrice = cloudPackage.getPrice() * number;

        if (Double.valueOf(totalPrice).equals(relPrice)) {
            log.info("价格一致 ...totalPrice：" + totalPrice + "  relPrice：" + relPrice);
            //如果不包含电费
            Integer haveElectricPrice = cloudPackage.getHaveElectricPrice();
            if (haveElectricPrice == 0) {
                Double dailyElectricPower = cloudPackage.getDailyElectricPower();
                Double electricPrice = cloudPackage.getElectricPrice();
                Integer timeLimit = cloudPackage.getTimeLimit();
                BigDecimal totalelectricPrice = new BigDecimal(dailyElectricPower)
                        .multiply(new BigDecimal(electricPrice))
                        .multiply(new BigDecimal(timeLimit))
                        .multiply(new BigDecimal(number));
                Double price = totalelectricPrice.add(new BigDecimal(relPrice)).setScale(3, BigDecimal.ROUND_DOWN).doubleValue();
                cloudOrder.setPrice(price);
            } else {
                cloudOrder.setPrice(relPrice);
            }
        } else {
            log.info("价格不一致 ...totalPrice：" + totalPrice + "  relPrice：" + relPrice);
            return null;
        }
        String totalPerformance = request.getTotalPerformance();
        double relPerformance = cloudPackage.getPerformance() * number;
        if (Double.valueOf(totalPerformance).equals(relPerformance)) {
            log.info("算力一致 ...totalPerformance：" + totalPerformance + "  relPerformance：" + relPerformance);
            cloudOrder.setPerformance(relPerformance);
        } else {
            log.info("算力不一致 ...totalPerformance：" + totalPerformance + "  relPerformance：" + relPerformance);
            return null;
        }

        cloudOrder.setCreateTime(new Date());
        cloudOrder.setState(CloudOrder.STATE_TIMEING);
        return cloudOrder;
    }
}
