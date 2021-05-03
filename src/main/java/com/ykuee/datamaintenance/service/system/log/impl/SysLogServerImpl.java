package com.ykuee.datamaintenance.service.system.log.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ykuee.datamaintenance.common.base.constant.LogConstant;
import com.ykuee.datamaintenance.common.base.serviceImpl.ServiceImpl;
import com.ykuee.datamaintenance.common.log.SysLogListener;
import com.ykuee.datamaintenance.mapper.system.log.SysLogMapper;
import com.ykuee.datamaintenance.model.system.log.entity.SysLogEntity;
import com.ykuee.datamaintenance.service.system.log.SysLogService;

@Service
public class SysLogServerImpl extends ServiceImpl<SysLogMapper, SysLogEntity> implements SysLogService{
	
	private static Logger logger=LoggerFactory.getLogger(SysLogListener.class);
	
	@Override
	public void saveLog(SysLogEntity sysLog) {
		logger.debug("本次请求操作日志:{}",sysLog);
		if(LogConstant.LogOperateType.QUERY.equals(sysLog.getOperateType())) {
			logger.debug("本次请求操作为query，不进行入库操作，desc:{}",sysLog.getDescription());
			return;
		}
		save(sysLog);
	}

}
