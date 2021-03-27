package com.kg.platform.model.mongoTable;

/**
 * Created by Administrator on 2018/10/19.
 */
public class EconnoisseurRecordMongo {

	private String econnoisseurIp;

	private String econnoisseurMobile;

	private String time;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getEconnoisseurIp() {
		return econnoisseurIp;
	}

	public void setEconnoisseurIp(String econnoisseurIp) {
		this.econnoisseurIp = econnoisseurIp;
	}

	public String getEconnoisseurMobile() {
		return econnoisseurMobile;
	}

	public void setEconnoisseurMobile(String econnoisseurMobile) {
		this.econnoisseurMobile = econnoisseurMobile;
	}
}
