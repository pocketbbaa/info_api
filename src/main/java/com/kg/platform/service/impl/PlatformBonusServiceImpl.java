package com.kg.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.exception.ApiException;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.lock.Lock;
import com.kg.platform.common.mongo.MongoTables;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.mongo.Seach;
import com.kg.platform.common.mq.MQConfig;
import com.kg.platform.common.mq.MQProduct;
import com.kg.platform.common.utils.Bean2MapUtil;
import com.kg.platform.common.utils.Constants;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.common.utils.JsonUtil;
import com.kg.platform.dao.entity.BalanceDeducted;
import com.kg.platform.dao.entity.PushMessage;
import com.kg.platform.dao.entity.UserRelation;
import com.kg.platform.dao.read.ArticleRMapper;
import com.kg.platform.dao.read.KgInfoSwitchRMapper;
import com.kg.platform.dao.read.UserRMapper;
import com.kg.platform.dao.read.UserRelationRMapper;
import com.kg.platform.dao.write.AccountWMapper;
import com.kg.platform.dao.write.BalanceDeductedWMapper;
import com.kg.platform.enumeration.BusinessTypeEnum;
import com.kg.platform.enumeration.CoinEnum;
import com.kg.platform.enumeration.FlowStatusEnum;
import com.kg.platform.model.in.AccountInModel;
import com.kg.platform.model.in.ArticleInModel;
import com.kg.platform.model.mongoTable.AccountFlowKgMongo;
import com.kg.platform.model.mongoTable.SubTributeRecordMongo;
import com.kg.platform.model.out.ArticleOutModel;
import com.kg.platform.model.out.KgInfoSwitchOutModel;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.AccountRequest;
import com.kg.platform.service.PlatformBonusService;
import com.kg.platform.service.PushService;
import com.kg.platform.service.TokenManager;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.client.MongoCursor;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PlatformBonusServiceImpl implements PlatformBonusService {

    private static final String USER_ACCOUNT_LOCK_KEY = "kguser::AccountService::updateAddUbalance::";

    private static final Logger logger = LoggerFactory.getLogger(PlatformBonusServiceImpl.class);

    @Inject
    UserRMapper userRMapper;

    @Inject
    ArticleRMapper articleRMapper;

    @Inject
    IDGen idGenerater;

    @Inject
    AccountWMapper accountWMapper;

    @Inject
    UserRelationRMapper userRelationRMapper;

    @Inject
    MQProduct mqProduct;

    @Inject
    TokenManager manager;

    @Inject
    KgInfoSwitchRMapper kgInfoSwitchRMapper;

    @Autowired
    private MQConfig subTributeRecordMqConfig;

    @Autowired
    private BalanceDeductedWMapper balanceDeductedWMapper;

    @Autowired
    private PushService pushService;


    public void mqPush(String userId, String messageText, String tags) {


        pushService.getShareKgBonus(userId, "????????????", messageText, tags, 1);
    }

    @Override
    @Transactional
    public BigDecimal getReadBonus(AccountRequest request) {
        Boolean isok = true;
        Long startDate = null;
        Long endDate = null;
        Long userId = request.getUserId();
        logger.info("???????????????????????????????????????");
        ArticleInModel ainModel = new ArticleInModel();
        ainModel.setArticleId(request.getArticleId());
        ArticleOutModel articleOutModel = articleRMapper.getCreateArticle(ainModel);
        if (articleOutModel == null) {
            return null;
        }

//        AccountFlowInModel inModel = new AccountFlowInModel();
//        inModel.setArticleId(request.getArticleId());
//        inModel.setUserId(userId);
//        inModel.setBusinessTypeId(BusinessTypeEnum.READARTICLETKGAWARD.getStatus());
        logger.info("???????????????????????????????????????????????????");

        BasicDBObject querry = new BasicDBObject();
        querry.append("business_type_id", new BasicDBObject(Seach.EQ.getOperStr(), BusinessTypeEnum.READARTICLETKGAWARD.getStatus()));
        querry.append("user_id", new BasicDBObject(Seach.EQ.getOperStr(), userId));
        querry.append("article_id", new BasicDBObject(Seach.EQ.getOperStr(), request.getArticleId()));
        startDate = DateUtils.getDateStart(new Date()).getTime();
        endDate = DateUtils.getDateEnd(new Date()).getTime();
        querry.append("flow_date", BasicDBObjectBuilder.start(Seach.GTE.getOperStr(), startDate)
                .add(Seach.LTE.getOperStr(), endDate).get());
        Long bonusCount = MongoUtils.count(MongoTables.ACCOUNT_FLOW_KG, querry);
        //int bonusCount = accountFlowTxbRMapper.checkArticleBonus(inModel);
        logger.info(">>>>>>querry>>>>>>" + querry.toString());
        logger.info(">>>>>>bonusCount>>>>>>" + bonusCount);
        if (bonusCount > 0) {
            return null;
        }
        logger.info("??????????????????????????????20?????????");
        querry.remove("article_id");
        bonusCount = MongoUtils.count(MongoTables.ACCOUNT_FLOW_KG, querry);
        logger.info(">>>>>>querry>>>>>>" + querry.toString());
        logger.info(">>>>>>bonusCount>>>>>>" + bonusCount);
        //bonusCount = accountFlowTxbRMapper.selectTypeFlowCount(inModel);

        if (bonusCount >= Constants.PALTFORMREADCOUNT) {
            return null;
        }
        logger.info("????????????????????????");
        long flowid = idGenerater.nextId();
        request.setDeleteUserFlowId(flowid);
        request.setFlowId(flowid);
        request.setBonusType("1");
        request.setUserId(userId);
        try {
            isok = this.getPlatformReadKgBonus(request);
            logger.info("????????????????????????");
            UserRelation userRelation = userRelationRMapper.selectParentUser(request.getUserId());
            if (userRelation != null) {
                request.setRelationFlowId(flowid);
                flowid = idGenerater.nextId();
                request.setFlowId(flowid);
                request.setBonusType("2");
                request.setSubUserId(userRelation.getRelUserId());
                request.setUserId(userRelation.getUserId());
                isok = this.getPlatformReadKgBonus(request);

                //??????????????????
                SubTributeRecordMongo subTributeRecordMongo = new SubTributeRecordMongo();
                subTributeRecordMongo.setTribute_id(idGenerater.nextId());
                subTributeRecordMongo.setCoin_type(2);
                subTributeRecordMongo.setAmount(Constants.READMASTERKGBONUS);
                subTributeRecordMongo.setSub_id(userRelation.getRelUserId());
                subTributeRecordMongo.setSub_name(userRelation.getSubName());
                subTributeRecordMongo.setMaster_id(userRelation.getUserId());
                subTributeRecordMongo.setMaster_name(userRelation.getUserName());
                subTributeRecordMongo.setCreate_date(new Date().getTime());
                logger.info("---------------????????????????????????-------------------" + JsonUtil.writeValueAsString(subTributeRecordMongo));
                mqProduct.sendMessage(subTributeRecordMqConfig.getTopic(), subTributeRecordMqConfig.getTags(), "", JsonUtil.writeValueAsString(subTributeRecordMongo));
            }
        } catch (Exception e) {
            delteMonggoRecord(request);
            throw new ApiException("????????????-------" + e.toString());
        }
        // app1.1.0????????????????????????
        // KgInfoSwitchOutModel outModel =
        // kgInfoSwitchRMapper.selectByPrimaryKey(request.getUserId());
        // boolean ifPush = false;
        // if (outModel == null) {
        // ifPush = true;
        // } else if (outModel.getSystemInfoSwitch().intValue() == 1) {
        // ifPush = true;
        // }
        // if (isok && ifPush) {
        // String messageText = "";
        // // ?????????????????????????????????
        // logger.info(">>>>>>>>>>??????????????????????????????");
        // messageText = "???????????????" + articleOutModel.getArticleTitle() + "?????????????????????"
        // + Constants.READKGBONUS + "KG";
        // if (userRelation != null) {
        // mqPush(request.getSubUserId().toString(), messageText,
        // "platformReward");
        // } else {
        // mqPush(request.getUserId().toString(), messageText,
        // "platformReward");
        // }
        //
        // }
        return Constants.READKGBONUS;
    }

    @Override
    public Boolean getPlatformReadKgBonus(AccountRequest request) {
        logger.info("??????????????????KG???{}", JSON.toJSONString(request.getUserId()));
        boolean success;
        Lock lock = new Lock();
        // Long userId = request.getUserId();
        String key = USER_ACCOUNT_LOCK_KEY + "READ_KG_BONUS" + request.getUserId();
        try {
            lock.lock(key);
            if (request.getUserId() == null) {
                throw new ApiException("???????????? ????????????ID?????????" + request.getUserId());
            }
            // ???????????????
            UserkgOutModel userkgModel = userRMapper.getUserDetails(request.getUserId());
            if (userkgModel == null) {
                throw new ApiException("???????????? ????????????????????? userId:" + request.getUserId());
            }

            ArticleInModel ainModel = new ArticleInModel();
            ainModel.setArticleId(request.getArticleId());
            ArticleOutModel articleOutModel = articleRMapper.getCreateArticle(ainModel);

            AccountFlowKgMongo accountFlowKg = new AccountFlowKgMongo();
            accountFlowKg.setAccount_flow_id(request.getFlowId());
            if (request.getRelationFlowId() != null) {
                accountFlowKg.setRelation_flow_id(request.getRelationFlowId());
            } else {
                accountFlowKg.setRelation_flow_id(request.getFlowId());
            }
            accountFlowKg.setUser_id(request.getUserId());
            accountFlowKg.setUser_phone(userkgModel.getUserMobile());
            accountFlowKg.setUser_name(userkgModel.getUserName());
            accountFlowKg.setUser_email(userkgModel.getUserEmail());
            if ("1".equals(request.getBonusType())) {
                accountFlowKg.setAmount(Constants.READKGBONUS);
                accountFlowKg.setAccount_amount(Constants.READKGBONUS);
                accountFlowKg.setFlow_detail("???????????????" + articleOutModel.getArticleTitle() + "???,????????????????????????");
                accountFlowKg.setBusiness_type_id(BusinessTypeEnum.READARTICLETKGAWARD.getStatus());
            } else if ("2".equals(request.getBonusType())) {
                UserkgOutModel subUserkgModel = userRMapper.getUserDetails(request.getSubUserId());
                accountFlowKg.setAmount(Constants.READMASTERKGBONUS);
                accountFlowKg.setAccount_amount(Constants.READMASTERKGBONUS);
                accountFlowKg.setFlow_detail("????????????" + subUserkgModel.getUserName() + "??????");
                accountFlowKg.setBusiness_type_id(BusinessTypeEnum.SUBCONTRI.getStatus());
            }
            accountFlowKg.setArticle_id(request.getArticleId());
            accountFlowKg.setFlow_date(new Date().getTime());
            accountFlowKg.setAccount_date(new Date().getTime());
            accountFlowKg.setFlow_status(FlowStatusEnum.NOSTATUS.getStatus());
            MongoUtils.insertOne(MongoTables.ACCOUNT_FLOW_KG, new Document(Bean2MapUtil.bean2map(accountFlowKg)));


            AccountInModel inModel = new AccountInModel();
            inModel.setUserId(request.getUserId());
            inModel.setBalance(accountFlowKg.getAmount());
            success = accountWMapper.addTxbBalance(inModel) > 0;

            accountFlowKg = new AccountFlowKgMongo();
            accountFlowKg.setAccount_flow_id(idGenerater.nextId());
            accountFlowKg.setRelation_flow_id(request.getFlowId());
            accountFlowKg.setUser_id(Constants.PLATFORM_USER_ID);
            if ("1".equals(request.getBonusType())) {
                accountFlowKg.setAmount(Constants.READKGBONUS.negate());
                accountFlowKg.setAccount_amount(Constants.READKGBONUS.negate());
                accountFlowKg.setFlow_detail("??????userId:" + request.getUserId() + "????????????");
                accountFlowKg.setBusiness_type_id(BusinessTypeEnum.READARTICLETKGAWARD.getStatus());
            } else if ("2".equals(request.getBonusType())) {
                accountFlowKg.setAmount(Constants.READMASTERKGBONUS.negate());
                accountFlowKg.setAccount_amount(Constants.READMASTERKGBONUS.negate());
                accountFlowKg.setFlow_detail("??????userId:" + request.getUserId() + "????????????");
                accountFlowKg.setBusiness_type_id(BusinessTypeEnum.SUBCONTRI.getStatus());
            }
            accountFlowKg.setArticle_id(request.getArticleId());
            accountFlowKg.setFlow_date(new Date().getTime());
            accountFlowKg.setAccount_date(new Date().getTime());
            accountFlowKg.setFlow_status(FlowStatusEnum.NOSTATUS.getStatus());
            //??????monggosb????????????
            MongoUtils.insertOne(MongoTables.ACCOUNT_FLOW_KG, new Document(Bean2MapUtil.bean2map(accountFlowKg)));

            inModel.setUserId(Constants.PLATFORM_USER_ID);

            BalanceDeducted balanceDeducted = BalanceDeducted.buildBalanceDeducted(inModel);
            balanceDeductedWMapper.insert(balanceDeducted);
//            success = accountWMapper.reduceTxbBalance(inModel) > 0;
            if ("2".equals(request.getBonusType()) && success) {

                // ??????????????? app1.1.0????????????????????????
                // mqPushMaster(request, Constants.READMASTERKGBONUS);
            }
        } catch (Exception e) {
            logger.info("-----------------" + e.toString());
            throw new ApiException("????????????" + e.getMessage());
        } finally {
            lock.unLock();
        }
        return success;
    }

    @Override
    @Transactional
    public Boolean getShareBonus(AccountRequest request) {
        Boolean isok = true;
        Long startDate = null;
        Long endDate = null;
        Long userId = request.getUserId();
//        AccountFlowInModel inModel = new AccountFlowInModel();
//        inModel.setArticleId(request.getArticleId());
//        inModel.setUserId(userId);
//        inModel.setBusinessTypeId(BusinessTypeEnum.SHAREARTICLETKGAWARD.getStatus());
        logger.info("?????????????????????????????????????????????");

        BasicDBObject querry = new BasicDBObject();
        querry.append("business_type_id", new BasicDBObject(Seach.EQ.getOperStr(), BusinessTypeEnum.SHAREARTICLETKGAWARD.getStatus()));
        querry.append("user_id", new BasicDBObject(Seach.EQ.getOperStr(), userId));
        querry.append("article_id", new BasicDBObject(Seach.EQ.getOperStr(), request.getArticleId()));
        //??????????????????????????????
        startDate = DateUtils.getDateStart(new Date()).getTime();
        endDate = DateUtils.getDateEnd(new Date()).getTime();
        querry.append("flow_date", BasicDBObjectBuilder.start(Seach.GTE.getOperStr(), startDate)
                .add(Seach.LTE.getOperStr(), endDate).get());
        Long bonusCount = MongoUtils.count(MongoTables.ACCOUNT_FLOW_KG, querry);
        // bonusCount = accountFlowTxbRMapper.checkArticleBonus(inModel);
        logger.info(">>>>>>>querry:" + querry.toString());
        logger.info(">>>>bonusCount:" + bonusCount);
        if (bonusCount > 0) {
            return false;
        }
        logger.info("??????????????????????????????6???????????????");
        querry.remove("article_id");
        bonusCount = MongoUtils.count(MongoTables.ACCOUNT_FLOW_KG, querry);
        logger.info(">>>>>>>>>>querry" + querry.toString());
        logger.info(">>>>bonusCount:" + bonusCount);
        // bonusCount = accountFlowTxbRMapper.selectTypeFlowCount(inModel);
        if (bonusCount >= Constants.PALTFORMSHARECOUNT) {
            return false;
        }
        // ????????????????????????
        long flowid = idGenerater.nextId();
        Long tributeUd = idGenerater.nextId();
        request.setTributeId(tributeUd);
        request.setDeleteUserFlowId(flowid);

        // ????????????????????????
        request.setFlowId(flowid);
        request.setBonusType("1");
        request.setUserId(userId);
        try {
            isok = this.getPlatformShareKgBonus(request);
            // ?????????????????????
            UserRelation userRelation = userRelationRMapper.selectParentUser(request.getUserId());
            // ????????????????????????
            if (userRelation != null) {
                request.setRelationFlowId(flowid);
                flowid = idGenerater.nextId();
                request.setFlowId(flowid);
                request.setBonusType("2");
                request.setSubUserId(userRelation.getRelUserId());
                request.setUserId(userRelation.getUserId());
                isok = this.getPlatformShareKgBonus(request);

                //??????????????????
                SubTributeRecordMongo subTributeRecordMongo = new SubTributeRecordMongo();
                subTributeRecordMongo.setTribute_id(tributeUd);
                subTributeRecordMongo.setCoin_type(2);
                subTributeRecordMongo.setAmount(Constants.SHAREMASTERKGBONUS);
                subTributeRecordMongo.setSub_id(userRelation.getRelUserId());
                subTributeRecordMongo.setSub_name(userRelation.getSubName());
                subTributeRecordMongo.setMaster_id(userRelation.getUserId());
                subTributeRecordMongo.setMaster_name(userRelation.getUserName());
                subTributeRecordMongo.setCreate_date(new Date().getTime());
                logger.info("---------------??????????????????-------------------" + JsonUtil.writeValueAsString(subTributeRecordMongo));
                mqProduct.sendMessage(subTributeRecordMqConfig.getTopic(), subTributeRecordMqConfig.getTags(), "", JsonUtil.writeValueAsString(subTributeRecordMongo));
            }
        } catch (Exception e) {
            //??????monggo??????
            delteMonggoRecord(request);
            throw new ApiException("??????????????????");
        }
        return isok;
    }

    @Override
    public Boolean getPlatformShareKgBonus(AccountRequest request) {
        logger.info("??????????????????KG???{}", JSON.toJSONString(request.getUserId()));
        boolean success;
        Lock lock = new Lock();
        // Long userId = request.getUserId();
        String key = USER_ACCOUNT_LOCK_KEY + "SHARE_KG_BONUS" + request.getUserId();
        try {
            lock.lock(key);
            // ???????????????
            UserkgOutModel userkgModel = userRMapper.getUserDetails(request.getUserId());
            if (request.getUserId() == null || userkgModel == null) {
                logger.error("?????????????????????id {}", request.getUserId());
                throw new ApiException("??????????????????");
            }

            ArticleInModel ainModel = new ArticleInModel();
            ainModel.setArticleId(request.getArticleId());
            ArticleOutModel articleOutModel = articleRMapper.selectByIdDetails(ainModel);
            if (articleOutModel == null) {
                throw new ApiException("??????????????????");
            }

            //??????kg????????????
            AccountFlowKgMongo accountFlowKg = new AccountFlowKgMongo();
            accountFlowKg.setAccount_flow_id(request.getFlowId());
            if (request.getRelationFlowId() != null) {
                accountFlowKg.setRelation_flow_id(request.getRelationFlowId());
            } else {
                accountFlowKg.setRelation_flow_id(request.getFlowId());
            }
            accountFlowKg.setUser_id(request.getUserId());
            accountFlowKg.setUser_phone(userkgModel.getUserMobile());
            accountFlowKg.setUser_email(userkgModel.getUserEmail());
            if ("1".equals(request.getBonusType())) {
                accountFlowKg.setAccount_amount(Constants.SHAREKGBONUS);
                accountFlowKg.setAmount(Constants.SHAREKGBONUS);
                accountFlowKg.setFlow_detail("???????????????" + articleOutModel.getArticleTitle() + "???,????????????????????????");
                accountFlowKg.setBusiness_type_id(BusinessTypeEnum.SHAREARTICLETKGAWARD.getStatus());
            } else if ("2".equals(request.getBonusType())) {
                UserkgOutModel subUserkgModel = userRMapper.getUserDetails(request.getSubUserId());
                accountFlowKg.setAccount_amount(Constants.SHAREMASTERKGBONUS);
                accountFlowKg.setAmount(Constants.SHAREMASTERKGBONUS);
                accountFlowKg.setFlow_detail("????????????" + subUserkgModel.getUserName() + "??????");
                accountFlowKg.setBusiness_type_id(BusinessTypeEnum.SUBCONTRI.getStatus());
            }
            accountFlowKg.setArticle_id(request.getArticleId());
            accountFlowKg.setFlow_date(new Date().getTime());
            accountFlowKg.setAccount_date(new Date().getTime());
            accountFlowKg.setFlow_status(FlowStatusEnum.NOSTATUS.getStatus());
            accountFlowKg.setUser_name(userkgModel.getUserName());
            MongoUtils.insertOne(MongoTables.ACCOUNT_FLOW_KG, new Document(Bean2MapUtil.bean2map(accountFlowKg)));


            AccountInModel inModel = new AccountInModel();
            inModel.setUserId(request.getUserId());
            inModel.setBalance(accountFlowKg.getAmount());
            success = accountWMapper.addTxbBalance(inModel) > 0;

            accountFlowKg = new AccountFlowKgMongo();
            accountFlowKg.setAccount_flow_id(idGenerater.nextId());
            accountFlowKg.setRelation_flow_id(request.getFlowId());
            accountFlowKg.setUser_id(Constants.PLATFORM_USER_ID);
            if ("1".equals(request.getBonusType())) {
                accountFlowKg.setAmount(Constants.SHAREKGBONUS.negate());
                accountFlowKg.setAccount_amount(Constants.SHAREKGBONUS.negate());
                accountFlowKg.setFlow_detail("??????userId:" + request.getUserId() + "????????????");
                accountFlowKg.setBusiness_type_id(BusinessTypeEnum.SHAREARTICLETKGAWARD.getStatus());
            } else if ("2".equals(request.getBonusType())) {
                accountFlowKg.setAccount_amount(Constants.SHAREMASTERKGBONUS.negate());
                accountFlowKg.setAmount(Constants.SHAREMASTERKGBONUS.negate());
                accountFlowKg.setFlow_detail("??????userId:" + request.getUserId() + "????????????");
                accountFlowKg.setBusiness_type_id(BusinessTypeEnum.SUBCONTRI.getStatus());
            }
            accountFlowKg.setArticle_id(request.getArticleId());
            accountFlowKg.setAccount_date(new Date().getTime());
            accountFlowKg.setFlow_date(new Date().getTime());
            accountFlowKg.setFlow_status(FlowStatusEnum.NOSTATUS.getStatus());
            //??????monggosb????????????
            MongoUtils.insertOne(MongoTables.ACCOUNT_FLOW_KG, new Document(Bean2MapUtil.bean2map(accountFlowKg)));

            inModel.setUserId(Constants.PLATFORM_USER_ID);
            BalanceDeducted balanceDeducted = BalanceDeducted.buildBalanceDeducted(inModel);
            balanceDeductedWMapper.insert(balanceDeducted);
//            success = accountWMapper.reduceTxbBalance(inModel) > 0;

            // ?????????????????????????????????
            if (success && "1".equals(request.getBonusType())) {
                KgInfoSwitchOutModel outModel = kgInfoSwitchRMapper.selectByPrimaryKey(request.getUserId());
                boolean ifPush = false;
                if (outModel == null) {
                    ifPush = true;
                } else if (outModel.getSystemInfoSwitch().intValue() == 1) {
                    ifPush = true;
                }
                if (ifPush) {
                    logger.info(">>>>>>>>>>??????????????????????????????");
                    String messageText = "";
                    messageText = "???????????????" + articleOutModel.getArticleTitle() + "????????????????????????" + Constants.SHAREKGBONUS
                            + "KG";
                    mqPush(request.getUserId().toString(), messageText, "platformReward");
                }
            }
            // ???????????????
            if (success && "2".equals(request.getBonusType())) {
                //??????????????????????????????
                //mqPushMaster(request, Constants.SHAREMASTERKGBONUS);
            }
        } catch (Exception e) {
            //??????monggo??????
            throw new ApiException("??????????????????");
        } finally {
            lock.unLock();
        }
        return success;
    }

    private void delteMonggoRecord(AccountRequest request) {
        //???????????????????????????
        deleteSubflow(request);
        //???????????????????????????
        deleteMasterflow(request);
        //??????????????????
        deleteTribueflow(request);
    }

    private void deleteTribueflow(AccountRequest request) {
        logger.info("============??????????????????==============");
        Long flowId = null;
        String id = "";
        BasicDBObject querry = new BasicDBObject();
        querry.append("tribute_id", request.getTributeId());
        logger.info("=========querry==============" + querry.toString());
        MongoCursor<Document> cursor = MongoUtils.findByFilter(MongoTables.SUB_TRIBUTE_RECORD, querry);
        if (cursor != null) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                id = doc.get("_id") + "";
                if (StringUtils.isNotEmpty(id)) {
                    MongoUtils.deleteById(MongoTables.SUB_TRIBUTE_RECORD, id + "");
                }
            }
        }
    }

    private void deleteSubflow(AccountRequest request) {
        //???????????????????????????????????????
        logger.info("===============??????????????????=================" + JsonUtil.writeValueAsString(request));
        BasicDBObject querry = new BasicDBObject();
        querry.append("account_flow_id", request.getDeleteUserFlowId());
        delteMonggoFlow(querry);

        logger.info("===============??????????????????=================");
        querry = new BasicDBObject();
        querry.append("relation_flow_id", request.getDeleteUserFlowId());
        querry.append("user_id", new BasicDBObject(Seach.EQ.getOperStr(), Constants.PLATFORM_USER_ID));
        delteMonggoFlow(querry);

    }

    private void deleteMasterflow(AccountRequest request) {

        logger.info("===============??????????????????=================");
        BasicDBObject querry = new BasicDBObject();
        querry.append("relation_flow_id", request.getDeleteUserFlowId());
        querry.append("user_id", request.getUserId());
        Long flowId = delteMonggoFlow(querry);

        logger.info("===============??????????????????=================");
        querry = new BasicDBObject();
        querry.append("relation_flow_id", flowId);
        querry.append("user_id", new BasicDBObject(Seach.EQ.getOperStr(), Constants.PLATFORM_USER_ID));
        delteMonggoFlow(querry);

    }

    private Long delteMonggoFlow(BasicDBObject querry) {
        Long flowId = null;
        String id = "";
        MongoCursor<Document> cursor = MongoUtils.findByFilter(MongoTables.ACCOUNT_FLOW_KG, querry);
        logger.info("==========querry===========" + querry.toString());
        if (cursor != null) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                id = doc.get("_id") + "";
                flowId = doc.getLong("account_flow_id");
                if (StringUtils.isNotEmpty(id)) {
                    MongoUtils.deleteById(MongoTables.ACCOUNT_FLOW_KG, id + "");
                }
            }
        }
        return flowId;
    }

    @Override
    public Boolean checkBrowerCount(AccountRequest request) {
        Boolean isok = true;
        BasicDBObject querry = new BasicDBObject();
        querry.append("user_id", new BasicDBObject(Seach.EQ.getOperStr(), request.getUserId()));
        querry.append("business_type_id", new BasicDBObject(Seach.EQ.getOperStr(), BusinessTypeEnum.READARTICLETKGAWARD.getStatus()));
        Long bonusCount = MongoUtils.count(MongoTables.ACCOUNT_FLOW_KG, querry);
        if (bonusCount != null) {
            if (bonusCount.intValue() > Constants.PALTFORMREADCOUNT) {
                isok = false;
            }
        }
        return isok;

    }


}
