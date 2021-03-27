package com.kg.platform.dao.write;

import com.kg.platform.model.in.ThirdPartyLoginInModel;

public interface ThirdPartyLoginWMapper {

    int insertSelective(ThirdPartyLoginInModel thirdPartyLoginInModel);

    int updateByPrimaryKeySelective(ThirdPartyLoginInModel thirdPartyLoginInModel);

    /**
     * 解除绑定
     * 
     * @param thirdPartyLoginInModel
     * @return
     */
    int unbindAccount(ThirdPartyLoginInModel thirdPartyLoginInModel);

}
