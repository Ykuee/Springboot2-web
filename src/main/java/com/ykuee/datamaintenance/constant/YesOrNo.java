package com.ykuee.datamaintenance.constant;

import com.ykuee.datamaintenance.common.base.codeenum.BaseCodeEnum;

public enum YesOrNo implements BaseCodeEnum<String>{
    NO("0","否"),
    YES("1","是");

    private String key;
    private String value;
    
    private YesOrNo(String key, String value){
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
