package com.ykuee.datamaintenance.common.base.exception;

import com.ykuee.datamaintenance.common.base.constant.ExceptionCode;

public class BaseException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/*** 异常代码*/
	private String exceCode;
	/*** 异常描述*/
	private String exceMessage;

	public BaseException(String message) {
		super(message);
		this.exceCode=ExceptionCode.INTERNAL_SERVER_ERROR.getCode();
		this.exceMessage=message;
	}

	public BaseException(String exceCode, String exceMessage) {
		super(exceMessage);
		this.exceCode = exceCode;
		this.exceMessage = exceMessage;
	}
	
	public BaseException(String exceCode, String exceMessage,Throwable cause) {
		super(exceMessage, cause);
		this.exceCode = exceCode;
		this.exceMessage = exceMessage;
	}
	public BaseException(String exceCode,Throwable cause) {
		super(cause.getMessage(), cause);
		this.exceCode = exceCode;
		this.exceMessage = cause.getMessage();
	}

	public String getExceCode() {
		return exceCode;
	}

	public String getExceMessage() {
		return exceMessage;
	}
	

}
