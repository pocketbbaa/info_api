package com.kg.platform.controller;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.dao.entity.UserRelation;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.AccountFlowRequest;
import com.kg.platform.model.request.AccountRequest;
import com.kg.platform.model.request.DiscipleRequest;
import com.kg.platform.model.request.UserRelationRequest;
import com.kg.platform.model.request.UserkgRequest;
import com.kg.platform.model.response.AccountResponse;
import com.kg.platform.model.response.DiscipleInfoResponse;
import com.kg.platform.model.response.DiscipleRemindRespose;
import com.kg.platform.model.response.MasterInfoResponse;
import com.kg.platform.model.response.admin.UserQueryResponse;
import com.kg.platform.service.AccountFlowService;
import com.kg.platform.service.AccountService;
import com.kg.platform.service.DiscipleService;
import com.kg.platform.service.UserRelationService;
import com.kg.platform.service.admin.UserService;

/**
 * 我的徒弟模块
 * 
 * @author 74190
 *
 */
@Controller
@RequestMapping("disciple")
public class DiscipleController extends ApiBaseController {

    @Inject
    private DiscipleService discipleService;

    @Inject
    private UserRelationService userRelationService;

    @Inject
    private AccountFlowService uccountFlowService;

    @Inject
    private UserService userService;

    @Inject
    AccountService accountService;

