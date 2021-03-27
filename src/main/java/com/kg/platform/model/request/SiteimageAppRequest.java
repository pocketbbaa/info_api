package com.kg.platform.model.request;

import java.io.Serializable;

/**
 * 首页图片推荐位接口入参
 *
 * @author think
 */
public class SiteimageAppRequest implements Serializable {

    private static final long serialVersionUID = -3137952004450056188L;

    private Integer columnId; //栏目ID

    private Integer image_type;// 图片类型 1 资讯 2 广告 3 其他

    private Integer navigator_pos;// 展示位置 1 首页 2 栏目列表 3 频道页 4 资讯详情

    private Integer image_pos;// 对应具体位置

    private Integer  type;//1.视频

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getColumnId() {
        return columnId;
    }

    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
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
     * public SiteimageRequest(Integer image_type, Integer navigator_pos,
     * Integer image_pos) { super(); this.image_type = image_type;
     * this.navigator_pos = navigator_pos; this.image_pos = image_pos; }
     */

}
