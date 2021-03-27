package com.kg.platform.service.app;

import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.model.request.UserkgRequest;
import com.kg.platform.model.response.UserkgResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2018/3/20.
 */
public interface UserKgAppService {
    void pushBindInfo(HttpServletRequest servletRequest,String userId);

    void mqPush(HttpServletRequest servletRequest, String userId, int type, String tags);

    AppJsonEntity appRegister(UserkgRequest request, HttpServletRequest servletRequest);

    AppJsonEntity appSendSmsCode(UserkgRequest request,HttpServletRequest req);

    AppJsonEntity chckSmsEmailCode(UserkgRequest request);

    AppJsonEntity completeProfile(UserkgRequest request, UserkgResponse kguser, HttpServletRequest req);

    AppJsonEntity checkLogin(UserkgRequest request, HttpServletRequest req);

    AppJsonEntity checkLoginForM(UserkgRequest request);
}
