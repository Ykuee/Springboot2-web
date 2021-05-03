package com.ykuee.datamaintenance.common.datasources;

import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
/**
 * @Description: Mybatis多数据源配置
 * @Author: heguangyue
 * @Date: Created in 2020/7/16
 */
@Configuration
@MapperScan("com.ykuee.datamaintenance.mapper")
@EnableTransactionManagement
public class DataSourceConfig {

    @Value("${spring.datasource.jndi-name}")
    private String jndiName;
    
	@Bean(destroyMethod = "close", initMethod = "init")
	@ConfigurationProperties("spring.datasource.druid.master")
	@ConditionalOnProperty(prefix = "spring.datasource", name = "ds-type", havingValue = "jdbc")
	public DataSource masterDataSource() {
		return DruidDataSourceBuilder.create().build();
	}
	
    @Primary
    @Bean("jndiDataSource")
    @ConditionalOnProperty(prefix = "spring.datasource", name = "ds-type", havingValue = "jndi")
    public DataSource primaryDataSource() throws NamingException {
        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
        bean.setJndiName(jndiName);
        bean.setProxyInterface(DataSource.class);
        bean.setLookupOnStartup(false);
        bean.afterPropertiesSet();
        return (DataSource) bean.getObject();
    }

	@Bean(destroyMethod = "close", initMethod = "init")
	@ConfigurationProperties("spring.datasource.druid.slave")
	@ConditionalOnProperty(prefix = "spring.datasource.druid.slave", name = "enabled", havingValue = "true")
	public DataSource slaveDataSource() {
		return DruidDataSourceBuilder.create().build();
	}

	@Primary
	@Bean(name = "dynamicDataSource")
	@ConditionalOnProperty(prefix = "spring.datasource", name = "ds-type", havingValue = "jdbc")
	public DynamicDataSource dataSource(DataSource masterDataSource, DataSource slaveDataSource) {
		Map<Object, Object> targetDataSources = new HashMap<>();
		targetDataSources.put(DataDymcSourceType.MASTER.name(), masterDataSource);
		targetDataSources.put(DataDymcSourceType.SLAVE.name(), slaveDataSource);
		return new DynamicDataSource(masterDataSource(), targetDataSources);
	}

	
}
