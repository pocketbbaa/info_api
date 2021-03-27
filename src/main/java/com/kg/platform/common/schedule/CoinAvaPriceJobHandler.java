package com.kg.platform.common.schedule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.utils.HttpUtil;
import com.kg.platform.common.utils.JedisKey;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.model.response.CurrencyResponse;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 数字币均价获取
 */
@Component
@JobHander(value = "coinAvaPriceJobHandler")
public class CoinAvaPriceJobHandler extends IJobHandler {

    private static final String USERAGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36";

    public static final String BTC_NAME = "BTC";

    @Autowired
    private JedisUtils jedisUtils;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {

        try {
            XxlJobLogger.log("数字币均价获取 start ......");
            /**
             * 获取区块奖励/系数/难度
             */
            String result = HttpUtil.get("https://appapi.ks.top/api/currency/appCurrencyList", USERAGENT);
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("responseBody");
			List<CurrencyResponse> currencyResponseList = JSONArray.parseArray(jsonArray.toJSONString(),CurrencyResponse.class);
			final CurrencyResponse[] btcCurrencyResponse = {new CurrencyResponse()};
			currencyResponseList.forEach(currencyResponse -> {
				if(BTC_NAME.equalsIgnoreCase(currencyResponse.getCurrencyName())){
					btcCurrencyResponse[0] = currencyResponse;
				}
			});
            CurrencyResponse currencyResponse = btcCurrencyResponse[0];
            BigDecimal lastPriceCNY = new BigDecimal(currencyResponse.getLastPriceCNY());
			jedisUtils.set(JedisKey.coinAvaData(currencyResponse.getCurrencyName()), currencyResponse);
			XxlJobLogger.log("获取RMB：lastPriceCNY：" + lastPriceCNY.stripTrailingZeros().toPlainString());
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            XxlJobLogger.log("获取BTC均价 异常 e：" + e.getMessage());
            e.printStackTrace();
            return ReturnT.SUCCESS;
        }
    }

}
