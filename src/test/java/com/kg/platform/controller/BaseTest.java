package com.kg.platform.controller;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Base64;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring-context*.xml"})
public class BaseTest {

    public static void main(String[] args) {

//        String str = "eyJjdXJyZW50UGFnZSI6MSwicGFnZVNpemUiOjEwLCJzZWNvbmRDb2x1bW4iOiIzMTEifQ==";
//        String value = new String(Base64.getDecoder().decode(str));
//
        String str1 = "{\"columnId\":-1}";

        String stren = new String(Base64.getEncoder().encode(str1.getBytes()));



        System.out.println(stren);
    }



}
