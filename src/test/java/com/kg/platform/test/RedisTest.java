package com.kg.platform.test;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.controller.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class RedisTest extends BaseTest {


    @Autowired
    private JedisUtils jedisUtils;


    @Test
    public void list() {

        List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);
        list.add(3L);

        System.err.println(JSONObject.toJSONString("list : " + list));

        String key = "123123";
        jedisUtils.set(key, list);

        //验证
        List list2 = jedisUtils.get(key, List.class);

        System.err.println(JSONObject.toJSONString("list2:" + list2));

    }


}
