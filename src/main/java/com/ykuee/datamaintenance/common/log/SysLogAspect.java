package com.ykuee.datamaintenance.common.log;


import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.ykuee.datamaintenance.common.base.constant.Constant;
import com.ykuee.datamaintenance.common.base.response.BaseResponse;
import com.ykuee.datamaintenance.common.log.entity.SysLogResult;
import com.ykuee.datamaintenance.common.support.HttpContextHolder;
import com.ykuee.datamaintenance.common.support.JsonMapper;
import com.ykuee.datamaintenance.common.support.JwtUtil;
import com.ykuee.datamaintenance.common.support.LogUtil;
import com.ykuee.datamaintenance.model.system.log.entity.SysLogEntity;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import cn.hutool.extra.servlet.ServletUtil;

/**
 * 操作日志使用spring event异步入库
 */
@Aspect
@Order(999)
public class SysLogAspect {

    private static Logger logger = LoggerFactory.getLogger(SysLogAspect.class);

    @Value("${datamaintenance.name}")
    private String moduleName;
    /**
     * 事件发布是由ApplicationContext对象管控的，我们发布事件前需要注入ApplicationContext对象调用publishEvent方法完成事件发布
     **/
    @Autowired
    private ApplicationContext applicationContext;

    /***
     * 定义controller切入点拦截规则，拦截SysLog注解的方法
     */
    @Pointcut("@annotation(com.ykuee.datamaintenance.common.log.entity.SysLog)")
    public void sysLogAspect() {

    }

    @Before(value = "sysLogAspect()")
    public void recordLog(JoinPoint joinPoint) {
        tryCatch(false, (val) -> {
            SysLogEntity operateLogDTO = SysLogContextHolder.get();

            // 开始时间
            operateLogDTO.setStartTime(LocalDateTime.now());

            annotation(operateLogDTO, joinPoint);

            userInfo(operateLogDTO);

            classMethodArgs(operateLogDTO, joinPoint);

            httpInfo(operateLogDTO, joinPoint);

            SysLogContextHolder.set(operateLogDTO);
        });
    }

    /**
     * 返回通知
     *
     * @param ret
     * @throws Throwable
     */
    @AfterReturning(returning = "ret", pointcut = "sysLogAspect()")
    public void doAfterReturning(Object ret) {
        tryCatch(true, (x) -> {
            //BaseResponse baseResponse = Convert.convert(BaseResponse.class, ret);
            SysLogEntity operateLogDTO = SysLogContextHolder.get();
            if (ret == null) {
                operateLogDTO.setOperateResult(SysLogResult.SUCCESS.name());
                operateLogDTO.setResult(JSON.toJSONString(ret));
            } else {
                operateLogDTO.setOperateResult(SysLogResult.SUCCESS.name());
                operateLogDTO.setResult(JSON.toJSONString(ret));
            }

            publishEvent(operateLogDTO);
        });
    }

    /**
     * 异常通知
     *
     * @param e
     */
    @AfterThrowing(pointcut = "sysLogAspect()", throwing = "e")
    public void doAfterThrowable(Throwable e) {
        tryCatch(true, (x) -> {
            SysLogEntity operateLogDTO = SysLogContextHolder.get();
            operateLogDTO.setOperateResult(SysLogResult.FAIL.name());

            // 异常信息
            operateLogDTO.setExDesc(e.getMessage());
            // 异常对象
            operateLogDTO.setExDetail(LogUtil.getStackTrace(e));

            publishEvent(operateLogDTO);
        });
    }
    
    private void annotation(SysLogEntity operateLogDTO, JoinPoint joinPoint) {
        // 方法注解信息
        Method method = LogUtil.getAnnotationMethod(joinPoint);
        operateLogDTO.setDescription(LogUtil.getDescription(method));

        String moduleCode = LogUtil.getModuleCode(method);
        String moduleName = LogUtil.getModuleName(method);
        if (StringUtils.hasLength(moduleCode) && StringUtils.hasLength(moduleName)) {
            operateLogDTO.setModuleCode(moduleCode);
            operateLogDTO.setModuleName(moduleName);
        } else {
            operateLogDTO.setModuleCode(PinyinUtil.getFirstLetter(this.moduleName,""));
            operateLogDTO.setModuleName(this.moduleName);
        }

        operateLogDTO.setBizType(LogUtil.getBizType(method));
        operateLogDTO.setBizTypeDescription(LogUtil.getBizTypeDescription(method));
        operateLogDTO.setOperateType(LogUtil.getOperateType(method));
        operateLogDTO.setOperateTypeDescription(LogUtil.getOperateTypeDescription(method));
    }

    private void userInfo(SysLogEntity operateLogDTO) {
        // 用户信息
    	if(!StrUtil.isBlankIfStr(SecurityUtils.getSubject().getPrincipal())) {
    		String token = SecurityUtils.getSubject().getPrincipal().toString();
    		String userId =  JwtUtil.getClaim(token, Constant.USER_ID);
    		String userLoginName =  JwtUtil.getClaim(token, Constant.LOGIN_NAME);
    		operateLogDTO.setUserId(userId);
    		operateLogDTO.setUserLoginName(userLoginName);
    	}
    }

    private void classMethodArgs(SysLogEntity operateLogDTO, JoinPoint joinPoint) {
        // 类名 + 方法 + 参数
        operateLogDTO.setClazz(joinPoint.getTarget().getClass().getName());
        operateLogDTO.setMethod(joinPoint.getSignature().getName());

        Object[] args = joinPoint.getArgs();

        String strArgs = "";
        HttpServletRequest request = HttpContextHolder.getHttpServletRequest();
        try {
            if (!request.getContentType().contains("multipart/form-data")) {
                strArgs = JsonMapper.nonDefaultMapper().toJson(args);
            }
        } catch (Exception e) {
            try {
                strArgs = Arrays.toString(args);
            } catch (Exception ex) {
                logger.warn("解析参数异常", ex);
            }
        }
        operateLogDTO.setParams(getText(strArgs));
    }

    private void httpInfo(SysLogEntity operateLogDTO, JoinPoint joinPoint) {
        HttpServletRequest request = HttpContextHolder.getHttpServletRequest();
        if (request != null) {
            operateLogDTO.setRequestIp(ServletUtil.getClientIP(request));
            operateLogDTO.setRequestUri(URLUtil.getPath(request.getRequestURI()));
            operateLogDTO.setHttpMethod(request.getMethod());
            operateLogDTO.setUa(StrUtil.sub(request.getHeader("user-agent"), 0, 500));
        }
    }


    private void tryCatch(boolean remove, Consumer<String> consumer) {
        try {
            consumer.accept("");
        } catch (Exception e) {
            logger.warn("记录操作日志异常", e);
        } finally {
            if (remove) {
                SysLogContextHolder.remove();
            }
        }
    }

    private void publishEvent(SysLogEntity operateLogDTO) {
        operateLogDTO.setEndTime(LocalDateTime.now());
        operateLogDTO.setExecuteTime(operateLogDTO.getStartTime().until(operateLogDTO.getEndTime(), ChronoUnit.MILLIS));
        applicationContext.publishEvent(new SysLogEvent(operateLogDTO));
    }

    /**
     * 截取指定长度的字符串
     *
     * @param val
     * @return
     */
    private String getText(String val) {
        return StrUtil.sub(val, 0, 65535);
    }

}
