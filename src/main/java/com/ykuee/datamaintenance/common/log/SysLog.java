package com.ykuee.datamaintenance.common.log;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    /**
     * 描述
     *
     * @return
     */
    String description() default "";

    /**
     * 模块编码
     *
     * @return
     */
    String moduleCode() default "";

    /**
     * 模块名称
     *
     * @return
     */
    String moduleName() default "";

    /**
     * 操作类型
     *
     * @return
     */
    String operateType();

    /**
     * 操作类型描述
     *
     * @return
     */
    String operateTypeDescription();

    /**
     * 业务类型
     *
     * @return
     */
    String bizType()  default "";

    /**
     * 业务类型描述
     *
     * @return
     */
    String bizTypeDescription()  default "";

}
