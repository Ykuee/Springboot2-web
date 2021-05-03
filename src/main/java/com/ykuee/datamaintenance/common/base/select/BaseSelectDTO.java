package com.ykuee.datamaintenance.common.base.select;

import lombok.Data;

@Data
public class BaseSelectDTO {

	public BaseSelectDTO() {
		super();
	}
	
	public BaseSelectDTO(String selKey, String selValue) {
		super();
		this.selKey = selKey;
		this.selValue = selValue;
	}
	/**
	 * 下拉框中的key
	 */
	private Object selKey;
	/**
	 * 下拉框中的value
	 */
	private String selValue;

}
