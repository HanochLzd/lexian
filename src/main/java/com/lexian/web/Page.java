package com.lexian.web;

import java.util.Objects;

public class Page {


    private Integer pageNo = 1;

    private int pageSize = 30;

    private long totalSize;

    private int pageNums = 1;

    private Object data;

    public int getPageNums() {
        return pageNums;
    }

    public void setPageNums(int pageNums) {
        this.pageNums = pageNums;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {

        if (pageNo != null && pageNo > 0) {
            this.pageNo = pageNo;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalSize() {

        return totalSize;
    }

    public void setTotalSize(long totalSize) {


        if (totalSize > pageSize) {
            if (totalSize % pageSize != 0) {
                pageNums = (int) (totalSize / pageSize) + 1;
            } else {
                pageNums = (int) (totalSize / pageSize);
            }
        }

        this.totalSize = totalSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Page page = (Page) o;
        return pageSize == page.pageSize &&
                totalSize == page.totalSize &&
                pageNums == page.pageNums &&
                pageNo.equals(page.pageNo) &&
                Objects.equals(data, page.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageNo, pageSize, totalSize, pageNums, data);
    }
}
