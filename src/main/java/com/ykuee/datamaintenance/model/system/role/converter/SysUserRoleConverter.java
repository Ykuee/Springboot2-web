package com.ykuee.datamaintenance.model.system.role.converter;

import org.mapstruct.Mapper;

import com.ykuee.datamaintenance.common.base.beancopy.DtoEntityConverter;
import com.ykuee.datamaintenance.model.system.role.dto.SysUserRoleDTO;
import com.ykuee.datamaintenance.model.system.role.entity.SysUserRoleEntity;

@Mapper(componentModel = "spring")
public interface SysUserRoleConverter  extends DtoEntityConverter<SysUserRoleDTO, SysUserRoleEntity>{

}
