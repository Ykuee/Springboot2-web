package com.ykuee.datamaintenance.model.system.role.converter;

import org.mapstruct.Mapper;

import com.ykuee.datamaintenance.common.base.beancopy.DtoEntityConverter;
import com.ykuee.datamaintenance.model.system.role.dto.SysRoleDTO;
import com.ykuee.datamaintenance.model.system.role.entity.SysRoleEntity;

@Mapper(componentModel = "spring")
public interface SysRoleConverter  extends DtoEntityConverter<SysRoleDTO, SysRoleEntity>{

}
