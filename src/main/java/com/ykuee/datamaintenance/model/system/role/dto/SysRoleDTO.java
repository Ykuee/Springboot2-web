package com.ykuee.datamaintenance.model.system.role.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ykuee.datamaintenance.common.base.model.dto.BaseDTO;
import com.ykuee.datamaintenance.model.system.role.entity.SysRoleEntity;

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
public class SysRoleDTO extends BaseDTO<String>{

    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private String code;

    private String remark;

    private String delFlag;

    private String selected;

    private String disableFlag;
    
}
