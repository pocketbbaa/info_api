package com.kg.platform.service.admin.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.kg.framework.toolkit.Check;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ApiException;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.lock.Lock;
import com.kg.platform.common.mongo.MongoTables;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.mq.ConsumeMessge;
import com.kg.platform.common.mq.MQConfig;
import com.kg.platform.common.mq.MQProduct;
import com.kg.platform.common.utils.*;
import com.kg.platform.common.utils.message.SendSms;
import com.kg.platform.dao.entity.*;
import com.kg.platform.dao.read.*;
import com.kg.platform.dao.read.admin.AdminArticleRMapper;
import com.kg.platform.dao.read.admin.AdminArticleRMapper1;
import com.kg.platform.dao.read.admin.AdminArticleStatRMapper;
import com.kg.platform.dao.read.admin.AdminBonusRMapper;
import com.kg.platform.dao.write.*;
import com.kg.platform.enumeration.ArticleAuditStatusEnum;
import com.kg.platform.enumeration.ArticleDisplayStatusEnum;
import com.kg.platform.enumeration.BusinessTypeEnum;
import com.kg.platform.enumeration.FlowStatusEnum;
import com.kg.platform.model.in.*;
import com.kg.platform.model.mongoTable.AccountFlowKgMongo;
import com.kg.platform.model.mongoTable.CountArticle;
import com.kg.platform.model.out.*;
import com.kg.platform.model.request.AccountRequest;
import com.kg.platform.model.request.admin.ArticleBatchReviewRequest;
import com.kg.platform.model.request.admin.ArticleEditRequest;
import com.kg.platform.model.request.admin.ArticleQueryRequest;
import com.kg.platform.model.request.admin.DeleteArticleRequest;
import com.kg.platform.model.response.PushArticleResponse;
import com.kg.platform.model.response.admin.*;
import com.kg.platform.service.PushService;
import com.kg.platform.service.TokenManager;
import com.kg.platform.service.admin.AdminRewardService;
import com.kg.platform.service.admin.ArticleService;
import com.mongodb.BasicDBObject;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;


@Service("AdminArticleService")
public class ArticleServiceImpl implements ArticleService {

    private static final String article_redis_key = "ArticleAppServiceImpl/getArticleOutModelText:";

    private static final String PUBLISH_ARTICLE_BONUS_KEY = "kguser::ArticleService::PublishArticleBonus::";

    private static final String article_push_limit_key = "article_push_number";
    @Autowired
    protected JedisUtils jedisUtils;
    protected Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
    @Inject
    TokenManager manager;
    @Autowired
    private AdminArticleRMapper adminArticleRMapper;
    @Autowired
    private ArticleWMapper articleWMapper;
    @Autowired
    private TagsRMapper tagsRMapper;
    @Autowired
    private TagsWMapper tagsWMapper;
    @Inject
    private IDGen generater;
    @Autowired
    private AdminArticleRMapper1 adminArticleRMapper1;
    @Autowired
    private AdminBonusRMapper adminBonusRMapper;
    @Autowired
    private AdminArticleStatRMapper adminArticleStatRMapper;
    @Autowired
    private UserRMapper userRMapper;
    @Autowired
    private UserRelationRMapper userRelationRMapper;
    @Autowired
    private AccountRMapper accountRMapper;
    @Autowired
    private AccountWMapper accountWMapper;
    @Autowired
    private AccountFlowWMapper accountFlowWMapper;
    @Autowired
    private AccountFlowRMapper accountFlowRMapper;
    @Autowired
    private KgArticleBonusWMapper kgArticleBonusWMapper;
    @Autowired
    private KgArticleStatisticsRMapper kgArticleStatisticsRMapper;
    @Autowired
    private KgArticleStatisticsWMapper kgArticleStatisticsWMapper;
    @Autowired
    private KgInfoSwitchRMapper kgInfoSwitchRMapper;
    @Inject
    private IDGen idGenerater;
    @Autowired
    private MQProduct mqProduct;
    @Autowired
    private SendSms sendSms;
    @Autowired
    private ArticleRMapper articleRMapper;
    @Autowired
    private MQConfig articleSyncMqConfig;
    @Autowired
    private KgCommonSettingRMapper kgCommonSettingRMapper;
    @Autowired
    private KgArticleExtendWMapper kgArticleExtendWMapper;
    @Autowired
    private KgArticleExtendRMapper kgArticleExtendRMapper;
    @Autowired
    private MQConfig awardMqConfig;
    @Autowired
    private AdminRewardService adminRewardService;

    @Autowired
    private PushService pushService;

