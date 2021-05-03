package com.ykuee.datamaintenance.common.dynamictablename;

/**
 * 系统上下文，使用threadlocal
 *
 * @author Cris
 */
public class TableNameContextHolder {

    private static ThreadLocal<String> tableNameAttributesHolder = new InheritableThreadLocal<>();

    /**
     * 获取当前线程的变量
     *
     * @return
     */
    public static String getTableNameAttributes() {
        return TableNameContextHolder.tableNameAttributesHolder.get();
    }

    /**
     * 设置当前线程的变量
     *
     * @param page
     */
    public static void setTableNameAttributes(String page) {
        TableNameContextHolder.tableNameAttributesHolder.set(page);
    }

    /**
     * 移除当前线程的变量
     */
    public static void removeTableNameAttributes() {
        TableNameContextHolder.tableNameAttributesHolder.remove();
    }

}
