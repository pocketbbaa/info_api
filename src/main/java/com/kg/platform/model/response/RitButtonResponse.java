package com.kg.platform.model.response;

public class RitButtonResponse {

    private String tip;
    private String url;
    private int show;
    private int state;
    private String buttonText;

    public RitButtonResponse() {
    }

    public RitButtonResponse(String tip, String url, int show, int state, String buttonText) {
        this.tip = tip;
        this.url = url;
        this.show = show;
        this.state = state;
        this.buttonText = buttonText;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public static RitButtonResponse init() {
        return new RitButtonResponse("", "", 0, 0, "");
    }
}