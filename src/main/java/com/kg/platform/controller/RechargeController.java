package com.kg.platform.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kg.framework.toolkit.Check;
import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ApiMessageException;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.CheckUtils;
import com.kg.platform.common.utils.HttpUtil;
import com.kg.platform.model.request.UserChargeRequest;
import com.kg.platform.model.request.UserDepositRequest;
import com.kg.platform.model.response.SiteinfoResponse;
import com.kg.platform.service.SiteinfoService;
import com.kg.platform.service.UserChargeService;
import com.kg.platform.service.UserDepositService;

@Controller
@RequestMapping("recharge")
public class RechargeController extends ApiBaseController {

    @Inject
    UserChargeService rechargeService;

    @Inject
    UserDepositService depositService;

    @Autowired
    SiteinfoService siteinfoService;

    /**
     * 用户充值
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("userCharge")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserChargeRequest.class)
    public JsonEntity userCharge(@RequestAttribute UserChargeRequest request, HttpServletRequest req) {

        Result<SiteinfoResponse> siteinfoResponse = siteinfoService.selectSiteInfo();
        CheckUtils.checkRetInfo(siteinfoResponse, true);

        SiteinfoResponse siteInfo = siteinfoResponse.getData();
        if (!Check.NuNString(siteInfo.getLimitIp())) {
            String userIp = HttpUtil.getIpAddr(req);
            logger.info("====用户充值来源ip" + userIp);

            if (siteInfo.getLimitIp().indexOf(userIp) == -1) {
                throw new ApiMessageException(ExceptionEnum.ADMINIPERROR);
            }
        }

        boolean success = rechargeService.recharge(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        }

        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(), "用户充值失败");

    }

    /**
     * 用户缴纳保证金
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("deposit")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserDepositRequest.class)
    public JsonEntity deposit(@RequestAttribute UserDepositRequest request, HttpServletRequest req) {

        Result<SiteinfoResponse> siteinfoResponse = siteinfoService.selectSiteInfo();
        CheckUtils.checkRetInfo(siteinfoResponse, true);

        SiteinfoResponse siteInfo = siteinfoResponse.getData();
        if (!Check.NuNString(siteInfo.getLimitIp())) {
            String userIp = HttpUtil.getIpAddr(req);
            logger.info("====用户缴纳保证金来源ip" + userIp);

            if (siteInfo.getLimitIp().indexOf(userIp) == -1) {
                throw new ApiMessageException(ExceptionEnum.ADMINIPERROR);
            }
        }

        boolean success = depositService.deposit(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        }

        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(), "用户缴纳保证金失败");

    }

}
