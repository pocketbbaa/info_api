package com.kg.platform.service.m;

import com.kg.platform.common.entity.MJsonEntity;
import com.kg.platform.model.request.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2018/10/19.
 */
public interface MUserService {

    MJsonEntity register(UserkgRequest request);

    MJsonEntity sendSmsCode(UserkgRequest userkgRequest, UserkgMCodeRequest codeRequest);

    MJsonEntity sendSmsCodeLogin(UserkgRequest userkgRequest, UserkgMCodeRequest codeRequest);

    /**
     * 专栏主页
     *
     * @param request
     * @return
     */
    MJsonEntity selectByuserprofileId(UserProfileRequest request);

    /**
     * 他人主页信息
     *
     * @param request
     * @return
     */
    MJsonEntity getByIdProfile(UserProfileRequest request);

    /**
     * 他人的评论列表
     *
     * @param request
     * @return
     */
    MJsonEntity getOthersComment(UserCommentRequest request);

    /**
     * 获取用户粉丝列表
     *
     * @param request
     * @return
     */
    MJsonEntity getFansList(UserConcernRequest request);

    MJsonEntity invite(UserkgRequest request);

    /**
     * 登录
     *
     * @param request
     * @param req
     * @return
     */
    MJsonEntity checkLogin(UserkgRequest request);
}
