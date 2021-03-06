package com.kg.platform.controller;

import java.math.BigDecimal;

import javax.inject.Inject;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.model.request.*;
import com.kg.platform.model.response.AccountFlowResponse130;
import com.kg.platform.service.app.AppAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kg.framework.utils.StringUtils;
import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.CheckUtils;
import com.kg.platform.common.utils.JedisKey;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.enumeration.AccountWithdrawStatusEnum;
import com.kg.platform.enumeration.UserRoleEnum;
import com.kg.platform.model.in.AccountInModel;
import com.kg.platform.model.response.AccountFlowResponse;
import com.kg.platform.model.response.AccountResponse;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.AccountFlowService;
import com.kg.platform.service.AccountService;
import com.kg.platform.service.AccountWithdrawFlowService;
import com.kg.platform.service.UserAccountService;
import com.kg.platform.service.UserkgService;

@Controller
@RequestMapping("accountFlow")
public class AccountFlowController extends ApiBaseController {

    @Inject
    AccountFlowService accountFlowService;

    @Inject
    UserkgService userkgService;

    @Inject
    AccountService accountService;

    @Inject
    UserAccountService userAccountService;

    @Inject
    AccountWithdrawFlowService accountWithdrawFlowService;

    @Inject
    JedisUtils jedisUtils;

    @Autowired
    AppAccountService appAccountService;


