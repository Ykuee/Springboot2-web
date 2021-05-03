package com.ykuee.datamaintenance.model.system.user.converter;

import org.mapstruct.Mapper;

import com.ykuee.datamaintenance.common.base.beancopy.DtoEntityConverter;
import com.ykuee.datamaintenance.model.system.user.dto.SysUserDTO;
import com.ykuee.datamaintenance.model.system.user.entity.SysUserEntity;

@Mapper(componentModel = "spring")
public interface SysUserConverter extends DtoEntityConverter<SysUserDTO, SysUserEntity>{
	
}