    /**
     * 累计/今日 徒弟进贡列表
     * 
     * @param request
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("getContributionList")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = DiscipleRequest.class)
    public JsonEntity getContributionList(@RequestAttribute DiscipleRequest request,
            PageModel<DiscipleInfoResponse> page) {
        // 验证是否传入userId
        if (null == request || null == request.getUserId()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        page.setCurrentPage(request.getCurrentPage());
        page.setPageSize(request.getPageSize());
        page = discipleService.getDiscipleContribution(request, page);
        if (page != null) {
            return JsonEntity.makeSuccessJsonEntity(page);
        }
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                ExceptionEnum.DATAERROR.getMessage());
    }

    /**
     * 通过邀请码绑定我的师傅
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("bindingTeacher")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = UserRelationRequest.class)
    public JsonEntity bindingTeacher(@RequestAttribute UserRelationRequest request) {
        if (null == request || null == request.getUserId() || null == request.getInviteCode()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        UserkgOutModel userkgOutModel = userService.selectByInviteCode(request.getInviteCode());
        // 验证是否存在该邀请人
        if (userkgOutModel == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.NONE_INVITER.getCode(),
                    ExceptionEnum.NONE_INVITER.getMessage());
        }
        // 验证用户是否已经绑定过师傅
        UserRelation userRelation = userRelationService.getMyTeacher(request.getUserId());
        if (userRelation != null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.EXIST_TEACHER.getCode(),
                    ExceptionEnum.EXIST_TEACHER.getMessage());
        }
        // 验证师傅是否在绑定徒弟的账号
        UserRelation teacherRelation = userRelation = userRelationService
                .getMyTeacher(Long.valueOf(userkgOutModel.getUserId()));
        if (teacherRelation != null && teacherRelation.getUserId().longValue() == request.getUserId().longValue()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.TEACHER_BIND_SUB_ERROR.getCode(),
                    ExceptionEnum.TEACHER_BIND_SUB_ERROR.getMessage());
        }
        // 验证是否存在绑定自己账号
        if (userkgOutModel.getUserId().equals(String.valueOf(request.getUserId()))) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.INVITER_SELF_ERROR.getCode(),
                    ExceptionEnum.INVITER_SELF_ERROR.getMessage());
        }
        // 专栏作者无法成为普通用户的徒弟 可以绑定和自己等级的用户
        UserQueryResponse uqr = userService.getUserInfo(request.getUserId());
        if (uqr == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        } else if (uqr.getUserRole() == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        } else if (!((userkgOutModel.getUserRole().intValue() == 1 && uqr.getUserRole().intValue() == 1)
                || (userkgOutModel.getUserRole().intValue() > 1 && uqr.getUserRole().intValue() > 1)
                || (userkgOutModel.getUserRole().intValue() > 1 && uqr.getUserRole().intValue() == 1))) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.BINGD_SPCOLUMN_ERROR.getCode(),
                    ExceptionEnum.BINGD_SPCOLUMN_ERROR.getMessage());
        }
        // 绑定师傅
        request.setRelUserId(request.getUserId());
        request.setUserId(Long.valueOf(userkgOutModel.getUserId()));
        Boolean isoK = userRelationService.bindTeacher(request);
        if (isoK) {
            return JsonEntity.makeSuccessJsonEntity("绑定成功");
        }
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.BINGD_ERROR.getCode(),
                ExceptionEnum.BINGD_ERROR.getMessage());
    }

    /**
     * 查询我的师傅信息
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("getTeacherInfo")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JsonEntity getTeacherInfo(@RequestAttribute UserkgRequest request) {
        if (null == request || null == request.getUserId()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        Result<MasterInfoResponse> masterInfoResponse = discipleService.getMaterInfo(request.getUserId());
        return JsonEntity.makeSuccessJsonEntity(masterInfoResponse.getData());
    }

    /**
     * 打赏我的徒弟
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("rewardDisciple")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = AccountRequest.class)
    public JsonEntity rewardDisciple(@RequestAttribute AccountRequest request) {
        if (null == request || null == request.getUserId()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        if (null == request.getRewardTv() || request.getRewardTv().compareTo(new BigDecimal(0)) <= 0) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.REWARD_INPUT_ERROR.getCode(),
                    ExceptionEnum.REWARD_INPUT_ERROR.getMessage());
        }
        if (request.getRewardTv().compareTo(new BigDecimal(50)) > 0) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.REWARD_MAX_ERROR.getCode(),
                    ExceptionEnum.REWARD_MAX_ERROR.getMessage());
        }
        if (StringUtils.isEmpty(request.getTxPassword())) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.VERIFY_TXPASS_ERROR.getCode(),
                    ExceptionEnum.VERIFY_TXPASS_ERROR.getMessage());
        }
        UserQueryResponse re = userService.getUserInfo(request.getRewardUid());
        if (re == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.REWARD_PERSON_ERROR.getCode(),
                    ExceptionEnum.REWARD_PERSON_ERROR.getMessage());
        }

        AccountFlowRequest accFlowRequest = new AccountFlowRequest();
        accFlowRequest.setRewardUid(request.getRewardUid());
        accFlowRequest.setUserId(request.getUserId());
        int tipsout = uccountFlowService.getRewardTips(accFlowRequest);
        logger.info("打赏次数：>>>>>>>>>" + tipsout);
        if (tipsout >= 1) {
            return JsonEntity.makeExceptionJsonEntity("", "您今天已经打赏Ta1次了，明天再来吧");
        }
        Result<AccountResponse> response = accountService.validationPwd(request);
        if (null != response && null != response.getData()) {
            BigDecimal balance = new BigDecimal(response.getData().getBalance());
            logger.info("查询账户余额：>>>>>>>>>" + balance);
            if (balance == null || request.getRewardTv().compareTo(balance) > 0) {
                return JsonEntity.makeExceptionJsonEntity("", "当前账户余额小于打赏金额");
            }
            if (null != response.getData().getAccountId()) {
                request.setBalance(request.getRewardTv());
                boolean SUCCESS = discipleService.rewardDisciple(request);
                if (SUCCESS) {
                    return JsonEntity.makeSuccessJsonEntity(SUCCESS);
                }
                return JsonEntity.makeExceptionJsonEntity("", "打赏失败");
            }
        }
        return JsonEntity.makeExceptionJsonEntity("", "交易密码验证失败");
    }

    /**
     * 我的徒弟进贡/师傅打赏提醒
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("inviteRemind")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, beanClazz = DiscipleRequest.class)
    public JsonEntity inviteRemind(@RequestAttribute DiscipleRequest request) {
        if (null == request || null == request.getUserId()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        DiscipleRemindRespose resp = discipleService.inviteRemind(request);
        if (resp == null || resp.getDiscipleReminder() == null) {
            resp = new DiscipleRemindRespose();
            resp.setDiscipleReminder(0L);
        }
        return JsonEntity.makeSuccessJsonEntity(resp);
    }

}
