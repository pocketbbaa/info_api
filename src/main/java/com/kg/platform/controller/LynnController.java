package com.kg.platform.controller;

import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.search.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.model.request.TxPasswordEditRequest;
import com.kg.platform.model.request.UserCertEditRequest;
import com.kg.platform.service.LynnService;

@RequestMapping("lynn")
@RestController
public class LynnController extends ApiBaseController {

    @Autowired
    private LynnService lynnService;

    /**
     * 设置交易密码
     */
    @RequestMapping(value = "setTxPassword", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = TxPasswordEditRequest.class)
    public JsonEntity setTxPassword(@RequestAttribute TxPasswordEditRequest request, @RequestAttribute UserkgResponse kguser) {
        return lynnService.setTxPassword(request,kguser);
    }

    /**
     * 获取授权码
     */
    @RequestMapping(value = "getAuthCode",method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = TxPasswordEditRequest.class)
    public JsonEntity getAuthCode(@RequestAttribute TxPasswordEditRequest request, @RequestAttribute UserkgResponse kguser){
        return lynnService.getWebTxPasswdCode(request,kguser);
    }

    /**
     * 修改交易密码
     */
    @RequestMapping(value = "updateTxPassword",method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = TxPasswordEditRequest.class)
    public JsonEntity updateTxPassword(@RequestAttribute TxPasswordEditRequest request, @RequestAttribute UserkgResponse kguser){
        return lynnService.updateTxPassword(request,kguser);
    }

    /**
     * 实名认证
     */
    @RequestMapping(value = "userCert", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = UserCertEditRequest.class)
    public JsonEntity userCert(@RequestAttribute UserCertEditRequest request) {
        return lynnService.userCert(request);
    }
}
