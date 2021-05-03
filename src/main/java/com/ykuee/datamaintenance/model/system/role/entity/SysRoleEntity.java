package com.ykuee.datamaintenance.model.system.role.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ykuee.datamaintenance.common.base.model.entity.BaseEntity;

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
@TableName("SYS_ROLE")
public class SysRoleEntity extends BaseEntity<String>{


    @TableId("ID")
    private String id;

    @TableField("NAME")
    private String name;

    @TableField("CODE")
    private String code;

    @TableField("REMARK")
    private String remark;
    
    private String disableFlag;
    

}
