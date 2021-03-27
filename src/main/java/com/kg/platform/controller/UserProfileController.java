package com.kg.platform.controller;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.CheckUtils;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.enumeration.AuditStatusEnum;
import com.kg.platform.model.request.UserProfileRequest;
import com.kg.platform.model.request.UserkgRequest;
import com.kg.platform.model.response.UserProfileResponse;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.UserProfileService;
import com.kg.platform.service.UserkgService;

@Controller
@RequestMapping("userprofile")
public class UserProfileController extends ApiBaseController {

    @Inject
    UserProfileService userProfileService;

    @Inject
    UserkgService userkgService;

    @Inject
    protected JedisUtils jedisUtils;

    /**
     * 他人主页
     */
    @ResponseBody
    @RequestMapping("getByIdProfile")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserProfileRequest.class)
    public JsonEntity getByIdProfile(@RequestAttribute UserProfileRequest request) {
        Result<UserProfileResponse> response = userProfileService.getByIdProfile(request);
        if (response.getData() != null) {
            return JsonEntity.makeSuccessJsonEntity(response);
        }
        return JsonEntity.makeExceptionJsonEntity("", "获取数据有误");

    }

    /**
     * 详情页面右侧作者模块（√.）
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("selectByuserprofileid")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserProfileRequest.class)
    public JsonEntity selectByuserprofileId(@RequestAttribute UserProfileRequest request) {

        Result<UserProfileResponse> responses = userProfileService.selectByuserprofileId(request);
        if (null != responses) {
            return JsonEntity.makeSuccessJsonEntity(responses);
        }
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                ExceptionEnum.DATAERROR.getMessage());

    }

    /**
     * 首页右侧热门作者（√.）
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("getUserprofile")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserProfileRequest.class)
    public JsonEntity getUserproFile() {
        Result<List<UserProfileResponse>> listResponse = userProfileService.getUserproFile();
        if (null != listResponse) {
            return JsonEntity.makeSuccessJsonEntity(listResponse);
        }
        return JsonEntity.makeExceptionJsonEntity("", "获取数据有误");

    }

    /**
     * 验证是否申请过专栏
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("checkProfile")
    @BaseControllerNote(needCheckParameter = true, beanClazz = UserProfileRequest.class)
    public JsonEntity checkProfile(@RequestAttribute UserProfileRequest request) {
        UserkgResponse responsekg = new UserkgResponse();
        UserkgRequest urequesta = new UserkgRequest();
        urequesta.setUserId(request.getUserId());
        Result<UserkgResponse> responses = userkgService.getUserDetails(urequesta);
        CheckUtils.checkRetInfo(responses, true);

        int auditStatus = responses.getData().getAuditStatus();
        // 判断是在申请中返回资料
        if (auditStatus == AuditStatusEnum.AUDITING.getStatus() || auditStatus == AuditStatusEnum.REFUSE.getStatus()) {
            UserkgRequest urequest = new UserkgRequest();
            urequest.setUserId(request.getUserId());
            Result<UserkgResponse> response = userkgService.selectApply(urequest);
            responsekg.setResponse(response.getData());
            if (response.getData() != null) {
                if ("4".equals(response.getData().getApplyRole())) {
                    UserkgResponse userkgResponse = new UserkgResponse();
                    String depositAmount = propertyLoader.getProperty("common", "global.userDepositAmount");
                    userkgResponse.setRemainingAmount(depositAmount);
                }
                return JsonEntity.makeSuccessJsonEntity(responsekg);
            }
        }

        if (responses.getData().getUserRole() == 1) {
            responsekg.setUserStatus(ExceptionEnum.USER_PROFILE.getCode());
            return JsonEntity.makeSuccessJsonEntity(responsekg);
        }

        UserProfileRequest requestfile = new UserProfileRequest();
        requestfile.setUserId(Long.parseLong(request.getUserId().toString()));
        boolean SUCCESS = userProfileService.countArticle(request);
        if (SUCCESS == false) {
            responsekg.setUserStatus(ExceptionEnum.USER_ARTICLE.getCode());
            return JsonEntity.makeSuccessJsonEntity(responsekg);
        }
        responsekg.setUserStatus(ExceptionEnum.USER_ARTICLECOUNT.getCode());

        return JsonEntity.makeSuccessJsonEntity(responsekg);

    }

    /**
     * 申请专栏
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("addUserProfile")
    @BaseControllerNote(needCheckParameter = true, beanClazz = UserProfileRequest.class)
    public JsonEntity addUserProfile(@RequestAttribute UserProfileRequest request) {

        if (request.getUserId() == null) {
            return JsonEntity.makeExceptionJsonEntity("", "请先登录");
        }
        if (request.getCode() == null && "".equals(request.getCode())) {
            return JsonEntity.makeExceptionJsonEntity("", "请输入验证码");
        }

        if (null != request.getCode()) {
            Result<String> checkcode = userkgService.checkSmsEmailcode(request.getMobile());
            if (checkcode.getData() == null) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USER_VALIDATE.getCode(),
                        ExceptionEnum.USER_VALIDATE.getMessage());
            } else if (!checkcode.getData().equals(request.getCode())) {
                return JsonEntity.makeExceptionJsonEntity("", "验证码有误");
            }
        }

        boolean success = userProfileService.addUserProfile(request);
        if (success) {
            if (request.getRoleId() == 4) {

                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(), "申请企业角色还需缴纳"
                        + propertyLoader.getProperty("common", "global.userDepositAmount") + "TV的钛值作为账号保证金，请转入此地址:"
                        + propertyLoader.getProperty("common", "global.userDepositAddress") + "转账时请务必在备注中填写您的手机号");
            }
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        }
        return JsonEntity.makeExceptionJsonEntity("", "保存失败");

    }

    /**
     * 获取基本资料
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("getProfile")
    @BaseControllerNote(needCheckParameter = true, beanClazz = UserProfileRequest.class)
    public JsonEntity getProfile(@RequestAttribute UserProfileRequest request, HttpServletRequest req) {
        Result<UserProfileResponse> result = userProfileService.getProfile(request, req);
        if (request != null) {
            return JsonEntity.makeSuccessJsonEntity(result);
        } else {
            return JsonEntity.makeSuccessJsonEntity("", "请先申请专栏");
        }

    }

    /**
     * 修改基本资料
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("updateProfile")
    @BaseControllerNote(needCheckParameter = true, beanClazz = UserProfileRequest.class)
    public JsonEntity updateProfile(@RequestAttribute UserProfileRequest request) {
        boolean success = userProfileService.updateProfile(request);
        if (success) {
            return JsonEntity.makeSuccessJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        }
        return JsonEntity.makeExceptionJsonEntity("", "修改失败");

    }

    /***
     * auditStatus 0:申请中，1申请通过
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("getColumn")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = UserProfileRequest.class)
    public JsonEntity getColumn(@RequestAttribute UserProfileRequest request) {
        boolean result = userProfileService.checkProfile(request);
        if (result == false) {
            return JsonEntity.makeSuccessJsonEntity("", "请申请专栏");
        }
        if ("0".equals(request.getAuditStatus())) {
            UserkgRequest urequest = new UserkgRequest();
            urequest.setUserId(request.getUserId());
            Result<UserkgResponse> response = userkgService.selectApply(urequest);
            if (response.getData() != null) {
                return JsonEntity.makeSuccessJsonEntity(response);
            }

        }

        return JsonEntity.makeExceptionJsonEntity("", "修改失败");

    }

}
