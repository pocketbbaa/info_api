package com.kg.platform.model.request;

import java.io.Serializable;

/**
 * 首页图片推荐位接口入参
 * 
 * @author think
 *
 */
public class SiteimageRequest {

    private static final long serialVersionUID = -3130530004450056188L;

    Integer image_type;// 图片类型 1 资讯 2 广告 3 其他

    Integer navigator_pos;// 展示位置 1 首页 2 栏目列表 3 频道页 4 资讯详情

    Integer image_pos;// 对应具体位置

    Long userId;// 用户id

    Long articleId;// 文章id

    int currentPage; //当前页数

    int pageSize; //页数

    Integer disPlayPort;// 对应具体位置

	Integer osVersion;

	public Integer getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(Integer osVersion) {
		this.osVersion = osVersion;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getDisPlayPort() {
        return disPlayPort;
    }

    public void setDisPlayPort(Integer disPlayPort) {
        this.disPlayPort = disPlayPort;
    }

    /*
     * public SiteimageRequest(Integer image_type, Integer navigator_pos,
     * Integer image_pos) { super(); this.image_type = image_type;
     * this.navigator_pos = navigator_pos; this.image_pos = image_pos; }
     */

}
