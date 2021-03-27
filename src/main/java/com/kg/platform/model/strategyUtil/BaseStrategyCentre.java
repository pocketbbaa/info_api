package com.kg.platform.model.strategyUtil;

import com.kg.platform.common.utils.CommonService;
import com.kg.platform.dao.read.*;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.admin.UserQueryRequest;

import java.util.List;

public abstract class BaseStrategyCentre {
    /**
     * 子类需要用到的mapper集合
     */
    protected UserRMapper userRMapper;
    protected UserRelationRMapper userRelationRMapper;
    protected ArticleRMapper articleRMapper;
    protected UserCommentRMapper userCommentRMapper;
    protected UserCollectRMapper userCollectRMapper;

    protected CommonService commonService;

    protected List<UserkgOutModel> resultModel;
    protected Integer totalNumber;
    protected String strategyType;
    protected UserQueryRequest request;


    public BaseStrategyCentre(UserQueryRequest request){
        this.request = request;
    }

    public abstract BaseStrategyCentre setUserRMapper(UserRMapper userRMapper);

    public abstract BaseStrategyCentre setCommonService(CommonService commonService);

    public abstract BaseStrategyCentre setUserRelationRMapper(UserRelationRMapper userRelationRMapper);

    public abstract BaseStrategyCentre setStrategyType(String type);

    public abstract BaseStrategyCentre setArticleRMapper(ArticleRMapper articleRMapper);

    public abstract List<UserkgOutModel> getResultModel();

    public abstract Integer getTotalNumber();

    public abstract BaseStrategyCentre queryStrategy();

    public abstract BaseStrategyCentre setUserCommentRMapper(UserCommentRMapper userCommentRMapper);

    public abstract BaseStrategyCentre setUserCollectRMapper(UserCollectRMapper userCollectRMapper);
}