    @Override
    public PageModel<ArticleQueryResponse> getArticleList(ArticleQueryRequest request,
                                                          PageModel<ArticleQueryResponse> page) {
        request.setStart((page.getCurrentPage() - 1) * page.getPageSize());
        request.setLimit(page.getPageSize());
        if (StringUtils.isNotEmpty(request.getStartDay()) && StringUtils.isNotEmpty(request.getEndDay())) {
            request.setStartDay(request.getStartDay() + " 00:00:00");
            request.setEndDay(request.getEndDay() + " 23:59:59");
        }
        List<ArticleQueryResponse> articleList = adminArticleRMapper.selectByCondition(request);
        if (null != articleList && articleList.size() > 0) {
            articleList.forEach(article -> {
                article.setPublishStatusDisplay(ArticleAuditStatusEnum
                        .getArticleAuditStatusEnum(NumberUtils.intValue(article.getPublishStatus())).getDisplay());
                article.setDisplayStatusDisplay(ArticleDisplayStatusEnum
                        .getArticleDisplayStatusEnum(NumberUtils.intValue(article.getDisplayStatus())).getDisplay());
                Date auditDate = DateUtils.parseDate(article.getAuditDate(), DateUtils.TIMESTAMP_FORMAT);
                article.setAuditDate(DateUtils.formatDate(auditDate, DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
                Date createDate = DateUtils.parseDate(article.getPublishDate(), DateUtils.TIMESTAMP_FORMAT);
                article.setCreateDate(DateUtils.formatDate(createDate, DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
                Date updateDate = DateUtils.parseDate(article.getUpdateDate(), DateUtils.TIMESTAMP_FORMAT);
                article.setUpdateDate(DateUtils.formatDate(updateDate, DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));

            });
        }
        long count = adminArticleRMapper.selectCountByCondition(request);
        page.setData(articleList);
        page.setTotalNumber(count);
        return page;
    }

    @Override
    public boolean setDisplayOrder(Long articleId, Integer displayOrder) {
        Article article = new Article();
        article.setArticleId(articleId + "");
        article.setDisplayOrder(displayOrder);
        int success = articleWMapper.updateByPrimaryKeySelective(article);
        if (success > 0) {
            sendArticleSyncMessage(article);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean auditArticle(Long articleId, Long auditUser, Integer columnId, Integer secondColumn,
                                Integer auditStatus, String refuseReason) {
        Article article = adminArticleRMapper1.selectByPrimaryKey(articleId);
        if (null != article) {
            ArticleAuditStatusEnum articleAuditStatusEnum = ArticleAuditStatusEnum
                    .getArticleAuditStatusEnum(NumberUtils.intValue(article.getPublishStatus()));
            switch (articleAuditStatusEnum) {
                case PASS:
                case REFUSE:
                case DRAFT:
                    return false;
                case AUDITING:
                    // 通过或不通过
                    articleAuditStatusEnum = ArticleAuditStatusEnum
                            .getArticleAuditStatusEnum(NumberUtils.intValue(auditStatus));
                    switch (articleAuditStatusEnum) {
                        case PASS: {
                            Article article1 = new Article();
                            article1.setArticleId(articleId + "");
                            article1.setAuditUser(auditUser);
                            article1.setAuditDate(new Date());
                            article1.setPublishStatus(auditStatus);
                            article1.setColumnId(columnId);
                            article1.setSecondColumn(secondColumn);
                            articleWMapper.updateByPrimaryKeySelective(article1);

                            KgArticleStatisticsInModel statisticsInModel = new KgArticleStatisticsInModel();
                            statisticsInModel.setArticleId(articleId);
                            KgArticleStatisticsOutModel statisticsOutModel = kgArticleStatisticsRMapper
                                    .selectByPrimaryKey(statisticsInModel);
                            statisticsInModel.setCreateUser(Long.valueOf(article.getCreateUser()));

                            // 文章统计表金额操作
                            if (statisticsOutModel == null) {
                                kgArticleStatisticsWMapper.insertSelective(statisticsInModel);
                            } else if (statisticsOutModel.getArticleId() != null) {
                                statisticsInModel.setThumbupNum(statisticsOutModel.getThumbupNum());
                                statisticsInModel.setCollectNum(statisticsOutModel.getCollectNum());
                                kgArticleStatisticsWMapper.updateStatistics(statisticsInModel);
                            }
                            insertCountArticleRecord(articleId.toString(), columnId, secondColumn, new Date().getTime(), 1);
                            sendArticleSyncMessage(article1);
                        }
                        return true;
                        case REFUSE: {

                            Article article1 = new Article();
                            article1.setArticleId(articleId + "");
                            article1.setAuditUser(auditUser);
                            article1.setAuditDate(new Date());
                            article1.setPublishStatus(auditStatus);
                            article1.setRefuseReason(refuseReason);
                            articleWMapper.updateByPrimaryKeySelective(article1);
                            // 冻结转可用 查询出被冻结了多少钱（sum(bonus_value) account_bonus)
                            AccountFlowInModel accountFlowInModel = new AccountFlowInModel();
                            accountFlowInModel.setBusinessTypeId(350);
                            accountFlowInModel.setArticleId(articleId);
                            List<AccountFlowOutModel> accountFlowOutModelList = accountFlowRMapper
                                    .selectByBusinessTypeId(accountFlowInModel);
                            if (null != accountFlowOutModelList && accountFlowOutModelList.size() > 0) {
                                KgArticleStatisticsInModel kgArticleStatisticsInModel = new KgArticleStatisticsInModel();
                                kgArticleStatisticsInModel.setArticleId(articleId);
                                KgArticleStatisticsOutModel kgArticleStatisticsOutModel = kgArticleStatisticsRMapper
                                        .selectByPrimaryKey(kgArticleStatisticsInModel);
                                BigDecimal bonusTotal = BigDecimal.ZERO;
                                if (null != kgArticleStatisticsOutModel) {
                                    bonusTotal = kgArticleStatisticsOutModel.getBonusTotal();
                                    Account account = accountRMapper.selectByUserId(Long.parseLong(article.getCreateUser()));
                                    account.setBalance(account.getBalance().add(kgArticleStatisticsOutModel.getBonusTotal()));
                                    account.setFrozenBalance(account.getFrozenBalance()
                                            .add(kgArticleStatisticsOutModel.getBonusTotal().multiply(new BigDecimal(-1))));
                                    accountWMapper.updateByPrimaryKeySelective(account);
                                }
                                // 生成一条交易记录
                                AccountFlowInModel model = new AccountFlowInModel();
                                model.setAccountAmount(bonusTotal);
                                model.setAccountDate(new Date());
                                model.setFlowDate(new Date());
                                model.setFlowStatus(8);
                                model.setAccountFlowId(generater.nextId());
                                model.setAmount(bonusTotal);
                                model.setArticleId(articleId);
                                model.setFlowDetail("为文章：《" + article.getArticleTitle() + "》设置阅读奖励 未生效");
                                model.setUserId(Long.parseLong(article.getCreateUser()));
                                UserkgOutModel user = userRMapper.selectByPrimaryKey(Long.parseLong(article.getCreateUser()));
                                if (null != user) {
                                    model.setUserEmail(user.getUserEmail());
                                    model.setUserPhone(user.getUserMobile());
                                }
                                model.setBusinessTypeId(350);
                                accountFlowWMapper.insertFlowSelective(model);
                                KgArticleBonusInModel kgArticleBonusInModel = new KgArticleBonusInModel();
                                kgArticleBonusInModel.setArticleId(articleId);
                                kgArticleBonusInModel.setBonusStatus(0);
                                kgArticleBonusWMapper.updateBonusStatusByArticleId(kgArticleBonusInModel);
                            }
                        }
                        return true;
                        default:
                            return false;
                    }
                default:
                    return false;

            }
        }
        return false;
    }

    @Override
    public boolean setDisplayStatus(String articleId, Integer updateUser, Integer displayStatus) {
        List<Long> idList = StringUtils.convertString2LongList(articleId, ",");
        idList.forEach(id -> {
            Article article = new Article();
            article.setArticleId(id + "");
            article.setUpdateSysUser(updateUser);
            // article.setUpdateDate(new Date());
            article.setDisplayStatus(displayStatus);
            Integer displayStatusOld = articleRMapper.getArticleDisplayStatus(id);
            articleWMapper.updateByPrimaryKeySelective(article);
            sendArticleSyncMessage(article);
            Article article1 = adminArticleRMapper1.selectByPrimaryKey(Long.valueOf(id));
            if (article1 != null) {
                insertCountArticleRecord(id.toString(), article1.getColumnId(), article1.getSecondColumn(), new Date().getTime(), 1);
            }
            // 获取置顶，推荐奖励
            if (displayStatus == 2 || displayStatus == 3) {
                adminRewardService.setDisplayStatusReward(displayStatus, String.valueOf(id), article1, displayStatusOld, 1);
            }
        });
        return true;
    }

    @Override
    public boolean setBlockchainUrl(String artId, String blockchainUrl) {
    	if(StringUtils.isBlank(blockchainUrl)){
    		blockchainUrl = "";
		}
        Long articleId = Long.parseLong(artId);

        KgArticleExtendInModel kgArticleExtendInModel = new KgArticleExtendInModel();
        kgArticleExtendInModel.setArticleId(articleId);
        kgArticleExtendInModel.setBlockchainUrl(blockchainUrl);

        KgArticleExtendOutModel kgArticleExtendOutModel = kgArticleExtendRMapper
                .selectByPrimaryKey(articleId);
        if (kgArticleExtendOutModel == null) {
            kgArticleExtendWMapper.insertSelective(kgArticleExtendInModel);
        } else {
            kgArticleExtendWMapper.updateByPrimaryKeySelective(kgArticleExtendInModel);
        }

        return true;
    }

    @Override
    @Transactional
    public String publishArticle(ArticleEditRequest request) {
        jedisUtils.del(article_redis_key + request.getArticleId()); // 删除资讯正文缓存
        Article article = new Article();
        article.setArticleTitle(request.getArticleTitle());
        article.setArticleText(request.getArticleText());
        article.setArticleDescription(request.getDescription());
        article.setArticleImage(request.getImage());
        article.setArticleType(request.getType());
        if (request.getPublishStatus() != ArticleAuditStatusEnum.REFUSE.getStatus()) {
            article.setColumnId(request.getColumnId());
            article.setSecondColumn(request.getSecondColumn());
        }
        article.setDisplayStatus(request.getDisplayStatus());
        article.setDisplayOrder(request.getDisplayOrder());
        article.setCommentSet(request.getCommentSet());
        article.setPublishSet(request.getPublishSet());
        article.setPublishTime(request.getPublishTime());
        article.setBonusStatus(request.getBonusStatus());
        article.setTextnum(request.getTextnum());
        article.setArticleLink(request.getArticleLink());
        article.setArticleSource(request.getArticleSource());
        // 根据邮箱号或手机号查询用户账号
        article.setSysUser(request.getSysUser());
        article.setRefuseReason(request.getRefuseReason());
        article.setTagnames(request.getTagnames());
        article.setCreateUser(request.getCreateUser());
        article.setPublishKind(request.getPublishKind());
        article.setVideoUrl(request.getVideoUrl());
        article.setVideoFilename(request.getVideoFilename());
        article.setAuditUser(request.getAuditUser());
        article.setIfPush(request.getIfPush());
        article.setIfPlatformPublishAward(request.getIfPlatformPublishAward());
        article.setArticleImgSize(request.getArticleImgSize());
        article.setIfIntoIndex(request.getIfIntoIndex());

        KgArticleExtendInModel kgArticleExtendInModel = new KgArticleExtendInModel();

        if (!StringUtils.isBlank(request.getTagnames())) {
            // 保存tagname，如果存在则+1，不存在则新增
            List<String> tagList = StringUtils.convertString2List(request.getTagnames(), ",");
            if (null != request.getCreateUser() && null != tagList && tagList.size() > 0) {
                List<Long> idList = new ArrayList<>();
                tagList.forEach(s -> {
                    TagsInModel tagsInModel = new TagsInModel();
                    tagsInModel.setTagName(s);
                    TagsOutModel model = tagsRMapper.getTags(tagsInModel);
                    if (null != model) {
                        Tags tags = new Tags();
                        tags.setTagId(model.getTagId());
                        if (model.getUsedNum() == null) {
                            model.setUsedNum(0);
                        }
                        tags.setUsedNum(model.getUsedNum() + 1);
                        tagsWMapper.updateByPrimaryKeySelective(tags);
                        idList.add(tags.getTagId());
                    } else {
                        TagsInModel tags = new TagsInModel();
                        tags.setCreateUser(Long.parseLong(request.getCreateUser()));
                        tags.setCreateDate(new Date());
                        tags.setTagName(s);
                        tags.setTagId(generater.nextId());
                        tagsWMapper.insertSelective(tags);
                        idList.add(tags.getTagId());
                    }
                });
                request.setArticleTag(StringUtils.convertList2String(idList, ','));
            }
        }
        article.setArticleTags(request.getArticleTag());

        if (null != request.getPublishStatus()) {
        	if(request.getPublishStatus() == ArticleAuditStatusEnum.DRAFT.getStatus()){
        		logger.info("后台将文章保存为草稿，更新创建时间------");
        		article.setCreateDate(new Date());
			}
            article.setPublishStatus(request.getPublishStatus());
        } else {
            article.setPublishStatus(ArticleAuditStatusEnum.PASS.getStatus());
        }

        KgArticleStatisticsInModel statisticsInModel = new KgArticleStatisticsInModel();
        if (request.getPublishStatus() == ArticleAuditStatusEnum.PASS.getStatus()
                || request.getPublishSet() == 1) {
            statisticsInModel
                    .setCreateUser((null == request.getCreateUser()) ? null : Long.parseLong(request.getCreateUser()));

            //1.3.2去掉初始化浏览量
//            if (null != request.getBrowseNum()) {
//                if (request.getBrowseNum().intValue() < 3000) {
//                    statisticsInModel.setBrowseNum(RandomUtils.randomInt(3511, 4523));
//                } else {
//                    statisticsInModel.setBrowseNum(request.getBrowseNum());
//                }
//            } else {
//                statisticsInModel.setBrowseNum(RandomUtils.randomInt(3511, 4523));
//            }
            if (request.getBrowseNum() == null) {
                statisticsInModel.setBrowseNum(0);
            } else {
                statisticsInModel.setBrowseNum(request.getBrowseNum());
            }
            statisticsInModel.setThumbupNum(request.getThumbupNum());
            statisticsInModel.setCollectNum(request.getCollectNum());
        }

        if (null != request.getArticleId()) {

            Integer displayStatusOld = articleRMapper.getArticleDisplayStatus(request.getArticleId());

            Article article1 = adminArticleRMapper1.selectByPrimaryKey(request.getArticleId());
            Integer publishStatus = article1.getPublishStatus();
            if (request.getPublishStatus() == ArticleAuditStatusEnum.PASS.getStatus()
                    || request.getPublishSet() == 1) {
                statisticsInModel.setArticleId(request.getArticleId());
                KgArticleStatisticsOutModel statisticsOutModel = kgArticleStatisticsRMapper
                        .selectByPrimaryKey(statisticsInModel);
                if (null == statisticsOutModel) {
                    kgArticleStatisticsWMapper.insertSelective(statisticsInModel);
                } else {
                    kgArticleStatisticsWMapper.updateStatistics(statisticsInModel);
                }

                // article.setAuditUser(request.getSysUser() == null ? null :
                // request.getSysUser().longValue());
                logger.info("<<<<<<<<<<<修改文章逻辑进入>>>>>>>>>>>>>>");

                if (article1.getAuditDate() == null || article1.getPublishStatus()!=ArticleAuditStatusEnum.PASS.getStatus()) {
                    logger.info("<<<<<<<<<<<修改文章逻辑进入2>>>>>>>>>>>>>>");
                    article.setAuditDate(new Date());
                }
                if(article1.getSysUser()!=null && article1.getPublishDate() == null && article1.getPublishStatus()!= ArticleAuditStatusEnum.PASS.getStatus()){
                	//说明是后台用户在发文 需要更新发布时间和审核时间
					logger.info("后台发布文章更新发布时间------");
					article.setPublishDate(new Date());
				}
            } else if (request.getPublishStatus() == ArticleAuditStatusEnum.REFUSE.getStatus()) {
                AccountFlowInModel accountFlowInModel = new AccountFlowInModel();
                accountFlowInModel.setBusinessTypeId(350);
                accountFlowInModel.setArticleId(request.getArticleId());
                List<AccountFlowOutModel> accountFlowOutModelList = accountFlowRMapper
                        .selectByBusinessTypeId(accountFlowInModel);
                if (null != accountFlowOutModelList && accountFlowOutModelList.size() > 0) {
                    KgArticleStatisticsInModel kgArticleStatisticsInModel = new KgArticleStatisticsInModel();
                    kgArticleStatisticsInModel.setArticleId(request.getArticleId());
                    KgArticleStatisticsOutModel kgArticleStatisticsOutModel = kgArticleStatisticsRMapper
                            .selectByPrimaryKey(kgArticleStatisticsInModel);
                    BigDecimal bonusTotal = BigDecimal.ZERO;
                    if (null != kgArticleStatisticsOutModel) {
                        bonusTotal = kgArticleStatisticsOutModel.getBonusTotal();
                        Account account = accountRMapper.selectByUserId(Long.parseLong(article.getCreateUser()));
                        account.setBalance(account.getBalance().add(kgArticleStatisticsOutModel.getBonusTotal()));
                        account.setFrozenBalance(account.getFrozenBalance()
                                .add(kgArticleStatisticsOutModel.getBonusTotal().multiply(new BigDecimal(-1))));
                        accountWMapper.updateByPrimaryKeySelective(account);
                    }
                    // 生成一条交易记录
                    AccountFlowInModel model = new AccountFlowInModel();
                    model.setAccountAmount(bonusTotal);
                    model.setAccountDate(new Date());
                    model.setFlowDate(new Date());
                    model.setFlowStatus(8);
                    model.setAccountFlowId(generater.nextId());
                    model.setAmount(bonusTotal);
                    model.setArticleId(request.getArticleId());
                    model.setFlowDetail("为文章：《" + article.getArticleTitle() + "》设置阅读奖励 未生效");
                    model.setUserId(Long.parseLong(article.getCreateUser()));
                    UserkgOutModel user = userRMapper.selectByPrimaryKey(Long.parseLong(article.getCreateUser()));
                    if (null != user) {
                        model.setUserEmail(user.getUserEmail());
                        model.setUserPhone(user.getUserMobile());
                    }
                    model.setBusinessTypeId(350);
                    accountFlowWMapper.insertFlowSelective(model);
                    KgArticleBonusInModel kgArticleBonusInModel = new KgArticleBonusInModel();
                    kgArticleBonusInModel.setArticleId(request.getArticleId());
                    kgArticleBonusInModel.setBonusStatus(0);
                    kgArticleBonusWMapper.updateBonusStatusByArticleId(kgArticleBonusInModel);
                }

                // article.setAuditUser(request.getSysUser() == null ? null :
                // request.getSysUser().longValue());
                article.setAuditDate(new Date());
            }
            if (request.getPublishSet() != null && request.getPublishSet() == 1) {
                article.setPublishStatus(ArticleAuditStatusEnum.DRAFT.getStatus());
                logger.info("后台定时发布文章更新创建时间------");
                article.setCreateDate(new Date());
            }
            article.setCreateUser(request.getCreateUser());
            if (null != request.getEditArticle() && request.getEditArticle() == 1) {
                article.setUpdateDate(new Date());
            }
            article.setUpdateSysUser(request.getSysUser());
            article.setArticleId(request.getArticleId() + "");
            article.setArticleFrom(1); // 抓取文章在后台编辑后自动标记为站内文章

            int updateSuccess = articleWMapper.updateByPrimaryKeySelective(article);
            // 修改拓展表对应信息
            kgArticleExtendInModel.setArticleId(request.getArticleId());
            kgArticleExtendInModel.setIfIntoIndex(request.getIfIntoIndex());

            KgArticleExtendOutModel kgArticleExtendOutModel = kgArticleExtendRMapper
                    .selectByPrimaryKey(request.getArticleId());
            if (kgArticleExtendOutModel == null) {
                if (kgArticleExtendInModel.getIfIntoIndex() != null && kgArticleExtendInModel.getIfIntoIndex() == 1) {
                    // 以前的文章可能没有在拓展表 则修改时插入到拓展表
                    kgArticleExtendWMapper.insertSelective(kgArticleExtendInModel);
                    //改变了文章显示，则要重新计算文章总量，将此记录记入mongo
                    if (request.getPublishStatus() != null & request.getPublishStatus() == 1) {
                        insertCountArticleRecord(request.getArticleId().toString(), request.getColumnId(), request.getSecondColumn(), new Date().getTime(), 1);
                    }
                }
            } else {
                // 拓展表有的话则直接修改
                if (!Objects.equals(kgArticleExtendOutModel.getIfIntoIndex(), kgArticleExtendInModel.getIfIntoIndex())) {
                    kgArticleExtendWMapper.updateByPrimaryKeySelective(kgArticleExtendInModel);
                    //改变了文章显示，则要重新计算文章总量，将此记录记入mongo
                    if (kgArticleExtendInModel.getIfIntoIndex() == 0 && request.getPublishStatus() != null & request.getPublishStatus() == 1) {
                        insertCountArticleRecord(request.getArticleId().toString(), request.getColumnId(), request.getSecondColumn(), new Date().getTime(), 2);
                    }
                    if (request.getPublishStatus() != null & request.getPublishStatus() == 1) {
                        insertCountArticleRecord(request.getArticleId().toString(), request.getColumnId(), request.getSecondColumn(), new Date().getTime(), 1);
                    }
                }
            }

            if (request.getPublishStatus() != null && request.getPublishStatus() == 1) {
                if (request.getPublishSet() == null || request.getPublishSet() != 1) {

                    if (!Objects.equals(article1.getPublishStatus(), request.getPublishStatus()) || !article1.getDisplayStatus().equals(request.getDisplayStatus())) {
                        //改变了文章显示，则要重新计算文章总量，将此记录记入mongo
                        if (request.getDisplayStatus() == 4) {
                            insertCountArticleRecord(request.getArticleId().toString(), request.getColumnId(), request.getSecondColumn(), new Date().getTime(), 2);
                        }
                        insertCountArticleRecord(request.getArticleId().toString(), request.getColumnId(), request.getSecondColumn(), new Date().getTime(), 1);
                    }

                    if (StringUtils.isNotBlank(request.getArticleTitle()) && !request.getArticleTitle().equals(article1.getArticleTitle())) {
                        //标题不一致了
                        insertCountArticleRecord(request.getArticleId().toString(), request.getColumnId(), request.getSecondColumn(), new Date().getTime(), 2);
                    }
                    if (StringUtils.isNotBlank(request.getDescription()) && !request.getDescription().equals(article1.getArticleDescription())) {
                        //摘要不一致了
                        insertCountArticleRecord(request.getArticleId().toString(), request.getColumnId(), request.getSecondColumn(), new Date().getTime(), 3);
                    }
                    if (StringUtils.isNotBlank(request.getImage()) && !request.getImage().equals(article1.getArticleImage())) {
                        insertCountArticleRecord(request.getArticleId().toString(), request.getColumnId(), request.getSecondColumn(), new Date().getTime(), 4);
                    }
                    if (kgArticleExtendOutModel != null && kgArticleExtendOutModel.getIfIntoIndex() != null && kgArticleExtendOutModel.getIfIntoIndex() == 1 && article1.getDisplayStatus() == 1) {
                        if (request.getDisplayStatus() == 2 || request.getDisplayStatus() == 3) {
                            //当文章以前是展示到首页且正常显示 变为推荐或者置顶时 首页栏目总量是不变的 所以要插入type为2的记录
                            insertCountArticleRecord(request.getArticleId().toString(), request.getColumnId(), request.getSecondColumn(), new Date().getTime(), 2);
                        }
                    }
                    if (article1.getDisplayStatus() == 2) {
                        //文章由置顶变为推荐 首页栏目总量也是不变的 所以要插入type为2的记录
                        if (request.getDisplayStatus() == 1 || request.getDisplayStatus() == 3) {
                            insertCountArticleRecord(request.getArticleId().toString(), request.getColumnId(), request.getSecondColumn(), new Date().getTime(), 2);
                        }
                    }
                    if (article1.getDisplayStatus() == 3) {
                        //文章由推荐变为置顶 首页栏目总量也是不变的 所以要插入type为2的记录
                        if (request.getDisplayStatus() == 1 || request.getDisplayStatus() == 2) {
                            insertCountArticleRecord(request.getArticleId().toString(), request.getColumnId(), request.getSecondColumn(), new Date().getTime(), 2);
                        }
                    }
                    if (!Objects.equals(article1.getColumnId(), request.getColumnId()) || !Objects.equals(article1.getSecondColumn(), request.getSecondColumn())) {
                        //栏目变化了
                        insertCountArticleRecord(request.getArticleId().toString(), article1.getColumnId(), article1.getSecondColumn(), new Date().getTime(), 1);
                        insertCountArticleRecord(request.getArticleId().toString(), request.getColumnId(), request.getSecondColumn(), new Date().getTime(), 1);
                    }


                    logger.info("非定时且审核通过的逻辑<<<<<<<<<<<<<<<");
                    logger.info("前端传入的ifPlatformPublishAward为:" + request.getIfPlatformPublishAward());

                    // 非定时且审核通过的进行推送
                    if (request.getIfPush() != null && request.getIfPush() == 1) {
                        logger.info("前端传入的ifPush为:" + request.getIfPush() + "publishKind:" + request.getPublishKind()
                                + "publishstatus:" + request.getPublishStatus() + "type:" + request.getType());
                        mqPush(article, request, 1);
                    }
                }
                logger.info("【后台发文】，更新文章结果  updateSuccess：" + updateSuccess);
                if (updateSuccess > 0) {
                    //首次发文并审核通过后自动发放奖励
                    adminRewardService.setFirstPostReward(article);
                    // 置顶推荐奖励
                    adminRewardService.setDisplayStatusReward(article.getDisplayStatus(), article.getArticleId(), article, displayStatusOld, publishStatus);
                }
            }
            sendArticleSyncMessage(article);
            return request.getArticleId().toString();
        } else {
            long articleId = generater.nextId();

            // 如果定时发布开启，则存为草稿
            if (request.getPublishSet() == 1) {
                article.setPublishStatus(ArticleAuditStatusEnum.DRAFT.getStatus());
            }
            article.setArticleId(articleId + "");
            article.setCreateDate(new Date());
            article.setCreateUser(request.getCreateUser());

            statisticsInModel.setArticleId(articleId);

            if (request.getPublishStatus() == ArticleAuditStatusEnum.PASS.getStatus()) {
                kgArticleStatisticsWMapper.insertSelective(statisticsInModel);
            }

            articleWMapper.insertSelective(article);
            // 存入扩展信息到文章扩展表
            if (article.getIfIntoIndex() != null) {
                kgArticleExtendInModel.setArticleId(articleId);
                kgArticleExtendInModel.setIfIntoIndex(article.getIfIntoIndex());
                kgArticleExtendWMapper.insertSelective(kgArticleExtendInModel);
            }
            sendArticleSyncMessage(article);


            return String.valueOf(articleId);
        }
    }

    /**
     * 发送数据同步消息
     *
     * @param article
     */
    private void sendArticleSyncMessage(Article article) {
        Map<String, Object> map = new HashMap<>();
        map.put("types", "0");
        map.put("articleId", article.getArticleId());
        logger.info("ES同步文章MQ------topic:{},  map:{}", articleSyncMqConfig.getTopic(), JSONObject.toJSON(map));
        mqProduct.sendMessage(articleSyncMqConfig.getTopic(), articleSyncMqConfig.getTags(), null, JSON.toJSONString(map));
    }

    public void insertCountArticleRecord(String articleId, Integer columnId, Integer secondColumn, Long createDate, Integer type) {
        CountArticle countArticle = new CountArticle();
        countArticle.setArticleId(articleId);
        countArticle.setColumnId(columnId);
        countArticle.setSecondColumn(secondColumn);
        countArticle.setCreateDate(createDate);
        countArticle.setType(type);
        MongoUtils.insertOne(MongoTables.KG_COUNT_ARTICLE, new Document(Bean2MapUtil.bean2map(countArticle)));
    }

    @Override
    public PageModel<AdminArticleReportResponse> toAuditList(PageModel<AdminArticleReportResponse> page) {
        int pageIndex = (page.getCurrentPage() - 1) * page.getPageSize();
        int pageSize = page.getPageSize();
        List<AdminArticleReportResponse> adminArticleReportResponses = adminArticleRMapper.toAuditList(pageIndex, pageSize);
        Integer count = adminArticleRMapper.toAuditListCount(pageIndex, pageSize);
        page.setData(adminArticleReportResponses);
        page.setTotalNumber(count);
        return page;
    }

    @Override
    public PageModel<AdminArticleReportByArticleIdResponse> toAuditListByArticleId(PageModel<AdminArticleReportByArticleIdResponse> page, ArticleEditRequest request) {
        int pageIndex = (page.getCurrentPage() - 1) * page.getPageSize();
        int pageSize = page.getPageSize();
        Long articleId = request.getArticleId();
        if (articleId == null || articleId <= 0) {
            return page;
        }
        List<AdminArticleReportByArticleIdResponse> adminArticleReportResponses = adminArticleRMapper.toAuditListByArticleId(articleId, pageIndex, pageSize);
        Integer count = adminArticleRMapper.toAuditListByArticleIdCount(articleId, pageIndex, pageSize);
        page.setData(adminArticleReportResponses);
        page.setTotalNumber(count);
        return page;
    }

    @Override
    public JsonEntity disposeReport(ArticleEditRequest request) {

        Long articleId = request.getArticleId();
        if (articleId == null || articleId <= 0) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        Integer success = articleWMapper.disposeReport(articleId);
        if (success > 0) {
            return JsonEntity.makeSuccessJsonEntity();
        }
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SYS_EXCEPTION);
    }

    public void delArticlePageRedis(Integer columnId) {
        String scolumnId = columnId != null ? columnId.toString() : "";
        jedisUtils.delKeys(JedisKey.getArticlePageKeyPattern(scolumnId), Lists.newArrayList());
        jedisUtils.delKeys(JedisKey.getCountArticleNum(scolumnId) + "*", Lists.newArrayList());
        //清除后 重新统计栏目下文章总量
//        articlekgService.pushCountArticleMq("");
    }

    private void mqPush(Article article, ArticleEditRequest request, Integer serviceType) {


        pushService.publishArticle(article, request, serviceType);

        checkPushArticle();
    }

    public void checkPushArticle() {
        Lock lock = new Lock();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        try {
            lock.lock(article_push_limit_key);
            Integer number = jedisUtils.get(JedisKey.getPushArticleLimit(), Integer.class);
            int numberOfDay = 0;
            if (number == null) {
                jedisUtils.set(JedisKey.getPushArticleLimit(), String.valueOf(numberOfDay + 1), zero.getTime());

            } else {
                numberOfDay = number;
                jedisUtils.set(JedisKey.getPushArticleLimit(), String.valueOf(numberOfDay + 1), zero.getTime());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            lock.unLock();
        }
    }

    @Override
    public boolean deleteArticle(DeleteArticleRequest request) {
        int success = articleWMapper.deleteByPrimaryKey(request.getArticleId());
        if (success > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("types", "1");
            map.put("articleId", request.getArticleId());
            logger.info("ES同步文章MQ------topic:{},  map:{}", articleSyncMqConfig.getTopic(), JSONObject.toJSON(map));
            mqProduct.sendMessage(articleSyncMqConfig.getTopic(), articleSyncMqConfig.getTags(), null, JSON.toJSONString(map));
            return true;
        }
        return false;
    }

    @Override
    public Article getArticleById(Long articleId) {
        Article article = adminArticleRMapper1.selectByPrimaryKey(articleId);
        if (article == null) {
            return null;
        }
        if (article.getIfIntoIndex() == null) {
            article.setIfIntoIndex(0);
        }
        Integer titleModify = article.getTitleModify();
        article.setTitleModify(titleModify == null ? 0 : titleModify);

        KgArticleStatisticsInModel statisticsInModel = new KgArticleStatisticsInModel();
        statisticsInModel.setArticleId(articleId);
        KgArticleStatisticsOutModel statisticsOutModel = kgArticleStatisticsRMapper
                .selectByPrimaryKey(statisticsInModel);

        if (!Check.NuNObject(statisticsOutModel)) {
            article.setBrowseNum(statisticsOutModel.getBrowseNum());
            article.setThumbupNum(statisticsOutModel.getThumbupNum());
            article.setCollectNum(statisticsOutModel.getCollectNum());
        }

        if (!Check.NuNObject(article.getCreateUser())) {
            UserkgOutModel kgUser = userRMapper.selectByPrimaryKey(Long.parseLong(article.getCreateUser()));
            if (!Check.NuNObject(kgUser)) {
                article.setUsername(kgUser.getUserName());
            }
        }

        return article;
    }

    @Override
    public List<BonusQueryResponse> getBonus(ArticleEditRequest request) {
        List<BonusQueryResponse> data = adminBonusRMapper.selectAll(request);
        if (null != data && data.size() > 0) {
            data.forEach(item -> item.setTotal(item.getValue() * item.getMax()));
        }
        return data;
    }

    @Override
    public ArticleStatQueryResponse getArticleStat(ArticleEditRequest request) {
        return adminArticleStatRMapper.selectAll(request);
    }

    @Override
    public Boolean freezePublishBonus(ArticleQueryRequest request) {
        Boolean isOk = true;

        String freezeReason = request.getFreezeReason();
        freezeReason = StringUtils.isEmpty(freezeReason) ? " " : freezeReason;

        Article article = new Article();
        article.setArticleId(request.getArticleId());
        article.setPublishBonusStatus(request.getPublishStatus());
        article.setOperFreezeTime(new Date());
        article.setOperFreezeUser(request.getAdminId());
        article.setFreezeReason(freezeReason);
        isOk = articleWMapper.updateByPrimaryKeySelective(article) > 0;

        // 如果冻结时则取消优质文章标识
        if (request.getPublishStatus() == 0) {
            Article article1 = new Article();
            article1.setArticleId(request.getArticleId());
            article1.setArticleMark(0);
            isOk = articleWMapper.updateByPrimaryKeySelective(article1) > 0;
        }
        if (isOk) {
            sendArticleSyncMessage(article);
        }
        return isOk;

    }

    @Override
    public Boolean markHighQualityArticles(ArticleQueryRequest request) {
        Article article = new Article();
        article.setArticleId(request.getArticleId());
        article.setArticleMark(request.getArticleMark());
        int count = articleWMapper.updateByPrimaryKeySelective(article);
        if (count > 0) {
            sendArticleSyncMessage(article);
            if (article.getArticleMark() == 1) {
                //优质原创奖励
                adminRewardService.markHighQualityArticlesReward(article.getArticleId());
            }
            ArticleOutModel articleOutModel = articleRMapper.selectByPrimaryKey(Long.valueOf(request.getArticleId()));
            insertCountArticleRecord(articleOutModel.getArticleId(), articleOutModel.getColumnId(), articleOutModel.getSecondColumn(), new Date().getTime(), 1);
            return true;
        }
        return false;
    }

    @Override
    public Boolean getArticleAddedTvBonus(ArticleQueryRequest request) {
        return adminArticleRMapper.getArticleAddedTvBonus(Long.valueOf(request.getArticleId())) > 0;
    }

    @Override
    public Boolean getArticleAddedTxbBonus(ArticleQueryRequest request) {
        return adminArticleRMapper.getArticleAddedTxbBonus(Long.valueOf(request.getArticleId())) > 0;
    }

    @Override
    @Transactional
    public Boolean publishArticleBonus(AccountRequest request) {
        Boolean isok = true;
        Long articleId = request.getArticleId();
        ArticleQueryRequest articleQueryRequest = new ArticleQueryRequest();
        articleQueryRequest.setArticleId(articleId.toString());
        ArticleQueryResponse articleQueryResponse = adminArticleRMapper.getArticle(articleQueryRequest);
        if (articleQueryResponse == null) {
            return false;
        }
        // 查文章作者
        UserkgOutModel umodel = userRMapper.getUserDetails(Long.valueOf(articleQueryResponse.getCreateUser()));
        if (null == umodel) {
            return false;
        }

        logger.info("发放用户发文奖励");
        long flowid = idGenerater.nextId();
        request.setFlowId(flowid);
        request.setArticleId(articleId);
        request.setUserId(Long.valueOf(articleQueryResponse.getCreateUser()));
        request.setUserEmail(umodel.getUserEmail());
        request.setUserPhone(umodel.getUserMobile());
        isok = this.publishTvBonus(request);

        logger.info("发放师傅进贡氪金奖励");
        UserRelation userRelation = userRelationRMapper.selectParentUser(request.getUserId());
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>发放师傅进贡氪金奖励>>>>>>>>>>>>>>>>>>>>>>" + userRelation);
        if (userRelation != null) {
            logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>发放师傅进贡氪金奖励>>>>>>>>>>>>>>>>>>>>>>");
            request.setRelationFlowId(flowid);
            flowid = idGenerater.nextId();
            request.setFlowId(flowid);
            request.setUserId(userRelation.getUserId());
            request.setSubUserId(userRelation.getRelUserId());
            logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>发放师傅进贡氪金奖励入参>>>>>>>>>>>>>>>>>>>>>>"
                    + JsonUtil.writeValueAsString(request));
            isok = this.publishKGBonus(request);
            logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>发放师傅进贡氪金奖励>>>>>>>>>>>>>>>>>>>>>>");
        }

        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>发送专栏作者奖励短信>>>>>>>>>>>>>>>>>>>>>>");
        // 发送专栏作者奖励短信
        if (isok && umodel.getUserRole() != 1) {
            sendSms.send(umodel.getUserMobile(), "尊敬的用户，您的文章《" + articleQueryResponse.getArticleTitle() + "》符合奖励标准，已获得"
                    + Constants.PUBLISG_BONUS_TV + "TV奖励。登录千氪，查看详情吧！");
        }
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>推送获得发文奖励的作者到APP>>>>>>>>>>>>>>>>>>>>>>" + isok);
        if (isok) {
            // 推送获得发文奖励的作者到APP
            String messageText = "";
            messageText = "您的《" + articleQueryResponse.getArticleTitle() + "》已审核通过,获得基础发文奖励"
                    + Constants.PUBLISG_BONUS_TV + "TV";
            String token = manager.getTokenByUserId(Long.valueOf(umodel.getUserId()));
            KgInfoSwitchOutModel outModel = kgInfoSwitchRMapper.selectByPrimaryKey(Long.valueOf(umodel.getUserId()));
            boolean ifPush = false;
            if (outModel == null) {
                ifPush = true;
            } else if (outModel.getSystemInfoSwitch() == 1) {
                ifPush = true;
            }
            if (ifPush) {
                if (StringUtils.isNotBlank(token)) {
                    mqPush(umodel.getUserId(), messageText, "publishArticleReward", 0);
                }
            }
            // 推送进贡信息给师傅的APP
            if (userRelation != null) {
                token = manager.getTokenByUserId(userRelation.getUserId());
                outModel = kgInfoSwitchRMapper.selectByPrimaryKey(userRelation.getUserId());
                ifPush = false;
                if (outModel == null) {
                    ifPush = true;
                } else if (outModel.getSystemInfoSwitch() == 1) {
                    ifPush = true;
                }
                if (ifPush) {
                    if (StringUtils.isNotBlank(token)) {
                        messageText = "收到" + umodel.getUserName() + "的进贡" + Constants.PUBLISG_MASTER_KG + "KG";
                        mqPush(userRelation.getUserId().toString(), messageText, "publishArticleToMasterReward", 1);
                    }
                }
            }
        }

        return isok;
    }

    private void mqPush(String userId, String messageText, String tags, Integer pushPlace) {


        pushService.publishArticleBonus(userId, messageText, tags, pushPlace);
    }

    @Override
    public Boolean publishTvBonus(AccountRequest request) {
        Boolean isok = true;
        Lock lock = new Lock();
        String key = PUBLISH_ARTICLE_BONUS_KEY + "PUBLISH_TV_BONUS" + request.getUserId();
        try {
            lock.lock(key);
            // 发布者余额+
            AccountInModel accountInModel = new AccountInModel();
            accountInModel.setUserId(request.getUserId());
            accountInModel.setBalance(Constants.PUBLISG_BONUS_TV);
            isok = accountWMapper.addUserbalance(accountInModel) > 0;
            ;

            // 发布者余额增加流水
            AccountFlowInModel accountFlowInModel = new AccountFlowInModel();
            accountFlowInModel.setAccountFlowId(request.getFlowId());
            accountFlowInModel.setRelationFlowId(request.getFlowId());
            accountFlowInModel.setUserId(request.getUserId());
            accountFlowInModel.setUserPhone(request.getUserPhone());
            accountFlowInModel.setArticleId(request.getArticleId());
            accountFlowInModel.setUserEmail(request.getUserEmail());
            accountFlowInModel.setAmount(Constants.PUBLISG_BONUS_TV);
            accountFlowInModel.setAccountAmount(Constants.PUBLISG_BONUS_TV);
            accountFlowInModel.setFlowDate(new Date());
            accountFlowInModel.setBusinessTypeId(BusinessTypeEnum.PUBLISHAWARD.getStatus());
            accountFlowInModel.setFlowStatus(FlowStatusEnum.NOSTATUS.getStatus());
            accountFlowInModel.setFlowDate(new Date());
            accountFlowInModel.setFlowDetail("我收到平台发文奖励" + Constants.PUBLISG_BONUS_TV + "TV");
            logger.info("发布者流水记录添加参数+" + JSON.toJSONString(accountFlowInModel));
            isok = accountFlowWMapper.insertFlowSelective(accountFlowInModel) > 0;
            ;

            // 平台账户减少余额
            accountInModel = new AccountInModel();
            accountInModel.setUserId(Constants.PLATFORM_USER_ID);
            accountInModel.setBalance(Constants.PUBLISG_BONUS_TV.negate());
            logger.info("平台账户减少余额+" + JSON.toJSONString(accountInModel));
            isok = accountWMapper.addUserbalance(accountInModel) > 0;
            ;

            // 添加平台减少流水
            accountFlowInModel = new AccountFlowInModel();
            long uflowid = idGenerater.nextId();
            accountFlowInModel.setAccountFlowId(uflowid);
            accountFlowInModel.setRelationFlowId(request.getFlowId());
            accountFlowInModel.setUserId(Constants.PLATFORM_USER_ID);
            accountFlowInModel.setArticleId(request.getArticleId());
            accountFlowInModel.setAmount(Constants.PUBLISG_BONUS_TV.negate());
            accountFlowInModel.setAccountAmount(Constants.PUBLISG_BONUS_TV.negate());
            accountFlowInModel.setFlowDate(new Date());
            accountFlowInModel.setBusinessTypeId(BusinessTypeEnum.PUBLISHAWARD.getStatus());
            accountFlowInModel.setFlowStatus(FlowStatusEnum.NOSTATUS.getStatus());
            accountFlowInModel.setFlowDate(new Date());
            accountFlowInModel.setFlowDetail("发放用户" + request.getUserId() + "发文奖励" + Constants.PUBLISG_BONUS_TV + "TV");
            logger.info("平台账户减少余额流水+" + JSON.toJSONString(accountFlowInModel));
            isok = accountFlowWMapper.insertFlowSelective(accountFlowInModel) > 0;

        } catch (Exception e) {
            return false;
        } finally {
            lock.unLock();
        }
        return isok;

    }

    @Override
    public Boolean publishKGBonus(AccountRequest request) {
        logger.info("获取发文奖励KG：{}", JSON.toJSONString(request.getUserId()));
        boolean success;
        Lock lock = new Lock();
        // Long userId = request.getUserId();
        String key = PUBLISH_ARTICLE_BONUS_KEY + "PUBLISH_KG_BONUS" + request.getUserId();
        try {
            lock.lock(key);
            // 查当前用户
            UserkgOutModel userkgModel = userRMapper.getUserDetails(request.getUserId());
            if (request.getUserId() == null || userkgModel == null) {
                logger.info("获取阅读人用户id {}", request.getUserId());
                return false;
            }

            AccountFlowKgMongo accountFlowKg = new AccountFlowKgMongo();
            accountFlowKg.setAccount_flow_id(request.getFlowId());
            accountFlowKg.setRelation_flow_id(request.getRelationFlowId());
            accountFlowKg.setUser_id(request.getUserId());
            accountFlowKg.setUser_phone(userkgModel.getUserMobile());
            accountFlowKg.setUser_email(userkgModel.getUserEmail());
            accountFlowKg.setUser_name(userkgModel.getUserName());
            accountFlowKg.setAmount(Constants.PUBLISG_MASTER_KG);
            accountFlowKg.setAccount_amount(Constants.PUBLISG_MASTER_KG);
            UserkgOutModel subUserkgModel = userRMapper.getUserDetails(request.getSubUserId());
            accountFlowKg.setFlow_detail("获得徒弟" + subUserkgModel.getUserName() + "进贡");
            accountFlowKg.setBusiness_type_id(BusinessTypeEnum.SUBCONTRI.getStatus());
            accountFlowKg.setArticle_id(request.getArticleId());
            accountFlowKg.setFlow_date(new Date().getTime());
            accountFlowKg.setAccount_date(new Date().getTime());
            accountFlowKg.setFlow_status(FlowStatusEnum.NOSTATUS.getStatus());
            logger.info("氪金奖励记录入参 {}", JsonUtil.writeValueAsString(accountFlowKg));
            MongoUtils.insertOne(MongoTables.ACCOUNT_FLOW_KG, new Document(Bean2MapUtil.bean2map(accountFlowKg)));

            AccountInModel inModel = new AccountInModel();
            inModel.setUserId(request.getUserId());
            inModel.setBalance(Constants.PUBLISG_MASTER_KG);
            logger.info("氪金奖励流水入参 {}", JsonUtil.writeValueAsString(inModel));
            success = accountWMapper.addTxbBalance(inModel) > 0;

            accountFlowKg = new AccountFlowKgMongo();
            accountFlowKg.setAccount_flow_id(idGenerater.nextId());
            accountFlowKg.setRelation_flow_id(request.getFlowId());
            accountFlowKg.setUser_id(Constants.PLATFORM_USER_ID);
            accountFlowKg.setAmount(Constants.PUBLISG_MASTER_KG.negate());
            accountFlowKg.setAccount_amount(Constants.PUBLISG_MASTER_KG.negate());
            accountFlowKg.setFlow_detail("发放userId:" + request.getUserId() + "徒弟进贡");
            accountFlowKg.setBusiness_type_id(BusinessTypeEnum.SUBCONTRI.getStatus());
            accountFlowKg.setArticle_id(request.getArticleId());
            accountFlowKg.setFlow_date(new Date().getTime());
            accountFlowKg.setAccount_date(new Date().getTime());
            accountFlowKg.setFlow_status(FlowStatusEnum.NOSTATUS.getStatus());
            logger.info("平台氪金奖励流水入参 {}", JsonUtil.writeValueAsString(accountFlowKg));
            MongoUtils.insertOne(MongoTables.ACCOUNT_FLOW_KG, new Document(Bean2MapUtil.bean2map(accountFlowKg)));

            inModel.setUserId(Constants.PLATFORM_USER_ID);
            success = accountWMapper.reduceTxbBalance(inModel) > 0;
        } catch (Exception e) {
            logger.info("出现异常:" + e.getMessage());
            return false;
        } finally {
            logger.info("解锁了");
            lock.unLock();
        }
        return success;
    }

    @Override
    public void pushPublishTvBonus(Long articleId) {
        // 验证当前文章审核业务是否重复
        BasicDBObject basicDBObject = new BasicDBObject("articleId", articleId).append("serviceType", 3);
        logger.info(">>>>>>>>>" + basicDBObject.toString());
        long count = MongoUtils.count(ConsumeMessge.mongoTable, basicDBObject);
        if (count == 0) {
            logger.info("---判断业务重复-----");
            // 审核通过发放发文奖励
            logger.info("-----------审核通过发放用户发文奖励mq通知----------");
            KgArticleStatisticsInModel statinModel = new KgArticleStatisticsInModel();
            statinModel.setArticleId(articleId);
            statinModel.setTypes(3);
            mqProduct.sendMessage(awardMqConfig.getTopic(), awardMqConfig.getTags(), String.valueOf(articleId),
                    JSONObject.toJSONString(statinModel));
        }
    }

    @Override
    public PushArticleResponse getPushAticleInfo() {
        KgCommonSettingInModel inModel = new KgCommonSettingInModel();
        inModel.setSettingKey(PushArticleResponse.SETTING_KEY);
        KgCommonSettingOutModel outModel = kgCommonSettingRMapper.selectBySettingKey(inModel);
        PushArticleResponse pushArticleResponse = JSON.parseObject(outModel.getSettingValue(),
                PushArticleResponse.class);
        // 取出当日已推送的文章数量
        Integer number = jedisUtils.get(JedisKey.getPushArticleLimit(), Integer.class);
        if (number == null) {
            number = 0;
        }
        pushArticleResponse.setPushArticleNumber(number);
        return pushArticleResponse;
    }

    @Transactional
    @Override
    public JsonEntity batchReviewArticle(ArticleBatchReviewRequest request) throws Exception {

        if (request == null || StringUtils.isEmpty(request.getArticleIds())) {
            return JsonEntity.makeExceptionJsonEntity("", "批量审核失败,没有传入待审核资讯IDs");
        }
        int publicStatus = request.getPublicStatus(); //1审核通过，3审核不通过
        if (publicStatus == ArticleAuditStatusEnum.REFUSE.getStatus()) {
            AuditArticleNoPass(request);
            return JsonEntity.makeSuccessJsonEntity();
        }
        AuditArticlePass(request);
        return JsonEntity.makeSuccessJsonEntity();
    }

    /**
     * 审核不通过
     *
     * @param request
     * @throws Exception
     */
    private void AuditArticleNoPass(ArticleBatchReviewRequest request) throws Exception {
        String[] articleIds = request.getArticleIds().split(",");
        if (articleIds.length <= 0) {
            throw new ApiException("批量审核失败,没有传入待审核资讯IDs");
        }
        for (String articleId : articleIds) {
            Article article = buildArticleModel(articleId, request.getAuditUser(), request.getPublicStatus(), request.getColumnId(), request.getRefuseReason(), request.getIfIntoIndex(), request.getSecondColumn());
            int count = articleWMapper.updateByPrimaryKeySelective(article);
            if (count <= 0) {
                throw new ApiException("批量审核失败,更新资讯信息失败：" + JSONObject.toJSONString(article));
            }
            List<AccountFlowOutModel> accountFlowOutModelList = getAccountFlowOutModelList(Long.valueOf(articleId));
            if (CollectionUtils.isNotEmpty(accountFlowOutModelList)) {
                BigDecimal bonusTotal = BigDecimal.ZERO;
                ArticleOutModel outModel = articleRMapper.getArticleCreateuser(getArticleInModel(Long.valueOf(article.getArticleId())));
                if (outModel == null) {
                    throw new ApiException("批量审核失败,资讯不存在：" + JSONObject.toJSONString(article));
                }
                bonusTotal = updateAccount(Long.valueOf(articleId), outModel.getCreateUser(), bonusTotal);
                article.setCreateUser(String.valueOf(outModel.getCreateUser()));
                // 生成一条交易记录
                buildAccountFlow(bonusTotal, article);
            }
            sendArticleSyncMessage(article);
        }
    }

    private BigDecimal updateAccount(Long articleId, Long createUser, BigDecimal bonusTotal) {
        KgArticleStatisticsInModel kgArticleStatisticsInModel = new KgArticleStatisticsInModel();
        kgArticleStatisticsInModel.setArticleId(articleId);
        KgArticleStatisticsOutModel kgArticleStatisticsOutModel = kgArticleStatisticsRMapper.selectByPrimaryKey(kgArticleStatisticsInModel);
        if (kgArticleStatisticsOutModel != null) {
            bonusTotal = kgArticleStatisticsOutModel.getBonusTotal();
            Account account = accountRMapper.selectByUserId(createUser);
            account.setBalance(account.getBalance().add(bonusTotal));
            account.setFrozenBalance(account.getFrozenBalance().add(kgArticleStatisticsOutModel.getBonusTotal().multiply(new BigDecimal(-1))));
            accountWMapper.updateByPrimaryKeySelective(account);
        }
        return bonusTotal;
    }

    private void buildAccountFlow(BigDecimal bonusTotal, Article article) {
        AccountFlowInModel model = buildAccountFlowInModel(bonusTotal, article);
        UserkgOutModel user = userRMapper.selectByPrimaryKey(Long.parseLong(article.getCreateUser()));
        if (null != user) {
            model.setUserEmail(user.getUserEmail());
            model.setUserPhone(user.getUserMobile());
        }
        model.setBusinessTypeId(350);
        accountFlowWMapper.insertFlowSelective(model);
        KgArticleBonusInModel kgArticleBonusInModel = new KgArticleBonusInModel();
        kgArticleBonusInModel.setArticleId(Long.valueOf(article.getArticleId()));
        kgArticleBonusInModel.setBonusStatus(0);
        kgArticleBonusWMapper.updateBonusStatusByArticleId(kgArticleBonusInModel);
    }

    private AccountFlowInModel buildAccountFlowInModel(BigDecimal bonusTotal, Article article) {
        ArticleOutModel outModel = articleRMapper.getArticleCreateuser(getArticleInModel(Long.valueOf(article.getArticleId())));
        AccountFlowInModel model = new AccountFlowInModel();
        model.setAccountAmount(bonusTotal);
        model.setAccountDate(new Date());
        model.setFlowDate(new Date());
        model.setFlowStatus(8);
        model.setAccountFlowId(generater.nextId());
        model.setAmount(bonusTotal);
        model.setArticleId(Long.valueOf(article.getArticleId()));
        model.setFlowDetail("为文章：《" + outModel.getArticleTitle() + "》设置阅读奖励 未生效");
        model.setUserId(outModel.getCreateUser());
        return model;
    }

    private List<AccountFlowOutModel> getAccountFlowOutModelList(Long articleId) {
        AccountFlowInModel accountFlowInModel = new AccountFlowInModel();
        accountFlowInModel.setBusinessTypeId(350);
        accountFlowInModel.setArticleId(articleId);
        return accountFlowRMapper.selectByBusinessTypeId(accountFlowInModel);
    }

    /**
     * 审核通过
     *
     * @param request
     * @throws Exception
     */
    private void AuditArticlePass(ArticleBatchReviewRequest request) throws ApiException {
        String[] articleIds = request.getArticleIds().split(",");
        if (articleIds.length <= 0) {
            throw new ApiException("批量审核失败,没有传入待审核资讯IDs");
        }
        String auditUser = request.getAuditUser();
        int publishStatus = request.getPublicStatus();
        int columnId = request.getColumnId();
        Integer secondColumn = request.getSecondColumn();
        KgArticleExtendInModel kgArticleExtendInModel = new KgArticleExtendInModel();
        kgArticleExtendInModel.setIfIntoIndex(request.getIfIntoIndex());

        for (String articleId : articleIds) {
            Article article = buildArticleModel(articleId, auditUser, publishStatus, columnId, request.getRefuseReason(), request.getIfIntoIndex(), secondColumn);
            int count = articleWMapper.updateByPrimaryKeySelective(article);
            kgArticleExtendInModel.setArticleId(Long.valueOf(articleId));
            KgArticleExtendOutModel kgArticleExtendOutModel = kgArticleExtendRMapper.selectByPrimaryKey(Long.valueOf(articleId));
            if (kgArticleExtendOutModel == null) {
                kgArticleExtendWMapper.insert(kgArticleExtendInModel);
            }

            if (count <= 0) {
                throw new ApiException("批量审核失败,更新资讯信息失败：" + JSONObject.toJSONString(article));
            }
            ArticleOutModel outModel = articleRMapper.getArticleCreateuser(getArticleInModel(Long.valueOf(articleId)));
            KgArticleStatisticsInModel statisticsInModel = new KgArticleStatisticsInModel();
            statisticsInModel.setCreateUser(outModel.getCreateUser());
            statisticsInModel.setArticleId(Long.valueOf(articleId));
            statisticsInModel.setBrowseNum(0);
            KgArticleStatisticsOutModel statisticsOutModel = kgArticleStatisticsRMapper
                    .selectByPrimaryKey(statisticsInModel);
            int updateCount;
            int addCount;
            if (statisticsOutModel != null) {
                statisticsInModel.setThumbupNum(statisticsOutModel.getThumbupNum());
                statisticsInModel.setCollectNum(statisticsOutModel.getCollectNum());
                updateCount = kgArticleStatisticsWMapper.updateStatistics(statisticsInModel);
                if (updateCount <= 0) {
                    throw new ApiException("批量审核失败,更新资讯统计表失败：" + JSONObject.toJSONString(statisticsInModel));
                }
            } else {
                addCount = kgArticleStatisticsWMapper.insertSelective(statisticsInModel);
                if (addCount <= 0) {
                    throw new ApiException("批量审核失败,添加资讯统计表失败：" + JSONObject.toJSONString(statisticsInModel));
                }
            }
            insertCountArticleRecord(articleId, request.getColumnId(), request.getSecondColumn(), new Date().getTime(), 1);
            sendArticleSyncMessage(article);
            article.setCreateUser(String.valueOf(outModel.getCreateUser()));
        }
        // 发送首次发文奖励（批量审核）
        adminRewardService.setFirstPostRewardForBatch(request.getArticleIds());
    }

    private Article buildArticleModel(String articleId, String auditUser, Integer publishStatus, Integer columnId, String refuseReason, Integer ifIntoIndex, Integer secondColumn) throws ApiException {
        checkParams(articleId, auditUser, publishStatus, columnId);
        Article article = new Article();
        article.setArticleId(articleId);
        article.setAuditUser(Long.valueOf(auditUser));
        article.setAuditDate(new Date());
        article.setPublishStatus(publishStatus);
        article.setIfIntoIndex(ifIntoIndex);
        if (publishStatus == ArticleAuditStatusEnum.PASS.getStatus()) {
            article.setColumnId(columnId);
            if (secondColumn != null) {
                article.setSecondColumn(secondColumn);
            }
        }
        if (publishStatus == ArticleAuditStatusEnum.REFUSE.getStatus()) {
            article.setRefuseReason(refuseReason);
        }
        return article;
    }

    private void checkParams(String articleId, String auditUser, Integer publishStatus, Integer columnId) {
        if (StringUtils.isEmpty(articleId)) {
            throw new ApiException("批量审核失败,传入参数articleId为空！");
        }
        if (StringUtils.isEmpty(auditUser)) {
            throw new ApiException("批量审核失败,传入参数auditUser为空！");
        }
        if (publishStatus != ArticleAuditStatusEnum.PASS.getStatus() && publishStatus != ArticleAuditStatusEnum.REFUSE.getStatus()) {
            throw new ApiException("批量审核失败,传入参数publishStatus异常！publishStatus：" + publishStatus);
        }
        if (publishStatus == ArticleAuditStatusEnum.PASS.getStatus()) {
            if (columnId <= 0) {
                throw new ApiException("批量审核失败,传入参数columnId异常！columnId：" + columnId);
            }
        }
    }

    private ArticleInModel getArticleInModel(Long articleId) {
        ArticleInModel inModel = new ArticleInModel();
        inModel.setArticleId(articleId);
        return inModel;
    }

    @Override
    public List<ArticleQueryResponse> getArticles(ArticleQueryRequest request) {
        return adminArticleRMapper.getAllArticles(request);

    }
}

