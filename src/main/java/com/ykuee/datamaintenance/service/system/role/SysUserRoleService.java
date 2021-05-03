package com.ykuee.datamaintenance.service.system.role;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykuee.datamaintenance.model.system.role.dto.SysUserRoleDTO;
import com.ykuee.datamaintenance.model.system.role.entity.SysUserRoleEntity;

public interface SysUserRoleService extends IService<SysUserRoleEntity>{

	boolean addUserRole(List<SysUserRoleDTO> sysUserRoleList);

	boolean delUserRole(List<SysUserRoleDTO> sysUserRoleList);


}
