package com.kg.platform.model.response;

/**
 * Created by Administrator on 2018/11/2.
 */
public class SuspendIconResponse {

	public static final String SETTING_KEY="suspend_icon";

	private String suspendImg;//悬浮图标图片地址

	private String suspendUrl;//悬浮图标点击跳转链接

	private Integer type;//0：h5活动，1：新人红包活动 2：文章 3：广告 4：专栏 5：积分商城

	public String getSuspendImg() {
		return suspendImg;
	}

	public void setSuspendImg(String suspendImg) {
		this.suspendImg = suspendImg;
	}

	public String getSuspendUrl() {
		return suspendUrl;
	}

	public void setSuspendUrl(String suspendUrl) {
		this.suspendUrl = suspendUrl;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
