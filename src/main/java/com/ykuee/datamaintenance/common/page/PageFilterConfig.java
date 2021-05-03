package com.ykuee.datamaintenance.common.page;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ykuee.datamaintenance.common.page.filter.PageFilter;

/**
 * 
  * @version:
  * @Description: 分页拦截
  * @author: Ykuee
  * @date: 2021-4-29 14:12:52
 */
@Configuration
public class PageFilterConfig {
 
    @Bean
    public FilterRegistrationBean registFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new PageFilter());
        registration.addUrlPatterns("/*");
        registration.setName("pageFilter");
        registration.setOrder(300);
        return registration;
    }
 
}
