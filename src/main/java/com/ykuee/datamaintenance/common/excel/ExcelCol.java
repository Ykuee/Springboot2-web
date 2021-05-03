package com.ykuee.datamaintenance.common.excel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
  * @version:
  * @Description: excel导出列 字段尽量为String类型、Integer类型
  * @author: Ykuee
  * @date: 2021-3-10 11:17:26
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelCol {
    String value() default "";
    
    int order() default -1;
    
    boolean tureOrFalse() default false;
}
