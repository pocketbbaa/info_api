package com.kg.platform.service;

import java.math.BigDecimal;

import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.request.UserCollectRequest;
import com.kg.platform.model.response.UserCollectResponse;

public interface UsercollectService {
    /**
     * 我的收藏
     * 
     * @param request
     * @param page
     * @return
     */
    PageModel<UserCollectResponse> getCollectAll(UserCollectRequest request, PageModel<UserCollectResponse> page);

    /**
     * 取消收藏
     * 
     * @param request
     * @return
     */
    boolean deleteByPrimaryKey(UserCollectRequest request);

    /**
     * 增加点赞收藏
     * 
     * @param request
     * @return
     */
    boolean insertSelective(UserCollectRequest request);

    BigDecimal addCollect(UserCollectRequest request);

    /**
     * 查询分享人是否分享过文章
     */
    boolean getShareStatus(UserCollectRequest request);

    /**
     * 查询分享人是否浏览过文章
     */
    boolean getBrowseStatus(UserCollectRequest request);

    /**
     * 查询用户是否点赞或收藏
     * 
     * @param request
     * @return
     */
    Result<Integer> getCollectByUserIdAndArticleId(UserCollectRequest request);
}
