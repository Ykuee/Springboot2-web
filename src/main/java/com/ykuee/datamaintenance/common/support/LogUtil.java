package com.ykuee.datamaintenance.common.support;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;

import com.ykuee.datamaintenance.common.log.entity.SysLog;

public class LogUtil {

    public static String getDescription(Method method) {
        return method == null ? "" : method.getAnnotation(SysLog.class).description();
    }

    public static String getModuleCode(Method method) {
        return method == null ? "" : method.getAnnotation(SysLog.class).moduleCode();
    }

    public static String getModuleName(Method method) {
        return method == null ? "" : method.getAnnotation(SysLog.class).moduleName();
    }

    public static String getOperateType(Method method) {
        return method == null ? null : method.getAnnotation(SysLog.class).operateType();
    }

    public static String getOperateTypeDescription(Method method) {
        return method == null ? "" : method.getAnnotation(SysLog.class).operateTypeDescription();
    }

    public static String getBizType(Method method) {
        return method == null ? "" : method.getAnnotation(SysLog.class).bizType();
    }

    public static String getBizTypeDescription(Method method) {
        return method == null ? "" : method.getAnnotation(SysLog.class).bizTypeDescription();
    }

    public static String getClass(JoinPoint point) {
        return point.getTarget().getClass().getName();
    }

    public static String getMethod(JoinPoint point) {
        return point.getSignature().getName();
    }

    public static Method getAnnotationMethod(JoinPoint point) {
        try {
            // 获取连接点目标类名
            String targetName = point.getTarget().getClass().getName();
            // 获取连接点签名的方法名
            String methodName = point.getSignature().getName();
            // 获取连接点参数
            Object[] args = point.getArgs();
            // 根据连接点类的名字获取指定类
            Class targetClass = Class.forName(targetName);
            // 获取类里面的方法
            Method[] methods = targetClass.getMethods();

            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class[] clazzs = method.getParameterTypes();
                    if (clazzs.length == args.length) {
                        return method;
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * 获取堆栈信息
     *
     * @param throwable
     * @return
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }
}
