package com.ykuee.datamaintenance.common.log;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.ykuee.datamaintenance.model.system.log.entity.SysLogEntity;
import com.ykuee.datamaintenance.service.system.log.SysLogService;


/**
 * 异步监听日志事件
 */
public class SysLogListener {
	
	private static Logger logger=LoggerFactory.getLogger(SysLogListener.class);
	
	@Autowired
    private SysLogService logService;

    public SysLogListener(SysLogService logService) {
		super();
		this.logService = logService;
	}
	public SysLogListener() {
	}
    
    @Async
    @Order
    @EventListener(SysLogEvent.class)
    public void saveSysLog(SysLogEvent event) {
    	logger.debug("日志监听消费响应");
        SysLogEntity sysLog = (SysLogEntity) event.getSource();
        logService.saveLog(sysLog);
    }

}
