package com.kg.platform.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.model.request.ArticleRequest;
import com.kg.platform.service.m.MArticleService;

public class MArticleSearchTest extends BaseTest{
	@Autowired
	private MArticleService mArticleService;

	@Test
	public void test1() {
		ArticleRequest request = new ArticleRequest();
		request.setArticleId(430679087800000512L);
		System.err.println(JSONObject.toJSONString(mArticleService.recommendForYou(request)));
	}
}
