package com.kg.platform.dao.read;

import java.math.BigDecimal;
import java.util.List;

import com.kg.platform.dao.entity.AccountFlow;
import com.kg.platform.model.in.AccountFlowInModel;
import com.kg.platform.model.in.DiscipleInModel;
import com.kg.platform.model.out.AccountFlowOutModel;
import com.kg.platform.model.response.DiscipleInfoResponse;
import com.kg.platform.model.response.DiscipleRemindRespose;
import org.apache.ibatis.annotations.Param;

public interface AccountFlowRMapper {

    AccountFlow selectByPrimaryKey(Long accountFlowId);

    List<AccountFlowOutModel> selectByUserflow(AccountFlowInModel inModel);

    List<AccountFlowOutModel> selectByBusinessTypeId(AccountFlowInModel inModel);

    int selectByUserCount(AccountFlowInModel inModel);

    // 累积所得
    AccountFlowOutModel cumulativeIncome(AccountFlowInModel inModel);

    // 今日所得，今日消耗
    AccountFlowOutModel Today(AccountFlowInModel inModel);

    // 昨日所得
    AccountFlowOutModel BeforeIncome(AccountFlowInModel inModel);

    AccountFlowOutModel cumulativeIncomeTxb(AccountFlowInModel inModel);

    AccountFlowOutModel TodayTxb(AccountFlowInModel inModel);

    AccountFlowOutModel BeforeIncomeTxb(AccountFlowInModel inModel);

    // 审核是否有提现
    AccountFlowOutModel auditAmount(AccountFlowInModel inModel);

    // 提现限制数量
    AccountFlowOutModel getMaxMinTimes(AccountFlowInModel inModel);

    // 统计文章被打赏数
    int getTipsinCount(AccountFlowInModel inModel);

    // 统计师傅打赏当天打赏次数
    int getRewardTips(AccountFlowInModel inModel);

    // 统计用户当天打赏次数
    int getTipsout(AccountFlowInModel inModel);

    AccountFlowOutModel selectUserAccount(AccountFlowInModel inModel);

    // 统计阅读奖励领取各种类型数量
    int getBusinessTypeCount(AccountFlowInModel inModel);

    BigDecimal getBusinessTypeValue(AccountFlowInModel inModel);

    List<AccountFlowOutModel> selectUserTxbflow(AccountFlowInModel inModel);

    int selectUserTxbCount(AccountFlowInModel inModel);

    List<AccountFlowOutModel> selectUserTypeFlow(AccountFlowInModel inModel);

    /**
     * 统计我的师傅详情
     */
    AccountFlowOutModel getMasterInfo(Long userId);

    /**
     * 统计我的徒弟今日/历史进贡量
     */
    List<DiscipleInfoResponse> getDiscipleInfos(DiscipleInModel discipleInModel);

    /**
     * 统计我的徒弟今日/历史进贡量
     */
    Long getDiscipleInfosCount(DiscipleInModel discipleInModel);

    /**
     * 获取的我的徒弟进贡提醒
     */
    DiscipleRemindRespose getContriRemindCount(DiscipleInModel discipleInModel);

    /**
     * 查询分享奖励发放次数
     */
    int getShareBonusCount(AccountFlowInModel inModel);

    /**
     * 查询分享奖励领取状态
     */
    int getShareBonusStatusCount(AccountFlowInModel inModel);

    //app钛值交易流水
    List<AccountFlowOutModel> selectByUserflowForApp(AccountFlowInModel inModel);

    //app钛值交易流水总数
    int selectByUserflowCountForApp(AccountFlowInModel inModel);

    //app钛小白交易流水
    List<AccountFlowOutModel> selectUserTxbflowForApp(AccountFlowInModel inModel);

    //app钛小白交易流水总数
    int selectUserTxbflowCountForApp(AccountFlowInModel inModel);

    //app钛值详情
    AccountFlowOutModel getTzDetailBill(AccountFlowInModel inModel);

    //app钛小白详情
    AccountFlowOutModel getTxbDetailBill(AccountFlowInModel inModel);

    /**
     * 获取资讯详情的奖励收入(发文奖励+额外奖励+打赏)
     *
     * @param userId
     * @return
     */
    String getAmountByUserId(@Param("userId") Long userId, @Param("articleId") Long articleId);

    /**
     * app钛值交易流水125
     *
     * @param inModel
     * @return
     */
    List<AccountFlowOutModel> selectByUserflowForApp125(AccountFlowInModel inModel);

    /**
     * app钛值交易流水总数125
     *
     * @param inModel
     * @return
     */
    int selectByUserflowCountForApp125(AccountFlowInModel inModel);

    List<AccountFlowOutModel> selectUserTxbflowForApp125(AccountFlowInModel inModel);

    int selectUserTxbflowCountForApp125(AccountFlowInModel inModel);
}
