package com.ykuee.datamaintenance.common.base.constant;


import com.ykuee.datamaintenance.common.base.codeenum.BaseCodeEnum;

public enum ApproveStatus implements BaseCodeEnum<String> {
	SAVE("10","待提交"),
	APPROVING("20","审批中"),
	APPROVED("100","审批通过"),//审批通过为生效状态
	REJECT("30","审批拒绝");

    private String key;
    private String value;

    private ApproveStatus(String key, String value){
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
