package com.ykuee.datamaintenance.common.dynamictablename;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.parsers.ITableNameHandler;
import com.ykuee.datamaintenance.common.datasources.DataDymcSourceType;
import com.ykuee.datamaintenance.constant.DataSourceType;

import cn.hutool.core.util.StrUtil;

@Component
public class TableNameHandler implements ITableNameHandler {

	@Override
	public String dynamicTableName(MetaObject metaObject, String sql, String tableName) {
		//返回null不会替换 注意 多租户过滤会将它一块过滤不会替换@SqlParser(filter=true) 可不会替换;
		String dynamicTableName = TableNameContextHolder.getTableNameAttributes();
		if(StrUtil.isBlank(dynamicTableName)) {
			return null;
		}
		if(DataSourceType.NUDE.toString().equalsIgnoreCase(dynamicTableName)
			||DataSourceType.PROC.toString().equalsIgnoreCase(dynamicTableName)) {
			return dynamicTableName+"_"+tableName;
		}else {
			return dynamicTableName;
		}
	}

}
