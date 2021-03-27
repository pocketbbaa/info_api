package com.kg.platform.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.dao.entity.SysUser;
import com.kg.platform.model.in.FreezeInModel;
import com.kg.platform.model.request.AccountRequest;
import com.kg.platform.model.request.AccountWithDrawRequest;
import com.kg.platform.model.request.admin.*;
import com.kg.platform.model.response.AccountWithdrawFlowRitResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.service.SiteinfoService;
import com.kg.platform.service.admin.AccountService;

@RestController("AdminAccountController")
@RequestMapping("admin/account")
public class AccountController extends AdminBaseController {

    @Autowired
    private AccountService accountService;

    @Autowired
    SiteinfoService siteinfoService;

    @RequestMapping(value = "getAccountRecharge", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = AccountRechargeQueryRequest.class)
    public JsonEntity getAccountRecharge(@RequestAttribute AccountRechargeQueryRequest request) {
        return JsonEntity.makeSuccessJsonEntity(accountService.getAccountRecharge(request));
    }

    @RequestMapping(value = "getAccount", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = AccountQueryRequest.class)
    public JsonEntity getAccount(@RequestAttribute AccountQueryRequest request) {
        return JsonEntity.makeSuccessJsonEntity(accountService.getAccount(request));
    }

    @RequestMapping(value = "getTxbAccount", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = AccountQueryRequest.class)
    public JsonEntity getTxbAccount(@RequestAttribute AccountQueryRequest request) {
        return JsonEntity.makeSuccessJsonEntity(accountService.getTxbAccount(request));
    }

    @RequestMapping(value = "getRitAccount", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = AccountQueryRequest.class)
    public JsonEntity getRitAccount(@RequestAttribute AccountQueryRequest request) {
        return JsonEntity.makeSuccessJsonEntity(accountService.getRitAccount(request));
    }

    @RequestMapping(value = "getBusinessType", method = RequestMethod.POST)
    public JsonEntity getBusinessType() {
        return JsonEntity.makeSuccessJsonEntity(accountService.getBusinessType());
    }

    /**
     * 用户中心/奖励明细 查询太小白奖励和钛值奖励和rit
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "getSumBonus", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = AccountQueryRequest.class)
    public JsonEntity getSumBonus(@RequestAttribute AccountQueryRequest request) {
        return JsonEntity.makeSuccessJsonEntity(accountService.getUserTotalBonus(request));
    }

    @RequestMapping(value = "getAccountWithdraw", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = AccountWithdrawQueryRequest.class)
    public JsonEntity getAccountWithdraw(@RequestAttribute AccountWithdrawQueryRequest request) {
        return JsonEntity.makeSuccessJsonEntity(accountService.getAccountWithdraw(request));
    }

    @RequestMapping(value = "getAccountDiposit", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = AccountDipositQueryRequest.class)
    public JsonEntity getAccountDiposit(@RequestAttribute AccountDipositQueryRequest request) {
        return JsonEntity.makeSuccessJsonEntity(accountService.getAccountDiposit(request));
    }

    @RequestMapping(value = "auditAccountWithdraw", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = AccountWithdrawEditRequest.class)
    public JsonEntity auditAccountWithdraw(@RequestAttribute AccountWithdrawEditRequest request,
                                           HttpServletRequest req) {

        accountService.checkAuditUserIp(req);

        if (null != request.getFlowId() && null != request.getStatus()) {
            boolean success = accountService.auditAccountWithdraw(request);
            if (success) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                        ExceptionEnum.SUCCESS.getMessage());
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                        ExceptionEnum.DATAERROR.getMessage());
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

    /**
     * 用户奖励列表
     *
     * @return
     */
    @RequestMapping(value = "getUserBonusList", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = UserBonusQueryRequest.class)
    public JsonEntity getUserBonusList(@RequestAttribute UserBonusQueryRequest request) {
        return JsonEntity.makeSuccessJsonEntity(accountService.getUserBonusList(request));
    }

    /**
     * 用户奖励详情
     *
     * @return
     */
    @RequestMapping(value = "getUserBonusDetail", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = UserBonusQueryRequest.class)
    public JsonEntity getUserBonusDetail(@RequestAttribute UserBonusQueryRequest request) {
        return JsonEntity.makeSuccessJsonEntity(accountService.getUserBonusDetail(request));
    }

    /**
     * 用户奖励详情列表
     *
     * @return
     */
    @RequestMapping(value = "getUserBonusDetailList", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = UserBonusQueryRequest.class)
    public JsonEntity getUserBonusDetailList(@RequestAttribute UserBonusQueryRequest request) {
        return JsonEntity.makeSuccessJsonEntity(accountService.getUserBonusDetailList(request));
    }

