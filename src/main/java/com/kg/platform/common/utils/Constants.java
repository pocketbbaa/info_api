package com.kg.platform.common.utils;

import java.math.BigDecimal;

public final class Constants {

    public static final String SALT = "KG.combtc123";

    public static final Long ADMIN_USER_ID = -999L;

    public static final BigDecimal POUNDAGE_RATE = BigDecimal.valueOf(0.002d);

    // 发放奖励的平台账号
    public static final Long PLATFORM_USER_ID = -1L;

    // 尚币哥的账号
    public static final Long BIG_BOSS_ID = 411541162382467072L;

    // 首次登录奖励 1000 氪金
    public static final BigDecimal LOGIN_BONUS_TXB = BigDecimal.valueOf(1000);

    // 首次实名认证奖励 0.1 TV
    public static final BigDecimal REALNAME_BONUS_TV = BigDecimal.valueOf(0.1);

    // 专利申请通过奖励 0.1 TV
    public static final BigDecimal USERCOLUMN_BONUS_TV = BigDecimal.valueOf(0.1);

    // 最小单位 0.001 TV
    public static final BigDecimal MIN_TV_VALUE = BigDecimal.valueOf(0.001);

    public static final Integer ZERO = 0;

    // 最大投票数
    public static final Long MAXVOTENUM = 10000L;

    // 文章额外奖励钛值最大数
    public static final BigDecimal MAXTVBOUNS = BigDecimal.valueOf(50);

    // 文章额外奖励氪金最大数
    public static final BigDecimal MAXTXBBOUNS = BigDecimal.valueOf(500);

    // 分享奖励钛值0.5TV
    public static final BigDecimal SHARETVBONUS = BigDecimal.valueOf(0.5);

    // 分享奖励氪金20KG
    public static final BigDecimal SHAREKGBONUS = BigDecimal.valueOf(25);

    // 分享奖励师傅钛值0.1TV
    public static final BigDecimal SHAREMASTERTVBONUS = BigDecimal.valueOf(0.1);

    // 分享奖励师傅氪金10KG
    public static final BigDecimal SHAREMASTERKGBONUS = BigDecimal.valueOf(10);

    // 阅读奖励氪金10KG
    public static final BigDecimal READKGBONUS = BigDecimal.valueOf(10);

    // 阅读奖励师傅氪金5KG
    public static final BigDecimal READMASTERKGBONUS = BigDecimal.valueOf( 5);

    public static final Integer ONE = 1;

    public static final Integer FIRST = 2;

    // 活动开始时间
    public static final String ACTVITY_START = "2018-4-25 13:14:00";

    // 活动结束时间
    public static final String ACTVITY_END = "2018-4-29 20:00:00";

    // 二等奖中奖次数
    public static final Integer SECONDPRIZE = 30;

    // 三等奖中奖次数
    public static final Integer THIRDPRIZE = 150;

    // 四等奖中奖次数
    public static final Integer FORTHPRIZE = 10000000;

    // 满足10人则可以抽奖
    public static final Integer INVITE_COUNT = 10;

    // 满足阅读奖励阅读次数条件
    public static final Integer PALTFORMREADCOUNT = 20;

    // 满足分享奖励分享次数条件
    public static final Integer PALTFORMSHARECOUNT = 6;

    // 发文奖励设备最大浏览奖励数
    public static final Integer PALTFORMMAXBROWERCOUNT = 10;

    // 发文奖励 设备最大分享奖励数
    public static final Integer PALTFORMMAXSHARECOUNT = 10;

    // 发文奖励基础tv奖励
    public static final BigDecimal PUBLISG_BONUS_TV = BigDecimal.valueOf(5);

    // 发文奖励进贡师傅
    public static final BigDecimal PUBLISG_MASTER_KG = BigDecimal.valueOf(50);

    // 发文奖励设备浏览奖励0.1tv
    public static final BigDecimal PUBLISG_BONUS_BROWER__TV = BigDecimal.valueOf(0.1);

    // 发文奖励设备分享奖励1tv
    public static final BigDecimal PUBLISG_BONUS_SHARE_TV = BigDecimal.valueOf(1);

    // 发放用户奖励最大手机号数
    public static final Integer MAXUSERBONUSMOBILES = 100;

    // 参加矿机活动氪金价格
    public static final BigDecimal ACTIVITY_MINER_PRICE = BigDecimal.valueOf(1000);

    // 参加矿机立减蓄力值百分比
    public static final BigDecimal ACTIVITY_MINER_ASSIST_RATE = BigDecimal.valueOf(0.3);

    // 矿机活动新用户助力次数
    public static final Integer MINER_ASSIST_NEWUSER_COUNT = 3;

    // 矿机活动老用户助力次数
    public static final Integer MINER_ASSIST_OLDUSER_COUNT = 1;

    // 助力抢单最大数
    public static final Integer MAX_ASSIST_TIMES = 3;

    //rit兑换率
    public static final String RIT_RATE ="150-1";

    //冻结
    public static final String FREEZE = "freeze";

    //解冻
    public static final String RELVE = "releve";

    public static final String CONCERNBASEINFO = "关注了你";

    public static final Integer FRONTDAY = -7;
}
