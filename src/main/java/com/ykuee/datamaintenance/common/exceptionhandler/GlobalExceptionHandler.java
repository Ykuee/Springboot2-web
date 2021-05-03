package com.ykuee.datamaintenance.common.exceptionhandler;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.connection.PoolException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.ykuee.datamaintenance.common.base.constant.ExceptionCode;
import com.ykuee.datamaintenance.common.base.exception.BusinessException;
import com.ykuee.datamaintenance.common.base.response.BaseResponse;
import com.ykuee.datamaintenance.common.page.disablewrapper.DisableWrapper;

import cn.hutool.core.util.StrUtil;



/**
 * 全局性的mvc异常处理
 */
@Order(799)
@ConditionalOnClass({RestController.class, Controller.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ResponseBody
@ControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<Object> busException(BusinessException ex, HttpServletRequest request, HandlerMethod handlerMethod) {
        logger.error("BizException:", ex);

        boolean hasDisableWrapper = this.hasDisableWrapper(handlerMethod);
        if (!hasDisableWrapper) {
            return null;
        }
        BaseResponse<Object> baseResponse = build(ex.getCode(), ex.getMessage(), request.getRequestURI());
        return baseResponse;
    }
    
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public BaseResponse handle401(UnauthorizedException e) {
        return build(ExceptionCode.UNAUTHORIZED.getCode(), ExceptionCode.UNAUTHORIZED.getDescription(),"");
    }
    
	@ExceptionHandler(PoolException.class)
	public BaseResponse<Object> handlePoolException(PoolException e) {
		logger.error(e.getMessage(), e);
		return build(ExceptionCode.REDIS_EX.getCode(), ExceptionCode.REDIS_EX.getDescription(), "");
	}
	
    @ExceptionHandler(AuthorizationException.class)
    public BaseResponse<Object> handleAuthorizationException(HttpServletRequest request, AuthorizationException e)
    {
    	logger.error(" 权限校验异常》》"+e.getMessage(), e);
    	return build(ExceptionCode.UNAUTHORIZED.getCode(), ExceptionCode.UNAUTHORIZED.getDescription(), request.getRequestURI());
    }
    
    @ExceptionHandler(AuthenticationException.class)
    public BaseResponse<Object> handleAuthenticationException(HttpServletRequest request, AuthenticationException e)
    {
    	logger.error(" 权限校验异常》》"+e.getMessage(), e);
    	return build(ExceptionCode.JWT_TOKEN_EXPIRED.getCode(), ExceptionCode.JWT_TOKEN_EXPIRED.getDescription(), request.getRequestURI());
    }
    
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public BaseResponse<Object> httpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request, HandlerMethod handlerMethod) {
        logger.error("HttpMessageNotReadableException:", ex);

        boolean hasDisableWrapper = this.hasDisableWrapper(handlerMethod);
        if (!hasDisableWrapper) {
            return null;
        }

        String message = ex.getMessage();
        if (StrUtil.containsAny(message, "Could not read document:")) {
            String msg = String.format("无法正确的解析json类型的参数：%s", StrUtil.subBetween(message, "Could not read document:", " at "));

            return build(ExceptionCode.PARAM_EX.getCode(), msg, request.getRequestURI());
        }
        return build(ExceptionCode.PARAM_EX.getCode(), ExceptionCode.PARAM_EX.getDescription(), request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BindException.class)
    public BaseResponse<Object> bindException(BindException ex, HttpServletRequest request, HandlerMethod handlerMethod) {
        logger.error("BindException:", ex);

        boolean hasDisableWrapper = this.hasDisableWrapper(handlerMethod);
        if (!hasDisableWrapper) {
            return null;
        }

        try {
            String msgs = ex.getBindingResult().getFieldError().getDefaultMessage();
            if (StrUtil.isNotEmpty(msgs)) {
                return build(ExceptionCode.PARAM_EX.getCode(), msgs, request.getRequestURI());
            }
        } catch (Exception ee) {
        }
        StringBuilder msg = new StringBuilder();
        List<FieldError> fieldErrors = ex.getFieldErrors();
        fieldErrors.forEach((oe) ->
                msg.append("参数:[").append(oe.getObjectName())
                        .append(".").append(oe.getField())
                        .append("]的传入值:[").append(oe.getRejectedValue()).append("]与预期的字段类型不匹配.")
        );
        return build(ExceptionCode.PARAM_EX.getCode(), msg.toString(), request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    BaseResponse<Object> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request, HandlerMethod handlerMethod) {
        logger.error("MethodArgumentTypeMismatchException:", ex);

        boolean hasDisableWrapper = this.hasDisableWrapper(handlerMethod);
        if (!hasDisableWrapper) {
            return null;
        }

        StringBuilder msg = new StringBuilder("参数：[").append(ex.getName())
                .append("]的传入值：[").append(ex.getValue())
                .append("]与预期的字段类型：[").append(ex.getRequiredType().getName()).append("]不匹配");
        return build(ExceptionCode.PARAM_EX.getCode(), msg.toString(), request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalStateException.class)
    BaseResponse<Object> illegalStateException(IllegalStateException ex, HttpServletRequest request, HandlerMethod handlerMethod) {
        logger.error("IllegalStateException:", ex);

        boolean hasDisableWrapper = this.hasDisableWrapper(handlerMethod);
        if (!hasDisableWrapper) {
            return null;
        }

        return build(ExceptionCode.ILLEGALA_ARGUMENT_EX.getCode(), ExceptionCode.ILLEGALA_ARGUMENT_EX.getDescription(), request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    BaseResponse<Object> missingServletRequestParameterException(MissingServletRequestParameterException ex, HttpServletRequest request, HandlerMethod handlerMethod) {
        logger.error("MissingServletRequestParameterException:", ex);

        boolean hasDisableWrapper = this.hasDisableWrapper(handlerMethod);
        if (!hasDisableWrapper) {
            return null;
        }

        StringBuilder msg = new StringBuilder();
        msg.append("缺少必须的[").append(ex.getParameterType()).append("]类型的参数[").append(ex.getParameterName()).append("]");
        return build(ExceptionCode.ILLEGALA_ARGUMENT_EX.getCode(), msg.toString(), request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NullPointerException.class)
    BaseResponse<Object> nullPointerException(NullPointerException ex, HttpServletRequest request, HandlerMethod handlerMethod) {
        logger.error("NullPointerException:", ex);

        boolean hasDisableWrapper = this.hasDisableWrapper(handlerMethod);
        if (!hasDisableWrapper) {
            return null;
        }

        return build(ExceptionCode.SYSTEM_BUSY.getCode(), ExceptionCode.SYSTEM_BUSY.getDescription(), request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalArgumentException.class)
    BaseResponse<Object> illegalArgumentException(IllegalArgumentException ex, HttpServletRequest request, HandlerMethod handlerMethod) {
        logger.error("IllegalArgumentException:", ex);

        boolean hasDisableWrapper = this.hasDisableWrapper(handlerMethod);
        if (!hasDisableWrapper) {
            return null;
        }

        return build(ExceptionCode.ILLEGALA_ARGUMENT_EX.getCode(), ex.getMessage(), request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    BaseResponse<Object> httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex, HttpServletRequest request, HandlerMethod handlerMethod) {
        logger.error("HttpMediaTypeNotSupportedException:", ex);

        boolean hasDisableWrapper = this.hasDisableWrapper(handlerMethod);
        if (!hasDisableWrapper) {
            return null;
        }

        MediaType contentType = ex.getContentType();
        if (contentType != null) {
            StringBuilder msg = new StringBuilder();
            msg.append("请求类型(Content-Type)[").append(contentType.toString()).append("] 与实际接口的请求类型不匹配");
            return build(ExceptionCode.MEDIA_TYPE_EX.getCode(), msg.toString(), request.getRequestURI());
        }
        return build(ExceptionCode.MEDIA_TYPE_EX.getCode(), "无效的Content-Type类型", request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MissingServletRequestPartException.class)
    BaseResponse<Object> missingServletRequestPartException(MissingServletRequestPartException ex, HttpServletRequest request, HandlerMethod handlerMethod) {
        logger.error("MissingServletRequestPartException:", ex);

        boolean hasDisableWrapper = this.hasDisableWrapper(handlerMethod);
        if (!hasDisableWrapper) {
            return null;
        }

        return build(ExceptionCode.REQUIRED_FILE_PARAM_EX.getCode(), ExceptionCode.REQUIRED_FILE_PARAM_EX.getDescription(), request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ServletException.class)
    BaseResponse<Object> servletException(ServletException ex, HttpServletRequest request, HandlerMethod handlerMethod) {
        logger.error("ServletException:", ex);

        boolean hasDisableWrapper = this.hasDisableWrapper(handlerMethod);
        if (!hasDisableWrapper) {
            return null;
        }

        String msg = "UT010016: Not a multi part request";
        if (msg.equalsIgnoreCase(ex.getMessage())) {
            return build(ExceptionCode.REQUIRED_FILE_PARAM_EX.getCode(), ExceptionCode.REQUIRED_FILE_PARAM_EX.getDescription(), request.getRequestURI());
        }
        return build(ExceptionCode.SYSTEM_BUSY.getCode(), ex.getMessage(), request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MultipartException.class)
    BaseResponse<Object> multipartException(MultipartException ex, HttpServletRequest request, HandlerMethod handlerMethod) {
        logger.error("MultipartException:", ex);

        boolean hasDisableWrapper = this.hasDisableWrapper(handlerMethod);
        if (!hasDisableWrapper) {
            return null;
        }

        return build(ExceptionCode.REQUIRED_FILE_PARAM_EX.getCode(), ExceptionCode.REQUIRED_FILE_PARAM_EX.getDescription(), request.getRequestURI());
    }

    /**
     * jsr 规范中的验证异常
     *
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ConstraintViolationException.class)
    BaseResponse<Object> constraintViolationException(ConstraintViolationException ex, HttpServletRequest request, HandlerMethod handlerMethod) {
        logger.error("ConstraintViolationException:", ex);

        boolean hasDisableWrapper = this.hasDisableWrapper(handlerMethod);
        if (!hasDisableWrapper) {
            return null;
        }

        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        String message = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(";"));
        return build(ExceptionCode.PARAM_VALID_EX.getCode(), message, request.getRequestURI());
    }

    /**
     * spring 封装的参数验证异常， 在conttoller中没有写result参数时，会进入
     *
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    BaseResponse<Object> methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request, HandlerMethod handlerMethod) {
        logger.error("MethodArgumentNotValidException:", ex);

        boolean hasDisableWrapper = this.hasDisableWrapper(handlerMethod);
        if (!hasDisableWrapper) {
            return null;
        }

        BaseResponse<Object> response=build(ExceptionCode.PARAM_VALID_EX.getCode(), ExceptionCode.PARAM_VALID_EX.getDescription(), request.getRequestURI());
        return response;
        
    }

    /**
     * 其他异常
     *
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    BaseResponse<Object> otherExceptionHandler(Exception ex, HttpServletRequest request, HandlerMethod handlerMethod) {
        logger.error("Exception:", ex);

        boolean hasDisableWrapper = this.hasDisableWrapper(handlerMethod);
        if (!hasDisableWrapper) {
            return null;
        }

        if (ex.getCause() instanceof BusinessException) {
            return busException((BusinessException) ex.getCause(), request, handlerMethod);
        }
        return build(ExceptionCode.SYSTEM_BUSY.getCode(), ExceptionCode.SYSTEM_BUSY.getDescription(), request.getRequestURI());
    }

    /**
     * 返回状态码:405
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    BaseResponse<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpServletRequest request, HandlerMethod handlerMethod) {
        logger.error("HttpRequestMethodNotSupportedException:", ex);

        boolean hasDisableWrapper = this.hasDisableWrapper(handlerMethod);
        if (!hasDisableWrapper) {
            return null;
        }

        return build(ExceptionCode.METHOD_NOT_ALLOWED.getCode(), ExceptionCode.METHOD_NOT_ALLOWED.getDescription(), request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(PersistenceException.class)
    BaseResponse<Object> persistenceException(PersistenceException ex, HttpServletRequest request, HandlerMethod handlerMethod) {
        logger.error("PersistenceException:", ex);

        boolean hasDisableWrapper = this.hasDisableWrapper(handlerMethod);
        if (!hasDisableWrapper) {
            return null;
        }

        if (ex.getCause() instanceof BusinessException) {
            return busException((BusinessException) ex.getCause(), request, handlerMethod);
        }
        return build(ExceptionCode.SQL_EX.getCode(), ExceptionCode.SQL_EX.getDescription(), request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MyBatisSystemException.class)
    BaseResponse<Object> myBatisSystemException(MyBatisSystemException ex, HttpServletRequest request, HandlerMethod handlerMethod) {
        logger.error("PersistenceException:", ex);

        boolean hasDisableWrapper = this.hasDisableWrapper(handlerMethod);
        if (!hasDisableWrapper) {
            return null;
        }

        if (ex.getCause() instanceof PersistenceException) {
            return persistenceException((PersistenceException) ex.getCause(), request, handlerMethod);
        }
        return build(ExceptionCode.SQL_EX.getCode(), ExceptionCode.SQL_EX.getDescription(), request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SQLException.class)
    BaseResponse<Object> sqlException(SQLException ex, HttpServletRequest request, HandlerMethod handlerMethod) {
        logger.error("SQLException:", ex);

        boolean hasDisableWrapper = this.hasDisableWrapper(handlerMethod);
        if (!hasDisableWrapper) {
            return null;
        }

        return build(ExceptionCode.SQL_EX.getCode(), ExceptionCode.SQL_EX.getDescription(), request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataIntegrityViolationException.class)
    BaseResponse<Object> dataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request, HandlerMethod handlerMethod) {
        logger.error("DataIntegrityViolationException:", ex);

        boolean hasDisableWrapper = this.hasDisableWrapper(handlerMethod);
        if (!hasDisableWrapper) {
            return null;
        }

        return build(ExceptionCode.SQL_EX.getCode(), ExceptionCode.SQL_EX.getDescription(), request.getRequestURI());
    }

    private BaseResponse<Object> build(String code, String description, String path) {
        BaseResponse<Object> baseResponse = new BaseResponse(code, description, path);
        baseResponse.setCode("500");
        baseResponse.setMsg(description);

        return baseResponse;
    }

    /**
     * 是否包装结果
     *
     * @param handlerMethod
     * @return
     */
    private Boolean hasDisableWrapper(HandlerMethod handlerMethod) {
        DisableWrapper disableWrapper = handlerMethod.getMethodAnnotation(DisableWrapper.class);
        return disableWrapper == null ? true : false;
    }

}
