package com.kg.platform.test;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.mongo.MongoTables;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.mongo.Seach;
import com.kg.platform.common.utils.Bean2MapUtil;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.controller.BaseTest;
import com.kg.platform.model.in.admin.RitExchangeInModel;
import com.kg.platform.model.request.admin.RitExchangeQueryRequest;
import com.kg.platform.model.response.admin.RitExchangeQueryResponse;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Decimal128;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.regex.Pattern;

public class MongoTest {


    public static void main(String[] args) {
//        /**
//         * 插入测试
//         */
//        insert();
//
//        /**
//         * 删除测试
//         */
//        del();


        /**
         * 查询测试
         */
//        queryFilterPage();

        /**
         * 获取分页列表统计
         */
        getRitExchangeAmount();

    }

    public static void getRitExchangeAmount() {
        RitExchangeQueryRequest request = new RitExchangeQueryRequest();
        request.setUserRole(2);
        JsonEntity json = getRitExchangeAmount(request);
        System.err.println(JSONObject.toJSONString(json));
    }


    public static void insert() {
        RitExchangeInModel response = new RitExchangeInModel();
        response.setId(5L);
        response.setUserId(5L);
        response.setUserName("liq12213sdasd");
        response.setUserPhone("13882364635");
        response.setUserRole(2);
        response.setRitAmount(new BigDecimal("1000.22103098", MathContext.DECIMAL128));
        response.setKgAmount(new BigDecimal("15000.22133098", MathContext.DECIMAL128));
        response.setCreateTime(new Date().getTime());
        MongoUtils.insertOne(MongoTables.KG_RIT_EXCHANGE_FLOW, new Document(Bean2MapUtil.bean2map(response)));
    }

    public static void del() {
        MongoUtils.deleteById(MongoTables.KG_RIT_EXCHANGE_FLOW, "5b921855d02820bf2460c7f4");
    }

    public static void queryFilterPage() {
        RitExchangeQueryRequest request = new RitExchangeQueryRequest();
        request.setCurrentPage(1);
        request.setPageSize(10);
//        request.setUserName("li");
//        request.setUserPhone("13880264645");
//        request.setKgAmountmMin("150.221");
//        request.setKgAmountMax("160");

        JsonEntity json = getRitExchangeList(request);

        System.err.println(JSONObject.toJSONString(json));
    }


