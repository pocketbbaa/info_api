package com.kg.platform.model.out;

/**
 * Created by Administrator on 2018/11/2.
 */
public class SuspendIconOutModel {

	public static final String SETTING_KEY="suspend_icon";

	private String suspendImg;//悬浮图标图片地址

	private String suspendUrl;//悬浮图标点击跳转链接

	private int iosSwitch;//0:关闭 1：打开

	private int androidSwitch;//0:关闭 1：打开

	private Integer type;//1:活动 2：广告 3：文章 4：专栏 5：积分商城

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

	public int getIosSwitch() {
		return iosSwitch;
	}

	public void setIosSwitch(int iosSwitch) {
		this.iosSwitch = iosSwitch;
	}

	public int getAndroidSwitch() {
		return androidSwitch;
	}

	public void setAndroidSwitch(int androidSwitch) {
		this.androidSwitch = androidSwitch;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
