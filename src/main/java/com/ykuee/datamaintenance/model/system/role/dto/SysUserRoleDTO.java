package com.ykuee.datamaintenance.model.system.role.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SysUserRoleDTO {

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
