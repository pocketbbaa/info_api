package com.kg.platform.service.app.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.exception.ApiException;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.lock.Lock;
import com.kg.platform.common.mongo.MongoTables;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.mongo.Seach;
import com.kg.platform.common.mq.MQConfig;
import com.kg.platform.common.mq.MQProduct;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.dao.entity.Account;
import com.kg.platform.dao.entity.KgRitConvert;
import com.kg.platform.dao.read.AccountRMapper;
import com.kg.platform.dao.read.AppKgRitConvertRMapper;
import com.kg.platform.dao.write.AccountWMapper;
import com.kg.platform.model.in.AccountInModel;
import com.kg.platform.model.response.ConvertCompleteResponse;
import com.kg.platform.model.response.ConvertIndexResponse;
import com.kg.platform.model.response.RitConvertResponse;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.app.AppAccountConvertService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Decimal128;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

@Service
public class AppAccountConvertServiceImpl implements AppAccountConvertService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String KG_TO_RIT = "kgapi::AccountService::kgToRit::";
    private static final int CONVERT_NOW = 1; //兑换
    private static final int CONVERT_CHECK = 0; //兑换校验
    private static final String CONVERT_KEY = "convertibleAmount_";

    @Autowired
    private AppKgRitConvertRMapper kgRitConvertRMapper;

    @Autowired
    private AccountRMapper accountRMapper;

    @Autowired
    private AccountWMapper accountWMapper;

    @Autowired
    private MQConfig convertFlowMqConfig;

    @Autowired
    private MQProduct mqProduct;

    @Autowired
    protected JedisUtils jedisUtils;


    /**
     * 1:用户当天是否已兑换完成
     * 2:若没有完成，返回兑换页面数据
     * 3:若完成，返回今日已兑换页面数据
     *
     * @param kguser
     * @return
     */
    @Override
    public AppJsonEntity kgToRitIndex(UserkgResponse kguser) {
        log.info("kgToRitIndex -> kguser:" + JSONObject.toJSONString(kguser));
        Long userId = Long.valueOf(kguser.getUserId());
        Integer userType = kguser.getUserRole() == 1 ? 0 : 1;
        KgRitConvert kgRitConvert = kgRitConvertRMapper.getSetByUserType(userType);
        log.info("kgToRitIndex 获取生效配置-> kgRitConvert：" + JSONObject.toJSONString(kgRitConvert));
        boolean isConvertCountOver = isConvertCountOver(userId, kgRitConvert, kguser.getUserRole());
        log.info("当前兑换次数是否已用完 : " + isConvertCountOver);
        if (isConvertCountOver) {
            return convertedResponse(userId);
        }
        return convertIndexResponse(userId, kgRitConvert, kguser.getUserRole());
    }

    @Transactional
    @Override
    public AppJsonEntity kgToRit(UserkgResponse kguser) throws Exception {

        Long userId = Long.valueOf(kguser.getUserId());
        Integer userType = kguser.getUserRole() == 1 ? 0 : 1;
        KgRitConvert kgRitConvert = kgRitConvertRMapper.getSetByUserType(userType);
        log.info("[kgToRit 兑换] -> 获取生效配置：" + JSONObject.toJSONString(kgRitConvert));
        AppJsonEntity checkEntity = checkConvert(kguser, kgRitConvert, CONVERT_NOW);
        if (!checkEntity.isSuccess()) {
            return checkEntity;
        }
        Lock lock = new Lock();
        String key = KG_TO_RIT + kguser.getUserId();
        try {
            lock.lock(key);
            Account account = accountRMapper.selectByUserId(userId);
            log.info("[kgToRit 兑换] -> 获取用户账户信息 account：" + JSONObject.toJSONString(account));
            if (account == null) {
                throw new ApiException("用户账户不存在 userId：" + userId);
            }
            BigDecimal kgBalance = account.getTxbBalance();
            BigDecimal ritAmount = calculateConvertAmount(kgBalance, kgRitConvert); //应增加RIT余额
            ritAmount = calculateConvertRitAmount(ritAmount, kgBalance, kgRitConvert, kguser.getUserRole());

            String shouldRitAmount = jedisUtils.get(CONVERT_KEY + userId);
            if (StringUtils.isNotEmpty(shouldRitAmount)) {
                if (!shouldRitAmount.equals(ritAmount.stripTrailingZeros().toPlainString())) {
                    log.info("[kgToRit 兑换] 应兑换数额：" + shouldRitAmount + "  实际账户可兑换数额：" + ritAmount.stripTrailingZeros().toPlainString());
                    return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.CONVERT_SYSTEM_ERROR);
                }
            }
            BigDecimal reduceKgBalance = ritAmount.multiply(rate(kgRitConvert)); //获取应扣除KG余额

            log.info("[kgToRit 兑换] userId: " + userId + "-> 用户剩余氪金余额：" + kgBalance.toString() + "  应增加RIT余额:" + ritAmount.toString() + "  应扣除KG余额:" + reduceKgBalance.toString());

            int success = upatekgToRitBalance(userId, ritAmount, reduceKgBalance);
            if (success <= 0) {
                throw new ApiException("[kgToRit 兑换] 失败");
            }
            int successSys = upatekgToRitSysBalance(ritAmount, reduceKgBalance);
            if (successSys <= 0) {
                throw new ApiException("[kgToRit 兑换] 失败");
            }

            sendMqMessage(kguser, ritAmount, reduceKgBalance, kgRitConvert.getRitRate());

        } finally {
            jedisUtils.del(CONVERT_KEY + userId);
            lock.unLock();
        }
        return AppJsonEntity.makeSuccessJsonEntity("兑换成功");
    }

    /**
     * 计算是否足够全部兑换，若否，返回实际兑换的RIT数额
     *
     * @param kgBalance
     * @param kgRitConvert
     * @return
     */
    private BigDecimal calculateConvertRitAmount(BigDecimal ritAmount, BigDecimal kgBalance, KgRitConvert kgRitConvert, Integer userRole) {
        BigDecimal residueRitAmountToday = getResidueRitAmount(userRole); //总兑换数
        BigDecimal dayLimitBigDecimal = new BigDecimal(kgRitConvert.getDayLimit()); //总额度
        BigDecimal convertibleAmount = dayLimitBigDecimal.subtract(residueRitAmountToday); //剩余额度
        log.info("[kgToRit 兑换] -> 总已兑换数:" + residueRitAmountToday + "  总额度：" + dayLimitBigDecimal + "  剩余额度：" + convertibleAmount);
        BigDecimal convertAmount = calculateConvertAmount(kgBalance, kgRitConvert); //可兑换数额
        log.info("[kgToRit 兑换] -> 氪金是否可以全部兑换 residueRitAmountToday(当天剩余RIT额度):" + convertibleAmount.toString() + "  convertAmount(可兑换RIT数额)：" + convertAmount);
        if (convertibleAmount.compareTo(convertAmount) < 0) {
            log.info("今日兑换总量还剩" + convertibleAmount.stripTrailingZeros().toPlainString() + "RIT,你今日最多只能兑换" + convertibleAmount.stripTrailingZeros().toPlainString() + "RIT,是否继续兑换");
            ritAmount = convertibleAmount;
        }
        return ritAmount;
    }

    /**
     * 更新系统账户余额
     *
     * @param ritAmount
     * @param reduceKgBalance
     * @return
     */
    private int upatekgToRitSysBalance(BigDecimal ritAmount, BigDecimal reduceKgBalance) {
        AccountInModel inModel = new AccountInModel();
        inModel.setRitBalance(ritAmount);
        inModel.setTxbBalance(reduceKgBalance);
        return accountWMapper.upatekgToRitSysBalance(inModel);
    }

    private void sendMqMessage(UserkgResponse kguser, BigDecimal ritAmount, BigDecimal reduceKgBalance, String rate) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("kguser", URLEncoder.encode(JSONObject.toJSONString(kguser), "utf-8"));
        map.put("ritAmount", ritAmount);
        map.put("reduceKgBalance", reduceKgBalance);
        map.put("rate", rate);
        mqProduct.sendMessage(convertFlowMqConfig.getTopic(), convertFlowMqConfig.getTags(), null, JSON.toJSONString(map));
    }

    @Override
    public AppJsonEntity kgToRitCheck(UserkgResponse kguser) {
        Integer userType = kguser.getUserRole() == 1 ? 0 : 1;
        KgRitConvert kgRitConvert = kgRitConvertRMapper.getSetByUserType(userType);
        log.info("[kgToRit 兑换] -> 获取生效配置：" + JSONObject.toJSONString(kgRitConvert));
        return checkConvert(kguser, kgRitConvert, CONVERT_CHECK);
    }

    private int upatekgToRitBalance(Long userId, BigDecimal ritAmount, BigDecimal reduceKgBalance) throws Exception {
        AccountInModel inModel = new AccountInModel();
        inModel.setUserId(userId);
        inModel.setRitBalance(ritAmount);
        inModel.setTxbBalance(reduceKgBalance);
        return accountWMapper.upatekgToRitBalance(inModel);
    }

    /**
     * 当前兑换次数是否已用完
     *
     * @param userId
     * @return
     */
    private boolean isConvertCountOver(Long userId, KgRitConvert kgRitConvert, Integer userRole) {
        long count = getConvertCountForToday(userId, userRole);//查询当天完成兑换数
        Integer convertCount = kgRitConvert.getDayCnt();//查询生效配置数
        log.info("[当前兑换次数是否已用完] count(当天完成兑换数):" + count + "  convertCount(生效配置数):" + convertCount);
        return count >= convertCount;
    }

    /**
     * 兑换条件校验
     *
     * @param kguser
     * @return
     */
    private AppJsonEntity checkConvert(UserkgResponse kguser, KgRitConvert kgRitConvert, int convertType) {

        Long userId = Long.valueOf(kguser.getUserId());
        //1:每日额度是否已抢完
        if (calculateConvertOver(kgRitConvert.getDayLimit(), kguser.getUserRole())) {
            log.info("[kgToRit 兑换] -> 今天兑换额度被抢完了，明天早点来抢吧！");
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.CONVERT_OVER);
        }

        //2:是否满足兑换时间
        Integer isConvertStart = getIsConvertStart(kgRitConvert);
        if (isConvertStart == 0) {
            log.info("[kgToRit 兑换] -> 当前不能兑换");
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.CONVERT_NOT_START);
        }

        //3:今日兑换次数是否已用完,返回次数用完页面数据
        if (isConvertCountOver(userId, kgRitConvert, kguser.getUserRole())) {
            log.info("[kgToRit 兑换] -> 今日兑换次数已用完");
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.CONVERT_COUNT_OVER);
        }

        //4:氪金余额是否足够兑换
        Account account = accountRMapper.selectByUserId(userId);
        String kgRate = kgRitConvert.getRitRate().split("-")[0];
        BigDecimal txbBalance = account.getTxbBalance();
        log.info("[kgToRit 兑换] -> 氪金余额是否足够兑换 用户氪金余额：" + txbBalance + "  氪金兑换率：" + kgRate);
        if (txbBalance.compareTo(new BigDecimal(kgRate)) < 0) {
            log.info("[kgToRit 兑换] -> 你还没有足够的KG兑换哦,快去赚KG吧");
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.CONVERT_KG_NOT_ENOUGH);
        }
        //如果是立即兑换则不校验是否可以全部兑换条件
        if (convertType == CONVERT_NOW) {
            return AppJsonEntity.makeSuccessJsonEntity("氪金余额将兑换为RIT，兑换数量以实际兑换结果为准，是否确认兑换？");
        }
        //5:氪金是否可以全部兑换
        BigDecimal residueRitAmountToday = getResidueRitAmount(kguser.getUserRole()); //总兑换数
        BigDecimal dayLimitBigDecimal = new BigDecimal(kgRitConvert.getDayLimit()); //总额度
        BigDecimal convertibleAmount = dayLimitBigDecimal.subtract(residueRitAmountToday); //剩余额度
        log.info("[kgToRit 兑换] -> 总已兑换数:" + residueRitAmountToday + "  总额度：" + dayLimitBigDecimal + "  剩余额度：" + convertibleAmount);

        BigDecimal convertAmount = calculateConvertAmount(txbBalance, kgRitConvert); //可兑换数额
        log.info("[kgToRit 兑换] -> 氪金是否可以全部兑换 residueRitAmountToday(当天剩余RIT额度):" + convertibleAmount.toString() + "  convertAmount(可兑换RIT数额)：" + convertAmount);
        if (convertibleAmount.compareTo(convertAmount) < 0) {
            log.info("今日兑换总量还剩" + convertibleAmount.stripTrailingZeros().toPlainString() + "RIT,你今日最多只能兑换" + convertibleAmount.stripTrailingZeros().toPlainString() + "RIT,是否继续兑换");
            jedisUtils.set(CONVERT_KEY + userId, convertibleAmount.stripTrailingZeros().toPlainString(), 600);
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.CONVERT_ALL_NOT_ENOUGH.getCode(), "今日兑换总量还剩" + convertibleAmount.stripTrailingZeros().toPlainString() + "RIT,你今日最多只能兑换" + convertibleAmount.stripTrailingZeros().toPlainString() + "RIT,是否继续兑换");
        }
        log.info("氪金余额将兑换为RIT，兑换数量以实际兑换结果为准，是否确认兑换？");
        jedisUtils.set(CONVERT_KEY + userId, convertAmount, 600);
        return AppJsonEntity.makeSuccessJsonEntity("氪金余额将兑换为RIT，兑换数量以实际兑换结果为准，是否确认兑换？");
    }

    /**
     * 计算KG可兑换RIT数额
     *
     * @param txbBalance
     * @param kgRitConvert
     * @return
     */
    private BigDecimal calculateConvertAmount(BigDecimal txbBalance, KgRitConvert kgRitConvert) {
        return txbBalance.divide(rate(kgRitConvert), 0, BigDecimal.ROUND_DOWN);
    }

    /**
     * 获取兑换比列
     *
     * @param kgRitConvert
     * @return
     */
    private BigDecimal rate(KgRitConvert kgRitConvert) {
        BigDecimal kgRate = new BigDecimal(kgRitConvert.getRitRate().split("-")[0]);
        BigDecimal ritRate = new BigDecimal(kgRitConvert.getRitRate().split("-")[1]);
        return kgRate.divide(ritRate, 8, BigDecimal.ROUND_DOWN);
    }

    /**
     * 兑换页面数据
     *
     * @param userId
     * @return
     */
    private AppJsonEntity convertIndexResponse(Long userId, KgRitConvert kgRitConvert, Integer userRole) {
        Account account = accountRMapper.selectByUserId(userId);
        log.info("[兑换页面] -> 用户账户信息 account：" + JSONObject.toJSONString(account));
        ConvertIndexResponse convertIndexResponse = buildConvertIndexResponse(account, kgRitConvert, userRole);
        RitConvertResponse response = new RitConvertResponse();
        response.setIsComplete(0);
        response.setConvertIndex(convertIndexResponse);
        return AppJsonEntity.makeSuccessJsonEntity(response);
    }

    /**
     * 兑换页面数据组装
     *
     * @param account
     * @param kgRitConvert
     * @return
     */
    private ConvertIndexResponse buildConvertIndexResponse(Account account, KgRitConvert kgRitConvert, Integer userRole) {
        BigDecimal kgBalance = account.getTxbBalance();
        String kgToRitRate = kgRitConvert.getRitRate(); //汇率
        String[] kgToRitRateArry = kgToRitRate.split("-");
        ConvertIndexResponse convertIndexResponse = new ConvertIndexResponse();
        convertIndexResponse
                .buildConvertRit(kgBalance)
                .buildRate(kgToRitRateArry)
                .buildConvertCountSet(kgRitConvert);
        convertIndexResponse = calculateConvertOver(kgRitConvert, convertIndexResponse, userRole);

        Integer isConvertStart = getIsConvertStart(kgRitConvert);
        String pushTime = kgRitConvert.getPushTime();
        String startTime = pushTime.split(" ")[1];
        convertIndexResponse.setIsConvertStart(isConvertStart);
        convertIndexResponse.setStartTime(startTime);
        log.info("[兑换页面] -> 生效配置信息：" + JSONObject.toJSONString(kgRitConvert) + "  返回数据：" + JSONObject.toJSONString(convertIndexResponse));
        return convertIndexResponse;
    }

    /**
     * 计算今日额度是否已抢完
     *
     * @param kgRitConvert
     * @return
     */
    private ConvertIndexResponse calculateConvertOver(KgRitConvert kgRitConvert, ConvertIndexResponse convertIndexResponse, Integer userRole) {
        convertIndexResponse.setIsConvertOver(0);
        long dayLimit = kgRitConvert.getDayLimit();
        boolean isConvertOver = calculateConvertOver(dayLimit, userRole);
        if (isConvertOver) {
            convertIndexResponse.setIsConvertOver(1);
        }
        return convertIndexResponse;
    }

    /**
     * 计算今日额度是否已抢完
     *
     * @param dayLimit
     * @return
     */
    private boolean calculateConvertOver(long dayLimit, Integer userRole) {
        BigDecimal convertRitAmountToday = getResidueRitAmount(userRole);
        BigDecimal dayLimitBigDecimal = new BigDecimal(dayLimit);
        return convertRitAmountToday.compareTo(dayLimitBigDecimal) >= 0;
    }

    /**
     * 获取当天已兑换RIT数量
     *
     * @return
     */
    private BigDecimal getResidueRitAmount(Integer userRole) {
        MongoCursor<Document> cursor = MongoUtils.aggregate(MongoTables.KG_RIT_EXCHANGE_FLOW, getDBObjectGroupForCalculateConvertOver(userRole));
        Decimal128 totalRitAmount = new Decimal128(new BigDecimal("0"));
        if (cursor != null) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                totalRitAmount = (Decimal128) document.get("totalRitAmount");
            }
        }
        return totalRitAmount.bigDecimalValue();
    }

    /**
     * 判断兑换是否已开启
     *
     * @param kgRitConvert
     * @return
     */
    private Integer getIsConvertStart(KgRitConvert kgRitConvert) {
        String pushTime = kgRitConvert.getPushTime();
        String hms = pushTime.split(" ")[1];
        String now = DateUtils.formatDate(new Date(), DateUtils.DATE_FORMAT_YYYY_MM_DD);
        long nowTime = new Date().getTime();
        long startTime = getStartDate(now, hms).getTime();
        return nowTime >= startTime ? 1 : 0;
    }

    private Date getStartDate(String now, String hms) {
        return DateUtils.parseDate(now + " " + hms, DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 今日兑换已完成返回
     *
     * @return
     */
    private AppJsonEntity convertedResponse(Long userId) {
        Account account = accountRMapper.selectByUserId(userId);
        log.info("[今日兑换已完成] 用户账户信息：" + JSONObject.toJSONString(account));
        BigDecimal kgBalance = account.getTxbBalance();
        BigDecimal convertRitToday = getConvertRitToday(userId);
        log.info("[当前用户当天已兑换总数] convertRitToday:" + convertRitToday);

        ConvertCompleteResponse completeResponse = new ConvertCompleteResponse();
        completeResponse.setKgBalance(kgBalance.toString().equals("0E-8") ? "0" : kgBalance.stripTrailingZeros().toPlainString());
        completeResponse.setConvertRitToday(convertRitToday.stripTrailingZeros().toPlainString());

        RitConvertResponse response = new RitConvertResponse();
        response.setIsComplete(1);
        response.setConvertComplete(completeResponse);
        log.info("[今日兑换已完成] response:" + JSONObject.toJSONString(response));
        return AppJsonEntity.makeSuccessJsonEntity(response);
    }

    /**
     * 当前用户当天已兑换总数
     *
     * @param userId
     * @return
     */
    private BigDecimal getConvertRitToday(Long userId) {

        MongoCursor<Document> cursor = MongoUtils.aggregate(MongoTables.KG_RIT_EXCHANGE_FLOW, getDBObjectGroup(userId));
        Decimal128 totalRitAmount = new Decimal128(new BigDecimal("0"));
        if (cursor != null) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                totalRitAmount = (Decimal128) document.get("totalRitAmount");
            }
        }
        return totalRitAmount.bigDecimalValue();
    }

    private long getConvertCountForToday(Long userId, Integer userRole) {
        return MongoUtils.count(MongoTables.KG_RIT_EXCHANGE_FLOW, buildQueryParam(userId, userRole));
    }

    private BasicDBObject buildQueryParam(Long userId, Integer userRole) {
        BasicDBObject basicDBObject = new BasicDBObject("userId", userId);
        Date now = new Date();
        Date start = DateUtils.getDateStart(now);
        Date end = DateUtils.getDateEnd(now);
        if (userRole == 1) {
            basicDBObject.append("userRole", userRole);
        } else {
            basicDBObject.append("userRole", new BasicDBObject(Seach.NE.getOperStr(), 1));
        }
        basicDBObject.append("createTime", new BasicDBObject(Seach.GTE.getOperStr(), start.getTime()).append(Seach.LTE.getOperStr(), end.getTime()));
        return basicDBObject;
    }

    private BasicDBObject buildQueryParam(Long userId) {
        BasicDBObject basicDBObject = new BasicDBObject("userId", userId);
        Date now = new Date();
        Date start = DateUtils.getDateStart(now);
        Date end = DateUtils.getDateEnd(now);
        basicDBObject.append("createTime", new BasicDBObject(Seach.GTE.getOperStr(), start.getTime()).append(Seach.LTE.getOperStr(), end.getTime()));
        return basicDBObject;
    }

    private List<Bson> getDBObjectGroup(Long userId) {
        List<Bson> ops = new ArrayList<>();
        DBObject query = buildQueryParam(userId);
        Bson match = new BasicDBObject(Seach.MATCH.getOperStr(), query);
        ops.add(match);
        Bson group = buildBsonGroup();
        ops.add(group);
        return ops;
    }

    private Bson buildBsonGroup() {
        return new BasicDBObject(Seach.GROUP.getOperStr(),
                new BasicDBObject("_id", null)
                        .append("totalRitAmount", new BasicDBObject(Seach.SUM.getOperStr(), "$ritAmount")));
    }


    private List<Bson> getDBObjectGroupForCalculateConvertOver(Integer userRole) {
        List<Bson> ops = new ArrayList<>();
        DBObject query = buildQueryParamForCalculateConvertOver(userRole);
        Bson match = new BasicDBObject(Seach.MATCH.getOperStr(), query);
        ops.add(match);
        Bson group = buildBsonGroup();
        ops.add(group);
        return ops;
    }

    private DBObject buildQueryParamForCalculateConvertOver(Integer userRole) {
        BasicDBObject basicDBObject = new BasicDBObject();
        Date now = new Date();
        Date start = DateUtils.getDateStart(now);
        Date end = DateUtils.getDateEnd(now);
        if (userRole == 1) {
            basicDBObject.append("userRole", userRole);
        } else {
            basicDBObject.append("userRole", new BasicDBObject(Seach.NE.getOperStr(), 1));
        }
        basicDBObject.append("createTime", new BasicDBObject(Seach.GTE.getOperStr(), start.getTime()).append(Seach.LTE.getOperStr(), end.getTime()));
        return basicDBObject;
    }
}
