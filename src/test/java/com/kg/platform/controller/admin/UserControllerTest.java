package com.kg.platform.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.controller.BaseTest;
import com.kg.platform.dao.entity.UserActive;
import com.kg.platform.model.TokenModel;
import com.kg.platform.model.request.UserProfileRequest;
import com.kg.platform.model.request.admin.CertificationColumnRequest;
import com.kg.platform.service.TokenManager;
import com.kg.platform.model.request.admin.UserQueryRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

public class UserControllerTest extends BaseTest {
    @Test
    public void getUserAccountInfo() throws Exception {
        UserQueryRequest request = new UserQueryRequest();
        request.setUserMobile("17602883374");
        JsonEntity entity = userController.getUserAccountInfo(request);
        System.err.println(JSONObject.toJSONString(entity));
    }

    @Autowired
    private UserController userController;
    @Inject
    private TokenManager tokenManager;

    @Test
    public void getColumnIdentity() {
        JsonEntity entity = userController.getColumnIdentity();
        System.err.println(JSONObject.toJSONString(entity));
    }

    @Test
    public void getColumnLevelList() {
        JsonEntity entity = userController.getColumnLevelList();
        System.err.println(JSONObject.toJSONString(entity));
    }

    @Test
    public void updateColumnLevel() {
        UserQueryRequest request = new UserQueryRequest();
        request.setUserId("13111111148131");
        request.setColumnLevel(2);
        JsonEntity entity = userController.updateColumnLevel(request);
        System.err.println(JSONObject.toJSONString(entity));
    }

    @Test
    public void certificationColumn() {
        CertificationColumnRequest request = new CertificationColumnRequest();
        request.setUserId("434346240138944512");
        request.setColumnIdentity("千克财经官方专栏");
        JsonEntity jsonEntity = userController.certificationColumn(request);
        System.err.println(JSONObject.toJSONString(jsonEntity));
    }

    @Test
    public void cancelCertification() {
        CertificationColumnRequest request = new CertificationColumnRequest();
        request.setUserId("434346240138944512");
        JsonEntity jsonEntity = userController.cancelCertification(request);
        System.err.println(JSONObject.toJSONString(jsonEntity));
    }

    @Test
    public void createUserToken() {
        TokenModel model = tokenManager.createToken(454636110174400512L);
        System.err.println(JSONObject.toJSONString(model));
    }

    @Test
    public void setUserOrder() {
        UserQueryRequest request = new UserQueryRequest();
        request.setUserId("454636110174400512");
        request.setUserOrder(50);
        JsonEntity jsonEntity = userController.setUserOrder(request);
        System.out.println(JSONObject.toJSONString(jsonEntity));
    }

    @Test
    public void setHotUser() {
        UserQueryRequest request = new UserQueryRequest();
        request.setUserId("466182197989257216");
        request.setHotUser(false);
        request.setRankingList(1);
        userController.setHotUser(request);
    }

    @Test
    public void getChannel() {
        JsonEntity jsonEntity = userController.getChannel();
        System.err.println(JSONObject.toJSONString(jsonEntity));
    }

    public static void main(String[] args) {
        Long startTime = System.currentTimeMillis();
        for (Integer i = 0; i < 500000; i++) {
            System.out.println("111");
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - startTime));
    }

    private static void fetchFiled(Object obj) throws Exception {
        //获取所有属性值
        Field[] field = obj.getClass().getDeclaredFields();
        for (int j = 0; j < field.length; j++) {
            // 获取属性的名字
            String name = field[j].getName();
            // 将属性的首字符大写，方便构造get，set方法
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            // 获取属性的类型
            String type = field[j].getGenericType().toString();
            // 如果type是类类型，则前面包含"class "，后面跟类名
            System.out.println("属性为：" + name);
            Method m = obj.getClass().getMethod("get" + name);
            // 调用getter方法获取属性值
            Long value = (Long) m.invoke(obj);
            System.out.println("name:" + value);
        }
    }

    @Test
    public void testGetOptimizeUserList() throws Exception {
        UserQueryRequest request = new UserQueryRequest();
        request.setOrderByClause("audit_date asc");
//        request.setPlatform(1);
//        request.setMaster("大");
//        request.setLockStatus(1);
//        request.setCreateDateStart(DateUtils.getAfterDayTime(new Date(),-10));
//        request.setCreateDateEnd(DateUtils.getAfterDayTime(new Date(),0));
//        request.setUserName("FGNURE");
//        request.setOrderByClause("article_num desc");
//        request.setIsHotUser(1);
//        request.setChannel(6);
        request.setCurrentPage(1);
        request.setPageSize(20);
        request.setColumnLevel(1);
        System.out.println(JSON.toJSONString(userController.getOptimizeUserList(request)));
    }

    @Test
    public void testGetUserInfo() {
        UserQueryRequest request = new UserQueryRequest();
        request.setUserId("13111111148131");
        JsonEntity jsonEntity = userController.getUserInfo(request);
        System.err.println(JSONObject.toJSONString(jsonEntity));
    }

}