    public static JsonEntity getRitExchangeAmount(RitExchangeQueryRequest request) {
        MongoCursor<Document> cursor = MongoUtils.aggregate(MongoTables.KG_RIT_EXCHANGE_FLOW, getDBObjectGroup(request));
        Map<String, String> map = new HashMap<>();
        if (cursor != null) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                Decimal128 totalRitAmount = (Decimal128) document.get("totalRitAmount");
                Decimal128 totalKgAmount = (Decimal128) document.get("totalKgAmount");
                String ritTotal = totalRitAmount.toString();
                String kgTotal = totalKgAmount.toString();
                map.put("totalRitAmount", ritTotal);
                map.put("totalKgAmount", kgTotal);
            }
        }
        return JsonEntity.makeSuccessJsonEntity(map);
    }

    public static JsonEntity getRitExchangeList(RitExchangeQueryRequest request) {
        String collName = MongoTables.KG_RIT_EXCHANGE_FLOW;
        BasicDBObject basicDBObject = buildQueryParamsForgetRitExchangeList(request);

        BasicDBObject sort = new BasicDBObject("createTime", -1);
        int pageNo = (request.getCurrentPage() - 1) * request.getPageSize();
        int pageSize = request.getPageSize();

        MongoCursor<Document> cursor = MongoUtils.findByPage(collName, basicDBObject, sort, pageNo, pageSize);


        List<RitExchangeQueryResponse> responsesList = new ArrayList<>();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            RitExchangeQueryResponse response = getRitExchangeQueryResponse(document);
            responsesList.add(response);
        }
        long count = MongoUtils.count(collName, basicDBObject);
        return JsonEntity.makeSuccessJsonEntity(buildPageModelForGetRitExchangeList(request, responsesList, count));
    }

    private static RitExchangeQueryResponse getRitExchangeQueryResponse(Document document) {
        RitExchangeQueryResponse response = new RitExchangeQueryResponse();
        response.setId(document.getString("id"));
        response.setUserId(document.getString("userId"));
        response.setUserName(document.getString("userName"));
        response.setUserPhone(document.getString("userPhone"));
        response.setUserRole(document.getInteger("userRole"));
        response.setRitAmount(document.get("ritAmount").toString());
        response.setKgAmount(document.get("kgAmount").toString());
        response.setCreateTime(String.valueOf(document.getLong("createTime")));
        return response;
    }

    private static PageModel<RitExchangeQueryResponse> buildPageModelForGetRitExchangeList(RitExchangeQueryRequest request, List<RitExchangeQueryResponse> responsesList, long count) {
        PageModel<RitExchangeQueryResponse> pageModel = new PageModel<>();
        pageModel.setCurrentPage(request.getCurrentPage());
        pageModel.setPageSize(request.getPageSize());
        pageModel.setData(responsesList);
        pageModel.setTotalNumber(count);
        return pageModel;
    }

    private Map<String, Object> buildResponse(RitExchangeQueryRequest request) {
        MongoCursor<Document> cursor = MongoUtils.aggregate(MongoTables.KG_RIT_EXCHANGE_FLOW, getDBObjectGroup(request));
        if (cursor != null) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                Decimal128 totalDec = (Decimal128) document.get("totalRitAmount");
                String total = totalDec.toString();
            }
        }

        return null;
    }

    /**
     * 构建RIT查询参数
     *
     * @param request
     * @return
     */
    private static BasicDBObject buildQueryParamsForgetRitExchangeList(RitExchangeQueryRequest request) {
        BasicDBObject basicDBObject = new BasicDBObject();
        String userName = request.getUserName();
        if (StringUtils.isNotEmpty(userName)) {
            Pattern pattern = Pattern.compile("^.*" + userName + ".*$", Pattern.CASE_INSENSITIVE);
            basicDBObject.put("userName", pattern);
        }
        String userphone = request.getUserPhone();
        if (StringUtils.isNotEmpty(userphone)) {
            basicDBObject.put("userPhone", userphone);
        }
        Integer userRole = request.getUserRole();
        if (userRole != null) {
            basicDBObject.put("userRole", userRole);
        }
        String ritAmountMin = request.getRitAmountmMin();
        if (StringUtils.isNotEmpty(ritAmountMin)) {
            BigDecimal ritMin = new BigDecimal(ritAmountMin);
            basicDBObject.put("ritAmount", new BasicDBObject(Seach.GTE.getOperStr(), ritMin));
        }
        String ritAmountMax = request.getRitAmountMax();
        if (StringUtils.isNotEmpty(ritAmountMax)) {
            BigDecimal ritMax = new BigDecimal(ritAmountMax);
            basicDBObject.put("ritAmount", new BasicDBObject(Seach.LTE.getOperStr(), ritMax));
        }
        String kgAmountMin = request.getKgAmountmMin();
        if (StringUtils.isNotEmpty(kgAmountMin)) {
            BigDecimal kgMin = new BigDecimal(kgAmountMin);
            basicDBObject.put("kgAmount", new BasicDBObject(Seach.GTE.getOperStr(), kgMin));
        }
        String kgAmountMax = request.getKgAmountMax();
        if (StringUtils.isNotEmpty(kgAmountMax)) {
            BigDecimal kgMax = new BigDecimal(kgAmountMax);
            basicDBObject.put("kgAmount", new BasicDBObject(Seach.LTE.getOperStr(), kgMax));
        }
        String startTime = request.getExchangeStartTime();
        String endTime = request.getExchangeEndTime();
        if (StringUtils.isNotEmpty(startTime)) {
            Date start = DateUtils.parseDate(startTime, DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
            if (start != null) {
                basicDBObject.put("createTime", new BasicDBObject(Seach.GTE.getOperStr(), start.getTime()));
            }
        }
        if (StringUtils.isNotEmpty(endTime)) {
            Date end = DateUtils.parseDate(endTime, DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
            if (end != null) {
                basicDBObject.put("createTime", new BasicDBObject(Seach.GTE.getOperStr(), end.getTime()));
            }
        }
        return basicDBObject;
    }

    private static List<Bson> getDBObjectGroup(RitExchangeQueryRequest request) {
        List<Bson> ops = new ArrayList<>();
        DBObject query = buildQueryParamsForgetRitExchangeList(request);
        Bson match = new BasicDBObject("$match", query);
        ops.add(match);
        Bson group = new BasicDBObject("$group",
                new BasicDBObject("_id", null)
                        .append("totalRitAmount", new BasicDBObject("$sum", "$ritAmount"))
                        .append("totalKgAmount", new BasicDBObject("$sum", "$kgAmount")));
        ops.add(group);
        return ops;
    }

}
