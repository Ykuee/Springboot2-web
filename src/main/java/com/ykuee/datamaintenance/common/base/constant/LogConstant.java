package com.ykuee.datamaintenance.common.base.constant;

public class LogConstant {
    /**
     * 日志记录的操作类型
     */
    public static class LogOperateType {
        /** 点击菜单 */
        public static final String CLICKMENU = "CLICKMENU";
        /** 进入明细 */
        public static final String INTODETAIL = "INTODETAIL";
        /** 新增进入界面 */
        public static final String INTOINSERT = "INTOINSERT";
        /** 修改进入界面 */
        public static final String INTOUPDATE = "INTOUPDATE";
        /** 新增操作 */
        public static final String INSERT = "INSERT";
        /** 修改操作 */
        public static final String UPDATE = "UPDATE";
        /** 批量修改操作 */
        public static final String BATCHUPDATE = "BATCHUPDATE";
        /** 保存操作 */
		public static final String ADD = "ADD";
        /** 单笔删除操作 */
        public static final String DELETE = "DELETE";
        /** 批量删除操作 */
        public static final String BATCHDELETE = "BATCHDELETE";
        /** 提交流程操作 */
        public static final String SUBMIT = "SUBMIT";
        /** 审批成功操作 */
        public static final String CHECKNEXT = "CHECKNEXT";
        /** 审批拒绝操作 */
        public static final String CHECKREFUSE = "CHECKREFUSE";
        /** 提交撤销流程操作 */
        public static final String SUBMITREVOCK = "SUBMITREVOCK";
        /** 查询操作 */
        public static final String QUERY = "QUERY";
        /** 登录操作 */
        public static final String LOGIN = "LOGIN";
        /** 登出操作 */
        public static final String LOGOUT = "LOGOUT";
        /** 其他操作 */
        public static final String OTHER = "OTHER";

    }

    /**
     * 操作日志结果
     */
    public enum LogOperateResult {
        SUCCESS(1,"成功"),
        FAIL(2,"失败");

        private long value;
        private String explain;

        public long getValue() {
            return value;
        }

        public String getExplain() {
            return explain;
        }

        LogOperateResult(long value, String explain) {
            this.value = value;
            this.explain = explain;
        }
    }


}
