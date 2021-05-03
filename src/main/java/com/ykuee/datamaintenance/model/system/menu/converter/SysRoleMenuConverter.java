package com.ykuee.datamaintenance.model.system.menu.converter;

import org.mapstruct.Mapper;

import com.ykuee.datamaintenance.common.base.beancopy.DtoEntityConverter;
import com.ykuee.datamaintenance.model.system.menu.dto.SysRoleMenuDTO;
import com.ykuee.datamaintenance.model.system.menu.entity.SysRoleMenuEntity;

@Mapper(componentModel = "spring")
public interface SysRoleMenuConverter extends DtoEntityConverter<SysRoleMenuDTO, SysRoleMenuEntity>{

}
