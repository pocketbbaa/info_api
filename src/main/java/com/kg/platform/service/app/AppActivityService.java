package com.kg.platform.service.app;

import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.model.in.KgMinerAssistInModel;
import com.kg.platform.model.out.KgMinerAssistAmountOutModel;
import com.kg.platform.model.request.AccountRequest;
import com.kg.platform.model.request.DeepfinRequest;
import com.kg.platform.model.request.KgActivityCompetionRequest;
import com.kg.platform.model.request.KgActivityGuessRequest;
import com.kg.platform.model.request.KgActivityMinerRequest;
import com.kg.platform.model.request.KgMinerAssistRequest;
import com.kg.platform.model.request.KgMinerRobRequest;
import com.kg.platform.model.response.UserkgResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2018/5/18.
 */
public interface AppActivityService {
    /**
     * 获取矿机列表信息
     *
     * @return
     */
    AppJsonEntity minerList();

    /**
     * 抢购矿机
     *
     * @return
     */
    AppJsonEntity rushToMiner(UserkgResponse kguser, KgActivityMinerRequest request);

    /**
     * 
     * 矿机 扣除用户余额 增加平台余额
     *
     * 
     * @return
     */
    Boolean reduceUserAccountToPlatform(AccountRequest request);

    /**
     * 生成助理码
     *
     * @return
     */
    String getCode();

    /**
     * 生成助理码
     *
     * @return
     */
    Boolean checkCode(String code);

    /**
     * 我的抢单列表
     *
     * @return
     */
    AppJsonEntity myRobList(UserkgResponse kguser);

    /**
     * 好友助力
     * 
     * @return
     */
    AppJsonEntity friendHelp(UserkgResponse kguser, KgActivityMinerRequest request);

    /**
     * 查看抢单详情
     * 
     * @return
     */
    AppJsonEntity detailRob(KgMinerRobRequest request);

    /**
     * 抢单的助力榜列表
     * 
     * @param request
     * @return
     */
    AppJsonEntity assistList(KgMinerAssistRequest request);

    /**
     * 矿机活动开始和结束时间（毫秒时间戳）
     * 
     * @return
     */
    AppJsonEntity minerTime();

    /**
     * 
     * 获取蓄力值
     * 
     * 1.随机蓄力值a为：1<=a(蓄力值)<A(剩余平均值)，a精确到整数位，四舍五入
     * 2.设蓄力总人数为X，蓄力池总值为Y（注：公式中人数均不包括抢单人自己） A(剩余平均值)=[（Y-好友已蓄力总值）/（X-以蓄力总人数）]*2
     * 3.蓄力池总值Y=（2/3)*矿机价格(元)，精确到整数位，四舍五入 4.剩最后一人蓄力时，无需计算，蓄力值即为剩余量。
     * 举例：一台熊猫矿机价值16800元，需1700位好友助力，则Y=（2/3)*16800=11200
     * 当前已有100个好友蓄力了2000蓄力值，则随机蓄力值a的区间为1<=a<12
     */
    KgMinerAssistAmountOutModel getAssistAmount(KgMinerAssistInModel kgMinerAssistInModel);

    /**
     * 对应矿机的进度列表，TOP10*
     * 
     * @param request
     * @return
     */
    AppJsonEntity minerProgressList(KgMinerAssistRequest request);

    /**
     * APP弹窗信息
     * 
     * @return
     */
    AppJsonEntity activityPopInfo(HttpServletRequest request);

    /**
     * APP弹窗信息
     * 
     * @return
     */
    AppJsonEntity validRushMiner(UserkgResponse kguser, KgActivityMinerRequest request);

    /**
     * 世界杯活动时间
     *
     * @return
     */
    AppJsonEntity worldCupTime();

    /**
     * 验证当天是都有赛事
     *
     * @return
     */
    AppJsonEntity checkMatch(KgActivityCompetionRequest request);

    /**
     * 竞猜球队
     *
     * @return
     */
    AppJsonEntity guessWinner(KgActivityGuessRequest request);

    /**
     * 助力详情
     *
     * @return
     */
    AppJsonEntity assistDetail(KgActivityCompetionRequest request);

    /**
     * 当天世界杯比赛赛事列表
     * 
     * @return
     */
    AppJsonEntity worldCupCompetionList(KgActivityCompetionRequest request);

    /**
     * 请求deepfin自动注册接口
     * 
     * @return
     */
    AppJsonEntity deepfinRequest(DeepfinRequest request);

}
