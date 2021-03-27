package com.kg.platform.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.model.request.DuiBaRequest;
import com.kg.platform.model.response.UserkgResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Created by Administrator on 2018/10/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/spring-context*.xml" })
public class AppDuiBaControllerTest {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private AppDuiBaController appDuiBaController;
	@Test
	public void generatorDuiBaUrl() throws Exception {
		UserkgResponse kguser = new UserkgResponse();
		kguser.setUserId("395260122580000768");
		kguser.setUserRole(1);
		DuiBaRequest request = new DuiBaRequest();
		request.setRedirect("dddddd");
		JSONObject jsonObject = appDuiBaController.generatorDuiBaUrl(kguser,request);
		logger.info("测试结果：{}",jsonObject.toJSONString());
	}

}