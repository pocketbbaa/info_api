package com.kg.platform.service;

import com.kg.platform.model.TokenModel;

public interface TokenManager {

    /**
     * 创建一个token关联上指定用户
     *
     * @param userId 指定用户的id
     * @return 生成的token
     */
    public TokenModel createToken(Long userId);

    /**
     * 创建一个token关联上指定用户
     *
     * @param userId 指定用户的id
     * @return 生成的token
     */
    public TokenModel createAppToken(Long userId);

    /**
     * 检查token是否有效
     *
     * @param model token
     * @return 是否有效
     */
    public boolean checkToken(TokenModel model);

    /**
     * 从字符串中解析token
     *
     * @param authentication 加密后的字符串
     * @return
     */
    public TokenModel getToken(String authentication);

    /**
     * 根据用户ID获取APP的Token
     *
     * @param userId
     * @return
     */
    public String getTokenByUserId(Long userId);


    /**
     * 清除token
     *
     * @param userId 登录用户的id
     */
    public void deleteToken(Long userId);

    /**
     * 清除Apptoken
     *
     * @param userId 登录用户的id
     */
    public void deleteAppToken(Long userId);

    /**
     * 校验APPtoken
     *
     * @param model
     * @return
     */
    boolean checkAppToken(TokenModel model);
}
