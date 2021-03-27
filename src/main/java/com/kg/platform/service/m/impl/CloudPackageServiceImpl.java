package com.kg.platform.service.m.impl;

import com.kg.platform.common.entity.MJsonEntity;
import com.kg.platform.common.schedule.CoinAvaPriceJobHandler;
import com.kg.platform.common.utils.HttpUtil;
import com.kg.platform.common.utils.JedisKey;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.dao.cloud.CloudPackageMapper;
import com.kg.platform.dao.entity.CloudPackage;
import com.kg.platform.model.response.CloudPackageResponse;
import com.kg.platform.model.response.CurrencyResponse;
import com.kg.platform.service.m.CloudPackageService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service("cloudPackageService")
public class CloudPackageServiceImpl implements CloudPackageService {

    private static final String BTC_URL = "https://blockchain.info/q/getdifficulty";
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36";

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CloudPackageMapper cloudPackageMapper;

    @Autowired
    private JedisUtils jedisUtils;


    @Override
    public MJsonEntity getPackageList() {
        List<CloudPackage> cloudPackages = cloudPackageMapper.getPackageList();
        if (CollectionUtils.isEmpty(cloudPackages)) {
            return MJsonEntity.makeSuccessJsonEntity(new ArrayList<>());
        }
        List<CloudPackageResponse> cloudPackageResponseList = buildList(cloudPackages);
        return MJsonEntity.makeSuccessJsonEntity(cloudPackageResponseList);
    }

    private BigDecimal buildDifficulty() {
        String data = HttpUtil.get(BTC_URL, USER_AGENT);
        if (StringUtils.isEmpty(data)) {
            return null;
        }
        return new BigDecimal(data);
    }

    private List<CloudPackageResponse> buildList(List<CloudPackage> cloudPackages) {
		CurrencyResponse currencyResponse = jedisUtils.get(JedisKey.coinAvaData(CoinAvaPriceJobHandler.BTC_NAME), CurrencyResponse.class);
        List<CloudPackageResponse> cloudPackageResponseList = new ArrayList<>();
        cloudPackages.forEach(cloudPackage -> {
            CloudPackageResponse response = build(cloudPackage, currencyResponse);
            cloudPackageResponseList.add(response);
        });
        return cloudPackageResponseList;
    }

    private CloudPackageResponse build(CloudPackage cloudPackage,CurrencyResponse currencyResponse) {
        CloudPackageResponse response = new CloudPackageResponse();
        BeanUtils.copyProperties(cloudPackage, response);
        response.setCastLines(new BigDecimal(cloudPackage.getCastLines()).stripTrailingZeros().toPlainString());
        response.setDailyEarningsBtc(cloudPackage.getDailyEarningsBtc().stripTrailingZeros().toPlainString());
        if(currencyResponse!=null){
			logger.info("从缓存中获取BTC均价用于计算------");
        	BigDecimal dailyRmb = cloudPackage.getDailyEarningsBtc().multiply(new BigDecimal(currencyResponse.getLastPriceCNY()));
			response.setDailyEarningsRmb(dailyRmb.setScale(3, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());
		}else {
			response.setDailyEarningsRmb(new BigDecimal(cloudPackage.getDailyEarningsRmb()).setScale(3, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());
		}
        response.setPerformance(new BigDecimal(cloudPackage.getPerformance()).stripTrailingZeros().toPlainString());
        response.setPrice(new BigDecimal(cloudPackage.getPrice()).stripTrailingZeros().toPlainString());
        response.setYearsEarningsPercent(new BigDecimal(cloudPackage.getYearsEarningsPercent()).stripTrailingZeros().toPlainString());
        response.setElectricPrice(new BigDecimal(cloudPackage.getElectricPrice()).setScale(3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());
        response.setDailyElectricPower(new BigDecimal(cloudPackage.getDailyElectricPower()).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());
        return response;
    }
}
