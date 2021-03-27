package com.kg.platform.model.strategyUtil;

import com.kg.platform.common.utils.CommonService;
import com.kg.platform.dao.read.*;
import com.kg.platform.enumeration.StrategyEnum;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.admin.UserQueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 策略一：普通查询
 * 描述：查询字段根据kg_user中的字段查询
 */
public class NormalQueryStrategy extends BaseStrategyCentre implements ProcessHandler{
    private static Logger logger = LoggerFactory.getLogger(NormalQueryStrategy.class);

    public NormalQueryStrategy(UserQueryRequest request) {
        super(request);
    }

    @Override
    public NormalQueryStrategy queryStrategy() {
        try {
            super.resultModel = userRMapper.getUserListByUserIds(request);
            super.totalNumber = userRMapper.selectFilterCnt(request);
        } catch (Exception e) {
            logger.error("【策略执行失败 type:{"+super.strategyType+"} e:{"+e.getMessage()+"}】");
            super.resultModel = new ArrayList<>();
        }
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
    public NormalQueryStrategy setUserRMapper(UserRMapper userRMapper) {
        super.userRMapper = userRMapper;
        return this;
    }

    @Override
    public NormalQueryStrategy setStrategyType(String type) {
        super.strategyType = type;
        return this;
    }

    @Override
    public BaseStrategyCentre setArticleRMapper(ArticleRMapper articleRMapper) {
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
    public NormalQueryStrategy setCommonService(CommonService commonService) {
        super.commonService = commonService;
        return this;
    }

    @Override
    public NormalQueryStrategy setUserRelationRMapper(UserRelationRMapper userRelationRMapper) {
        return this;
    }

    @Override
    public NormalQueryStrategy changeStrategyProcess(StrategyEnum strategyEnum) {
        return null;
    }
}
