package com.kg.platform.dao.read;

import java.util.List;

import com.kg.platform.model.in.UserConcernInModel;
import com.kg.platform.model.out.HotAuthorsOutModel;
import com.kg.platform.model.out.UserConcernListOutModel;
import com.kg.platform.model.out.UserConcernOutModel;

public interface UserConcernRMapper {

    /**
     * 查询用户关联关系
     *
     * @param request
     * @return
     */
    UserConcernOutModel getConcernInfo(UserConcernInModel inModel);

    /**
     * 查询我的关注列表
     *
     * @param request
     * @return
     */
    List<UserConcernListOutModel> getConcernList(UserConcernInModel inModel);

    /**
     * 查询我的关注总条数
     *
     * @param request
     * @return
     */
    long getConcernCount(UserConcernInModel inModel);

    /**
     * 查询我的粉丝列表
     *
     * @param request
     * @return
     */
    List<UserConcernListOutModel> getFansList(UserConcernInModel inModel);

    /**
     * 查询我的关注总条数
     *
     * @param request
     * @return
     */
    long getFansCount(UserConcernInModel inModel);

    /**
     * 获取是否有粉丝关注
     *
     * @param inModel
     * @return
     */
    Integer concernReminder(UserConcernInModel inModel);

    /**
     * 查询关注信息
     *
     * @param request
     * @return
     */
    List<UserConcernListOutModel> getConcernDetails(UserConcernInModel inModel);


    /**
     * 查询未关注的推荐作者
     *
     * @param request
     * @return
     */

    List<Long> getConcernListIds(UserConcernInModel inModel);

    List<HotAuthorsOutModel> getRecommendHotAuthors(UserConcernInModel inModel);

    /**
     * 查询未关注的推荐作者数量
     *
     * @param request
     * @return
     */
    long getRecommendHotAuthorsCount(UserConcernInModel userConcernInModel);

    /**
     * 查询我已经关注的作者ID
     *
     * @param userConcernInModel
     * @return
     */
    List<Long> getConcernedUserIds(UserConcernInModel userConcernInModel);

    /**
     * 查询我的关注列表
     *
     * @param request
     * @return
     */
    List<UserConcernListOutModel> getHotAuthors(UserConcernInModel userConcernInModel);

    /**
     * 热门作者总数
     *
     * @param request
     * @return
     */
    long getHotAuthorsCount(UserConcernInModel userConcernInModel);

    /**
     * 获取关注我的信息列表
     */
    List<UserConcernListOutModel> getConcernedList(UserConcernInModel userConcernInModel);

    /**
     * 搜索作者
     *
     * @param inModel
     * @return
     */
    List<UserConcernListOutModel> searchAuthors(UserConcernInModel inModel);

    /**
     * 搜索结果总数
     *
     * @param inModel
     * @return
     */
    long searchAuthorsCount(UserConcernInModel inModel);
}
