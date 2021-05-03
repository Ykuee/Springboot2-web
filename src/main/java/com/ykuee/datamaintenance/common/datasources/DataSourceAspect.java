package com.ykuee.datamaintenance.common.datasources;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.ObjectUtil;

import java.lang.reflect.Method;

/**
 * 
  * @version:
  * @Description: 多数据源处理
  * @author: Ykuee
  * @date: 2021-3-3 16:22:11
 */
@Aspect
@Component
@Order(1)
//@EnableAsync
public class DataSourceAspect {
	
	//private static final Logger log = LoggerFactory.getLogger(DataSourceAspect.class);
	
	@Pointcut("@annotation(com.ykuee.datamaintenance.common.datasources.DataSource)")
    public void dsPointCut()
    {

    }
	
	@Around("dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable
    {
        MethodSignature signature = (MethodSignature) point.getSignature();

        Method method = signature.getMethod();

        DataSource dataSource = method.getAnnotation(DataSource.class);

        if (null!=dataSource)
        {
            DataSourceContextHolder.setDataSource(dataSource.value().name());
        }

        try
        {
            return point.proceed();
        }
        finally
        {
            // 销毁数据源 在执行方法之后
        	DataSourceContextHolder.clearDataSource();
        }
    }
	
	 /**
     * 获取需要切换的数据源
     */
    public DataSource getDataSource(ProceedingJoinPoint point)
    {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class<? extends Object> targetClass = point.getTarget().getClass();
        DataSource targetDataSource = targetClass.getAnnotation(DataSource.class);
        if (ObjectUtil.isNotNull(targetDataSource))
        {
            return targetDataSource;
        }
        else
        {
            Method method = signature.getMethod();
            DataSource dataSource = method.getAnnotation(DataSource.class);
            return dataSource;
        }
    }
}
