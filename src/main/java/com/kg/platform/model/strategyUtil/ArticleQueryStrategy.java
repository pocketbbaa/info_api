package com.kg.platform.model.strategyUtil;

import com.kg.platform.common.utils.CommonService;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.dao.read.*;
import com.kg.platform.enumeration.StrategyEnum;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.admin.UserQueryRequest;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 策略三
 * 描述：按照发文数量进行排序
 * 思路：
 *      1 按照基础查询条件查询出所有满足要求的userId
 *      2 查询article表 排序获得发文数排序列表
 *      3 剔除不满足基础查询条件的userId
 *      4 返回结果
 */
public class ArticleQueryStrategy extends BaseStrategyCentre implements ProcessHandler{
    private static Logger logger = Logger.getLogger(ArticleQueryStrategy.class);
    public ArticleQueryStrategy(UserQueryRequest request) {
        super(request);
    }

    @Override
    public BaseStrategyCentre queryStrategy() {
        //获取文章排序的userId列表
        String orderStr = request.getOrderByClause();
        if(StringUtils.isEmpty(orderStr)){
            logger.info("【执行策略:{"+strategyType+"}时发生冲突,需要排序参数,但是排序参数为空】");
            return null;
        }
        String sortOrder = request.getOrderByClause();
        String sort = sortOrder.split(" ")[1];
        request.setSort(sort);
        Long start1 =System.currentTimeMillis();
        //文章数列表
        super.resultModel = userRMapper.selectOrderArticleByFilter(request);
        System.out.println("【获取文章数据耗时:("+(System.currentTimeMillis()-start1)+")】");
        //总数
        Long start2 = System.currentTimeMillis();
        super.totalNumber = userRMapper.selectFilterCnt(request);
        System.out.println("【获取总数:("+(System.currentTimeMillis()-start2)+")】");
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

    private List<UserkgOutModel> getSortFilterUserCntList(){
        String sortOrder = request.getOrderByClause();
        String sortType = sortOrder.split(" ")[0]; //排序字段
        String sort = sortOrder.split(" ")[1];
        request.setSort(sort);
        switch (sortType) {
            case "article_num":                            //发文数
                return userRMapper.selectOrderArticleByFilter(request);
/*            case "comment_num":                            //评论数
                return userCommentRMapper.selectOrderArticleByFilter();
            case "collect_num":                            //收藏数
                return userCollectRMapper.selectOrderArticleByFilter();*/
            default:
                return new ArrayList<>();
        }
    }

    private void addArticleCntAttr(List<UserkgOutModel> allUserIdsList, List<HashMap> articleSortList) {
        HashMap<String,Integer> userArticleMap = getArticleCntMap(articleSortList);
        String userId;
        for (UserkgOutModel userkgOutModel:allUserIdsList){
            userId = userkgOutModel.getUserId();
            if(!userArticleMap.containsKey(userId)){
                continue;
            }
            userkgOutModel.setSortCnt(userArticleMap.get(userId));
        }
    }

    private HashMap<String,Integer> getArticleCntMap(List<HashMap> articleSortList){
        HashMap<String,Integer> result = new HashMap<>();
        Object userIdobj;
        for (HashMap map:articleSortList){
            userIdobj = map.get("user_id");
            if(StringUtils.isEmpty(userIdobj+"")){
                continue;
            }
            try {
                result.put(userIdobj+"",Integer.parseInt(map.get("cnt")+""));
            } catch (NumberFormatException ignored) {
            }
        }
        return result;
    }

    @Override
    public ArticleQueryStrategy setUserRMapper(UserRMapper userRMapper) {
        super.userRMapper = userRMapper;
        return this;
    }

    @Override
    public ArticleQueryStrategy setStrategyType(String type) {
        super.strategyType = type;
        return this;
    }

    @Override
    public BaseStrategyCentre setArticleRMapper(ArticleRMapper articleRMapper) {
        super.articleRMapper = articleRMapper;
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
    public ArticleQueryStrategy setCommonService(CommonService commonService) {
        super.commonService = commonService;
        return this;
    }

    @Override
    public ArticleQueryStrategy setUserRelationRMapper(UserRelationRMapper userRelationRMapper) {
        super.userRelationRMapper = userRelationRMapper;
        return this;
    }

    @Override
    public ArticleQueryStrategy changeStrategyProcess(StrategyEnum strategyEnum) {
        return null;
    }
}
