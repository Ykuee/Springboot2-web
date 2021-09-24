package com.ykuee.datamaintenance.common.response.page;

import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.pagehelper.PageHelper;

/**
 *
  * @version:
  * @Description: 分页插件配置类
  * @author: Ykuee
  * @date: 2021-3-3 16:21:24
 */
@Configuration
public class PageHelperConfig {

    /**
     * 分页插件处理
     * @return
     */
    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        properties.setProperty("reasonable", "true");
        properties.setProperty("dialect", "oracle");//配置postgresql数据库的方言
        pageHelper.setProperties(properties);
        return pageHelper;
    }
}
