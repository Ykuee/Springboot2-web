package com.ykuee.datamaintenance.util;

import com.ykuee.datamaintenance.common.dynamictablename.TableNameContextHolder;
import com.ykuee.datamaintenance.constant.DataSourceType;

public class TableNameUtil {

	/**
	 * 
	  *<p>Title: setTableName</p>
	  *<p>Description: 为表名添加NUDE PROC两种前缀</p>
	  * @author Ykuee
	  * @date 2021-4-29 
	  * @param dataSourceType
	 */
	public static void setTableName(DataSourceType dataSourceType) {
		TableNameContextHolder.setTableNameAttributes(dataSourceType.getKey().toLowerCase());
	}
	
	/**
	 * 
	  *<p>Title: setTableName</p>
	  *<p>Description: 替换dynamic_table_name为传入的表名</p>
	  * @author Ykuee
	  * @date 2021-4-29 
	  * @param tableName
	 */
	public static void setTableName(String tableName) {
		TableNameContextHolder.setTableNameAttributes(tableName);
	}
	
	/**
	 * 
	  *<p>Title: clear</p>
	  *<p>Description: 清除动态替换</p>
	  * @author Ykuee
	  * @date 2021-4-29
	 */
	public static void clear() {
		TableNameContextHolder.removeTableNameAttributes();
	}
	
}
