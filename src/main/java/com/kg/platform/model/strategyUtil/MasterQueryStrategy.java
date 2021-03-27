package com.kg.platform.model.strategyUtil;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.utils.CommonService;
import com.kg.platform.dao.entity.UserRelation;
import com.kg.platform.dao.read.*;
import com.kg.platform.enumeration.StrategyEnum;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.admin.UserQueryRequest;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 策略二：师傅字段方式查询
 * 描述：通过师傅名称获取满足条件的userId 反向查找relation表获取所有满足条件的徒弟userId
 */
public class MasterQueryStrategy extends BaseStrategyCentre implements ProcessHandler{
    private static Logger logger = Logger.getLogger(NormalQueryStrategy.class);
    public MasterQueryStrategy(UserQueryRequest request) {
        super(request);
    }

    @Override
    public MasterQueryStrategy setUserRMapper(UserRMapper userRMapper) {
        super.userRMapper = userRMapper;
        return this;
    }

    @Override
    public BaseStrategyCentre setUserRelationRMapper(UserRelationRMapper userRelationRMapper) {
        super.userRelationRMapper = userRelationRMapper;
        return this;
    }

    @Override
    public BaseStrategyCentre setStrategyType(String type) {
        super.strategyType = type;
        return this;
    }

    @Override
    public BaseStrategyCentre setArticleRMapper(ArticleRMapper articleRMapper) {
        super.articleRMapper = articleRMapper;
        return this;
    }

    @Override
    public BaseStrategyCentre setCommonService(CommonService commonService) {
        super.commonService = commonService;
        return this;
    }

    @Override
    public BaseStrategyCentre queryStrategy() {
        /**
         * 1 根据师傅字段去user表查出满足条件的师傅id
         * 2 然后根据师傅id列表去relation表查出满足条件的徒弟id列表
         * 3 根据徒弟id列表去user表获取最终数据列表
         * 根据mysql的特性 当子查询数据小时 使用in方式查询 反之则使用exists
         */
        //获取所有师傅userId
        List<Long> filterIds = userRMapper.selectParentFilterQuery(request);
        logger.debug("filterIds:"+JSON.toJSONString(filterIds));
        Set<Long> childUserIdSet = new HashSet<>(filterIds);
        if(childUserIdSet.size() == 0){
            return null;
        }
        //根据师傅userId获取满足条件的徒弟userId列表
        List<UserRelation> childUserIds = userRelationRMapper.selectMoreChildUserRelationInfo(new ArrayList<>(childUserIdSet));
        Set<Long> userIdSet;
        try {
            userIdSet = commonService.parserAllUserId(childUserIds,"RelUserId");
        } catch (Exception e) {
            logger.error("【解析集合{userId}属性失败 e:】"+e.getMessage());
            return null;
        }
        //根据徒弟id列表去user表获取最终数据列表
        //todo  这里进行第一层分页 只获取pageSize个徒弟id以供前端展示
        List<Long> childList = commonService.getPageSet(request.getCurrentPage(),request.getPageSize(),new ArrayList<Long>(userIdSet));
        if(childList.size()==0){
            return this;
        }
        super.totalNumber = userIdSet.size();
        super.resultModel = userRMapper.selectMoreParentInfo(childList);
        return this;
    }

    @Override
    public BaseStrategyCentre setUserCommentRMapper(UserCommentRMapper userCommentRMapper) {
        super.userCommentRMapper = userCommentRMapper;
        return this;
    }

    @Override
    public BaseStrategyCentre setUserCollectRMapper(UserCollectRMapper userCollectRMapper) {
        super.userCollectRMapper = userCollectRMapper;
        return this;
    }

    @Override
    public List<UserkgOutModel> getResultModel() {
        return super.resultModel;
    }

    @Override
    public Integer getTotalNumber() {
        return super.totalNumber;
    }

    @Override
    public BaseStrategyCentre changeStrategyProcess(StrategyEnum strategyEnum) {
        return null;
    }
}
