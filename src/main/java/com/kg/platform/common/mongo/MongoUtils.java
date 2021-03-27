package com.kg.platform.common.mongo;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.dao.entity.PushMessage;
import com.kg.platform.model.response.PushMessageResponse;
import com.mongodb.*;
import com.mongodb.client.AggregateIterable;
import org.bson.BSON;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

/**
 * mongo操作工具类
 * <p>
 * 方法均操作配置的默认数据库：getDefaultCollection()
 * </p>
 *
 * @author hesimin 2016/11/15
 */
public class MongoUtils {
    /**
     * 获取指定数据库的指定表
     *
     * @param dbName   数据库名称
     * @param collName 表名称
     * @return {@link }
     */
    public static MongoCollection<Document> getCollection(String dbName, String collName) {
        return MongoDao.INSTANCE.getCollection(dbName, collName);
    }

    /**
     * 获取默认数据库表
     *
     * @param collName 表名称
     * @return
     */
    public static MongoCollection<Document> getDefaultCollection(String collName) {
        return MongoDao.INSTANCE.getDefaultCollection(collName);
    }

    /**
     * 通过id查询
     *
     * @param collName 表名
     * @param id       主键 mongodb默认主键名称为‘_id’ 无法修改
     * @return
     */
    public static Document findById(String collName, String id) {
        MongoCollection<Document> coll = getDefaultCollection(collName);
        return MongoDao.INSTANCE.findById(coll, id);
    }

    /**
     * 条件查询
     *
     * @param collName 表名
     * @param filter   条件
     * @return
     */
    public static MongoCursor<Document> findByFilter(String collName, Bson filter) {
        MongoCollection<Document> coll = getDefaultCollection(collName);
        return MongoDao.INSTANCE.findByFilter(coll, filter);
    }


    /**
     * 条件查询排序
     *
     * @param collName 表名
     * @param filter   条件
     * @param sort   排序条件
     * @return
     */
    public static MongoCursor<Document> findByFilter(String collName, Bson filter,Bson sort) {
        MongoCollection<Document> coll = getDefaultCollection(collName);
        return MongoDao.INSTANCE.findByFilter(coll, filter,sort);
    }

    /**
     * 分页查询
     *
     * @param collName 表名
     * @param filter   查询条件
     * @param sort     排序条件 使用 1 和 -1 来指定排序的方式，其中 1 为升序排列，而-1是用于降序排列。
     * @param pageNo   需要跳转页
     * @param pageSize 每页大小
     * @return
     */
    public static MongoCursor<Document> findByPage(String collName, Bson filter, Bson sort, int pageNo, int pageSize) {
        MongoCollection<Document> coll = getDefaultCollection(collName);
        return MongoDao.INSTANCE.findByPage(coll, filter, sort, pageNo, pageSize);
    }

    /**
     * 查询表中记录条数
     *
     * @param collName 表名
     * @return
     */
    public static long count(String collName, Bson filter) {
        MongoCollection<Document> coll = getDefaultCollection(collName);
        return coll.count(filter);
    }

    /**
     * 通过id删除
     *
     * @param collName 表名
     * @param id       mongodb 默认主键 ‘_id’
     * @return 被删除的条数
     */
    public static int deleteById(String collName, String id) {
        MongoCollection<Document> coll = getDefaultCollection(collName);
        return MongoDao.INSTANCE.deleteById(coll, id);
    }

    /**
     * 按条件删除
     *
     * @param collName 表名
     * @param filter   条件
     * @return
     */
    public static int deleteByFilter(String collName, Bson filter) {
        MongoCollection<Document> coll = getDefaultCollection(collName);
        return MongoDao.INSTANCE.deleteByFilter(coll, filter);
    }

    /**
     * 通过Id修改
     *
     * @param collName 表名称
     * @param id       mongodb默认主键 ‘_id’
     * @param newdoc   被修改后的对象
     * @return 返回被修改的条数
     */
    public static int updateById(String collName, String id, Document newdoc) {
        MongoCollection<Document> coll = getDefaultCollection(collName);
        return MongoDao.INSTANCE.updateById(coll, id, newdoc);
    }

    /**
     * 按条件修改
     *
     * @param collName 表名称
     * @param filter   条件
     * @param newdoc   被修改后的对象
     * @return 被修改的条数
     */
    public static int updateByFilter(String collName, Bson filter, Document newdoc) {
        MongoCollection<Document> coll = getDefaultCollection(collName);
        return MongoDao.INSTANCE.updateByFilter(coll, filter, newdoc);
    }

    /**
     * 新增一个
     *
     * @param collName 表名称
     * @param document 需新增对象 若没有设置'_id' 则mongodb自动生成
     */
    public static void insertOne(String collName, Document document) {
        MongoCollection<Document> coll = getDefaultCollection(collName);
        MongoDao.INSTANCE.insertOne(coll, document);
    }

    /**
     * 新增多个 若没有设置'_id' 则mongodb自动生成
     *
     * @param collName  表名
     * @param documents 需新增对象
     */
    public static void insertMany(String collName, List<Document> documents) {
        if (documents.size() == 0) {
            return;
        }
        MongoCollection<Document> coll = getDefaultCollection(collName);
        MongoDao.INSTANCE.insertMany(coll, documents);
    }

    /**
     * 聚合查询
     *
     * @param collName
     * @param filters
     * @return
     */
    public static MongoCursor<Document> aggregate(String collName, List<Bson> filters) {
        MongoCollection<Document> coll = getDefaultCollection(collName);
        if (coll == null) {
            return null;
        }
        return MongoDao.INSTANCE.aggregateByFilter(coll, filters);
    }


    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    /*    PushMessage pushMessage = new PushMessage();
        pushMessage.set_id(12321321333333333l);
        pushMessage.setMessageText("ddddddd");
        pushMessage.setCreateDate(new Date().getTime());
        pushMessage.setReadState(0);
        pushMessage.setTitle("asdfasdfas");
        pushMessage.setReceive("sfgasdffawere");
        pushMessage.setType(1);
        pushMessage.setPushPlace(0);
        pushMessage.setMessageAvatar("");
        Map beanMap = net.sf.cglib.beans.BeanMap.create(pushMessage);
        System.out.println(beanMap);
        insertOne("message3",new Document(beanMap));*/
        Document basicDBObject = new Document("receive", "tokentoken").append("pushPlace", 0);
        Document sort = new Document("createDate", -1);
        MongoCursor<Document> cursor = MongoUtils.findByPage(PushMessage.mongoTable, basicDBObject, sort, 1, 5);
        List<PushMessageResponse> responses = new ArrayList<>();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            PushMessageResponse response = new PushMessageResponse();
            response.setMessage_id(document.getLong("_id").toString());
            response.setMessageText(document.getString("messageText"));
            response.setCreateDate(DateUtils.calculateTime(new Date(document.getLong("createDate"))));
            response.setReadState(document.getInteger("readState"));
            response.setTitle(document.getString("title"));
            response.setMessageAvatar(document.getString("messageAvatar"));
            responses.add(response);
        }
        System.out.println(responses.size());
//        insertOne("message2",new Document(BeanUtils.describe(pushMessage)));
        /*BasicDBObject basicDBObject = new BasicDBObject("type","1");

        updateByFilter("message2",basicDBObject,new Document("type","0"));*/
    }
}
