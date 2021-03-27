package com.kg.platform.model.response;

/**
 * Created by Administrator on 2018/5/21.
 */
public class ActivityPopInfoResponse {
    //activityUrl：活动跳转URL、activityTitle：活动标题、activityBtnText：活动按钮文字、activityDesc：活动描述、activityBg：活动背景图、activitySmallIcon：活动小图标、activityKey: 活动时间的setting_key
    public static final String SETTING_KEY = "activity_pop_info";

    private String activityKey;

    private String activityImg;

    private String activityUrl;

    private String activityBg;

    private String activityTitle;

    private String activityDesc;

    private String activityBtnText;

    private String activitySmallIcon;//活動小圖標

    private int iosSwitch;//0:关闭 1：打开

    private int androidSwitch;//0:关闭 1：打开

	private Integer type;//0：h5活动，1：新人红包活动 2：文章 3：广告 4：专栏 5：积分商城

	private Integer popMethod;//1:首次启动APP时弹窗 2：每次启动APP时弹窗 3：每自然日启动APP时弹窗

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getPopMethod() {
		return popMethod;
	}

	public void setPopMethod(Integer popMethod) {
		this.popMethod = popMethod;
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

    public String getActivitySmallIcon() {
        return activitySmallIcon;
    }

    public void setActivitySmallIcon(String activitySmallIcon) {
        this.activitySmallIcon = activitySmallIcon;
    }

    public String getActivityKey() {
        return activityKey;
    }

    public void setActivityKey(String activityKey) {
        this.activityKey = activityKey;
    }

    public String getActivityBg() {
        return activityBg;
    }

    public void setActivityBg(String activityBg) {
        this.activityBg = activityBg;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public String getActivityBtnText() {
        return activityBtnText;
    }

    public void setActivityBtnText(String activityBtnText) {
        this.activityBtnText = activityBtnText;
    }

    public String getActivityImg() {
        return activityImg;
    }

    public void setActivityImg(String activityImg) {
        this.activityImg = activityImg;
    }

    public String getActivityUrl() {
        return activityUrl;
    }

    public void setActivityUrl(String activityUrl) {
        this.activityUrl = activityUrl;
    }
}
