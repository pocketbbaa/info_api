package com.kg.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.lock.Lock;
import com.kg.platform.common.mongo.MongoTables;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.mongo.Seach;
import com.kg.platform.common.mq.MQProduct;
import com.kg.platform.common.utils.*;
import com.kg.platform.dao.entity.PushMessage;
import com.kg.platform.dao.read.AccountFlowRMapper;
import com.kg.platform.dao.read.KgInfoSwitchRMapper;
import com.kg.platform.dao.read.UserRMapper;
import com.kg.platform.dao.read.UserRelationRMapper;
import com.kg.platform.dao.write.AccountFlowWMapper;
import com.kg.platform.dao.write.AccountWMapper;
import com.kg.platform.enumeration.BusinessTypeEnum;
import com.kg.platform.enumeration.FlowStatusEnum;
import com.kg.platform.model.PupilTributeLogModel;
import com.kg.platform.model.UserTagBuild;
import com.kg.platform.model.in.AccountFlowInModel;
import com.kg.platform.model.in.AccountInModel;
import com.kg.platform.model.in.DiscipleInModel;
import com.kg.platform.model.in.UserInModel;
import com.kg.platform.model.out.AccountFlowOutModel;
import com.kg.platform.model.out.KgInfoSwitchOutModel;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.AccountRequest;
import com.kg.platform.model.request.DiscipleRequest;
import com.kg.platform.model.response.DiscipleInfoResponse;
import com.kg.platform.model.response.DiscipleRemindRespose;
import com.kg.platform.model.response.MasterInfoResponse;
import com.kg.platform.service.DiscipleService;
import com.kg.platform.service.PushService;
import com.kg.platform.service.TokenManager;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.client.MongoCursor;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Decimal128;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class DiscipleServiceImpl implements DiscipleService {

    private static final Logger logger = LoggerFactory.getLogger(DiscipleServiceImpl.class);

    private static final String USER_ACCOUNT_LOCK_KEY = "kguser::AccountService::updateAddUbalance::";

    private List<Long> userIds = new ArrayList<>();

    @Inject
    AccountFlowRMapper accountFlowRMapper;

    @Inject
    UserRelationRMapper userRelationRMapper;

    @Inject
    AccountFlowWMapper accountFlowWMapper;

    @Inject
    AccountWMapper accountWMapper;

    @Inject
    JedisUtils jedisUtils;

    @Inject
    UserRMapper userRMapper;

    @Inject
    IDGen idGenerater;

    @Inject
    MQProduct mqProduct;

    @Inject
    TokenManager manager;

    @Autowired
    KgInfoSwitchRMapper kgInfoSwitchRMapper;

    @Autowired
    UserTagsUtil userTagsUtil;

    @Autowired
    private PushService pushService;


    @Override
    public PageModel<DiscipleInfoResponse> getDiscipleContribution(DiscipleRequest request,
                                                                   PageModel<DiscipleInfoResponse> page) {
        //查询我的徒弟进贡进贡量排序信息
        List<DiscipleInfoResponse> subTributesRecord = this.sumSubTribute(request);
        //查询我的徒弟列表
        DiscipleInModel discipleInModel = new DiscipleInModel();
        discipleInModel.setUserId(request.getUserId());
        discipleInModel.setStart((page.getCurrentPage() - 1) * page.getPageSize());
        discipleInModel.setLimit(request.getPageSize());
        List<DiscipleInfoResponse> resps = new ArrayList<>();

        logger.info("徒弟数据========" + JsonUtil.writeValueAsString(subTributesRecord));
        if (CollectionUtils.isNotEmpty(subTributesRecord)) {
            resps = this.getSubTributeList(discipleInModel, subTributesRecord, request, page);
        } else {
            resps = this.getSubList(discipleInModel);
        }
        long count = accountFlowRMapper.getDiscipleInfosCount(discipleInModel);
        page.setData(resps);
        page.setTotalNumber(count);
        if (request.getColumnType() == 1) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String lastTime = sdf.format(new Date());
            jedisUtils.set(JedisKey.getLastRemindDate(request.getUserId()), lastTime);
        }
        return page;
    }

    @Override
    public PageModel<DiscipleInfoResponse> getPupilTribute(DiscipleRequest request, PageModel<DiscipleInfoResponse> page) {

        logger.info("传入参数： request：" + JSONObject.toJSONString(request) + "  page:" + JSONObject.toJSONString(page));
        long count = userRelationRMapper.selectInviteCountForApp(request.getUserId());
        logger.info("徒弟总数 count:" + count);
        if (count <= 0) {
            page.setData(new ArrayList<>());
            return page;
        }
        String key = "getPupilTribute:" + String.valueOf(request.getUserId());

        int currentPage = page.getCurrentPage();
        if (currentPage == 1) {
            jedisUtils.del(key);
        }
        PupilTributeLogModel model = jedisUtils.get(key, PupilTributeLogModel.class);
        logger.info("PupilTributeLogModel : " + JSONObject.toJSONString(model));
        if (model == null) {
            model = PupilTributeLogModel.init();
        }

        int pageIndex = (page.getCurrentPage() - 1) * page.getPageSize();
        int pageSize = page.getPageSize() <= 0 ? 20 : page.getPageSize();

        logger.info("sumSubTribute 入参： userId:" + request.getUserId() + "  pageIndex:" + pageIndex + "  pageSize:" + pageSize);
        List<DiscipleInfoResponse> subTributesRecord = sumSubTribute(request.getUserId(), pageIndex, pageSize);

        logger.info("sumSubTribute 结果：subTributesRecord：" + JSONObject.toJSONString(subTributesRecord));
        if (CollectionUtils.isNotEmpty(subTributesRecord)) {
            List<Long> ids = new ArrayList<>();
            for (DiscipleInfoResponse response : subTributesRecord) {
                ids.add(Long.valueOf(response.gettId()));
            }
            model = PupilTributeLogModel.addList(model, ids);

            logger.info("model:" + JSONObject.toJSONString(model));
            jedisUtils.set(key, model);
            if (subTributesRecord.size() == pageSize) {
                model = PupilTributeLogModel.addCount(model);
                jedisUtils.set(key, model);
                return buildDiscipleInfoResponse(page, subTributesRecord, count);
            }
        }

        int pageCount = model.getCount();
        int start = (page.getCurrentPage() - pageCount - 1) * page.getPageSize();
        List<Long> ids = model.getIds();
        logger.info("start:" + start + "  pageSize:" + pageSize + " ids.size:" + ids.size());
        List<DiscipleInfoResponse> subTributesRecordFromDB = getSubList(request.getUserId(), start, pageSize, ids);

        logger.info("subTributesRecordFromDB :" + JSONObject.toJSONString(subTributesRecordFromDB));
        subTributesRecord.addAll(subTributesRecordFromDB);
        if (CollectionUtils.isEmpty(subTributesRecord)) {
            page.setData(new ArrayList<>());
            return page;
        }
        return buildDiscipleInfoResponse(page, subTributesRecord, count);
    }

    /**
     * 构建返回参数
     *
     * @param page
     * @param subTributesRecord
     * @param count
     * @return
     */
    private PageModel<DiscipleInfoResponse> buildDiscipleInfoResponse(PageModel<DiscipleInfoResponse> page, List<DiscipleInfoResponse> subTributesRecord, long count) {
        List<DiscipleInfoResponse> subTributesRecordResponse = new ArrayList<>();
        for (DiscipleInfoResponse response : subTributesRecord) {
            Long userId = Long.valueOf(response.gettId());
            UserInModel inModel = new UserInModel();
            inModel.setUserId(userId);
            UserkgOutModel outModel = userRMapper.getUserProfiles(inModel);
            if (outModel == null) {
                continue;
            }
            response.setAvatar(outModel.getAvatar());
            response.setUserName(outModel.getUserName());
            subTributesRecordResponse.add(response);
        }
        page.setData(subTributesRecordResponse);
        page.setTotalNumber(count);
        return page;
    }


    private List<DiscipleInfoResponse> sumSubTribute(Long userId, int pageIndex, int pageSize) {
        List<Bson> conditions = getSubtribQuerry(userId, pageIndex, pageSize);
        List<DiscipleInfoResponse> subTributeList = new ArrayList<>();
        MongoCursor<Document> cu = MongoUtils.aggregate(MongoTables.SUB_TRIBUTE_RECORD, conditions);
        if (cu != null) {
            while (cu.hasNext()) {
                Document doc = cu.next();
                Long subId = doc.get("_id") == null ? null : doc.getLong("_id");
                if (subId == null) {
                    continue;
                }
                DiscipleInfoResponse discipleInfoResponse = new DiscipleInfoResponse();
                BigDecimal amount = doc.get("total") == null ? BigDecimal.ZERO : new BigDecimal(doc.get("total").toString());
                discipleInfoResponse.setIncome(amount);
                discipleInfoResponse.settId(subId.toString());
                subTributeList.add(discipleInfoResponse);
            }
        }
        return subTributeList;
    }

    private List<Bson> getSubtribQuerry(Long userId, int pageIndex, int pageSize) {
        List<Bson> conditions = new ArrayList<>();
        BasicDBObject querry = new BasicDBObject();
        querry.append("master_id", userId);
        querry.append("coin_type", 2);

        Bson querryCondition = new BasicDBObject("$match", querry);
        conditions.add(querryCondition);

        BasicDBObject groupBy = new BasicDBObject();
        groupBy.append("_id", "$sub_id");
        groupBy.append("total", new BasicDBObject("$sum", "$amount"));
        Bson groupcondition = new BasicDBObject("$group", groupBy);
        conditions.add(groupcondition);

        BasicDBObject sort = new BasicDBObject();
        sort.append("total", -1);
        Bson sortCondition = new BasicDBObject("$sort", sort);
        conditions.add(sortCondition);

        conditions.add(new BasicDBObject("$skip", pageIndex));
        conditions.add(new BasicDBObject("$limit", pageSize));
        logger.info("-----------conditions--------------" + conditions.toString());
        return conditions;
    }


    private List<DiscipleInfoResponse> getSubTributeList(DiscipleInModel discipleInModel, List<DiscipleInfoResponse> subTributesRecord, DiscipleRequest request, PageModel<DiscipleInfoResponse> page) {
        logger.info(".........subTributesRecord................." + JsonUtil.writeValueAsString(subTributesRecord));
        List<DiscipleInfoResponse> resps = new ArrayList<>();
        List<Long> subList = new ArrayList<>();
        for (DiscipleInfoResponse d : subTributesRecord) {
            if (d.gettId() != null) {
                subList.add(Long.valueOf(d.gettId()));
            }
        }
        logger.info(".........subList................." + JsonUtil.writeValueAsString(subList));
        discipleInModel.setSubList(subList);
        logger.info(".........discipleInModel................." + JsonUtil.writeValueAsString(discipleInModel));
        resps = userRelationRMapper.getSubTribList(discipleInModel);
        logger.info("---------徒弟列表-----------" + JsonUtil.writeValueAsString(resps));
        for (DiscipleInfoResponse discipleInfoResponse : resps) {
            String subId = discipleInfoResponse.gettId();
            DiscipleInfoResponse serchDis = this.serchInfo(subId, subTributesRecord);
            if (serchDis != null) {
                logger.info("-----serchDis-------" + JsonUtil.writeValueAsString(serchDis));
                if (request.getColumnType() == 0) {
                    //今日进贡
                    discipleInfoResponse.setTodayIncome(serchDis.getIncome());
                    String reDdate = DateUtils.parseTimestampByFormat(serchDis.getRelDate(), "HH:mm:ss");
                    discipleInfoResponse.setRelDate(reDdate);
                } else {
                    //累计进贡
                    discipleInfoResponse.setIncome(serchDis.getIncome());
                    String reDdate = DateUtils.parseTimestampByFormat(serchDis.getRelDate(), "yyyy-MM-dd HH:mm:ss ");
                    discipleInfoResponse.setRelDate(reDdate);
                }
            }
        }
        return resps;
    }

    private List<DiscipleInfoResponse> getSubList(DiscipleInModel discipleInModel) {
        List<DiscipleInfoResponse> resps = userRelationRMapper.getSubList(discipleInModel);
        for (DiscipleInfoResponse ds : resps) {
            ds.setRelDate("");
            ds.setTodayIncome(BigDecimal.ZERO);
            ds.setIncome(BigDecimal.ZERO);
        }
        return resps;
    }

    private List<DiscipleInfoResponse> getSubList(Long userId, int start, int pageSize, List<Long> ids) {
        List<DiscipleInfoResponse> resps = userRelationRMapper.getSubListFilter(userId, start, pageSize, ids);
        if (CollectionUtils.isEmpty(resps)) {
            return new ArrayList<>();
        }
        for (DiscipleInfoResponse ds : resps) {
            ds.setRelDate("");
            ds.setTodayIncome(BigDecimal.ZERO);
            ds.setIncome(BigDecimal.ZERO);
        }
        return resps;
    }


    private DiscipleInfoResponse serchInfo(String subId, List<DiscipleInfoResponse> subTributesRecord) {
        logger.info("---------查询徒弟进贡数----------");
        DiscipleInfoResponse discipleInfoResponse = null;
        if (subTributesRecord != null && subTributesRecord.size() > 0) {
            for (DiscipleInfoResponse d : subTributesRecord) {
                if (d.gettId().equals(subId)) {
                    discipleInfoResponse = new DiscipleInfoResponse();
                    discipleInfoResponse.setIncome(d.getIncome());
                    discipleInfoResponse.setRelDate(d.getRelDate());
                    logger.info("---------subId:{}---进贡数量：{}----", subId, d.getIncome());
                    break;
                }
            }
        }
        return discipleInfoResponse;
    }


    private List<DiscipleInfoResponse> sumSubTribute(DiscipleRequest request) {
        //查询徒弟进贡信息
        List<Bson> conditions = this.getSubtribQuerry(request);
        List<DiscipleInfoResponse> subTributeList = new ArrayList<>();
        MongoCursor<Document> cu = MongoUtils.aggregate(MongoTables.SUB_TRIBUTE_RECORD, conditions);
        if (cu != null) {
            while (cu.hasNext()) {
                Document doc = cu.next();
                logger.info(".........doc..........." + JsonUtil.writeValueAsString(doc));
                DiscipleInfoResponse discipleInfoResponse = new DiscipleInfoResponse();
                BigDecimal amount = doc.get("total") == null ? BigDecimal.ZERO : new BigDecimal(doc.get("total").toString());
                discipleInfoResponse.setIncome(amount);
                Long subId = doc.get("_id") == null ? null : doc.getLong("_id");
                if (subId != null) {
                    discipleInfoResponse.settId(subId.toString());
                }
                Long relDate = doc.get("relDate") == null ? null : doc.getLong("relDate");
                discipleInfoResponse.setRelDate(relDate.toString());
                subTributeList.add(discipleInfoResponse);
            }
        }
        return subTributeList;
    }

    private List<Bson> getSubtribQuerry(DiscipleRequest request) {
        List<Bson> conditions = new ArrayList<>();
        BasicDBObject querry = new BasicDBObject();
        querry.append("master_id", new BasicDBObject(Seach.EQ.getOperStr(), request.getUserId()));
        //今日进贡统计栏目
        //查询
        if (request.getColumnType() == 0) {
            Date now = new Date();
            Date startDate = DateUtils.getDateStart(now);
            Date endDate = DateUtils.getDateEnd(now);
            querry.append("create_date", BasicDBObjectBuilder.start(Seach.GTE.getOperStr(), startDate.getTime()).add(Seach.LTE.getOperStr(), endDate.getTime()).get());
        }
        querry.append("coin_type", 2);
        Bson querryCondition = new BasicDBObject("$match", querry);
        conditions.add(querryCondition);

        //分组统计
        BasicDBObject groupBy = new BasicDBObject();
        groupBy.append("_id", "$sub_id");
        groupBy.append("total", new BasicDBObject("$sum", "$amount"));
        groupBy.append("relDate", new BasicDBObject("$max", "$create_date"));
        Bson groupcondition = new BasicDBObject("$group", groupBy);
        conditions.add(groupcondition);

        //排序
        BasicDBObject sort = new BasicDBObject();
        sort.append("total", -1);
        Bson sortCondition = new BasicDBObject("$sort", sort);
        conditions.add(sortCondition);

        logger.info("-----------conditions--------------" + conditions.toString());
        return conditions;
    }


