package com.kg.platform.controller.app;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.app.AppAccountConvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/kgApp/account/convert")
public class AppAccountConvertController {

    @Autowired
    private AppAccountConvertService appAccountConvertService;

    /**
     * kg兑换rit首页
     *
     * @param kguser
     * @return
     */
    @RequestMapping("kgToRitIndex")
    @ResponseBody
    @BaseControllerNote(needCheckToken = true, needCheckParameter = false)
    public AppJsonEntity kgToRitIndex(@RequestAttribute UserkgResponse kguser) {
        return appAccountConvertService.kgToRitIndex(kguser);
    }

    /**
     * kg兑换rit,校验
     *
     * @param kguser
     * @return
     */
    @RequestMapping("kgToRitCheck")
    @ResponseBody
    @BaseControllerNote(needCheckToken = true, needCheckParameter = false)
    public AppJsonEntity kgToRitCheck(@RequestAttribute UserkgResponse kguser) {
        return appAccountConvertService.kgToRitCheck(kguser);
    }

    /**
     * kg兑换rit
     *
     * @param kguser
     * @return
     */
    @RequestMapping("kgToRit")
    @ResponseBody
    @BaseControllerNote(needCheckToken = true, needCheckParameter = false)
    public AppJsonEntity kgToRit(@RequestAttribute UserkgResponse kguser) {
        try {
            return appAccountConvertService.kgToRit(kguser);
        } catch (Exception e) {
            e.printStackTrace();
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.CONVERT_SYSTEM_ERROR);
        }
    }
}
