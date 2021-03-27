package com.kg.platform.model.response;

/**
 * Created by Administrator on 2019/2/13.
 */
public class AdhubConfigResponse {

	public static final String SETTINT_KEY = "adhub_config";

	private String adhubIndex;

	private String iosAdhubUnitId;

	private String androidAdhubUnitId;

	private String adhubNums;

	private String adhubInterval;

	private Integer iOSvisiableCloudReward;

	private Integer androidvisiableCloudReward;

	public Integer getiOSvisiableCloudReward() {
		return iOSvisiableCloudReward;
	}

	public void setiOSvisiableCloudReward(Integer iOSvisiableCloudReward) {
		this.iOSvisiableCloudReward = iOSvisiableCloudReward;
	}

	public Integer getAndroidvisiableCloudReward() {
		return androidvisiableCloudReward;
	}

	public void setAndroidvisiableCloudReward(Integer androidvisiableCloudReward) {
		this.androidvisiableCloudReward = androidvisiableCloudReward;
	}

	public String getAdhubNums() {
		return adhubNums;
	}

	public void setAdhubNums(String adhubNums) {
		this.adhubNums = adhubNums;
	}

	public String getAdhubInterval() {
		return adhubInterval;
	}

	public void setAdhubInterval(String adhubInterval) {
		this.adhubInterval = adhubInterval;
	}

	public String getAdhubIndex() {
		return adhubIndex;
	}

	public void setAdhubIndex(String adhubIndex) {
		this.adhubIndex = adhubIndex;
	}

	public String getIosAdhubUnitId() {
		return iosAdhubUnitId;
	}

	public void setIosAdhubUnitId(String iosAdhubUnitId) {
		this.iosAdhubUnitId = iosAdhubUnitId;
	}

	public String getAndroidAdhubUnitId() {
		return androidAdhubUnitId;
	}

	public void setAndroidAdhubUnitId(String androidAdhubUnitId) {
		this.androidAdhubUnitId = androidAdhubUnitId;
	}
}
