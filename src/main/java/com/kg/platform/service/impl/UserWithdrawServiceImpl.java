package com.kg.platform.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.exception.ApiMessageException;
import com.kg.platform.common.exception.BusinessException;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.lock.Lock;
import com.kg.platform.common.utils.Constants;
import com.kg.platform.common.utils.PropertyLoader;
import com.kg.platform.common.utils.message.SmsUtil;
import com.kg.platform.dao.entity.AccountFlow;
import com.kg.platform.dao.entity.AccountWithdrawFlow;
import com.kg.platform.dao.entity.UserRelation;
import com.kg.platform.dao.entity.WithdrawBonusFlow;
import com.kg.platform.dao.read.AccountWithdrawFlowRMapper;
import com.kg.platform.dao.read.UserRMapper;
import com.kg.platform.dao.read.WithdrawBonusFlowRMapper;
import com.kg.platform.dao.write.AccountFlowWMapper;
import com.kg.platform.dao.write.AccountWMapper;
import com.kg.platform.dao.write.AccountWithdrawFlowWMapper;
import com.kg.platform.dao.write.UserRelationWMapper;
import com.kg.platform.dao.write.WithdrawBonusFlowWMapper;
import com.kg.platform.enumeration.AccountWithdrawStatusEnum;
import com.kg.platform.enumeration.BonusRelTypeEnum;
import com.kg.platform.enumeration.BusinessTypeEnum;
import com.kg.platform.enumeration.FlowStatusEnum;
import com.kg.platform.enumeration.RelBonusEnum;
import com.kg.platform.enumeration.WithdrawBonusStatusEnum;
import com.kg.platform.model.in.AccountInModel;
import com.kg.platform.model.in.UserWithdrawInModel;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.TVWithdrawRequest;
import com.kg.platform.model.request.UserRelationRequest;
import com.kg.platform.model.response.TVWithdrawResponse;
import com.kg.platform.service.UserWithdrawService;

@Service
@Transactional
public class UserWithdrawServiceImpl implements UserWithdrawService {

    private static final Logger logger = LoggerFactory.getLogger(UserWithdrawServiceImpl.class);

    private static final String USER_WITHDRAW_LOCK_KEY = "kguser::UserWithdrawService::withdraw::";

    @Resource(name = "propertyLoader")
    protected PropertyLoader propertyLoader;

    @Inject
    IDGen idGenerater;

    @Inject
    private UserRMapper userRMapper;

    @Inject
    private AccountWithdrawFlowRMapper accountWithdrawFlowRMapper;

    @Inject
    private AccountWithdrawFlowWMapper accountWithdrawFlowWMapper;

    @Inject
    private AccountFlowWMapper accountFlowWMapper;

    @Inject
    AccountWMapper accountWMapper;

    @Inject
    private WithdrawBonusFlowWMapper withdrawBonusFlowWMapper;

    @Inject
    private WithdrawBonusFlowRMapper withdrawBonusFlowRMapper;

    @Inject
    UserRelationWMapper userRelationWMapper;

