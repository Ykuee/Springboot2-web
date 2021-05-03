package com.ykuee.datamaintenance.model.system.menu.converter;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.ykuee.datamaintenance.common.base.beancopy.DtoEntityConverter;
import com.ykuee.datamaintenance.model.system.menu.dto.SysMenuDTO;
import com.ykuee.datamaintenance.model.system.menu.entity.SysMenuEntity;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysMenuConverter extends DtoEntityConverter<SysMenuDTO, SysMenuEntity>{

}
