package com.ykuee.datamaintenance.common.page.disablewrapper;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DisableWrapper {
}
