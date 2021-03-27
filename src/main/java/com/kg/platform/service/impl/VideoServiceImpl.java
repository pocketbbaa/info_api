package com.kg.platform.service.impl;

import com.kg.platform.common.aop.Cacheable;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.mongo.Seach;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.dao.read.ArticleRMapper;
import com.kg.platform.enumeration.DateEnum;
import com.kg.platform.enumeration.UserLogEnum;
import com.kg.platform.model.in.ArticleInModel;
import com.kg.platform.model.out.ArticleOutModel;
import com.kg.platform.model.response.ArticleResponse;
import com.kg.platform.service.VideoService;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/6/2.
 */
@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    private ArticleRMapper articleRMapper;

    @Cacheable(key = "VideoServiceImpl/hotVideoList", expire = 2, dateType = DateEnum.HOURS)
    @Override
    public JsonEntity hotVideoList() {

        Date start = DateUtils.getBeforeDay(new Date(), 3);
        List<Bson> bsonList = conditionList(start);
        MongoCursor<Document> cursor = MongoUtils.aggregate(UserLogEnum.KG_USER_BROWSE.getTable(), bsonList);
        List<ArticleResponse> responses = new ArrayList<>();
        if (cursor != null) {
            while (cursor.hasNext()) {
                ArticleResponse response = new ArticleResponse();
                Document document = cursor.next();
                Long articleId = document.getLong("_id");
                ArticleInModel inModel = new ArticleInModel();
                inModel.setArticleId(articleId);
                ArticleOutModel outModel = articleRMapper.selectArticleBase(inModel);
                if (outModel == null) {
                    continue;
                }
                response.setArticleImage(outModel.getArticleImage());
                response.setArticleTitle(outModel.getArticleTitle());
                response.setVideoUrl(outModel.getVideoUrl());
                response.setArticleId(outModel.getArticleId());
                response.setArticleImgSize(outModel.getArticleImgSize());
                responses.add(response);
            }
        }
        if (responses.size() > 5) {
            responses = responses.subList(0, 5);
        }
        return JsonEntity.makeSuccessJsonEntity(responses);
    }


    private List<Bson> conditionList(Date start) {
        List<Bson> conditionList = new ArrayList<>();
        BasicDBObject querryBy = new BasicDBObject();
        querryBy.put("collectDate", BasicDBObjectBuilder.start(Seach.GTE.getOperStr(), start).get());
        querryBy.put("publishKind", 2);

        Bson querryCondition = new BasicDBObject("$match", querryBy);
        conditionList.add(querryCondition);

        BasicDBObject groupBy = new BasicDBObject();
        groupBy.append("_id", "$articleId");
        groupBy.append("count", new BasicDBObject("$sum", 1));
        Bson group = new BasicDBObject("$group", groupBy);
        conditionList.add(group);
        conditionList.add(new BasicDBObject("$sort", new BasicDBObject("count", -1)));
        conditionList.add(new BasicDBObject("$limit", 40));
        return conditionList;
    }
}
