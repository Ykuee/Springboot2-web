package com.ykuee;

import java.net.InetAddress;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,PageHelperAutoConfiguration.class})
@MapperScan(basePackages = {"com.ykuee.datamaintenance.**.mapper.*"}, annotationClass = Repository.class)
public class SpringbootStart extends SpringBootServletInitializer{

	private static Logger logger = LoggerFactory.getLogger(SpringbootStart.class);
	
    public static void main(String[] args) throws Exception {
    	ConfigurableApplicationContext application = SpringApplication.run(SpringbootStart.class, args);
        Environment env = application.getEnvironment();
        logger.info("\n----------------------------------------------------------\n\t" +
                        "项目运行成功! swagger访问连接:\t" +
                        "http://{}:{}{}/swagger-ui.html\n" +
                        "----------------------------------------------------------",
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                env.getProperty("server.servlet.context-path"));
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringbootStart.class);
    }
    
}
