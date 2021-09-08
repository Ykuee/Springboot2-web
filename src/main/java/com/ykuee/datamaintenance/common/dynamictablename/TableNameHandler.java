package com.ykuee.datamaintenance.common.dynamictablename;

import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.parsers.ITableNameHandler;
import com.ykuee.datamaintenance.constant.DataSourceType;

import cn.hutool.core.util.StrUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TableNameHandler implements ITableNameHandler {

	private static Logger logger = LoggerFactory.getLogger(TableNameHandler.class);


	@Override
	public String process(MetaObject metaObject, String sql, String tableName) {
		String dynamicTableName = dynamicTableName(metaObject, sql, tableName);
		if (null != dynamicTableName && !dynamicTableName.equalsIgnoreCase(tableName)) {
			//sql = sql.replaceAll("\\s+"+tableName+"\\s+", " "+dynamicTableName+" ");
			if(sql.endsWith(tableName)){
				sql = replaceAllIgnoreCase(sql,"\\s+"+tableName," "+dynamicTableName+" ");
			}else{
				sql = replaceAllIgnoreCase(sql,"\\s+"+tableName+"\\s+"," "+dynamicTableName+" ");
			}
			return sql;
		}
		return sql;
	}

	public String replaceAllIgnoreCase(String input, String regex, String replacement) {
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(input);
		String result = m.replaceAll(replacement);
		return result;
	}

	@Override
	public String dynamicTableName(MetaObject metaObject, String sql, String tableName) {
		//返回null不会替换 注意 多租户过滤会将它一块过滤不会替换@SqlParser(filter=true) 可不会替换;
		String dynamicTableName = TableNameContextHolder.getTableNameAttributes();
		logger.info("动态表名:{}，表名:{}", dynamicTableName,tableName);
		if (StrUtil.isBlank(dynamicTableName)) {
			return null;
		}
		if (DataSourceType.DATA_1.toString().equalsIgnoreCase(dynamicTableName)
				|| DataSourceType.DATA_2.toString().equalsIgnoreCase(dynamicTableName)
				|| DataSourceType.DATA_3.toString().equalsIgnoreCase(dynamicTableName)) {
			return dynamicTableName + "_" + tableName;
		} else {
			return dynamicTableName;
		}
	}

}
