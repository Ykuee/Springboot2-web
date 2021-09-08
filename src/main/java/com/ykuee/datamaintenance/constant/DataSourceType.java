package com.ykuee.datamaintenance.constant;

import com.ykuee.datamaintenance.common.base.codeenum.BaseCodeEnum;

public enum DataSourceType implements BaseCodeEnum<String>{
    DATA_1("DATA_1","表头1"),
    DATA_2("DATA_2","表头2"),
    DATA_3("DATA_3","表头3");

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