    /**
     * ??????????????????
     * 
     */
    @Override
    public boolean withdraw(UserWithdrawInModel inModel) {
        logger.info("?????????????????????UserWithdrawInModel={}", JSON.toJSONString(inModel));

        Lock lock = new Lock();

        Long withdrawFlowId = inModel.getWithdrawFlowId();

        String key = USER_WITHDRAW_LOCK_KEY + withdrawFlowId;
        final String TVAddress = propertyLoader.getProperty("platform", "global.TVAddress");
        final String userAddress = propertyLoader.getProperty("common", "global.userTxAddress");

        try {

            // ??????
            lock.lock(key);

            AccountWithdrawFlow accountWithdrawFlow = accountWithdrawFlowRMapper.selectByPrimaryKey(withdrawFlowId);
            if (accountWithdrawFlow == null || null == accountWithdrawFlow.getAccountAmount()) {
                logger.error("???????????????????????????withdrawFlowId {}", withdrawFlowId);
                throw new BusinessException(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                        ExceptionEnum.PARAMEMPTYERROR.getMessage());
            }

            Long userId = accountWithdrawFlow.getUserId();
            BigDecimal balance = accountWithdrawFlow.getAccountAmount();
            balance = balance.setScale(3, BigDecimal.ROUND_FLOOR);

            // ???????????????????????????
            UserkgOutModel user = userRMapper.selectByPrimaryKey(userId);
            if (user == null) {
                logger.error("?????????????????????????????????id {}", userId);
                throw new BusinessException(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                        ExceptionEnum.PARAMEMPTYERROR.getMessage());
            }

            TVWithdrawRequest tvWithdrawRequest = new TVWithdrawRequest(balance.toString(),
                    accountWithdrawFlow.getToAddress(), accountWithdrawFlow.getRemark());

            String requestJson = JSON.toJSONString(tvWithdrawRequest);

            logger.info("??????????????????{},????????????{} :", TVAddress, requestJson);

            String response = SmsUtil.sendSmsByPost(TVAddress, requestJson);
            // "{\"code\":200,\"data\":\"8b565d9b8555741cb5f53e6f914cbff80480ddc8\",\"message\":\"??????\"}";
            logger.info("?????????????????????????????? :" + response);

            if (null != response) {
                TVWithdrawResponse tvWithdrawResponse = JSON.parseObject(response, TVWithdrawResponse.class);
                if (tvWithdrawResponse.getCode().equals("200")) {

                    AccountWithdrawFlow userWithdrawFlow = new AccountWithdrawFlow();
                    Date accountTime = new Date();

                    userWithdrawFlow.setWithdrawFlowId(withdrawFlowId);
                    userWithdrawFlow.setFromAddress(userAddress);
                    userWithdrawFlow.setToAddress(inModel.getTxAddress());
                    userWithdrawFlow.setStatus(AccountWithdrawStatusEnum.PASS.getStatus());
                    userWithdrawFlow.setTxId(tvWithdrawResponse.getData());
                    userWithdrawFlow.setAccountTime(accountTime);

                    int success = accountWithdrawFlowWMapper.updateByPrimaryKeySelective(userWithdrawFlow);

                    logger.info("======??????????????????????????????" + success);

                    // ??????????????????
                    AccountFlow accountFlow = new AccountFlow();
                    long flowId = accountWithdrawFlow.getAccountFlowId();
                    accountFlow.setAccountFlowId(flowId);

                    accountFlow.setTxId(tvWithdrawResponse.getData());
                    accountFlow.setFlowDetail("???????????????");
                    accountFlow.setAccountDate(accountTime);
                    accountFlow.setFlowStatus(FlowStatusEnum.TRANSFER_SUCCESS.getStatus());

                    success = accountFlowWMapper.updateByPrimaryKeySelective(accountFlow);

                    logger.info("======????????????????????????" + success);

                    AccountInModel auntinModel = new AccountInModel();
                    auntinModel.setUserId(userId);
                    auntinModel.setFrozenBalance(accountWithdrawFlow.getWithdrawAmount());

                    success = accountWMapper.reduceFBalance(auntinModel);

                    logger.info("======??????????????????????????????" + success);

                    return true;
                }
            }
        } finally {
            lock.unLock();
        }

        return false;
    }

