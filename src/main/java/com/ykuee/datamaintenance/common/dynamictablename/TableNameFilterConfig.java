package com.ykuee.datamaintenance.common.dynamictablename;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ykuee.datamaintenance.common.page.filter.PageFilter;

/**
 * 
  * @version:
  * @Description: 用于清除动态表名的ThreadLocal
  * @author: Ykuee
  * @date: 2021-4-29 14:13:54
 */
@Configuration
public class TableNameFilterConfig {
 
    @Bean
    public FilterRegistrationBean registTableNameFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new TableNameFilter());
        registration.addUrlPatterns("/*");
        registration.setName("tableNameFilter");
        registration.setOrder(400);
        return registration;
    }
 
}
