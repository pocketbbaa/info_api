package com.kg.platform.common.utils;

import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.mongo.MongoTables;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.model.mongoTable.UserLoginLog;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class UserLogUtil {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IDGen idGenerater;

    /**
     * 记录登录日志
     *
     * @param userId
     * @param platfrom
     * @param userPhone
     */
    public void recordLoginLog(Long userId, int platfrom, String userPhone) {
        UserLoginLog loginLog = null;
        try {
            loginLog = new UserLoginLog();
            loginLog.setUserId(userId);
            loginLog.setPlatfrom(platfrom);
            loginLog.setUserPhone(userPhone);
            loginLog.setCreateTime(DateUtils.formatDate(new Date(), DateUtils.DATE_FORMAT_YYYYMMDDHHMMSS));
            MongoUtils.insertOne(MongoTables.KG_USERLOGIN_LOG_TABLE, new Document(Bean2MapUtil.bean2map(loginLog)));
            log.info("【记录登录日志】成功：{}", loginLog);
        } catch (Exception e) {
            log.error("【记录登录日志】失败：{},入参：{}", e.getMessage(), loginLog);
        }
    }

}
