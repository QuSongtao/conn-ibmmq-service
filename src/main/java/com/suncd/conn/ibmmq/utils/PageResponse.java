/*
 * 版权所有 © 成都太阳高科技有限责任公司
 * http://www.suncd.com
 */
package com.suncd.conn.ibmmq.utils;

import java.util.List;

/**
 * 功能描述: 分页响应对象
 *
 * @author qust
 * @version 1.0 2018/4/14 21:39
 */
public class PageResponse<T> {
    private PageResponse<T>.Page page;
    private List<T> rows;

    public PageResponse() {
        this.page = new PageResponse<T>.Page();
    }

    public PageResponse(long totalRows, List<T> rows) {
        this.page = new PageResponse<T>.Page(totalRows);
        this.rows = rows;
    }

    public PageResponse(long totalRows, String attachInfo, List<T> rows) {
        this.page = new PageResponse<T>.Page(totalRows, attachInfo);
        this.rows = rows;
    }

    public PageResponse<T>.Page getPage() {
        return this.page;
    }

    public void setPage(PageResponse<T>.Page pages) {
        this.page = pages;
    }

    public List<T> getRows() {
        return this.rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public PageResponse total(Long total) {
        this.page.setTotalRows(total);
        return this;
    }

    public PageResponse rows(List<T> rows) {
        this.rows = rows;
        return this;
    }

    public PageResponse attachInfo(String info) {
        this.page.setAttachInfo(info);
        return this;
    }

    public class Page {
        private String attachInfo;
        private long totalRows;

        public Page() {
        }

        public String getAttachInfo() {
            return this.attachInfo;
        }

        public void setAttachInfo(String attachInfo) {
            this.attachInfo = attachInfo;
        }

        public Page(long totalRows) {
            this.totalRows = totalRows;
        }

        public Page(long totalRows, String attachInfo) {
            this.attachInfo = attachInfo;
            this.totalRows = totalRows;
        }

        public long getTotalRows() {
            return this.totalRows;
        }

        public void setTotalRows(long totalRows) {
            this.totalRows = totalRows;
        }
    }
}
