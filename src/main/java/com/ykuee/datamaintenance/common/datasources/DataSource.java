package com.ykuee.datamaintenance.common.datasources;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
  * @version:
  * @Description: 自定义注解，用于类或方法上，优先级：方法>类
  * @author: Ykuee
  * @date: 2021-3-3 16:21:46
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {
	/**
     * 切换数据源名称
     */
	DataDymcSourceType value() default DataDymcSourceType.MASTER;
}
