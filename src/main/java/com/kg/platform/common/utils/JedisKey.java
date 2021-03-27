package com.kg.platform.common.utils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * jedis Key jedis cache 所有的key在此定义
 *
 * @author zhaiwenbo
 */
public final class JedisKey {

    static final Logger logger = LoggerFactory.getLogger(JedisKey.class);

    public static final int ONE_SECONDS = 1;

    public static final int ONE_MINUTE = ONE_SECONDS * 60;

    public static final int TEN_MINUTE = ONE_MINUTE * 10;

    public static final int THREE_MINUTE = ONE_MINUTE * 3;

    public static final int HALF_HOUR = TEN_MINUTE * 3;

    // 临时屏蔽缓存

    public static final int ONE_HOUR = ONE_MINUTE * 60;

    public static final int TWO_HOURS = ONE_HOUR * 2;

    public static final int EIGHT_HOUR = ONE_HOUR * 8;

    public static final int ONE_DAY = ONE_HOUR * 24;

    public static final int ONE_WEEK = ONE_DAY * 7;

    public static final int ONE_MONTH = ONE_DAY * 30;

    private static final String PREFIX = "api";

    private static final String ARTICLEPAGEPREFIX = "articlePage";

    private static final String USERCURRENTPAGE = "userCurrentPage";

    public static final String CACHE_PREFIX = ":";

    private JedisKey() {

    }

    public static String brownKey(String userId, String articleId) {
        return buildKey("brownKey", userId, articleId);
    }

    public static String ipAddress(String userIp) {
        return buildKey("ipAddress", userIp);
    }

    public static String registerIp(String userIp) {
        return buildKey("registerIp", userIp);
    }

    public static String limitIp(String limitIp) {
        return buildKey("limitIp", limitIp);
    }

    public static String loginIp(String loginIp) {
        return buildKey("loginIp", loginIp);
    }

    public static String txPassIp(String txPassIp) {
        return buildKey("txPassIp", txPassIp);
    }

    public static String limitTxPassIp(String limitIp) {
        return buildKey("limitTxPassIp", limitIp);
    }

    public static String vatcodeKey(String code) {
        return buildKey("vatCode", code);
    }

    public static String wxInfoKey(String code) {
        return buildKey("wxInfo", code);
    }

    public static String wbInfoKey(String code) {
        return buildKey("wbInfo", code);
    }

    public static String longuseridKey() {
        return buildKey("userId");
    }

    public static String LonguserRoleKey() {
        return buildKey("userRole");
    }

    public static String siteInfoKey() {
        return buildKey("siteInfoKey");
    }

    public static String sensitiveWordsKey() {
        return buildKey("sensitiveWordsKey");
    }

    public static String sensitiveFilterKey() {
        return buildKey("sensitiveFilterKey");
    }

    public static String userTokenKey(Long userId) {
        return buildKey("userTokenKey", userId);
    }

    public static String getAddCustomerKey(Long customerId, Long supplierId) {
        return buildKey("getAddCustomerKey", customerId, supplierId);
    }

    public static String getLastRemindDate(Long userId) {
        return buildKey("getLastRemindDate", userId);
    }

    public static String getSkuBySpuIdKey(Long spuId) {
        return buildKey("getSkuBySpuIdKey", spuId);
    }

    public static String getSpuKey(Long spuId) {
        return buildKey("spuId", spuId);
    }

    public static String getSpuAppendKey(Long spuId) {
        return buildKey("spu_append_spuId", spuId);
    }

    public static String getSkuKey(Long skuId) {
        return buildKey("sku", skuId);
    }

    public static String getStrategySpuKey(Long spuId) {
        return buildKey("strategy_spu", spuId);
    }

    public static String getSiteKey(Long siteId) {
        return buildKey("site", siteId);
    }

    public static String getScenicKey(Long scenicId) {
        return buildKey("scenic", scenicId);
    }

    public static String getScenicSupplierKey(Long supplierId) {
        return buildKey("scenic_supplier", supplierId);
    }

    public static String getProductType() {
        return buildKey("product_type");
    }

    public static String getDeviceSiteByCode(String code) {
        return buildKey("device_code", code);
    }

    public static String getProductDict() {
        return buildKey("dict");
    }

    public static String getPushArticleLimit() {
        return buildKey("push_article_limit");
    }

    public static String getPushNewsFlashLimit() {
        return buildKey("push_newsFlash_limit");
    }

    public static String getAuthorizationKey(String code) {
        return buildKey("authorizationCode", code);
    }

	public static String coinAvaData(String coinType) {
		return buildKey("coin_ava_data", coinType);
	}

    public static String getPriceKey(String code) {

        return buildKey("index_price", code);
    }

    public static String getIndexNewsFlashKey() {

        return buildKey("index_newsFlash");
    }

    public static String getArticleWithPage(String column, String search, Long total, Integer currentPage) {
        search = StringUtils.isBlank(search) ? "" : search;
        return buildArticlePageKey("columnId" + column, search + "C" + currentPage.toString() + "T" + total.toString());
    }

