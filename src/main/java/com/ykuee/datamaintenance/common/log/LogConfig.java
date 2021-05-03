package com.ykuee.datamaintenance.common.log;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogConfig {

    @Bean
    public SysLogAspect sysLogAspect() {
        return new SysLogAspect();
    }
    
    @Bean
    public SysLogListener sysLogListener() {
    	return new SysLogListener();
    }
    
}
