package com.ykuee.datamaintenance.common.response.page.context;

import com.google.common.base.CaseFormat;
import com.ykuee.datamaintenance.common.response.page.entity.Page;
import com.ykuee.datamaintenance.common.response.page.entity.Sort;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

public class PageContext {

    private static String getOrderBy(List<Sort> sortList) {
        StringBuffer sb = new StringBuffer();

        int size = sortList.size();
        int commaCount = size - 1;
        for (int i = 0; i < size; i++) {
            Sort sortItem = sortList.get(i);
            String column = sortItem.getColumn();
            String order = sortItem.getOrder();
            if (StringUtils.hasLength(column) && StringUtils.hasLength(order)) {
                column = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, column);
                sb.append(column).append(" ").append(order);
            }
            if (i < commaCount) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public static Page startPage(Page page) {
        if (page != null) {
            setPage(page);

            Integer pageNo = page.getPageNo();
            Integer pageSize = page.getPageSize();

            List<Sort> sortList = page.getSorts();
            String orderBy = !ObjectUtils.isEmpty(sortList) ? getOrderBy(sortList) : null;

            if (pageNo != null && pageSize != null && StringUtils.hasLength(orderBy)) {
                return startPage(pageNo, pageSize, orderBy);
            } else if (pageNo != null && pageSize != null && !StringUtils.hasLength(orderBy)) {
                return startPage(pageNo, pageSize);
            } else if (pageNo == null && pageSize == null && !StringUtils.hasLength(orderBy)) {
                startSort(orderBy);
                return null;
            }
        }

        return null;
    }

    public static Page startPage(int pageNo, int pageSize) {
        com.github.pagehelper.Page p = com.github.pagehelper.PageHelper.startPage(pageNo, pageSize);
        return page(p);
    }

    public static Page startPage(int pageNo, int pageSize, String orderBy) {
        com.github.pagehelper.Page p = com.github.pagehelper.PageHelper.startPage(pageNo, pageSize, orderBy);
        return page(p);
    }

    private static Page page(com.github.pagehelper.Page p) {
        Page page = PageContextHolder.getPageAttributes();
        if (page == null) {
            page = new Page(p.getPageNum(), p.getPageSize());
            page.setTotal(p.getTotal());

            setPage(page);
        }
        return page;
    }

    public static void startSort(String orderBy) {
        com.github.pagehelper.PageHelper.orderBy(orderBy);
    }

    public static void clearPage() {
        com.github.pagehelper.PageHelper.clearPage();
        PageContextHolder.removePageAttributes();
    }

    public static void setPage(Page page) {
        PageContextHolder.setPageAttributes(page);
    }

    public static Page getPage() {
        return PageContextHolder.getPageAttributes();
    }

}
