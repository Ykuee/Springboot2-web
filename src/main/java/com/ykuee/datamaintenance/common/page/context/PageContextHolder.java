package com.ykuee.datamaintenance.common.page.context;

import com.ykuee.datamaintenance.common.page.entity.Page;

/**
 * 系统上下文，使用threadlocal
 *
 * @author Cris
 */
public class PageContextHolder {

    private static ThreadLocal<Page> pageAttributesHolder = new InheritableThreadLocal<>();

    /**
     * 获取当前线程的变量
     *
     * @return
     */
    public static Page getPageAttributes() {
        return PageContextHolder.pageAttributesHolder.get();
    }

    /**
     * 设置当前线程的变量
     *
     * @param page
     */
    public static void setPageAttributes(Page page) {
        PageContextHolder.pageAttributesHolder.set(page);
    }

    /**
     * 移除当前线程的变量
     */
    static void removePageAttributes() {
        PageContextHolder.pageAttributesHolder.remove();
    }

}
