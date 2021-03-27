package com.kg.platform.service.impl;

import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.*;
import com.kg.platform.dao.read.UserCertRMapper;
import com.kg.platform.model.in.UserCertInModel;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.search.dto.UserResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kg.platform.dao.entity.User;
import com.kg.platform.dao.read.UserRMapper;
import com.kg.platform.dao.write.UserWMapper;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.TxPasswordEditRequest;
import com.kg.platform.model.request.UserCertEditRequest;
import com.kg.platform.service.LynnService;
import redis.clients.jedis.Jedis;

import java.util.HashMap;

/**
 * lynn提供的前端api
 */
@Service
public class LynnServiceImpl implements LynnService {

    private static Logger logger = Logger.getLogger(LynnServiceImpl.class);

    @Autowired
    protected JedisUtils jedisUtils;

    @Autowired
    private UserWMapper userWMapper;

    @Autowired
    private UserRMapper userRMapper;

    @Autowired
    private UserCertRMapper userCertRMapper;

    @Override
    public JsonEntity setTxPassword(TxPasswordEditRequest request, UserkgResponse kguser) {
        if(kguser == null || kguser.getUserId() == null){
            logger.info("【非法操作,获取到的用户信息为空,设置交易密码失败】");
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.TOKENRERROR);
        }
        if(request.getCode() == null){
            logger.info("【传入参数 code为空,中断设置交易密码操作】");
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if(request.getPassword() == null || request.getPassword().isEmpty()){
            request.setPassword(request.getNewPwd());
        }
        UserkgOutModel model = userRMapper.selectByPrimaryKey(Long.parseLong(kguser.getUserId()));
        if (getSendSmsEmailcode(request.getCode(), model.getUserMobile())) {
            // 修改user_account的tx_password字段
            // 更新user的tradepassword_set为1
            request.setUserId(Long.parseLong(kguser.getUserId()));
            Boolean flag = updateTxPassword(request);
            jedisUtils.del(JedisKey.vatcodeKey(model.getUserMobile()));
            if(!flag){
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SERVERERROR);
            }
            return JsonEntity.makeSuccessJsonEntity();
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(), "验证码输入不正确！");
        }
    }

    private Boolean updateTxPassword(TxPasswordEditRequest request){
        request.setPassword(MD5Util.md5Hex(request.getPassword() + Constants.SALT));
        try {
            userWMapper.updateTxPassword(request);
            User user = new User();
            user.setUserId(request.getUserId());
            user.setTradepasswordSet(true);
            userWMapper.updateByPrimaryKeySelective(user);
        } catch (Exception e) {
            logger.error("【修改交易密码】更新数据库中交易密码时出现异常 e:"+e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 验证码放进redis
     */
    private boolean getSendSmsEmailcode(String code, String verIfy) {
        // this.jedisUtils.set(JedisKey.vatcodeKey(verIfy), code,
        // JedisKey.THREE_MINUTE);
        String vatcode = jedisUtils.get(JedisKey.vatcodeKey(verIfy));
        return code.equals(vatcode);
    }

    @Override
    public JsonEntity userCert(UserCertEditRequest request) {
        UserCertInModel inModel = new UserCertInModel();
        inModel.setIdcardNo(request.getIdcardNo());
        long ifHave = userCertRMapper.selectByIdCardNo(inModel);
        if(ifHave>0){
            //存在已通过或者审核中的记录，不能进行再次提交
           return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.USERIDCORDEXIST);
        }

        long count = userWMapper.selectCountUserCert(request);
        if (count > 0) {
            userWMapper.updateUserCert(request);
        } else {
            userWMapper.insertUserCert(request);
        }
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                ExceptionEnum.SUCCESS.getMessage());
    }

    @Override
    public AppJsonEntity getTxPasswdCode(TxPasswordEditRequest request, UserkgResponse kguser) {
        if(kguser == null || kguser.getMobIle() == null || request.getCode() == null){
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        //获取当前用户的手机号
        String userMobile = kguser.getMobIle();
        String key = JedisKey.vatcodeKey(userMobile);
        //获取缓存中的手机验证码
        String cacheCode = jedisUtils.get(key);
        if(!(request.getCode().equals(cacheCode))){
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.AUTOCODEERR);
        }
        jedisUtils.del(key);
        //生成12位授权码
        String authCodeStr = RandomUtils.getRandomNum(12);
        if(!setAuthCodeToCache(authCodeStr,userMobile)){
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.SERVERERROR);
        }
        HashMap<String,Object> resultMap = new HashMap<>();
        resultMap.put("code",authCodeStr);
        return AppJsonEntity.makeSuccessJsonEntity(resultMap);
    }

    @Override
    public JsonEntity getWebTxPasswdCode(TxPasswordEditRequest request, UserkgResponse kguser) {
        if(kguser == null || kguser.getMobIle() == null || request.getCode() == null){
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        //获取当前用户的手机号
        String userMobile = kguser.getMobIle();
        String key = JedisKey.vatcodeKey(userMobile);
        //获取缓存中的手机验证码
        String cacheCode = jedisUtils.get(key);
        jedisUtils.del(key);
        if(!(request.getCode().equals(cacheCode))){
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.AUTOCODEERR);
        }
        //生成12位授权码
        String authCodeStr = RandomUtils.getRandomNum(12);
        if(!setAuthCodeToCache(authCodeStr,userMobile)){
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SERVERERROR);
        }
        HashMap<String,Object> resultMap = new HashMap<>();
        resultMap.put("code",authCodeStr);
        return JsonEntity.makeSuccessJsonEntity(resultMap);
    }

    @Override
    public JsonEntity updateTxPassword(TxPasswordEditRequest request, UserkgResponse kguser) {
        if(kguser == null || kguser.getMobIle() == null){
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.TOKENRERROR);
        }
        if(request.getCode() == null){
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        //验证授权码有效性
        if(!invalidAuthCode(request.getCode(),kguser.getMobIle())){
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.AUTOCODEERR);
        }
        if(request.getPassword() == null){
            request.setPassword(request.getNewPwd());
        }
        if(request.getPassword() == null){
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        request.setUserId(Long.parseLong(kguser.getUserId()));
        //验证通过 修改交易密码
        if(!updateTxPassword(request)){
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SERVERERROR);
        }
        return JsonEntity.makeSuccessJsonEntity();
    }

    private boolean invalidAuthCode(String code, String mobIle) {
        String key = JedisKey.getAuthorizationKey(mobIle);
        String cacheCode;
        try {
            cacheCode = jedisUtils.get(key);
            jedisUtils.del(key);
        } catch (Exception e) {
            logger.error("【修改交易密码】获取缓存授权码失败 e:"+e.getMessage());
            return false;
        }
        return code.equals(cacheCode);
    }

    public Boolean setAuthCodeToCache(String authCodeStr,String mobile){
        //缓存至redis
        try{
            String key = JedisKey.getAuthorizationKey(mobile);
            jedisUtils.set(key,authCodeStr,JedisKey.TEN_MINUTE);
        } catch (Exception e) {
            logger.error("【修改交易密码】缓存用户授权码出现异常 e:" + e.getMessage());
            return false;
        }
        return true;
    }
}
