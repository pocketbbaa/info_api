package com.kg.platform.service.app;

import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.dao.entity.SysUser;
import com.kg.platform.enumeration.BusinessTypeEnum;
import com.kg.platform.enumeration.CoinEnum;
import com.kg.platform.enumeration.FlowStatusEnum;
import com.kg.platform.enumeration.OperTypeEnum;
import com.kg.platform.model.mongoTable.AccountWithdrawFlowRit;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.AccountFlowAppRequest;
import com.kg.platform.model.request.AccountWithDrawRequest;
import com.kg.platform.model.response.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 账户相关
 * <p>
 * by wangyang
 */
public interface AppAccountService {

    /**
     * 获取用户基本信息
     *
     * @param request
     * @return
     */
    UserBaseInfoResponse getUserBaseInfoById(UserkgResponse request,HttpServletRequest servletRequest);

    /**
     * 钛值交易流水
     *
     * @return
     */
    PageModel<AccountFlowAppResponse> selectUserTzflow(AccountFlowAppRequest request, PageModel<AccountFlowAppResponse> page);


    /**
     * 钛小白交易流水
     *
     * @param request
     * @param page
     * @return
     */
    PageModel<AccountFlowAppResponse> selectUserTxbflow(AccountFlowAppRequest request, PageModel<AccountFlowAppResponse> page);

    /**
     * RIT交易流水
     *
     * @param request
     * @param page
     * @return
     */
    PageModel<AccountFlowAppResponse> selectUserRitflow(AccountFlowAppRequest request, PageModel<AccountFlowAppResponse> page);



    /**
     * 钛值交易流水
     *
     * @return
     */
    PageModel<AccountFlowAppNewResponse> selectUserTzflow125(AccountFlowAppRequest request, PageModel<AccountFlowAppNewResponse> page);


//    /**
//     * 钛小白交易流水
//     *
//     * @param request
//     * @param page
//     * @return
//     */
//    PageModel<AccountFlowAppNewResponse> selectUserTxbflow125(AccountFlowAppRequest request, PageModel<AccountFlowAppNewResponse> page);
//
//
//    /**
//     * RIT交易流水
//     *
//     * @param request
//     * @param page
//     * @return
//     */
//    PageModel<AccountFlowAppNewResponse> selectUserRitflow125(AccountFlowAppRequest request, PageModel<AccountFlowAppNewResponse> page);


    /**
     * 钛小白交易流水
     *
     * @param request
     * @param page
     * @return
     */
    PageModel<AccountFlowAppNewResponse> selectUserTxbflow130(AccountFlowAppRequest request, PageModel<AccountFlowAppNewResponse> page);


    /**
     * RIT交易流水
     *
     * @param request
     * @param page
     * @return
     */
    PageModel<AccountFlowAppNewResponse> selectUserRitflow130(AccountFlowAppRequest request, PageModel<AccountFlowAppNewResponse> page);




    /**
     * 根据ID获取账单详情
     *
     * @param request
     */
    AccountFlowResponse130 getDetailBillById(AccountFlowAppRequest request);

    /**
     * 转出RIT页面的数据
     * @param kguser
     * @return
     */
    AppJsonEntity ritWithdrawPageData(UserkgResponse kguser);

    /**
     * 根据币种进行提现申请
     * @param kguser
     * @return
     */
    AppJsonEntity withdrawByCoinType(UserkgResponse kguser,AccountWithDrawRequest request);

    /**
     * 撤销待审核的提现工单
     * @param kguser
     * @param request
     * @return
     */
    AppJsonEntity cancelWithdraw(UserkgResponse kguser,AccountWithDrawRequest request);


    /**
     * RIT可用余额增加，冻结余额减少时产生的流水和操作日志
     * @param accountWithdrawFlowRit
     * @param kguser
     * @param balance_account_flow_id
     * @param frozen_account_flow_id
     * @param operId
     * @param now
     */
    void addRitBalanceReduceRitFrozen(AccountWithdrawFlowRit accountWithdrawFlowRit, UserkgOutModel kguser, SysUser operKguser,
                                      Long balance_account_flow_id, Long frozen_account_flow_id, Long operId, Long now, String flowDetail,
                                      BusinessTypeEnum balanceBusinessTypeEnum, BusinessTypeEnum frozenBusinessTypeEnum, FlowStatusEnum flowStatusEnum, OperTypeEnum operTypeEnum);

    /**
     * 根据工单号获取工单
     * @param withdrawFlowId
     * @return
     */
    AccountWithdrawFlowRit getByWithdrawFlowId(String withdrawFlowId);

    /**
     * 提现申请操作
     * @param request
     * @param coinEnum
     * @param kguser
     * @return
     */
    AppJsonEntity doWithDraw(AccountWithDrawRequest request, CoinEnum coinEnum, UserkgOutModel kguser,SysUser sysUser);

    /**
     * 算力收益
     * @param kguser
     * @return
     */
    AppJsonEntity performanceEarnings(UserkgResponse kguser);
}
