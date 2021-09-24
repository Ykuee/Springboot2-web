package com.ykuee.datamaintenance.common.mybatisplus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.DynamicTableNameParser;
import com.baomidou.mybatisplus.extension.parsers.ITableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.ykuee.datamaintenance.common.datasources.DynamicDataSource;
import com.ykuee.datamaintenance.common.dynamictablename.TableNameHandler;
import com.ykuee.datamaintenance.common.dynamictablename.TableNames;
import com.ykuee.datamaintenance.common.mybatisplus.handler.DefaultMetaObjectHandler;
import com.ykuee.datamaintenance.common.mybatisplus.typehandler.CodeEnumTypeHandler;
import com.ykuee.datamaintenance.common.mybatisplus.typehandler.FullLikeTypeHandler;
import com.ykuee.datamaintenance.common.mybatisplus.typehandler.LeftLikeTypeHandler;
import com.ykuee.datamaintenance.common.mybatisplus.typehandler.RightLikeTypeHandler;
import com.ykuee.datamaintenance.common.response.page.interceptor.PageInterceptor;
import com.ykuee.datamaintenance.common.uidgenerator.IdGenerator;

import cn.hutool.core.util.ArrayUtil;
/**
 * @Description: Mybatis多数据源配置
 * @Author: heguangyue
 * @Date: Created in 2020/7/16
 */
@Configuration
@MapperScan("com.ykuee.datamaintenance.mapper")
@EnableTransactionManagement
public class MybatisPlusConfig {

    @Value("${datamaintenance.db.db-type:oracle}")
    private String dbType;

    @Autowired
    private TableNameHandler tableNameHandler;

    @Autowired
    private TableNames tableNames;

    @Value("${spring.datasource.jndi-name}")
    private String jndiName;


	@Bean("transactionManager")
	@ConditionalOnProperty(prefix = "spring.datasource", name = "ds-type", havingValue = "jdbc")
	public PlatformTransactionManager transactionManager(DynamicDataSource dynamicDataSource) {
		return new DataSourceTransactionManager(dynamicDataSource);
	}

	@Bean("transactionManager")
	@ConditionalOnProperty(prefix = "spring.datasource", name = "ds-type", havingValue = "jndi")
	public PlatformTransactionManager jndiTransactionManager(DataSource jndiDataSource) {
		return new DataSourceTransactionManager(jndiDataSource);
	}

