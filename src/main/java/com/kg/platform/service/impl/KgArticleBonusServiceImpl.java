// package com.kg.platform.service.impl;
//
// import java.math.BigDecimal;
// import java.util.ArrayList;
// import java.util.Date;
// import java.util.List;
//
// import javax.inject.Inject;
//
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
//
// import com.alibaba.fastjson.JSON;
// import com.kg.platform.common.context.Result;
// import com.kg.platform.common.exception.ApiMessageException;
// import com.kg.platform.common.exception.BusinessException;
// import com.kg.platform.common.idgen.IDGen;
// import com.kg.platform.common.lock.Lock;
// import com.kg.platform.common.utils.NumberUtils;
// import com.kg.platform.dao.read.AccountFlowRMapper;
// import com.kg.platform.dao.read.AccountRMapper;
// import com.kg.platform.dao.read.ArticleRMapper;
// import com.kg.platform.dao.read.KgArticleBonusRMapper;
// import com.kg.platform.dao.read.KgArticleStatisticsRMapper;
// import com.kg.platform.dao.read.UserRMapper;
// import com.kg.platform.dao.write.AccountFlowWMapper;
// import com.kg.platform.dao.write.AccountWMapper;
// import com.kg.platform.dao.write.KgArticleBonusWMapper;
// import com.kg.platform.dao.write.KgArticleStatisticsHisWMapper;
// import com.kg.platform.dao.write.KgArticleStatisticsWMapper;
// import com.kg.platform.enumeration.BonusStatusEnum;
// import com.kg.platform.enumeration.BonusTypeEnum;
// import com.kg.platform.enumeration.BusinessTypeEnum;
// import com.kg.platform.enumeration.FlowStatusEnum;
// import com.kg.platform.model.in.AccountFlowInModel;
// import com.kg.platform.model.in.AccountInModel;
// import com.kg.platform.model.in.ArticleInModel;
// import com.kg.platform.model.in.KgArticleBonusInModel;
// import com.kg.platform.model.in.KgArticleStatisticsHisInModel;
// import com.kg.platform.model.in.KgArticleStatisticsInModel;
// import com.kg.platform.model.out.AccountOutModel;
// import com.kg.platform.model.out.ArticleOutModel;
// import com.kg.platform.model.out.KgArticleBonusOutModel;
// import com.kg.platform.model.out.KgArticleStatisticsOutModel;
// import com.kg.platform.model.out.UserkgOutModel;
// import com.kg.platform.model.request.KgArticleBonusRequest;
// import com.kg.platform.model.response.KgArticleBonusResponse;
// import com.kg.platform.service.ArticlekgService;
// import com.kg.platform.service.KgArticleBonusService;
//
// @Service
// @Transactional
// public class KgArticleBonusServiceImpl implements KgArticleBonusService {
//
// private static final Logger logger =
// LoggerFactory.getLogger(KgArticleBonusServiceImpl.class);
//
// private static final String USER_REWARD_LOCK_KEY = "kguser::
// KgArticleBonusService::reward::";
//
// private static final String USER_UPDATEBONUS_LOCK_KEY = "kguser::
// KgArticleBonusService::updateBonus::";
//
// private static final String USER_RESIDUE_LOCK_KEY = "kguser::
// KgArticleBonusService::updateAddBalance::";
//
// @Inject
// KgArticleBonusWMapper kgArticleBonusWMapper;
//
// @Inject
// KgArticleBonusRMapper kgArticleBonusRMapper;
//
// @Inject
// private IDGen idGenerater;
//
// @Inject
// KgArticleStatisticsRMapper kgArticleStatisticsRMapper;
//
// @Inject
// KgArticleStatisticsWMapper kgArticleStatisticsWMapper;
//
// @Inject
// AccountWMapper accountWMapper;
//
// @Inject
// AccountRMapper accountRMapper;
//
// @Inject
// ArticleRMapper articleRMapper;
//
// @Inject
// UserRMapper userRMapper;
//
// @Inject
// AccountFlowWMapper accountFlowWMapper;
//
// @Inject
// AccountFlowRMapper accountFlowRMapper;
//
// @Inject
// ArticlekgService articleService;
//
// @Inject
// KgArticleStatisticsHisWMapper kgArticleStatisticsHisWMapper;
//
// @Override
// public boolean insertSelective(KgArticleBonusRequest request) {
// logger.info("??????????????????{}", JSON.toJSONString(request));
// KgArticleBonusInModel inModel = new KgArticleBonusInModel();
// inModel.setBonusId(idGenerater.nextId());
// inModel.setArticleId(request.getArticleId());
// inModel.setBonusType(request.getBonusType() + "");
// inModel.setBonusSecondType(request.getBonusSecondType() + "");
// inModel.setBonusKind(request.getBonusKind());
// inModel.setBrowseTime(request.getBrowseTime());
// inModel.setBonusValue(request.getBonusValue());
// inModel.setMaxPeople(request.getMaxPeople());
// inModel.setCreateUser(request.getCreateUser());
// inModel.setCreateDate(new Date());
// return kgArticleBonusWMapper.insertSelective(inModel) > 0;
// }
//
// /**
// * ??????????????????????????????
// */
// @Override
// public Result<List<KgArticleBonusResponse>>
// getArticleBonus(KgArticleBonusRequest request) {
// logger.info("??????????????????????????????{}", JSON.toJSONString(request));
// KgArticleBonusInModel inModel = new KgArticleBonusInModel();
// inModel.setArticleId(request.getArticleId());
// // ????????????
// List<KgArticleBonusOutModel> list =
// kgArticleBonusRMapper.selectByArticleidKey(inModel);
//
// ArticleInModel articleInModel = new ArticleInModel();
// articleInModel.setArticleId(request.getArticleId());
// // ???????????????
//
// List<KgArticleBonusResponse> listModel = new
// ArrayList<KgArticleBonusResponse>();
// if (list.size() == 0) {
// ArticleOutModel articleOutModel =
// articleRMapper.getArticleContent(articleInModel);
// AccountInModel accountInModel = new AccountInModel();
// accountInModel.setUserId(articleOutModel.getCreateUser());
// // ??????????????????????????????
// AccountOutModel accountOutModel =
// accountRMapper.selectByUserbalance(accountInModel);
//
// KgArticleBonusResponse response = new KgArticleBonusResponse();
// response.setBalance(NumberUtils.formatBigDecimal(accountOutModel.getBalance()));//
// ????????????????????????
// listModel.add(response);
// }
//
// KgArticleStatisticsInModel statisticsInModel = new
// KgArticleStatisticsInModel();
// statisticsInModel.setArticleId(request.getArticleId());
// KgArticleStatisticsOutModel statisticsOutModel = kgArticleStatisticsRMapper
// .selectByPrimaryKey(statisticsInModel);
//
// for (KgArticleBonusOutModel kgOutModel : list) {
// KgArticleBonusResponse response = new KgArticleBonusResponse();
//
// if (null != statisticsOutModel) {
// response.setBonusGetNum(statisticsOutModel.getBonusNum());
// response.setBonusGetValue(NumberUtils.formatBigDecimal(statisticsOutModel.getBonusValue()));
// response.setBonusTotalValue(NumberUtils.formatBigDecimal(statisticsOutModel.getBonusTotal()));
// }
//
// response.setBonusId(kgOutModel.getBonusId().toString());
// response.setArticleId(kgOutModel.getArticleId().toString());
// response.setBonusType(kgOutModel.getBonusType());
// response.setBonusSecondType(kgOutModel.getBonusSecondType());
// response.setBrowseTime(kgOutModel.getBrowseTime());
// response.setBonusValue(kgOutModel.getBonusValue());
// response.setMaxPeople(kgOutModel.getMaxPeople());
// response.setBonusStatus(kgOutModel.getBonusStatus());
// response.setBonusTypeid(kgOutModel.getBonusTypeid());
// response.setBonusTypename(kgOutModel.getBonusTypename());
// response.setBonusKind(kgOutModel.getBonusKind());
//
// if (kgOutModel.getBonusType().intValue() == BonusTypeEnum.SHARE.getType()) {
// inModel.setBonusType(kgOutModel.getBonusType() + "");
// // ???????????????
// List<KgArticleBonusOutModel> listSecond =
// kgArticleBonusRMapper.selectSecondKey(inModel);
// for (KgArticleBonusOutModel kgArticleBonusOutModel : listSecond) {
// response.setBonusId(kgArticleBonusOutModel.getBonusId().toString());
// response.setArticleId(kgArticleBonusOutModel.getArticleId().toString());
// response.setBonusType(kgArticleBonusOutModel.getBonusType());
// response.setBonusSecondType(kgArticleBonusOutModel.getBonusSecondType());
// response.setBrowseTime(kgArticleBonusOutModel.getBrowseTime());
// response.setBonusValue(kgArticleBonusOutModel.getBonusValue());
// response.setMaxPeople(kgArticleBonusOutModel.getMaxPeople());
// response.setBonusStatus(kgArticleBonusOutModel.getBonusStatus());
// response.setBonusTypeid(kgArticleBonusOutModel.getBonusTypeid());
// response.setBonusTypename(kgArticleBonusOutModel.getBonusTypename());
// response.setBonusKind(kgArticleBonusOutModel.getBonusKind());
// }
// }
//
// listModel.add(response);
//
// }
//
// return new Result<List<KgArticleBonusResponse>>(listModel);
// }
//
// // ??????????????????
//
// public boolean updateBonus(KgArticleBonusRequest request) {
// logger.info("?????????????????? {}", JSON.toJSONString(request));
// String key = USER_UPDATEBONUS_LOCK_KEY + request.getArticleId() +
// request.getCreateUser();
// Lock lock = new Lock();
// try {
// lock.lock(key);
// boolean successarticle = false;
// BigDecimal bonusValuesum = new BigDecimal(0.000);
// List<KgArticleBonusRequest> list = request.getList();
// for (KgArticleBonusRequest kgArticleBonusRequest : list) {
// KgArticleBonusInModel inModelbonus = new KgArticleBonusInModel();
// inModelbonus.setBonusId(kgArticleBonusRequest.getBonusId());
// inModelbonus.setBonusType(kgArticleBonusRequest.getBonusType() + "");
// inModelbonus.setBonusSecondType(kgArticleBonusRequest.getBonusSecondType() +
// "");
// inModelbonus.setBrowseTime(kgArticleBonusRequest.getBrowseTime());
// inModelbonus.setBonusValue(kgArticleBonusRequest.getBonusValue());
// inModelbonus.setMaxPeople(kgArticleBonusRequest.getMaxPeople());
// inModelbonus.setBonusStatus(kgArticleBonusRequest.getBonusStatus());
// inModelbonus.setCreateUser(kgArticleBonusRequest.getCreateUser());
// inModelbonus.setCreateDate(new Date());
// inModelbonus.setUpdateUser(kgArticleBonusRequest.getCreateUser());
// inModelbonus.setBonusValue(kgArticleBonusRequest.getBonusValue());
// inModelbonus.setBonusKind(kgArticleBonusRequest.getBonusKind());
//
// bonusValuesum = bonusValuesum
// .add(request.getBonusValue().multiply(new
// BigDecimal(kgArticleBonusRequest.getMaxPeople())));
// //
// inModelbonus.setBonusValuesum(inModelbonus.getBonusValuesum().multiply(request.getBonusValue()));
// successarticle =
// kgArticleBonusWMapper.updateByPrimaryKeySelective(inModelbonus) > 0;
// }
//
// // ????????????????????? ?????????????????? ???????????????
// if (successarticle) {
// KgArticleStatisticsInModel statisticsInModel = new
// KgArticleStatisticsInModel();
// statisticsInModel.setArticleId(request.getArticleId());
// KgArticleStatisticsOutModel statisticsOutModel = kgArticleStatisticsRMapper
// .selectByPrimaryKey(statisticsInModel);
// int compare =
// statisticsOutModel.getBonusTotal().compareTo(statisticsOutModel.getBonusTotal());
//
// if (compare == -1) {
// // compare==-1,???????????????????????????????????????????????????????????????????????????=?????????????????????+??????
// // ????????????????????????+?????????????????????-??????
// BigDecimal add = statisticsOutModel.getBonusTotal().subtract(bonusValuesum);
// KgArticleStatisticsInModel aSInModel = new KgArticleStatisticsInModel();
// aSInModel.setArticleId(request.getArticleId());
// aSInModel.setBonusTotal(bonusValuesum);
// // ??????????????????????????????????????????????????????
// boolean success = kgArticleStatisticsWMapper.updateAddBonusTotal(aSInModel) >
// 0;
// AccountInModel inModel = new AccountInModel();
// inModel.setUserId(request.getCreateUser());
// inModel.setBalance(add);
// // ???????????????????????????+???????????????????????????-??????????????????????????????????????????-????????????????????????-???????????????????????????
//
// boolean addsuccess;
//
// addsuccess = accountWMapper.updateAddBalance(inModel) > 0;
//
// if (success == false && addsuccess == false) {
// throw new BusinessException("", "????????????????????????????????????????????????????????????");
// }
//
// }
// if (compare == 1) {
// // compare==1,??????????????????????????????????????????????????????????????????????????????=?????????????????????-??????
// BigDecimal Reduction =
// statisticsOutModel.getBonusTotal().subtract(bonusValuesum);
// // ????????????-?????? ???????????????+??????
// KgArticleStatisticsInModel aSInModel = new KgArticleStatisticsInModel();
// aSInModel.setArticleId(request.getArticleId());
// aSInModel.setBonusTotal(bonusValuesum);
// // ??????????????????????????????????????????????????????
// kgArticleStatisticsWMapper.updateReductionBonusTotal(aSInModel);
// AccountInModel inModel = new AccountInModel();
// inModel.setUserId(request.getCreateUser());
// inModel.setBalance(Reduction);
//
// // ???????????????????????????-???????????????????????????-??????????????????????????????????????????+????????????????????????-???????????????????????????
// accountWMapper.updateReduction(inModel);
//
// }
//
// }
// } finally {
// lock.unLock();
// }
//
// return true;
//
// }
//
// /**
// * ??????????????????????????????
// */
// @Override
// public boolean addArticleBonus(KgArticleBonusRequest request) {
// logger.info("????????????????????????id {}", request.getArticleId());
// AccountInModel accountInModel = new AccountInModel();
// accountInModel.setUserId(request.getCreateUser());
// AccountOutModel outModel = accountRMapper.selectOutUserId(accountInModel);
// ArticleInModel articleInModel = new ArticleInModel();
// articleInModel.setArticleId(request.getArticleId());
// ArticleOutModel aOutModel = articleRMapper.getArticleContent(articleInModel);
//
// // ???????????????????????????????????????????????????????????????
// KgArticleBonusInModel bonusInModel = new KgArticleBonusInModel();
// bonusInModel.setArticleId(request.getArticleId());
// // bonusInModel.setBonusStatus(BonusStatusEnum.TERMINATED.getStatus());
// Lock lock = new Lock();
// String key = USER_REWARD_LOCK_KEY + accountInModel.getUserId() +
// outModel.getAccountId();
// int count = kgArticleBonusRMapper.getBonusStatus(bonusInModel);
// try {
// lock.lock(key);
// if (count != 0) {
// KgArticleStatisticsInModel aStInModel = new KgArticleStatisticsInModel();
// aStInModel.setArticleId(request.getArticleId());
// // ??????????????????
// KgArticleStatisticsOutModel aStOutModel =
// kgArticleStatisticsRMapper.selectByPrimaryKey(aStInModel);
// if (aStOutModel != null) {
// KgArticleStatisticsHisInModel hisInModel = new
// KgArticleStatisticsHisInModel();
// hisInModel.setArticleId(aStOutModel.getArticleId());
// hisInModel.setBrowseNum(aStOutModel.getBrowseNum());
// hisInModel.setShareNum(aStOutModel.getShareNum());
// hisInModel.setThumbupNum(aStOutModel.getThumbupNum());
// hisInModel.setCollectNum(aStOutModel.getCollectNum());
// hisInModel.setBonusNum(aStOutModel.getBonusNum());
// hisInModel.setBonusValue(aStOutModel.getBonusValue());
// hisInModel.setBonusTotal(aStOutModel.getBonusTotal());
// hisInModel.setCreateUser(aStOutModel.getCreateUser());
// // ????????????????????????
// kgArticleStatisticsHisWMapper.insertSelective(hisInModel);
// // ????????????????????? ??????????????????????????????????????????????????????????????? ?????????0
//
// kgArticleStatisticsWMapper.initializeThe(aStInModel);
//
// }
//
// // ?????????????????????????????????ID??????????????????
// kgArticleBonusWMapper.deleteByArticle(bonusInModel);
//
// }
//
// BigDecimal bonusValuesum = new BigDecimal(0.000);
// int maxPeople = 0;
// List<KgArticleBonusRequest> listRequest = request.getList();
// for (KgArticleBonusRequest kgArticleBonusRequest : listRequest) {
//
// KgArticleBonusInModel inModelbonus = new KgArticleBonusInModel();
// inModelbonus.setBonusId(idGenerater.nextId());
// inModelbonus.setArticleId(request.getArticleId());
// inModelbonus.setBonusType(kgArticleBonusRequest.getBonusType() + "");
// inModelbonus.setBonusSecondType(kgArticleBonusRequest.getBonusSecondType() +
// "");
// inModelbonus.setBrowseTime(kgArticleBonusRequest.getBrowseTime());
// inModelbonus.setCreateUser(request.getCreateUser());
// inModelbonus.setCreateDate(new Date());
// inModelbonus.setUpdateUser(request.getCreateUser());
// inModelbonus.setBonusValue(kgArticleBonusRequest.getBonusValue());
// inModelbonus.setMaxPeople(kgArticleBonusRequest.getMaxPeople());
// inModelbonus.setBonusKind(kgArticleBonusRequest.getBonusKind());
//
// maxPeople += kgArticleBonusRequest.getMaxPeople().intValue();
// if (kgArticleBonusRequest.getMaxPeople() != null) {
// if (null != kgArticleBonusRequest.getBonusKind()
// && kgArticleBonusRequest.getBonusKind().intValue() == 2) {
// bonusValuesum = bonusValuesum.add(kgArticleBonusRequest.getBonusValue());
// } else {
// bonusValuesum = bonusValuesum.add(kgArticleBonusRequest.getBonusValue()
// .multiply(new BigDecimal(kgArticleBonusRequest.getMaxPeople())));
// }
// }
// if (aOutModel.getPublishStatus() == 4 || aOutModel.getPublishStatus() == 0) {
// inModelbonus.setBonusStatus(BonusStatusEnum.NOTVALID.getStatus());
// } else {
// inModelbonus.setBonusStatus(BonusStatusEnum.VALID.getStatus());
// }
// // ??????????????????????????????????????? ,sum==-1 ????????????????????????????????????
// int sum = outModel.getBalance().compareTo(bonusValuesum);
// if (sum == -1) {
// throw new ApiMessageException("??????????????????????????????????????????????????????????????????");
// }
// // ??????????????????
// kgArticleBonusWMapper.insertSelective(inModelbonus);
//
// }
//
// // ??????????????????????????????????????????????????????????????????????????????
// if (aOutModel.getPublishStatus() != 4) {
// ArticleInModel inModel = new ArticleInModel();
// inModel.setArticleId(request.getArticleId());
// ArticleOutModel articleOutModel = articleRMapper.getArticleContent(inModel);
//
// UserkgOutModel umodel = userRMapper
// .getUserDetails(Long.parseLong(articleOutModel.getCreateUser().toString()));
// AccountFlowInModel accountFlowInModel = new AccountFlowInModel();
// long flowid = idGenerater.nextId();
// accountFlowInModel.setAccountFlowId(flowid);
// accountFlowInModel.setRelationFlowId(flowid);
// accountFlowInModel.setUserId(Long.parseLong(umodel.getUserId()));
// accountFlowInModel.setUserPhone(umodel.getUserMobile());
// accountFlowInModel.setUserEmail(umodel.getUserEmail());
// accountFlowInModel.setAmount(bonusValuesum.multiply(new BigDecimal(-1)));
// accountFlowInModel.setFlowDate(new Date());
// accountFlowInModel.setArticleId(request.getArticleId());
// accountFlowInModel.setBonusTotalPerson(maxPeople);
// accountFlowInModel.setAccountAmount(bonusValuesum.multiply(new
// BigDecimal(-1)));
// accountFlowInModel
// .setFlowDetail("?????????" + "<<" + articleOutModel.getArticleTitle() + ">>" +
// "?????????????????? ?????????");
// accountFlowInModel.setBusinessTypeId(BusinessTypeEnum.READAWARD.getStatus());
// accountFlowInModel.setFlowStatus(FlowStatusEnum.VALID.getStatus());
// accountFlowWMapper.insertFlowSelective(accountFlowInModel);
//
// KgArticleStatisticsInModel statisticsInModel = new
// KgArticleStatisticsInModel();
// statisticsInModel.setArticleId(inModel.getArticleId());
// KgArticleStatisticsOutModel statisticsOutModel = kgArticleStatisticsRMapper
// .selectByPrimaryKey(statisticsInModel);
// statisticsInModel.setCreateUser(request.getCreateUser());
// statisticsInModel.setBonusTotal(bonusValuesum);
// // ???????????????????????????
// if (null == statisticsOutModel) {
// kgArticleStatisticsWMapper.insertSelective(statisticsInModel);
// } else {
// kgArticleStatisticsWMapper.updateAddBonusTotal(statisticsInModel);
// }
//
// logger.info("?????????????????? {}", accountInModel.getUserId(), inModel.getArticleId());
//
// accountInModel.setBalance(bonusValuesum);
//
// accountWMapper.updateOutUserId(accountInModel);
//
// }
// } finally {
// lock.unLock();
// }
//
// return true;
// }
//
// /**
// * ????????????????????????
// */
// @Override
// public boolean updateStatus(KgArticleBonusRequest request) {
// logger.info("??????????????????????????????id {}", request.getArticleId());
// boolean success;
// KgArticleBonusInModel inModelbonus = new KgArticleBonusInModel();
// Lock lock = new Lock();
// String key = USER_RESIDUE_LOCK_KEY + request.getArticleId() +
// request.getBonusStatus();
//
// try {
//
// lock.lock(key);
//
// inModelbonus.setArticleId(request.getArticleId());
// inModelbonus.setBonusStatus(request.getBonusStatus());
// // ??????bonusStatus==3 ????????????
// if (request.getBonusStatus() == BonusStatusEnum.TERMINATED.getStatus()) {
// KgArticleStatisticsInModel inModel = new KgArticleStatisticsInModel();
// inModel.setArticleId(request.getArticleId());
// // ???????????????
// KgArticleStatisticsOutModel outModel =
// kgArticleStatisticsRMapper.selectByPrimaryKey(inModel);
// // ????????????????????????????????????
// BigDecimal residue =
// outModel.getBonusTotal().subtract(outModel.getBonusValue());
// // ?????????
// ArticleInModel articleInModel = new ArticleInModel();
// articleInModel.setArticleId(request.getArticleId());
// ArticleOutModel articleOutModel =
// articleRMapper.getArticleContent(articleInModel);
// // ???????????????????????? ?????????????????????????????????????????????
// AccountInModel accountInModel = new AccountInModel();
// accountInModel.setUserId(articleOutModel.getCreateUser());
// accountInModel.setBalance(residue);
//
// success = accountWMapper.updateAddBalance(accountInModel) > 0;
//
// if (success) {
// UserkgOutModel umodel = userRMapper
// .getUserDetails(Long.parseLong(articleOutModel.getCreateUser().toString()));
// AccountFlowInModel flowInModel = new AccountFlowInModel();
// long flowid = idGenerater.nextId();
// flowInModel.setAccountFlowId(flowid);
// flowInModel.setRelationFlowId(flowid);
// flowInModel.setUserId(Long.parseLong(umodel.getUserId()));
// flowInModel.setUserPhone(umodel.getUserMobile());
// flowInModel.setUserEmail(umodel.getUserEmail());
// flowInModel.setAmount(residue);
// flowInModel.setAccountAmount(residue);
// flowInModel.setArticleId(request.getArticleId());
// flowInModel.setFlowDate(new Date());
// flowInModel.setFlowDetail("?????????" + "<<" + articleOutModel.getArticleTitle() +
// ">>" + "?????????????????????????????????");
// flowInModel.setBusinessTypeId(BusinessTypeEnum.READAWARD.getStatus());
// flowInModel.setFlowStatus(FlowStatusEnum.TERMINATED.getStatus());
// success = accountFlowWMapper.insertFlowSelective(flowInModel) > 0;
// }
//
// }
//
// if (request.getBonusStatus() == BonusStatusEnum.VALID.getStatus()) {
// success = kgArticleBonusWMapper.startBonusStatusByArticleId(inModelbonus) >
// 0;
// } else if (request.getBonusStatus() == BonusStatusEnum.PAUSED.getStatus()) {
// success = kgArticleBonusWMapper.pauseBonusStatusByArticleId(inModelbonus) >
// 0;
// } else {
// success = kgArticleBonusWMapper.updateBonusStatusByArticleId(inModelbonus) >
// 0;
// }
//
// } finally {
// lock.unLock();
// }
// return success;
// }
//
// }
