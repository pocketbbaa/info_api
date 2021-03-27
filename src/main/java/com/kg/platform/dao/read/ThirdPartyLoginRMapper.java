package com.kg.platform.dao.read;

import java.util.List;

import com.kg.platform.model.in.ThirdPartyLoginInModel;
import com.kg.platform.model.out.ThirdPartyLoginOutModel;
import com.kg.platform.model.out.UserkgOutModel;

public interface ThirdPartyLoginRMapper {

    /**
     * 通过open检查用户是否绑定过账号
     * 
     * @param openId
     * @return
     */
    Long checkUserBind(String openId);

    /**
     * 通过openId获取用户信息
     * 
     * @param openId
     * @return
     */
    UserkgOutModel getUserInfoByOpenId(String openId);

    /**
     * 通过userId获取用户信息
     * 
     * @param userId
     * @return
     */
    ThirdPartyLoginOutModel getUserThirdPartyLogin(Long userId);

    /**
     * 检查第三方账号绑定状态
     * 
     * @param userId
     * @return
     */
    List<ThirdPartyLoginOutModel> checkBindStatus(Long userId);

    /**
     * 检查第三方账号绑定状态
     * 
     * @param userId
     * @return
     */
    ThirdPartyLoginOutModel getBindInfo(ThirdPartyLoginInModel thirdPartyLoginInModel);

    /**
     * 检查第三方账号绑定状态 通过openID和openType判断
     * 
     * @param userId
     * @return
     */
    ThirdPartyLoginOutModel checkBindStatusByOpenId(ThirdPartyLoginInModel thirdPartyLoginInModel);

}