//    @Override
//    public Result<MasterInfoResponse> getMaterInfo(Long userId) {
//        AccountFlowOutModel accountFlowOutModel = accountFlowRMapper.getMasterInfo(userId);
//        MasterInfoResponse accountFlowResponse = new MasterInfoResponse();
//        if (accountFlowOutModel != null) {
//            accountFlowResponse = new MasterInfoResponse();
//            accountFlowResponse.setAvatar(accountFlowOutModel.getAvatar());
//            accountFlowResponse
//                    .setAwrad(accountFlowOutModel == null ? new BigDecimal("0.000") : accountFlowOutModel.getAwrad());
//            accountFlowResponse
//                    .setIncome(accountFlowOutModel == null ? new BigDecimal("0.000") : accountFlowOutModel.getIncome());
//            accountFlowResponse.setRecenflowDate(accountFlowOutModel.getRecenflowDate());
//            accountFlowResponse.setTodayIncome(
//                    accountFlowOutModel == null ? new BigDecimal("0.000") : accountFlowOutModel.getTodayIncome());
//            accountFlowResponse.settUId(accountFlowOutModel.gettUId());
//            accountFlowResponse.setUserName(accountFlowOutModel.getUserName());
//            UserTagBuild userTagBuild = userTagsUtil.buildTags(Long.valueOf(accountFlowOutModel.gettUId()));
//            accountFlowResponse.setIdentityTag(userTagBuild.getIdentityTag());
//            accountFlowResponse.setRealAuthedTag(userTagBuild.getRealAuthedTag());
//            accountFlowResponse.setVipTag(userTagBuild.getVipTag());
//        }
//        return new Result<MasterInfoResponse>(accountFlowResponse);
//    }


    public BasicDBObject getQuerryMaterInfo(Long masterId, Long userId) {
        //查询
        BasicDBObject querry = new BasicDBObject();
        querry.append("master_id", new BasicDBObject(Seach.EQ.getOperStr(), masterId));
        querry.append("sub_id", new BasicDBObject(Seach.EQ.getOperStr(), userId));
        querry.append("coin_type", new BasicDBObject(Seach.EQ.getOperStr(), 2));
        logger.info("---getMaterInfo:----querry:{}---------", querry.toString());
        return querry;
    }

    public BasicDBObject getSortMaterInfo() {
        //排序
        BasicDBObject sort = new BasicDBObject();
        sort.append("create_date", -1);
        logger.info("---getMaterInfo:----sort:{}---------", sort.toString());
        return sort;
    }

    @Override
    public Result<MasterInfoResponse> getMaterInfo(Long userId) {
        MasterInfoResponse masterInfoResponse = new MasterInfoResponse();
        BasicDBObject querry;
        BasicDBObject sort;
        BigDecimal income = BigDecimal.ZERO;
        BigDecimal todayIncome = BigDecimal.ZERO;
        Date date = new Date();
        Long start = DateUtils.getDateStart(date).getTime();
        Long end = DateUtils.getDateEnd(date).getTime();
        Long relDate = null;
        //获取我的师傅信息
        AccountFlowOutModel accountFlowOutModel = accountFlowRMapper.getMasterInfo(userId);
        logger.info("=============masterInfo============" + JsonUtil.writeValueAsString(accountFlowOutModel));
        if (accountFlowOutModel != null) {
            querry = this.getQuerryMaterInfo(Long.valueOf(accountFlowOutModel.gettUId()), userId);
            sort = getSortMaterInfo();
            MongoCursor<Document> doc = MongoUtils.findByFilter(MongoTables.SUB_TRIBUTE_RECORD, querry, sort);
            int i = 0;
            while (doc.hasNext()) {
                Document document = doc.next();
                BigDecimal amount = document.get("amount") == null ? BigDecimal.ZERO : ((Decimal128) document.get("amount")).bigDecimalValue().stripTrailingZeros();
                Long createDate = document.get("create_date") == null ? null : document.getLong("create_date");
                if (i == 0) {
                    relDate = createDate;
                }
                if (createDate != null) {
                    if (createDate >= start && createDate <= end) {
                        todayIncome = todayIncome.add(amount);
                    }
                }
                income = income.add(amount);
                i++;
            }
            logger.info("今日总进贡-----{}---历史总进贡----{}----最新进贡：{}---", todayIncome, income, relDate);
            masterInfoResponse = new MasterInfoResponse();
            masterInfoResponse.setAvatar(accountFlowOutModel.getAvatar());
            BigDecimal amount = accountFlowOutModel.getAmount();
            if (amount != null && amount.compareTo(new BigDecimal("0E-8")) != 0) {
                masterInfoResponse.setAwrad(amount);
            } else {
                masterInfoResponse.setAwrad(new BigDecimal(0));
            }
            masterInfoResponse.setIncome(income);
            if (relDate != null) {
                masterInfoResponse.setRecenflowDate(DateUtils.parseTimestampByFormat(relDate.toString(), "yyyy-MM-dd HH:mm:ss"));
            }
            masterInfoResponse.setTodayIncome(todayIncome);
            masterInfoResponse.settUId(accountFlowOutModel.gettUId());
            masterInfoResponse.setUserName(accountFlowOutModel.getUserName());
            UserTagBuild userTagBuild = userTagsUtil.buildTags(Long.valueOf(accountFlowOutModel.gettUId()));
            masterInfoResponse.setIdentityTag(userTagBuild.getIdentityTag());
            masterInfoResponse.setRealAuthedTag(userTagBuild.getRealAuthedTag());
            masterInfoResponse.setVipTag(userTagBuild.getVipTag());
            masterInfoResponse.setUserRole(userTagBuild.getRole());
        }
        return new Result<MasterInfoResponse>(masterInfoResponse);
    }


    /**
     * 打赏徒弟，徒弟账号+金额
     */
    @Override
    public Boolean rewardDisciple(AccountRequest accountRequest) {
        logger.info("打赏徒弟，徒弟账号+金额：{}", JSON.toJSONString(accountRequest));
        boolean success;
        Lock lock = new Lock();
        String key = USER_ACCOUNT_LOCK_KEY + accountRequest.getUserId() + accountRequest.getRewardUid();
        try {
            lock.lock(key);
            UserkgOutModel umodel = userRMapper.getUserDetails(accountRequest.getRewardUid());
            if (umodel == null) {
                return false;
            }
            AccountFlowInModel accountFlowInModel = new AccountFlowInModel();
            long flowid = idGenerater.nextId();
            accountFlowInModel.setAccountFlowId(flowid);
            accountFlowInModel.setRelationFlowId(flowid);
            accountFlowInModel.setUserId(accountRequest.getRewardUid());
            accountFlowInModel.setUserPhone(umodel.getUserMobile());
            accountFlowInModel.setUserEmail(umodel.getUserEmail());
            accountFlowInModel.setAmount(accountRequest.getBalance());
            accountFlowInModel.setAccountAmount(accountRequest.getBalance());
            accountFlowInModel.setFlowDate(new Date());
            UserkgOutModel userkgOutModel = userRMapper.getUserDetails(accountRequest.getUserId());
            accountFlowInModel.setFlowDetail("师傅" + userkgOutModel.getUserName() + "打赏了我");
            accountFlowInModel.setBusinessTypeId(BusinessTypeEnum.TPREWARDINCOME.getStatus());
            accountFlowInModel.setFlowStatus(FlowStatusEnum.NOSTATUS.getStatus());
            accountFlowWMapper.insertFlowSelective(accountFlowInModel);
            logger.info("师傅打赏徒弟，作者账号金额增加接口：AccountRequest={}", JSON.toJSONString(accountRequest));
            AccountInModel inModel = new AccountInModel();
            inModel.setBalance(accountRequest.getBalance());
            inModel.setUserId(accountRequest.getRewardUid());
            // 加锁
            success = accountWMapper.addUserbalance(inModel) > 0;

            // 推送消息APP 入库
            // 获取被审核的用户TOKEN，用于推送手机APP
            String token = manager.getTokenByUserId(Long.valueOf(umodel.getUserId()));
            String messageText = "收到师傅的打赏" + accountRequest.getBalance() + "TV";


            pushService.rewardDisciple(umodel.getUserId(), "系统通知", messageText, 1, "discipleReward");
            success = this.updateReductionUbalance(accountRequest, flowid);
        } finally {
            lock.unLock();
        }
        return success;
    }

    /**
     * 打赏徒弟，师傅账号-金额
     */
    @Override
    public Boolean updateReductionUbalance(AccountRequest request, Long flowid) {
        logger.info("用户打赏作者，当前用户账号金额减少接口：AccountRequest={}", JSON.toJSONString(request));

        // 查用户
        UserkgOutModel umodel = userRMapper.getUserDetails(request.getUserId());
        if (umodel == null) {
            return false;
        }
        // 查文章作者
        AccountFlowInModel accountFlowInModel = new AccountFlowInModel();
        long aFlowid = idGenerater.nextId();
        accountFlowInModel.setAccountFlowId(aFlowid);
        accountFlowInModel.setRelationFlowId(flowid);
        accountFlowInModel.setUserId(Long.parseLong(umodel.getUserId()));
        accountFlowInModel.setUserPhone(umodel.getUserMobile());
        accountFlowInModel.setUserEmail(umodel.getUserEmail());
        accountFlowInModel.setAmount(request.getBalance().negate());
        accountFlowInModel.setAccountAmount(request.getBalance().negate());
        accountFlowInModel.setFlowDate(new Date());
        UserkgOutModel userkgOutModel = userRMapper.getUserDetails(request.getRewardUid());
        accountFlowInModel.setFlowDetail("我打赏了徒弟" + userkgOutModel.getUserName());
        accountFlowInModel.setBusinessTypeId(BusinessTypeEnum.TPREWARDPAY.getStatus());
        accountFlowInModel.setFlowStatus(FlowStatusEnum.NOSTATUS.getStatus());
        accountFlowInModel.setArticleId(request.getArticleId());
        accountFlowWMapper.insertFlowSelective(accountFlowInModel);

        AccountInModel inModel = new AccountInModel();
        inModel.setUserId(request.getUserId());
        inModel.setBalance(request.getBalance());

        accountWMapper.updateReductionUbalance(inModel);

        return true;
    }

    @Override
    public DiscipleRemindRespose inviteRemind(DiscipleRequest request) {
        // 查询我的徒弟进贡提醒
        // 从redis里面回去上次获取时间
        String lastTime = jedisUtils.get(JedisKey.getLastRemindDate(request.getUserId()));
        Long date = 0L;
        if (StringUtils.isEmpty(lastTime)) {
            date = new Date().getTime();
        } else {
            date = DateUtils.parseDate(lastTime, "yyyy-MM-dd HH:mm:ss").getTime();
        }
        BasicDBObject querry = new BasicDBObject();
        querry.append("flow_date", new BasicDBObject(Seach.GTE.getOperStr(), date));
        querry.append("user_id", new BasicDBObject(Seach.EQ.getOperStr(), request.getUserId()));
        querry.append("business_type_id", new BasicDBObject(Seach.EQ.getOperStr(), BusinessTypeEnum.SUBCONTRI.getStatus()));
        MongoCursor<Document> s = MongoUtils.findByFilter(MongoTables.ACCOUNT_FLOW_KG, querry);
        int i = 0;
        if (s != null) {
            while (s.hasNext()) {
                i++;
            }
        }
        DiscipleRemindRespose discipleRemindRespose = new DiscipleRemindRespose();
        discipleRemindRespose.setDiscipleReminder(Long.valueOf(i));
        return discipleRemindRespose;
    }
}
