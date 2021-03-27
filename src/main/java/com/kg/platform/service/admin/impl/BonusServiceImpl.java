package com.kg.platform.service.admin.impl;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.context.AnalysisContext;
import com.alibaba.excel.read.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.easyexcel.read.ExcelReaderFactory;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ApiException;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.lock.Lock;
import com.kg.platform.common.mongo.MongoTables;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.mongo.Seach;
import com.kg.platform.common.mq.MQProduct;
import com.kg.platform.common.utils.*;
import com.kg.platform.dao.entity.Account;
import com.kg.platform.dao.entity.AccountFlow;
import com.kg.platform.dao.entity.PushMessage;
import com.kg.platform.dao.read.AccountFlowTxbRMapper;
import com.kg.platform.dao.read.AccountRMapper;
import com.kg.platform.dao.read.SysUserRMapper;
import com.kg.platform.dao.read.UserRMapper;
import com.kg.platform.dao.read.admin.AdminUserRMapper;
import com.kg.platform.dao.write.AccountFlowRitWMapper;
import com.kg.platform.dao.write.AccountFlowTxbWMapper;
import com.kg.platform.dao.write.AccountFlowWMapper;
import com.kg.platform.dao.write.AccountWMapper;
import com.kg.platform.dao.write.admin.AdminUserBonusDetailWMapper;
import com.kg.platform.dao.write.admin.AdminUserBonusWMapper;
import com.kg.platform.dao.write.admin.AdminUserMutiBonusDetailWMapper;
import com.kg.platform.enumeration.*;
import com.kg.platform.model.ConfirmBonusPushModel;
import com.kg.platform.model.in.AccountInModel;
import com.kg.platform.model.in.UserInModel;
import com.kg.platform.model.in.admin.BonusInModel;
import com.kg.platform.model.in.admin.UserBonusDetailInModel;
import com.kg.platform.model.in.admin.UserBonusInModel;
import com.kg.platform.model.in.admin.UserMutiBonusDetailInModel;
import com.kg.platform.model.mongoTable.AccountFlowKgMongo;
import com.kg.platform.model.mongoTable.AccountFlowRitMongo;
import com.kg.platform.model.out.AccountOutModel;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.admin.*;
import com.kg.platform.model.response.SiteinfoResponse;
import com.kg.platform.model.response.admin.*;
import com.kg.platform.service.PushMessageService;
import com.kg.platform.service.PushService;
import com.kg.platform.service.SiteinfoService;
import com.kg.platform.service.admin.BonusService;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

@Service("AdminBonusService")
public class BonusServiceImpl implements BonusService {

    private static final String USER_ACCOUNT_LOCK_KEY = "kguser::CommonService::CommmomUpdateAddUbalance::";

    private static final Logger logger = LoggerFactory.getLogger(BonusServiceImpl.class);

    @Autowired
    private AdminUserRMapper adminUserRMapper;

    @Autowired
    private UserRMapper userRMapper;

    @Autowired
    private IDGen idGenerater;

    @Autowired
    private AccountFlowWMapper accountFlowWMapper;

    @Autowired
    private AccountWMapper accountWMapper;

    @Autowired
    private AccountRMapper accountRMapper;

    @Autowired
    private AccountFlowTxbRMapper accountFlowTxbRMapper;

    @Autowired
    private AdminUserBonusDetailWMapper adminUserBonusDetailWMapper;

    @Autowired
    private AdminUserBonusWMapper adminUserBonusWMapper;

    @Autowired
    private AdminUserMutiBonusDetailWMapper adminUserMutiBonusDetailWMapper;

    @Autowired
    private SiteinfoService siteinfoService;

    @Autowired
    private JedisUtils jedisUtils;

    @Autowired
    private MQProduct mqProduct;

    @Resource(name = "propertyLoader")
    protected PropertyLoader propertyLoader;

    @Autowired
    private PushService pushService;

    @Override
    public PageModel<InviteBonusQueryResponse> getInviteBonus(BonusQueryRequest request) {
        request.setLimit(request.getPageSize());
        request.setStart((request.getCurrentPage() - 1) * request.getPageSize());

        List<InviteBonusQueryResponse> data = adminUserRMapper.selectUserInviteByCondition(request);
        if (null != data && data.size() > 0) {
            data.forEach(item -> {
                item.setLevelDisplay("初级");
                item.setUserRoleDisplay(
                        UserRoleEnum.getUserRoleEnum(NumberUtils.intValue(item.getUserRole())).getDisplay());
            });
        }
        long count = adminUserRMapper.selectUserInviteCountByCondition(request);
        PageModel<InviteBonusQueryResponse> pageModel = new PageModel<>();
        pageModel.setCurrentPage(request.getCurrentPage());
        pageModel.setPageSize(request.getPageSize());
        pageModel.setData(data);
        pageModel.setTotalNumber(count);
        return pageModel;
    }

    @Override
    public PageModel<RealnameQueryResponse> realnameBonus(BonusQueryRequest request) {
        request.setLimit(request.getPageSize());
        request.setStart((request.getCurrentPage() - 1) * request.getPageSize());
        request.setBusinessTypeId(BusinessTypeEnum.REALNAMEBONUS.getStatus());

        List<RealnameQueryResponse> data = adminUserRMapper.selectRealnameByCondition(request);
        if (null != data && data.size() > 0) {
            data.forEach(item -> {
                item.setLevelDisplay("初级");
                item.setUserRoleDisplay(
                        UserRoleEnum.getUserRoleEnum(NumberUtils.intValue(item.getUserRole())).getDisplay());
            });
        }
        long count = adminUserRMapper.selectRealnameCountByCondition(request);
        PageModel<RealnameQueryResponse> pageModel = new PageModel<>();
        pageModel.setCurrentPage(request.getCurrentPage());
        pageModel.setPageSize(request.getPageSize());
        pageModel.setData(data);
        pageModel.setTotalNumber(count);
        return pageModel;
    }

