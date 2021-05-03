package com.ykuee.datamaintenance.model.system.menu.entity;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ykuee.datamaintenance.common.base.model.entity.BaseEntity;
import com.ykuee.datamaintenance.common.base.node.INode;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色信息表
 * </p>
 *
 * @author heguangyue
 * @since 2020-08-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_role_menu")
public class SysRoleMenuEntity{

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
