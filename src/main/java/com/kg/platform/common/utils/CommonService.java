package com.kg.platform.common.utils;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.mongo.MongoDao;
import com.kg.platform.common.mongo.MongoTables;
import com.kg.platform.dao.entity.RoleLevel;
import com.kg.platform.dao.entity.SysUser;
import com.kg.platform.dao.entity.User;
import com.kg.platform.dao.read.RoleLevelRMapper;
import com.kg.platform.dao.read.SysUserRMapper;
import com.kg.platform.dao.read.UserRMapper;
import com.kg.platform.enumeration.OperTypeEnum;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.admin.LoginRequest;

import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.admin.impl.AccountServiceImpl;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class CommonService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Inject
    SysUserRMapper sysUserRMapper;

    @Inject
    RoleLevelRMapper roleLevelRMapper;

    public static HashMap<Integer,String> userLevelMap = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(CommonService.class);
    public static final String _FORMAT_DATA = "yyyy-MM-dd HH:mm:ss";

    public ExceptionEnum invalidUserToken(UserkgResponse kguser){
        if (kguser == null) {
            log.error("invalid user token fail:【UserkgResponse is null】");
            return ExceptionEnum.TOKENRERROR;
        }
        if (org.springframework.util.StringUtils.isEmpty(kguser.getUserId())) {
            log.error("invalid user token fail:【userId is null】");
            return ExceptionEnum.TOKENRERROR;
        }

        return ExceptionEnum.SUCCESS;
    }

    public static String formatData(Date date, String format){
        if(!StringUtils.hasText(format)){
            format = _FORMAT_DATA;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }


    public List getPagingList(List listRows, Integer page, Integer pageSize){
        Integer conpageSize=pageSize==null?listRows.size():pageSize;
        if(listRows==null||listRows.size()==0){
            return new ArrayList();
        }
        if(page==null){
            page=1;
        }
        if(pageSize==null){
            pageSize=listRows.size();
            page=1;
        }
        if(listRows.size() < page*pageSize){
            pageSize=listRows.size();
        }else{
            pageSize=page*pageSize;
        }
        return listRows.subList((page-1)*conpageSize,pageSize);
    }

    public SysUser getTokenModelByUserId(Long userId){
        if(userId == null){
            return null;
        }

        SysUser sysUser = sysUserRMapper.selectByPrimaryKey(userId);
        if(sysUser == null){
            log.error("fetch user model is empty");
            return null;
        }
        return sysUser;
    }

    public Boolean strIsNull(String str){
        return str==null||str.equals("")||str.equals("null")||str.equals("undefined");
    }

    public Set<Long> parserAllUserId(List<?> list, String filedName) throws Exception {
        Set<Long> result = new HashSet<>();
        if(list == null || list.size()==0){
            return result;
        }
        Field[] fields;
        for (Object obj:list){
            fields = obj.getClass().getDeclaredFields();
            Object value = getFieldName(fields,filedName,obj);
            if(value == null){
                continue;
            }
            result.add(Long.parseLong(value+""));
        }
        return result;
    }

    public HashMap<Long, Object> transferFormat(List<?> list, String filedName) throws Exception {
        HashMap<Long, Object> result = new HashMap<>();
        if (list == null || list.isEmpty()) {
            return result;
        }
        Long userId;
        for (Object obj : list) {
            Field[] field = obj.getClass().getDeclaredFields();
            Object name = getFieldName(field, filedName, obj);
            if (name == null) {
                continue;
            }
            userId = Long.parseLong(name + "");
            result.put(userId, obj);
        }
        return result;
    }

    public Object getFieldName(Field[] field, String filedName, Object obj) throws Exception {
        for (Field aField : field) {
            String name = aField.getName();
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            if (!name.equals(filedName)) {
                continue;
            }
            Method m = obj.getClass().getMethod("get" + name);
            return m.invoke(obj);
        }
        return null;
    }

    public List<Long> getPageSet(Integer page,Integer pageSize,List<Long> list) {
        if (list.size() == 0) {
            return new ArrayList<>();
        }
        Integer setMaxSize = list.size();
        Integer endIndex = (page + 1) * pageSize;
        //如果超出最大页,默认返回第一页数据
        if (endIndex > setMaxSize) {
            page = 0;
            pageSize = setMaxSize;
        }
        return list.subList(page * pageSize, (page + 1) * pageSize);
    }

    public void doSortListByField(List compeList, final String fieldName, final String order) {
        compeList.sort((o1, o2) -> {
            //反射获取排序字段的值
            Field[] fields1 = o1.getClass().getDeclaredFields();
            Field[] fields2 = o2.getClass().getDeclaredFields();
            Object value1 ="0",value2 ="0";
            try {
                value1 = getFieldName(fields1,fieldName,o1);
                value2 = getFieldName(fields2,fieldName,o2);
            } catch (Exception e) {
            }
            Integer d1 = Integer.parseInt(value1+"");
            Integer d2 = Integer.parseInt(value2+"");
            if (order.equals("asc")) {
                return d1.compareTo(d2);
            } else {
                return d2.compareTo(d1);
            }
        });
    }

    public void doSortDBObjectListByField(List<DBObject> compeList, final String fieldName, final String order) {
        Collections.sort(compeList, new Comparator<DBObject>() {
            @Override
            public int compare(DBObject o1, DBObject o2) {
                Double d1 = Double.parseDouble(o1.get(fieldName) + "");
                Double d2 = Double.parseDouble(o2.get(fieldName) + "");
                if (order.equals("asc")) {
                    return d1.compareTo(d2);

                } else {
                    return d2.compareTo(d1);

                }
            }
        });
    }

    public List getIndexPageList(List rowsList,Integer page,Integer pageSize){

        if(pageSize > rowsList.size()){
            pageSize = rowsList.size();
            page=0;
        }
        return rowsList.subList(page,pageSize);
    }

    public static Long getSecondTimestampTwo(Date date){
        if (null == date) {
            return 0L;
        }
        String timestamp = String.valueOf(date.getTime()/1000);
        return Long.valueOf(timestamp);
    }

    public static BigDecimal setScaleBig(BigDecimal big,int retain,int pattern){
        if(big == null){
            return new BigDecimal(-1);
        }
        return big.setScale(retain,pattern);
    }

    public Boolean judgeRitRollout(Long userId){
        if(userId == null){
            return false;
        }
        DBCollection dbCollection;
        try {
            dbCollection = MongoDao.INSTANCE.getMongoDB().getCollection(MongoTables.ACCOUNT_WITHDRAW_FLOW_RIT);
        }catch (Exception e){
            logger.error("【mongo连接获取失败,请检查mongo数据库运行状态......】");
            return false;
        }
        if(dbCollection == null){
            return false;
        }
        BasicDBObject dbObject = getFilterByUserId(userId);
        List<DBObject> ritRolloutData = dbCollection.find(dbObject).toArray();
        return ritRolloutData != null && !ritRolloutData.isEmpty();
    }

    private BasicDBObject getFilterByUserId(Long userId) {
        BasicDBObject result = new BasicDBObject()
                .append("userId",userId);

        BasicDBList orList = new BasicDBList();
        orList.add(new BasicDBObject("status",OperTypeEnum.WITHDRAW_PENDING.getStatus()));
        orList.add(new BasicDBObject("status",OperTypeEnum.WITHDRAW_WATING.getStatus()));

        result.put("$or",orList);

        return result;
    }
}
