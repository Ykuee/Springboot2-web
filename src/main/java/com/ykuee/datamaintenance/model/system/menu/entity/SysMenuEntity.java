package com.ykuee.datamaintenance.model.system.menu.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ykuee.datamaintenance.common.base.model.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 菜单信息表
 * </p>
 *
 * @author heguangyue
 * @since 2020-08-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("SYS_MENU")
public class SysMenuEntity extends BaseEntity<String>{

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;

    @TableField("TITLE")
    private String title;
    
    @TableField("NAME")
    private String name;

    /**
     * 路径
     */
    @TableField("PATH")
    private String path;
    /**
     * 前端路由
     */
    @TableField("COMPONENT")
    private String component;

    /**
     * 编码
     */
    @TableField("CODE")
    private String code;

    /**
     * 排序使用
     */
    @TableField("SORT")
    private Integer sort;


    /**
     * 菜单小图标
     */
    @TableField("ICON")
    private String icon;

    /**
     * 级别
     */
    @TableField("LEVEL_TYPE")
    private Integer levelType;

    /**
     * 父级ID(顶级为0)
     */
    @TableField("PARENT_ID")
    private String parentId;
    
    @TableField("DISABLE_FLAG")
    private String disableFlag;

}
