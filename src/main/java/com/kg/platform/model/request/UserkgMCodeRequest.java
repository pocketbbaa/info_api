package com.kg.platform.model.request;


import java.io.Serializable;

/**
 * 前台用户入参
 *
 * @author think
 */
public class UserkgMCodeRequest implements Serializable {

    /**
     *
     */

    private static final long serialVersionUID = 9029186834138237452L;

	private String callerName;

	private String session;

	private String token;

	private String sig;

	private Integer platform;

	private String scene;

	public String getCallerName() {
		return callerName;
	}

	public void setCallerName(String callerName) {
		this.callerName = callerName;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSig() {
		return sig;
	}

	public void setSig(String sig) {
		this.sig = sig;
	}

	public Integer getPlatform() {
		return platform;
	}

	public void setPlatform(Integer platform) {
		this.platform = platform;
	}

	public String getScene() {
		return scene;
	}

	public void setScene(String scene) {
		this.scene = scene;
	}
}