package com.ykuee.datamaintenance.model.system.menu.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ykuee.datamaintenance.common.base.node.INode;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SysRoleMenuDTO {
	
    /**
     * 主键
     */
    private String id;

    /**
     * 菜单ID
     */
    private String menuId;

    /**
     * 角色ID
     */
    private String roleId;
}
