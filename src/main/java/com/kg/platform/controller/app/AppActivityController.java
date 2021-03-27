package com.kg.platform.controller.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.model.request.DeepfinRequest;
import com.kg.platform.model.request.KgActivityCompetionRequest;
import com.kg.platform.model.request.KgActivityGuessRequest;
import com.kg.platform.model.request.KgActivityMinerRequest;
import com.kg.platform.model.request.KgMinerAssistRequest;
import com.kg.platform.model.request.KgMinerRobRequest;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.app.AppActivityService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2018/5/18.
 */
@Controller
@RequestMapping("/kgApp")
public class AppActivityController {
    @Autowired
    private AppActivityService appActivityService;

    @ResponseBody
    @RequestMapping("/miner/minerList")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = false)
    public JSONObject minerList() {
        return AppJsonEntity.jsonFromObject(appActivityService.minerList());
    }

    /**
     * 抢购矿机
     *
     * @param kguser
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/miner/rushToMiner")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, isLogin = true, beanClazz = KgActivityMinerRequest.class)
    public JSONObject rushToMiner(@RequestAttribute UserkgResponse kguser,
            @RequestAttribute KgActivityMinerRequest request) {
        return AppJsonEntity.jsonFromObject(appActivityService.rushToMiner(kguser, request));
    }

    /**
     * 验证是否抢购过矿机或者是否登录
     *
     * @param kguser
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/miner/validRushMiner")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, isLogin = true, beanClazz = KgActivityMinerRequest.class)
    public JSONObject validRushMiner(@RequestAttribute UserkgResponse kguser,
            @RequestAttribute KgActivityMinerRequest request) {
        return AppJsonEntity.jsonFromObject(appActivityService.validRushMiner(kguser, request));
    }

    @ResponseBody
    @RequestMapping("/miner/myRobList")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = false)
    public JSONObject myRobList(@RequestAttribute UserkgResponse kguser) {
        return AppJsonEntity.jsonFromObject(appActivityService.myRobList(kguser));
    }

    /**
     * 好友助力
     *
     * @param kguser
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/miner/friendHelp")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, isLogin = true, beanClazz = KgActivityMinerRequest.class)
    public JSONObject friendHelp(@RequestAttribute UserkgResponse kguser,
            @RequestAttribute KgActivityMinerRequest request) {
        return AppJsonEntity.jsonFromObject(appActivityService.friendHelp(kguser, request));
    }

    @ResponseBody
    @RequestMapping("/miner/detailRob")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = KgMinerRobRequest.class)
    public JSONObject detailRob(@RequestAttribute KgMinerRobRequest request) {
        return AppJsonEntity.jsonFromObject(appActivityService.detailRob(request));
    }

    @ResponseBody
    @RequestMapping("/miner/assistList")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = KgMinerAssistRequest.class)
    public JSONObject assistList(@RequestAttribute KgMinerAssistRequest request) {
        return AppJsonEntity.jsonFromObject(appActivityService.assistList(request));
    }

    @ResponseBody
    @RequestMapping("/miner/minerTime")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = false)
    public JSONObject minerTime() {
        return AppJsonEntity.jsonFromObject(appActivityService.minerTime());
    }

    @ResponseBody
    @RequestMapping("/miner/minerProgressList")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = KgMinerAssistRequest.class)
    public JSONObject minerProgressList(@RequestAttribute KgMinerAssistRequest request) {
        return AppJsonEntity.jsonFromObject(appActivityService.minerProgressList(request));
    }

    @ResponseBody
    @RequestMapping("/activity/V2/activityPopInfo")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = false)
    public JSONObject activityPopInfo(HttpServletRequest request) {
        return AppJsonEntity.jsonFromObject(appActivityService.activityPopInfo(request));
    }

    @ResponseBody
    @RequestMapping("/worldCup/worldCupTime")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = false)
    public JSONObject worldCupTime() {
        return AppJsonEntity.jsonFromObject(appActivityService.worldCupTime());
    }

    /**
     * 验证当天是否有赛事
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/worldCup/checkMatch")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = KgActivityCompetionRequest.class)
    public JSONObject checkMatch(@RequestAttribute KgActivityCompetionRequest request) {
        return AppJsonEntity.jsonFromObject(appActivityService.checkMatch(request));
    }

    /**
     * 竞猜
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/worldCup/guessWinner")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = KgActivityGuessRequest.class)
    public JSONObject guessWin(@RequestAttribute KgActivityGuessRequest request,
            @RequestAttribute UserkgResponse kguser) {
        request.setUserId(Long.valueOf(kguser.getUserId()));
        return AppJsonEntity.jsonFromObject(appActivityService.guessWinner(request));
    }

    /**
     * 助力详情
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/worldCup/assistDetail")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = KgActivityCompetionRequest.class)
    public JSONObject assistDetail(@RequestAttribute KgActivityCompetionRequest request) {
        return AppJsonEntity.jsonFromObject(appActivityService.assistDetail(request));
    }

    @ResponseBody
    @RequestMapping("/worldCup/worldCupCompetionList")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = false)
    public JSONObject worldCupCompetionList(@RequestAttribute UserkgResponse kguser) {
        KgActivityCompetionRequest request = new KgActivityCompetionRequest();
        request.setUserId(Long.parseLong(kguser.getUserId()));
        return AppJsonEntity.jsonFromObject(appActivityService.worldCupCompetionList(request));
    }

    /**
     * 自动注册请求
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/worldCup/deepfinRegist")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = DeepfinRequest.class)
    public JSONObject deepfinRegist(@RequestAttribute DeepfinRequest request) {
        return AppJsonEntity.jsonFromObject(appActivityService.deepfinRequest(request));
    }

}
