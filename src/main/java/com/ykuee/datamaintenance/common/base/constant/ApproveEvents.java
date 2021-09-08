package com.ykuee.datamaintenance.common.base.constant;

import com.ykuee.datamaintenance.common.base.codeenum.BaseCodeEnum;

public enum ApproveEvents implements BaseCodeEnum<String> {
	SUBMIT("SUBMIT","提交审批"),
	AGREE("AGREE","审批通过"),
	REJECT("REJECT","审批拒绝");//审批通过为生效状态

    private String key;
    private String value;

    private ApproveEvents(String key, String value){
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
