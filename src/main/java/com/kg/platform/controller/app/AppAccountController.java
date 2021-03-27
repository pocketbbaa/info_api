package com.kg.platform.controller.app;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.model.request.AccountWithDrawRequest;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.app.AppAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/kgApp/account")
public class AppAccountController {

    @Autowired
    private AppAccountService appAccountService;

    @RequestMapping("ritWithdrawPageData")
    @ResponseBody
    @BaseControllerNote(needCheckToken = true, needCheckParameter = false)
    public AppJsonEntity withdrawPageData(@RequestAttribute UserkgResponse kguser) {
        return appAccountService.ritWithdrawPageData(kguser);
    }

    @RequestMapping("withdrawByCoinType")
    @ResponseBody
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = AccountWithDrawRequest.class)
    public AppJsonEntity withdrawByCoinType(@RequestAttribute UserkgResponse kguser, @RequestAttribute AccountWithDrawRequest request) {
        return appAccountService.withdrawByCoinType(kguser, request);
    }

    @RequestMapping("cancelWithdraw")
    @ResponseBody
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = AccountWithDrawRequest.class)
    public AppJsonEntity cancelWithdraw(@RequestAttribute UserkgResponse kguser, @RequestAttribute AccountWithDrawRequest request) {
        return appAccountService.cancelWithdraw(kguser, request);
    }

    /**
     * 我的算力收益
     *
     * @param kguser
     * @param request
     * @return
     */
    @RequestMapping("performance/earnings")
    @ResponseBody
    @BaseControllerNote(needCheckToken = true, needCheckParameter = false)
    public AppJsonEntity performanceEarnings(@RequestAttribute UserkgResponse kguser) {
        return appAccountService.performanceEarnings(kguser);
    }

}
