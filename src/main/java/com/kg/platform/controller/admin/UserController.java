package com.kg.platform.controller.admin;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.model.request.UserProfileRequest;
import com.kg.platform.model.request.admin.*;
import com.kg.platform.model.response.admin.ChannelResponse;
import com.kg.platform.model.response.admin.UserQueryResponse;
import com.kg.platform.service.admin.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController("AdminUserController")
@RequestMapping("admin/user")
public class UserController extends AdminBaseController {

    @Autowired
    private UserService userService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true)); // true:允许输入空值，false:不能为空值

    }

    /**
     * 获取用户列表(新版)
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "getUserList", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = UserQueryRequest.class)
    public JsonEntity getOptimizeUserList(@RequestAttribute UserQueryRequest request) throws Exception {
        return userService.getOptimizeUserList(request);
    }

    /**
     * 设置用户排序
     *
     * @return
     */
    @RequestMapping(value = "setUserOrder", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = UserQueryRequest.class)
    public JsonEntity setUserOrder(@RequestAttribute UserQueryRequest request) {
        return userService.setUserOrder(request);
    }

    /**
     * 获取渠道列表
     *
     * @param
     * @return
     */
    @RequestMapping(value = "getChannel", method = RequestMethod.POST)
    @BaseControllerNote()
    public JsonEntity getChannel() {
        List<ChannelResponse> list = userService.getChannel();
        return JsonEntity.makeSuccessJsonEntity(list);
    }

    @RequestMapping(value = "getInviteUserList", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = UserQueryRequest.class)
    public JsonEntity getInviteUserList(@RequestAttribute UserQueryRequest request) {
        PageModel<UserQueryResponse> page = new PageModel<>();
        page.setCurrentPage(request.getCurrentPage());
        page.setPageSize(request.getPageSize());
        request.setRelType(1);

        page = userService.getInviteUserList(request, page);
        return JsonEntity.makeSuccessJsonEntity(page);
    }

    @RequestMapping(value = "getSubUserList", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = UserQueryRequest.class)
    public JsonEntity getSubUserList(@RequestAttribute UserQueryRequest request) {
        PageModel<UserQueryResponse> page = new PageModel<>();
        page.setCurrentPage(request.getCurrentPage());
        page.setPageSize(request.getPageSize());
        page = userService.getInviteUserList(request, page);
        return JsonEntity.makeSuccessJsonEntity(page);
    }

    @RequestMapping(value = "unBindUser", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = UserQueryRequest.class)
    public JsonEntity unBindUser(@RequestAttribute UserQueryRequest request) {
        if (null != request.getUserId()) {
            boolean success = userService.unBindUser(Long.parseLong(request.getInviteUserId()),
                    Long.parseLong(request.getUserId()));
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

    @RequestMapping(value = "setHotUser", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = UserQueryRequest.class)
    public JsonEntity setHotUser(@RequestAttribute UserQueryRequest request) {
        if (null != request.getUserId()) {
            boolean success = userService.setHotUser(Long.parseLong(request.getUserId()), request.getHotUser(), request.getRankingList());
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

    @RequestMapping(value = "auditUser", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = AdminAuditUserRequest.class)
    public JsonEntity auditUser(@RequestAttribute AdminAuditUserRequest request) {
        if (!StringUtils.isBlank(request.getUserId()) && null != request.getAuditUserId()
                && null != request.getAuditStatus()) {
            Result<String> result = userService.auditUser(StringUtils.convertString2LongList(request.getUserId(), ","),
                    request.getAuditUserId(), request.getAuditStatus(), request.getRefuseReason());
            if ("true".equals(result.getData())) {
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

    @RequestMapping(value = "checkUser", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = AdminInviteUserRequest.class)
    public JsonEntity checkUser(@RequestAttribute AdminInviteUserRequest request) {
        if (StringUtils.isNotBlank(request.getUserId()) && null != request.getAuditUserId()) {
            boolean success = userService.checkUser(request.getUserId(), request.getAuditUserId(), 0);
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

    @RequestMapping(value = "freezeUser", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = AdminInviteUserRequest.class)
    public JsonEntity freezeUser(@RequestAttribute AdminInviteUserRequest request) {
        if (StringUtils.isNotBlank(request.getUserId()) && null != request.getAuditUserId()) {
            boolean success = userService.freezeUser(request.getUserId(), request.getAuditUserId(),
                    request.getBonusStatus(), request.getBonusFreezeReason());
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

    @RequestMapping(value = "lockUser", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = AdminUserLockRequest.class)
    public JsonEntity lockUser(@RequestAttribute AdminUserLockRequest request) {
        if (!StringUtils.isBlank(request.getUserId()) && null != request.getLockUserId()) {
            Result<String> result = userService.lockUser(StringUtils.convertString2LongList(request.getUserId(), ","),
                    request.getLockUserId(), request.getLockTime(), request.getLockUnit());
            if ("true".equals(result.getData())) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                        ExceptionEnum.SUCCESS.getMessage());
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                        ExceptionEnum.PARAMEMPTYERROR.getMessage());
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

    @RequestMapping(value = "getUserInfo", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = UserQueryRequest.class)
    public JsonEntity getUserInfo(@RequestAttribute UserQueryRequest request) {
        if (null != request.getUserId()) {
            UserQueryResponse response = userService.getUserInfo(Long.parseLong(request.getUserId()));
            if (null != response) {
                return JsonEntity.makeSuccessJsonEntity(response);
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                        ExceptionEnum.DATAERROR.getMessage());
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

    @RequestMapping(value = "getParentUserInfo", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = UserQueryRequest.class)
    public JsonEntity getParentUserInfo(@RequestAttribute UserQueryRequest request) {
        if (null != request.getUserId()) {
            UserQueryResponse response = userService.getParentUserInfo(Long.parseLong(request.getUserId()));

            return JsonEntity.makeSuccessJsonEntity(response);
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

    @RequestMapping(value = "loginSet", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = AdminLoginSetRequest.class)
    public JsonEntity loginSet(@RequestAttribute AdminLoginSetRequest request) {
        boolean success = userService.loginSet(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
    }

    @RequestMapping(value = "userInfoSet", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = AdminUserInfoSetRequest.class)
    public JsonEntity userInfoSet(@RequestAttribute AdminUserInfoSetRequest request) {
        boolean success = userService.userInfoSet(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
    }

    @RequestMapping(value = "getUserCert", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = UserCertQueryRequest.class)
    public JsonEntity getUserCert(@RequestAttribute UserCertQueryRequest request) {
        return JsonEntity.makeSuccessJsonEntity(userService.getUserCert(request));
    }

    @RequestMapping(value = "auditUserCert", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = UserCertEditRequest.class)
    public JsonEntity auditUserCert(@RequestAttribute UserCertEditRequest request) {
        return userService.auditUserCert(request);
    }

    @RequestMapping(value = "getUserId", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = UserQueryRequest.class)
    public JsonEntity getUserId(@RequestAttribute UserQueryRequest request) {
        if (null != request.getUserMobile()) {
            UserQueryResponse data = userService.getUserId(request);
            if (null != data) {
                return JsonEntity.makeSuccessJsonEntity(data);
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(), "该手机号不存在！");
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

    @RequestMapping(value = "getSiteInfo", method = RequestMethod.POST)
    public JsonEntity getSiteInfo() {
        return JsonEntity.makeSuccessJsonEntity(userService.getSiteInfo());
    }

    /**
     * 获取专栏身份
     *
     * @return
     */
    @RequestMapping(value = "getColumnIdentity", method = RequestMethod.POST)
    public JsonEntity getColumnIdentity() {
        return JsonEntity.makeSuccessJsonEntity(userService.getColumnIdentity());
    }

    /**
     * 认证专栏
     *
     * @return
     */
    @RequestMapping(value = "certificationColumn", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = CertificationColumnRequest.class)
    public JsonEntity certificationColumn(@RequestAttribute CertificationColumnRequest request) {
        if (request == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if (StringUtils.isEmpty(request.getUserId())) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if (StringUtils.isEmpty(request.getColumnIdentity())) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        return userService.certificationColumn(request);
    }

    /**
     * 取消专栏认证
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "cancelCertification", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = CertificationColumnRequest.class)
    public JsonEntity cancelCertification(@RequestAttribute CertificationColumnRequest request) {
        if (request == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if (StringUtils.isEmpty(request.getUserId())) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        return userService.cancelCertification(request);
    }

    /**
     * 广告中心查询作者列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "getAuthors", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = UserQueryRequest.class)
    public JsonEntity getAuthors(@RequestAttribute UserQueryRequest request) {
        if (request == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if (StringUtils.isEmpty(request.getUserName())) {
            return JsonEntity.makeSuccessJsonEntity("");
        }
        return JsonEntity.makeSuccessJsonEntity(userService.getAuthorList(request));
    }

    /**
     * 更新专栏名称
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "updateColumnName", method = RequestMethod.POST)
    @ResponseBody
    @BaseControllerNote(beanClazz = UserProfileRequest.class)
    public JsonEntity updateColumnName(@RequestAttribute UserProfileRequest request) {
        if (request == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if (request.getUserId() == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }

        return userService.updateColumnName(request);
    }

    @RequestMapping(value = "getUserAccountInfo", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = UserQueryRequest.class)
    public JsonEntity getUserAccountInfo(@RequestAttribute UserQueryRequest request) {
        return userService.getUserAccountInfo(request);
    }

    /**
     * 获取专栏等级列表
     *
     * @return
     */
    @RequestMapping(value = "getColumnLevelList", method = RequestMethod.POST)
    public JsonEntity getColumnLevelList() {
        return userService.getColumnLevelList();
    }

    /**
     * 修改专栏等级
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "updateColumnLevel", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = UserQueryRequest.class)
    public JsonEntity updateColumnLevel(@RequestAttribute UserQueryRequest request) {
        return userService.updateColumnLevel(request);
    }
}
