package com.kg.platform.service.app.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.lock.Lock;
import com.kg.platform.common.utils.Constants;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.common.utils.HttpUtil;
import com.kg.platform.common.utils.JedisKey;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.common.utils.JsonUtil;
import com.kg.platform.common.utils.RandomUtils;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.dao.entity.AccountFlowTxb;
import com.kg.platform.dao.read.AccountFlowTxbRMapper;
import com.kg.platform.dao.read.ActivityInviteLogRMapper;
import com.kg.platform.dao.read.KgActivityCompetionRMapper;
import com.kg.platform.dao.read.KgActivityGuessRMapper;
import com.kg.platform.dao.read.KgActivityMinerRMapper;
import com.kg.platform.dao.read.KgCommonSettingRMapper;
import com.kg.platform.dao.read.KgMinerAssistRMapper;
import com.kg.platform.dao.read.KgMinerRobRMapper;
import com.kg.platform.dao.read.UserRMapper;
import com.kg.platform.dao.write.AccountFlowTxbWMapper;
import com.kg.platform.dao.write.AccountWMapper;
import com.kg.platform.dao.write.ActivityInviteLogWMapper;
import com.kg.platform.dao.write.KgActivityGuessWMapper;
import com.kg.platform.dao.write.KgActivityMinerWMapper;
import com.kg.platform.dao.write.KgMinerAssistWMapper;
import com.kg.platform.dao.write.KgMinerRobWMapper;
import com.kg.platform.enumeration.BusinessTypeEnum;
import com.kg.platform.enumeration.FlowStatusEnum;
import com.kg.platform.enumeration.LockStatusEnum;
import com.kg.platform.model.in.AccountInModel;
import com.kg.platform.model.in.ActivityInviteLogInModel;
import com.kg.platform.model.in.KgActivityCompetionInModel;
import com.kg.platform.model.in.KgActivityGuessInModel;
import com.kg.platform.model.in.KgActivityMinerInModel;
import com.kg.platform.model.in.KgCommonSettingInModel;
import com.kg.platform.model.in.KgMinerAssistInModel;
import com.kg.platform.model.in.KgMinerRobInModel;
import com.kg.platform.model.out.ActivityInviteLogOutModel;
import com.kg.platform.model.out.KgActivityCompetionOutModel;
import com.kg.platform.model.out.KgActivityGuessOutModel;
import com.kg.platform.model.out.KgActivityMinerOutModel;
import com.kg.platform.model.out.KgCommonSettingOutModel;
import com.kg.platform.model.out.KgMinerAssistAmountOutModel;
import com.kg.platform.model.out.KgMinerAssistOutModel;
import com.kg.platform.model.out.KgMinerRobOutModel;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.AccountRequest;
import com.kg.platform.model.request.DeepfinRequest;
import com.kg.platform.model.request.KgActivityCompetionRequest;
import com.kg.platform.model.request.KgActivityGuessRequest;
import com.kg.platform.model.request.KgActivityMinerRequest;
import com.kg.platform.model.request.KgMinerAssistRequest;
import com.kg.platform.model.request.KgMinerRobRequest;
import com.kg.platform.model.request.UserkgRequest;
import com.kg.platform.model.response.ActivityPopInfoResponse;
import com.kg.platform.model.response.KgActivityCompetionResponse;
import com.kg.platform.model.response.KgActivityMinerResponse;
import com.kg.platform.model.response.KgMinerAssistResponse;
import com.kg.platform.model.response.KgMinerRobResponse;
import com.kg.platform.model.response.MinerActivityDateResponse;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.model.response.WorldcupActivityDateResponse;
import com.kg.platform.service.TokenManager;
import com.kg.platform.service.UserkgService;
import com.kg.platform.service.app.AppActivityService;

/**
 * Created by Administrator on 2018/5/18.
 */
@Service
public class AppActivityServiceImpl implements AppActivityService {

    private static final String USER_ACCOUNT_LOCK_KEY = "kguser::AppActivityService::reduceUserAccountToPlatform::";

    private static final Logger logger = LoggerFactory.getLogger(AppActivityServiceImpl.class);

    @Autowired
    private KgActivityMinerRMapper kgActivityMinerRMapper;

    @Autowired
    private KgActivityMinerWMapper kgActivityMinerWMapper;

    @Autowired
    private KgMinerRobRMapper kgMinerRobRMapper;

    @Autowired
    private KgMinerRobWMapper kgMinerRobWMapper;

    @Autowired
    private KgMinerAssistWMapper kgMinerAssistWMapper;

    @Autowired
    private AccountFlowTxbRMapper accountFlowTxbRMapper;

    @Autowired
    private JedisUtils jedisUtils;

    @Inject
    TokenManager manager;

    @Autowired
    private UserRMapper userRMapper;

    @Autowired
    private IDGen idGenerater;

    @Autowired
    private AccountFlowTxbWMapper accountFlowTxbWMapper;

    @Autowired
    private AccountWMapper accountWMapper;

    @Autowired
    private KgMinerAssistRMapper kgMinerAssistRMapper;

    @Autowired
    private KgCommonSettingRMapper kgCommonSettingRMapper;

    @Autowired
    private KgActivityGuessWMapper KgActivityGuessWMapper;

    @Autowired
    private KgActivityCompetionRMapper kgActivityCompetionRMapper;

    @Autowired
    private ActivityInviteLogWMapper activityInviteLogWMapper;

    @Autowired
    private ActivityInviteLogRMapper activityInviteLogRMapper;

    @Autowired
    private UserkgService userkgService;

    @Autowired
    private KgActivityGuessRMapper kgActivityGuessRMapper;

    @Override
    public AppJsonEntity minerList() {

        List<KgActivityMinerOutModel> outModels = kgActivityMinerRMapper.getMinerList();
        List<KgActivityMinerResponse> responses = new ArrayList<>();
        for (KgActivityMinerOutModel outModel : outModels) {
            KgActivityMinerResponse response = new KgActivityMinerResponse();
            response.setMinerId(outModel.getMinerId().toString());
            response.setMinerPhoto(outModel.getMinerPhoto());
            response.setMinerName(outModel.getMinerName());
            response.setMinerNumber(outModel.getMinerNumber());
            response.setMinerPrice(outModel.getMinerPrice().toString());
            response.setRobedNumber(outModel.getRobedNumber());
            responses.add(response);
        }
        Map<String, List<KgActivityMinerResponse>> map = new HashMap<>();
        map.put("minerList", responses);
        return AppJsonEntity.makeSuccessJsonEntity(map);
    }

