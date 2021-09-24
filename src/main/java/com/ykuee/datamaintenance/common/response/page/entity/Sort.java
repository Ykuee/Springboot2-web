package com.ykuee.datamaintenance.common.response.page.entity;

import java.sql.SQLException;

public class Sort {

    private String column;

    private String order;

    public Sort(String column, String order) {
        this.column = column;
        this.order = order;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        // TODO 需要实现字符串的检测，防止sql注入
        this.column = column;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) throws SQLException {
        if (order.equalsIgnoreCase("desc") || order.equalsIgnoreCase("asc")) {
            this.order = order;
        } else {
            throw new SQLException("sql order by param error");
        }
    }

	@Override
	public String toString() {
		return "Sort [column=" + column + ", order=" + order + "]";
	}

}
