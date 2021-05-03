package com.ykuee.datamaintenance.common.log;

import com.ykuee.datamaintenance.model.system.log.entity.SysLogEntity;

public class SysLogContextHolder {

    public static final ThreadLocal<SysLogEntity> THREAD_LOCAL = new InheritableThreadLocal<>();

    public static SysLogEntity get() {
        SysLogEntity operateLogDTO = THREAD_LOCAL.get();
        if (operateLogDTO == null) {
            return new SysLogEntity();
        }
        return operateLogDTO;
    }

    public static void set(SysLogEntity operateLogDTO) {
        THREAD_LOCAL.set(operateLogDTO);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

}