    @Override
    public PageModel<RealnameQueryResponse> userColumnBonus(BonusQueryRequest request) {
        request.setLimit(request.getPageSize());
        request.setStart((request.getCurrentPage() - 1) * request.getPageSize());
        request.setBusinessTypeId(BusinessTypeEnum.USERCOLUMNBONUS.getStatus());

        List<RealnameQueryResponse> data = adminUserRMapper.selectRealnameByCondition(request);
        if (null != data && data.size() > 0) {
            data.forEach(item -> {
                item.setLevelDisplay("初级");
                item.setUserRoleDisplay(
                        UserRoleEnum.getUserRoleEnum(NumberUtils.intValue(item.getUserRole())).getDisplay());
            });
        }
        long count = adminUserRMapper.selectRealnameCountByCondition(request);
        PageModel<RealnameQueryResponse> pageModel = new PageModel<>();
        pageModel.setCurrentPage(request.getCurrentPage());
        pageModel.setPageSize(request.getPageSize());
        pageModel.setData(data);
        pageModel.setTotalNumber(count);
        return pageModel;
    }

    private BigDecimal getPublishArticleBonus() {
        //查询钛小白总额
        BigDecimal total = BigDecimal.ZERO;
        List<Bson> codition = new ArrayList<>();
        BasicDBObject querryBy = new BasicDBObject();
        querryBy.append("business_type_id", new BasicDBObject(Seach.EQ.getOperStr(), BusinessTypeEnum.ADDEDTXBBONUS.getStatus()));
        querryBy.append("amount", new BasicDBObject(Seach.GT.getOperStr(), 0));
        Bson querry = new BasicDBObject("$match", querryBy);
        codition.add(querry);

        BasicDBObject groupBy = new BasicDBObject();
        groupBy.append("_id", "$business_type_id");
        groupBy.append("total", new BasicDBObject("$sum", "$amount"));
        Bson group = new BasicDBObject("$group", groupBy);
        codition.add(group);

        logger.info("--------getPublishArticleBonusQuerry---------" + codition.toString());
        MongoCursor<Document> cursor = MongoUtils.aggregate(MongoTables.ACCOUNT_FLOW_KG, codition);
        if (cursor != null) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                total = doc.get("total") == null ? BigDecimal.ZERO : new BigDecimal(doc.get("total").toString());
            }
        }
        return total;
    }

    @Override
    public PageModel<PublishBonusQueryResponse> publishArticleBonus(PublishBonusQueryRequest request) {
        request.setLimit(request.getPageSize());
        request.setStart((request.getCurrentPage() - 1) * request.getPageSize());
        List<PublishBonusQueryResponse> data = adminUserRMapper.getPublishBonusList(request);
        if (CollectionUtils.isNotEmpty(data)) {
            for (PublishBonusQueryResponse response : data) {
                long count = MongoUtils.count(UserLogEnum.KG_USER_SHARE.getTable(), new BasicDBObject("articleId", Long.valueOf(response.getArticleId())));
                long bcount = MongoUtils.count(UserLogEnum.KG_USER_BROWSE.getTable(), new BasicDBObject("articleId", Long.valueOf(response.getArticleId())));
                response.setShareNum((int) count);
                response.setBowseNum((int) bcount);
            }
        }
        PageModel<PublishBonusQueryResponse> pageModel = new PageModel<PublishBonusQueryResponse>();
        pageModel.setCurrentPage(request.getCurrentPage());
        pageModel.setPageSize(request.getPageSize());
        pageModel.setData(data);
        BigDecimal total = this.getPublishArticleBonus();
        // 查询系统总发文奖励和太小白
        PublishBonusQueryResponse publishBonusQueryResponse = adminUserRMapper.getTotalPublishBonus();
        pageModel.setTotalPrice(publishBonusQueryResponse.getTvAmount().setScale(3, BigDecimal.ROUND_HALF_DOWN) + "TV"
                + "," + total.setScale(3, BigDecimal.ROUND_HALF_DOWN) + "KG");
        long count = adminUserRMapper.selectPublishBonusCountByCondition(request);
        pageModel.setTotalNumber(count);
        return pageModel;
    }

    @Override
    public Boolean addedTxbBonus(BonusInModel bonusInModel) {
        Boolean SUCCESS = true;
        Lock lock = new Lock();
        String key = USER_ACCOUNT_LOCK_KEY + bonusInModel.getUserId() + bonusInModel.getModelType();
        try {
            lock.lock(key);
            AccountFlowKgMongo accountFlowTxb = new AccountFlowKgMongo();

            long flowid = bonusInModel.getFlowId();
            if (bonusInModel.getFlowId() == null) {
                flowid = idGenerater.nextId();
            }

            accountFlowTxb.setAccount_flow_id(flowid);
            accountFlowTxb.setRelation_flow_id(flowid);
            UserkgOutModel userkgOutModel = userRMapper.getUserDetails(bonusInModel.getUserId());
            if (userkgOutModel == null) {
                return false;
            }
            accountFlowTxb.setUser_id(bonusInModel.getUserId());
            accountFlowTxb.setUser_phone(userkgOutModel.getUserMobile());
            accountFlowTxb.setUser_name(userkgOutModel.getUserName());
            accountFlowTxb.setArticle_id(bonusInModel.getArticleId());
            accountFlowTxb.setUser_email(userkgOutModel.getUserEmail());
            accountFlowTxb.setAmount(bonusInModel.getBalance());
            accountFlowTxb.setAccount_amount(bonusInModel.getBalance());
            Date date = new Date();
            accountFlowTxb.setFlow_date(date.getTime());
            accountFlowTxb.setAccount_date(date.getTime());
            accountFlowTxb.setFlow_detail(bonusInModel.getFlowDetail());
            accountFlowTxb.setBusiness_type_id(bonusInModel.getBussinssId());
            accountFlowTxb.setFlow_status(bonusInModel.getFlowStatus());
            MongoUtils.insertOne(MongoTables.ACCOUNT_FLOW_KG, new Document(Bean2MapUtil.bean2map(accountFlowTxb)));


            AccountInModel inModel = new AccountInModel();
            inModel.setUserId(bonusInModel.getUserId());
            inModel.setBalance(bonusInModel.getBalance());
            this.initAccount(inModel, 1);

            accountFlowTxb = new AccountFlowKgMongo();
            accountFlowTxb.setAccount_flow_id(idGenerater.nextId());
            accountFlowTxb.setArticle_id(bonusInModel.getArticleId());
            accountFlowTxb.setRelation_flow_id(flowid);
            accountFlowTxb.setUser_id(bonusInModel.getFromUserId());
            accountFlowTxb.setAmount(bonusInModel.getBalance().negate());
            accountFlowTxb.setAccount_amount(bonusInModel.getBalance().negate());
            accountFlowTxb.setFlow_date(date.getTime());
            accountFlowTxb.setAccount_date(date.getTime());
            accountFlowTxb.setFlow_detail(bonusInModel.getFromUserFlowDetail());
            accountFlowTxb.setBusiness_type_id(bonusInModel.getBussinssId());
            accountFlowTxb.setFlow_status(bonusInModel.getFlowStatus());
            MongoUtils.insertOne(MongoTables.ACCOUNT_FLOW_KG, new Document(Bean2MapUtil.bean2map(accountFlowTxb)));

            inModel.setUserId(bonusInModel.getFromUserId());
            SUCCESS = accountWMapper.reduceTxbBalance(inModel) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException("发放氪金奖励失败");
        } finally {
            lock.unLock();
        }
        return SUCCESS;

    }

    public Boolean addedRitBonus(BonusInModel bonusInModel) {
        Boolean SUCCESS = true;
        Lock lock = new Lock();
        String key = USER_ACCOUNT_LOCK_KEY + bonusInModel.getUserId() + bonusInModel.getModelType();
        try {
            lock.lock(key);
            AccountFlowRitMongo accountFlowRit = new AccountFlowRitMongo();
            long flowid = bonusInModel.getFlowId();
            accountFlowRit.setAccount_flow_id(flowid);
            accountFlowRit.setRelation_flow_id(flowid);
            UserkgOutModel userkgOutModel = userRMapper.getUserDetails(bonusInModel.getUserId());
            if (userkgOutModel == null) {
                throw new ApiException("发放rit奖励失败");
            }
            accountFlowRit.setUser_id(bonusInModel.getUserId());
            accountFlowRit.setArticle_id(bonusInModel.getArticleId());
            accountFlowRit.setUser_phone(userkgOutModel.getUserMobile());
            accountFlowRit.setUser_email(userkgOutModel.getUserEmail());
            Date date = new Date();
            accountFlowRit.setFlow_date(date.getTime());
            accountFlowRit.setAccount_date(date.getTime());
            accountFlowRit.setUser_name(userkgOutModel.getUserName());
            accountFlowRit.setFlow_detail(bonusInModel.getFlowDetail());
            accountFlowRit.setBusiness_type_id(bonusInModel.getBussinssId());
            accountFlowRit.setFlow_status(bonusInModel.getFlowStatus());
            if (bonusInModel.getRewardType() == 1) {
                accountFlowRit.setAmount(bonusInModel.getBalance());
                accountFlowRit.setAccount_amount(bonusInModel.getBalance());
            } else {
                accountFlowRit.setBusiness_type_id(BusinessTypeEnum.PLATFORMFRZEERITBONUS.getStatus());
                accountFlowRit.setFreeze_amount(bonusInModel.getBalance());
            }
            MongoUtils.insertOne(MongoTables.ACCOUNT_FLOW_RIT, new Document(Bean2MapUtil.bean2map(accountFlowRit)));

            // 增加用户余额
            AccountInModel auntinModel = new AccountInModel();
            auntinModel.setUserId(bonusInModel.getUserId());
            auntinModel.setBalance(bonusInModel.getBalance());
            if (bonusInModel.getRewardType() == 1) {
                this.initAccount(auntinModel, 2);
            } else {
                this.initAccount(auntinModel, 3);
            }

            accountFlowRit = new AccountFlowRitMongo();
            accountFlowRit.setAccount_flow_id(idGenerater.nextId());
            accountFlowRit.setArticle_id(bonusInModel.getArticleId());
            accountFlowRit.setRelation_flow_id(flowid);
            accountFlowRit.setUser_id(bonusInModel.getFromUserId());
            accountFlowRit.setAmount(bonusInModel.getBalance().negate());
            accountFlowRit.setAccount_amount(bonusInModel.getBalance().negate());
            accountFlowRit.setFlow_date(date.getTime());
            accountFlowRit.setAccount_date(date.getTime());
            accountFlowRit.setFlow_detail(bonusInModel.getFromUserFlowDetail());
            accountFlowRit.setBusiness_type_id(bonusInModel.getBussinssId());
            if (bonusInModel.getRewardType() == 2) {
                accountFlowRit.setBusiness_type_id(BusinessTypeEnum.PLATFORMFRZEERITBONUS.getStatus());
            }
            accountFlowRit.setFlow_status(FlowStatusEnum.NOSTATUS.getStatus());
            //accountFlowRitWMapper.insertSelective(accountFlowRit);
            MongoUtils.insertOne(MongoTables.ACCOUNT_FLOW_RIT, new Document(Bean2MapUtil.bean2map(accountFlowRit)));

            // 减少平台余额
            AccountInModel inModel = new AccountInModel();
            inModel.setBalance(bonusInModel.getBalance().negate());
            inModel.setUserId(bonusInModel.getFromUserId());
            SUCCESS = accountWMapper.addRitBalance(inModel) > 0;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException("发放rit奖励失败");
        } finally {
            lock.unLock();
        }
        return SUCCESS;
    }


    @Override
    public Boolean addedTvBonus(BonusInModel bonusInModel) {
        Boolean SUCCESS = true;
        Lock lock = new Lock();
        String key = USER_ACCOUNT_LOCK_KEY + bonusInModel.getUserId() + bonusInModel.getModelType();
        try {
            lock.lock(key);
            AccountFlow accountFlow = new AccountFlow();
            long flowid = bonusInModel.getFlowId();
            accountFlow.setAccountFlowId(flowid);
            accountFlow.setRelationFlowId(flowid);
            UserkgOutModel userkgOutModel = userRMapper.getUserDetails(bonusInModel.getUserId());
            if (userkgOutModel == null) {
                throw new ApiException("发放rit奖励失败");
            }
            accountFlow.setUserId(bonusInModel.getUserId());
            accountFlow.setArticleId(bonusInModel.getArticleId());
            accountFlow.setUserPhone(userkgOutModel.getUserMobile());
            accountFlow.setUserEmail(userkgOutModel.getUserEmail());
            accountFlow.setAmount(bonusInModel.getBalance());
            accountFlow.setAccountAmount(bonusInModel.getBalance());
            accountFlow.setFlowDate(new Date());
            accountFlow.setFlowDetail(bonusInModel.getFlowDetail());
            accountFlow.setBusinessTypeId(bonusInModel.getBussinssId());
            accountFlow.setFlowStatus(bonusInModel.getFlowStatus());
            SUCCESS = accountFlowWMapper.insertSelective(accountFlow) > 0;

            // 增加用户余额
            AccountInModel auntinModel = new AccountInModel();
            auntinModel.setUserId(bonusInModel.getUserId());
            auntinModel.setBalance(bonusInModel.getBalance());
            SUCCESS = accountWMapper.addUserbalance(auntinModel) > 0;

            accountFlow = new AccountFlow();
            accountFlow.setAccountFlowId(idGenerater.nextId());
            accountFlow.setArticleId(bonusInModel.getArticleId());
            accountFlow.setRelationFlowId(flowid);
            accountFlow.setUserId(bonusInModel.getFromUserId());
            accountFlow.setAmount(bonusInModel.getBalance().negate());
            accountFlow.setAccountAmount(bonusInModel.getBalance().negate());
            accountFlow.setFlowDate(new Date());
            accountFlow.setFlowDetail(bonusInModel.getFromUserFlowDetail());
            accountFlow.setBusinessTypeId(bonusInModel.getBussinssId());
            accountFlow.setFlowStatus(bonusInModel.getFlowStatus());
            accountFlowWMapper.insertSelective(accountFlow);
            // 减少平台余额
            AccountInModel inModel = new AccountInModel();
            inModel.setBalance(bonusInModel.getBalance().negate());
            inModel.setUserId(bonusInModel.getFromUserId());
            SUCCESS = accountWMapper.addUserbalance(inModel) > 0;


        } finally {
            lock.unLock();
        }
        return SUCCESS;
    }

    @Override
    public PageModel<ShareQueryResponse> shareArticleBonus(ShareBonusQueryRequest request) {
        PageModel<ShareQueryResponse> pageModel = new PageModel<ShareQueryResponse>();
        request.setLimit(request.getPageSize());
        request.setStart((request.getCurrentPage() - 1) * request.getPageSize());
        List<ShareQueryResponse> data = adminUserRMapper.getShareBonusList(request);
        pageModel.setCurrentPage(request.getCurrentPage());
        pageModel.setPageSize(request.getPageSize());
        pageModel.setData(data);
        // 总页数
        long count = adminUserRMapper.shareBonusCountByCondition(request);
        pageModel.setTotalNumber(count);
        return pageModel;
    }

    @Override
    public Result<List<UserQueryResponse>> getUserBonusInfos(UserBonusQueryRequest request) {
        Result<List<UserQueryResponse>> res = new Result<List<UserQueryResponse>>();
        if (StringUtils.isEmpty(request.getUserMobiles())) {
            res.setErrorCode(Integer.parseInt(ExceptionEnum.PARAMEMPTYERROR.getCode()));
            res.setErrorMsg(ExceptionEnum.PARAMEMPTYERROR.getMessage());
            return res;
        }
        res.setData(adminUserRMapper.getUserListByMobile(request));
        return res;
    }

    private Result<UserQueryResponse> validateBonus(UserBonusQueryRequest request, HttpServletRequest servletRequest) {
        Result<UserQueryResponse> res = new Result<UserQueryResponse>();
        logger.info("验证是否是当前ip在访问");
        Result<SiteinfoResponse> siteinfoResponse = siteinfoService.selectSiteInfo();
        CheckUtils.checkRetInfo(siteinfoResponse, true);

        logger.info("验证数据是否为空");
        if (StringUtils.isEmpty(request.getUserMobiles()) || null == request.getAdminId()
                || null == request.getBonusReason() || null == request.getAwardType() || null == request.getBonus()) {
            res.setErrorCode(Integer.parseInt(ExceptionEnum.PARAMEMPTYERROR.getCode()));
            res.setErrorMsg(ExceptionEnum.PARAMEMPTYERROR.getMessage());
            return res;
        }
        logger.info("验证氪金金额是否大于零");
        if (request.getBonus().compareTo(BigDecimal.ZERO) <= 0) {
            res.setErrorCode(Integer.parseInt(ExceptionEnum.ADMIN_USER_BONUS_KG_ERROR.getCode()));
            res.setErrorMsg(ExceptionEnum.ADMIN_USER_BONUS_KG_ERROR.getMessage());
            return res;
        }
        logger.info("验证发放的手机号是否有重复");
        List<String> list = Lists.newArrayList(request.getUserMobiles().split(","));
        Set<String> set = Sets.newHashSet(list);
        logger.info("验证手机号是否超过100个");
        if (list.size() > Constants.MAXUSERBONUSMOBILES) {
            res.setErrorCode(Integer.parseInt(ExceptionEnum.MAX_USER_BONUS_MOBILES.getCode()));
            res.setErrorMsg(ExceptionEnum.MAX_USER_BONUS_MOBILES.getMessage());
            return res;
        }
        boolean duplicate = (list.size() == set.size());
        if (!duplicate) {
            res.setErrorCode(Integer.parseInt(ExceptionEnum.ADMIN_USER_BONUS_REPEAT_ERROR.getCode()));
            res.setErrorMsg(ExceptionEnum.ADMIN_USER_BONUS_REPEAT_ERROR.getMessage());
            return res;
        }

        logger.info("验证重复提交");
        String jedisKey = JedisKey.getUserBonus();
        if (jedisUtils.get(jedisKey) != null) {
            res.setErrorCode(Integer.parseInt(ExceptionEnum.REPEAT_ERROR.getCode()));
            res.setErrorMsg(ExceptionEnum.REPEAT_ERROR.getMessage());
            return res;
        }
        jedisUtils.set(jedisKey, "user_bonus", 10);
        return res;
    }

    private Boolean getAward(UserBonusQueryRequest request, List<UserQueryResponse> datas) {
        Boolean isOk = true;
        List<Long> flowIds = new ArrayList<>();
        List<ConfirmBonusPushModel> confirmBonusPushModelList = new ArrayList<>();
        try {
            logger.info("插入用户奖励主表数据");
            UserBonusInModel userBonusInModel = new UserBonusInModel();
            Long extraBonusId = idGenerater.nextId();
            userBonusInModel.setBonusReason(request.getBonusReason());
            userBonusInModel.setCreateTime(new Date());
            userBonusInModel.setExtraBonusId(extraBonusId);
            userBonusInModel.setTotalNum(datas.size());
            userBonusInModel.setTotalKgAmount(BigDecimal.ZERO);
            userBonusInModel.setTotalTvAmount(BigDecimal.ZERO);
            userBonusInModel.setRewardType(request.getAwardType());
            if (request.getAwardType() == 2) {
                userBonusInModel.setTotalKgAmount(request.getBonus().multiply(new BigDecimal(datas.size())));
            }
            userBonusInModel.setSysUserId(request.getAdminId());
            adminUserBonusWMapper.addUserBonus(userBonusInModel);

            //查询平台rit余额和kg余额
            AccountInModel platAccountInModel = new AccountInModel();
            platAccountInModel.setUserId(Constants.PLATFORM_USER_ID);
            AccountOutModel platAccountInfo = accountRMapper.selectByUserbalance(platAccountInModel);
            for (UserQueryResponse userQueryResponse : datas) {
                String message = "收到千氪奖励";
                BigDecimal diff = BigDecimal.ZERO;
                if (request.getAwardType() == 0 || request.getAwardType() == 1) {
                    diff = platAccountInfo.getRitBalance().subtract(request.getBonus());
                    if (diff.compareTo(BigDecimal.ZERO) < 0) {
                        logger.info("平台账户RIT金额不足");
                        throw new ApiException("平台账户RIT余额不足");
                    }
                } else if (request.getAwardType() == 2) {
                    logger.info("验证平台账户氪金金额是否足够");
                    diff = platAccountInfo.getTxbBalance().subtract(request.getBonus());
                    if (diff.compareTo(BigDecimal.ZERO) < 0) {
                        logger.info("平台账户氪金金额不足");
                        throw new ApiException("平台氪金余额不足");
                    }
                }
                UserMutiBonusDetailInModel userMutiBonusDetailInModel = new UserMutiBonusDetailInModel();
                UserBonusDetailInModel userBonusDetailInModel = new UserBonusDetailInModel();


                BonusInModel bonusInModel = new BonusInModel();
                Long flowId = null;
                bonusInModel.setUserId(Long.valueOf(userQueryResponse.getUserId()));
                bonusInModel.setFromUserId(Constants.PLATFORM_USER_ID);
                bonusInModel.setFlowStatus(FlowStatusEnum.NOSTATUS.getStatus());
                bonusInModel.setBalance(request.getBonus());
                bonusInModel.setModelType("PUBLISHBONUS");
                bonusInModel.setFromUserFlowDetail("发放千氪奖励:" + request.getBonusReason());
                bonusInModel.setFlowDetail("获得千氪奖励:" + request.getBonusReason());
                flowId = idGenerater.nextId();
                flowIds.add(flowId);
                bonusInModel.setFlowId(flowId);
                if (request.getAwardType() == 0) {

                    logger.info("发放rit奖励到rit余额");
                    bonusInModel.setFlowStatus(FlowStatusEnum.NOSTATUS.getStatus());
                    bonusInModel.setBussinssId(BusinessTypeEnum.PLATFORMRITBONUS.getStatus());
                    bonusInModel.setRewardType(1);
                    isOk = this.addedRitBonus(bonusInModel);
                    userMutiBonusDetailInModel.setAccountFlowId(flowId);
                    message += request.getBonus() + "RIT,";

                } else if (request.getAwardType() == 1) {

                    logger.info("发放奖励到rit冻结余额");
                    //冻结状态
                    bonusInModel.setFlowStatus(FlowStatusEnum.FREEZED.getStatus());
                    bonusInModel.setBussinssId(BusinessTypeEnum.PLATFORMRITBONUS.getStatus());
                    bonusInModel.setRewardType(2);
                    isOk = this.addedRitBonus(bonusInModel);
                    userMutiBonusDetailInModel.setAccountFlowId(flowId);
                    message += request.getBonus() + "RIT,";

                } else if (request.getAwardType() == 2) {

                    logger.info("发放氪金奖励");
                    bonusInModel.setBussinssId(BusinessTypeEnum.PLATFORMKGBONUS.getStatus());
                    isOk = this.addedTxbBonus(bonusInModel);
                    userBonusDetailInModel.setKgAccountFlowId(flowId);
                    message += request.getBonus() + "KG,";

                }
                message += "奖励原因:" + request.getBonusReason() + "";
                logger.info("插入奖励详细列表");
                if (request.getAwardType() == 0 || request.getAwardType() == 1) {
                    userMutiBonusDetailInModel.setBonusDetailId(idGenerater.nextId());
                    userMutiBonusDetailInModel.setExtraBonusId(extraBonusId);
                    userMutiBonusDetailInModel.setCreateTime(new Date());
                    userMutiBonusDetailInModel.setAmount(request.getBonus());
                    userMutiBonusDetailInModel.setUserId(Long.valueOf(userQueryResponse.getUserId()));
                    userMutiBonusDetailInModel.setCoinType(1);
                    adminUserMutiBonusDetailWMapper.addUserMutiBonusDetail(userMutiBonusDetailInModel);
                } else if (request.getAwardType() == 2) {
                    userBonusDetailInModel.setBonusDetailId(idGenerater.nextId());
                    userBonusDetailInModel.setExtraBonusId(extraBonusId);
                    userBonusDetailInModel.setCreateTime(new Date());
                    userBonusDetailInModel.setKgAmount(request.getBonus());
                    userBonusDetailInModel.setUserId(Long.valueOf(userQueryResponse.getUserId()));
                    adminUserBonusDetailWMapper.addUserBonusDetail(userBonusDetailInModel);
                }
                logger.info("推送发放奖励奖励消息 userId:" + userQueryResponse.getUserId() + "  message:" + message);
                confirmBonusPushModelList.add(new ConfirmBonusPushModel(userQueryResponse.getUserId(), message));
            }
        } catch (Exception e) {
            //删除所有生成monggo记录
            logger.info("-------------删除所有生成monggo记录----------------");
            deleteAllFlowRecoed(flowIds, request.getAwardType());
            throw new ApiException("发放奖励失败");
        }
        sendPushMessage(confirmBonusPushModelList);
        return isOk;
    }

    /**
     * 推送消息
     *
     * @param userId
     * @param messageText
     * @param tags
     */
    private void rewardPush(String userId, String messageText) {
        logger.info("【推送消息】userId：" + userId + "  messageText：" + messageText);
        pushService.rewardPush(userId, "收到奖励", messageText, "bonus");


    }

    private void deleteAllFlowRecoed(List<Long> flowIds, Integer awardType) {
        String tableName = "";
        if (awardType == 0 || awardType == 1) {
            logger.info("=================删除rit记录===============");
            tableName = MongoTables.ACCOUNT_FLOW_RIT;
        } else if (awardType == 2) {
            logger.info("=================删除氪金记录===============");
            tableName = MongoTables.ACCOUNT_FLOW_KG;
        }
        for (Long flowId : flowIds) {
            BasicDBObject querry = new BasicDBObject();
            querry.append("account_flow_id", flowId);
            logger.info("--------------" + querry.toString());
            deleteAllFlow(tableName, querry);

            querry = new BasicDBObject();
            querry.append("relation_flow_id", flowId);
            querry.append("user_id", Constants.PLATFORM_USER_ID);
            logger.info("--------------" + querry.toString());
            deleteAllFlow(tableName, querry);
        }
    }

    private void deleteAllFlow(String tableName, BasicDBObject querry) {
        MongoCursor<Document> cur = MongoUtils.findByFilter(tableName, querry);
        if (cur != null) {
            while (cur.hasNext()) {
                Document doc = cur.next();
                String id = doc.get("_id") + "";
                MongoUtils.deleteById(tableName, id);
            }
        }
    }

    @Override
    @Transactional
    public Result<UserQueryResponse> confirmBonus(UserBonusQueryRequest request, HttpServletRequest servletRequest) {
        logger.info("=========验证用户奖励=========");
        Result<UserQueryResponse> res = new Result<UserQueryResponse>();
        res = this.validateBonus(request, servletRequest);
        if (res.getErrorCode() != 10000) {
            return res;
        }

        logger.info("查询所有待发放用户奖励");
        Boolean isOk = true;
        List<UserQueryResponse> datas = adminUserRMapper.getUserListByMobile(request);
        if (datas != null && datas.size() > 0) {
            AccountInModel accountInModel = new AccountInModel();
            int b;
            accountInModel.setBalance(request.getBonus().multiply(new BigDecimal(datas.size())));
            accountInModel.setUserId(Constants.PLATFORM_USER_ID);
            if (request.getAwardType() == 0 || request.getAwardType() == 1) {
                logger.info("验证平台rit金额是否大于发放rit金额");
                b = accountRMapper.validatePlatformRitBanlace(accountInModel);
                if (b == 0) {
                    res.setErrorCode(Integer.parseInt(ExceptionEnum.ACCOUNT_BALANCE_RIT_ERROR.getCode()));
                    res.setErrorMsg(ExceptionEnum.ACCOUNT_BALANCE_RIT_ERROR.getMessage());
                    return res;

                }
            } else if (request.getAwardType() == 2) {
                logger.info("验证平台氪金金额是否大于发放氪金金额");
                b = accountFlowTxbRMapper.validatePlatformKgBanlace(accountInModel);
                if (b == 0) {
                    res.setErrorCode(Integer.parseInt(ExceptionEnum.ACCOUNT_BALANCE_KG_ERROR.getCode()));
                    res.setErrorMsg(ExceptionEnum.ACCOUNT_BALANCE_KG_ERROR.getMessage());
                    return res;
                }
            } else {
                res.setErrorCode(Integer.parseInt(ExceptionEnum.PARAMEMPTYERROR.getCode()));
                res.setErrorMsg(ExceptionEnum.PARAMEMPTYERROR.getMessage());
                return res;
            }
            this.getAward(request, datas);
        } else {
            res.setErrorCode(Integer.parseInt(ExceptionEnum.PARAMEMPTYERROR.getCode()));
            res.setErrorMsg(ExceptionEnum.PARAMEMPTYERROR.getMessage());
            return res;
        }
        return res;
    }


    @Transactional
    @Override
    public Result<UserQueryResponse> confirmBonusForFile(UserBonusListRequest request) throws ApiException {
        if (request == null) {
            return new Result<>(ExceptionEnum.PARAMEMPTYERROR);
        }
        List<UserBonus> list = request.getList();
        if (CollectionUtils.isEmpty(list)) {
            return new Result<>(ExceptionEnum.PARAMEMPTYERROR);
        }

        logger.info("入参：" + JSONObject.toJSONString(request));

        Integer awardType = request.getAwardType();
        Integer adminId = request.getAdminId();

        List<Long> flowIds = new ArrayList<>();

        //查询平台rit余额和kg余额
        AccountInModel platAccountInModel = new AccountInModel();
        platAccountInModel.setUserId(Constants.PLATFORM_USER_ID);
        AccountOutModel platAccountInfo = accountRMapper.selectByUserbalance(platAccountInModel);
        List<ConfirmBonusPushModel> confirmBonusPushModelList = new ArrayList<>();
        try {
            for (UserBonus userBonus : list) {

                BigDecimal bonus = userBonus.getAmount();
                String userId = userBonus.getUserId();
                String reason = userBonus.getReason();

                Long extraBonusId = addBonusToKgExtra(reason, awardType, bonus, adminId);
                checkPlatAccountInfo(awardType, bonus, platAccountInfo);

                Long flowId = idGenerater.nextId();
                flowIds.add(flowId);
                BonusInModel bonusInModel = buildeBaseBonusInModel(userId, bonus, reason, flowId);
                String message = issueRewards(bonusInModel, awardType, flowId, bonus, "收到千氪奖励");
                message += "奖励原因:" + reason + "";
                addAwardDetail(flowId, idGenerater, extraBonusId, bonus, userId, awardType);
                confirmBonusPushModelList.add(new ConfirmBonusPushModel(userId, message));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("-------------删除所有生成monggo记录----------------");
            deleteAllFlowRecoed(flowIds, request.getAwardType());
            throw new ApiException("发放奖励失败");
        }
        sendPushMessage(confirmBonusPushModelList);
        return new Result<>();
    }

    /**
     * 推送发放奖励奖励消息
     *
     * @param message
     * @param userId
     */
    private void sendPushMessage(List<ConfirmBonusPushModel> confirmBonusPushModelList) throws ApiException {
        logger.info("推送发放奖励奖励消息 confirmBonusPushModelList.size:" + confirmBonusPushModelList.size());
        if (CollectionUtils.isEmpty(confirmBonusPushModelList)) {
            return;
        }
        for (ConfirmBonusPushModel model : confirmBonusPushModelList) {
            logger.info("推送发放奖励奖励消息 model:" + JSONObject.toJSONString(model));
            String userId = model.getUserId();
            String message = model.getMessage();
            if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(message)) {
                return;
            }
            rewardPush(userId, message);
        }
    }

    /**
     * 插入奖励详细列表
     *
     * @param flowId
     * @param idGenerater
     * @param extraBonusId
     * @param bonus
     * @param userId
     */
    private void addAwardDetail(Long flowId, IDGen idGenerater, Long extraBonusId, BigDecimal bonus, String userId, Integer awardType) {

        logger.info("插入奖励详细列表");
        if (awardType == 0 || awardType == 1) {
            UserMutiBonusDetailInModel userMutiBonusDetailInModel = new UserMutiBonusDetailInModel();
            userMutiBonusDetailInModel.setAccountFlowId(flowId);
            userMutiBonusDetailInModel.setBonusDetailId(idGenerater.nextId());
            userMutiBonusDetailInModel.setExtraBonusId(extraBonusId);
            userMutiBonusDetailInModel.setCreateTime(new Date());
            userMutiBonusDetailInModel.setAmount(bonus);
            userMutiBonusDetailInModel.setUserId(Long.valueOf(userId));
            userMutiBonusDetailInModel.setCoinType(1);
            adminUserMutiBonusDetailWMapper.addUserMutiBonusDetail(userMutiBonusDetailInModel);
        }
        if (awardType == 2) {
            UserBonusDetailInModel userBonusDetailInModel = new UserBonusDetailInModel();
            userBonusDetailInModel.setKgAccountFlowId(flowId);
            userBonusDetailInModel.setBonusDetailId(idGenerater.nextId());
            userBonusDetailInModel.setExtraBonusId(extraBonusId);
            userBonusDetailInModel.setCreateTime(new Date());
            userBonusDetailInModel.setKgAmount(bonus);
            userBonusDetailInModel.setUserId(Long.valueOf(userId));
            adminUserBonusDetailWMapper.addUserBonusDetail(userBonusDetailInModel);
        }
    }

    /**
     * 发放奖励
     *
     * @param bonusInModel
     * @param awardType
     * @param flowId
     * @param bonus
     * @return
     */
    private String issueRewards(BonusInModel bonusInModel, Integer awardType, Long flowId, BigDecimal bonus, String message) throws ApiException {
        boolean success = false;
        if (awardType == 0) {
            logger.info("发放rit奖励到rit余额");
            bonusInModel.setFlowStatus(FlowStatusEnum.NOSTATUS.getStatus());
            bonusInModel.setBussinssId(BusinessTypeEnum.PLATFORMRITBONUS.getStatus());
            bonusInModel.setRewardType(1);
            success = addedRitBonus(bonusInModel);
            logger.info("发放【 rit余额 】奖励结果 ：" + success + "   入参：" + JSONObject.toJSONString(bonusInModel));
            message += bonus + "RIT,";
        }
        if (awardType == 1) {
            logger.info("发放奖励到rit冻结余额");
            //冻结状态
            bonusInModel.setFlowStatus(FlowStatusEnum.FREEZED.getStatus());
            bonusInModel.setBussinssId(BusinessTypeEnum.PLATFORMRITBONUS.getStatus());
            bonusInModel.setRewardType(2);
            success = addedRitBonus(bonusInModel);
            logger.info("发放【 rit冻结余额  】奖励结果 ：" + success + "   入参：" + JSONObject.toJSONString(bonusInModel));
            message += bonus + "RIT,";

        }
        if (awardType == 2) {
            logger.info("发放氪金奖励");
            bonusInModel.setBussinssId(BusinessTypeEnum.PLATFORMKGBONUS.getStatus());
            success = addedTxbBonus(bonusInModel);
            logger.info("发放【 氪金  】奖励结果 ：" + success + "   入参：" + JSONObject.toJSONString(bonusInModel));
            message += bonus + "KG,";
        }
        if (!success) {
            throw new ApiException();
        }
        return message;
    }

    private BonusInModel buildeBaseBonusInModel(String userId, BigDecimal bonus, String reason, Long flowId) {
        BonusInModel bonusInModel = new BonusInModel();
        bonusInModel.setUserId(Long.valueOf(userId));
        bonusInModel.setFromUserId(Constants.PLATFORM_USER_ID);
        bonusInModel.setFlowStatus(FlowStatusEnum.NOSTATUS.getStatus());
        bonusInModel.setBalance(bonus);
        bonusInModel.setModelType("PUBLISHBONUS");
        bonusInModel.setFromUserFlowDetail("发放千氪奖励:" + reason);
        bonusInModel.setFlowDetail("获得千氪奖励:" + reason);
        bonusInModel.setFlowId(flowId);
        return bonusInModel;
    }

    /**
     * 校验平台账户余额是否足够
     *
     * @param awardType
     * @param bonus
     * @param platAccountInfo
     * @throws ApiException
     */
    private void checkPlatAccountInfo(Integer awardType, BigDecimal bonus, AccountOutModel platAccountInfo) throws ApiException {
        BigDecimal diff = BigDecimal.ZERO;
        if (awardType == 0 || awardType == 1) {
            diff = platAccountInfo.getRitBalance().subtract(bonus);
            if (diff.compareTo(BigDecimal.ZERO) < 0) {
                logger.info("平台账户RIT金额不足");
                throw new ApiException("平台账户RIT余额不足");
            }
        }
        if (awardType == 2) {
            logger.info("验证平台账户氪金金额是否足够");
            diff = platAccountInfo.getTxbBalance().subtract(bonus);
            if (diff.compareTo(BigDecimal.ZERO) < 0) {
                logger.info("平台账户氪金金额不足");
                throw new ApiException("平台氪金余额不足");
            }
        }
    }

    /**
     * 记录主表流水kg_extra_bonus
     *
     * @param reason
     * @param awardType
     * @param bonus
     * @param adminId
     * @return
     */
    private Long addBonusToKgExtra(String reason, Integer awardType, BigDecimal bonus, Integer adminId) {
        logger.info("插入用户奖励主表数据");
        UserBonusInModel userBonusInModel = new UserBonusInModel();
        Long extraBonusId = idGenerater.nextId();
        userBonusInModel.setBonusReason(reason);
        userBonusInModel.setCreateTime(new Date());
        userBonusInModel.setExtraBonusId(extraBonusId);
        userBonusInModel.setTotalNum(1);
        userBonusInModel.setTotalKgAmount(BigDecimal.ZERO);
        userBonusInModel.setTotalTvAmount(BigDecimal.ZERO);
        userBonusInModel.setRewardType(awardType);
        if (awardType == 2) {
            userBonusInModel.setTotalKgAmount(bonus);
        }
        userBonusInModel.setSysUserId(adminId);
        adminUserBonusWMapper.addUserBonus(userBonusInModel);
        return extraBonusId;
    }


    @Override
    public JsonEntity bonusListUpload(MultipartFile[] files) {
        List<Map<String, String>> list = new ArrayList<>();
        try {
            for (MultipartFile file : files) {

                InputStream in = file.getInputStream();
                AnalysisEventListener<List<String>> listener = new AnalysisEventListener<List<String>>() {

                    @Override
                    public void invoke(List<String> object, AnalysisContext context) {
                        Map<String, String> map = new HashMap<>();
                        if (context.getCurrentRowNum() == 0) {
                            return;
                        }
                        String userId = object.get(0);
                        if (StringUtils.isEmpty(userId)) {
                            return;
                        }
                        UserInModel inModel = new UserInModel();
                        try {
                            inModel.setUserId(Long.valueOf(userId));
                        } catch (Exception e) {
                            logger.info("【Excel导入】用户ID有误 : " + userId);
                            return;
                        }
                        UserkgOutModel outModel = userRMapper.getUserInfo(inModel);
                        if (outModel == null) {
                            logger.info("【Excel导入】用户不存在 : " + JSONObject.toJSONString(inModel));
                            return;
                        }
                        map = buildeResultMap(outModel, object, map);
                        list.add(map);
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext context) {
                    }
                };
                ExcelReader excelReader = ExcelReaderFactory.getExcelReader(in, null, listener);
                excelReader.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonEntity.makeSuccessJsonEntity(list);
    }


    /**
     * 构建Excel上传奖励用户返回数据
     *
     * @param outModel
     * @param object
     * @param map
     * @return
     */
    private Map<String, String> buildeResultMap(UserkgOutModel outModel, List<String> object, Map<String, String> map) {
        map.put("userId", outModel.getUserId());
        map.put("userName", outModel.getUserName());
        map.put("phone", outModel.getUserMobile());
        map.put("regist", RegistOriginEnum.getByCode(outModel.getRegisterOrigin()).getMessage());
        map.put("createTime", DateUtils.formatDate(outModel.getCreateDate(), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
        map.put("role", UserRoleEnum.getUserRoleEnum(outModel.getUserRole()).getDisplay());
        map.put("amount", object.get(1));
        map.put("reason", object.get(2));
        return map;
    }

    /**
     * 初始化账户
     *
     * @param accountInModel
     * @param awardType      发放类型  1.kg 2.rit余额 3.rit冻结余额
     */
    public void initAccount(AccountInModel accountInModel, Integer awardType) {
        final String userAddress = propertyLoader.getProperty("common", "global.userTxAddress");
        Account account = accountRMapper.selectByUserId(accountInModel.getUserId());
        if (account != null) {
            if (awardType == 1) {
                accountWMapper.addTxbBalance(accountInModel);
            } else if (awardType == 2) {
                accountWMapper.addRitBalance(accountInModel);
            } else if (awardType == 3) {
                accountWMapper.addRitFrozenBalance(accountInModel);
            }
        } else {
            // 初始化账户
            Account userAccount = new Account();
            userAccount.setAccountId(idGenerater.nextId());
            userAccount.setUserId(accountInModel.getUserId());
            userAccount.setBalance(BigDecimal.valueOf(0.000d));
            userAccount.setFrozenBalance(BigDecimal.valueOf(0.000d));
            userAccount.setTxbFrozenBalance(BigDecimal.valueOf(0.000d));
            userAccount.setTxbBalance(BigDecimal.valueOf(0.000d));
            userAccount.setRitBalance(BigDecimal.valueOf(0.000d));
            userAccount.setRitFrozenBalance(BigDecimal.valueOf(0.000d));
            userAccount.setTxAddress(userAddress);
            if (awardType == 1) {
                userAccount.setTxbBalance(accountInModel.getBalance());
            } else if (awardType == 2) {
                userAccount.setRitBalance(accountInModel.getBalance());
            } else if (awardType == 3) {
                userAccount.setRitFrozenBalance(accountInModel.getBalance());
            }
            accountWMapper.insertSelective(userAccount);
        }
    }


}
