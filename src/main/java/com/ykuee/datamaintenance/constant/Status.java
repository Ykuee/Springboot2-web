package com.ykuee.datamaintenance.constant;

import com.ykuee.datamaintenance.common.base.codeenum.BaseCodeEnum;

public enum Status implements BaseCodeEnum<String>{
	SAVE("10","待提交"),
	APPROVING("20","待审批"),
	APPROVED("100","审批通过"),//审批通过为生效状态
	REFUSE("30","审批拒绝");

    private String key;
    private String value;
    
    private Status(String key, String value){
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