    /**
     * mybatis Sql Session 工厂
     *
     * @param globalConfig
     * @param metaObjectHandler
     * @return
     * @throws Exception
     */
    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("globalConfig") GlobalConfig globalConfig,
                                               @Qualifier("metaObjectHandler") MetaObjectHandler metaObjectHandler,
                                               DataSource dataSource,
                                               MybatisPlusProperties mybatisPlusProperties) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);

        StringBuilder sb = new StringBuilder();
        sb.append("classpath*:mapper/**/").append(dbType).append("/*Mapper.xml");

        String[] mapperLocations = mybatisPlusProperties.getMapperLocations();
        mapperLocations = ArrayUtil.addAll(mapperLocations, new String[]{sb.toString()});

        return setMybatisSqlSessionFactoryBean(sqlSessionFactory, mapperLocations, globalConfig, metaObjectHandler);
    }

	protected SqlSessionFactory setMybatisSqlSessionFactoryBean(MybatisSqlSessionFactoryBean sqlSessionFactory,
																String[] resourceLocationPatterns,
																GlobalConfig globalConfig,
																MetaObjectHandler metaObjectHandler)throws Exception {
		Resource[] all = new Resource[] {};
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		for (String locationPattern : resourceLocationPatterns) {
			all = ArrayUtil.addAll(all, resolver.getResources(locationPattern));
		}
		sqlSessionFactory.setMapperLocations(all);

		MybatisConfiguration configuration = new MybatisConfiguration();
		configuration.setJdbcTypeForNull(JdbcType.NULL);
		configuration.setMapUnderscoreToCamelCase(true);
		configuration.setCacheEnabled(false);

		// 添加分页的拦截器
		configuration.addInterceptor(new PageInterceptor());
		configuration.addInterceptor(new com.github.pagehelper.PageInterceptor());
		configuration.addInterceptor(new OptimisticLockerInterceptor());
		configuration.addInterceptor(new OptimisticLockerInterceptor());

		// 添加默认的枚举处理器
		configuration.setDefaultEnumTypeHandler(CodeEnumTypeHandler.class);

		sqlSessionFactory.setConfiguration(configuration);

		 List<Interceptor> list = new ArrayList<>();
		 list.add(paginationInterceptor());
		 sqlSessionFactory.setPlugins(list.toArray(new Interceptor[list.size()]));

		globalConfig.setMetaObjectHandler(metaObjectHandler);
		sqlSessionFactory.setGlobalConfig(globalConfig);

		addTypeHandler(sqlSessionFactory);
		return sqlSessionFactory.getObject();
	}

    /**
     * 将like类型处理器加入到mybatis中
     *
     * @param sessionFactory
     */
    private static void addTypeHandler(final MybatisSqlSessionFactoryBean sessionFactory) {
        TypeHandler<?>[] typeHandlers = new TypeHandler[3];
        typeHandlers[0] = new LeftLikeTypeHandler();
        typeHandlers[1] = new RightLikeTypeHandler();
        typeHandlers[2] = new FullLikeTypeHandler();
        sessionFactory.setTypeHandlers(typeHandlers);
        Class<?>[] typeAliases = new Class[3];
        typeAliases[0] = LeftLikeTypeHandler.class;
        typeAliases[1] = RightLikeTypeHandler.class;
        typeAliases[2] = FullLikeTypeHandler.class;
        sessionFactory.setTypeAliases(typeAliases);
    }

	/**
	 * Mybatis Plus 注入器
	 *
	 * @param idGenerate
	 * @return
	 */
	@Bean("metaObjectHandler")
	public MetaObjectHandler metaObjectHandler(@Qualifier("globalConfig") GlobalConfig globalConfig,
			IdGenerator idGenerate, @Value("${datamaintenance.db.db-type:oracle}") String dbType) {
		String logicNotDeleteValue = globalConfig.getDbConfig().getLogicNotDeleteValue();
		return new DefaultMetaObjectHandler(logicNotDeleteValue, idGenerate, dbType);
	}

    /**
     * mybatis plus 全局配置
     *
     * @return
     */
    @Bean("globalConfig")
    @ConfigurationProperties(prefix = "mybatis-plus.global-config")
    public GlobalConfig globalConfig() {
        return defGlobalConfig();
    }

	protected GlobalConfig defGlobalConfig() {
		GlobalConfig conf = new GlobalConfig();
		GlobalConfig.DbConfig config = new GlobalConfig.DbConfig();
		config.setIdType(IdType.INPUT);
		config.setInsertStrategy(FieldStrategy.NOT_NULL);
		config.setUpdateStrategy(FieldStrategy.NOT_NULL);
		config.setSelectStrategy(FieldStrategy.NOT_EMPTY);
		conf.setDbConfig(config);
		return conf;
	}

	/**
	 *
	  *<p>Title: paginationInterceptor</p>
	  *<p>Description: mybatis动态表名
	  * - 通过配置文件application-tablename.yml 配置需要替换的表名
	  * - 使用com.ykuee.datamaintenance.util.TableNameUtil替换表名
	  *</p>
	  * @author Ykuee
	  * @date 2021-4-29
	  * @return
	 */
	@Bean
    public PaginationInterceptor paginationInterceptor() {
        //分页查询上限重新赋值
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        // 创建SQL解析器集合
        List<ISqlParser> sqlParserList = new ArrayList<>();

        // 动态表名SQL解析器
        DynamicTableNameParser dynamicTableNameParser = new DynamicTableNameParser();
        Map<String, ITableNameHandler> tableNameHandlerMap = new HashMap<>();
        List<String> tables = tableNames.getTableNames();
        if(tables!=null && tables.size()>0) {
        	tables.stream().forEach(table -> tableNameHandlerMap.put(table, tableNameHandler));
        }
        dynamicTableNameParser.setTableNameHandlerMap(tableNameHandlerMap);
        sqlParserList.add(dynamicTableNameParser);
        paginationInterceptor.setSqlParserList(sqlParserList);
        return paginationInterceptor;
    }
}
