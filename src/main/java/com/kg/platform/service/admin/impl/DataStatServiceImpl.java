package com.kg.platform.service.admin.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.mongo.Seach;
import com.kg.platform.enumeration.UserLogEnum;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.query.QueryUtils;
import org.springframework.stereotype.Service;

import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.dao.read.admin.AdminDataStatRMapper;
import com.kg.platform.model.request.admin.DataStatQueryRequest;
import com.kg.platform.model.response.admin.DataStatQueryResponse;
import com.kg.platform.service.admin.DataStatService;

@Service("AdminDataStatService")
public class DataStatServiceImpl implements DataStatService {

    @Autowired
    private AdminDataStatRMapper adminDataStatRMapper;

    @Override
    public List<DataStatQueryResponse> getDataStatChart(DataStatQueryRequest request) {
        List<DataStatQueryResponse> data = new ArrayList<>();
        Date startDate = request.getStartDate();
        request.setStartDate(DateUtils.getDateStart(startDate));
        Date endDate = request.getEndDate();
        request.setEndDate(DateUtils.getDateEnd(endDate));

        List<DataStatQueryResponse> list = adminDataStatRMapper.selectNormalUser(request);
        if (null != list && list.size() > 0) {
            list.forEach(item -> item.setName("普通用户"));
            data.addAll(list);
        }
        // 专栏数据
        if (request.getType() == 2) {
            List<DataStatQueryResponse> list2 = adminDataStatRMapper.selectColumnUser(request);
            if (null != list2 && list2.size() > 0) {
                list2.forEach(item -> item.setName("专栏用户"));
                data.addAll(list2);
            }
        }
        return data;
    }

    @Override
    public List<DataStatQueryResponse> getColumnUserList(DataStatQueryRequest request) {
        List<DataStatQueryResponse> data = adminDataStatRMapper.selectColumnUserList(request);
        if (null != data && data.size() > 0) {
            data.stream().forEach(item -> {
                int value = item.getValue();
                int personal = item.getPersonal().intValue();
                int media = item.getMedia().intValue();
                int articleNum = item.getArticleNum();
                value = personal + media;
                if (value != 0) {
                    item.setAvgNum((double) articleNum / (double) value);
                } else {
                    item.setAvgNum(0d);
                }
            });
        }
        return data;
    }

    @Override
    public List<DataStatQueryResponse> getNormalUserList(DataStatQueryRequest request) {
        List<DataStatQueryResponse> dataStatQueryList = adminDataStatRMapper.selectNormalUserList(request);
        List<DataStatQueryResponse> dataStatQueryResponses = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dataStatQueryList)) {
            for (DataStatQueryResponse response : dataStatQueryList) {
                DataStatQueryResponse dataStatQuery = new DataStatQueryResponse();
                BeanUtils.copyProperties(response, dataStatQuery);

                String time = response.getTime();
                Date start = DateUtils.parseDate(time + " 00:00:00", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                Date end = DateUtils.parseDate(time + " 23:59:59", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                BasicDBObject query = new BasicDBObject();
                query.append("collectDate", BasicDBObjectBuilder.start(Seach.GTE.getOperStr(), start)
                        .add(Seach.LTE.getOperStr(), end).get());
                long countCollect = MongoUtils.count(UserLogEnum.KG_USER_COLLECT.getTable(), query);
                long countShare = MongoUtils.count(UserLogEnum.KG_USER_SHARE.getTable(), query);
                long countBrow = MongoUtils.count(UserLogEnum.KG_USER_BROWSE.getTable(), query);
                dataStatQuery.setBrowseNum((int) countBrow);
                dataStatQuery.setShareNum((int) countShare);
                dataStatQuery.setCollectNum((int) countCollect);
                dataStatQueryResponses.add(dataStatQuery);
            }
        }
        return dataStatQueryResponses;
    }
}
