package com.kg.platform.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.Constants;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.common.utils.JsonUtil;
import com.kg.platform.enumeration.LockStatusEnum;
import com.kg.platform.model.in.LotteryInModel;
import com.kg.platform.model.out.LotteryOutModel;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.UserkgRequest;
import com.kg.platform.model.response.InviteUserResponse;
import com.kg.platform.model.response.JayCherrDetailResponse;
import com.kg.platform.service.ActivityService;
import com.kg.platform.service.UserRelationService;
import com.kg.platform.service.UserkgService;

@Controller
@RequestMapping("activitys")
public class JayActivitysController extends ApiBaseController {

    private static final Logger logger = LoggerFactory.getLogger(JayActivitysController.class);

    @Inject
    UserRelationService userRelationService;

    @Inject
    UserkgService userkgService;

    @Inject
    ActivityService activityService;

    /**
     * 周杰伦活动助力详情用户页面
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("jayZhouCheerDetail")
    @BaseControllerNote(needCheckToken = true, isLogin = true, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JsonEntity jayZhouCheerDetail(@RequestAttribute UserkgRequest request) {
        if (null == request || null == request.getUserId()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        // 抽奖还未开始
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = simpleDateFormat.parse(Constants.ACTVITY_START);
            endDate = simpleDateFormat.parse(Constants.ACTVITY_END);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (DateUtils.compareDate(new Date(), startDate) < 0) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACTIVITY_START.getCode(),
                    ExceptionEnum.ACTIVITY_START.getMessage());

        }
        if (DateUtils.compareDate(new Date(), endDate) > 0) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACTIVITY_END.getCode(),
                    ExceptionEnum.ACTIVITY_END.getMessage());
        }

        if (null == request || null == request.getUserId()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        UserkgRequest userkgRequest = new UserkgRequest();
        userkgRequest.setUserId(request.getUserId());
        UserkgOutModel userkgOutModel = userkgService.getUserProfiles(userkgRequest);
        logger.info("查询个人返回信息>>>>>>>>>" + JsonUtil.writeValueAsString(userkgOutModel));

        if (userkgOutModel == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        if (userkgOutModel.getLockStatus().intValue() == LockStatusEnum.LOCKED.getStatus()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.LOCKERROR.getCode(),
                    ExceptionEnum.LOCKERROR.getMessage());
        }
        if (userkgOutModel.getBonusStatus() == 0 || userkgOutModel.getBonusStatus() == 2) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.BONUS_FREEZE_ERROR.getCode(),
                    ExceptionEnum.BONUS_FREEZE_ERROR.getMessage());
        }
        request.setActivityId(1);
        request.setStartTime(Constants.ACTVITY_START);
        request.setEndTime(Constants.ACTVITY_END);
        List<InviteUserResponse> inviteUsers = userRelationService.getJayActivityInviteUsers(request);

        logger.info("查询我的邀请列表>>>>>>>>>" + JsonUtil.writeValueAsString(inviteUsers));

        // 返回信息
        JayCherrDetailResponse jayCherrDetailResponse = new JayCherrDetailResponse();
        jayCherrDetailResponse.setInviteUsers(inviteUsers);
        jayCherrDetailResponse.setImageLink(propertyLoader.getProperty("platform", "global.imgServer"));
        jayCherrDetailResponse.setHeadImage(userkgOutModel.getAvatar());
        jayCherrDetailResponse.setInviteCode(userkgOutModel.getInviteCode());
        jayCherrDetailResponse.setNickName(userkgOutModel.getUserName());
        jayCherrDetailResponse.setInviteNum(inviteUsers.size());
        jayCherrDetailResponse.setIsDraw(0);
        LotteryInModel lotteryInModel = new LotteryInModel();
        lotteryInModel.setActivityId(1);
        lotteryInModel.setUserId(request.getUserId());
        LotteryOutModel lotteryOutModel = activityService.getLotteryOut(lotteryInModel);
        if (lotteryOutModel != null) {
            jayCherrDetailResponse.setIsDraw(1);
            jayCherrDetailResponse.setPrizeIndex(lotteryOutModel.getPrizeId());
        }
        logger.info("接口返回信息>>>>>>>>>" + JsonUtil.writeValueAsString(jayCherrDetailResponse));
        return JsonEntity.makeSuccessJsonEntity(jayCherrDetailResponse);
    }

    /**
     * 周杰伦活动助力详情登录页面
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("jayCheerDetail")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JsonEntity jayCheerDetail(@RequestAttribute UserkgRequest request) {
        if (null == request || null == request.getUserId()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }

        // 抽奖还未开始
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = simpleDateFormat.parse(Constants.ACTVITY_START);
            endDate = simpleDateFormat.parse(Constants.ACTVITY_END);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (DateUtils.compareDate(new Date(), startDate) < 0) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACTIVITY_START.getCode(),
                    ExceptionEnum.ACTIVITY_START.getMessage());

        }
        if (DateUtils.compareDate(new Date(), endDate) > 0) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACTIVITY_END.getCode(),
                    ExceptionEnum.ACTIVITY_END.getMessage());
        }

        if (null == request || null == request.getUserId()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }

        UserkgRequest userkgRequest = new UserkgRequest();
        userkgRequest.setUserId(request.getUserId());
        UserkgOutModel userkgOutModel = userkgService.getUserProfiles(userkgRequest);
        logger.info("查询个人返回信息>>>>>>>>>" + JsonUtil.writeValueAsString(userkgOutModel));

        if (userkgOutModel == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        if (userkgOutModel.getLockStatus().intValue() == LockStatusEnum.LOCKED.getStatus()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.LOCKERROR.getCode(),
                    ExceptionEnum.LOCKERROR.getMessage());
        }
        if (userkgOutModel.getBonusStatus() == 0 || userkgOutModel.getBonusStatus() == 2) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.BONUS_FREEZE_ERROR.getCode(),
                    ExceptionEnum.BONUS_FREEZE_ERROR.getMessage());
        }

        request.setStartTime(Constants.ACTVITY_START);
        request.setEndTime(Constants.ACTVITY_END);
        request.setActivityId(1);
        List<InviteUserResponse> inviteUsers = userRelationService.getJayActivityInviteUsers(request);
        logger.info("查询我的邀请列表>>>>>>>>>" + JsonUtil.writeValueAsString(inviteUsers));

        // 返回信息
        JayCherrDetailResponse jayCherrDetailResponse = new JayCherrDetailResponse();
        jayCherrDetailResponse.setInviteUsers(inviteUsers);
        jayCherrDetailResponse.setImageLink(propertyLoader.getProperty("platform", "global.imgServer"));
        jayCherrDetailResponse.setHeadImage(userkgOutModel.getAvatar());
        jayCherrDetailResponse.setInviteCode(userkgOutModel.getInviteCode());
        jayCherrDetailResponse.setNickName(userkgOutModel.getUserName());
        jayCherrDetailResponse.setInviteNum(inviteUsers.size());
        logger.info("接口返回信息>>>>>>>>>" + JsonUtil.writeValueAsString(jayCherrDetailResponse));
        return JsonEntity.makeSuccessJsonEntity(jayCherrDetailResponse);
    }

    /**
     * 抽奖
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("getPrize")
    @BaseControllerNote(needCheckToken = true, isLogin = true, needCheckParameter = true, beanClazz = UserkgRequest.class)
    public JsonEntity getPrize(@RequestAttribute UserkgRequest request) {
        if (null == request || null == request.getUserId()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        // 抽奖还未开始
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = simpleDateFormat.parse(Constants.ACTVITY_START);
            endDate = simpleDateFormat.parse(Constants.ACTVITY_END);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (DateUtils.compareDate(new Date(), startDate) < 0) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACTIVITY_START.getCode(),
                    ExceptionEnum.ACTIVITY_START.getMessage());

        }
        if (DateUtils.compareDate(new Date(), endDate) > 0) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACTIVITY_END.getCode(),
                    ExceptionEnum.ACTIVITY_END.getMessage());
        }

        if (null == request || null == request.getUserId()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }

        // 判断用户账号状态
        UserkgRequest userkgRequest = new UserkgRequest();
        userkgRequest.setUserId(request.getUserId());
        UserkgOutModel userkgOutModel = userkgService.getUserProfiles(userkgRequest);
        logger.info("查询个人返回信息>>>>>>>>>" + JsonUtil.writeValueAsString(userkgOutModel));

        if (userkgOutModel == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        if (userkgOutModel.getLockStatus().intValue() == LockStatusEnum.LOCKED.getStatus()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.LOCKERROR.getCode(),
                    ExceptionEnum.LOCKERROR.getMessage());
        }
        if (userkgOutModel.getBonusStatus() == 0 || userkgOutModel.getBonusStatus() == 2) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.BONUS_FREEZE_ERROR.getCode(),
                    ExceptionEnum.BONUS_FREEZE_ERROR.getMessage());
        }

        // 查询人数是否邀请超过10人
        userkgRequest.setStartTime(Constants.ACTVITY_START);
        userkgRequest.setEndTime(Constants.ACTVITY_END);
        userkgRequest.setActivityId(1);
        List<InviteUserResponse> users = userRelationService.getJayActivityInviteUsers(userkgRequest);
        if (users.size() < Constants.INVITE_COUNT) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACTIVITY_INVITE_ERROR.getCode(),
                    ExceptionEnum.ACTIVITY_INVITE_ERROR.getMessage());
        }
        // 查询是否已经抽过奖
        LotteryInModel lotteryInModel = new LotteryInModel();
        lotteryInModel.setActivityId(1);
        lotteryInModel.setUserId(request.getUserId());
        LotteryOutModel lotteryOutModel = activityService.getLotteryOut(lotteryInModel);
        if (lotteryOutModel != null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PRIZEDRAWED.getCode(),
                    ExceptionEnum.PRIZEDRAWED.getMessage());
        }
        return JsonEntity.makeSuccessJsonEntity(activityService.getPrize(request));
    }

}
