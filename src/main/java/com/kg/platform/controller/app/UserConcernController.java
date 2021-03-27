package com.kg.platform.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.request.UserConcernRequest;
import com.kg.platform.model.response.HotAuthorsResponse;
import com.kg.platform.model.response.UserConcernListResponse;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.app.UserConcernService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2018/5/18.
 */
@Controller
@RequestMapping("/kgApp/concern")
public class UserConcernController {

    @Autowired
    private UserConcernService userConcernService;

    /**
     * 关注/取消关注用户
     *
     * @param kguser
     * @return
     */
    @ResponseBody
    @RequestMapping("/concernUser")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, isLogin = false, beanClazz = UserConcernRequest.class)
    public JSONObject concernUser(@RequestAttribute UserkgResponse kguser,
                                  @RequestAttribute UserConcernRequest request) {
        return AppJsonEntity.jsonFromObject(userConcernService.concernUser(kguser, request));
    }

    /**
     * 我的关注列表
     *
     * @param kguser
     * @return
     */
    @ResponseBody
    @RequestMapping("/concernList")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, isLogin = false, beanClazz = PageModel.class)
    public JSONObject concernList(@RequestAttribute UserkgResponse kguser,
                                  @RequestAttribute PageModel<UserConcernListResponse> request) {
        return AppJsonEntity.jsonFromObject(userConcernService.concernList(kguser, request));
    }

    /**
     * 我的粉丝列表
     *
     * @param kguser
     * @return
     */
    @ResponseBody
    @RequestMapping("/fansList")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, isLogin = false, beanClazz = PageModel.class)
    public JSONObject fansList(@RequestAttribute UserkgResponse kguser,
                               @RequestAttribute PageModel<UserConcernListResponse> request) {
        return AppJsonEntity.jsonFromObject(userConcernService.fansList(kguser, request));
    }

    /**
     * 关注用户提醒
     *
     * @param kguser
     * @return
     */
    @ResponseBody
    @RequestMapping("/concernReminder")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, isLogin = false)
    public JSONObject concernReminder(@RequestAttribute UserkgResponse kguser) {
        return AppJsonEntity.jsonFromObject(userConcernService.concernReminder(kguser));
    }


    /**
     * 注册成功后作者推荐热门作者
     *
     * @param kguser
     * @return
     */
    @ResponseBody
    @RequestMapping("/recommendHotAuthors")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, isLogin = false, beanClazz = PageModel.class)
    public JSONObject recommendHotAuthors(@RequestAttribute UserkgResponse kguser,
                                          @RequestAttribute PageModel<HotAuthorsResponse> request) {
        request.setPageSize(27);
        return AppJsonEntity.jsonFromObject(userConcernService.recommendHotAuthors(kguser, request));
    }

    /**
     * 热门作者列表
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/hotAuthorList")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, isLogin = false, beanClazz = UserConcernRequest.class)
    public JSONObject hotAuthorList(@RequestAttribute UserConcernRequest request, PageModel<UserConcernListResponse> page) {
        return AppJsonEntity.jsonFromObject(userConcernService.hotAuthorList(request, page));
    }

    /**
     * 搜索作者
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/searchAuthorList")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, isLogin = false, beanClazz = UserConcernRequest.class)
    public AppJsonEntity searchAuthorList(@RequestAttribute UserConcernRequest request, PageModel<UserConcernListResponse> page) {
        return AppJsonEntity.makeSuccessJsonEntity(userConcernService.searchAuthorList(request, page));
    }

}
