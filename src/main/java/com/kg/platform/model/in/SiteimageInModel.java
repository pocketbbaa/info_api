package com.kg.platform.model.in;

public class SiteimageInModel {
    Integer image_type;// 图片类型 1 资讯 2 广告 3 其他

    Integer navigator_pos;// 展示位置 1 首页 2 栏目列表 3 频道页 4 资讯详情

    Integer image_pos;// 对应具体位置

    Integer os_version;//1：IOS 2:Android

    public Integer getOs_version() {
        return os_version;
    }

    public void setOs_version(Integer os_version) {
        this.os_version = os_version;
    }

    public Integer getImage_type() {
        return image_type;
    }

    public void setImage_type(Integer image_type) {
        this.image_type = image_type;
    }

    public Integer getNavigator_pos() {
        return navigator_pos;
    }

    public void setNavigator_pos(Integer navigator_pos) {
        this.navigator_pos = navigator_pos;
    }

    public Integer getImage_pos() {
        return image_pos;
    }

    public void setImage_pos(Integer image_pos) {
        this.image_pos = image_pos;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SiteimageinModel [image_type=" + image_type + ", navigator_pos=" + navigator_pos + ", image_pos="
                + image_pos + ", getImage_type()=" + getImage_type() + ", getNavigator_pos()=" + getNavigator_pos()
                + ", getImage_pos()=" + getImage_pos() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
                + ", toString()=" + super.toString() + "]";
    }

}
