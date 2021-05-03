package com.ykuee.datamaintenance.common.base.exception;

import com.ykuee.datamaintenance.common.base.constant.ExceptionCode;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description: 自定义业务异常
 * @Author: heguangyue
 * @Date: Created in 2019/12/11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BusinessException extends RuntimeException{
  /**
   * 结果码
   */
  private String code;
  /**
   * 结果码描述
   */
  private String message;
  /**
   * 结果码枚举
   */
  private ExceptionCode resultCode;

  public BusinessException() {
    super(ExceptionCode.INTERNAL_SERVER_ERROR.getDescription());
    this.code=ExceptionCode.INTERNAL_SERVER_ERROR.getCode();
    this.message = ExceptionCode.INTERNAL_SERVER_ERROR.getDescription();
  }
  
  public BusinessException(String message) {
	  super(message);
	  this.code=ExceptionCode.INTERNAL_SERVER_ERROR.getCode();
	  this.message = message;
  }
  
  public BusinessException(String code, String message) {
    super(message);
    this.code=code;
    this.message = message;
  }
  
  public BusinessException(ExceptionCode resultCode) {
    super(resultCode.getDescription());
    this.code = resultCode.getCode();
    this.message = resultCode.getDescription();
    this.resultCode = resultCode;
  }

}
