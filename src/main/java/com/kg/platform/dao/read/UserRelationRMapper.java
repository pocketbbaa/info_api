package com.kg.platform.dao.read;

import java.util.List;
import java.util.Set;

import com.kg.platform.dao.entity.UserRelation;
import com.kg.platform.model.in.DiscipleInModel;
import com.kg.platform.model.in.UserRelationInModel;
import com.kg.platform.model.response.DiscipleInfoResponse;
import com.kg.platform.model.response.InviteUserResponse;
import org.apache.ibatis.annotations.Param;

public interface UserRelationRMapper {

    UserRelation selectByPrimaryKey(Long relId);

    /**
     * 根据被关联用户ID 查询
     *
     * @param relUserId
     * @return
     */
    UserRelation selectByRelUserId(@Param("relUserId") Long relUserId);

    Long selectInviteCount(Long userId);

    /**
     * 徒弟数，包括邀新和手动绑定
     *
     * @param userId
     * @return
     */
    long selectInviteCountForApp(Long userId);

    /**
     * 查询 我的师傅
     *
     * @param userId
     * @return
     */
    UserRelation selectParentUser(Long userId);

    /**
     * 查询前十个我的邀请记录
     *
     * @param userId
     * @return
     */
    List<InviteUserResponse> getJayActivityInviteUser(UserRelationInModel userRelationInModel);

    /**
     * 检查我是否有资格参加世界杯活动
     *
     * @param userId
     * @return
     */
    List<UserRelation> checkJoinWordCup(Long userId);

    List<UserRelation> selectMoreUserRelationInfo(List<Long> userIdList);

    List<UserRelation> selectMoreChildUserRelationInfo(List<Long> userIdList);


    /**
     * 查询我的哦徒弟无进贡列表
     *
     * @param discipleInModel
     * @return
     */
    List<DiscipleInfoResponse> getSubList(DiscipleInModel discipleInModel);


    /**
     * 我的徒弟列表，过滤ID
     *
     * @param discipleInModel
     * @return
     */
    List<DiscipleInfoResponse> getSubListFilter(@Param("userId") Long userId, @Param("start") int start, @Param("limit") int pageSize, @Param("list") List<Long> list);


    /**
     * 查询我的徒弟进贡列表
     *
     * @param discipleInModel
     * @return
     */
    List<DiscipleInfoResponse> getSubTribList(DiscipleInModel discipleInModel);


    /**
     * 是否已经绑定了师傅
     *
     * @param userRelationInModel
     * @return
     */
    int ifRel(UserRelation userRelation);


}
