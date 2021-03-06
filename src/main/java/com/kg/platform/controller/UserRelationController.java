package com.kg.platform.controller;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kg.framework.utils.StringUtils;
import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.CheckUtils;
import com.kg.platform.common.utils.Constants;
import com.kg.platform.common.utils.JedisKey;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.enumeration.InviteBonusEnum;
import com.kg.platform.model.in.AccountInModel;
import com.kg.platform.model.request.AccountRequest;
import com.kg.platform.model.request.UserRelationRequest;
import com.kg.platform.model.request.UserkgRequest;
import com.kg.platform.model.response.AccountResponse;
import com.kg.platform.model.response.UserRelationResponse;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.AccountService;
import com.kg.platform.service.UserRelationService;
import com.kg.platform.service.UserWithdrawService;
import com.kg.platform.service.UserkgService;

@Controller
@RequestMapping("userRelation")
public class UserRelationController extends ApiBaseController {

    @Inject
    private UserRelationService userRelationService;

    @Inject
    private UserkgService userkgService;

    @Inject
    private AccountService accountService;

    @Inject
    private UserWithdrawService userWithdrawService;

    @Inject
    private JedisUtils jedisUtils;

    @ResponseBody
    @RequestMapping("invite")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserRelationRequest.class)
    public JsonEntity invite(@RequestAttribute UserRelationRequest request) {
        if (null == request || null == request.getUserId()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }

        UserRelationResponse userRelationResponse = new UserRelationResponse();
        userRelationResponse.setInviteCount(userRelationService.getInviteCount(request.getUserId()));

        UserkgRequest req = new UserkgRequest();
        req.setUserId(request.getUserId());
        Result<UserkgResponse> response = userkgService.getUserDetails(req);
        if (CheckUtils.checkRetInfo(response)) {
            UserkgResponse ukrs = response.getData();
            String myCode = ukrs.getInviteCode();
            if (StringUtils.isBlank(myCode)) {
                myCode = userkgService.getInviteCode(); // ????????????????????????

                userkgService.updateInviteCode(request.getUserId(), myCode);
            }

            userRelationResponse.setInviteCode(myCode);
            userRelationResponse.setInviteStatus(ukrs.getBonusStatus());
            userRelationResponse.setInviteFreezeReason(ukrs.getBonusFreezeReason());
        }

        return JsonEntity.makeSuccessJsonEntity(userRelationResponse);
    }

//    /**
//     * ??????????????????-????????????????????????
//     *
//     * @param request
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping("applyWithdraw")
//    @BaseControllerNote(needCheckParameter = true, beanClazz = UserRelationRequest.class)
//    public JsonEntity applyWithdraw(@RequestAttribute UserRelationRequest request) {
//        if (null == request || null == request.getUserId()) {
//            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
//                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
//        }
//
//        // ?????????????????????????????????
//        UserkgRequest req = new UserkgRequest();
//        req.setUserId(request.getUserId());
//        Result<UserkgResponse> response = userkgService.getUserDetails(req);
//        if (CheckUtils.checkRetInfo(response)) {
//            UserkgResponse ukrs = response.getData();
//            Integer bonusStatus = ukrs.getBonusStatus();
//            if (bonusStatus == 0 || bonusStatus == 2) {
//                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.INVITE_FREZEE.getCode(),
//                        ExceptionEnum.INVITE_FREZEE.getMessage());
//            }
//        } else {
//            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
//                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
//        }
//        // ??????????????????
//        if (StringUtils.isBlank(request.getTxPassword())) {
//            return JsonEntity.makeExceptionJsonEntity("", "?????????????????????");
//        }
//        AccountRequest accountRequest = new AccountRequest();
//        accountRequest.setTxPassword(request.getTxPassword());
//        accountRequest.setUserId(request.getUserId());
//        Result<AccountResponse> model = accountService.validationPwd(accountRequest);
//
//        if (null == model || null == model.getData()) {
//
//            int txTimes = userkgService.checkTxpassLimit(String.valueOf(request.getUserId()));
//            if (txTimes == 0) {
//                return JsonEntity.makeExceptionJsonEntity(20014, "????????????????????????5???????????????????????????????????????");
//            } else {
//                return JsonEntity.makeExceptionJsonEntity(20014,
//                        "???????????????????????????" + (5 - txTimes) + "??????????????????????????????5?????????????????????????????????????????????");
//            }
//        }
//
//        // ????????????????????????????????????????????????
//        String jedisKey = JedisKey.getInviteWithdraw(request.getUserId());
//        if (jedisUtils.get(jedisKey) != null) {
//            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.INVITE_WITHDRAW_HANDLING.getCode(),
//                    ExceptionEnum.INVITE_WITHDRAW_HANDLING.getMessage());
//        }
//        jedisUtils.set(jedisKey, request.getUserId(), 10);
//
//        Long _count = userRelationService.getInviteCount(request.getUserId());
//        // ???????????????????????? ??????????????????
//        if (null == _count || 0L == _count || InviteBonusEnum.TEN.get_count() > _count) {
//            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.INVITE_WITHDRAW_ERROR.getCode(),
//                    ExceptionEnum.INVITE_WITHDRAW_ERROR.getMessage());
//        }
//        request.setInviteCount(_count);
//
//        // ????????????????????????
//        BigDecimal _bonus = BigDecimal.ZERO;
//        Long _inviteCount = request.getInviteCount();
//        Long targetCount = request.getInviteCount();
//        if (_inviteCount >= InviteBonusEnum.TEN.get_count() && _inviteCount < InviteBonusEnum.THRITY.get_count()) {
//            targetCount = InviteBonusEnum.TEN.get_count();
//            _bonus = InviteBonusEnum.TEN.get_bonus();
//
//        } else if (_inviteCount >= InviteBonusEnum.THRITY.get_count()
//                && _inviteCount < InviteBonusEnum.FIFTY.get_count()) {
//            targetCount = InviteBonusEnum.THRITY.get_count();
//            _bonus = InviteBonusEnum.THRITY.get_bonus();
//
//        } else if (_inviteCount >= InviteBonusEnum.FIFTY.get_count()
//                && _inviteCount < InviteBonusEnum.EIGHTY.get_count()) {
//            targetCount = InviteBonusEnum.FIFTY.get_count();
//            _bonus = InviteBonusEnum.FIFTY.get_bonus();
//
//        } else if (_inviteCount >= InviteBonusEnum.EIGHTY.get_count()) {
//            targetCount = InviteBonusEnum.EIGHTY.get_count();
//            _bonus = InviteBonusEnum.EIGHTY.get_bonus();
//        }
//        // ???????????????????????????????????????
//
//        request.set_bonus(_bonus);
//        request.setTargetCount(targetCount);
//
//        AccountInModel accountInModel = new AccountInModel();
//        accountInModel.setUserId(Constants.PLATFORM_USER_ID);
//        accountInModel.setBalance(_bonus);
//        if (!accountService.validPlatformBalance(accountInModel)) {
//            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PLATFOEM_BALANCE_ERROR.getCode(),
//                    ExceptionEnum.PLATFOEM_BALANCE_ERROR.getMessage());
//        }
//        // ????????????
//        userWithdrawService.applyWithdraw(request);
//        return JsonEntity.makeSuccessJsonEntity("??????????????????!");
//    }

}
