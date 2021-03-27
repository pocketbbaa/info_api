package com.kg.platform.controller.admin;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.model.request.admin.*;
import com.kg.platform.model.response.admin.UserQueryResponse;
import com.kg.platform.service.admin.BonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 奖励相关
 */
@RestController("AdminBonusController")
@RequestMapping("admin/bonus")
public class BonusController extends AdminBaseController {

    @Autowired
    private BonusService bonusService;

    @RequestMapping(value = "getInviteBonus")
    @BaseControllerNote(beanClazz = BonusQueryRequest.class)
    public JsonEntity getInviteBonus(@RequestAttribute BonusQueryRequest request) {
        return JsonEntity.makeSuccessJsonEntity(bonusService.getInviteBonus(request));
    }

    /**
     * 实名认证奖励
     * @param request
     * @return
     */
    @RequestMapping(value = "realnameBonus")
    @BaseControllerNote(beanClazz = BonusQueryRequest.class)
    public JsonEntity realnameBonus(@RequestAttribute BonusQueryRequest request) {
        return JsonEntity.makeSuccessJsonEntity(bonusService.realnameBonus(request));
    }

    /**
     * 专栏认证通过奖励
     * @param request
     * @return
     */
    @RequestMapping(value = "userColumnBonus")
    @BaseControllerNote(beanClazz = BonusQueryRequest.class)
    public JsonEntity userColumnBonus(@RequestAttribute BonusQueryRequest request) {
        return JsonEntity.makeSuccessJsonEntity(bonusService.userColumnBonus(request));
    }

    /**
     * 发文奖励
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "publishArticleBonus")
    @BaseControllerNote(beanClazz = PublishBonusQueryRequest.class)
    public JsonEntity publishArticleBonus(@RequestAttribute PublishBonusQueryRequest request) {
        return JsonEntity.makeSuccessJsonEntity(bonusService.publishArticleBonus(request));
    }

    /**
     * 分享奖励
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "shareArticleBonus")
    @BaseControllerNote(beanClazz = ShareBonusQueryRequest.class)
    public JsonEntity shareArticleBonus(@RequestAttribute ShareBonusQueryRequest request) {
        return JsonEntity.makeSuccessJsonEntity(bonusService.shareArticleBonus(request));
    }

    /**
     * 检索发放用户奖励 财富管理-用户奖励-新增奖励-检索用户
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "checkInfo", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = UserBonusQueryRequest.class)
    public JsonEntity checkInfo(@RequestAttribute UserBonusQueryRequest request) {
        Result<List<UserQueryResponse>> data = bonusService.getUserBonusInfos(request);
        if (data.getErrorCode() != 10000) {
            return JsonEntity.makeExceptionJsonEntity(data.getErrorCode(), data.getErrorMsg());
        }
        return JsonEntity.makeSuccessJsonEntity(data.getData());
    }

    /**
     * 确认发放用户奖励  财富管理-用户奖励-新增奖励-确认发放
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "confirmBonus", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = UserBonusQueryRequest.class)
    public JsonEntity confirmBonus(@RequestAttribute UserBonusQueryRequest request, HttpServletRequest servletRequest) {
        Result<UserQueryResponse> res = bonusService.confirmBonus(request, servletRequest);
        if (res.getErrorCode() == 1000) {
            return JsonEntity.makeSuccessJsonEntity("发放成功");
        }
        return JsonEntity.makeExceptionJsonEntity(res.getErrorCode(), res.getErrorMsg());
    }

    /**
     * 确认发放用户奖励ForFile
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "confirmBonus/forFile", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = UserBonusListRequest.class)
    public JsonEntity confirmBonusForFile(@RequestAttribute UserBonusListRequest request) {

        Result<UserQueryResponse> res = bonusService.confirmBonusForFile(request);
        if (res.getErrorCode() == 1000) {
            return JsonEntity.makeSuccessJsonEntity("发放成功");
        }
        return JsonEntity.makeExceptionJsonEntity(res.getErrorCode(), res.getErrorMsg());
    }


    /**
     * 上传Excel返回用户待奖励
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "bonusList/upload/", method = RequestMethod.POST, produces = "application/json")
    @BaseControllerNote(needCheckToken = false, isLogin = false)
    public JsonEntity bonusListUpload(@RequestParam("file") MultipartFile[] files) {
        return bonusService.bonusListUpload(files);
    }
}
