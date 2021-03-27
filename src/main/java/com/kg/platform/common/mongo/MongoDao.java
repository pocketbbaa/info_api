package com.kg.platform.common.mongo;

import java.util.List;

import com.mongodb.DB;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

/**
 * mongo操作dao
 *
 * @author hesimin 2016/11/15
 * @see MongoUtils
 */
public enum MongoDao {

    /**
     * 定义一个枚举的元素，它代表此类的一个实例
     */
    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(MongoDao.class);

    private MongoClient mongoClient;

    /**
     * 指定默认数据库
     */
    private static String DEFAULT_DBNAME;

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    static {
        logger.info("====================mongoDB 初始化 start==================");

        // 配置参数
        String clientURI = MongoProperty.getUri();
        DEFAULT_DBNAME = MongoProperty.getDefaultDatabaseName();
        Integer perHost = MongoProperty.getConnectionsPerHost();

        Builder builder = new Builder();
        if (perHost != null) {
            builder.connectionsPerHost(perHost);
        }
        // builder.writeConcern(WriteConcern.UNACKNOWLEDGED);//默认是ACKNOWLEDGED

        if (clientURI == null || "".equals(clientURI)) {
            throw new IllegalArgumentException("mongo.uri 不能为空！");
        }

        MongoClientURI mongoClientURI = new MongoClientURI(clientURI, builder);
        INSTANCE.mongoClient = new MongoClient(mongoClientURI);

        logger.info("====================mongoDB 初始化 end==================");
    }

    /**
     *
     * 获取指定数据库的指定表
     *
     * @param dbName
     *            数据库名称
     * @param collName
     *            表名称
     * @return {@link }
     */
    public MongoCollection<Document> getCollection(String dbName, String collName) {
        if (null == collName || "".equals(collName)) {
            return null;
        }
        if (null == dbName || "".equals(dbName)) {
            return null;
        }
        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection(collName);
        return collection;
    }


    /**
     *
     * 获取默认数据库表
     *
     * @param collName
     *            表名称
     * @return
     */
    public MongoCollection<Document> getDefaultCollection(String collName) {
        if (null == collName || "".equals(collName)) {
            return null;
        }
        MongoCollection<Document> collection = mongoClient.getDatabase(DEFAULT_DBNAME).getCollection(collName);
        return collection;
    }

    public DB getMongoDB(){
        return mongoClient.getDB(DEFAULT_DBNAME);
    }

    /**
     *
     * 通过id查询
     *
     * @param coll
     *            表
     * @param id
     *            主键 mongodb默认主键名称为‘_id’ 无法修改
     * @return
     */
    public Document findById(MongoCollection<Document> coll, String id) {
        ObjectId _idobj = null;
        try {
            _idobj = new ObjectId(id);
        } catch (Exception e) {
            return null;
        }
        return coll.find(Filters.eq("_id", _idobj)).first();
    }

    /**
     *
     * 条件查询
     *
     * @param coll
     *            表
     * @param filter
     *            条件
     * @return
     */
    public MongoCursor<Document> findByFilter(MongoCollection<Document> coll, Bson filter) {
        return coll.find(filter).iterator();
    }


    /**
     *
     * 条件查询
     *
     * @param coll
     *            表
     * @param filter
     *            条件
     * @return
     */
    public MongoCursor<Document> findByFilter(MongoCollection<Document> coll, Bson filter,Bson sort) {
        if (sort == null) {
            sort = new BasicDBObject("_id", 1);
        }
        return coll.find(filter).sort(sort).iterator();
    }

    /**
     * 聚合查询
     *
     * @param coll
     * @param filter
     * @return
     */
    public MongoCursor<Document> aggregateByFilter(MongoCollection<Document> coll, List<Bson> filter) {
        return coll.aggregate(filter).iterator();
    }

    /**
     *
     * 分页查询
     *
     * @param coll
     *            表
     * @param filter
     *            查询条件
     * @param sort
     *            排序条件 使用 1 和 -1 来指定排序的方式，其中 1 为升序排列，而-1是用于降序排列。
     * @param pageNo
     *            需要跳转页
     * @param pageSize
     *            每页大小
     * @return
     */
    public MongoCursor<Document> findByPage(MongoCollection<Document> coll, Bson filter, Bson sort, int pageNo,
            int pageSize) {
        if (sort == null) {
            sort = new BasicDBObject("_id", 1);
        }
        return coll.find(filter).sort(sort).skip((pageNo - 1) * pageSize).limit(pageSize).batchSize(pageSize)
                .iterator();
    }

    /**
     *
     * 通过id删除
     *
     * @param coll
     *            表
     * @param id
     *            mongodb 默认主键 ‘_id’
     * @return 被删除的条数
     */
    public int deleteById(MongoCollection<Document> coll, String id) {
        ObjectId _id = null;
        try {
            _id = new ObjectId(id);
        } catch (Exception e) {
            return 0;
        }
        Bson filter = Filters.eq("_id", _id);
        DeleteResult deleteResult = coll.deleteOne(filter);
        return (int) deleteResult.getDeletedCount();
    }

    /**
     *
     * 按条件删除
     *
     * @param coll
     *            表
     * @param filter
     *            条件
     * @return
     */
    public int deleteByFilter(MongoCollection<Document> coll, Bson filter) {
        DeleteResult deleteResult = coll.deleteMany(filter);
        return (int) deleteResult.getDeletedCount();
    }

    /**
     *
     * 通过Id修改
     *
     * @param coll
     *            表名称
     * @param id
     *            mongodb默认主键 ‘_id’
     * @param newdoc
     *            被修改后的对象
     * @return 返回被修改的条数
     */
    public int updateById(MongoCollection<Document> coll, String id, Document newdoc) {
        ObjectId _idobj = null;
        try {
            _idobj = new ObjectId(id);
        } catch (Exception e) {
            return 0;
        }
        Bson filter = Filters.eq("_id", _idobj);
        UpdateResult updateResult = coll.updateOne(filter, new Document(MongoOperation.SET.getOperStr(), newdoc));
        return (int) updateResult.getModifiedCount();
    }

    /**
     *
     * 按条件修改
     *
     * @param coll
     *            表
     * @param filter
     *            条件
     * @param newdoc
     *            被修改后的对象
     * @return 被修改的条数
     */
    public int updateByFilter(MongoCollection<Document> coll, Bson filter, Document newdoc) {
        UpdateResult updateResult = coll.updateMany(filter, new Document(MongoOperation.SET.getOperStr(), newdoc));
        return (int) updateResult.getModifiedCount();
    }

    /**
     *
     * 新增一个
     *
     * @param coll
     *            表
     * @param document
     *            需新增对象 若没有设置'_id' 则mongodb自动生成
     */
    public void insertOne(MongoCollection<Document> coll, Document document) {
        coll.insertOne(document);
    }

    /**
     *
     * 新增多个 若没有设置'_id' 则mongodb自动生成
     *
     * @param coll
     *            表
     * @param documents
     *            需新增对象
     */
    public void insertMany(MongoCollection<Document> coll, List<Document> documents) {
        coll.insertMany(documents);
    }

}
