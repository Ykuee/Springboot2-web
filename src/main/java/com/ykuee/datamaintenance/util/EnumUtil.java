package com.ykuee.datamaintenance.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ykuee.datamaintenance.common.base.exception.BusinessException;
import com.ykuee.datamaintenance.common.base.select.BaseSelectDTO;

public class EnumUtil {

    private static Logger logger = LoggerFactory.getLogger(EnumUtil.class);

    public static List<BaseSelectDTO> getEnumSelect (Class clz) {
        try {
            logger.info("常量类：" + clz.getName());
            List<BaseSelectDTO> result = new ArrayList<BaseSelectDTO>();
            Method name = clz.getMethod("name");
            Method getValue = clz.getMethod("getValue");
            for(Object o : clz.getEnumConstants()) {
            	BaseSelectDTO option = new BaseSelectDTO(String.valueOf(name.invoke(o)), String.valueOf(getValue.invoke(o)));
                result.add(option);
            }
            return result;
        } catch (Exception e) {
            logger.error("组装下拉框异常："+e.getMessage(),e);
            throw new RuntimeException("组装下拉框异常："+e.getMessage());
        }
    }
    /**
     * 获取枚举下拉框（包含空）
     * @param clz
     * @return
     */
    public static List<BaseSelectDTO> getEnumSelectContainsEmpty (Class clz) {
        try {
            logger.info("常量类：" + clz.getName());
            List<BaseSelectDTO> result = new ArrayList<BaseSelectDTO>();
            //添加空元素
            BaseSelectDTO emptyOption = new BaseSelectDTO("-1", "");
            result.add(emptyOption);
            //获取枚举类字段名，循环处理
            Method name = clz.getMethod("name");
            Method getValue = clz.getMethod("getValue");
            for(Object o : clz.getEnumConstants()) {
            	BaseSelectDTO option = new BaseSelectDTO(String.valueOf(name.invoke(o)), String.valueOf(getValue.invoke(o)));
                result.add(option);
            }
            return result;
        } catch (Exception e) {
            logger.error("组装下拉框异常："+e.getMessage(),e);
            throw new RuntimeException("组装下拉框异常："+e.getMessage());
        }
    }
    
	@SuppressWarnings("unchecked")
    public static <E extends Enum<?>> E valueOf(Class enumClass, Object value, Method method) {
		E[] es = (E[]) enumClass.getEnumConstants();
        for (E e : es) {
            Object evalue;
            try {
                method.setAccessible(true);
                evalue = method.invoke(e);
            } catch (IllegalAccessException | InvocationTargetException e1) {
                throw new BusinessException("反射枚举类找不到方法 method:"+method);
            }
            if (value instanceof Number && evalue instanceof Number
                    && new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(String.valueOf(evalue))) == 0) {
                return e;
            }
            if (Objects.equals(evalue, value)) {
                return e;
            }
        }
        return null;
    }
    
    public static <E extends Enum<E>> E getEnumByValue(final Class enumClass,Object value) {
        try {
            return valueOf(enumClass,value,enumClass.getMethod("getValue"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
