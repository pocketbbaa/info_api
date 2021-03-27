package com.kg.platform.service.app;

import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.out.UserConcernOutModel;
import com.kg.platform.model.request.UserConcernRequest;

import com.kg.platform.model.response.HotAuthorsResponse;

import com.kg.platform.model.response.UserConcernListResponse;
import com.kg.platform.model.response.UserkgResponse;

/**
 * 账户相关
 * <p>
 * by wangyang
 */
public interface UserConcernService {

    /**
     * 我的粉丝列表
     *
     * @return
     */
    AppJsonEntity fansList(UserkgResponse kguser, PageModel<UserConcernListResponse> page);

    /**
     * 我的关注列表
     *
     * @return
     */
    AppJsonEntity concernList(UserkgResponse kguser, PageModel<UserConcernListResponse> page);

    /**
     * 关注、取消关注
     *
     * @return
     */
    AppJsonEntity concernUser(UserkgResponse kguser, UserConcernRequest request);

    /**
     * 关注红点提示
     *
     * @return
     */
    AppJsonEntity concernReminder(UserkgResponse kguser);

    /**
     * 注册推荐关注
     *
     * @return
     */
    AppJsonEntity recommendHotAuthors(UserkgResponse kguser, PageModel<HotAuthorsResponse> page);

    /**
     * 热门作者列表
     *
     * @return
     */
    AppJsonEntity hotAuthorList(UserConcernRequest request, PageModel<UserConcernListResponse> page);

    /**
     * 搜索作者列表
     *
     * @return
     */
    PageModel<UserConcernListResponse> searchAuthorList(UserConcernRequest request, PageModel<UserConcernListResponse> page);

    /**
     * 是否已关注
     *
     * @param relUserId 被关注用户ID
     * @param userId    关注用户ID
     * @return
     */
    boolean isConcerned(Long userId, Long relUserId);

    /**
     * 获取关注信息
     *
     * @param userId
     * @param relUserId
     */
    UserConcernOutModel getConcernInfo(Long userId, Long relUserId);
}