    /**
     * ??????????????????
     * 
     * @param request
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("selectByUserflow")
    @BaseControllerNote(needCheckParameter = true, beanClazz = AccountFlowRequest.class)
    public JsonEntity selectByUserflow(@RequestAttribute AccountFlowRequest request,
            PageModel<AccountFlowResponse> page) {

        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setUserId(request.getUserId());
        Result<AccountResponse> result = accountService.selectOutUserId(accountRequest);
        if (result.getData() == null) {
            AccountInModel accountInModel = new AccountInModel();
            accountInModel.setUserId(request.getUserId());
            userAccountService.init(accountInModel);
        }

        page.setCurrentPage(request.getCurrentPage());
        page = accountFlowService.selectByUserflow(request, page);
        if (page != null) {
            return JsonEntity.makeSuccessJsonEntity(page);
        }
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                ExceptionEnum.DATAERROR.getMessage());
    }

    @ResponseBody
    @RequestMapping("selectUserTxbflow")
    @BaseControllerNote(needCheckParameter = true, beanClazz = AccountFlowRequest.class)
    public JsonEntity selectUserTxbflow(@RequestAttribute AccountFlowRequest request,
            PageModel<AccountFlowResponse130> page) {

        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setUserId(request.getUserId());
        Result<AccountResponse> result = accountService.selectOutUserId(accountRequest);
        if (result.getData() == null) {
            AccountInModel accountInModel = new AccountInModel();
            accountInModel.setUserId(request.getUserId());
            userAccountService.init(accountInModel);
        }

        page.setCurrentPage(request.getCurrentPage());
        page = accountFlowService.selectUserTxbflow(request, page);
        if (page != null) {
            return JsonEntity.makeSuccessJsonEntity(page);
        }
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                ExceptionEnum.DATAERROR.getMessage());
    }

    /**
     * ????????????
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("undoUserTimes")
    @BaseControllerNote(needCheckParameter = true, beanClazz = AccountFlowRequest.class)
    public JsonEntity undoUserTimes(@RequestAttribute AccountFlowRequest request) {

        AccountWithdrawFlowRequest withdrawFlowRequest = new AccountWithdrawFlowRequest();
        withdrawFlowRequest.setUserId(request.getUserId());
        withdrawFlowRequest.setStatus(AccountWithdrawStatusEnum.FAIL.getStatus());
        withdrawFlowRequest.setWithdrawFlowId(request.getWithdrawFlowId());

        boolean SUCCESS = accountWithdrawFlowService.updateByUserSelective(withdrawFlowRequest);
        if (SUCCESS) {
            return JsonEntity.makeSuccessJsonEntity("????????????");
        }

        return JsonEntity.makeExceptionJsonEntity("", "????????????");

    }

    /**
     * ????????????
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("addUserTimes")
    @BaseControllerNote(needCheckParameter = true, isLogin = true, beanClazz = AccountFlowRequest.class)
    public JsonEntity addUserTimes(@RequestAttribute AccountFlowRequest request) {

        // ??????????????????????????????
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setTxPassword(request.getTxPassword());
        accountRequest.setUserId(request.getUserId());

        if (StringUtils.isBlank(request.getTxPassword())) {
            return JsonEntity.makeExceptionJsonEntity("", "?????????????????????");
        }

        if (null == request.getAmount()) {
            return JsonEntity.makeExceptionJsonEntity("", "??????????????????");
        }

        // ?????????????????????????????????????????????
        AccountRequest accrequest = new AccountRequest();
        accrequest.setUserId(request.getUserId());
        Result<AccountFlowResponse> result = accountService.getMaxMinTimes(accrequest);
        BigDecimal actionMinTimes = new BigDecimal(result.getData().getActionMinTimes());
        int falg = request.getAmount().compareTo(actionMinTimes);
        // ??????????????????????????????????????????
        // -1??????????????????????????????????????????
        if (falg == -1) {
            return JsonEntity.makeExceptionJsonEntity("20015", "??????????????????????????????" + actionMinTimes);
        }
        UserkgRequest userkgRequest = new UserkgRequest();
        userkgRequest.setUserId(request.getUserId());
        Result<UserkgResponse> uResponse = userkgService.getUserDetails(userkgRequest);
        // ???????????????????????????????????????????????????????????????????????????????????????
        CheckUtils.checkRetInfo(uResponse, true);
        int userRole = uResponse.getData().getUserRole().intValue();
        if (userRole == UserRoleEnum.ENTERPRICE.getRole() || userRole == UserRoleEnum.OTHER.getRole()) {
            BigDecimal actionMaxTimes = new BigDecimal(result.getData().getActionTimes());
            int res = request.getAmount().compareTo(actionMaxTimes);
            if (res == 1) {
                return JsonEntity.makeExceptionJsonEntity("20016", "??????????????????????????????" + actionMaxTimes);
            }
        }

        AccountRequest condition = new AccountRequest();
        condition.setUserId(request.getUserId());
        // ?????????????????????
        Result<AccountResponse> outModel = accountService.selectByUserbalance(condition);

        // ???????????????
        if (outModel.getData() == null) {
            AccountInModel accountIn = new AccountInModel();
            accountIn.setUserId(request.getUserId());
            accountIn.setBalance(new BigDecimal(0.000));
            userAccountService.init(accountIn);
        }

        Result<AccountResponse> model = accountService.validationPwd(accountRequest);
        if (null == model || null == model.getData()) {

            int txTimes = userkgService.checkTxpassLimit(String.valueOf(request.getUserId()));
            if (txTimes == 0) {
                return JsonEntity.makeExceptionJsonEntity(20014, "????????????????????????5???????????????????????????????????????");
            } else {
                return JsonEntity.makeExceptionJsonEntity(20014,
                        "???????????????????????????" + (5 - txTimes) + "??????????????????????????????5?????????????????????????????????????????????");
            }
        }
        if (null == request.getCode() || "".equals(request.getCode())) {
            return JsonEntity.makeExceptionJsonEntity("", "??????????????????");
        }

        Result<String> checkcode = userkgService.checkSmsEmailcode(request.getUserMobile());
        if (checkcode.getData() == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_VALIDATE.getCode(),
                    ExceptionEnum.USER_VALIDATE.getMessage());
        } else if (!checkcode.getData().equals(request.getCode())) {
            return JsonEntity.makeExceptionJsonEntity("", "???????????????");
        }

        jedisUtils.del(JedisKey.vatcodeKey(request.getUserMobile()));

        boolean SUCCESS = accountFlowService.addUserTimes(request);
        if (SUCCESS) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        }
        return JsonEntity.makeExceptionJsonEntity("", "??????????????????");

    }

    /**
     * ??????,??????????????????
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("TopupTimes")
    @BaseControllerNote(needCheckParameter = true, beanClazz = AccountFlowRequest.class)
    public JsonEntity TopupTimes() {

        Result<AccountFlowResponse> response = accountFlowService.TopupTimes();
        if (response.getData() != null) {
            return JsonEntity.makeSuccessJsonEntity(response);

        }
        return JsonEntity.makeSuccessJsonEntity("", "??????????????????");

    }

    /**
     * ????????????
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("detailBill")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, isLogin = true, beanClazz = AccountFlowAppRequest.class)
    public JsonEntity detailBill(@RequestAttribute AccountFlowAppRequest request) {
        if (request == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        logger.info("[detailBill] -> request:" + JSONObject.toJSONString(request));
        AccountFlowResponse130 accountFlowResponse = appAccountService.getDetailBillById(request);
        if (accountFlowResponse == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
        }
        return JsonEntity.makeSuccessJsonEntity(accountFlowResponse);
    }
}
