package com.ykuee.datamaintenance.model.system.role.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ykuee.datamaintenance.common.base.model.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("SYS_USER_ROLE")
public class SysUserRoleEntity {

	/**
     * 主键
     */
    @TableId("ID")
    private String id;

    /**
     * 用户ID
     */
    @TableField("USER_ID")
    private String userId;

    /**
     * 角色ID
     */
    @TableField("ROLE_ID")
    private String roleId;
    
}
