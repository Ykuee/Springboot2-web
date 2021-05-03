package com.ykuee.datamaintenance.model.system.user.dto;

import com.ykuee.datamaintenance.common.base.model.dto.BaseDTO;

import lombok.Data;

/**
 * @Description:用户entity
 * @Author: hgy
 * @Date: Created in 2018-03-29
 */
@Data
public class SysUserDTO extends BaseDTO<String>{

	private String searchCode;
    //用户id
    private String id;
    //用户编码
    private String code;
    //用户名称
    private String name;
    //登录名
    private String loginName;
    //密码
    private String password;
    //验证码
    private String captchaCode;
    
    private String disableFlag;
}
