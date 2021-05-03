package com.ykuee.datamaintenance.common.datasources;

import java.util.Map;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
	
	public DynamicDataSource(javax.sql.DataSource dataSource, Map<Object, Object> targetDataSources)
    {
        super.setDefaultTargetDataSource(dataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

	@Override
	protected Object determineCurrentLookupKey() {
		
		return DataSourceContextHolder.getDataSource();
	}

}
