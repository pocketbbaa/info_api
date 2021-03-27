
package com.kg.platform.common.entity;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * 分页对象
 * 
 * @version $Id: PageModel.java
 */
public class PageModel<T> implements Serializable {

    private static final long serialVersionUID = 2386907222204760125L;

    private int currentPage = 1;

    private int pageSize = 20;

    private long totalNumber;

    private long totalPage;

    private Object totalPrice;

    private List<T> data;

    private List<T> extraData;

    public PageModel() {
    }

    public PageModel(PageableRequestBean page) {
        this.currentPage = page.getCurrentPage();
        this.pageSize = page.getPageSize();
    }

    public PageModel(int currentPage) {
        this.currentPage = currentPage;
    }

    public PageModel(int currentPage, int totalNumber, int totalPage, int pageSize) {
        this.currentPage = currentPage;
        this.totalNumber = totalNumber;
        this.totalPage = totalPage;
        this.pageSize = pageSize;
    }

    public PageModel(int currentPage, int totalNumber, int totalPage) {
        this.currentPage = currentPage;
        this.totalNumber = totalNumber;
        this.totalPage = totalPage;
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

    public long getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(long totalNumber) {
        this.totalNumber = totalNumber;
        this.totalPage = (this.totalNumber % this.pageSize == 0 ? this.totalNumber / this.pageSize
                : this.totalNumber / this.pageSize + 1);
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Object getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Object totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public List<T> getExtraData() {
        return extraData;
    }

    public void setExtraData(List<T> extraData) {
        this.extraData = extraData;
    }
    
}