    public static String getUserCurrentPage(String code) {
        return buildUserCurrentPageKey(code);
    }

    public static String getAppConfigInterface() {
        return buildKey("/AppConfigServiceImpl/getAppConfig");
    }

    public static String getCountArticleNum(String columnId) {
        return buildKey("countArticleNum_" + columnId);
    }


    /**
     * 邮箱手机验证码
     *
     * @param code
     * @return
     */
    public static String getVerificationCode(String code) {

        return buildKey("vatCode", code);
    }

    public static String getSitemapXmlCode(String code) {

        return buildKey("sitemapxml", "sitemap" + code);
    }

    /**
     * 激活码
     *
     * @param code
     * @return
     */
    public static String getProductSiteDevice(String code) {
        return buildKey("site_device", code);
    }

    /**
     * 关闭事件key
     *
     * @param spuId
     * @return
     */
    public static String getProductClose(Long spuId) {
        return buildKey("product_close", spuId);
    }

    /**
     * 填单项配置
     *
     * @return
     */
    public static String getProductFilled() {
        return buildKey("filled");
    }

    /**
     * 填单项配置
     *
     * @return
     */
    public static String getProductRule() {
        return buildKey("rule");
    }

    /**
     * 产品填单项配置
     *
     * @return
     */
    public static String getSpuFilledRelation(Long spuId) {
        return buildKey("filled_relation_spu", spuId);
    }

    /**
     * 产品规则
     *
     * @param spuId
     * @return
     */
    public static String getSpuRuleRelation(Long spuId) {
        return buildKey("rule_relation_spu", spuId);
    }

    /**
     * 产品规则
     *
     * @return
     */
    public static String getStrategyRule() {
        return buildKey("strategy_rule");
    }

    /**
     * 产品提交线程
     *
     * @param exCode
     * @return
     */
    public static String getProductCommitThread(String exCode) {
        return buildKey("CommonproductCommit", exCode);
    }

    /**
     * 政策提交线程
     *
     * @param code
     * @return
     */
    public static String getStrategyCommitThread(String code) {
        return buildKey("StrategyCommit", code);
    }

    public static String getNewsKey(String code) {
        return buildKey("outter_news", code);
    }

    public static String getNewsFlashKey(String code) {
        return buildKey("news_flash", code);
    }

    public static String getTestKey(Object key) {
        return buildKey(key);
    }

    private static String buildKey(Object str1) {
        StringBuffer stringBuffer = new StringBuffer(PREFIX);
        stringBuffer.append(CACHE_PREFIX).append(str1);
        return stringBuffer.toString();
    }

    /**
     * 邀新提现申请
     *
     * @return
     */
    public static String getInviteWithdraw(Long userId) {
        return buildKey("invite_withdraw", userId);
    }

    /**
     * 矿机活动
     *
     * @return
     */
    public static String getRushToMiner(String userId) {
        return buildKey("rushToMiner", userId);
    }

    /**
     * 矿机助力重复校验
     *
     * @param code
     * @return
     */
    public static String getMinerAssit(String code) {
        return buildKey("minerAssit", code);
    }

    /**
     * 世界杯活动
     *
     * @return
     */
    public static String getWorldCup(String userId) {
        return buildKey("worldCup", userId);
    }

    /**
     * 后台发放奖励重复提交验证
     *
     * @return
     */
    public static String getUserBonus() {
        return buildKey("send_user_bonus");
    }

    /**
     * 上次拉取粉丝列表时
     *
     * @return
     */
    public static String getFansTime(String userId) {
        return buildKey("getFansTime", userId);
    }

    /**
     * @param str1
     * @param array
     * @return
     */
    private static String buildKey(Object str1, Object... array) {
        StringBuffer stringBuffer = new StringBuffer(PREFIX);
        stringBuffer.append(CACHE_PREFIX).append(str1);
        for (Object obj : array) {
            stringBuffer.append(CACHE_PREFIX).append(obj);
        }
        return stringBuffer.toString();
    }

    private static String buildArticlePageKey(String column, Object str1, Object... array) {
        StringBuffer stringBuffer = new StringBuffer(ARTICLEPAGEPREFIX + "_" + column);
        stringBuffer.append(CACHE_PREFIX).append(str1);
        for (Object obj : array) {
            stringBuffer.append(CACHE_PREFIX).append(obj);
        }
        return stringBuffer.toString();
    }

    private static String buildUserCurrentPageKey(Object str1, Object... array) {
        StringBuffer stringBuffer = new StringBuffer(USERCURRENTPAGE);
        stringBuffer.append(CACHE_PREFIX).append(str1);
        for (Object obj : array) {
            stringBuffer.append(CACHE_PREFIX).append(obj);
        }
        return stringBuffer.toString();
    }

    public static String getArticlePageKeyPattern(String columnId) {
        return ARTICLEPAGEPREFIX + "_columnId" + columnId + "*";
    }

    public static void main(String[] args) {
        System.out.println(brownKey("123123", "333333"));

    }

}
