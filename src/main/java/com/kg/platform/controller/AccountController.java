package com.kg.platform.controller;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.support.CustomBigDecimalEditor;
import com.kg.platform.model.in.AccountInModel;
import com.kg.platform.model.request.AccountFlowRequest;
import com.kg.platform.model.request.AccountRequest;
import com.kg.platform.model.request.ArticleRequest;
import com.kg.platform.model.response.AccountFlowResponse;
import com.kg.platform.model.response.AccountResponse;
import com.kg.platform.model.response.ArticleResponse;
import com.kg.platform.service.AccountFlowService;
import com.kg.platform.service.AccountService;
import com.kg.platform.service.ArticlekgService;
import com.kg.platform.service.UserAccountService;

@Controller
@RequestMapping("account")
public class AccountController extends ApiBaseController {

    @Inject
    AccountService accountService;

    @Inject
    UserAccountService userAccountService;

    @Inject
    ArticlekgService articleService;

    @Inject
    AccountFlowService accountFlowService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        CustomBigDecimalEditor bigDecimalEditor = new CustomBigDecimalEditor();
        binder.registerCustomEditor(BigDecimal.class, bigDecimalEditor);

    }

    /**
     * 通用验证提现密码
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("verifyTXPass")
    @BaseControllerNote(needCheckParameter = true, isLogin = true, beanClazz = AccountRequest.class)
    public JsonEntity verifyTXPass(@RequestAttribute AccountRequest request) {
        if (null == request || null == request.getTxPassword()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PASSWORD_FORMAT.getCode(),
                    ExceptionEnum.PASSWORD_FORMAT.getMessage());
        }
        Result<AccountResponse> response = accountService.validationPwd(request);
        if (null != response && null != response.getData()) {
            return JsonEntity.makeSuccessJsonEntity("验证交易密码成功!");
        }
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.VERIFY_TXPASS_ERROR.getCode(),
                ExceptionEnum.VERIFY_TXPASS_ERROR.getMessage());
    }

    /**
     * 文章详情打赏
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("updateBalance")
    @BaseControllerNote(needCheckParameter = true, isLogin = true, beanClazz = AccountRequest.class)
    public JsonEntity updateBalance(@RequestAttribute AccountRequest request) {

        BigDecimal tipsmin = new BigDecimal(propertyLoader.getProperty("common", "global.tipsmin"));

        BigDecimal tipsmax = new BigDecimal(propertyLoader.getProperty("common", "global.tipsmax"));

        BigDecimal tips = request.getBalance();
        if (tips == null) {
            return JsonEntity.makeExceptionJsonEntity("", "打赏金额不能为空");
        }
        if (tips.compareTo(tipsmin) < 0 || tips.compareTo(tipsmax) > 0) {
            return JsonEntity.makeExceptionJsonEntity("", "打赏金额必须大于" + tipsmin + "小于" + tipsmax);
        }

        ArticleRequest articleRequest = new ArticleRequest();
        articleRequest.setArticleId(request.getArticleId());
        Result<ArticleResponse> result = articleService.getArticleContent(articleRequest);
        if (result.getData().getCreateUser().equals(request.getUserId().toString())) {
            return JsonEntity.makeExceptionJsonEntity("", "文章作者不能打赏自己发布的文章");
        }
        AccountFlowRequest accFlowRequest = new AccountFlowRequest();
        accFlowRequest.setUserId(request.getUserId());
        accFlowRequest.setArticleId(request.getArticleId());
        int tipsout = accountFlowService.getTipsout(accFlowRequest);
        if (tipsout >= 5) {
            return JsonEntity.makeExceptionJsonEntity("", "您今天已经打赏Ta5次了，明天再来吧");
        }

        Result<AccountResponse> response = accountService.validationPwd(request);
        if (null != response && null != response.getData()) {
            BigDecimal balance = new BigDecimal(response.getData().getBalance());
            if (tips.compareTo(balance) > 0) {
                return JsonEntity.makeExceptionJsonEntity("", "当前账户余额小于打赏金额");
            }

            if (null != response.getData().getAccountId()) {
                boolean SUCCESS = accountService.updateAddUbalance(request);
                if (SUCCESS) {
                    return JsonEntity.makeSuccessJsonEntity(SUCCESS);
                }
                return JsonEntity.makeExceptionJsonEntity("", "打赏失败");

            }
        }
        return JsonEntity.makeExceptionJsonEntity("", "交易密码验证失败");

    }

    /**
     * 我的钛值
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("selectByUserbalance")
    @BaseControllerNote(needCheckParameter = true, beanClazz = AccountRequest.class)
    public JsonEntity selectByUserbalance(@RequestAttribute AccountRequest request) {
        Result<AccountResponse> response = accountService.selectByUserbalance(request);
        if (response.getData() != null) {
            return JsonEntity.makeSuccessJsonEntity(response);
        }
        return JsonEntity.makeSuccessJsonEntity("", "获取失败");
    }

    @ResponseBody
    @RequestMapping("selectUserTxbBalance")
    @BaseControllerNote(needCheckParameter = true, beanClazz = AccountRequest.class)
    public JsonEntity selectUserTxbBalance(@RequestAttribute AccountRequest request) {
        Result<AccountResponse> response = accountService.selectUserTxbBalance(request);
        if (response.getData() != null) {
            return JsonEntity.makeSuccessJsonEntity(response);
        }
        return JsonEntity.makeSuccessJsonEntity("", "获取失败");
    }

    /**
     * 申请提现判断
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("auditAmount")
    @BaseControllerNote(needCheckParameter = true, beanClazz = AccountRequest.class)
    public JsonEntity auditAmount(@RequestAttribute AccountRequest request) {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setUserId(request.getUserId());
        Result<AccountResponse> response = accountService.selectOutUserId(accountRequest);
        if (response.getData() == null) {
            AccountInModel accountInModel = new AccountInModel();
            accountInModel.setUserId(request.getUserId());
            userAccountService.init(accountInModel);
        }
        Result<AccountFlowResponse> result = accountService.auditAmount(request);
        if (result.getData() != null) {
            return JsonEntity.makeSuccessJsonEntity(result);
        }
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(), ExceptionEnum.SUCCESS.getMessage());
    }

    /**
     * 文章奖励发放
     * 
     * @param request
     * @return
     */
    // app1.1.0屏蔽阅读奖励
    // @ResponseBody
    // @RequestMapping("updateUserbalance")
    // @BaseControllerNote(needCheckToken = false, needCheckParameter = true,
    // beanClazz = AccountRequest.class)
    // public JsonEntity updateUserbalance(@RequestAttribute AccountRequest
    // request) {
    //
    // BigDecimal success = accountService.updateUserbalance(request);
    //
    // return JsonEntity.makeSuccessJsonEntity(success);
    //
    // }

    /**
     * 设置阅读奖励前初始化用户账户
     * 
     * @param request
     * @return
     */
    // @ResponseBody
    // @RequestMapping("initAccount")
    // @BaseControllerNote(needCheckParameter = true, beanClazz =
    // AccountRequest.class)
    // public JsonEntity InitAccount(@RequestAttribute AccountRequest request) {
    // Result<AccountResponse> response =
    // accountService.selectOutUserId(request);
    // if (response == null) {
    // AccountInModel accountInModel = new AccountInModel();
    // accountInModel.setUserId(request.getUserId());
    // boolean SUCCESS = userAccountService.init(accountInModel);
    // if (SUCCESS) {
    // return
    // JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
    // ExceptionEnum.SUCCESS.getMessage());
    // }
    // return JsonEntity.makeExceptionJsonEntity("", "初始化账户出错");
    // }
    // return JsonEntity.makeSuccessJsonEntity(response);
    //
    // }




}