    @Override
    @Transactional
    public AppJsonEntity rushToMiner(UserkgResponse kguser, KgActivityMinerRequest request) {

        logger.info("----------??????????????????-----------");
        if (request == null || kguser == null || request.getMinerId() == null
                || StringUtils.isEmpty(kguser.getUserId())) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }

        logger.info("-----------????????????????????????????????????-----------------");
        KgCommonSettingInModel inModel = new KgCommonSettingInModel();
        inModel.setSettingKey(MinerActivityDateResponse.SETTING_KEY);
        KgCommonSettingOutModel outModel = kgCommonSettingRMapper.selectBySettingKey(inModel);
        MinerActivityDateResponse response = JSON.parseObject(outModel.getSettingValue(),
                MinerActivityDateResponse.class);
        Date startDate = DateUtils.parseDate(response.getStartTime(), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
        Date endDate = DateUtils.parseDate(response.getEndTime(), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
        if (DateUtils.compareDate(new Date(), startDate) < 0) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACTIVITY_START.getCode(),
                    ExceptionEnum.ACTIVITY_START.getMessage());
        }
        logger.info("----------????????????????????????????????????--------------");
        if (DateUtils.compareDate(new Date(), endDate) > 0) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACTIVITY_END.getCode(),
                    ExceptionEnum.ACTIVITY_END.getMessage());
        }

        logger.info("------------?????????????????????????????????????????????--------------");
        KgActivityMinerOutModel miner = kgActivityMinerRMapper.selectByPrimaryKey(request.getMinerId());
        if (miner == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        logger.info("----------????????????????????????????????????--------------");
        startDate = miner.getMinerStartDate();
        endDate = miner.getMinerEndDate();
        if (DateUtils.compareDate(new Date(), startDate) < 0) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACTIVITY_START.getCode(),
                    ExceptionEnum.ACTIVITY_START.getMessage());
        }
        logger.info("----------????????????????????????????????????--------------");
        if (DateUtils.compareDate(new Date(), endDate) > 0) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACTIVITY_END.getCode(),
                    ExceptionEnum.ACTIVITY_END.getMessage());
        }

        logger.info("----------??????????????????????????????--------------");
        if (miner.getMinerNumber() == 0) {

            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.NONE_MINER_ERROR.getCode(),
                    ExceptionEnum.NONE_MINER_ERROR.getMessage());
        }

        logger.info("----------??????????????????????????????--------------");
        KgMinerRobInModel kgMinerRobInModel = new KgMinerRobInModel();
        kgMinerRobInModel.setMinerId(miner.getMinerId());
        kgMinerRobInModel.setUserId(Long.valueOf(kguser.getUserId()));
        KgMinerRobOutModel kgMinerRobOutModel = kgMinerRobRMapper.checkIsRob(kgMinerRobInModel);
        if (kgMinerRobOutModel != null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.ROBED_MINNER_ERROR.getCode(),
                    kgMinerRobOutModel.getRobId() + "");
        }

        logger.info("----------??????????????????-----------");
        String jedisKey = JedisKey.getRushToMiner(kguser.getUserId() + "_" + miner.getMinerId());
        if (jedisUtils.get(jedisKey) != null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.REPEAT_ERROR.getCode(),
                    ExceptionEnum.REPEAT_ERROR.getMessage());
        }
        jedisUtils.set(jedisKey, kguser.getUserId() + "_" + miner.getMinerId(), 5);

        logger.info("----------??????????????????????????????--------------");
        AccountInModel accountInModel = new AccountInModel();
        accountInModel.setBalance(Constants.ACTIVITY_MINER_PRICE);
        accountInModel.setUserId(Long.valueOf(kguser.getUserId()));
        int valiMoney = accountFlowTxbRMapper.validatePlatformKgBanlace(accountInModel);
        if (valiMoney == 0) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACCOUNT_KG_BANLANCE_ERROR.getCode(),
                    ExceptionEnum.ACCOUNT_KG_BANLANCE_ERROR.getMessage());
        }
        logger.info("----------????????????--------------");
        logger.info("----------??????????????????--------------");
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setUserId(Long.valueOf(kguser.getUserId()));
        accountRequest.setBalance(Constants.ACTIVITY_MINER_PRICE);
        this.reduceUserAccountToPlatform(accountRequest);

        logger.info("----------?????????????????????--------------");
        long robId = idGenerater.nextId();
        kgMinerRobInModel = new KgMinerRobInModel();
        kgMinerRobInModel.setAssistCode(getCode());
        kgMinerRobInModel.setMinerId(miner.getMinerId());
        kgMinerRobInModel.setRobDate(new Date());
        kgMinerRobInModel.setRobStatus(2);
        kgMinerRobInModel.setUserId(Long.valueOf(kguser.getUserId()));
        kgMinerRobInModel.setRobId(robId);
        kgMinerRobWMapper.insertSelective(kgMinerRobInModel);

        logger.info("----------??????????????????--------------");
        KgMinerAssistInModel kgMinerAssistInModel = new KgMinerAssistInModel();
        kgMinerAssistInModel.setAssistDate(new Date());
        kgMinerAssistInModel.setAssistId(idGenerater.nextId());
        kgMinerAssistInModel.setRobId(robId);
        kgMinerAssistInModel.setAssistStatus(1);
        kgMinerAssistInModel.setMinerId(miner.getMinerId());
        kgMinerAssistInModel.setUserId(Long.valueOf(kguser.getUserId()));
        BigDecimal assistAmount = BigDecimal.ZERO;
        assistAmount = (miner.getMinerPrice().multiply(Constants.ACTIVITY_MINER_ASSIST_RATE)).setScale(0,
                BigDecimal.ROUND_HALF_UP);
        kgMinerAssistInModel.setAssistAmount(assistAmount.intValue());
        kgMinerAssistWMapper.insertSelective(kgMinerAssistInModel);

        // ????????????????????????
        KgActivityMinerInModel kgActivityMinerInModel = new KgActivityMinerInModel();
        kgActivityMinerInModel.setMinerId(miner.getMinerId());
        kgActivityMinerWMapper.addJoinNums(kgActivityMinerInModel);

        Map<String, String> map = new HashMap<String, String>();
        map.put("robId", robId + "");
        return AppJsonEntity.makeSuccessJsonEntity(map);
    }

    @Override
    public Boolean reduceUserAccountToPlatform(AccountRequest request) {
        logger.info("??????????????????-----??????????????????", JSON.toJSONString(request));
        boolean success;
        Lock lock = new Lock();
        String key = USER_ACCOUNT_LOCK_KEY + request.getUserId() + request.getArticleId();
        try {
            lock.lock(key);
            // ???????????????
            UserkgOutModel userkgModel = userRMapper.getUserDetails(request.getUserId());
            AccountFlowTxb accountFlowTxb = new AccountFlowTxb();
            long flowid = idGenerater.nextId();
            accountFlowTxb.setAccountFlowId(flowid);
            accountFlowTxb.setRelationFlowId(flowid);
            accountFlowTxb.setUserId(Long.parseLong(userkgModel.getUserId()));
            accountFlowTxb.setUserPhone(userkgModel.getUserMobile());
            accountFlowTxb.setUserEmail(userkgModel.getUserEmail());
            accountFlowTxb.setAmount(request.getBalance().negate());
            accountFlowTxb.setAccountAmount(request.getBalance().negate());
            accountFlowTxb.setFlowDate(new Date());
            accountFlowTxb.setFlowDetail("?????????????????????");
            accountFlowTxb.setBusinessTypeId(BusinessTypeEnum.REDUCEKG.getStatus());
            accountFlowTxb.setFlowStatus(FlowStatusEnum.NOSTATUS.getStatus());
            logger.info("???????????????????????????????????????????????????>>>>>>>>>>", JSON.toJSONString(accountFlowTxb));
            success = accountFlowTxbWMapper.insertSelective(accountFlowTxb) > 0;

            AccountInModel inModel = new AccountInModel();
            inModel.setUserId(request.getUserId());
            inModel.setBalance(request.getBalance().negate());
            logger.info("?????????????????????????????????????????????>>>>>>>>>>", JSON.toJSONString(inModel));
            success = accountWMapper.addTxbBalance(inModel) > 0;

            accountFlowTxb = new AccountFlowTxb();
            accountFlowTxb.setAccountFlowId(idGenerater.nextId());
            accountFlowTxb.setRelationFlowId(flowid);
            accountFlowTxb.setUserId(Constants.PLATFORM_USER_ID);
            accountFlowTxb.setAmount(request.getBalance());
            accountFlowTxb.setAccountAmount(request.getBalance());
            accountFlowTxb.setFlowDate(new Date());
            accountFlowTxb.setFlowDetail("?????????????????????");
            accountFlowTxb.setBusinessTypeId(BusinessTypeEnum.REDUCEKG.getStatus());
            accountFlowTxb.setFlowStatus(FlowStatusEnum.NOSTATUS.getStatus());
            logger.info("?????????????????????????????????????????????>>>>>>>>>>", JSON.toJSONString(accountFlowTxb));
            success = accountFlowTxbWMapper.insertSelective(accountFlowTxb) > 0;

            inModel = new AccountInModel();
            inModel.setUserId(Constants.PLATFORM_USER_ID);
            inModel.setBalance(request.getBalance());
            success = accountWMapper.addTxbBalance(inModel) > 0;

        } finally {
            lock.unLock();
        }
        return success;
    }

    @Override
    public String getCode() {
        String myCode = RandomUtils.getRandomString(6);
        while (checkCode(myCode)) {
            myCode = RandomUtils.getRandomString(6);
        }
        return myCode;
    }

    @Override
    public Boolean checkCode(String code) {
        return kgMinerRobRMapper.checkCode(code) > 0;
    }

    @Override
    public AppJsonEntity myRobList(UserkgResponse kguser) {
        KgMinerRobInModel inModel = new KgMinerRobInModel();
        inModel.setUserId(Long.valueOf(kguser.getUserId()));
        List<KgMinerRobOutModel> outModels = kgMinerRobRMapper.myRobList(inModel);
        List<KgMinerRobResponse> responses = new ArrayList<>();
        for (KgMinerRobOutModel outModel : outModels) {
            KgMinerRobResponse response = new KgMinerRobResponse();
            if (outModel.getAssistAmount() == null) {
                response.setAssistAmount(0);
            } else {
                response.setAssistAmount(outModel.getAssistAmount());
            }
            response.setMinerId(outModel.getMinerId().toString());
            response.setRobId(outModel.getRobId().toString());
            response.setMinerName(outModel.getMinerName());
            response.setMinerPhoto(outModel.getMinerPhoto());
            response.setMinerNumber(outModel.getMinerNumber());
            String remainTime = "0";
            long interval = outModel.getMinerEndDate().getTime() - System.currentTimeMillis();
            if (interval > 0) {
                remainTime = String.valueOf(interval);
            }
            response.setRemainTime(remainTime);
            response.setMinerPrice(outModel.getMinerPrice().toString());
            response.setRobStatus(outModel.getRobStatus());
            responses.add(response);
        }
        Map<String, List<KgMinerRobResponse>> map = new HashMap<>();
        map.put("myRobList", responses);
        return AppJsonEntity.makeSuccessJsonEntity(map);
    }

    @Override
    @Transactional
    public AppJsonEntity friendHelp(UserkgResponse kguser, KgActivityMinerRequest request) {
        logger.info("----------??????????????????-----------");
        if (request == null || kguser == null || request.getAssistCode() == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        logger.info("----------???????????????????????????-----------");
        KgMinerRobOutModel kgMinerRobOutModel = kgMinerRobRMapper.selectByCode(request.getAssistCode());
        if (kgMinerRobOutModel == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.INVALID_ASSINT_CODE_ERROR.getCode(),
                    ExceptionEnum.INVALID_ASSINT_CODE_ERROR.getMessage());
        }
        logger.info("-----------????????????????????????????????????-----------------");
        KgCommonSettingInModel inModel = new KgCommonSettingInModel();
        inModel.setSettingKey(MinerActivityDateResponse.SETTING_KEY);
        KgCommonSettingOutModel outModel = kgCommonSettingRMapper.selectBySettingKey(inModel);
        MinerActivityDateResponse response = JSON.parseObject(outModel.getSettingValue(),
                MinerActivityDateResponse.class);
        Date aStartDate = DateUtils.parseDate(response.getStartTime(), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
        Date aEndDate = DateUtils.parseDate(response.getEndTime(), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
        logger.info("----------????????????????????????????????????--------------");
        if (DateUtils.compareDate(new Date(), aStartDate) < 0) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACTIVITY_START.getCode(),
                    ExceptionEnum.ACTIVITY_START.getMessage());
        }
        logger.info("----------????????????????????????????????????--------------");
        if (DateUtils.compareDate(new Date(), aEndDate) > 0) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACTIVITY_END.getCode(),
                    ExceptionEnum.ACTIVITY_END.getMessage());
        }

        logger.info("------------?????????????????????????????????????????????--------------");
        KgActivityMinerOutModel miner = kgActivityMinerRMapper.selectByPrimaryKey(kgMinerRobOutModel.getMinerId());
        if (miner == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        logger.info("----------??????????????????????????????????????????--------------");
        Date startDate = miner.getMinerStartDate();
        Date endDate = miner.getMinerEndDate();
        if (DateUtils.compareDate(new Date(), startDate) < 0) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACTIVITY_START.getCode(),
                    ExceptionEnum.ACTIVITY_START.getMessage());
        }
        logger.info("----------??????????????????????????????????????????--------------");
        if (DateUtils.compareDate(new Date(), endDate) > 0) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACTIVITY_END.getCode(),
                    ExceptionEnum.ACTIVITY_END.getMessage());
        }

        logger.info("----------??????????????????????????????--------------");
        if (miner.getMinerNumber() == 0) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.NONE_MINER_ERROR.getCode(),
                    ExceptionEnum.NONE_MINER_ERROR.getMessage());
        }

        logger.info("----------???????????????????????????----------");
        if (kgMinerRobOutModel.getRobStatus() == 1) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.ROB_STATUS_ERROR.getCode(),
                    ExceptionEnum.ROB_STATUS_ERROR.getMessage());
        }

        logger.info("----------??????????????????????????????-----------");
        if (kguser.getUserId().equals(kgMinerRobOutModel.getUserId().toString())) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.MINER_SELF_ASSIT.getCode(),
                    ExceptionEnum.MINER_SELF_ASSIT.getMessage());
        }
        KgMinerAssistInModel aInModel = new KgMinerAssistInModel();
        aInModel.setUserId(Long.valueOf(kguser.getUserId()));
        aInModel.setRobId(kgMinerRobOutModel.getRobId());
        int assitStatus = kgMinerAssistRMapper.checkUserAssitStatus(aInModel);
        logger.info("----------???????????????????????????????????????----------" + assitStatus);
        if (assitStatus > 0) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.TODAY_ASSIST_ERROR.getCode(),
                    ExceptionEnum.TODAY_ASSIST_ERROR.getMessage());
        }
        int assitTimes = kgMinerAssistRMapper.checkRobMinnerTimes(aInModel);
        logger.info("----------???????????????????????????????????????----------" + assitTimes);
        if (assitTimes >= Constants.MAX_ASSIST_TIMES) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.MAX_ASSIST_ROB_TIMES_ERROR.getCode(),
                    ExceptionEnum.MAX_ASSIST_ROB_TIMES_ERROR.getMessage());
        }

        logger.info("----------??????????????????????????????-----------");
        System.out.println(">>>>>>>>>" + JsonUtil.writeValueAsString(kguser));
        Date registDate = kguser.getCreateDate();
        logger.info("----------??????????????????-----------");
        String jedisKey = JedisKey.getMinerAssit(kguser.getUserId() + "_" + request.getAssistCode());
        if (jedisUtils.get(jedisKey) != null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.REPEAT_ERROR.getCode(),
                    ExceptionEnum.REPEAT_ERROR.getMessage());
        }
        jedisUtils.set(jedisKey, kguser.getUserId() + "_" + request.getAssistCode(), 5);

        int assitCount = 0;
        logger.info("----------????????????????????????????????????-----------");
        int firstAssist = kgMinerAssistRMapper.checkUserFirstAssit(aInModel);

        int a = miner.getMinerAssistFrequency() - kgMinerAssistRMapper.getTotalAssist(aInModel);
        logger.info("----------????????????????????????????????????-----------" + a);
        if (DateUtils.compareDate(registDate, aStartDate) >= 0 && DateUtils.compareDate(registDate, aEndDate) <= 0
                && firstAssist == 0 && a > 1) {
            logger.info("----------???????????????????????????????????????????????????-----------");
            assitCount = Constants.MINER_ASSIST_NEWUSER_COUNT;
        } else {
            logger.info("----------??????????????????????????????????????????????????????-----------");
            assitCount = Constants.MINER_ASSIST_OLDUSER_COUNT;
        }
        Integer assistAmount = 0;
        BigDecimal hasAssistAmount = BigDecimal.ZERO;
        Integer hasCount = 0;

        // ???????????????2/3
        BigDecimal y = (miner.getMinerPrice().divide(new BigDecimal(3), 2, BigDecimal.ROUND_HALF_UP));
        // y/3
        y = y.multiply(new BigDecimal(2)).setScale(0, BigDecimal.ROUND_HALF_UP);

        for (int i = 0; i < assitCount; i++) {
            logger.info("----------????????????-----------");
            logger.info("----------??????????????????-----------");

            KgMinerAssistInModel kgMinerAssistInModel = new KgMinerAssistInModel();
            kgMinerAssistInModel.setRobId(kgMinerRobOutModel.getRobId());
            kgMinerAssistInModel.setMinerPrice(miner.getMinerPrice());
            kgMinerAssistInModel.setAssistNumber(miner.getMinerAssistFrequency());
            // bug1:??????????????? X 2/3 ????????????????????????-????????????

            // y-?????????????????????
            y = y.subtract(hasAssistAmount);

            kgMinerAssistInModel.setMinerPrice(y);
            kgMinerAssistInModel.setAssistNumber(miner.getMinerAssistFrequency() - hasCount);

            logger.info(JsonUtil.writeValueAsString(">>>>>>>>>>>" + kgMinerAssistInModel));

            KgMinerAssistAmountOutModel kgMinerAssistAmountOutModel = this.getAssistAmount(kgMinerAssistInModel);
            kgMinerAssistInModel = new KgMinerAssistInModel();
            kgMinerAssistInModel.setAssistDate(new Date());
            kgMinerAssistInModel.setAssistId(idGenerater.nextId());
            kgMinerAssistInModel.setRobId(kgMinerRobOutModel.getRobId());
            kgMinerAssistInModel.setAssistStatus(1);
            kgMinerAssistInModel.setMinerId(miner.getMinerId());
            kgMinerAssistInModel.setUserId(Long.valueOf(kguser.getUserId()));
            kgMinerAssistInModel.setAssistAmount(kgMinerAssistAmountOutModel.getAssistAmount().intValue());
            assistAmount += kgMinerAssistAmountOutModel.getAssistAmount();
            logger.info("----------????????????????????????-----------" + JsonUtil.writeValueAsString(kgMinerAssistInModel));
            kgMinerAssistWMapper.insertSelective(kgMinerAssistInModel);

            KgActivityMinerInModel kgActivityMinerInModel = new KgActivityMinerInModel();
            kgActivityMinerInModel.setMinerId(miner.getMinerId());
            if (kgMinerAssistAmountOutModel.getIsFull() == 1) {
                logger.info("----------???????????????-----------");
                KgMinerRobInModel kgMinerRobInModel = new KgMinerRobInModel();
                kgMinerRobInModel.setRobId(kgMinerRobOutModel.getRobId());
                kgMinerRobInModel.setRobStatus(1);
                kgMinerRobInModel.setDealDate(new Date());
                kgMinerRobWMapper.updateByPrimaryKeySelective(kgMinerRobInModel);
                // ????????????????????????
                kgActivityMinerWMapper.reduceMinerNumber(kgActivityMinerInModel);
                break;
            }
            hasAssistAmount = hasAssistAmount.add(new BigDecimal(kgMinerAssistAmountOutModel.getAssistAmount()));
            hasCount += 1;

            // ????????????????????????
            // kgActivityMinerWMapper.addJoinNums(kgActivityMinerInModel);
        }
        Map<String, Integer> obj = new HashMap<String, Integer>();
        obj.put("assistAmount", assistAmount);
        return AppJsonEntity.makeSuccessJsonEntity(obj);
    }

    @Override
    public KgMinerAssistAmountOutModel getAssistAmount(KgMinerAssistInModel kgMinerAssistInModel) {

        KgMinerAssistAmountOutModel kgMinerAssistAmountOutModel = new KgMinerAssistAmountOutModel();
        logger.info("----------??????????????????????????????????????? ????????????-----------");
        KgMinerAssistOutModel k = kgMinerAssistRMapper.getTotalAssistInfo(kgMinerAssistInModel);

        BigDecimal assitAmount = kgMinerAssistInModel.getMinerPrice();
        logger.info("----------??????????????????????????????-----------" + assitAmount);

        BigDecimal assitNumber = new BigDecimal(kgMinerAssistInModel.getAssistNumber());
        logger.info("----------???a??????????????????????????????-----------" + assitNumber);

        if (k.getAssistAmount() != null) {
            BigDecimal b = new BigDecimal(k.getAssistAmount());
            assitAmount = assitAmount.subtract(b);
        }

        if (k.getAssistCount() != null) {
            assitNumber = assitNumber.subtract(new BigDecimal(k.getAssistCount()))
                    .subtract(new BigDecimal(kgMinerAssistInModel.getNowAssitCount()));
            logger.info("----------??????????????????-----------" + assitAmount);
            logger.info("----------???????????????????????????????????????----------" + assitAmount);
            if (assitNumber.compareTo(BigDecimal.ONE) <= 0) {
                // ?????????????????????????????? ???????????????????????????
                kgMinerAssistAmountOutModel.setAssistAmount(assitAmount.intValue());
                kgMinerAssistAmountOutModel.setIsFull(1);
                return kgMinerAssistAmountOutModel;
            }
        }

        BigDecimal devideAssitNumber = assitAmount.divide(assitNumber, 0, BigDecimal.ROUND_HALF_UP);

        int rate = (devideAssitNumber.multiply(new BigDecimal(2))).intValue();

        int randomRate = RandomUtils.randomInt(1, rate);
        logger.info("----------???????????????-----------" + randomRate);
        kgMinerAssistAmountOutModel.setAssistAmount(randomRate);
        kgMinerAssistAmountOutModel.setIsFull(0);
        return kgMinerAssistAmountOutModel;
    }

    public AppJsonEntity detailRob(KgMinerRobRequest request) {
        if (request.getRobId() == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        KgMinerRobInModel kgMinerRobInModel = new KgMinerRobInModel();
        kgMinerRobInModel.setRobId(request.getRobId());
        KgMinerRobOutModel kgMinerRobOutModel = kgMinerRobRMapper.detailRob(kgMinerRobInModel);
        if (kgMinerRobOutModel == null) {
            return AppJsonEntity.makeExceptionJsonEntity("", "???????????????");
        }
        KgMinerRobResponse response = new KgMinerRobResponse();
        response.setRobId(kgMinerRobOutModel.getRobId().toString());
        response.setRobAvatar(kgMinerRobOutModel.getRobAvatar());
        response.setRobName(kgMinerRobOutModel.getRobName());
        response.setAssistCode(kgMinerRobOutModel.getAssistCode());
        if (kgMinerRobOutModel.getAssistAmount() == null) {
            response.setAssistAmount(0);
        } else {
            response.setAssistAmount(kgMinerRobOutModel.getAssistAmount());
        }
        KgActivityMinerOutModel activityMinerOutModel = kgActivityMinerRMapper
                .selectByPrimaryKey(kgMinerRobOutModel.getMinerId());
        response.setMinerPhoto(activityMinerOutModel.getMinerPhoto());
        response.setMinerName(activityMinerOutModel.getMinerName());
        response.setMinerNumber(activityMinerOutModel.getMinerNumber());
        response.setMinerId(activityMinerOutModel.getMinerId().toString());
        String remainTime = "0";
        long interval = activityMinerOutModel.getMinerEndDate().getTime() - System.currentTimeMillis();
        if (interval > 0) {
            remainTime = String.valueOf(interval);
        }
        response.setRemainTime(remainTime);
        response.setMinerLink(activityMinerOutModel.getMinerLink());
        response.setMinerJoinFrequency(activityMinerOutModel.getMinerJoinFrequency());
        response.setMinerPrice(activityMinerOutModel.getMinerPrice().toString());
        response.setMinerDesc(activityMinerOutModel.getMinerDesc());

        Map<String, KgMinerRobResponse> map = new HashMap<>();
        map.put("robDetail", response);
        return AppJsonEntity.makeSuccessJsonEntity(map);
    }

    @Override
    public AppJsonEntity assistList(KgMinerAssistRequest request) {
        if (request.getRobId() == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        PageModel pageModel = new PageModel();
        if (request.getCurrentPage() == null) {
            pageModel.setCurrentPage(1);
        } else {
            pageModel.setCurrentPage(request.getCurrentPage());
        }
        pageModel.setPageSize(request.getPageSize());
        KgMinerAssistInModel inModel = new KgMinerAssistInModel();
        inModel.setStart((pageModel.getCurrentPage() - 1) * pageModel.getPageSize());
        inModel.setLimit(pageModel.getPageSize());
        inModel.setRobId(request.getRobId());
        List<KgMinerAssistOutModel> outModels = kgMinerAssistRMapper.assistList(inModel);
        List<KgMinerAssistResponse> responses = new ArrayList<>();
        buildKgMinerAssistResponse(responses, outModels);
        Map<String, Object> map = new HashMap<>();
        map.put("assistList", responses);

        // ?????????
        KgMinerAssistInModel minerAssistInModel = new KgMinerAssistInModel();
        minerAssistInModel.setRobId(request.getRobId());
        long sum = kgMinerAssistRMapper.countAssistList(minerAssistInModel);
        long totalPage = (sum + request.getPageSize() - 1) / (request.getPageSize());
        map.put("totalPage", totalPage);
        return AppJsonEntity.makeSuccessJsonEntity(map);
    }

    @Override
    public AppJsonEntity minerTime() {
        KgCommonSettingInModel inModel = new KgCommonSettingInModel();
        inModel.setSettingKey(MinerActivityDateResponse.SETTING_KEY);
        KgCommonSettingOutModel outModel = kgCommonSettingRMapper.selectBySettingKey(inModel);
        MinerActivityDateResponse response = JSON.parseObject(outModel.getSettingValue(),
                MinerActivityDateResponse.class);
        String startTime = String.valueOf(
                (DateUtils.parseDate(response.getStartTime(), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS)).getTime());
        String endTime = String.valueOf(
                (DateUtils.parseDate(response.getEndTime(), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS)).getTime());
        response.setStartTime(startTime);
        response.setEndTime(endTime);
        Map<String, MinerActivityDateResponse> map = new HashMap<>();
        map.put("minerActivityTime", response);
        return AppJsonEntity.makeSuccessJsonEntity(map);
    }

    @Override
    public AppJsonEntity minerProgressList(KgMinerAssistRequest request) {
        if (request.getMinerId() == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        KgMinerRobInModel kgMinerRobInModel = new KgMinerRobInModel();
        kgMinerRobInModel.setMinerId(request.getMinerId());
        List<KgMinerRobOutModel> kgMinerAssistOutModels = kgMinerRobRMapper.topTenList(kgMinerRobInModel);
        List<KgMinerRobResponse> responses = new ArrayList<>();
        for (KgMinerRobOutModel outModel : kgMinerAssistOutModels) {
            KgMinerRobResponse response = new KgMinerRobResponse();
            response.setRobAvatar(outModel.getRobAvatar());
            response.setRobName(outModel.getRobName());
            if (outModel.getAssistAmount() == null) {
                response.setAssistAmount(0);
            } else {
                response.setAssistAmount(outModel.getAssistAmount());
            }
            response.setRobStatus(outModel.getRobStatus());
            responses.add(response);
        }
        KgActivityMinerOutModel kgActivityMinerOutModel = kgActivityMinerRMapper
                .selectByPrimaryKey(request.getMinerId());
        KgActivityMinerResponse kgActivityMinerResponse = new KgActivityMinerResponse();
        kgActivityMinerResponse.setMinerPhoto(kgActivityMinerOutModel.getMinerPhoto());
        kgActivityMinerResponse.setMinerName(kgActivityMinerOutModel.getMinerName());
        kgActivityMinerResponse.setMinerPrice(kgActivityMinerOutModel.getMinerPrice().toString());
        kgActivityMinerResponse.setMinerNumber(kgActivityMinerOutModel.getMinerNumber());

        Map<String, Object> map = new HashMap<>();
        map.put("minerInfo", kgActivityMinerResponse);
        map.put("topTenList", responses);
        return AppJsonEntity.makeSuccessJsonEntity(map);
    }

    @Override
    public AppJsonEntity activityPopInfo(HttpServletRequest request) {

        KgCommonSettingInModel inModel = new KgCommonSettingInModel();
        inModel.setSettingKey(ActivityPopInfoResponse.SETTING_KEY);
        KgCommonSettingOutModel outModel = kgCommonSettingRMapper.selectBySettingKey(inModel);
        if(outModel==null||StringUtils.isBlank(outModel.getSettingValue())){
            //??????????????????
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
        }

        ActivityPopInfoResponse response = JSON.parseObject(outModel.getSettingValue(), ActivityPopInfoResponse.class);
        //???????????????IOS???Android????????????
        if(request.getIntHeader("os_version")==1&&response.getIosSwitch()==0){
            //IOS??????????????????
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
        }
        if(request.getIntHeader("os_version")==2&&response.getAndroidSwitch()==0){
            //Android??????????????????
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
        }

		Map<String, Object> map = new HashMap<>();
        //??????activityKey??????????????????
		if(response.getType()==0||response.getType()==1){
        	//????????????????????????????????????
			inModel.setSettingKey(response.getActivityKey());
			KgCommonSettingOutModel activityTime = kgCommonSettingRMapper.selectBySettingKey(inModel);
			MinerActivityDateResponse dateResponse = JSON.parseObject(activityTime.getSettingValue(), MinerActivityDateResponse.class);
			long start = (DateUtils.parseDate(dateResponse.getStartTime(), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS)).getTime();
			long end = (DateUtils.parseDate(dateResponse.getEndTime(), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS)).getTime();
			long now = System.currentTimeMillis();
			//????????????????????????
			if (!(start <= now && now < end)) {

				return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);

			}
			map.put("ifStart",1);
		}
        map.put("activityPopInfo", response);
        return AppJsonEntity.makeSuccessJsonEntity(map);
    }

    public void buildKgMinerAssistResponse(List<KgMinerAssistResponse> responses,
            List<KgMinerAssistOutModel> kgMinerAssistOutModels) {
        for (KgMinerAssistOutModel outModel : kgMinerAssistOutModels) {
            KgMinerAssistResponse response = new KgMinerAssistResponse();
            response.setAssistAvatar(outModel.getAssistAvatar());
            response.setAssistName(outModel.getAssistName());
            response.setAssistAmount(outModel.getAssistAmount());
            responses.add(response);
        }
    }

    @Override
    public AppJsonEntity validRushMiner(UserkgResponse kguser, KgActivityMinerRequest request) {
        logger.info("----------??????????????????-----------");
        if (request == null || kguser == null || request.getMinerId() == null
                || StringUtils.isEmpty(kguser.getUserId())) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        logger.info("-----------????????????????????????????????????-----------------");
        KgCommonSettingInModel inModel = new KgCommonSettingInModel();
        inModel.setSettingKey(MinerActivityDateResponse.SETTING_KEY);
        KgCommonSettingOutModel outModel = kgCommonSettingRMapper.selectBySettingKey(inModel);
        MinerActivityDateResponse response = JSON.parseObject(outModel.getSettingValue(),
                MinerActivityDateResponse.class);
        Date startDate = DateUtils.parseDate(response.getStartTime(), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
        Date endDate = DateUtils.parseDate(response.getEndTime(), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
        if (DateUtils.compareDate(new Date(), startDate) < 0) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACTIVITY_START.getCode(),
                    ExceptionEnum.ACTIVITY_START.getMessage());
        }
        logger.info("----------????????????????????????????????????--------------");
        if (DateUtils.compareDate(new Date(), endDate) > 0) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACTIVITY_END.getCode(),
                    ExceptionEnum.ACTIVITY_END.getMessage());
        }

        logger.info("------------?????????????????????????????????????????????--------------");
        KgActivityMinerOutModel miner = kgActivityMinerRMapper.selectByPrimaryKey(request.getMinerId());
        if (miner == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        logger.info("----------????????????????????????????????????--------------");
        startDate = miner.getMinerStartDate();
        endDate = miner.getMinerEndDate();
        if (DateUtils.compareDate(new Date(), startDate) < 0) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACTIVITY_START.getCode(),
                    ExceptionEnum.ACTIVITY_START.getMessage());
        }
        logger.info("----------????????????????????????????????????--------------");
        if (DateUtils.compareDate(new Date(), endDate) > 0) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACTIVITY_END.getCode(),
                    ExceptionEnum.ACTIVITY_END.getMessage());
        }

        logger.info("----------??????????????????????????????--------------");
        if (miner.getMinerNumber() == 0) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACTIVITY_END.getCode(),
                    ExceptionEnum.ACTIVITY_END.getMessage());
        }

        logger.info("----------??????????????????????????????--------------");
        KgMinerRobInModel kgMinerRobInModel = new KgMinerRobInModel();
        kgMinerRobInModel.setMinerId(miner.getMinerId());
        kgMinerRobInModel.setUserId(Long.valueOf(kguser.getUserId()));
        KgMinerRobOutModel kgMinerRobOutModel = kgMinerRobRMapper.checkIsRob(kgMinerRobInModel);
        if (kgMinerRobOutModel != null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.ROBED_MINNER_ERROR.getCode(),
                    kgMinerRobOutModel.getRobId() + "");
        }
        return AppJsonEntity.makeSuccessJsonEntity("??????");
    }

    @Override
    public AppJsonEntity worldCupTime() {
        KgCommonSettingInModel inModel = new KgCommonSettingInModel();
        inModel.setSettingKey(WorldcupActivityDateResponse.SETTING_KEY);
        KgCommonSettingOutModel outModel = kgCommonSettingRMapper.selectBySettingKey(inModel);
        WorldcupActivityDateResponse response = JSON.parseObject(outModel.getSettingValue(),
                WorldcupActivityDateResponse.class);
        response.setStartTime(String.valueOf(
                (DateUtils.parseDate(response.getStartTime(), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS)).getTime()));
        response.setEndTime(String.valueOf(
                (DateUtils.parseDate(response.getEndTime(), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS)).getTime()));
        Map<String, WorldcupActivityDateResponse> map = new HashMap<>();
        long now = System.currentTimeMillis();
        if (Long.parseLong(response.getStartTime()) <= now && now < Long.parseLong(response.getEndTime())) {
            // ???????????????
            response.setIfStart(1);
        } else if (Long.parseLong(response.getStartTime()) > now) {
            // ??????????????????
            response.setIfStart(0);
        } else {
            // ???????????????
            response.setIfStart(2);
        }
        map.put("worldCupActivityTime", response);
        return AppJsonEntity.makeSuccessJsonEntity(map);
    }

    @Override
    public AppJsonEntity worldCupCompetionList(KgActivityCompetionRequest request) {
        KgActivityCompetionInModel inModel = new KgActivityCompetionInModel();
        inModel.setUserId(request.getUserId());
        // ????????????????????????
        AppJsonEntity reult = worldCupTime();
        Map<String, WorldcupActivityDateResponse> maps = (Map) reult.getData();
        WorldcupActivityDateResponse dateResponse = maps.get("worldCupActivityTime");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(Long.parseLong(dateResponse.getEndTime())));
        calendar.add(Calendar.DATE, 1);
        Date Time = calendar.getTime();
        String endTime = DateUtils.formatYMD(Time);
        inModel.setEndTime(endTime);

        List<KgActivityCompetionOutModel> outModels = kgActivityCompetionRMapper.worldCupCompetionList(inModel);
        List<KgActivityCompetionResponse> responses = new ArrayList<>();
        for (KgActivityCompetionOutModel outModel : outModels) {
            KgActivityCompetionResponse response = new KgActivityCompetionResponse();
            response.setCompetionId(outModel.getCompetionId());
            response.setHomeTeamLogo(outModel.getHomeTeamLogo());
            response.setGuestTeamLogo(outModel.getGuestTeamLogo());
            response.setHomeTeamName(outModel.getHomeTeamName());
            response.setGuestTeamName(outModel.getGuestTeamName());
            response.setIfGuess(outModel.getGuessCount());
            long now = System.currentTimeMillis();
            if (outModel.getCompetitionDate().getTime() <= now) {
                // ??????????????????
                response.setIfStart(1);
            } else {
                response.setIfStart(0);
            }
            response.setCompetitionDate(String.valueOf(outModel.getCompetitionDate().getTime()));
            response.setHomeTeamId(outModel.getHomeTeamId());
            response.setGuestTeamId(outModel.getGuestTeamId());
            response.setSupportTeamId(outModel.getSupportTeamId());
            response.setHomeTeamPanda(outModel.getHomeTeamPanda());
            response.setGuestTeamPanda(outModel.getGuestTeamPanda());
            responses.add(response);
        }
        Map<String, List<KgActivityCompetionResponse>> map = new HashMap<>();
        map.put("worldCupCompetionList", responses);
        return AppJsonEntity.makeSuccessJsonEntity(map);
    }

    @Override
    public AppJsonEntity checkMatch(KgActivityCompetionRequest request) {
        KgCommonSettingInModel inModel = new KgCommonSettingInModel();
        inModel.setSettingKey(WorldcupActivityDateResponse.SETTING_KEY);
        KgCommonSettingOutModel outModel = kgCommonSettingRMapper.selectBySettingKey(inModel);
        WorldcupActivityDateResponse response = JSON.parseObject(outModel.getSettingValue(),
                WorldcupActivityDateResponse.class);
        Date endDate = DateUtils.parseDate(response.getEndTime(), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
        logger.info("----------???????????????????????????????????????--------------");
        if (DateUtils.compareDate(new Date(), endDate) > 0) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACTIVITY_END.getCode(),
                    ExceptionEnum.ACTIVITY_END.getMessage());
        }
        logger.info("----------???????????????????????????--------------");
        int i = kgActivityCompetionRMapper.checkTodayMatch();
        if (i == 0) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACTIVITY_NO_MATCH.getCode(),
                    ExceptionEnum.ACTIVITY_NO_MATCH.getMessage());
        }
        return AppJsonEntity.makeSuccessJsonEntity("????????????????????????");
    }

    @SuppressWarnings("unchecked")
    @Override
    public AppJsonEntity guessWinner(KgActivityGuessRequest request) {
        logger.info("----------??????????????????-----------");
        if (request == null || request.getUserId() == null || request.getGuessInfo() == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        KgCommonSettingInModel inModel = new KgCommonSettingInModel();
        inModel.setSettingKey(WorldcupActivityDateResponse.SETTING_KEY);
        KgCommonSettingOutModel outModel = kgCommonSettingRMapper.selectBySettingKey(inModel);
        WorldcupActivityDateResponse response = JSON.parseObject(outModel.getSettingValue(),
                WorldcupActivityDateResponse.class);
        Date endDate = DateUtils.parseDate(response.getEndTime(), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
        logger.info("----------???????????????????????????????????????--------------");
        if (DateUtils.compareDate(new Date(), endDate) > 0) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACTIVITY_END.getCode(),
                    ExceptionEnum.ACTIVITY_END.getMessage());
        }
        logger.info("----------???????????????????????????--------------");
        List<ActivityInviteLogOutModel> userRelations = activityInviteLogRMapper
                .checkJoinWordCup(Long.valueOf(request.getUserId()));
        if (userRelations.size() == 0) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACTIVITY_INVITE_COUNT_ERROR.getCode(),
                    ExceptionEnum.ACTIVITY_INVITE_COUNT_ERROR.getMessage());
        }

        logger.info("----------??????????????????-----------");
        String jedisKey = JedisKey.getWorldCup(request.getUserId() + "");
        if (jedisUtils.get(jedisKey) != null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.REPEAT_ERROR.getCode(),
                    ExceptionEnum.REPEAT_ERROR.getMessage());
        }
        jedisUtils.set(jedisKey, request.getUserId(), 2);

        List<KgActivityGuessOutModel> a = new ArrayList<KgActivityGuessOutModel>();
        a = JsonUtil.readJson(request.getGuessInfo(), List.class, KgActivityGuessOutModel.class);
        if (a != null) {
            for (KgActivityGuessOutModel gu : a) {
                KgActivityGuessInModel kgActivityGuessInModel = new KgActivityGuessInModel();
                kgActivityGuessInModel.setCompetionId(gu.getCompetionId());
                kgActivityGuessInModel.setGuessDate(new Date());
                kgActivityGuessInModel.setSupportTeamId(gu.getSupportTeamId());
                kgActivityGuessInModel.setUserId(request.getUserId());

                logger.info("----------???????????????????????????--------------");
                KgActivityGuessOutModel s = kgActivityGuessRMapper.checkGuess(kgActivityGuessInModel);
                if (s == null) {
                    KgActivityGuessWMapper.insertSelective(kgActivityGuessInModel);
                }
            }
        }
        logger.info("----------??????????????????--------------");
        ActivityInviteLogInModel activityInviteLogInModel = new ActivityInviteLogInModel();
        activityInviteLogInModel.setUserId(request.getUserId());
        activityInviteLogInModel.setActivityId(4);
        activityInviteLogWMapper.updateWordcupInviteStatus(activityInviteLogInModel);
        return AppJsonEntity.makeSuccessJsonEntity("????????????");
    }

    @Override
    public AppJsonEntity assistDetail(KgActivityCompetionRequest request) {
        logger.info("----------??????????????????-----------");
        Map<String, Object> data = new HashMap<>();
        if (request == null || request.getUserId() == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        KgCommonSettingInModel inModel = new KgCommonSettingInModel();
        inModel.setSettingKey(WorldcupActivityDateResponse.SETTING_KEY);
        KgCommonSettingOutModel outModel = kgCommonSettingRMapper.selectBySettingKey(inModel);
        WorldcupActivityDateResponse response = JSON.parseObject(outModel.getSettingValue(),
                WorldcupActivityDateResponse.class);
        Date endDate = DateUtils.parseDate(response.getEndTime(), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
        logger.info("----------???????????????????????????????????????--------------");
        if (DateUtils.compareDate(new Date(), endDate) > 0) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.ACTIVITY_END.getCode(),
                    ExceptionEnum.ACTIVITY_END.getMessage());
        }
        logger.info("----------?????????????????????????????????--------------");
        int i = kgActivityCompetionRMapper.checkTodayMatch();
        int isHavePlay = 1;
        if (i == 0) {
            isHavePlay = 0;
        }
        UserkgRequest userkgRequest = new UserkgRequest();
        userkgRequest.setUserId(request.getUserId());
        UserkgOutModel userkgOutModel = userkgService.getUserProfiles(userkgRequest);
        logger.info("????????????????????????>>>>>>>>>" + JsonUtil.writeValueAsString(userkgOutModel));
        if (userkgOutModel == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        if (userkgOutModel.getLockStatus().intValue() == LockStatusEnum.LOCKED.getStatus()) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.LOCKERROR.getCode(),
                    ExceptionEnum.LOCKERROR.getMessage());
        }
        logger.info("----------???????????????????????????--------------");
        List<ActivityInviteLogOutModel> userRelations = activityInviteLogRMapper
                .checkJoinWordCup(Long.valueOf(request.getUserId()));
        logger.info("----------?????????????????????????????????--------------" + userRelations.size());
        Integer isDraw = 0;
        if (userRelations.size() > 0) {
            isDraw = 1;
        }
        data.put("isHavePlay", isHavePlay);
        data.put("avatar", userkgOutModel.getAvatar());
        data.put("nickName", userkgOutModel.getUserName());
        data.put("inviteCode", userkgOutModel.getInviteCode());
        data.put("isDraw", isDraw);
        return AppJsonEntity.makeSuccessJsonEntity(data);
    }

    @Override
    public AppJsonEntity deepfinRequest(DeepfinRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("phone", request.getPhone());
        map.put("invitationCode", request.getInvitationCode());
        map.put("sign", request.getSign());
        String s = HttpUtil.post(request.getRequestUrl(), map);
        logger.info(">>>>>>>deepfinRequest>>>>>>>" + s);
        return null;
    }

}