    /**
     * RIT兑换列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "getRitExchangeList", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = RitExchangeQueryRequest.class)
    public JsonEntity getRitExchangeList(@RequestAttribute RitExchangeQueryRequest request) {
        return accountService.getRitExchangeList(request);
    }

    /**
     * RIT兑换统计
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "getRitExchangeAmount", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = RitExchangeQueryRequest.class)
    public JsonEntity getRitExchangeAmount(@RequestAttribute RitExchangeQueryRequest request) {
        return accountService.getRitExchangeAmount(request);
    }


    @RequestMapping(value = "getRitWithdrawList", method = RequestMethod.POST)
    @BaseControllerNote(needCheckToken = false,needCheckParameter = true,beanClazz = AccountWithDrawRequest.class)
    public JsonEntity getRitWithdrawList(@RequestAttribute AccountWithDrawRequest request, PageModel<AccountWithdrawFlowRitResponse> pageModel) {
        return accountService.getRitWithdrawList(request,pageModel);
    }

    @RequestMapping(value = "detailRitWithdraw", method = RequestMethod.POST)
    @BaseControllerNote(needCheckToken = false,needCheckParameter = true,beanClazz = AccountWithDrawRequest.class)
    public JsonEntity detailRitWithdraw(@RequestAttribute AccountWithDrawRequest request) {
        logger.info("RIT审核提现详情请求参数:{}", JSON.toJSONString(request));
        return accountService.detailRitWithdraw(request);
    }

    /**
     * 获取用户资产接口
     */
    @RequestMapping(value = "getUserAssetInfo",method = RequestMethod.POST)
    @ResponseBody
    @BaseControllerNote(beanClazz = AccountWithDrawRequest.class)
    public JsonEntity getUserAssetInfo(@RequestAttribute AccountWithDrawRequest request){
        return accountService.getUserAssetInfoInfo(request);
    }

    /**
     * 冻结/解冻资产
     */
    @RequestMapping(value = "assetOperation",method = RequestMethod.POST)
    @ResponseBody
    @BaseControllerNote(beanClazz = FreezeInModel.class)
    public JsonEntity assetOperation(@RequestAttribute SysUser sysUser, @RequestAttribute FreezeInModel request){
        return accountService.assetOperation(sysUser,request);
    }

    @RequestMapping(value = "withDrawSearchConditions", method = RequestMethod.POST)
    @BaseControllerNote(needCheckToken = false,needCheckParameter = true,beanClazz = AccountWithDrawRequest.class)
    public JsonEntity withDrawSearchConditions(@RequestAttribute AccountWithDrawRequest request){
        return accountService.withDrawSearchConditions(request);
    }

    @RequestMapping(value = "auditRitWithdraw", method = RequestMethod.POST)
    @BaseControllerNote(needCheckToken = true,needCheckParameter = true,beanClazz = AccountWithDrawRequest.class)
    public JsonEntity auditRitWithdraw(@RequestAttribute AccountWithDrawRequest request, @RequestAttribute SysUser sysUser, HttpServletRequest httpServletRequest) {
        logger.info("RIT审核提现请求参数:{}", JSON.toJSONString(request));
        return accountService.auditRitWithdraw(request,sysUser,httpServletRequest);
    }

    @RequestMapping(value = "batchAuditRitWithdraw", method = RequestMethod.POST)
    @BaseControllerNote(needCheckToken = true,needCheckParameter = true,beanClazz = AccountWithDrawRequest.class)
    public JsonEntity batchAuditRitWithdraw(@RequestAttribute AccountWithDrawRequest request, @RequestAttribute SysUser sysUser, HttpServletRequest httpServletRequest) {
        logger.info("RIT批量审核提现请求参数:{}", JSON.toJSONString(request));
        return accountService.batchAuditRitWithdraw(request,sysUser,httpServletRequest);
    }

    @ResponseBody
    @RequestMapping(value = "excelOfRitWithdrawFlowList",method = RequestMethod.GET)
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true,beanClazz = AccountWithDrawRequest.class)
    public void excelOfRitWithdrawFlowList( @RequestAttribute AccountWithDrawRequest request, HttpServletResponse response, HttpServletRequest servletRequest){
        accountService.excelOfRitWithdrawFlowList(request,response,servletRequest);
    }

    @RequestMapping(value = "manualTransfer", method = RequestMethod.POST)
    @BaseControllerNote(needCheckToken = true,needCheckParameter = true,beanClazz = AccountWithDrawRequest.class)
    public JsonEntity manualTransfer(@RequestAttribute AccountWithDrawRequest request, @RequestAttribute SysUser sysUser,HttpServletRequest servletRequest) {
        logger.info("后台手动发起转账入参：{}", JSON.toJSONString(request));
        return accountService.manualTransfer(request,sysUser,servletRequest);
    }

	/**
	 * 后台管理员修改账户余额
	 */
	@RequestMapping(value = "updateAsset",method = RequestMethod.POST)
	@ResponseBody
	@BaseControllerNote(beanClazz = AccountRequest.class)
	public JsonEntity updateAsset(@RequestAttribute AccountRequest request, @RequestAttribute HttpServletRequest servletRequest){
		if(request.getUserId()==null || request.getBalance()==null || request.getCoinType()==null){
			return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
		}
		return accountService.updateAsset(request, servletRequest);
	}
}
