package com.ykuee.datamaintenance.common.conf;

import static com.ykuee.datamaintenance.common.support.DateUtils.DEFAULT_DATE_FORMAT;
import static com.ykuee.datamaintenance.common.support.DateUtils.DEFAULT_DATE_TIME_FORMAT;
import static com.ykuee.datamaintenance.common.support.DateUtils.DEFAULT_TIME_FORMAT;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.Servlet;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.ykuee.datamaintenance.common.dateconverter.String2DateConverter;
import com.ykuee.datamaintenance.common.dateconverter.String2LocalDateConverter;
import com.ykuee.datamaintenance.common.dateconverter.String2LocalDateTimeConverter;
import com.ykuee.datamaintenance.common.dateconverter.String2LocalTimeConverter;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class})
public class DateConverterConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // ?????? @RequestParam(value = "date") Date date
        registry.addConverter(new String2DateConverter());
        // ?????? @RequestParam(value = "time") LocalDate time
        registry.addConverter(new String2LocalDateConverter());
        // ?????? @RequestParam(value = "time") LocalTime time
        registry.addConverter(new String2LocalTimeConverter());
        // ?????? @RequestParam(value = "time") LocalDateTime time
        registry.addConverter(new String2LocalDateTimeConverter());
    }

    /**
     * serializerByType ??????json???????????? LocalDateTime ????????????
     * deserializerByType ??????string?????????????????? LocalDateTime ????????????
     *
     * @return
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder
                .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)))
                .serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
                .serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)))
                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)))
                .deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
                .deserializerByType(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
    }

}
