package com.kg.platform.dao.read;

import com.kg.platform.model.in.UserCertInModel;
import com.kg.platform.model.out.UserCertOutModel;

public interface UserCertRMapper {

    UserCertOutModel selectByUser(UserCertInModel inModel);

    long selectByIdCardNo(UserCertInModel inModel);

}
