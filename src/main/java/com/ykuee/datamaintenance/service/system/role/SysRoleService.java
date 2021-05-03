package com.ykuee.datamaintenance.service.system.role;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykuee.datamaintenance.model.system.role.dto.SysRoleDTO;
import com.ykuee.datamaintenance.model.system.role.entity.SysRoleEntity;

public interface SysRoleService extends IService<SysRoleEntity> {

	List<SysRoleDTO> getSelectedRoleListByUserId(String userId);

	boolean addRole(SysRoleDTO sysRoleDTO);

	boolean updateRole(SysRoleDTO sysRoleDTO);

	boolean delRole(SysRoleDTO sysRoleDTO);

	List<SysRoleDTO> getRolesByUserId(String userId);

	int getCountByCode(SysRoleDTO sysRoleDTO);

}
