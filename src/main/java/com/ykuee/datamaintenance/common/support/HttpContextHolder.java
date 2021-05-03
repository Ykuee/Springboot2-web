package com.ykuee.datamaintenance.common.support;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpContextHolder {

    public static HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    public static String getRequestPath() {
        String contextPath = getHttpServletRequest().getContextPath();
        String requestPath = getHttpServletRequest().getRequestURI();
        return contextPath + requestPath;
    }

    public static String getClientIp() {
        HttpServletRequest httpServletRequest = getHttpServletRequest();
        String clientIp = httpServletRequest.getHeader("X-Real-IP");
        return clientIp;
    }

    public static HttpServletResponse getHttpServletResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getResponse() : null;
    }

    public static String getHeader(String key) {
        return getHttpServletRequest().getHeader(key);
    }

}
