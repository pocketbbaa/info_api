package com.kg.platform.service;

import java.util.List;

/**
 * Created by Administrator on 2018/8/24.
 */
public interface RefreshColumnArticle {
    /**
     * 后台发文后 更新对应栏目的文章总量 以及对应栏目的第一页文章数据
     */
    void refreshColumnArticle(Integer types , List<Integer> columnIds);
}
