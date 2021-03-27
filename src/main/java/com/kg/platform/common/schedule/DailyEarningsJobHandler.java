package com.kg.platform.common.schedule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.utils.HttpUtil;
import com.kg.platform.dao.cloud.CloudPackageMapper;
import com.kg.platform.dao.entity.CloudPackage;
import com.kg.platform.model.response.CurrencyResponse;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 日收益计算
 * 日收益=算力（h/s）*时间（秒）*区块奖励/系数/难度*币的价格(CNY)
 * 日收益更新规则：每个自然日12点获取当日最新收益
 */
@Component
@JobHander(value = "dailyEarningsJobHandler")
public class DailyEarningsJobHandler extends IJobHandler {

    public static final String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36";

    @Autowired
    private CloudPackageMapper cloudPackageMapper;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {

        try {
            XxlJobLogger.log("日收益计算 start ......");
            /**
             * 获取区块奖励/系数/难度
             */
            String result = HttpUtil.get("https://appapi.ks.top/api/currency/appCurrencyList", userAgent);
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("responseBody");
            JSONObject json = jsonArray.getJSONObject(0);
            CurrencyResponse currencyResponse = JSONObject.parseObject(JSONObject.toJSONString(json), CurrencyResponse.class);

            //难度
            BigDecimal currencyDifficulty = new BigDecimal(currencyResponse.getCurrencyDifficulty());
            XxlJobLogger.log("获取BTC难度：currencyDifficulty：" + currencyDifficulty.stripTrailingZeros().toPlainString());

            //区块奖励
            BigDecimal blockAward = new BigDecimal(currencyResponse.getBlockAward());
            XxlJobLogger.log("获取BTC区块奖励：blockAward：" + blockAward.stripTrailingZeros().toPlainString());

            //系数
            BigDecimal currencyCoefficient = new BigDecimal(currencyResponse.getCurrencyCoefficient());
            XxlJobLogger.log("获取BTC系数：currencyCoefficient：" + currencyCoefficient.stripTrailingZeros().toPlainString());

            //一天时间
            BigDecimal time = new BigDecimal(24 * 60 * 60);
            XxlJobLogger.log("获取计算时间：time：" + time.stripTrailingZeros().toPlainString());

            BigDecimal lastPriceCNY = new BigDecimal(currencyResponse.getLastPriceCNY());
            XxlJobLogger.log("获取RMB：lastPriceCNY：" + lastPriceCNY.stripTrailingZeros().toPlainString());

            List<CloudPackage> cloudPackageList = cloudPackageMapper.getPackageList();
            /**
             * 计算日收益
             */
            cloudPackageList.forEach(cloudPackage -> {
                XxlJobLogger.log("计算收益-套餐：cloudPackage：" + JSONObject.toJSONString(cloudPackage));
                //每份算力
                BigDecimal performance = new BigDecimal(cloudPackage.getPerformance());

                XxlJobLogger.log("计算收益-开始计算：performance：" + performance.toPlainString() + "  time：" + time.toPlainString() + "  blockAward:" + blockAward.toPlainString() + "  currencyCoefficient:" + currencyCoefficient.toPlainString() + "  currencyDifficulty:" + currencyDifficulty.toPlainString());
                //日收益（BTC/份）
                BigDecimal dailyEarningsBtc1 = performance.multiply(time).multiply(blockAward).divide(currencyCoefficient, 20, RoundingMode.HALF_DOWN);
                XxlJobLogger.log("计算结果：日收益（BTC/份）： dailyEarningsBtc1 ：" + dailyEarningsBtc1.toPlainString());
                BigDecimal dailyEarningsBtc = dailyEarningsBtc1.divide(currencyDifficulty, 20, RoundingMode.HALF_DOWN).multiply(new BigDecimal(1000000000000L));
                //日收益（元/份）
                BigDecimal dailyEarningsRmb = dailyEarningsBtc.multiply(lastPriceCNY.multiply(new BigDecimal(1.1)));

                XxlJobLogger.log("计算结果：日收益（BTC/份）：dailyEarningsBtc ：" + dailyEarningsBtc.toPlainString());
                XxlJobLogger.log("计算结果：日收益（元/份）：dailyEarningsRmb ：" + dailyEarningsRmb.toPlainString());
                CloudPackage record = new CloudPackage();
                record.setDailyEarningsBtc(dailyEarningsBtc);
                record.setDailyEarningsRmb(dailyEarningsRmb.doubleValue());
                record.setId(cloudPackage.getId());
                int success = cloudPackageMapper.updateByPrimaryKeySelective(record);
                XxlJobLogger.log("更新日收益结果 success：" + (success > 0));
            });

            return ReturnT.SUCCESS;
        } catch (Exception e) {
            XxlJobLogger.log("更新日收益结果 异常 e：" + e.getMessage());
            e.printStackTrace();
            return ReturnT.SUCCESS;
        }
    }

}
