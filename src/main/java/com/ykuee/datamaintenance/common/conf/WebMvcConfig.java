package com.ykuee.datamaintenance.common.conf;

import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.servlet.Servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * @description：对controller返回的数据设置转换器
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class})
public class WebMvcConfig implements WebMvcConfigurer  {

    private static Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    	// 由于jackson默认对String类型返回值处理为text/plan。所以对string类型处理有问题，因为项目统一包装的原因，所以去掉该类型
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();

        List<SerializerFeature> serializerFeatureList = new ArrayList<>(10);
        serializerFeatureList.add(SerializerFeature.PrettyFormat);
        serializerFeatureList.add(SerializerFeature.WriteEnumUsingName);
        serializerFeatureList.add(SerializerFeature.WriteMapNullValue);
        SerializerFeature[] serializerFeatures = serializerFeatureList.toArray(new SerializerFeature[serializerFeatureList.size()]);
        fastJsonConfig.setSerializerFeatures(serializerFeatures);

        // 序列化配置
        SerializeConfig serializeConfig = new SerializeConfig();
        fastJsonConfig.setSerializeConfig(serializeConfig);

        // 解析配置
        ParserConfig parserConfig = new ParserConfig();
        fastJsonConfig.setParserConfig(parserConfig);

        fastConverter.setFastJsonConfig(fastJsonConfig);

        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        supportedMediaTypes.add(MediaType.APPLICATION_PDF);
        supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XML);
        supportedMediaTypes.add(MediaType.IMAGE_GIF);
        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
        supportedMediaTypes.add(MediaType.IMAGE_PNG);
        supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        supportedMediaTypes.add(MediaType.TEXT_XML);
        fastConverter.setSupportedMediaTypes(supportedMediaTypes);
        converters.add(fastConverter);
    	logger.info("http converters:[{}]", converters);
    }
    
    private SerializeConfig serializeConfig() {
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
        serializeConfig.put(BigDecimal.class, ToStringSerializer.instance);
        serializeConfig.put(Long.class, ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
        return serializeConfig;
    }

}
