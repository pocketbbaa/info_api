package com.kg.platform.service.impl;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.kg.platform.dao.write.KgArticleStatisticsWMapper;
import com.kg.platform.model.in.KgArticleStatisticsInModel;
import com.kg.platform.model.request.KgArticleStatisticsRequest;
import com.kg.platform.service.KgArticleStatisticsService;

@Service
public class KgArticleStatisticsImpl implements KgArticleStatisticsService {
    private static final Logger logger = LoggerFactory.getLogger(KgArticleStatisticsImpl.class);

    @Inject
    KgArticleStatisticsWMapper articleStatisticsWMapper;

    /**
     * 增加文章浏览数，转发数，点赞数，收藏数，奖励已领取人数，奖励已领取金额
     */
    @Override
    public boolean updateByPrimaryKeySelective(KgArticleStatisticsRequest request) {
        logger.info("增加文章统计数{}", JSON.toJSONString(request));
        KgArticleStatisticsInModel inModel = new KgArticleStatisticsInModel();
        inModel.setBonusNum(request.getBonusNum());// 奖励领取人数
        inModel.setBrowseNum(request.getBrowseNum());// 文章浏览数
        inModel.setShareNum(request.getShareNum());// 转发数
        inModel.setThumbupNum(request.getThumbupNum());// 点赞数
        inModel.setCollectNum(request.getCollectNum());// 收藏数
        inModel.setArticleId(request.getArticleId());
        return articleStatisticsWMapper.updateByPrimaryKeySelective(inModel) > 0;
    }

}
