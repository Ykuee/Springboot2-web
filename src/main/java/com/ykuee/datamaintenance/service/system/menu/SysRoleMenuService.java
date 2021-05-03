package com.ykuee.datamaintenance.service.system.menu;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykuee.datamaintenance.model.system.menu.dto.SysRoleMenuDTO;
import com.ykuee.datamaintenance.model.system.menu.entity.SysRoleMenuEntity;

public interface SysRoleMenuService extends IService<SysRoleMenuEntity> {

	boolean addRoleMenu(List<SysRoleMenuDTO> roleMenuDTO);

	boolean delRoleMenu(List<SysRoleMenuDTO> roleMenuDTO);

}
