package com.ykuee.datamaintenance.model.system.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ykuee.datamaintenance.common.base.model.entity.BaseEntity;

import lombok.Data;

@Data
@TableName("SYS_USER")
public class SysUserEntity extends BaseEntity<String>{

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
    
    private String disableFlag;
    
}
