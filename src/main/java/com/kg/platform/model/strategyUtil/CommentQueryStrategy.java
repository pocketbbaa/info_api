package com.kg.platform.model.strategyUtil;

import com.kg.platform.common.utils.CommonService;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.dao.read.*;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.admin.UserQueryRequest;
import org.apache.log4j.Logger;

import java.util.List;

public class CommentQueryStrategy extends BaseStrategyCentre {
    private static Logger logger = Logger.getLogger(ArticleQueryStrategy.class);
    public CommentQueryStrategy(UserQueryRequest request) {
        super(request);
    }

    @Override
    public BaseStrategyCentre queryStrategy() {
        String orderStr = request.getOrderByClause();
        if(StringUtils.isEmpty(orderStr)){
            logger.info("【执行策略:{"+strategyType+"}时发生冲突,需要排序参数,但是排序参数为空】");
            return null;
        }
        String sortOrder = request.getOrderByClause();
        String sort = sortOrder.split(" ")[1];
        request.setSort(sort);
        //文章数列表
        super.resultModel = userRMapper.selectOrderCommentByFilter(request);
        //总数
        super.totalNumber = userRMapper.selectFilterCnt(request);
        return this;
    }

    @Override
    public CommentQueryStrategy setUserCommentRMapper(UserCommentRMapper userCommentRMapper) {
        super.userCommentRMapper = userCommentRMapper;
        return this;
    }

    @Override
    public CommentQueryStrategy setUserCollectRMapper(UserCollectRMapper userCollectRMapper) {
        super.userCollectRMapper = userCollectRMapper;
        return this;
    }

    @Override
    public CommentQueryStrategy setUserRMapper(UserRMapper userRMapper) {
        super.userRMapper = userRMapper;
        return this;
    }

    @Override
    public CommentQueryStrategy setStrategyType(String type) {
        super.strategyType = type;
        return this;
    }

    @Override
    public CommentQueryStrategy setArticleRMapper(ArticleRMapper articleRMapper) {
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
    public CommentQueryStrategy setCommonService(CommonService commonService) {
        super.commonService = commonService;
        return this;
    }

    @Override
    public CommentQueryStrategy setUserRelationRMapper(UserRelationRMapper userRelationRMapper) {
        return this;
    }
}
