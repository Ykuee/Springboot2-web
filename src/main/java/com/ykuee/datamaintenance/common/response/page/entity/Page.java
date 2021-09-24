package com.ykuee.datamaintenance.common.response.page.entity;

import java.util.List;

/**
 * 分页对象
 *
 * @author Cris
 */
public class Page {

    /**
     * 分页偏移量
     */
    private Integer pageNo = null;
    /**
     * 分页尺寸
     */
    private Integer pageSize = null;
    /**
     * 总页数
     */
    private Long total = null;

    private List<Sort> sorts = null;

    public Page() {
    }

    public Page(Integer pageNo, Integer pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<Sort> getSorts() {
        return sorts;
    }

    public void setSorts(List<Sort> sorts) {
        this.sorts = sorts;
    }

    @Override
    public String toString() {
        return "Page [pageNo=" + pageNo + ", pageSize=" + pageSize + ", total=" + total + ", sorts=" + sorts + "]";
    }

}
