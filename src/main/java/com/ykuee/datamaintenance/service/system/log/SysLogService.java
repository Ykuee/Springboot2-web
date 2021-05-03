package com.ykuee.datamaintenance.service.system.log;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykuee.datamaintenance.model.system.log.entity.SysLogEntity;

public interface SysLogService extends IService<SysLogEntity> {

	void saveLog(SysLogEntity sysLog);


}
