package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.UserProfile;
import com.kg.platform.model.in.UserProfileInModel;
import org.apache.ibatis.annotations.Param;

public interface UserProfileWMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(UserProfileInModel inModel);

    int insertSelective(UserProfileInModel inModel);

    int updateByPrimaryKeySelective(UserProfile record);

    int updateByPrimaryKey(UserProfile record);

    int updateProfile(UserProfileInModel inModel);

    Integer updateColumnAuthed(@Param("userId") Long userId, @Param("columnIdentity") String columnIdentity);
}
