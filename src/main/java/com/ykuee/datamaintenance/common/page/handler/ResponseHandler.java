package com.ykuee.datamaintenance.common.page.handler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.ykuee.datamaintenance.common.base.response.BaseResponse;
import com.ykuee.datamaintenance.common.page.context.PageContextHolder;
import com.ykuee.datamaintenance.common.page.disablewrapper.DisableWrapper;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ConditionalOnClass(ResponseBodyAdvice.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ControllerAdvice(annotations = {RestController.class, ResponseBody.class})
public class ResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 如果返回true继续执行，false不向下执行
        boolean hasWrapper = this.hasDisableWrapper(returnType);
        if (hasWrapper) {
            hasWrapper = !this.hasResponseType(returnType);
        }
        return hasWrapper;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse serverHttpResponse) {
        BaseResponse<Object> baseResponse = new BaseResponse<>();
        baseResponse.setCode("200");
        baseResponse.setMsg("成功");
        baseResponse.setPath("");
        baseResponse.setData(body);
        baseResponse.setPage(PageContextHolder.getPageAttributes());
        return baseResponse;
    }


    /**
     * 是否包装结果
     *
     * @param returnType
     * @return
     */
    private boolean hasDisableWrapper(MethodParameter returnType) {
        DisableWrapper disableWrapper = returnType.getMethod().getAnnotation(DisableWrapper.class);
        return disableWrapper == null ? true : false;
    }

    /**
     * 返回类型是否Response
     *
     * @param returnType 方法的返回类型
     * @return 如果返回的类型是Response就返回true，
     */
    private boolean hasResponseType(MethodParameter returnType) {
        Type type = returnType.getMethod().getReturnType();
        return type == BaseResponse.class;
    }

}
