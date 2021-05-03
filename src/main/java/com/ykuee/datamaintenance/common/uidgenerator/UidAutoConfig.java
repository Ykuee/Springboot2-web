package com.ykuee.datamaintenance.common.uidgenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ykuee.datamaintenance.common.uidgenerator.impl.DefaultUidGenerator;

@Configuration
public class UidAutoConfig {

	/**
	 * 
	  *<p>Title: snowIdGenerator</p>
	  *<p>Description: Snowflake id生成器
	  * https://github.com/baidu/uid-generator
	  * </p>
	  * @author Ykuee
	  * @date 2021-4-29 
	  * @return
	 */
    @Bean
    public IdGenerator<String> snowIdGenerator() {
        UidGenerator uidGenerator = new DefaultUidGenerator(() -> 0);
        return () -> uidGenerator.getUID();
    }
    
}
