package com.kg.platform.service.admin;

import java.util.List;

import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.dao.entity.SysUser;
import com.kg.platform.model.in.FreezeInModel;
import com.kg.platform.dao.entity.SysUser;
import com.kg.platform.model.request.AccountRequest;
import com.kg.platform.model.request.AccountWithDrawRequest;
import com.kg.platform.model.request.admin.*;
import com.kg.platform.model.response.AccountWithdrawFlowRitResponse;
import com.kg.platform.model.response.admin.AccountDipositQueryResponse;
import com.kg.platform.model.response.admin.AccountQueryResponse;
import com.kg.platform.model.response.admin.AccountRechargeQueryResponse;
import com.kg.platform.model.response.admin.AccountWithdrawQueryResponse;
import com.kg.platform.model.response.admin.BusinessTypeQueryResponse;
import com.kg.platform.model.response.admin.UserBonusDetailQueryResponse;
import com.kg.platform.model.response.admin.UserBonusQueryResponse;
import org.springframework.web.bind.annotation.RequestAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AccountService {

    PageModel<AccountRechargeQueryResponse> getAccountRecharge(AccountRechargeQueryRequest request);

    PageModel<AccountQueryResponse> getAccount(AccountQueryRequest request);

    PageModel<AccountQueryResponse> getTxbAccount(AccountQueryRequest request);

    PageModel<AccountQueryResponse> getRitAccount(AccountQueryRequest request);

    PageModel<AccountWithdrawQueryResponse> getAccountWithdraw(AccountWithdrawQueryRequest request);

    PageModel<AccountDipositQueryResponse> getAccountDiposit(AccountDipositQueryRequest request);

    boolean auditAccountWithdraw(AccountWithdrawEditRequest request);

    List<BusinessTypeQueryResponse> getBusinessType();

    /**
     * 用户奖励列表
     *
     * @param request
     * @return
     */
    PageModel<UserBonusQueryResponse> getUserBonusList(UserBonusQueryRequest request);

    /**
     * 用户奖励详情
     *
     * @param request
     * @return
     */
    UserBonusQueryResponse getUserBonusDetail(UserBonusQueryRequest request);

    /**
     * 用户奖励详情列表
     *
     * @param request
     * @return
     */
    PageModel<UserBonusDetailQueryResponse> getUserBonusDetailList(UserBonusQueryRequest request);

    /**
     * 查询用户奖励明细 氪金/钛值奖励统计
     *
     * @param request
     * @return
     */
    String getUserTotalBonus(AccountQueryRequest request);

    /**
     * RIT兑换查询
     *
     * @param request
     * @return
     */
    JsonEntity getRitExchangeList(RitExchangeQueryRequest request);

    /**
     * RIT兑换统计
     *
     * @param request
     * @return
     */
    JsonEntity getRitExchangeAmount(RitExchangeQueryRequest request);

    /**
     * 根据搜索条件获取RIT提现工单列表
     * @param request
     * @return
     */
    JsonEntity getRitWithdrawList(AccountWithDrawRequest request,PageModel<AccountWithdrawFlowRitResponse> pageModel);

    /**
     * 根据工单号查询对应工单
     * @param request
     * @return
     */
    JsonEntity detailRitWithdraw(AccountWithDrawRequest request);

    /**
     * 获取用户资产接口
     */
    JsonEntity getUserAssetInfoInfo(AccountWithDrawRequest request);

    /**
     * 冻结/解冻资产接口
     */
    JsonEntity assetOperation(SysUser sysUser,FreezeInModel request);


    /**
     * 获取提现工单列表搜索条件的定义含义
     * @return
     */
    JsonEntity withDrawSearchConditions(AccountWithDrawRequest request);

    /**
     * 审核RIT提现工单
     * @return
     */
    JsonEntity auditRitWithdraw(AccountWithDrawRequest request,SysUser sysUser, HttpServletRequest httpServletRequest);

    /**
     * 批量审核RIT提现工单
     * @return
     */
    JsonEntity batchAuditRitWithdraw(AccountWithDrawRequest request,SysUser sysUser, HttpServletRequest httpServletRequest);

    /**
     * 财务导出提现工单EXCEL
     * @param request
     * @param response
     * @param servletRequest
     * @return
     */
    void excelOfRitWithdrawFlowList(AccountWithDrawRequest request, HttpServletResponse response, HttpServletRequest servletRequest);

    /**
     * 手动发起转账
     * @param request
     * @param sysUser
     * @return
     */
    JsonEntity manualTransfer(@RequestAttribute AccountWithDrawRequest request, @RequestAttribute SysUser sysUser,HttpServletRequest servletRequest);


    /**
     * 检查审核人的IP是否能审核
     * @param httpServletRequest
     */
    void checkAuditUserIp(HttpServletRequest httpServletRequest);

    /**
     * 导出EXCEL检验TOKEN和验签
     * @param token
     * @param servletRequest
     */
    void checkTokenSign(String token,HttpServletRequest servletRequest);

    /**
     * 批量审核MQ业务
     * @param withdrawFlowIds
     * @param request
     * @param sysUser
     * @param now
     */
    void doBatchAuditRitWithdraw(List<Long> withdrawFlowIds,AccountWithDrawRequest request, SysUser sysUser, long now );

	/**
	 * 后台管理员修改账户余额
	 * @param request
	 * @return
	 */
	JsonEntity updateAsset(AccountRequest request, HttpServletRequest servletRequest);
}
