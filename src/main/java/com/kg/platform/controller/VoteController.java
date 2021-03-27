package com.kg.platform.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.model.in.UserInModel;
import com.kg.platform.model.in.VoteInModel;
import com.kg.platform.model.request.UserkgRequest;
import com.kg.platform.model.request.VoteRequest;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.model.response.VoteResponse;
import com.kg.platform.service.AccountService;
import com.kg.platform.service.UserkgService;
import com.kg.platform.service.VoteService;
import com.kg.platform.service.admin.UserService;

@Controller
@RequestMapping("vote")
public class VoteController extends ApiBaseController {

    @Inject
    VoteService voteService;

    @Inject
    UserkgService userkgService;

    @Inject
    UserService userService;

    @Inject
    AccountService accountService;

    /**
     * 投票
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("vote")
    @BaseControllerNote(needCheckParameter = true, isLogin = true, beanClazz = VoteRequest.class)
    public JsonEntity vote(@RequestAttribute VoteRequest request) {
        if (null == request.getUserId() || null == request.getVoteId() || null == request.getVoteNum()
                || request.getVoteNum() <= 0) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        VoteInModel voteInModel = new VoteInModel();
        voteInModel.setUserId(request.getUserId());
        voteInModel.setVoteId(request.getVoteId());
        voteInModel.setVoteNum(request.getVoteNum());
        // 验证用户是否存在
        UserkgRequest userkgRequest = new UserkgRequest();
        userkgRequest.setUserId(request.getUserId());
        Result<UserkgResponse> userp = userkgService.getUserDetails(userkgRequest);
        if (userp.getData() == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        // 验证用户是否被锁定 userkgService.getUserDetails(request.getUserId());
        UserInModel userInModel = new UserInModel();
        userInModel.setUserId(request.getUserId());
        if (userkgService.checkUserIsLock(userInModel)) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.LOCKERROR.getCode(),
                    ExceptionEnum.LOCKERROR.getMessage());
        }
        ;
        // 验证用户是否钛值超过100余额不足 跳转充值
        if (!accountService.checkTvAmount(request.getUserId())) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACCOUNT_BALANCE_ERROR.getCode(),
                    ExceptionEnum.ACCOUNT_BALANCE_ERROR.getMessage());
        }
        // 验证用户凌晨4点到第二天凌晨4点投票数是否超过一万
        if (!voteService.checkVoteNum(voteInModel)) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.MAX_VOTE_ERROR.getCode(),
                    ExceptionEnum.MAX_VOTE_ERROR.getMessage());
        }

        // 投票
        Boolean isok = voteService.vote(voteInModel);
        if (isok) {
            return JsonEntity.makeSuccessJsonEntity("投票成功");
        }
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                ExceptionEnum.DATAERROR.getMessage());
    }

    /**
     * 投票列表
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("voteList")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = VoteRequest.class)
    public JsonEntity voteList(@RequestAttribute VoteRequest request) {
        VoteInModel voteInModel = new VoteInModel();
        voteInModel.setActivityId(request.getActivityId());
        List<VoteResponse> votes = voteService.getVoteList(voteInModel);
        return JsonEntity.makeSuccessJsonEntity(votes);
    }

}
