package com.ykuee.datamaintenance.common.excel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
  * @version:
  * @Description: Excel标题头
  * @author: Ykuee
  * @date: 2021-3-10 11:17:48
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExcelHeader {
    String value() default "";
}
