package com.kg.platform.dao.write;

import com.kg.platform.dao.entity.User;
import com.kg.platform.model.in.UserInModel;
import com.kg.platform.model.request.TxPasswordEditRequest;
import com.kg.platform.model.request.UserCertEditRequest;
import org.apache.ibatis.annotations.Param;

public interface UserWMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(UserInModel model);

    int insertSelective(UserInModel model);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int updatePssword(UserInModel inModel);

    int updateByUserSelective(UserInModel inModel);

    int centerUped(UserInModel inModel);

    int UpdateRole(UserInModel inModel);

    void updateTxPassword(TxPasswordEditRequest request);

    void updateUserCert(UserCertEditRequest request);

    void insertUserCert(UserCertEditRequest request);

    long selectCountUserCert(UserCertEditRequest request);

    Integer updateColumnAuthed(@Param("userId") Long userId, @Param("isAuthed") int isAuthed);

    /**
     * 更新专栏等级
     *
     * @param aLong
     * @param columnLevel
     * @return
     */
    int updateColumnLevel(@Param("userId") Long userId, @Param("columnLevel") Integer columnLevel);
}