    @Override
    public boolean applyWithdraw(UserRelationRequest request) {

        logger.info("???????????????????????????????????????{}", JSON.toJSONString(request));
        boolean SUCCESS;
        Lock lock = new Lock();
        String key = USER_WITHDRAW_LOCK_KEY + "inviteBonus" + request.getUserId();
        try {
            lock.lock(key);
            // ????????????????????????
            UserkgOutModel umodel = userRMapper.getUserDetails(request.getUserId());
            if (umodel == null) {
                logger.error("????????????????????????????????????id {}", request.getUserId());
                throw new ApiMessageException(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                        ExceptionEnum.PARAMEMPTYERROR.getMessage());
            }
            // ????????????????????????????????????
            /*
             * WithdrawBonusInModel withdrawBonusInModel = new
             * WithdrawBonusInModel();
             * withdrawBonusInModel.setUserId(request.getUserId());
             * WithdrawBonusOutModel outModel =
             * withdrawBonusFlowRMapper.getauditAmount(withdrawBonusInModel); if
             * (outModel != null) { throw new
             * ApiMessageException("??????????????????????????????,??????????????????"); }
             */

            BigDecimal _bonus = request.get_bonus();
            Long targetCount = request.getTargetCount();
            AccountFlow accountFlow = new AccountFlow();

            long flowid = idGenerater.nextId();
            accountFlow.setAccountFlowId(flowid);
            accountFlow.setRelationFlowId(flowid);
            accountFlow.setUserId(request.getUserId());
            accountFlow.setUserPhone(umodel.getUserMobile());
            accountFlow.setUserEmail(umodel.getUserEmail());
            // accountFlow.setAmount(_bonus);
            // accountFlow.setAccountAmount(_bonus);
            accountFlow.setFreezeAmount(_bonus);
            accountFlow.setFlowDate(new Date());
            accountFlow.setFlowDetail("????????????????????????");
            accountFlow.setBusinessTypeId(BusinessTypeEnum.INVITEBONUS.getStatus());
            accountFlow.setFlowStatus(FlowStatusEnum.FREEZED.getStatus());
            SUCCESS = accountFlowWMapper.insertSelective(accountFlow) > 0;

            // ????????????????????????
            AccountInModel auntinModel = new AccountInModel();
            auntinModel.setUserId(request.getUserId());
            auntinModel.setFrozenBalance(_bonus);
            SUCCESS = accountWMapper.increaseFbalance(auntinModel) > 0;

            accountFlow = new AccountFlow();
            accountFlow.setAccountFlowId(idGenerater.nextId());
            accountFlow.setRelationFlowId(flowid);
            accountFlow.setUserId(Constants.PLATFORM_USER_ID);
            accountFlow.setAmount(_bonus.negate());
            accountFlow.setAccountAmount(_bonus.negate());
            accountFlow.setFlowDate(new Date());
            accountFlow.setFlowDetail("????????????????????????");
            accountFlow.setBusinessTypeId(BusinessTypeEnum.INVITEBONUS.getStatus());
            accountFlow.setFlowStatus(FlowStatusEnum.PAYED.getStatus());
            accountFlowWMapper.insertSelective(accountFlow);

            // ??????????????????
            AccountInModel inModel = new AccountInModel();
            inModel.setBalance(_bonus.negate());
            inModel.setUserId(Constants.PLATFORM_USER_ID);
            SUCCESS = accountWMapper.addUserbalance(inModel) > 0;

            WithdrawBonusFlow withdrawBonusFlow = new WithdrawBonusFlow();
            withdrawBonusFlow.setWithdrawFlowId(idGenerater.nextId());
            withdrawBonusFlow.setAccountFlowId(flowid);
            withdrawBonusFlow.setUserId(request.getUserId());
            withdrawBonusFlow.setInviteCount(targetCount.intValue());
            withdrawBonusFlow.setUserPhone(umodel.getUserMobile());
            withdrawBonusFlow.setUserEmail(umodel.getUserEmail());
            withdrawBonusFlow.setWithdrawAmount(_bonus);
            // withdrawBonusFlow.setAccountAmount(_bonus);
            withdrawBonusFlow.setWithdrawTime(new Date());
            // withdrawBonusFlow.setAccountTime(new Date());
            withdrawBonusFlow.setStatus(WithdrawBonusStatusEnum.AUDITING.getStatus());
            SUCCESS = withdrawBonusFlowWMapper.insertSelective(withdrawBonusFlow) > 0;

            // ??????????????????????????????
            UserRelation userRelation = new UserRelation();
            userRelation.setUserId(request.getUserId());
            userRelation.setBonusStatus(RelBonusEnum.RECIEVED.getType());
            userRelation.setRelType(BonusRelTypeEnum.INVITE.getType());
            userRelation.setTargrtCount(targetCount);
            userRelationWMapper.updateBonusStatus(userRelation);

        } finally {
            lock.unLock();
        }
        return SUCCESS;

    }

}
