package com.kg.platform.service.app.impl;

import com.kg.platform.common.duiba.CreditTool;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.dao.entity.Account;
import com.kg.platform.dao.read.AccountRMapper;
import com.kg.platform.model.request.DuiBaRequest;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.app.AppDuiBaService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Administrator on 2018/10/13.
 */
@Service
public class AppDuiBaServiceImpl implements AppDuiBaService {

	@Autowired
	private CreditTool creditTool;

	@Autowired
	private AccountRMapper accountRMapper;
	@Override
	public AppJsonEntity generatorDuiBaUrl(UserkgResponse kguser, DuiBaRequest request) {
		//获取用户KG积分
		Account account = accountRMapper.selectByUserId(Long.valueOf(kguser.getUserId()));
		String credits = "0";
		if(account!=null){
			credits = String.valueOf(account.getTxbBalance().longValue());
		}
		Map<String,String> params = new HashedMap();
		params.put("uid", kguser.getUserId());
		params.put("credits", credits);
		params.put("vip",kguser.getUserRole().toString());
		if(request!=null&&StringUtils.isNotBlank(request.getRedirect())){
			params.put("redirect", request.getRedirect());
		}
		String url =  creditTool.buildUrlWithSign(creditTool.getDuiBaUrl(),params);
		params.clear();
		params.put("duiBaUrl", url);
		return AppJsonEntity.makeSuccessJsonEntity(params);
	}
}
