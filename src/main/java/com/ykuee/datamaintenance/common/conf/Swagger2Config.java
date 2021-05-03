package com.ykuee.datamaintenance.common.conf;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ykuee.datamaintenance.common.prop.DataMaintenanceProp;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
  * @version:
  * @Description: Swagger 配置文件
  * @author: Ykuee
  * @date: 2021-3-2 23:34:41
 */
@Configuration
@EnableSwagger2 //启动swagger注解 启动服务，浏览器输入"http://服务名:8080/swagger-ui.html"
public class Swagger2Config implements WebMvcConfigurer{
	@Autowired
	private DataMaintenanceProp dataMaintenanceProp;
	
	@Value("${swagger2.enable}")
  	private boolean enable;
  
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				// 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
				.apiInfo(apiInfo())
				// 设置哪些接口暴露给Swagger展示
				.select()
				 // 扫描所有有注解的api，用这种方式更灵活
				.apis(RequestHandlerSelectors.basePackage("com.ykuee.datamaintenance"))
				// 扫描指定包中的swagger注解
				//.apis(RequestHandlerSelectors.basePackage("com.ykuee.datamaintenance.controller"))
				// 扫描所有 .apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo())
	            .enable(enable)
	            .globalOperationParameters(this.buildParameter());
	}
	
	  private List<Parameter> buildParameter(){
		    ParameterBuilder tokenPar = new ParameterBuilder();
		    List<Parameter> pars = new ArrayList<>();
		    tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
		    pars.add(tokenPar.build());
		    return pars;
		  }
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				//设置标题
				.title("数据维护平台 API文档")
				//描述
				.description("数据维护平台Swagger文档")
				 //作者信息
                //.contact(new Contact(dataMaintenanceProp.getName(), null, V2Config.getEmail_account()))
                //服务条款URL
				.termsOfServiceUrl("https://www.eolinker.com/#/share/index?shareCode=7TArnf")
				//版本
				.version("版本号:"+dataMaintenanceProp.getVersion())
				.build();
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 解决静态资源无法访问
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        // 解决swagger无法访问
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        // 解决swagger的js文件无法访问
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
