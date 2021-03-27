package com.kg.platform.common.duiba;

import java.util.HashMap;
import java.util.Map;

public class CreditTool {

	private String appKey;
	private String appSecret;
	private String duiBaUrl;

	public CreditTool() {
	}

	public String getDuiBaUrl() {
		return duiBaUrl;
	}

	public void setDuiBaUrl(String duiBaUrl) {
		this.duiBaUrl = duiBaUrl;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public CreditTool(String appKey,String appSecret){
		this.appKey=appKey;
		this.appSecret=appSecret;
	}
	
	/**
	 * 通用的url生成方法
	 * @param url
	 * @param params
	 * @return
	 */
	public String buildUrlWithSign(String url,Map<String, String> params){
		Map<String, String> newparams=new HashMap<String, String>(params);
		newparams.put("appKey", appKey);
		newparams.put("appSecret", appSecret);
		if(newparams.get("timestamp")==null){
			newparams.put("timestamp", System.currentTimeMillis()+"");
		}
		String sign=SignTool.sign(newparams);
		newparams.put("sign", sign);

		newparams.remove("appSecret");

		return AssembleTool.assembleUrl(url, newparams);
	}
	
	
	/**
	 *  构建在兑吧商城自动登录的url地址
	 * @param uid 用户id
	 * @param dbredirect 免登陆接口回传回来 dbredirect参数
	 * @param credits 用户积分余额
	 * @return 自动登录的url地址
	 */
	public String buildAutoLoginRequest( String uid, Long credits, String dbredirect){
		String url="https://home.m.duiba.com.cn/autoLogin/autologin?";
		Map<String, String> params=new HashMap<String, String>();
		params.put("uid", uid);
		params.put("credits", credits+"");
		if(dbredirect!=null){
			params.put("redirect", dbredirect);
		}
		String autologinUrl= buildUrlWithSign(url,params);
		return autologinUrl;
	}
	
}
