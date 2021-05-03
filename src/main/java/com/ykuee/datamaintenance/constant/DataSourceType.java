package com.ykuee.datamaintenance.constant;

import com.ykuee.datamaintenance.common.base.codeenum.BaseCodeEnum;

public enum DataSourceType implements BaseCodeEnum<String>{
    NUDE("NUDE","裸数据库"),
    PROC("PROC","加工库");

    private String key;
    private String value;
    
    private DataSourceType(String key, String value){
        this.key = key;
        this.value = value;
    }
    
	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public String getValue() {
		return this.value;
	}

}
