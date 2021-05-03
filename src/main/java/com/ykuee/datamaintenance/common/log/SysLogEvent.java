package com.ykuee.datamaintenance.common.log;


import org.springframework.context.ApplicationEvent;

import com.ykuee.datamaintenance.model.system.log.entity.SysLogEntity;


/**
 * 系统日志事件
 *
 */
public class SysLogEvent extends ApplicationEvent {

    public SysLogEvent(SysLogEntity source) {
        super(source);
    }
}
