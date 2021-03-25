package org.firzjb.base.model.request;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Hao on 2017-03-23.
 */

public class PageRequest<T> {



    /**
     * 当前页码
     */
    @ApiModelProperty(hidden = true)
    private Integer page = 1;
    /**
     * 每页数量
     */
    @ApiModelProperty(hidden = true)
    private Integer limit = 15;
    /**
     * 排序规则：升序=asc，降序=desc
     */
    @ApiModelProperty(hidden = true)
    private String order;// asc desc
    /**
     * 排序字段名称
     */
    @ApiModelProperty(hidden = true)
    private String sort;

    @ApiModelProperty(hidden = true)
    private String search_time;

    @ApiModelProperty(hidden = true)
    protected String search_satrt;


    @ApiModelProperty(hidden = true)
    protected String search_end;


    private int export;

    public PageRequest() {
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }


    public String getSearch_time() {
        return search_time;
    }

    public void setSearch_time(String search_time) {
        this.search_time = search_time;
    }

    public String getSearch_satrt() {
        return search_satrt;
    }

    public void setSearch_satrt(String search_satrt) {
        this.search_satrt = search_satrt;
    }

    public String getSearch_end() {
        return search_end;
    }

    public void setSearch_end(String search_end) {
        this.search_end = search_end;
    }

    public int getExport() {
        return export;
    }

    public void setExport(int export) {
        this.export = export;
    }

    /**
     * 设置排序规则
     *
     * @param order 升序=asc，降序=desc
     * @return
     */
    public PageRequest order(String order) {
        setOrder(order);
        return this;
    }



    /**
     * 设置排序字段
     *
     * @param orderBy 字段名称
     * @return
     */
    public PageRequest orderBy(String orderBy) {
        setSort(orderBy);
        return this;
    }

    @Override
    public String toString() {
        return "PageRequest{" +
                "page=" + page +
                ", rows=" + limit +
                ", order='" + order + '\'' +
                ", sort='" + sort + '\'' +
                '}';
    }
}
