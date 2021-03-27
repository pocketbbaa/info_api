package com.kg.platform.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.kg.platform.common.context.Result;
import com.kg.platform.model.out.UserProfileOutModel;
import com.kg.platform.model.request.UserProfileRequest;
import com.kg.platform.model.response.UserProfileResponse;

public interface UserProfileService {

    /**
     * 咨询详情右侧作者模块
     */

    Result<UserProfileResponse> selectByuserprofileId(UserProfileRequest request);

    /**
     * 首页热门作者
     */
    Result<List<UserProfileResponse>> getUserproFile();

    /**
     * 申请专栏
     */
    boolean addUserProfile(UserProfileRequest request);

    /**
     * 保存用户信息
     */
    boolean initUserProfile(UserProfileRequest request);

    /**
     * 统计文章个数判断是否有文章
     */

    boolean countArticle(UserProfileRequest request);

    /**
     * 获取资料
     * 
     * @param request
     * @return
     */
    Result<UserProfileResponse> getProfile(UserProfileRequest request, HttpServletRequest req);

    /**
     * 修改资料
     * 
     * @param request
     * @return
     */
    public boolean updateProfile(UserProfileRequest request);

    /**
     * 判断是否申请
     */
    public boolean checkProfile(UserProfileRequest request);

    /**
     * 他人主页
     * 
     * @param request
     * @return
     */
    Result<UserProfileResponse> getByIdProfile(UserProfileRequest request);

    /**
     * 查询用户信息
     * 
     * @param request
     * @return
     */
    UserProfileOutModel getUserProfile(UserProfileRequest request);

}
